import React, { useEffect, useState } from 'react';
import moment from 'moment';
import TailwindModal from './TailwindModal';
import PopupMessaging from './PopupMessaging';
import { Payment } from '@mui/icons-material';
import { replaceVariablesValue } from '../../shared/util/customer-utils';

export default function UpdatePayment(props) {
  const { subscriptionContractDetails, customerPortalSettingEntity, paymentData, updateCustomerPaymentDetail, updatePaymentButtonText, updatingPayment, customerPaymentDetails, customerPaymentMethod, getCustomerPortalEntity } = props;
  const [isOpen, setIsOpen] = useState(false);

  const [showMessaging, setShowMessaging] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const [isSelectedPaymentIdValid, setIsSelectedPaymentIdValid] = useState(true);
  const [addNewPaymentMethodSelected, setAddNewPaymentMethodSelected] = useState(true);
  const [customerPaymentMethodsList, seCustomerPaymentMethodsList] = useState([]);
  const [selectedPaymentMethodId, setSelectedPaymentMethodId] = useState("");

  useEffect(() => {
    if (customerPaymentMethod?.id) {
      setSelectedPaymentMethodId(customerPaymentMethod?.id);
    }
  },[customerPaymentMethod])

  const updatePaymentActionHandler = async () => {
    setIsSelectedPaymentIdValid(true);
    if (!addNewPaymentMethodSelected) {
      if (!selectedPaymentMethodId) {
        setIsSelectedPaymentIdValid(false);
        return;
      }
    }
    let results = await updateCustomerPaymentDetail(addNewPaymentMethodSelected, selectedPaymentMethodId);
    if (results) {
      setShowMessaging(true);
      if (results?.value?.status === 200) {
        setSuccess(true);
        setError(false);
        getCustomerPortalEntity();
      } else {
        setSuccess(false);
        setError(true);
      }
    }
  };

  const resetModal = () => {
    setIsOpen(false);
    setShowMessaging(false);
    setSuccess(false);
    setError(false);
  };

  useEffect(() => {
    if (customerPaymentDetails && customerPaymentDetails?.edges && customerPaymentDetails?.edges.length) {
         seCustomerPaymentMethodsList(customerPaymentDetails?.edges?.map((paymentMethod) => {
          let data = paymentMethod.node;
          return {
            id: data?.id,
            maskedNumber: data?.instrument?.maskedNumber,
            name: data?.instrument?.name,
            brand: data?.instrument?.brand
          }
        }));
    }
  }, [customerPaymentDetails]);


  const getMonth = (idx) => {
    let objDate = new Date();
    objDate?.setDate(1);
    objDate?.setMonth(idx-1);
    return objDate?.toLocaleString(navigator?.language, { month: "long" });
  }

  return (
    <>
      <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-edit-payment">
        <div className="as-flex as-justify-between as-mb-2">
          <p className="as-text-sm as-text-gray-500 as-card_title as-edit-payment_title">
            {customerPortalSettingEntity?.paymentDetailAccordionTitleV2}
          </p>
          {paymentData && (
            <p
              className="as-text-sm as-text-blue-500 as-cursor-pointer as-cta as-card_cta as-edit-pyment_cta"
              onClick={() => setIsOpen(true)}
            >
              {customerPortalSettingEntity?.editPaymentButtonTextV2 || 'Edit'}
            </p>
          )}
        </div>
        <p class="as-text-sm as-text-gray-800 as-pt-3 as-card_data as-edit-payment_data">
          {!paymentData ? (
            'There is no payment method associated with this contract.'
          ) : paymentData?.__typename === 'CustomerCreditCard' ? (
            <>
              <p className="as-card-type">
                {' '}
                <span className="as-label">{customerPortalSettingEntity?.paymentMethodTypeText}<span className='colon-symbol'>:</span>&nbsp;</span>
                <span className="as-value">{paymentData?.__typename === 'CustomerCreditCard'
                  ? customerPortalSettingEntity?.creditCardTextV2
                  : paymentData?.__typename}{' '}
                - <span style={{ textTransform: 'uppercase' }}>{paymentData?.brand}</span> {customerPortalSettingEntity?.endingWithTextV2}{' '}
                {paymentData?.lastDigits}</span>
              </p>

              <p className="as-card-holder">
                <span className="as-label">{customerPortalSettingEntity?.cardHolderNameText}<span className='colon-symbol'>:</span>&nbsp;</span><span className="as-value">{paymentData?.name}</span>
              </p>
              <p className="as-card-expiry">
                <span className="as-label">{customerPortalSettingEntity?.cardExpiryTextV2}<span className='colon-symbol'>:</span>&nbsp;</span><span className="as-value">{getMonth(paymentData?.expiryMonth)}{' '}
                {paymentData?.expiryYear}</span>
              </p>
            </>
          ) : paymentData?.__typename === 'CustomerPaypalBillingAgreement' ? (
            <div className="btn-icon-vertical btn-square btn-transition as-paypal" outline="true" color="primary">
              {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
              <span className="as-label" style={{ fontSize: '15px' }}> {customerPortalSettingEntity?.paymentMethodTypeText}</span>
              <br></br>
              <span className="as-value"><Payment /> {customerPortalSettingEntity?.paypalLblText || 'Paypal'}</span>
            </div>
          ) : paymentData?.__typename === 'CustomerShopPayAgreement' ? (
            <div className="btn-icon-vertical btn-square btn-transition as-shop-pay" outline="true" color="primary">
              {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
              <span className="as-label" style={{ fontSize: '15px' }}> {customerPortalSettingEntity?.paymentMethodTypeText}</span>
              <br></br>
              <span className="as-value"><Payment /> {customerPortalSettingEntity?.shopPayLblText || 'ShopPay'}</span>
            </div>
          ) : (
            customerPortalSettingEntity?.unknownPaymentReachoutUsText || 'Unknown. Please reach out us.'
          )}
        </p>
      </div>
      <TailwindModal
        open={isOpen}
        setOpen={resetModal}
        actionMethod={updatePaymentActionHandler}
        actionButtonText={!showMessaging && (addNewPaymentMethodSelected ? `${customerPortalSettingEntity?.sendEmailTextV2 || "Send Email"}` : `${customerPortalSettingEntity?.saveButtonTextV2 || "Save"}`)}
        updatingFlag={updatingPayment}
        modalTitle={customerPortalSettingEntity?.paymentDetailAccordionTitleV2}
        ignoreSubscriptionContractFreezeStatus={true}
        className="as-model-payment-details"
        success={success}
      >
        {showMessaging && (
          <PopupMessaging showSuccess={success} showError={error} successMessage={addNewPaymentMethodSelected ? replaceVariablesValue(customerPortalSettingEntity?.updatePaymentMessageV2, {'customer_email_id': subscriptionContractDetails?.customerEmail || ''}) : customerPortalSettingEntity?.changePaymentMessage} />
        )}
        {!showMessaging && (
          <div className="as-text-sm as-text-gray-500">
            <div>
              {paymentData?.__typename === 'CustomerShopPayAgreement' ? (
                customerPortalSettingEntity?.shopPayPaymentUpdateTextV2 ? (
                  <>
                    <div dangerouslySetInnerHTML={{ __html: customerPortalSettingEntity?.shopPayPaymentUpdateTextV2 }} />
                  </>
                ) : (
                  <>
                    {' '}
                    You are using Shop Pay, please use this article for updating the payment methods.
                    <a
                      href="https://help.shop.app/hc/en-us/articles/4412203886996-How-do-I-manage-my-subscription-orders-with-Shop-Pay-"
                      target="_blank"
                    >
                      click here
                    </a>
                  </>
                )
              ) : (
                ''
              )}
              {customerPaymentMethodsList.length > 1 && (
                <div>
                  <div className="as-flex as-items-center as-mb-4">
                    <input
                      checked={addNewPaymentMethodSelected}
                      onChange={value => setAddNewPaymentMethodSelected(true)}
                      id="default-radio-1"
                      type="radio"
                      value="true"
                      name="default-radio"
                      className="as-w-4 as-h-4 as-text-blue-600 as-bg-white-100 as-border-gray-300 as-focus:ring-blue-500  as-focus:ring-2"
                    />
                    <label htmlFor="default-radio-1" className="as-ml-2 as-text-sm as-font-medium as-text-gray-900">
                      {customerPortalSettingEntity?.updatePaymentMethodTitleTextV2 || 'Update payment method.'}
                    </label>
                  </div>
                  <div className="flex items-center as-mb-4">
                    <input
                      checked={!addNewPaymentMethodSelected}
                      onChange={value => setAddNewPaymentMethodSelected(false)}
                      id="default-radio-2"
                      type="radio"
                      value="false"
                      name="default-radio"
                      className="as-w-4 as-h-4 as-text-blue-600 as-bg-white-100 as-border-gray-300 as-focus:ring-blue-500  as-focus:ring-2"
                    />
                    <label htmlFor="default-radio-2" className="as-ml-2 as-text-sm as-font-medium as-text-gray-900">
                      {customerPortalSettingEntity?.chooseAnotherPaymentMethodTitleText || 'Choose another payment method.'}
                    </label>
                  </div>
                  {!addNewPaymentMethodSelected && (
                    <div className="flex items-center">
                      <label htmlFor="methodType" className="as-block as-text-sm as-font-medium as-text-gray-700">
                      {customerPortalSettingEntity?.selectPaymentMethodTitleText || 'Select Payment Method'}
                      </label>
                      <select name="methodType" type="select" onChange={(e) => setSelectedPaymentMethodId(e.target.value)}
                        className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md">
                          {customerPaymentMethodsList.map(i => (
                            <option value={i.id} selected={i.id == selectedPaymentMethodId}>({i?.name} - {i?.maskedNumber}) - ({i?.brand})</option>
                          ))}
                      </select>
                      {!isSelectedPaymentIdValid && <p class="as-mt-2 as-text-sm as-text-red-600">{'Please select payment method'}</p>}
                    </div>
                  )}
                </div>
              )}
              <br />
              {customerPortalSettingEntity?.paymentNotificationText}
            </div>
          </div>
        )}
      </TailwindModal>
    </>
  );
}
