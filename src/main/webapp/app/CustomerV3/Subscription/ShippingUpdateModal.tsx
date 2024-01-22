import React, { useEffect, useRef, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Field, Form } from 'react-final-form';
import { IRootState } from 'app/shared/reducers';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { ISubscription } from 'app/shared/model/subscription.model';
import { ISubscriptionMailingAddress } from 'app/shared/model/subscription-mailing-address.model';
import { getLocations } from 'app/entities/shipping-profile/helper-data.reducer';
import { updateCustomerShippingAddress } from 'app/entities/subscriptions/subscription.reducer';
import TailwindModal from './TailwindModal';
import PopupMessaging from './PopupMessaging';

type ShippingMethodType = 'SHIPPING' | 'LOCAL' | 'PICK_UP';

type ShippingUpdateFormValues = ISubscriptionMailingAddress & {
  methodType?: ShippingMethodType;
  locationId?: string;
};

interface ShippingUpdateModalProps {
  subscriptionEntity?: ISubscription & {[key: string]: any};
  isOpen: boolean;
  setIsOpen: (open: boolean) => void;
}

const ShippingUpdateModal = (props: ShippingUpdateModalProps) => {
  const { isOpen, setIsOpen } = props;

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity);
  const shopInfo = useSelector((state: IRootState) => state.shopInfo.entity);
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );
  const availableLocations = useSelector((state: IRootState) => state.helperActions.locations);
  const isUpdating = useSelector((state: IRootState) => state.subscription.updatingShippingAddress);
  const countriesCodes = useSelector((state: IRootState) => state.customer.countriesCodes);
  const dispatch = useDispatch();
  const pickupLocations = useSelector((state: IRootState) => availableLocations.filter(item => item.pickupEnabled))

  const [responseInfo, setResponseInfo] = useState<{ success: boolean; message: string }>(null);

  const contractId = subscriptionEntity?.id?.split?.("/")?.pop?.();

  const submitForm = useRef<() => any>(() => {
    console.log('No submit function assigned.');
  });

  useEffect(() => {
    dispatch(getLocations());
  }, []);

  const initialShippingMethod: ShippingMethodType = (() => {
    switch (subscriptionEntity?.deliveryMethod?.__typename) {
      case 'SubscriptionDeliveryMethodShipping':
        return 'SHIPPING';
      case 'SubscriptionDeliveryMethodLocalDelivery':
        return 'LOCAL';
      case 'SubscriptionDeliveryMethodPickup':
        return 'PICK_UP';
    }
  })();

  const initialFormValues: ShippingUpdateFormValues = {
    ...subscriptionEntity?.deliveryMethod?.address,
    methodType: initialShippingMethod,
    locationId: '',
  };

  function getLocationId(location: { name: string; id: string }) {
    const splitLocationId = location?.id.split('/');
    return splitLocationId[splitLocationId.length - 1];
  }

  async function onSubmit(values: ShippingUpdateFormValues) {
    await updateCustomerShippingAddress(
      values,
      contractId
    )(dispatch)
      .then(results => {
        if (results?.value?.status === 200) {
          setResponseInfo({ success: true, message: 'Success!' });
        } else {
          setResponseInfo({ success: false, message: results?.action?.payload?.response?.data?.message });
        }
      })
      .catch(error => setResponseInfo({ success: false, message: error }));
  }

  function required(value: string) {
    return value ? null : customerPortalSettingsEntity?.requireFieldMessage;
  }

  function handleFormLevelValidation(values: ShippingUpdateFormValues): ShippingUpdateFormValues {
    const errors: ShippingUpdateFormValues = {};

    if (values.methodType === 'LOCAL' && !values.phone) {
      errors.phone = customerPortalSettingsEntity?.requireFieldMessage;
    }

    return errors;
  }

  function resetModal() {
    setResponseInfo(null);
  }

  return (
    <>
      <Form
        initialValues={initialFormValues}
        onSubmit={onSubmit}
        validate={handleFormLevelValidation}
        render={({ handleSubmit, values }) => {
          submitForm.current = handleSubmit;
          return (
            // @ts-ignore prevent TailwindModal throwing error for missing props
            <TailwindModal
              className="as-model-shipping-details"
              open={isOpen}
              setOpen={setIsOpen}
              modalTitle={customerPortalSettingsEntity?.shippingLabelText}
              actionMethod={submitForm.current}
              actionButtonText={!responseInfo ? customerPortalSettingsEntity?.saveButtonTextV2 || 'Save' : ''}
              updatingFlag={isUpdating}
              ignoreSubscriptionContractFreezeStatus={customerPortalSettingsEntity?.changeShippingAddressFlag || false}
              afterClose={resetModal}
            >
              {responseInfo && (
                <PopupMessaging
                  showSuccess={responseInfo.success}
                  showError={!responseInfo.success}
                  errorMessage={responseInfo.message}
                  successMessage={null}
                />
              )}
              {!responseInfo && (
                <form onSubmit={handleSubmit} className="as-grid as-gap-4">
                  <div className="as-text-sm as-text-gray-500">
                    <div className="as-grid as-gap-4 as-mb-4">
                      <div>
                        <label htmlFor="methodType" className="as-block as-text-sm as-font-medium as-text-gray-700">
                          {customerPortalSettingsEntity?.selectShippingMethodLabelTextV2 || 'Select Shipping Method'}
                        </label>

                        <Field
                          name="methodType"
                          component="select"
                          initialValue={initialShippingMethod}
                          className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                        >
                          <option value="SHIPPING">{customerPortalSettingsEntity?.shippingLabelTextV2 || 'Shipping'}</option>
                          {shopInfo?.allowLocalDelivery && (
                            <option value="LOCAL">{customerPortalSettingsEntity?.localDeliveryLabelTextV2 || 'Local Delivery'}</option>
                          )}
                          {shopInfo?.allowLocalPickup && (
                            <option value="PICK_UP">{customerPortalSettingsEntity?.pickupLabelTextV2 || 'Pickup'}</option>
                          )}
                        </Field>
                      </div>

                      {values.methodType === 'PICK_UP' && (
                        <div>
                          {pickupLocations?.length ? (
                            <>
                              <label htmlFor="locationId" className="as-block as-text-sm as-font-medium as-text-gray-700">
                                {customerPortalSettingsEntity?.pickupLocationLabelTextV2 || 'Pickup Location'}
                              </label>
                              <Field
                                name="locationId"
                                id="locationId"
                                component="select"
                                initialValue=""
                                validate={required}
                                className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                              >
                                <option value="" disabled>
                                  Select a Pickup Locations
                                </option>
                                {pickupLocations?.map(loc => (
                                  <option key={getLocationId(loc)} value={getLocationId(loc)}>
                                    {loc?.name}
                                  </option>
                                ))}
                              </Field>
                            </>
                          ) : (
                            <p>{customerPortalSettingsEntity?.noPickupLocationFoundLabelTextV2 || 'No Pickup location found.'}</p>
                          )}
                        </div>
                      )}
                    </div>
                  </div>
                  {values.methodType !== 'PICK_UP' && (
                    <div className="as-text-sm as-text-gray-500">
                      <div className="as-grid as-gap-4">
                        <div className="as-grid as-gap-4 as-grid-cols-2">
                          <div>
                            <label htmlFor="firstname" className="as-block as-text-sm as-font-medium as-text-gray-700">
                              {customerPortalSettingsEntity?.firstNameLabelText}
                            </label>
                            <Field
                              name="firstName"
                              id="firstName"
                              validate={required}
                              className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                            >
                              {({ input, meta }) => (
                                <div>
                                  <input
                                    {...input}
                                    type="text"
                                    placeholder={customerPortalSettingsEntity?.firstNameLabelText}
                                    className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                  />
                                  {meta.error && meta.touched && <span className="as-text-xs as-text-red-700">{meta.error}</span>}
                                </div>
                              )}
                            </Field>
                          </div>
                          <div>
                            <label htmlFor="lastName" className="as-block as-text-sm as-font-medium as-text-gray-700">
                              {customerPortalSettingsEntity?.lastNameLabelText}
                            </label>
                            <Field name="lastName" id="lastName" validate={required}>
                              {({ input, meta }) => (
                                <div>
                                  <input
                                    {...input}
                                    type="text"
                                    placeholder={customerPortalSettingsEntity?.lastNameLabelText}
                                    className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                  />
                                  {meta.error && meta.touched && <span className="as-text-xs as-text-red-700">{meta.error}</span>}
                                </div>
                              )}
                            </Field>
                          </div>
                        </div>
                        <div className="as-grid as-gap-4 as-grid-cols-2">
                          <div>
                            <label htmlFor="phone" className="as-block as-text-sm as-font-medium as-text-gray-700">
                              {customerPortalSettingsEntity?.phoneLabelText}
                            </label>
                            <Field
                              name="phone"
                              id="phone"
                              className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                            >
                              {({ input, meta }) => (
                                <div>
                                  <input
                                    {...input}
                                    type="text"
                                    placeholder={customerPortalSettingsEntity?.phoneLabelText}
                                    className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                  />
                                  {meta.error && meta.touched && <span className="as-text-xs as-text-red-700">{meta.error}</span>}
                                </div>
                              )}
                            </Field>
                          </div>
                          <div className="mt-3">
                            <label htmlFor="city" className="as-block as-text-sm as-font-medium as-text-gray-700">
                              {customerPortalSettingsEntity?.cityLabelText}
                            </label>
                            <Field name="city" id="city" validate={required}>
                              {({ input, meta }) => (
                                <div>
                                  <input
                                    {...input}
                                    type="text"
                                    placeholder={customerPortalSettingsEntity?.cityLabelText}
                                    className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                  />
                                  {meta.error && meta.touched && <span className="as-text-xs as-text-red-700">{meta.error}</span>}
                                </div>
                              )}
                            </Field>
                          </div>
                        </div>
                        <div>
                          <label htmlFor="city" className="as-block as-text-sm as-font-medium as-text-gray-700">
                            {customerPortalSettingsEntity?.address1LabelText}
                          </label>
                          <Field name="address1" id="address1" validate={required}>
                            {({ input, meta }) => (
                              <div>
                                <input
                                  {...input}
                                  type="text"
                                  placeholder={customerPortalSettingsEntity?.address1LabelText}
                                  className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                />
                                {meta.error && meta.touched && <span className="as-text-xs as-text-red-700">{meta.error}</span>}
                              </div>
                            )}
                          </Field>
                        </div>
                        <div>
                          <label htmlFor="address2" className="as-block as-text-sm as-font-medium as-text-gray-700">
                            {customerPortalSettingsEntity?.address2LabelText}
                          </label>
                          <Field
                            name="address2"
                            id="address2"
                            component="input"
                            type="text"
                            placeholder={customerPortalSettingsEntity?.address2LabelText}
                            className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                          />
                        </div>
                        <div className="as-grid as-gap-4 as-grid-cols-2">
                          <div>
                            <label htmlFor="company" className="as-block as-text-sm as-font-medium as-text-gray-700">
                              {customerPortalSettingsEntity?.companyLabelText ? customerPortalSettingsEntity?.companyLabelText : 'Company'}
                            </label>
                            <Field
                              name="company"
                              id="company"
                              component="input"
                              type="text"
                              placeholder={
                                customerPortalSettingsEntity?.companyLabelText ? customerPortalSettingsEntity?.companyLabelText : 'Company'
                              }
                              className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                            />
                          </div>
                          <div>
                            <label htmlFor="zip" className="as-block as-text-sm as-font-medium as-text-gray-700">
                              {customerPortalSettingsEntity?.zipLabelText}
                            </label>
                            <Field name="zip" id="zip" validate={required}>
                              {({ input, meta }) => (
                                <div>
                                  <input
                                    {...input}
                                    type="text"
                                    placeholder={customerPortalSettingsEntity?.zipLabelText}
                                    className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                  />
                                  {meta.error && meta.touched && <span className="as-text-xs as-text-red-700">{meta.error}</span>}
                                </div>
                              )}
                            </Field>
                          </div>
                          <div>
                            <label htmlFor="countryCode" className="as-block as-text-sm as-font-medium as-text-gray-700">
                              {customerPortalSettingsEntity?.countryCodeLabelText
                                ? customerPortalSettingsEntity?.countryCodeLabelText
                                : 'Country Code'}
                            </label>
                            <Field
                              name="countryCode"
                              id="countryCode"
                              component="select"
                              className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                            >
                              <option>Select Country</option>
                              {countriesCodes
                                ?.filter(country => country.name !== 'Rest of World')
                                ?.map(country => {
                                  return (
                                    <option key={country.code} value={country.code}>
                                      {country.name} ({country.code})
                                    </option>
                                  );
                                })}
                            </Field>
                          </div>
                          <div>
                            <label htmlFor="provinceCode" className="as-block as-text-sm as-font-medium as-text-gray-700">
                              {customerPortalSettingsEntity?.provinceCodeLabelText
                                ? customerPortalSettingsEntity?.provinceCodeLabelText
                                : 'Province Code'}
                            </label>
                            <Field
                              name="provinceCode"
                              id="provinceCode"
                              component="select"
                              className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                            >
                              {countriesCodes
                                ?.filter(country => country.code === values.countryCode)
                                .pop()
                                ?.provinces?.map((province: any) => {
                                  return (
                                    <option key={province.code} value={province.code}>
                                      {province.name} ({province.code})
                                    </option>
                                  );
                                })}
                            </Field>
                          </div>
                        </div>
                      </div>
                    </div>
                  )}
                </form>
              )}
            </TailwindModal>
          );
        }}
      />
    </>
  );
};


export default ShippingUpdateModal;
