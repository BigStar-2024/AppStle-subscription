import React, { useState, useEffect } from 'react';
import { getEntity } from 'app/entities/subscriptions/subscription.reducer';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Card, Row, Col, Label, CardBody } from 'reactstrap';
import { Form, Field } from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import Axios from 'axios';
import { toast } from 'react-toastify';

import './loader.scss';

const AddCustomAttributes = ({ getEntity, contractId, lineId, shop, currentVariant, ...props }) => {
  // const { contractId, lineId, shop, currentVariant } = props;

  const [modalShow, setModalShow] = useState(false);
  const [formInitialValue, setFormInitialValue] = useState({ customAttribute: [{ key: '', value: '' }] });
  const [updatingValue, setUpdatingValue] = useState(false);

  const reservedAttributes = ["_order-date", "_min-cycles", "_max-cycles", "_min-quantity", "_max-quantity", "_appstle-bb-id", "products", "Products", "_appstle-one-time-product", "_appstle-free-product", "_appstle-first-order-id", "_appstle-first-order-name", "_appstle-bb-product-sku"]

  useEffect(() => {
    if (currentVariant?.customAttributes?.length > 0) {
      setFormInitialValue({ customAttribute: [...currentVariant?.customAttributes] });
    }
  }, [currentVariant]);

  const toggleModal = () => {
    setModalShow(!modalShow);
  };

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  const checkDisabled = (key) => {
    if (reservedAttributes.indexOf(key) === -1) {
      return false
    } else {
      return true
    }
  }

  const saveLineItemAttribute = attributeValuesV2 => {
    console.log(attributeValuesV2);
    setUpdatingValue(true);
    const requestUrl = `/api/subscription-contracts-update-line-item-attributes?contractId=${contractId}&lineId=${lineId}`;
    Axios.put(requestUrl, Object.values(attributeValuesV2)[0])
      .then(data => {
        setModalShow(false);
        setUpdatingValue(false);
        getEntity(contractId);
        toast.success('Contract attribute added', options);
      })
      .catch(err => {
        console.log(err);
        setModalShow(false);
        setUpdatingValue(false);
        toast.error('Contract update failed', options);
      });
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
        <span onClick={() => setModalShow(true)} style={{ color: '#13b5ea', textDecoration: 'underline', cursor: 'pointer' }}>
          View Attributes
        </span>
      </div>

      <Modal isOpen={modalShow} toggle={toggleModal} size="lg">
        {/* <ModalHeader toggle={toggle} close={closeBtn}>{header}</ModalHeader> */}
        <ModalHeader toggle={toggleModal}>
          <div style={{ fontSize: '19px', fontWeight: '500' }}>
            Attributes for &nbsp;
            {/* <a href={`https://${shop}/admin/products/${currentVariant?.productId?.split('/')[4]}`} target="_blank"> */}
            {currentVariant?.title}{' '}
            {currentVariant?.variantTitle &&
              currentVariant?.variantTitle !== '-' &&
              currentVariant?.variantTitle !== 'Default Title' &&
              '-' + currentVariant?.variantTitle}
            {/* </a> */}
          </div>
        </ModalHeader>
        <ModalBody>
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
                  <div className="d-flex justify-content-end mb-2">
                    <Button
                      type="button"
                      color="primary"
                      disabled={errors?.customAttribute}
                      className="primary btn d-flex align-items-center"
                      onClick={() => push('customAttribute', undefined)}
                    >
                      <i className="lnr pe-7s-plus btn-icon-wrapper"></i> &nbsp; Add New
                    </Button>
                    {/* <Button
                      type="button"
                      color="warning"
                      className="ml-2 primary btn btn-primary d-flex align-items-center"
                      onClick={() => pop('customAttribute')}
                    >
                      <i className="lnr lnr-cross btn-icon-wrapper"></i> &nbsp; Remove Last
                    </Button> */}
                  </div>

                  <FieldArray name="customAttribute">
                    {({ fields }) =>
                      fields.map((name, index) => (
                        <>
                          <p className="text-with-line">
                            <span>
                              <b>Attribute {index + 1}</b>
                            </span>
                          </p>
                          <Row key={name}>
                            <Col md={4}>
                              <FormGroup>
                                <Label for="key">Key</Label>
                                <Field
                                  render={({ input, meta }) => (
                                    <Input {...input} placeholder="eg. Order Note"
                                    invalid={meta.error && meta.touched ? true : null}
                                    disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                                     />
                                  )}
                                  validate={value => {
                                    return !value ? 'Please provide proper value.' : undefined;
                                  }}
                                  id={`${name}.key`}
                                  className="form-control"
                                  type="text"
                                  name={`${name}.key`}
                                  disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                                />
                              </FormGroup>
                            </Col>
                            <Col md={7}>
                              <Label for="value">Value</Label>
                              <Field
                                render={({ input, meta }) => (
                                  <Input
                                    {...input}
                                    placeholder="eg. pack on gift wrap"
                                    invalid={meta.error && meta.touched ? true : null}
                                    disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                                  />
                                )}
                                validate={value => {
                                  return !value ? 'Please provide proper value.' : undefined;
                                }}
                                id={`${name}.value`}
                                className="form-control"
                                type="text"
                                name={`${name}.value`}
                                disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                              />
                            </Col>
                            <Col md={1}>
                              <Label for="key"></Label>
                              <Button
                                color="danger"
                                disabled={checkDisabled(values["customAttribute"]?.[index]?.["key"])}
                                style={{ padding: '12px' }}
                                className="mt-2 primary btn btn-primary d-flex align-items-center"
                                onClick={() => fields.remove(index)}
                              >
                                <i className="lnr lnr-trash btn-icon-wrapper"></i>
                              </Button>
                            </Col>
                          </Row>
                        </>
                      ))
                    }
                  </FieldArray>

                  <div className="d-flex justify-content-end">
                    <Button type="submit" color="success" disabled={submitting || pristine}>
                      {!updatingValue && (
                        <>
                          <i className="lnr pe-7s-diskette btn-icon-wrapper"></i> Save
                        </>
                      )}
                      {updatingValue && <div className="appstle_loadersmall" />}
                    </Button>
                    &nbsp;&nbsp;
                    <Button onClick={toggleModal}>Cancel</Button>
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
        </ModalBody>
      </Modal>
    </div>
  );
};

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(AddCustomAttributes);
