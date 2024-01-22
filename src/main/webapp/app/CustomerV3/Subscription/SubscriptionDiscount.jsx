import React, { useEffect, useState } from 'react';
import TailwindModal from "./TailwindModal";
import PopupMessaging from "./PopupMessaging";
import { Input } from 'reactstrap';
import axios from 'axios';
import { set } from 'lodash';

export default function SubscriptionDiscount(props) {
    const {globalDiscounts, subscriptionEntities, customerPortalSettingEntity, removeDiscountInProgress, applyDiscountOnCustomerPortal, contractId, shopName, removeDiscountOnCustomerPortal, customerEntity} = props;
    const [isOpen, setIsOpen] = useState(false)
    const [isRedeemRewardsOpen, setIsRedeemRewardsOpen] = useState(false)

    const [showRedeemRewardsMessaging, setShowRedeemRewardsMessaging] = useState(false);
    const [successRedeemRewards, setSuccessRedeemRewards] = useState(false);
    const [errorRedeemRewards, setErrorRedeemRewards] = useState(false);

    const [showMessaging, setShowMessaging] = useState(false);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(false);
    const [discountCode, setDiscountCode] = useState('');
    const [selectedDiscountCode, setSelectedDiscountCode] = useState(null);

    const [loyaltyCustomerDetails, setLoyaltyCustomerDetails] = useState({})
    const [loyaltyRedemptionOption, setIsLoyaltyRedemptionOption] = useState([])

    const [selectedReward, setSelectedReward] = useState(null)
    const [isRedemptionInProgress, setIsRedemptionInProgress] = useState(false);

    const [isDeleteInProgress, setIsDeleteInProgress] = useState(false);
    const [discountIdToDelete, setDiscountIdToDelete] = useState(null);

    useEffect(() => {
        axios.get(`/api/loyalty-integration/customer?customerId=${customerEntity?.id}`)
        .then(res => setLoyaltyCustomerDetails(res.data))
        .catch(err => console.log(err))
        axios.get(`/api/loyalty-integration/redeem-options?customerId=${customerEntity?.id}`)
        .then(res => {
            setIsLoyaltyRedemptionOption(res.data)
            setSelectedReward(res?.data[0]?.id)
        })
        .catch(err => console.log(err))
    }, [customerEntity])

    const updateDiscountHandler = async () => {
        let results = null;
        if (!isDeleteInProgress) {
            results = await applyDiscountOnCustomerPortal(contractId, discountCode)
        } else {
            results = await removeDiscountOnCustomerPortal(contractId, discountIdToDelete, shopName)
        }
        if (results) {
            setShowMessaging(true);
            if (isDeleteInProgress) {
                if (results?.value?.status === 200) {
                    setSuccess(true);
                    setError(false);
                } else {
                    setSuccess(false);
                    setError(true);
                }
            } else {
                if (results?.action?.payload?.data?.discounts?.edges?.length) {
                    setSuccess(true);
                    setError(false);
                } else {
                    setSuccess(false);
                    setError(true);
                }
            }

        }
    }


    const resetModal = () => {
        setIsOpen(false);
        setShowMessaging(false);
        setSuccess(false);
        setError(false);
        setIsDeleteInProgress(false);
    }

    const resetRedeemReawrdsModal = () => {
        setIsRedeemRewardsOpen(false);
        setShowRedeemRewardsMessaging(false);
        setSuccessRedeemRewards(false);
        setErrorRedeemRewards(false);
        setIsRedemptionInProgress(false)
    }

    const redeemRewards = () => {
       
        return axios.post(`/api/loyalty-integration/redeem?customerId=${customerEntity?.id}&redeemOptionId=${selectedReward}`).then(res => {
            let discountCode = res?.data;
            if (discountCode) {
                return applyDiscountOnCustomerPortal(contractId, discountCode)
            }
        })
    }

    const redeemRewardsHandler = async () => {
        let results = null;
        setIsRedemptionInProgress(true)
        results = await redeemRewards()
        setIsRedemptionInProgress(false)
        if (results) {
            setShowRedeemRewardsMessaging(true);
            if (results?.value?.status === 200) {
                setSuccessRedeemRewards(true);
                setErrorRedeemRewards(false);
            } else {
                setSuccessRedeemRewards(false);
                setErrorRedeemRewards(true);
            }

        }
    }

    return (
        <>
        {customerPortalSettingEntity?.discountCode && subscriptionEntities.status == 'ACTIVE' && <div className='as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-edit-discount'>
            <div className="as-flex as-justify-between as-mb-2">
            <p className="as-text-sm as-text-gray-500 as-card_title as-edit-discount_title">{subscriptionEntities?.discounts?.edges?.length > 0 ? (customerPortalSettingEntity?.discountCouponAppliedText || "Applied Discount Codes") :  (customerPortalSettingEntity?.addDiscountCodeText || "Add Discount Codes")}</p>
            <p className="as-text-sm as-text-blue-500 as-cursor-pointer as-cta as-card_cta as-edit-frequency_cta"
               onClick={() => setIsRedeemRewardsOpen(true)}>{customerPortalSettingEntity?.redeemRewardsTextV2 || "Redeem Rewards"}</p>
            </div>
            <div class="as-text-sm as-text-gray-800 as-pt-3 as-card_data as-edit-discount_data">
                {(subscriptionEntities?.discounts?.edges?.length > 0 && subscriptionEntities?.discounts?.edges?.map((discount, index) =>
                        <div class="as-flex as-items-center as-uppercase as-edit-discount_checkbox as-justify-between as-mb-2">
                            <div class="as-flex as-items-center">
                                <label for={index} class="as-ml-2 as-text-sm as-text-gray-900"> {discount?.node?.title} @ {discount?.node?.value?.percentage || discount?.node?.value?.amount?.amount}{discount?.node?.value?.percentage ? '% ' : discount?.node?.value?.amount?.currencyCode} {discount.node?.targetType == "SHIPPING_LINE" ? 'SHIPPING' : ''} </label>
                            </div>
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                class="as-h-6 as-w-6 as-stroke-red-500 as-cursor-pointer as-delete-icon"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                                stroke-width="2"
                                onClick={() =>{
                                    setDiscountIdToDelete(discount?.node?.id);
                                    setIsDeleteInProgress(true);
                                    setIsOpen(true);
                                }
                                }
                                >
                                <path
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                                />
                            </svg>
                        </div>
                ))}
                {(!subscriptionEntities?.discounts?.edges?.length || !customerPortalSettingEntity?.enableAllowOnlyOneDiscountCode) && (
                    <>
                        <form>
                            <Input
                                value={discountCode}
                                className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md as-edit-discount_input"
                                type="text"
                                onInput={event => setDiscountCode(event.target.value)}
                            />
                        </form>
                        <button type="button" onClick={() => setIsOpen(true)} class="as-mt-4 as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--primary  as-edit-discount_primary-button">
                            {(customerPortalSettingEntity.discountCodeApplyButtonText || "Add Discount Codes")}
                        </button>
                    </>
                )}
                </div>
            </div>
        }
        <TailwindModal open={isOpen} setOpen={resetModal} actionMethod={updateDiscountHandler} actionButtonText={!showMessaging ? (!isDeleteInProgress ? customerPortalSettingEntity.discountCodeApplyButtonText : customerPortalSettingEntity?.discountCouponRemoveText) : ""} updatingFlag={removeDiscountInProgress} modalTitle={isDeleteInProgress ? (customerPortalSettingEntity?.discountCouponRemoveText || "Remove Discount") : (customerPortalSettingEntity?.addDiscountCodeText || "Add Discount Codes")} className="as-model-discount-codes" success={success}>
            {showMessaging && <PopupMessaging
                showSuccess={success}
                showError={error}
                errorMessage= {customerPortalSettingEntity?.discountCouponNotAppliedText || "Invalid discount code"}
            />}
            {!showMessaging &&
                <div className="as-text-sm as-text-gray-500">
                    {isDeleteInProgress ? (customerPortalSettingEntity?.removeDiscountCodeAlertText || `Are you sure you want to remove the discount code.`) : (customerPortalSettingEntity?.addDiscountCodeAlertText || `Are you sure you want to add discount code`)}
                </div>
            }
        </TailwindModal>
        <TailwindModal open={isRedeemRewardsOpen} setOpen={resetRedeemReawrdsModal} actionMethod={redeemRewardsHandler} actionButtonText={(!showRedeemRewardsMessaging && selectedReward) ? 'Redeem Selected' : ''} modalTitle={customerPortalSettingEntity?.rewardsTextV2 || "Rewards"} className="as-model-redeem-rewards" success={success} updatingFlag={isRedemptionInProgress}>
            {showRedeemRewardsMessaging && <PopupMessaging
                showSuccess={successRedeemRewards}
                showError={errorRedeemRewards}
                errorMessage= {customerPortalSettingEntity?.discountCouponNotAppliedText || "Invalid discount code"}
            />}
            {!showRedeemRewardsMessaging &&

               <div>
                     <div class="as-rounded-xl as-py-4 as-flex as-justify-between as-bg-white as-items-center">
                    <div>
                      <h4 class="as-font-bold as-text-sm">
                        {customerPortalSettingEntity?.yourRewardsTextV2 || "Your Rewards"}
                      </h4>
                      <p class="as-text-gray-500 as-text-xs">{customerPortalSettingEntity?.yourAvailableRewardsPointsTextV2 || "Your available rewards points"}</p>
                    </div>
                    <div>
                    <button
                        class="as-group as-relative as-flex as-w-full as-justify-center as-rounded-md as-border as-border-transparent as-bg-indigo-600 as-py-2 as-px-4 as-text-sm as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-indigo-500 focus:as-ring-offset-2">
                           {loyaltyCustomerDetails?.availablePointsBalance}
                    </button>
                    </div>
                  </div>
                  <div className='as-grid as-gap-4'>
                    {loyaltyRedemptionOption?.map(item => {
                        return <div class="as-pointer as-rounded-xl as-shadow as-text-sm as-py-4 as-px-5 as-flex as-justify-between as-bg-white as-items-center" onClick={() => setSelectedReward(item?.id)}>
                            <div className='as-flex'>
                            <div className={`as-p-3 as-rounded-full ${(selectedReward === item?.id) ? `as-bg-[#43b049]` : `as-bg-gray-200`} as-flex as-justify-center as-items-center as-w-10 as-h-10`}>
                            {(selectedReward !== item?.id) &&<i class={`fa-solid ${item?.icon}`}></i>}
                            {(selectedReward === item?.id) && <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="as-w-6 as-h-6 as-text-white">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                            </svg>}
                        </div>
                        <div className='as-ml-4'>
                          <h4 class="as-font-bold as-text-sm">
                            {item?.pointsCostText}
                          </h4>
                          <p class="as-text-gray-500 as-text-xs">{item?.description}</p>
                        </div>
                            </div>
                            <div>
                                {<svg class="as-w-5 as-h-5 as-text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path></svg>}
                            </div>
                      </div>
                    })}
                  
                    
                  </div>
               </div>
              
            }
        </TailwindModal>
        </>

    )
}

{/* <span className="ml-3 appstle_font_size" style={{cursor: "pointer"}} onClick={() => removeDiscountOnCustomerPortal(contractId, discount?.node?.id, shopName)}>
{!removeDiscountInProgress &&
(<span style={{color: "#13b5ea", textDecoration: 'underline'}}>{customerPortalSettingEntity?.discountCouponRemoveText || "Remove"}</span>)}
{removeDiscountInProgress && <div className="appstle_loadersmall" />}
</span> */}
