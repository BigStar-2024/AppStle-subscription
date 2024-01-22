import React, {useState} from 'react';
import TailwindModal from "./TailwindModal";
import {formatPrice} from "app/shared/util/customer-utils";
import PopupMessaging from "./PopupMessaging";


export default function SubscriptionDetailProductOneTime({
                                                           prd, customerPortalSettingEntity, attributeEdit,
                                                           setAttributeEdit,
                                                           subscriptionContractFreezeStatus,
                                                           contractId,
                                                           shopName,
                                                           getCustomerPortalEntity,
                                                           index,
                                                           subscriptionEntities,
                                                           subUpcomingOrderEntities,
                                                           deleteOneOffProducts
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

  const deleteProduct = async (variantId, contractId, billingID) => {
    setDeleteInProgress(true);
    let results = await deleteOneOffProducts(variantId, contractId, billingID).then(res => {
      return res
    }).catch((err) => {
      return err;
    })
    await getCustomerPortalEntity(contractId);
    setDeleteInProgress(false);
    if (results) {
      setShowDeleteProductMessaging(true);
      if (results?.status === 200) {
        setDeleteProductSuccess(true);
        setDeleteProductError(false);
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
  }

  return (
    <tr class="as-bg-white as-border-b hover:as-bg-gray-50">
      <th scope="row" class="as-px-6 as-py-4 as-font-medium as-text-gray-900">
        <div className='as-grid as-grid-cols-12 as-gap-4 as-items-center as-py-2'>
          <div className='as-col-span-2'>
            <img
              src={prd?.prdImage}
              alt=""
              className="as-w-full as-h-70"
            />
          </div>
          <div className='as-col-span-10'>

            {<p className={`as-text-sm as-text-gray-800 as-mb-2`}>{prd?.title}</p>}
            {prd?.variantTitle && (prd?.variantTitle !== "-" && prd?.variantTitle !== "Default Title") &&
              <p className="as-text-xs as-text-gray-500"><span
                className="as-font-medium as-text-gray-600">{customerPortalSettingEntity?.variantLblText || "Variant"}:</span> {prd?.variantTitle}
              </p>}
            {<p className={`as-text-xs as-text-gray-500 md:as-hidden`}><span
              className="as-font-medium as-text-gray-600">{customerPortalSettingEntity?.amountLblV2}: </span>{formatPrice(prd?.price)}
            </p>}
            {<p className={`as-text-xs as-text-gray-500 md:as-hidden`}><span
              className="as-font-medium as-text-gray-600">{customerPortalSettingEntity?.quantityLbl}: </span>{prd?.node?.quantity || prd?.quantity || 1}
            </p>}
            {<p className={`as-text-xs as-text-gray-500 md:as-hidden`}><span
              className="as-font-medium as-text-gray-600">{customerPortalSettingEntity?.totalLblText || "Total"}: </span>{formatPrice(prd?.price)}
            </p>}
            <p
              className="as-text-xs as-text-gray-500 as-onetime-text">{customerPortalSettingEntity?.oneTimePurchaseOnlyText || "Added as one time purchase only"}</p>
            {/* <p className="as-mt-4">
                {(!customerPortalSettingEntity?.variantIdsToFreezeEditRemove?.includes(prd?.id)) && <span onClick={() => setIsEditModalOpen(true)} class="as-cursor-pointer as-font-medium as-text-indigo-600 hover:as-underline">Edit</span>}
            </p> */}

          </div>
        </div>
      </th>
      <td class="as-px-6 as-py-4 as-hidden md:as-table-cell">
        <div className=" as-flex as-items-center">
          {formatPrice(prd?.price)}
        </div>
      </td>
      <td className="as-px-6 as-py-4 as-hidden md:as-table-cell">
        {prd?.quantity || 1}
      </td>
      <td class="as-px-6 as-py-4 as-hidden md:as-table-cell">
        {formatPrice(prd?.price)}
      </td>
      {customerPortalSettingEntity?.deleteProductFlag && <td class="as-px-6 as-py-4">
        <svg xmlns="http://www.w3.org/2000/svg" class="as-h-6 as-w-6 as-stroke-red-500 as-cursor-pointer as-delete-icon"
             fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"
             onClick={() => setIsDeleteProductModalOpen(true)}>
          <path stroke-linecap="round" stroke-linejoin="round"
                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
        </svg>
      </td>}
      <TailwindModal open={isDeleteProductModalOpen} setOpen={resetDeleteProductModal}
                     actionMethod={() => deleteProduct(prd.id, contractId, prd.billingID)}
                     actionButtonText={!showDeleteProductMessaging ? customerPortalSettingEntity?.confirmCommonText || "Confirm" : ""}
                     updatingFlag={deleteInProgress}
                     modalTitle={customerPortalSettingEntity?.deleteProductTitleText || "Delete Product"}
                     className="as-model-one-time-delete-product"
                     success={deleteProductSuccess}
                     >
        {showDeleteProductMessaging && <PopupMessaging
          showSuccess={deleteProductSuccess}
          showError={deleteProductError}
        />}
        {!showDeleteProductMessaging && <div className="as-text-sm as-text-gray-500">
          <div>
            <p
              className="as-text-gray-500 as-text-sm">{customerPortalSettingEntity?.deleteConfirmationMsgTextV2 || "Are you sure?"}</p>
          </div>
        </div>}
      </TailwindModal>
      {/* <EditCustomerContractDetail
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
                productImage={prd?.node?.productId == null ? "https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660" :  prd?.node?.variantImage?.transformedSrc}
                subUpcomingOrderEntities={subUpcomingOrderEntities}
            /> */}
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
    </tr>)
}
