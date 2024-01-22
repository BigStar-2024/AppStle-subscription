import React, {useEffect, useState} from 'react';
import TailwindModal from './TailwindModal';
import axios from 'axios';
import EditCustomerContractDetail from './EditCustomerContractDetail';
import PopupMessaging from './PopupMessaging';
import LoyaltyDetailsLineItem from './LoyaltyDetailsLineItem';
import SwapProductCustomer from "app/CustomerV3/Subscription/AddProductModelStep/SwapProductCustomer";
import {connect, useSelector} from "react-redux";
import {
  getBundlingByToken,
  getBundlingListsBySingleProduct
} from "app/entities/subscription-bundling/subscription-bundling.reducer";
import getSymbolFromCurrency from "currency-symbol-map";
import EditSingleBundleProductContent from "./EditSingleBundleProductContent";
import {getEntity} from "app/entities/subscriptions/subscription.reducer";

function SubscriptionDetailSingleProduct({
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
                                           subscriptionContractDetails,
                                           getBundlingByToken,
                                           getBundlingListsBySingleProduct,
                                           subscriptionBundling,
                                           subscriptionBundlingList,
                                           getEntity
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
  const [isSwapSingleProducBuildABoxtModalOpen, setSwapSingleProducBuildABoxtModalOpen] = useState(false);
  const subscriptionEntitiesFull = useSelector(state => state.subscription.entity);
  let [isEditQuantity, setIsEditQuantity] = useState(false);
  let [buildABoxSingleProductProducts, setBuildABoxSingleProductProducts] = useState([]);
  let [aplicableProducts, setApplicableProducts] = useState([]);
  const [applicableSettings, setApplicableSettings] = useState(null);
  const [singleBuildABoxError, setSingleBuildABoxError] = useState(null);
  const [updatingLineAttribute, setUpdatingLineAttribute] = useState(false);
  const [searchValue, setSearchValue]=useState('')
  const [prodcutToSwap, setProductToSwap]= useState('')

  const [totalProductsAdded, setTotalProductsAdded] = useState(0)

  useEffect(() => {
    if (sellingPlan && sellingPlanData) {
      createLoyaltyTableData(sellingPlan, sellingPlanData);
    }
  }, [sellingPlan, sellingPlanData]);

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

  const resetSingleProductBuidABoxModel = () => {
    setSwapSingleProducBuildABoxtModalOpen(false);
    setIsEditQuantity(false);
    setSearchValue('')
  }

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


  useEffect(() => {
    if (subscriptionContractDetails.subscriptionType != null && subscriptionContractDetails?.subscriptionType === "BUILD_A_BOX_SINGLE_PRODUCT") {
      getBundlingListsBySingleProduct();
    }
  }, [subscriptionContractDetails])

  useEffect(() => {
    if (subscriptionBundlingList && subscriptionBundlingList.length > 0) {
      let buildABoxToken = null;
      if (subscriptionContractDetails.subscriptionTypeIdentifier != null) {
        buildABoxToken = subscriptionContractDetails.subscriptionTypeIdentifier;
      } else {
        buildABoxToken = subscriptionEntities?.lines?.edges[index]?.node?.customAttributes?.find((item) => item.key === "_appstle-bb-id")?.value || null
      }
      const appliedSubscriptionBundling = subscriptionBundlingList?.find((item) => item.uniqueRef === buildABoxToken);
      const singleProductSetting = appliedSubscriptionBundling?.singleProductSettings ? JSON.parse(appliedSubscriptionBundling?.singleProductSettings) : [];
      const commaSeparatedProducts = subscriptionEntities?.lines?.edges[index]?.node?.customAttributes?.find((item) => item.key === "products")?.value || "";
      const productId = subscriptionEntities?.lines?.edges[index]?.node?.productId?.split('/').pop()
      const applicableSettings = singleProductSetting?.find((item) => item?.sourceProduct?.id === parseInt(productId));
      const planProducts = applicableSettings?.products;
      const productTitleList = commaSeparatedProducts.split(",");

      let buildABoxSingleProductProducts = []
      productTitleList.forEach((item) => {
        const selectedProduct = planProducts?.find((product) => item.includes(product.title))
        if (selectedProduct) {
          const productData = {
            product: selectedProduct,
            quantity: parseInt(item.split("x")[0])
          }
          buildABoxSingleProductProducts.push(productData);
        }
      })
      setBuildABoxSingleProductProducts(buildABoxSingleProductProducts);
      if (applicableSettings) {
        setApplicableProducts(applicableSettings?.products || []);
      }
      setApplicableSettings(applicableSettings);
    }
  }, [subscriptionBundlingList])

  useEffect(() => {
    buildABoxSingleProductProducts.length ? setTotalProductsAdded(buildABoxSingleProductProducts.map(item => item.quantity).reduce((prev, next) => prev + next, 0)) : null
  }, [buildABoxSingleProductProducts])

  const renderSelectedProduct = (product) => {
    let found = buildABoxSingleProductProducts.find((item) => item.product.id === product.id);
    if (found) {
      return true
    } else {
      return false
    }
  }

  const handleAddRemoveProduct = (checked, product) => {
    if (checked) {
      const totalQuantity = buildABoxSingleProductProducts.length ? buildABoxSingleProductProducts.map(item => item.quantity).reduce((prev, next) => prev + next, 0) : 0;
      if (applicableSettings.maxQuantity && applicableSettings.maxQuantity < totalQuantity + 1) {
        setSingleBuildABoxError("Maximum limit " + applicableSettings.maxQuantity + " Items.");
        return;
      } else {
        const productData = {
          product: product,
          quantity: 1
        }
        setBuildABoxSingleProductProducts([...buildABoxSingleProductProducts, productData]);
        setSingleBuildABoxError(null);
      }
    } else {
      const newProducts = [...buildABoxSingleProductProducts];
      const toRemoveIndex = newProducts.findIndex((item) => item.product.id === product.id);
      if (toRemoveIndex > -1) {
        newProducts.splice(toRemoveIndex, 1)
        setBuildABoxSingleProductProducts(newProducts);
      }
    }
  }

  const removeSwapProduct = (product, addProduct) => {
    const newProducts = [...buildABoxSingleProductProducts];
    const toRemoveIndex = newProducts.findIndex((item) => item.product.id === product.id);
    if (toRemoveIndex > -1) {
      newProducts.splice(toRemoveIndex, 1)
      newProducts.push({
        product: addProduct,
        quantity: 1
      })
      setBuildABoxSingleProductProducts(newProducts);
      updateSubscriptionContract(newProducts)
    }
  }

  const handleChangeEditProductQuantity = (value, product) => {
    const newProducts = [...buildABoxSingleProductProducts];
    const editIndex = newProducts.findIndex((item) => item.product.id === product.id);
    const totalQuantity = buildABoxSingleProductProducts.length ? buildABoxSingleProductProducts.filter((item) => item.product.id !== product.id).map(item => item.quantity).reduce((prev, next) => prev + next, 0) : 0;
    if (editIndex > -1 && applicableSettings.maxQuantity && totalQuantity + parseInt(value) > applicableSettings.maxQuantity) {
      setSingleBuildABoxError("Maximum limit " + applicableSettings.maxQuantity + " Items.");
      return
    } else if (editIndex > -1 && (!applicableSettings.maxQuantity || totalQuantity + parseInt(value) <= applicableSettings.maxQuantity)) {
      newProducts[editIndex].quantity = parseInt(value);
      setSingleBuildABoxError(null);
    }
    setBuildABoxSingleProductProducts(newProducts);
  }

  const updateSubscriptionContract = (updatedProductList) => {
    setUpdatingLineAttribute(true);
    let productAttribute = []
    let productList  = updatedProductList ? updatedProductList : buildABoxSingleProductProducts
    productList.forEach((item) => {
      const text = item.quantity + "x " + item.product.title
      productAttribute.push(text)
    })
    const attributes = [...subscriptionEntities?.lines?.edges[index]?.node?.customAttributes, {
      key: "products",
      value: productAttribute.join(",")
    }]
    const requestUrl = `/api/subscription-contracts-update-line-item-attributes?contractId=${contractId}&lineId=${prd?.node?.id}`;
    axios.put(requestUrl, attributes)
      .then(data => {
        setIsEditQuantity(false);
        setUpdatingLineAttribute(false);
        setSwapSingleProducBuildABoxtModalOpen(false);
        getEntity(contractId);
      })
      .catch(err => {
        setIsEditQuantity(false);
        setUpdatingLineAttribute(false);
        setSwapSingleProducBuildABoxtModalOpen(false);
      });
  }
  const handleSearch = event => {
    if (event.target.value !== searchValue) {
      setSearchValue(event.target.value.toLowerCase());
    }
  };

  const handleSwap = (product) => {
    setProductToSwap(product)
    setIsEditQuantity(true);
    setSwapSingleProducBuildABoxtModalOpen(true);
  };

  return (
    <div class="as-bg-white as-border-b hover:as-bg-gray-50 as-product-line">
      <div class="as-px-6 as-py-4 as-font-medium as-text-gray-900 as-product-line-info">
        <div className="as-grid as-grid-cols-12 as-gap-4 as-items-center as-py-2 as-border-b">
          <div className="as-col-span-3 as-product-image-wrapper">
            <img
              src={
                prd?.node?.productId == null
                  ? 'https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660'
                  : prd?.node?.variantImage?.transformedSrc
              }
              alt=""
              className="as-w-full as-h-70 as-product-image"
            />
          </div>
          <div className="as-col-span-5 as-product-line-details">
            {prd?.node?.title && (
              <p
                className={`as-text-sm as-text-gray-800 as-mb-2 as-product-title ${prd?.node?.productId == null ? 'as-line-through' : ''}`}>
                {prd?.node?.title}
                {prd?.node?.productId == null ? '*' : ''}
              </p>
            )}
            {prd?.node?.variantTitle && prd?.node?.variantTitle !== '-' && prd?.node?.variantTitle !== 'Default Title' && (
              <p className="as-text-xs as-text-gray-500 as-variant">
                <span
                  className="as-font-medium as-text-gray-600 as-label">{customerPortalSettingEntity?.variantLblText || 'Variant'}:</span>{' '}
                <span className="as-value">{prd?.node?.variantTitle}</span>
              </p>
            )}
          </div>

          <div className="as-col-span-2 as-flex as-items-center as-product-line-details">
            <div className="as-px-6 as-py-4 as-hidden md:as-table-cell as-product-line-total">
        <span
          className="as-value as-total-value">{getSymbolFromCurrency(prd?.node?.lineDiscountedPrice?.currencyCode || "")}{parseFloat(prd?.node?.lineDiscountedPrice?.amount).toFixed(2)}</span>
            </div>
          </div>

          <div className="as-col-span-2 as-flex as-justify-center as-items-center as-product-line-details">
            {true ? (<span
              onClick={() =>{ setSwapSingleProducBuildABoxtModalOpen(false); setIsEditQuantity(true)}}
              className="as-cursor-pointer as-font-medium as-text-indigo-600 hover:as-underline as-cta as-edit-product-cta"
            >
                  {customerPortalSettingEntity?.editCommonText || 'Edit'}
            </span>) : ""}


            {customerPortalSettingEntity?.deleteProductFlag && subscriptionEntities?.lines?.edges.length > 1 ? (
              <div class="as-px-2 as-py-2 as-product-line-action">
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
              </div>
            ) : ""}
          </div>


        </div>
        <div className="as-grid as-gap-4 as-items-center as-py-2">
          {buildABoxSingleProductProducts.length ? buildABoxSingleProductProducts.map((item, index) => {
            return (
              <div className="as-flex as-items-center">
                <img
                  src={item?.product?.imageSrc}
                  width={80}
                  style={{padding: '6px'}}
                />
                <div>
                  <label
                    htmlFor="default-checkbox"
                    className="as-text-sm as-font-medium as-text-gray-900 as-flex as-items-center"
                  > <a
                    className={'as-text-blue-600 as-hover:underline'}
                    href={`https://${shopName}/products/${item?.product?.productHandle}`}
                    target="_blank"
                  >
                    {item?.product?.title}
                  </a>
                  </label>
                  <p>
                    {customerPortalSettingEntity?.quantityLbl || 'Quantity'}: {item?.quantity}
                  </p>
                  <p class="as-mt-4 as-product-action">
                    <span class="as-cursor-pointer as-font-medium as-text-indigo-600 hover:as-underline as-ml-2 as-cta as-swap-edit-product-cta" onClick={() => handleSwap(item)}>Swap</span>
                  </p>
                </div>
              </div>
            )
          }) : ""}


        </div>
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
        oldVariantId = {prd?.node?.variantId?.split('/').pop()}
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
        variantId= {prd?.node?.variantId?.split('/').pop()}
      />

      <TailwindModal
        open={isEditQuantity}
        setOpen={resetSingleProductBuidABoxModel}
        secondaryActionUpdatingFlag={updatingLineAttribute}
        modalTitle={isSwapSingleProducBuildABoxtModalOpen? (customerPortalSettingEntity?.swapProductBtnTextV2 || 'Swap Product') :('Edit single Product settings')}
        className="as-model-order-now"
        secondaryActionButtonText={isSwapSingleProducBuildABoxtModalOpen ? '' : totalProductsAdded >= applicableSettings?.minQuantity ? "Save" : ''}
        secondaryActionMethod={() => updateSubscriptionContract()}
      >
        <div className="as-flex as-items-center as-pb-5">
        <input
          className="as-w-full as-border-2 as-border-gray-300 as-bg-white as-h-10 as-px-5 as-pr-5 as-rounded-lg as-text-sm focus:as-outline-none as-add-product-search-input"
          type="search"
          name="search"
          placeholder= { isSwapSingleProducBuildABoxtModalOpen ? "Search a product to swap" : "Search a product"}
          onChange={handleSearch}
          value={searchValue}
          autocomplete="off"
        />
          <p className={"as-text-red-700"}>{singleBuildABoxError != null ? singleBuildABoxError : ""}</p>
        </div>
        <div className="as-grid lg:as-grid-cols-2 as-gap-4">
          {aplicableProducts.length && aplicableProducts.map((product, index) => {
            const entity = buildABoxSingleProductProducts.find((item) => item.product.id === product.id)
            return (
              product.title.toLowerCase().includes(searchValue) ? <EditSingleBundleProductContent
                entity={entity}
                customerPortalSettingEntity={customerPortalSettingEntity}
                renderSelectedProduct={renderSelectedProduct}
                handleAddRemoveProduct={handleAddRemoveProduct}
                product={product}
                handleChangeEditProductQuantity={handleChangeEditProductQuantity}
                totalProductsAdded={totalProductsAdded}
                applicableSettings={applicableSettings}
                isProductSwap = {isSwapSingleProducBuildABoxtModalOpen}
                prodcutToSwap={prodcutToSwap}
                removeSwapProduct={removeSwapProduct}
              /> :""
            )
          })}
        </div>

      </TailwindModal>

    </div>
  );
}


const mapStateToProps = state => ({
  account: state.authentication.account,
  subscriptionBundling: state.subscriptionBundling.entity,
  subscriptionBundlingList: state.subscriptionBundling.entities,
});

const mapDispatchToProps = {
  getBundlingByToken,
  getBundlingListsBySingleProduct,
  getEntity,
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionDetailSingleProduct);
