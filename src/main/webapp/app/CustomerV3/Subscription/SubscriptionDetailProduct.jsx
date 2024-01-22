import React, {useEffect, useState} from 'react';
import TailwindModal from './TailwindModal';
import {InformationCircleIcon} from '@heroicons/react/24/outline';
import AddCustomAttributes from './AddCustomAttributes';
import axios from 'axios';
import EditCustomerContractDetail from './EditCustomerContractDetail';
import PopupMessaging from './PopupMessaging';
import {checkIfPreventCancellationBeforeDays, formatPrice} from 'app/shared/util/customer-utils';
import LoyaltyDetailsLineItem from './LoyaltyDetailsLineItem';
import SwapProductCustomer from "app/CustomerV3/Subscription/AddProductModelStep/SwapProductCustomer";
import {useSelector} from "react-redux";
import getSymbolFromCurrency from "currency-symbol-map";
import { isFreeProduct, isOneTimeProduct } from 'app/shared/util/subscription-utils';

export default function SubscriptionDetailProduct({
                                                    prd,
                                                    customerPortalSettingEntity,
                                                    attributeEdit,
                                                    setAttributeEdit,
                                                    subscriptionContractFreezeStatus,
                                                    contractId,
                                                    shopName,
                                                    getCustomerPortalEntity,
                                                    index,
                                                    subscriptionEntities,
                                                    subUpcomingOrderEntities,
                                                    sellingPlan,
                                                    sellingPlanData,
                                                    recurringProductsInSubscription,
                                                    babDetails,
                                                    freeProductsInSubscription,
                                                    oneTimeProductsInSubscription
                                                  }) {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [isViewDetailsModalOpen, setIsViewDetailsModalOpen] = useState(false);
  const [isViewDiscountDetailsOpen, setIsViewDiscountDetailsOpen] = useState(false);
  const [isDeleteProductModalOpen, setIsDeleteProductModalOpen] = useState(false);
  const [deleteInProgress, setDeleteInProgress] = useState(false);

  const [showDeleteProductMessaging, setShowDeleteProductMessaging] = useState(false);
  const [deleteProductSuccess, setDeleteProductSuccess] = useState(false);
  const [deleteProductError, setDeleteProductError] = useState(false);
  let [deleteDiscountFirst, setDeleteDiscountFirst] = useState(false);
  const [loyaltyTableData, setLoyaltyTableData] = useState(null);
  const [isSwapProductModalOpen, setIsSwapProductModalOpen] = useState(false);
  const subscriptionEntitiesFull = useSelector(state => state.subscription.entity);
  const [productData, setProductData] = useState({})

  const [attributes, setAttributes] = useState([])

  useEffect(() => {
    setAttributes([...prd?.node?.customAttributes])
    if (prd?.node?.productId) {
      axios.get(`/api/data/product-handles?productIds=${prd?.node?.productId}`)
      .then(res => {
        let prdHandle = res.data[prd?.node?.productId?.split('/').pop()]
        if (prdHandle) {
          fetch(`${`https://${customerPortalSettingEntity?.shop}` || `${location?.origin}`}/products/${prdHandle}.js`)
          .then(res => {
            if (res.ok) {
              return res.json()
            } else {
              return {}
            }
          })
          .then(res => setProductData(res))
        }
      })
    }
  }, [prd])
  useEffect(() => {
    if (sellingPlan && sellingPlanData) {
      createLoyaltyTableData(sellingPlan, sellingPlanData);
    }
  }, [sellingPlan, sellingPlanData]);

  const checkDisabled = () => {
    let isAttributeAvl = attributes.filter(item => item?.key === "_appstle-bb-id");
    let isDeleteAllowed = true;
    let subscriptionSubTotal = 0; 
    let subscriptionTotalQuantity = 0;
    subscriptionEntities?.lines?.edges?.forEach((product) => {
      if (!isOneTimeProduct(product) && !isFreeProduct(product)) {
        subscriptionSubTotal = subscriptionSubTotal + parseFloat(product?.node?.lineDiscountedPrice?.amount);
        subscriptionTotalQuantity = subscriptionTotalQuantity + product?.node?.quantity;
      }
    })

    if (babDetails?.bundle?.minOrderAmount) {
      if ((parseFloat(subscriptionSubTotal).toFixed(2) - parseFloat(prd?.node?.lineDiscountedPrice?.amount).toFixed(2)) < parseFloat(babDetails?.bundle?.minOrderAmount).toFixed(2)) {
        isDeleteAllowed = false;
      }
    }

    if (babDetails?.bundle?.minProductCount) {
      if ((subscriptionTotalQuantity - prd?.node?.quantity) < babDetails?.bundle?.minProductCount) {
        isDeleteAllowed = false;
      }
    }
    if (isAttributeAvl.length && (customerPortalSettingEntity?.allowDeleteForBuildABoxProductV2 === 'false')) {
      return false
    } else {
      return isDeleteAllowed
    }
  }

  const deleteProduct = async removeDiscount => {
    setDeleteInProgress(true);
    const requestUrl = `api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${prd?.node?.id}&isExternal=true&removeDiscount=${removeDiscount != null ? removeDiscount : false}`;
    return axios
      .put(requestUrl)
      .then(res => {
        getCustomerPortalEntity(contractId);
        setDeleteInProgress(false);
        return res;
      })
      .catch(err => {
        console.log('Error in deleting product');
        setDeleteInProgress(false);
        setIsDeleteProductModalOpen(false);
        setDeleteDiscountFirst(true);
        return err;
      });
  };

  const deleteProductHandler = async removeDiscount => {
    let results = await deleteProduct(removeDiscount);
    if (results) {
      setShowDeleteProductMessaging(true);
      if (results?.status === 200) {
        setDeleteProductSuccess(true);
        setDeleteProductError(false);
        setDeleteDiscountFirst(false);
      } else {
        setDeleteProductSuccess(false);
        setDeleteProductError(true);
      }
    }
  };

  const resetDeleteProductModal = () => {
    setIsDeleteProductModalOpen(false);
    setShowDeleteProductMessaging(false);
    setDeleteProductSuccess(false);
    setDeleteProductError(false);
    setDeleteDiscountFirst(false);
  };

  const createLoyaltyTableData = (sellingPlan, sellingPlanData) => {
    var tableData = [];
    var sellingPlanData = sellingPlanData?.find(function (item) {
      return item?.id === sellingPlan;
    });
    var output = null;
    if (sellingPlanData?.freeTrialEnabled) {
      tableData.push(
        <LoyaltyDetailsLineItem
          billingCycle={0}
          discount={sellingPlanData.freeTrialCount}
          discountType={sellingPlanData.freeTrialInterval}
          freeTrail={sellingPlanData?.freeTrialEnabled}
          customerPortalSettingEntity={customerPortalSettingEntity}
        />
      );
    }
    if (sellingPlanData?.afterCycle2) {
      tableData.push(
        <LoyaltyDetailsLineItem
          billingCycle={sellingPlanData.afterCycle2}
          discount={sellingPlanData.discountOffer2}
          discountType={sellingPlanData.discountType2}
          freeTrail={false}
          customerPortalSettingEntity={customerPortalSettingEntity}
        />
      );
    }
    if (sellingPlanData?.appstleCycles?.length) {
      sellingPlanData?.appstleCycles?.forEach(function (cycle) {
        tableData.push(
          <LoyaltyDetailsLineItem
            billingCycle={cycle.afterCycle}
            discount={cycle.value}
            discountType={cycle.discountType}
            freeVariantId={cycle?.freeVariantId}
            freeTrail={false}
            customerPortalSettingEntity={customerPortalSettingEntity}
          />
        );
      });
    }
    setLoyaltyTableData(tableData);
  };

  function getPerkText(billingCycle, discount, discountType, freeTrail) {
    if (freeTrail) {
      return `Get <span class="appstle-loyalty-free-trial-discount">${discount} <span class="appstle-loyalty-free-trial-discount-count" style="text-transform: lowercase;">${discountType}${
        discount > 1 ? 's' : ''
      }</span></span> <span class="appstle-loyalty-free-trial-text">free trail.</span>`;
    } else {
      if (discountType === 'PERCENTAGE') {
        return `After <span class="appstle-loyalty-billing-cycle"><span class="appstle-loyalty-billing-cycle-count">${billingCycle}</span> billing cycle</span>, <span class="appstle-loyalty-discount">get <span class="appstle-loyalty-discount-amount">${discount +
        '% off'}</span></span>.`;
      } else if (discountType === 'SHIPPING') {
        return `After <span class="appstle-loyalty-billing-cycle"><span class="appstle-loyalty-billing-cycle-count">${billingCycle}</span> billing cycle</span>, <span class="appstle-loyalty-discount">get <span class="appstle-loyalty-discount-amount">${`shipping at ${formatPrice(
          discount * 100
        )}`}</span></span>.`;
      }
    }
  }

  function getBillingCycleText(billingCycle) {
    var j = billingCycle % 10,
      k = billingCycle % 100;
    if (j == 1 && k != 11) {
      return billingCycle + `<sup>st</sup>`;
    }
    if (j == 2 && k != 12) {
      return billingCycle + `<sup>nd</sup>`;
    }
    if (j == 3 && k != 13) {
      return billingCycle + `<sup>rd</sup>`;
    }
    return billingCycle + `<sup>th</sup>`;
  }

  const switchPopup = () => {
    setIsSwapProductModalOpen(true);
  };
  return (
    <tr class="as-bg-white as-border-b hover:as-bg-gray-50 as-product-line">
      <th scope="row" class="as-px-6 as-py-4 as-font-medium as-text-gray-900 as-product-line-info">
        <div className="as-grid as-grid-cols-12 as-gap-4 as-items-center as-py-2">
          <div className="as-col-span-2 as-product-image-wrapper">
            {(customerPortalSettingEntity?.enableRedirectToProductPage && productData?.handle) ? 
              <a className='as-product-title-tag-a' href={`https://${shopName}/products/${productData?.handle}`} target='_blank'> 
                <img
                  src={
                    prd?.node?.productId == null
                      ? 'https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660'
                      : prd?.node?.variantImage?.transformedSrc
                  }
                  alt=""
                  className="as-w-full as-h-70 as-product-image"
                />
              </a> :
              <img
                src={
                  prd?.node?.productId == null
                    ? 'https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660'
                    : prd?.node?.variantImage?.transformedSrc
                }
                alt=""
                className="as-w-full as-h-70 as-product-image"
              />
            }
          </div>
          <div className="as-col-span-10 as-product-line-details">
            {prd?.node?.title && (
              <p
                className={`as-text-sm as-text-gray-800 as-mb-2 as-product-title ${prd?.node?.productId == null ? 'as-line-through' : ''}`}>
                {(customerPortalSettingEntity?.enableRedirectToProductPage && productData?.handle) ? 
                  <a className='as-product-title-tag-a' href={`https://${shopName}/products/${productData?.handle}`} target='_blank'> {prd?.node?.title} {prd?.node?.productId == null ? '*' : ''} </a> :
                  <>{prd?.node?.title} {prd?.node?.productId == null ? '*' : ''}</>
                } 
              </p>
            )}
            {prd?.node?.variantTitle && prd?.node?.variantTitle !== '-' && prd?.node?.variantTitle !== 'Default Title' && (
              <p className="as-text-xs as-text-gray-500 as-variant">
                <span
                  className="as-font-medium as-text-gray-600 as-label">{customerPortalSettingEntity?.variantLblText || 'Variant'}<span className='colon-symbol'>:</span></span>{' '}
                <span className="as-value">{prd?.node?.variantTitle}</span>
              </p>
            )}
            {
              <p className={`as-text-xs as-text-gray-500 md:as-hidden as-amount`}>
                <span
                  className="as-font-medium as-text-gray-600 as-label">{customerPortalSettingEntity?.amountLblV2 || `Amount`}<span className='colon-symbol'>:</span></span>&nbsp;
                <span className="as-value">
                {formatPrice(parseFloat(prd?.node?.lineDiscountedPrice?.amount / Number(prd?.node?.quantity))?.toFixed(2)*100)}{' '}
                  {((prd.node?.pricingPolicy &&
                    prd.node.pricingPolicy?.cycleDiscounts?.length == 2 &&
                    prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.percentage !=
                    prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.percentage) || (loyaltyTableData?.length)) ? (
                    <InformationCircleIcon
                      onClick={() => setIsViewDiscountDetailsOpen(true)}
                      className="as-inline as-h-4 as-w-4 as-ml-1 as-stroke-indigo-500 as-cursor-pointer"
                    />
                  ) : ""}
                  </span>
              </p>
            }
            {prd?.node?.quantity && (
              <p className={`as-text-xs as-text-gray-500 md:as-hidden as-quantity`}>
                <span
                  className="as-font-medium as-text-gray-600 as-label">{customerPortalSettingEntity?.quantityLbl || 'Quantity'}<span className='colon-symbol'>:</span></span>&nbsp;
                <span className="as-value">{prd?.node?.quantity}</span>
              </p>
            )}
            {
              <p className={`as-text-xs as-text-gray-500 md:as-hidden as-total`}>
                <span
                  className="as-font-medium as-text-gray-600 as-label">{customerPortalSettingEntity?.totalLblText || 'Total'}<span className='colon-symbol'>:</span></span>&nbsp;
                <span
                  className="as-value">{formatPrice(parseFloat(prd?.node?.lineDiscountedPrice?.amount).toFixed(2)*100)}</span>
              </p>
            }
            {/* {
                prd.node?.sellingPlanName &&
                <p className="as-text-xs as-text-gray-500 as-selling-plan">
                <span className='as-font-medium as-text-gray-600 as-label'>{customerPortalSettingEntity?.sellingPlanNameText || "Selling Plan Name"}:</span>&nbsp;
                <span className="as-ml-1 as-value">
                    {prd.node?.sellingPlanName}
                </span>
            </p>
            } */}
            {isOneTimeProduct(prd) && (
              <p className="as-text-xs as-text-gray-500 as-onetime-text">{customerPortalSettingEntity?.oneTimePurchaseOnlyText || "Added as one time purchase only"}</p>
              )
            }

            {(subscriptionEntities?.status == 'ACTIVE' || subscriptionEntities?.status == 'PAUSED') &&
              <p className="as-mt-4 as-product-action">
                {!customerPortalSettingEntity?.variantIdsToFreezeEditRemove?.includes(prd?.node?.variantId?.split('/')?.pop()) && (
                  <>
                    {productData?.available && <span
                      onClick={() => setIsEditModalOpen(true)}
                      class="as-cursor-pointer as-font-medium as-text-indigo-600 hover:as-underline as-cta as-edit-product-cta"
                    >
                  {customerPortalSettingEntity?.editCommonText || 'Edit'}
                </span>}
                    {customerPortalSettingEntity?.enableSwapProductFeature &&
                    !checkIfPreventCancellationBeforeDays(customerPortalSettingEntity?.preventCancellationBeforeDays, subscriptionEntitiesFull?.nextBillingDate) ? (
                      <span
                        onClick={() => switchPopup()}
                        className="as-cursor-pointer as-font-medium as-text-indigo-600 hover:as-underline as-ml-2 as-cta as-swap-edit-product-cta"
                      >
                  {customerPortalSettingEntity?.swapProductBtnTextV2 || 'Swap'}
                </span>) : ''}
                  </>
                )}
                {customerPortalSettingEntity?.enableViewAttributes && (
                  <span
                    onClick={() => setIsViewDetailsModalOpen(true)}
                    class="as-cursor-pointer as-font-medium as-text-yellow-600 hover:as-underline as-ml-2 as-cta as-view-more-cta"
                  >
                  {customerPortalSettingEntity?.viewMoreText || 'View more'}
                </span>
                )}
              </p>}
          </div>
        </div>
      </th>
      <td class="as-px-6 as-py-4 as-hidden md:as-table-cell as-product-line-amount">
        <div className="as-flex as-items-center">
          <span
            className="as-value as-amount-value">{formatPrice(parseFloat(prd?.node?.lineDiscountedPrice?.amount / Number(prd?.node?.quantity))?.toFixed(2)*100)}</span>{' '}
          {prd.node?.pricingPolicy &&
            prd.node.pricingPolicy?.cycleDiscounts?.length == 2 &&
            prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.percentage !=
            prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.percentage && (
              <InformationCircleIcon
                onClick={() => setIsViewDiscountDetailsOpen(true)}
                className="as-inline as-h-4 as-w-4 as-ml-1 as-stroke-indigo-500 as-cursor-pointer"
              />
            )}
        </div>
      </td>
      <td className="as-px-6 as-py-4 as-hidden md:as-table-cell as-product-line-quantity">
        <span className="as-value as-quantity-value">{prd?.node?.quantity}</span>
      </td>
      <td class="as-px-6 as-py-4 as-hidden md:as-table-cell as-product-line-total">
        <span
          className="as-value as-total-value">{formatPrice(parseFloat(prd?.node?.lineDiscountedPrice?.amount).toFixed(2)*100)}</span>
      </td>
      {(customerPortalSettingEntity?.deleteProductFlag && (isOneTimeProduct(prd) || (recurringProductsInSubscription?.length > 1)) && checkDisabled()) ? (
        <td class="as-px-6 as-py-4 as-product-line-action">
          {!customerPortalSettingEntity?.variantIdsToFreezeEditRemove?.includes(prd?.node?.variantId?.split('/')?.pop()) && (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="as-h-6 as-w-6 as-stroke-red-500 as-cursor-pointer as-delete-icon"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="2"
              onClick={() => setIsDeleteProductModalOpen(true)}
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
              />
            </svg>
          )}
        </td>
      ) : ""}
      {((prd.node?.pricingPolicy &&
        prd.node.pricingPolicy?.cycleDiscounts?.length == 2 &&
        prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.percentage !=
        prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.percentage) || (loyaltyTableData?.length)) ? (
        <TailwindModal
          prd={prd}
          customerPortalSettingEntity={customerPortalSettingEntity}
          open={isViewDiscountDetailsOpen}
          setOpen={setIsViewDiscountDetailsOpen}
          modalTitle={customerPortalSettingEntity?.discountDetailsTitleTextV2 || 'Discount Details'}
          className="as-model-discount-details"
        >
          <div className="as-text-sm as-text-gray-500 as-model-discount-details">
            <ul className="as-list-disc as-pl-4">
              <li
                dangerouslySetInnerHTML={{
                  __html: customerPortalSettingEntity?.initialDiscountNoteDescription
                    ?.split('{{initialProductPrice}}')
                    ?.join(formatPrice(parseFloat(prd.node?.pricingPolicy?.cycleDiscounts[0]?.computedPrice?.amount)?.toFixed(2)*100))
                    ?.split('{{initialDiscount}}')
                    ?.join(
                      prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.amount
                        ? formatPrice(parseFloat(prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.amount)?.toFixed(2)*100)
                        : prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.percentage + '%'
                    )
                }}
              />

              <li
                dangerouslySetInnerHTML={{
                  __html: customerPortalSettingEntity?.afterCycleDiscountNoteDescription
                    ?.split('{{numberOfOrderCycle}}')
                    ?.join(prd.node?.pricingPolicy?.cycleDiscounts[1]?.afterCycle)
                    ?.split('{{afterCycleProductPrice}}')
                    ?.join(formatPrice(parseFloat(prd.node?.pricingPolicy?.cycleDiscounts[1]?.computedPrice?.amount)?.toFixed(2)*100))
                    ?.split('{{afterCycleDiscount}}')
                    ?.join(
                      prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.amount
                        ? formatPrice(parseFloat(prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.amount)?.toFixed(2)*100)
                        : prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.percentage + '%'
                    )
                }}
              />
            </ul>

            {loyaltyTableData?.length && (
              <div className="as-relative as-overflow-x-auto as-shadow-md sm:as-rounded-lg as-mt-8 as-model-loyalty">
                <table class="as-w-full as-text-sm as-text-left as-text-gray-500">
                  <thead class="as-text-xs as-text-gray-700 as-uppercase as-bg-gray-50">
                  <tr>
                    <th scope="col" class="as-px-6 as-py-3">
                      {customerPortalSettingEntity?.loyaltyDetailsLabelTextV2 || "Loyalty Details"}
                    </th>
                  </tr>
                  </thead>
                  <tbody>{loyaltyTableData}</tbody>
                </table>
              </div>
            )}
          </div>
        </TailwindModal>
      ) : ''}

      <div className="as-text-sm as-text-gray-500 as-modet-attributes-wrapper">
        {customerPortalSettingEntity?.enableViewAttributes && (
          <AddCustomAttributes
            contractId={contractId}
            lineId={prd?.node?.id}
            shop={shopName}
            currentVariant={prd?.node}
            attributeEdit={attributeEdit}
            setAttributeEdit={setAttributeEdit}
            subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
            customerPortalSettingEntity={customerPortalSettingEntity}
            shopName={shopName}
            isViewDetailsModalOpen={isViewDetailsModalOpen}
            setIsViewDetailsModalOpen={setIsViewDetailsModalOpen}
          />
        )}
      </div>

      <TailwindModal
        open={isDeleteProductModalOpen}
        setOpen={resetDeleteProductModal}
        actionMethod={() => deleteProductHandler(customerPortalSettingEntity?.removeDiscountCodeAutomatically)}
        actionButtonText={!showDeleteProductMessaging ? customerPortalSettingEntity?.confirmCommonText || 'Confirm' : ''}
        updatingFlag={deleteInProgress}
        modalTitle={customerPortalSettingEntity?.deleteProductTitleText || 'Delete Product'}
        className="as-model-delete-product-confirmation"
        success={deleteProductSuccess}
      >
        {showDeleteProductMessaging &&
          <PopupMessaging showSuccess={deleteProductSuccess} showError={deleteProductError}/>}
        {!showDeleteProductMessaging && (
          <div className="as-text-sm as-text-gray-500">
            <div>
              <p
                className="as-text-gray-500 as-text-sm as-confirmation-message">{customerPortalSettingEntity?.deleteConfirmationMsgTextV2 || 'Are you sure?'}</p>
            </div>
          </div>
        )}
      </TailwindModal>

      <TailwindModal
        open={deleteDiscountFirst}
        setOpen={resetDeleteProductModal}
        actionMethod={() => deleteProductHandler(true)}
        actionButtonText={'Confirm'}
        updatingFlag={deleteInProgress}
        modalTitle={'Delete Product'}
        className="as-model-delete-product-discount-confirmation"
      >
        <div className="as-text-sm as-text-gray-500">
          <div>
            <p className="as-text-gray-500 as-text-sm as-confirmation-message">
              {customerPortalSettingEntity?.removeDiscountCodeLabel ||
                'This product is tied to a discount, please remove the discount before trying to remove the product.'}
            </p>
          </div>
        </div>
      </TailwindModal>

      <EditCustomerContractDetail
        contractId={contractId}
        lineId={prd?.node?.id}
        shopName={shopName}
        index={index}
        totalProductPriceObj={prd?.node?.lineDiscountedPrice}
        productQuantity={prd?.node?.quantity}
        productId={prd?.node?.productId}
        variantId={prd?.node?.variantId}
        subscriptionEntities={subscriptionEntities?.lines?.edges}
        productPrice={prd?.node?.lineDiscountedPrice?.amount}
        currencyCode={subscriptionEntities?.deliveryPrice?.currencyCode}
        attributeEdit={attributeEdit}
        subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
        prd={prd}
        isEditModalOpen={isEditModalOpen}
        setIsEditModalOpen={setIsEditModalOpen}
        productTitle={prd?.node?.title}
        productImage={
          prd?.node?.productId == null
            ? 'https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660'
            : prd?.node?.variantImage?.transformedSrc
        }
        subUpcomingOrderEntities={subUpcomingOrderEntities}
        oldVariantId={prd?.node?.variantId?.split('/').pop()}
        productData={productData}
      />


      <SwapProductCustomer
        upcomingOrderId={subUpcomingOrderEntities[0]?.id.toString()}
        lineId={prd?.node?.id}
        fullfillmentId={subscriptionEntities?.originOrder?.fulfillmentOrders?.edges[0]?.node?.id}
        sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
          return line?.node?.sellingPlanId;
        })}
        shopName={shopName}
        contractId={contractId}
        subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
        customerPortalSettingEntity={customerPortalSettingEntity}
        isSwapProduct={isSwapProductModalOpen}
        isSwapProductModalOpen={isSwapProductModalOpen}
        setIsSwapProductModalOpen={setIsSwapProductModalOpen}
        variantId={prd?.node?.variantId?.split('/').pop()}
        oldProductDetail={prd?.node}
      />

      {/* <Card
        key={prd.id}
        style={{
            marginTop: '16px',
            backgroundColor: prd?.node?.productId == null ? 'beige' : ''
        }}
        >
        <CardBody>
            {prd?.node?.productId != null && (
            <Fragment>
                <Row>
                <Col md={8}>


                    {
                    prd?.node?.discountAllocations?.map(prdDiscountNode => {
                        let finalnode = null;
                        let isDiscountGlobal = globalDiscounts?.some((discount => {
                            return prdDiscountNode?.discount?.id === discount?.node?.id;
                        }))

                        if (!isDiscountGlobal) {
                            finalnode = subscriptionEntities?.discounts?.edges?.find(discountNode => {
                            return discountNode?.node?.id === prdDiscountNode?.discount?.id;
                            })
                        }

                        if (finalnode) {
                            return ( <p className="mt-2" style={{wordBreak: "break-all"}}>
                            <b>Discount Coupon Applied: </b>{finalnode?.node?.title} @ {finalnode?.node?.value?.percentage || finalnode?.node?.value?.amount?.amount}{finalnode?.node?.value?.percentage ? '% ' : finalnode?.node?.value?.amount?.currencyCode}
                            &nbsp; {!removeDiscountInProgress && <span style={{color: "#13b5ea", textDecoration: "underline", cursor: "pointer"}}  onClick={() => removeDiscountOnCustomerPortal(contractId, finalnode?.node?.id, shopName)}>delete</span>} {removeDiscountInProgress && <span style={{display: "inline-block"}} className="appstle_loadersmall" />}
                            </p>)
                        }

                    })
                    }
                </Col>
                (!((prd?.node?.variantId?.split('/')?.pop() == "41471913853099" || prd?.node?.variantId?.split('/')?.pop() == "41471913885867")  &&  (shopName == 'japan-art-club.myshopify.com' || shopName == 'shop.theotakubox.com')) ||
                {
                    (!customerPortalSettingEntity?.variantIdsToFreezeEditRemove?.includes(prd?.node?.variantId?.split('/')?.pop())) &&
                    <EditCustomerContractDetail
                    contractId={contractId}
                    lineId={prd?.node?.id}
                    shop={shopName}
                    index={index}
                    totalProductPriceObj={prd?.node?.lineDiscountedPrice}
                    productQuantity={prd?.node?.quantity}
                    productId={prd?.node?.productId}
                    variantId={prd?.node?.variantId}
                    subscriptionEntities={subscriptionEntities?.lines?.edges}
                    productPrice={prd?.node?.lineDiscountedPrice?.amount}
                    currencyCode={subscriptionEntities?.deliveryPrice?.currencyCode}
                    attributeEdit={attributeEdit}
                    subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                    />
                }



                { customerPortalSettingEntity?.enableSwapProductFeature && <SwapProductCustomer
                    upcomingOrderId = {subUpcomingOrderEntities[0]?.id.toString()}
                    lineId={prd?.node?.id}
                    fullfillmentId = {subscriptionEntities?.originOrder?.fulfillmentOrders?.edges[0]?.node?.id}
                    sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
                        return line?.node?.sellingPlanId
                    })}
                    shopName={shopName}
                    contractId={contractId}
                    subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                    customerPortalSettingEntity={customerPortalSettingEntity}
                />}
                </Row>
            </Fragment>
            )}
        </CardBody>
        </Card> */}
    </tr>
  );
}
