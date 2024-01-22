import React from 'react';
import { Button, Col, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { Field, Form } from 'react-final-form';
import { connect } from 'react-redux';
import { addDiscountCode } from 'app/entities/subscriptions/subscription.reducer';

const AddDiscountCode = ({
  toggle,
  showModal,
  shopName,
  contractId,
  addDiscountCode,
  addDiscountCodeInProgress,
  addDiscountCodeSuccess
}) => {
  const closeBtn = (
    <button className="close" onClick={toggle}>
      &times;
    </button>
  );

  const required = value => (value ? undefined : 'Above field is Required');
  const mustBeNumber = value => (isNaN(value) ? 'Must be a number' : undefined);
  const minValue = min => value => (isNaN(value) || value > min ? undefined : `Should be greater than ${min}`);
  const composeValidators = (...validators) => value => validators.reduce((error, validator) => error || validator(value), undefined);

  const save = async values => {
    if (values?.discountType === 'AMOUNT') {
      values.appliesOnEachItem =
        values.appliesOnEachItem === true ||
        values.appliesOnEachItem === null ||
        values.appliesOnEachItem === '' ||
        values.appliesOnEachItem === undefined;
    } else {
      values.appliesOnEachItem = false;
    }

    await addDiscountCode(contractId, values);
    toggle();
  };
  let submit;

  return (
    <Modal isOpen={showModal} toggle={toggle}>
      <ModalHeader toggle={toggle} close={closeBtn}>
        Create Discount Code
      </ModalHeader>
      <ModalBody>
        <Form
          onSubmit={save}
          render={({ handleSubmit, form, submitting, pristine, values, errors, valid }) => {
            submit = () => {
              return Object.keys(errors).length === 0 && handleSubmit();
            };
            return (
              <form onSubmit={handleSubmit}>
                <FormGroup>
                  <Label>Discount Title</Label>
                  <Field
                    render={({ input, meta }) => (
                      <>
                        <Input {...input} style={{ flexGrow: '1' }} invalid={meta.error && meta.touched ? true : null} />
                        {meta.error && (
                          <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                            {meta.error}
                          </div>
                        )}
                      </>
                    )}
                    initialValue=""
                    validate={required}
                    id="discountTitle"
                    className="form-control"
                    type="input"
                    name={`discountTitle`}
                  />
                </FormGroup>
                <FormGroup>
                  <Label>Discount Type</Label>
                  <Field
                    type="select"
                    id={'discountType'}
                    name={'discountType'}
                    render={({ input, meta }) => (
                      <Input
                        invalid={meta.error && meta.touched ? true : null}
                        {...input}
                        style={{
                          flexGrow: 1
                        }}
                      >
                        <option value="PERCENTAGE">Percentage</option>
                        <option value="AMOUNT">Amount</option>
                      </Input>
                    )}
                    initialValue="PERCENTAGE"
                    className="form-control"
                  />
                </FormGroup>
                {values?.['discountType'] === 'PERCENTAGE' && (
                  <FormGroup>
                    <Label>Discount Percentage (%)</Label>
                    <Field
                      render={({ input, meta }) => (
                        <>
                          <Input {...input} style={{ flexGrow: '1' }} invalid={meta.error && meta.touched ? true : null} />
                          {meta.error && (
                            <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                              {meta.error}
                            </div>
                          )}
                        </>
                      )}
                      initialValue=""
                      validate={composeValidators(required, mustBeNumber, minValue(0))}
                      id="percentage"
                      className="form-control"
                      type="input"
                      name={`percentage`}
                    />
                  </FormGroup>
                )}
                {values?.['discountType'] === 'AMOUNT' && (
                  <FormGroup>
                    <Label>Amount</Label>
                    <Field
                      render={({ input, meta }) => (
                        <>
                          <Input {...input} style={{ flexGrow: '1' }} invalid={meta.error && meta.touched ? true : null} />
                          {meta.error && (
                            <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                              {meta.error}
                            </div>
                          )}
                        </>
                      )}
                      initialValue=""
                      validate={composeValidators(required, mustBeNumber, minValue(0))}
                      id="amount"
                      className="form-control"
                      type="input"
                      name={`amount`}
                    />
                  </FormGroup>
                )}
                {/* {values?.['discountType'] === 'AMOUNT' && (
                  <Field
                    type="hidden"
                    name={`appliesOnEachItem`}
                    initialValue={false}
                    render={({ input }) =>
                      (
                        <div>
                          <label>
                            <strong>Applies on Each Item?</strong>
                          </label>
                          <br />
                          <Switch
                            checked={true}
                            onChange={input.onChange}
                            onColor="#3ac47d"
                            uncheckedIcon={false}
                            checkedIcon={false}
                            handleDiameter={20}
                            boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                            activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                            height={17}
                            width={36}
                            className="mr-2 mb-2"
                            id="material-switch"
                          />
                        </div>
                      );
                    }
                  />
                )} */}

                {values?.discountType === 'AMOUNT' ? (
                  <Row>
                    <Col xs={12} sm={12} md={12} lg={12}>
                      <FormGroup check>
                        <Field
                          render={({ input, meta }) => (
                            <Label check>
                              <Input
                                checked={input.value === true || input.value === null || input.value === '' || input.value === undefined}
                                onClick={event => {
                                  input.onChange(true);
                                }}
                                type="radio"
                                name="radio1"
                              />{' '}
                              Apply on subscription items.
                            </Label>
                          )}
                          className="form-control"
                          name="appliesOnEachItem"
                        />
                      </FormGroup>
                      <FormGroup check>
                        <Field
                          render={({ input, meta }) => (
                            <Label check>
                              <Input
                                checked={input.value === false}
                                onClick={event => {
                                  input.onChange(false);
                                }}
                                type="radio"
                                name="radio1"
                              />{' '}
                              Apply on subscription contract.
                            </Label>
                          )}
                          className="form-control"
                          name="appliesOnEachItem"
                        />
                      </FormGroup>
                    </Col>
                  </Row>
                ) : (
                  ''
                )}

                <FormGroup>
                  <Label>Recurring Cycle Limit</Label>
                  <Field
                    render={({ input, meta }) => (
                      <>
                        <Input {...input} style={{ flexGrow: '1' }} invalid={meta.error && meta.touched ? true : null} />
                        {meta.error && (
                          <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                            {meta.error}
                          </div>
                        )}
                      </>
                    )}
                    initialValue=""
                    // validate={composeValidators(required, mustBeNumber, minValue(0))}
                    id="recurringCycleLimit"
                    className="form-control"
                    type="input"
                    name={`recurringCycleLimit`}
                  />
                </FormGroup>
                <Field type="hidden" name="shop" initialValue={shopName}>
                  {({ input, meta }) => <input type="hidden" value={shopName}></input>}
                </Field>
                <Field type="hidden" name="contractId" initialValue={contractId}>
                  {({ input, meta }) => <input type="hidden" value={contractId}></input>}
                </Field>
              </form>
            );
          }}
        />
      </ModalBody>
      <ModalFooter>
        <Button
          color="success"
          style={{ marginLeft: '13px' }}
          className="primary btn btn-primary d-flex"
          onClick={() => submit()}
          type="submit"
        >
          {!addDiscountCodeInProgress && 'Add Discount code'}
          {addDiscountCodeInProgress && <div className="appstle_loadersmall" />}
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = state => ({
  addDiscountCodeInProgress: state.subscription.addDiscountCodeInProgress,
  addDiscountCodeSuccess: state.subscription.addDiscountCodeSuccess
});

const mapDispatchToProps = {
  addDiscountCode
};

export default connect(mapStateToProps, mapDispatchToProps)(AddDiscountCode);
