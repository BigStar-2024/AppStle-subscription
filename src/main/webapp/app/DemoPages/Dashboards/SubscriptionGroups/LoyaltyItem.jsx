import React, { Fragment } from 'react';
import { Button, Card, CardBody, Col, FormGroup, Input, InputGroup, Label, Row } from 'reactstrap';
import { Field } from 'react-final-form';
import { connect } from 'react-redux';
import { createEntity, getEntity, getUsedProducts, reset, updateEntity } from 'app/entities/subscription-group/subscription-group.reducer';
import { FieldArray } from 'react-final-form-arrays';
import './CreateSubscriptionGroup.scss';
import LoyaltyPlanItem from './LoyaltyPlanItem';

const LoyaltyItem = ({ index, values, name, update, form }) => {
  let addLine;
  return (
    <Card className={`mb-3`} id={`subscriptionPlans${index}`}>
      <CardBody>
        <Row>
          <Col xs={12} sm={12} md={12} lg={12}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h5 style={{ margin: '0' }}>
                  <b>{values['subscriptionPlans'][index]?.['planType'] == 'PAY_AS_YOU_GO' ? 'Pay as You Go Plan ' : 'Prepaid Plan'}</b>
                </h5>
                {(!values['subscriptionPlans'][index]?.['discountEnabled'] ||
                  (values['subscriptionPlans'][index]?.['freeTrialEnabled'] &&
                    !values?.['subscriptionPlans']?.[index]?.appstleCycles.length)) && (
                  <Button
                    onClick={() => {
                      if (!values['subscriptionPlans'][index]?.['discountEnabled']) {
                        update('subscriptionPlans', index, {
                          ...values?.['subscriptionPlans']?.[index],
                          discountEnabled: true
                        });
                      } else {
                        update('subscriptionPlans', index, {
                          ...values?.['subscriptionPlans']?.[index],
                          appstleCycles: [
                            {
                              afterCycle: 1,
                              discountType: 'PERCENTAGE',
                              value: 0
                            }
                          ]
                        });
                      }
                    }}
                    size="lg"
                    className="btn-shadow-primary"
                    color="primary"
                  >
                    Configure Loyalty
                  </Button>
                )}
                {!(
                  !values['subscriptionPlans'][index]?.['discountEnabled'] ||
                  (values['subscriptionPlans'][index]?.['freeTrialEnabled'] &&
                    !values?.['subscriptionPlans']?.[index]?.appstleCycles.length)
                ) && (
                  <Button
                    size="sm"
                    className="btn-shadow-primary mr-1"
                    color="primary"
                    style={{ width: '100px', height: '38px' }}
                    onClick={() => {
                      addLine({
                        afterCycle: 1,
                        discountType: 'PERCENTAGE',
                        value: 0,
                        offer: 'DISCOUNT'
                      });
                    }}
                  >
                    Add
                  </Button>
                )}
              </div>

              <hr />
              <div>
                Frequency Plan Name: <b>{values['subscriptionPlans'][index]?.['frequencyName']}</b>
              </div>
              <div>
                {values['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? 'Fulfillment frequency: ' : 'Order frequency: '}
                <b>
                  {values['subscriptionPlans'][index]?.['frequencyCount'] + ' ' + values['subscriptionPlans'][index]?.['frequencyInterval']}
                </b>
              </div>
              {values['subscriptionPlans'][index]?.['specificDayEnabled'] && (
                <div>
                  Delivery Day:
                  <b>
                    {values['subscriptionPlans'][index]?.['frequencyType'] === 'ON_SPECIFIC_DAY'
                      ? values['subscriptionPlans'][index]?.['specificDayValue']
                      : 'On Purchase Day'}
                  </b>
                </div>
              )}
              {values['subscriptionPlans'][index]?.['billingFrequencyCount'] &&
                values['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' && (
                  <div>
                    Billing frequency:
                    <b>
                      {values['subscriptionPlans'][index]?.['billingFrequencyCount'] +
                        ' ' +
                        values['subscriptionPlans'][index]?.['billingFrequencyInterval']}
                    </b>
                  </div>
                )}

              {Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled']) && (
                <div>
                  Free Trial Enabled:
                  <b>{Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled']) ? ' Yes' : ' No'}</b>
                </div>
              )}

              {Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled']) && (
                <div>
                  Free Trial Period:
                  <b>
                    {values['subscriptionPlans'][index]?.['freeTrialCount']}&nbsp;
                    {values['subscriptionPlans'][index]?.['freeTrialInterval']}
                  </b>
                </div>
              )}

              {!Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled']) && values['subscriptionPlans'][index]?.['discountOffer'] && (
                <div>
                  Discount Enabled:
                  <b>{values['subscriptionPlans'][index]?.['discountEnabled'] ? ' Yes' : ' No'}</b>
                </div>
              )}
              {!Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled']) &&
                values['subscriptionPlans'][index]?.['discountEnabled'] &&
                values['subscriptionPlans'][index]?.['discountOffer'] && (
                  <div>
                    {values['subscriptionPlans'][index]?.['discountType'] === 'FIXED' ? 'Amount OFF: ' : 'Discount: '}
                    <b>
                      {(values['subscriptionPlans'][index]?.['discountType'] === 'FIXED' ? '' : '') +
                        values['subscriptionPlans'][index]?.['discountOffer'] +
                        (values['subscriptionPlans'][index]?.['discountType'] === 'PERCENTAGE' ? '%' : '')}
                    </b>
                  </div>
                )}
            </div>
            {(values['subscriptionPlans'][index]?.['discountEnabled'] &&
              !Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled'])) ||
            values?.['subscriptionPlans']?.[index]?.appstleCycles.length ? (
              <Fragment>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '30px' }}>
                  <h5 style={{ margin: '0' }}>
                    <b>Loyalty Discount</b>
                  </h5>
                </div>
                <hr />
              </Fragment>
            ) : (
              ``
            )}
            {values['subscriptionPlans'][index]?.['discountEnabled'] && !Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled']) && (
              <Row>
                <Col md={6} sm={6} xs={24}>
                  <Fragment>
                    <Label for={`${name}.discountOffer`}>Initial Discount Offer</Label>
                    <FormGroup style={{ display: 'flex' }}>
                      <Field
                        render={({ input, meta }) => (
                          <InputGroup className="discountAmount">
                            <Input
                              {...input}
                              invalid={meta.error && meta.touched ? true : null}
                              disabled={Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled'])}
                            />
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
                            return 'Please provide a valid value for discount.';
                          } else {
                            if (value < 0) {
                              return 'Please provide a valid value for discount.';
                            } else {
                              return undefined;
                            }
                          }
                        }}
                        initialValue={false}
                        id={`${name}.discountOffer`}
                        className="form-control"
                        type="number"
                        name={`${name}.discountOffer`}
                      />
                    </FormGroup>
                  </Fragment>
                </Col>
                <Col md={5} sm={2}>
                  <Label>&nbsp;</Label>
                  <Field
                    render={({ input, meta }) => (
                      <Input {...input} disabled={Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled'])}>
                        <option value="PERCENTAGE">Percent off(%)</option>
                        <option value="FIXED">Amount off</option>
                        <option value="PRICE">Fixed Price</option>
                      </Input>
                    )}
                    initialValue="PERCENTAGE"
                    validate={value => undefined}
                    width="20px"
                    id="discountType"
                    className="form-control"
                    type="select"
                    name={`${name}.discountType`}
                  />
                </Col>
                <Col md={1} sm={2}>
                  <Label>&nbsp;</Label>
                  <FormGroup style={{ display: 'flex', justifyContent: 'space-between' }}>
                    {/* <Button size="sm" className="btn-shadow-primary mr-1" color="primary" style={{width: "48%", height: "38px"}} onClick={() => addLine({
                        afterCycle: 1,
                        discountType: "PERCENTAGE",
                        value: 0
                        })}>
                            Add
                            </Button> */}
                    <Button
                      size="sm"
                      className="btn-shadow-primary"
                      color="danger"
                      style={{ height: '38px', flexGrow: '1' }}
                      disabled={Boolean(values['subscriptionPlans'][index]?.['discountEnabledMasked'])}
                      onClick={() => {
                        update('subscriptionPlans', index, {
                          ...values?.['subscriptionPlans']?.[index],
                          discountEnabled: false
                        });
                      }}
                    >
                      Del
                    </Button>
                  </FormGroup>
                </Col>
              </Row>
            )}
            {values['subscriptionPlans'][index]?.['discountEnabled2'] &&
              !Boolean(values['subscriptionPlans'][index]?.['freeTrialEnabled']) && (
                <Row>
                  <Col md={3} sm={3} xs={8}>
                    <Fragment>
                      <Label for={`${name}.afterCycle2`}>After Billing Cycle(s)</Label>
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
                            </InputGroup>
                          )}
                          validate={value => {
                            if (value == null) {
                              return 'Second After Cycle can not be less than 1.';
                            } else {
                              if (value < 1) {
                                return 'Second After Cycle can not be less than 1.';
                              } else {
                                return undefined;
                              }
                            }
                          }}
                          initialValue={'1'}
                          id={`${name}.afterCycle2`}
                          className="form-control"
                          type="number"
                          name={`${name}.afterCycle2`}
                        />
                      </FormGroup>
                    </Fragment>
                  </Col>

                  <Col md={3} sm={6} xs={16}>
                    <Fragment>
                      <Label for={`${name}.discountOffer2`}>Offer</Label>
                      <FormGroup style={{ display: 'flex' }}>
                        <Field
                          render={({ input, meta }) => (
                            <Input {...input}>
                              <option value="PERCENTAGE">Percent off(%)</option>
                              <option value="FIXED">Amount off</option>
                              <option value="PRICE">Fixed Price</option>
                            </Input>
                          )}
                          initialValue="PERCENTAGE"
                          width="20px"
                          id="discountType2"
                          className="form-control"
                          type="select"
                          name={`${name}.discountType2`}
                        />
                      </FormGroup>
                    </Fragment>
                  </Col>
                  <Col md={5}>
                    <Label>&nbsp;</Label>
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
                      initialValue={'0'}
                      id={`${name}.discountOffer2`}
                      className="form-control"
                      type="number"
                      name={`${name}.discountOffer2`}
                    />
                  </Col>

                  <Col md={1} sm={2}>
                    <Label>&nbsp;</Label>
                    <FormGroup style={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Button
                        size="sm"
                        className="btn-shadow-primary"
                        color="danger"
                        style={{ height: '38px', flexGrow: '1' }}
                        onClick={() => {
                          update('subscriptionPlans', index, {
                            ...values?.['subscriptionPlans']?.[index],
                            discountEnabled2: false
                          });
                        }}
                      >
                        Del
                      </Button>
                    </FormGroup>
                  </Col>
                </Row>
              )}
            <FieldArray name={`${name}.appstleCycles`}>
              {({ fields }) => {
                addLine = fields.push;
                return fields.map((name, idx) => (
                  <LoyaltyPlanItem name={name} index={index} idx={idx} values={values} fields={fields} form={form} />
                ));
              }}
            </FieldArray>
          </Col>
        </Row>
      </CardBody>
    </Card>
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
