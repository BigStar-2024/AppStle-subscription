import React from 'react';
import { Button, Col, FormGroup, Input, Label, Modal, ModalBody, ModalHeader, Row } from 'reactstrap';
import { Field, Form } from 'react-final-form';
import './loader.scss';
import { FieldArray } from 'react-final-form-arrays';
import arrayMutators from 'final-form-arrays';

const UpdateOrderNoteModal = props => {
  const {
    isUpdating,
    modaltitle,
    confirmBtnText,
    isUpdateAttributeOpenFlag,
    toggleUpdateAttributeModal,
    updateSubscriptionsAttributesMethod,
    subscriptionEntities
  } = props;

  const reservedAttributes = ["_order-date", "_min-cycles", "_max-cycles", "_min-quantity", "_max-quantity", "_appstle-bb-id", "products", "Products", "_appstle-one-time-product", "_appstle-free-product", "_appstle-first-order-id", "_appstle-first-order-name", "_appstle-bb-product-sku"]

  const checkDisabled = (key) => {
    if (reservedAttributes.indexOf(key) === -1) {
      return false
    } else {
      return true
    }
  }

  const onSubmit = values => {
    updateSubscriptionsAttributesMethod(values.customAttributes);
  };
  return (
    <div>
      <Modal isOpen={isUpdateAttributeOpenFlag} size="lg" toggle={toggleUpdateAttributeModal}>
        <ModalHeader toggle={toggleUpdateAttributeModal}>{modaltitle}</ModalHeader>
        <ModalBody>
          <div style={{ padding: '22px' }}>
            <Form
              initialValues={subscriptionEntities}
              onSubmit={onSubmit}
              mutators={{
                ...arrayMutators
              }}
              render={({
                handleSubmit,
                form: {
                  mutators: { push, pop }
                },
                submitting,
                pristine,
                values
              }) => (
                <form onSubmit={handleSubmit}>
                  <div
                    style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center'
                    }}
                  >
                    <h5 className="m-0">Add New</h5>
                    <Button
                      type="button"
                      className="d-flex align-items-center appstle_order-detail_update-button "
                      onClick={() => push('customAttributes', undefined)}
                    >
                      Add New
                    </Button>
                  </div>
                  <hr />
                  <FieldArray name="customAttributes">
                    {({ fields }) =>
                      fields.map((name, index) => (
                        <Row key={name} className={index !== 0 ? 'mt-4' : ''}>
                          <Col md={4}>
                            <FormGroup>
                              <Label for="key">
                                <b>Attribute Key</b>
                              </Label>
                              <Field
                                render={({ input, meta }) => (
                                  <Input
                                    {...input}
                                    placeholder="eg. Order Note"
                                    invalid={meta.error && meta.touched ? true : null}
                                    disabled={checkDisabled(values["customAttributes"]?.[index]?.["key"])}
                                  />
                                )}
                                validate={value => {
                                  return !value ? 'Please provide proper value.' : undefined;
                                }}
                                id={`${name}.key`}
                                className="form-control"
                                type="text"
                                name={`${name}.key`}
                                disabled={checkDisabled(values["customAttributes"]?.[index]?.["key"])}
                              />
                            </FormGroup>
                          </Col>
                          <Col md={7}>
                            <Label for="value">
                              <b>Attribute Value</b>
                            </Label>
                            <Field
                              render={({ input, meta }) => (
                                <Input
                                  {...input}
                                  placeholder="eg. pack on gift wrap"
                                  invalid={meta.error && meta.touched ? true : null}
                                  disabled={checkDisabled(values["customAttributes"]?.[index]?.["key"])}
                                />
                              )}
                              validate={value => {
                                return !value ? 'Please provide proper value.' : undefined;
                              }}
                              id={`${name}.value`}
                              className="form-control"
                              type="text"
                              name={`${name}.value`}
                              disabled={checkDisabled(values["customAttributes"]?.[index]?.["key"])}
                            />
                          </Col>
                          <Col md={1}>
                            <Label for="key"></Label>
                            <Button
                              color="danger"
                              style={{ padding: '12px' }}
                              className="mt-2 danger btn btn-danger d-flex align-items-center"
                              onClick={() => fields.remove(index)}
                              disabled={checkDisabled(values["customAttributes"]?.[index]?.["key"])}
                            >
                              {' '}
                              <i className="lnr lnr-trash btn-icon-wrapper"></i>
                            </Button>
                          </Col>
                        </Row>
                      ))
                    }
                  </FieldArray>
                  <div style={{ marginTop: '21px', display: 'flex', justifyContent: 'center' }}>
                    <Button size="lg" className="btn-shadow-primary" color="danger" type="button" onClick={toggleUpdateAttributeModal}>
                      cancel
                    </Button>
                    <Button style={{ marginRight: '12px' }} size="lg" className="btn-shadow-primary" color="primary" type="submit">
                      {isUpdating ? (
                        <div className="d-flex align-items-center">
                          <div className="appstle_loadersmall" />
                          <span className="ml-2 font-weight-light"> Please Wait</span>
                        </div>
                      ) : (
                        confirmBtnText
                      )}
                    </Button>
                  </div>
                </form>
              )}
            />
          </div>
        </ModalBody>
      </Modal>
    </div>
  );
};

export default UpdateOrderNoteModal;
