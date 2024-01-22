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
  ...props
}) => {
  // const { contractId, lineId, shop, currentVariant } = props;

  const [formInitialValue, setFormInitialValue] = useState({ customAttribute: [{ key: '', value: '' }] });
  const [updatingValue, setUpdatingValue] = useState(false);

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

  const saveLineItemAttribute = attributeValuesV2 => {
    console.log(attributeValuesV2);
    setUpdatingValue(true);
    const requestUrl = `api/subscription-contracts-update-line-item-attributes?contractId=${contractId}&lineId=${lineId}&isExternal=true`;
    Axios.put(requestUrl, Object.values(attributeValuesV2)[0])
      .then(data => {
        setAttributeEdit(false);
        setUpdatingValue(false);
        getCustomerPortalEntity(contractId);
        toast.success('Contract attribute added', options);
      })
      .catch(err => {
        console.log(err);
        setAttributeEdit(false);
        setUpdatingValue(false);
        toast.error('Contract update failed', options);
      });
  };

  return (
    <>
      <Col md={2}></Col>
      <Col md={10}>
        {!attributeEdit && (
          <div className="mt-2">
            <span onClick={() => setAttributeEdit(true)} style={{ color: '#13b5ea', textDecoration: 'underline', cursor: 'pointer' }}>
              {customerPortalSettingEntity?.viewAttributeLabelTextV2}
            </span>
          </div>
        )}
      </Col>
      <Col md={2}></Col>
      <Col md={10}>
        <div className="mt-2">
          {attributeEdit && (
            <div>
              <Form
                onSubmit={saveLineItemAttribute}
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
                  return (
                    <form onSubmit={handleSubmit}>
                      <div className="d-flex justify-content-end mb-2"></div>
                      <Card style={{ boxShadow: 'none', border: '1px solid #ccc', borderRadius: '5px' }}>
                        <CardBody>
                          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <h5 className="m-0">{customerPortalSettingEntity?.attributeHeadingTextV2}</h5>
                            <Button
                              type="button"
                              disabled={errors?.customAttribute || subscriptionContractFreezeStatus}
                              className="d-flex align-items-center appstle_order-detail_update-button "
                              onClick={() => push('customAttribute', undefined)}
                            >
                              {subscriptionContractFreezeStatus && <FontAwesomeIcon className="mr-2" icon={faLock} />}
                              {customerPortalSettingEntity?.addNewButtonTextV2}
                            </Button>
                          </div>
                          <hr />
                          <FieldArray name="customAttribute">
                            {({ fields }) =>
                              fields.map((name, index) => (
                                <Row key={name} className={index !== 0 ? 'mt-4' : ''}>
                                  <Col md={4}>
                                    <FormGroup>
                                      <Label for="key">
                                        <b>{customerPortalSettingEntity?.attributeNameLabelTextV2}</b>
                                      </Label>
                                      <Field
                                        render={({ input, meta }) => (
                                          <Input
                                            {...input}
                                            placeholder="eg. Order Note"
                                            invalid={meta.error && meta.touched ? true : null}
                                          />
                                        )}
                                        validate={value => {
                                          return !value ? 'Please provide proper value.' : undefined;
                                        }}
                                        id={`${name}.key`}
                                        className="form-control"
                                        type="text"
                                        name={`${name}.key`}
                                      />
                                    </FormGroup>
                                  </Col>
                                  <Col md={7}>
                                    <Label for="value">
                                      <b>{customerPortalSettingEntity?.attributeValueV2}</b>
                                    </Label>
                                    <Field
                                      render={({ input, meta }) => (
                                        <Input
                                          {...input}
                                          placeholder="eg. pack on gift wrap"
                                          invalid={meta.error && meta.touched ? true : null}
                                        />
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide proper value.' : undefined;
                                      }}
                                      id={`${name}.value`}
                                      className="form-control"
                                      type="text"
                                      name={`${name}.value`}
                                    />
                                  </Col>
                                  <Col md={1}>
                                    <Label for="key"></Label>
                                    {!subscriptionContractFreezeStatus && (
                                      <Button
                                        color="danger"
                                        style={{ padding: '12px' }}
                                        disabled={subscriptionContractFreezeStatus}
                                        className="mt-2 danger btn btn-danger d-flex align-items-center"
                                        onClick={() => fields.remove(index)}
                                      >
                                        {' '}
                                        <i className="lnr lnr-trash btn-icon-wrapper"></i>
                                      </Button>
                                    )}
                                  </Col>
                                </Row>
                              ))
                            }
                          </FieldArray>
                        </CardBody>
                      </Card>

                      <div className="d-flex justify-content-end mt-3">
                        <Button
                          className="mr-2 appstle_order-detail_update-button "
                          disabled={subscriptionContractFreezeStatus}
                          type="submit"
                          disabled={submitting || pristine}
                        >
                          {subscriptionContractFreezeStatus && <FontAwesomeIcon className="mr-2" icon={faLock} />}
                          {!updatingValue && <>{customerPortalSettingEntity?.updateFreqBtnText}</>}
                          {updatingValue && <div className="appstle_loadersmall" />}
                        </Button>
                        <Button onClick={toggleModal} className="appstle_order-detail_cancel-button ">
                          {customerPortalSettingEntity?.cancelButtonTextV2}
                        </Button>
                      </div>
                      {/* <div className="buttons">
                  <button type="submit" disabled={submitting || pristine}>
                    Submit
                  </button>
                  <button type="button" onClick={form.reset} disabled={submitting || pristine}>
                    Reset
                  </button>
                </div> */}
                      {/* <pre>{JSON.stringify(values, 0, 2)}</pre> */}
                    </form>
                  );
                }}
              />
            </div>
          )}
        </div>
      </Col>
    </>
  );
};

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(AddCustomAttributes);
