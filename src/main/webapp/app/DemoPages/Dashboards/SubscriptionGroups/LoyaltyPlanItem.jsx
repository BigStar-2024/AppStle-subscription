import React, { Fragment, useState } from 'react';
//import './setting.scss';
import { Button, Col, FormGroup, Input, InputGroup, InputGroupAddon, Label, Row } from 'reactstrap';
import { Field } from 'react-final-form';
import { connect } from 'react-redux';
import { createEntity, getEntity, getUsedProducts, reset, updateEntity } from 'app/entities/subscription-group/subscription-group.reducer';
import './CreateSubscriptionGroup.scss';
import AddProductModalSingle from './AddProductModalSingle';

const LoyaltyItem = ({ index, values, name, fields, idx, form }) => {
  const [modal, setModal] = useState(false);
  let addLine;
  return (
    <>
      <Row>
        <Col md={3} sm={3} xs={8}>
          <Fragment>
            <Label for={`${name}.afterCycle`}>After Billing Cycle(s)</Label>
            <FormGroup style={{ display: 'flex' }}>
              <Field
                render={({ input, meta }) => (
                  <InputGroup>
                    <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                    {meta.error && (
                      <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                        {meta.error}
                      </div>
                    )}
                    {/* <InputGroupAddon addonType="append">%</InputGroupAddon> */}
                  </InputGroup>
                )}
                validate={(value, allValues, meta) => {
                  let indx = parseInt(meta.name.split('.')[1].match('\\d+')[0]);
                  if (value == null) {
                    return 'After Cycle can not be less than 1.';
                  } else {
                    if (value < (values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType !== 'FREE_PRODUCT' ? 1 : 0)) {
                      return `After Cycle can not be less than ${
                        values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType !== 'FREE_PRODUCT' ? 1 : 0
                      }.`;
                    } else if (
                      indx > 0 &&
                      parseInt(allValues.subscriptionPlans[index].appstleCycles[indx - 1]?.afterCycle) >= parseInt(value) &&
                      values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType !== 'FREE_PRODUCT' &&
                      values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType !== 'SHIPPING'
                    ) {
                      return 'After Cycle should be greater than ' + allValues.subscriptionPlans[index].appstleCycles[indx - 1]?.afterCycle;
                    } else if (
                      indx === 0 &&
                      values['subscriptionPlans'][index]?.['discountEnabled2'] &&
                      parseInt(values['subscriptionPlans'][index]?.['afterCycle2']) >= parseInt(value) &&
                      values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType !== 'FREE_PRODUCT' &&
                      values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType !== 'SHIPPING'
                    ) {
                      return 'After Cycle should be greater than ' + parseInt(values['subscriptionPlans'][index]?.['afterCycle2']);
                    } else {
                      return undefined;
                    }
                  }
                }}
                id={`${name}.afterCycle`}
                className="form-control"
                type="number"
                name={`${name}.afterCycle`}
              />
            </FormGroup>
          </Fragment>
        </Col>
        <Col md={3}>
          <Field
            render={({ input, meta }) => (
              <FormGroup>
                <Label>Offer</Label>
                <Input {...input}>
                  <option value="PERCENTAGE">Percent off(%)</option>
                  {idx === 0 && !values['subscriptionPlans'][index]?.['discountEnabled2'] && <option value="FIXED">Amount</option>}
                  <option value="PRICE">Fixed Price</option>
                  <option value="SHIPPING">Shipping Price</option>
                  <option value="FREE_PRODUCT">Free Product</option>
                </Input>
              </FormGroup>
            )}
            width="20px"
            id="discountType"
            className="form-control"
            type="select"
            name={`${name}.discountType`}
          />
        </Col>
        {values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType === 'PERCENTAGE' && (
          <Col md={5} sm={5} xs={16}>
            <Fragment>
              <Label for={`${name}.value`}>Discount Percentage</Label>
              <FormGroup style={{ display: 'flex' }}>
                <Field
                  render={({ input, meta }) => (
                    <InputGroup className="discountAmount">
                      <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                      {meta.error && (
                        <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                          {meta.error}
                        </div>
                      )}
                      <InputGroupAddon addonType='append'>%</InputGroupAddon>
                    </InputGroup>
                  )}
                  validate={value => {
                    if (value == null) {
                      return 'Please provide a valid value for After cycle discount percentage.';
                    } else {
                      if (value < 0) {
                        return 'Please provide a valid value for After cycle discount percentage.';
                      } else {
                        return undefined;
                      }
                    }
                  }}
                  id={`${name}.value`}
                  className="form-control"
                  type="number"
                  name={`${name}.value`}
                />
              </FormGroup>
            </Fragment>
          </Col>
        )}
        {values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType === 'FIXED' && (
          <Col md={5} sm={5} xs={16}>
            <Fragment>
              <Label for={`${name}.value`}>Discount Amount</Label>
              <FormGroup style={{ display: 'flex' }}>
                <Field
                  render={({ input, meta }) => (
                    <InputGroup className="discountAmount">
                      <InputGroupAddon addonType='prepend'>$</InputGroupAddon>
                      <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                      {meta.error && (
                        <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                          {meta.error}
                        </div>
                      )}
                    </InputGroup>
                  )}
                  validate={value => {
                    if (value == null) {
                      return 'Please provide a valid value for After cycle discount amount.';
                    } else {
                      if (value < 0) {
                        return 'Please provide a valid value for After cycle discount amount.';
                      } else {
                        return undefined;
                      }
                    }
                  }}
                  id={`${name}.value`}
                  className="form-control"
                  type="number"
                  name={`${name}.value`}
                />
              </FormGroup>
            </Fragment>
          </Col>
        )}
        {values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType === 'PRICE' && (
          <Col md={5} sm={5} xs={16}>
            <Fragment>
              <Label for={`${name}.value`}>Fixed Price</Label>
              <FormGroup style={{ display: 'flex' }}>
                <Field
                  render={({ input, meta }) => (
                    <InputGroup className="discountAmount">
                      <InputGroupAddon addonType='prepend'>$</InputGroupAddon>
                      <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                      {meta.error && (
                        <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                          {meta.error}
                        </div>
                      )}
                    </InputGroup>
                  )}
                  validate={value => {
                    if (value == null) {
                      return 'Please provide a valid value for After cycle fixed price.';
                    } else {
                      if (value < 0) {
                        return 'Please provide a valid value for After cycle fixed price.';
                      } else {
                        return undefined;
                      }
                    }
                  }}
                  id={`${name}.value`}
                  className="form-control"
                  type="number"
                  name={`${name}.value`}
                />
              </FormGroup>
            </Fragment>
          </Col>
        )}
        {values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType === 'SHIPPING' && (
          <Col md={5} sm={5} xs={16}>
            <Fragment>
              <Label for={`${name}.value`}>Shipping Price</Label>
              <FormGroup style={{ display: 'flex' }}>
                <Field
                  render={({ input, meta }) => (
                    <InputGroup className="discountAmount">
                      <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                      {meta.error && (
                        <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                          {meta.error}
                        </div>
                      )}
                      {/* <InputGroupAddon addonType="append">%</InputGroupAddon> */}
                    </InputGroup>
                  )}
                  validate={value => {
                    if (value == null) {
                      return 'Please provide a valid value for Shipping price.';
                    } else {
                      if (value < 0) {
                        return 'Please provide a valid value for Shipping price.';
                      } else {
                        return undefined;
                      }
                    }
                  }}
                  id={`${name}.value`}
                  className="form-control"
                  type="number"
                  name={`${name}.value`}
                />
              </FormGroup>
            </Fragment>
          </Col>
        )}
        {values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.discountType === 'FREE_PRODUCT' && (
          <Col md={5} sm={5} xs={16}>
            <Field
              render={({ input, meta }) => (
                <FormGroup>
                  <Label>Variant Id</Label>
                  <InputGroup className="freeVariantId">
                    {values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.[`FREE_PRODUCT_DETAILS`] && (
                      <InputGroupAddon addonType='prepend'>
                        <img
                          style={{ height: '38px', borderTopRightRadius: '0px', borderBottomRightRadius: '0px' }}
                          className="form-control"
                          src={JSON.parse(values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.[`FREE_PRODUCT_DETAILS`])?.imageSrc}
                        />
                      </InputGroupAddon>
                    )}
                    <Input {...input} invalid={meta.error && meta.touched ? true : null} />

                    <InputGroupAddon addonType='append'>
                      <Button color="primary" onClick={() => setModal(true)}>
                        Select Product
                      </Button>
                    </InputGroupAddon>
                  </InputGroup>
                  {meta.error && (
                    <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                      {meta.error}
                    </div>
                  )}
                </FormGroup>
              )}
              validate={value => {
                if (value == null) {
                  return 'Please provide a variant id';
                } else {
                  return undefined;
                }
              }}
              id={`${name}.freeVariantId`}
              className="form-control"
              type="text"
              name={`${name}.freeVariantId`}
            />
            <Field
              render={({ input, meta }) => (
                <FormGroup>
                  <InputGroup className="FREE_PRODUCT">
                    <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                  </InputGroup>
                </FormGroup>
              )}
              id={`${name}.FREE_PRODUCT_DETAILS`}
              className="form-control"
              type="hidden"
              name={`${name}.FREE_PRODUCT_DETAILS`}
            />
          </Col>
        )}
        <Col md={1} sm={2}>
          <Label>&nbsp;</Label>
          <FormGroup style={{ display: 'flex', justifyContent: 'space-between' }}>
            <Button
              size="sm"
              className="btn-shadow-primary"
              color="danger"
              style={{ height: '38px', flexGrow: '1' }}
              onClick={() => {
                fields.remove(idx);
              }}
              // disabled={!values['subscriptionPlans'][index]?.discountEnabled2Masked && (idx === 0)}
            >
              Del
            </Button>
          </FormGroup>
        </Col>
      </Row>
      <AddProductModalSingle
        selectedProductVarIds={[{ variantId: values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.[`freeVariantId`],
                                  productHandle: values?.['subscriptionPlans']?.[index]?.appstleCycles[idx]?.[`freeProductHandle`] }]}
        value={JSON.stringify([])}
        onChange={value => {
          console.log(value);
        }}
        totalTitle="Add Product"
        index={1}
        buttonLabel="Add Products"
        header="Select Product"
        addHandler={data => {
          form.mutators.setValue(`subscriptionPlans[${index}].appstleCycles[${idx}].freeVariantId`, data[0]?.variantId);
          form.mutators.setValue(`subscriptionPlans[${index}].appstleCycles[${idx}].FREE_PRODUCT_DETAILS`, JSON.stringify(data[0]));
          form.mutators.setValue(`subscriptionPlans[${index}].appstleCycles[${idx}].freeProductHandle`, data[0]?.productHandle);
        }}
        modal={modal}
        setModal={setModal}
      />
    </>
  );
};

const mapStateToProps = storeState => ({
  subscriptionGroupEntity: storeState.subscriptionGroup.entity,
  usedProductIds: storeState.subscriptionGroup.usedProductIds,
  loading: storeState.subscriptionGroup.loading,
  updating: storeState.subscriptionGroup.updating,
  updateSuccess: storeState.subscriptionGroup.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  getUsedProducts,
  updateEntity,
  createEntity,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(LoyaltyItem);
