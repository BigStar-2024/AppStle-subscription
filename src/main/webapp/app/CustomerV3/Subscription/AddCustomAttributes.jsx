import React, { useState, useEffect } from 'react';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Card, Row, Col, Label, CardBody } from 'reactstrap';
import { Form, Field } from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import Axios from 'axios';
import { toast } from 'react-toastify';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import TailwindModal from './TailwindModal';
import PopupMessaging from './PopupMessaging';
import { data } from 'app/DemoPages/Dashboards/GeneralSetting/CustomizeWidgetSetting/subscription-customisation';

// import './loader.scss';

const AddCustomAttributes = ({
  getCustomerPortalEntity,
  contractId,
  lineId,
  shop,
  currentVariant,
  attributeEdit,
  setAttributeEdit,
  customerPortalSettingEntity,
  subscriptionContractFreezeStatus,
  isViewDetailsModalOpen,
  setIsViewDetailsModalOpen,
  ...props
}) => {
  // const { contractId, lineId, shop, currentVariant } = props;

  const [formInitialValue, setFormInitialValue] = useState({ customAttribute: [{ key: '', value: '' }] });
  const [updatingValue, setUpdatingValue] = useState(false);

  const [showMessaging, setShowMessaging] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);

  const reservedAttributes = ["_order-date", "_min-cycles", "_max-cycles", "_min-quantity", "_max-quantity", "_appstle-bb-id", "products", "Products", "_appstle-one-time-product", "_appstle-free-product", "_appstle-first-order-id", "_appstle-first-order-name", "_appstle-bb-product-sku"]

  useEffect(() => {
    if (currentVariant?.customAttributes?.length > 0) {
      setFormInitialValue({ customAttribute: [...currentVariant?.customAttributes] });
    }
  }, [currentVariant]);

  const toggleModal = () => {
    setAttributeEdit(!attributeEdit);
  };

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  const checkDisabled = (key) => {
    if ((reservedAttributes.indexOf(key) === -1)) {
      if (customerPortalSettingEntity?.allowAttributeToEditV2 === 'false') {
        return true
      } else {
        return false
      }
    } else {
      return true
    }
  }

  const saveLineItemAttribute = async attributeValuesV2 => {
    console.log(attributeValuesV2);
    setUpdatingValue(true);
    const requestUrl = `api/subscription-contracts-update-line-item-attributes?contractId=${contractId}&lineId=${lineId}&isExternal=true`;
    return Axios.put(requestUrl, Object.values(attributeValuesV2)[0])
      .then(data => {
        setAttributeEdit(false);
        setUpdatingValue(false);
        getCustomerPortalEntity(contractId);
        toast.success(customerPortalSettingEntity?.contractUpdateMessageTextV2 || 'Contract attribute added', options);
        return data;
      })
      .catch(err => {
        console.log(err);
        setAttributeEdit(false);
        setUpdatingValue(false);
        toast.error(customerPortalSettingEntity?.contractErrorMessageTextV2 || 'Contract update failed', options);
        return err;
      });
  };

  const onSubmit = async values => {
    let results = await saveLineItemAttribute(values);

    if (results) {
      setShowMessaging(true);
      if (results?.status === 200) {
        setSuccess(true);
        setError(false);
      } else {
        setSuccess(false);
        setError(true);
      }
    }
  };

  const resetModal = () => {
    setIsViewDetailsModalOpen(false);
    setShowMessaging(false);
    setSuccess(false);
    setError(false);
  };

  let submit;
  return (
    <Form
      onSubmit={onSubmit}
      mutators={{
        ...arrayMutators
      }}
      initialValues={formInitialValue}
      render={({
        handleSubmit,
        form: {
          mutators: { push, pop }
        }, // injected from final-form-arrays above
        pristine,
        form,
        submitting,
        errors,
        values
      }) => {
        submit = handleSubmit;
        return (
          <TailwindModal
            open={isViewDetailsModalOpen}
            setOpen={resetModal}
            modalTitle={customerPortalSettingEntity?.attributeHeadingTextV2 || 'Attributes'}
            actionButtonText={
              customerPortalSettingEntity?.enableEditAttributes
                ? !showMessaging
                  ? customerPortalSettingEntity?.updateButtonText || 'Update'
                  : ''
                : ''
            }
            actionMethod={submit}
            secondaryActionButtonText={
              customerPortalSettingEntity?.enableEditAttributes
                ? !showMessaging
                  ? customerPortalSettingEntity?.addNewButtonTextV2
                  : ''
                : ''
            }
            secondaryActionMethod={() => push('customAttribute', undefined)}
            updatingFlag={updatingValue}
            className = 'as-model-custom-Attributes'
            success = {success}
          >
            {showMessaging && <PopupMessaging showSuccess={success} showError={error} />}
            {!showMessaging && (
              <div className="as-mt-2">
                <div>
                  <form onSubmit={handleSubmit}>
                    <div className="as-grid as-gap-4">
                      {/* <button
                      type="button"
                      disabled={errors?.customAttribute || subscriptionContractFreezeStatus}
                      className="d-flex align-items-center appstle_order-detail_update-button "
                      onClick={() => push('customAttribute', undefined)}
                    >
                    {
                    subscriptionContractFreezeStatus && <FontAwesomeIcon
                                  className='mr-2'
                                  icon={faLock}/>
                  }
                  {customerPortalSettingEntity?.addNewButtonTextV2}</button> */}
                      <FieldArray name="customAttribute">
                        {({ fields }) =>
                          fields.map((name, index) => (
                            <div key={name} className="as-grid as-grid-cols-12 as-gap-4 as-items-center">
                              <div className="as-col-span-5">
                                <div>
                                  <label htmlFor={`${name}.key`} className="as-block as-text-sm as-font-medium as-text-gray-700">
                                    {customerPortalSettingEntity?.attributeNameLabelTextV2}
                                  </label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <Input
                                        {...input}
                                        className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                        placeholder="eg. Order Note"
                                        invalid={meta.error && meta.touched ? true : null}
                                        disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                                      />
                                    )}
                                    validate={value => {
                                      return !value
                                        ? customerPortalSettingEntity?.requireFieldMessage || 'Please provide proper value.'
                                        : undefined;
                                    }}
                                    id={`${name}.key`}
                                    className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                    type="text"
                                    name={`${name}.key`}
                                    disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                                  />
                                </div>
                              </div>
                              <div className="as-col-span-5">
                                <label htmlFor={`${name}.key`} className="as-block as-text-sm as-font-medium as-text-gray-700">
                                  {customerPortalSettingEntity?.attributeValueV2}
                                </label>
                                <Field
                                  render={({ input, meta }) => (
                                    <Input
                                      {...input}
                                      placeholder="eg. pack on gift wrap"
                                      className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                      invalid={meta.error && meta.touched ? true : null}
                                      disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                                    />
                                  )}
                                  validate={value => {
                                    return !value
                                      ? customerPortalSettingEntity?.requireFieldMessage || 'Please provide proper value.'
                                      : undefined;
                                  }}
                                  id={`${name}.value`}
                                  className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                                  type="text"
                                  name={`${name}.value`}
                                  disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                                />
                              </div>
                              {
                              !checkDisabled(values["customAttribute"]?.[index]?.["key"]) && <div className="as-col-span-2">
                                <label className="as-block as-text-sm as-font-medium as-text-gray-700">&nbsp;</label>
                                <Button color="danger" disabled={subscriptionContractFreezeStatus || checkDisabled(values["customAttribute"]?.[index]?.["key"])} onClick={() => fields.remove(index)}>
                                  {' '}
                                  <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    class="as-h-6 as-w-6 as-stroke-red-500 as-cursor-pointer"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                    stroke="currentColor"
                                    stroke-width="2"
                                  >
                                    <path
                                      stroke-linecap="round"
                                      stroke-linejoin="round"
                                      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                                    />
                                  </svg>
                                </Button>
                              </div>
                            }
                            </div>
                          ))
                        }
                      </FieldArray>
                    </div>
                  </form>
                </div>
              </div>
            )}
          </TailwindModal>
        );
      }}
    />
  );
};

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(AddCustomAttributes);
