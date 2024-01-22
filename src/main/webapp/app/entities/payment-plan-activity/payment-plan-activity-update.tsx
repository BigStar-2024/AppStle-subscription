import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPlanInfo } from 'app/shared/model/plan-info.model';
import { getEntities as getPlanInfos } from 'app/entities/plan-info/plan-info.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './payment-plan-activity.reducer';
import { IPaymentPlanActivity } from 'app/shared/model/payment-plan-activity.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPaymentPlanActivityUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentPlanActivityUpdate = (props: IPaymentPlanActivityUpdateProps) => {
  const [planInfoId, setPlanInfoId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { paymentPlanActivityEntity, planInfos, loading, updating } = props;

  const { additionalDetails, features } = paymentPlanActivityEntity;

  const handleClose = () => {
    props.history.push('/payment-plan-activity' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getPlanInfos();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.activationDate = convertDateTimeToServer(values.activationDate);
    values.lastEmailSentToMerchant = convertDateTimeToServer(values.lastEmailSentToMerchant);
    values.billedDate = convertDateTimeToServer(values.billedDate);
    values.trialEndsOn = convertDateTimeToServer(values.trialEndsOn);

    if (errors.length === 0) {
      const entity = {
        ...paymentPlanActivityEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="subscriptionApp.paymentPlanActivity.home.createOrEditLabel">Create or edit a PaymentPlanActivity</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : paymentPlanActivityEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="payment-plan-activity-id">ID</Label>
                  <AvInput id="payment-plan-activity-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="payment-plan-activity-shop">
                  Shop
                </Label>
                <AvField
                  id="payment-plan-activity-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="chargeActivatedLabel">
                  <AvInput id="payment-plan-activity-chargeActivated" type="checkbox" className="form-check-input" name="chargeActivated" />
                  Charge Activated
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="activationDateLabel" for="payment-plan-activity-activationDate">
                  Activation Date
                </Label>
                <AvInput
                  id="payment-plan-activity-activationDate"
                  type="datetime-local"
                  className="form-control"
                  name="activationDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.paymentPlanActivityEntity.activationDate)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="recurringChargeIdLabel" for="payment-plan-activity-recurringChargeId">
                  Recurring Charge Id
                </Label>
                <AvField id="payment-plan-activity-recurringChargeId" type="string" className="form-control" name="recurringChargeId" />
              </AvGroup>
              <AvGroup>
                <Label id="lastEmailSentToMerchantLabel" for="payment-plan-activity-lastEmailSentToMerchant">
                  Last Email Sent To Merchant
                </Label>
                <AvInput
                  id="payment-plan-activity-lastEmailSentToMerchant"
                  type="datetime-local"
                  className="form-control"
                  name="lastEmailSentToMerchant"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.paymentPlanActivityEntity.lastEmailSentToMerchant)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="numberOfEmailSentToMerchantLabel" for="payment-plan-activity-numberOfEmailSentToMerchant">
                  Number Of Email Sent To Merchant
                </Label>
                <AvField
                  id="payment-plan-activity-numberOfEmailSentToMerchant"
                  type="string"
                  className="form-control"
                  name="numberOfEmailSentToMerchant"
                />
              </AvGroup>
              <AvGroup>
                <Label id="basePlanLabel" for="payment-plan-activity-basePlan">
                  Base Plan
                </Label>
                <AvInput
                  id="payment-plan-activity-basePlan"
                  type="select"
                  className="form-control"
                  name="basePlan"
                  value={(!isNew && paymentPlanActivityEntity.basePlan) || 'FREE'}
                >
                  <option value="FREE">FREE</option>
                  <option value="FREE_ANNUAL">FREE_ANNUAL</option>
                  <option value="STARTER">STARTER</option>
                  <option value="STARTER_ANNUAL">STARTER_ANNUAL</option>
                  <option value="STARTER_PAYG">STARTER_PAYG</option>
                  <option value="BUSINESS">BUSINESS</option>
                  <option value="BUSINESS_ANNUAL">BUSINESS_ANNUAL</option>
                  <option value="ENTERPRISE">ENTERPRISE</option>
                  <option value="ENTERPRISE_ANNUAL">ENTERPRISE_ANNUAL</option>
                  <option value="ENTERPRISE_PLUS">ENTERPRISE_PLUS</option>
                  <option value="ENTERPRISE_PLUS_ANNUAL">ENTERPRISE_PLUS_ANNUAL</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="additionalDetailsLabel" for="payment-plan-activity-additionalDetails">
                  Additional Details
                </Label>
                <AvInput id="payment-plan-activity-additionalDetails" type="textarea" name="additionalDetails" />
              </AvGroup>
              <AvGroup>
                <Label id="trialDaysLabel" for="payment-plan-activity-trialDays">
                  Trial Days
                </Label>
                <AvField id="payment-plan-activity-trialDays" type="string" className="form-control" name="trialDays" />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="payment-plan-activity-price">
                  Price
                </Label>
                <AvField id="payment-plan-activity-price" type="string" className="form-control" name="price" />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="payment-plan-activity-name">
                  Name
                </Label>
                <AvField id="payment-plan-activity-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="planTypeLabel" for="payment-plan-activity-planType">
                  Plan Type
                </Label>
                <AvInput
                  id="payment-plan-activity-planType"
                  type="select"
                  className="form-control"
                  name="planType"
                  value={(!isNew && paymentPlanActivityEntity.planType) || 'MONTHLY'}
                >
                  <option value="MONTHLY">MONTHLY</option>
                  <option value="YEARLY">YEARLY</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="billingTypeLabel" for="payment-plan-activity-billingType">
                  Billing Type
                </Label>
                <AvInput
                  id="payment-plan-activity-billingType"
                  type="select"
                  className="form-control"
                  name="billingType"
                  value={(!isNew && paymentPlanActivityEntity.billingType) || 'SHOPIFY_RECURRING'}
                >
                  <option value="SHOPIFY_RECURRING">SHOPIFY_RECURRING</option>
                  <option value="SHOPIFY_USAGE">SHOPIFY_USAGE</option>
                  <option value="SHOPIFY_ONE_TIME">SHOPIFY_ONE_TIME</option>
                  <option value="SHOPIFY_ANNUAL">SHOPIFY_ANNUAL</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="basedOnLabel" for="payment-plan-activity-basedOn">
                  Based On
                </Label>
                <AvInput
                  id="payment-plan-activity-basedOn"
                  type="select"
                  className="form-control"
                  name="basedOn"
                  value={(!isNew && paymentPlanActivityEntity.basedOn) || 'FIXED_PRICE'}
                >
                  <option value="FIXED_PRICE">FIXED_PRICE</option>
                  <option value="PRODUCT_COUNT">PRODUCT_COUNT</option>
                  <option value="SHOPIFY_PLAN">SHOPIFY_PLAN</option>
                  <option value="PRODUCT_COUNT_AND_SHOPIFY_PLAN">PRODUCT_COUNT_AND_SHOPIFY_PLAN</option>
                  <option value="SUBSCRIPTION_ORDER_AMOUNT">SUBSCRIPTION_ORDER_AMOUNT</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="featuresLabel" for="payment-plan-activity-features">
                  Features
                </Label>
                <AvInput id="payment-plan-activity-features" type="textarea" name="features" />
              </AvGroup>
              <AvGroup check>
                <Label id="testChargeLabel">
                  <AvInput id="payment-plan-activity-testCharge" type="checkbox" className="form-check-input" name="testCharge" />
                  Test Charge
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="billedDateLabel" for="payment-plan-activity-billedDate">
                  Billed Date
                </Label>
                <AvInput
                  id="payment-plan-activity-billedDate"
                  type="datetime-local"
                  className="form-control"
                  name="billedDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.paymentPlanActivityEntity.billedDate)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="validChargeLabel">
                  <AvInput id="payment-plan-activity-validCharge" type="checkbox" className="form-check-input" name="validCharge" />
                  Valid Charge
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="trialEndsOnLabel" for="payment-plan-activity-trialEndsOn">
                  Trial Ends On
                </Label>
                <AvInput
                  id="payment-plan-activity-trialEndsOn"
                  type="datetime-local"
                  className="form-control"
                  name="trialEndsOn"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.paymentPlanActivityEntity.trialEndsOn)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="shopFrozenLabel">
                  <AvInput id="payment-plan-activity-shopFrozen" type="checkbox" className="form-check-input" name="shopFrozen" />
                  Shop Frozen
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="paymentPlanEventLabel" for="payment-plan-activity-paymentPlanEvent">
                  Payment Plan Event
                </Label>
                <AvInput
                  id="payment-plan-activity-paymentPlanEvent"
                  type="select"
                  className="form-control"
                  name="paymentPlanEvent"
                  value={(!isNew && paymentPlanActivityEntity.paymentPlanEvent) || 'APP_UNINSTALLED'}
                >
                  <option value="APP_UNINSTALLED">APP_UNINSTALLED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="payment-plan-activity-planInfo">Plan Info</Label>
                <AvInput id="payment-plan-activity-planInfo" type="select" className="form-control" name="planInfo.id">
                  <option value="" key="0" />
                  {planInfos
                    ? planInfos.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/payment-plan-activity" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  planInfos: storeState.planInfo.entities,
  paymentPlanActivityEntity: storeState.paymentPlanActivity.entity,
  loading: storeState.paymentPlanActivity.loading,
  updating: storeState.paymentPlanActivity.updating,
  updateSuccess: storeState.paymentPlanActivity.updateSuccess
});

const mapDispatchToProps = {
  getPlanInfos,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentPlanActivityUpdate);
