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
import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './payment-plan.reducer';
import { IPaymentPlan } from 'app/shared/model/payment-plan.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPaymentPlanUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentPlanUpdate = (props: IPaymentPlanUpdateProps) => {
  const [planInfoId, setPlanInfoId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { paymentPlanEntity, planInfos, loading, updating } = props;

  const { additionalDetails, features } = paymentPlanEntity;

  const handleClose = () => {
    props.history.push('/admin/payment-plan');
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
//    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
//    props.setBlob(name, undefined, undefined);
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
        ...paymentPlanEntity,
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
          <h2 id="subscriptionApp.paymentPlan.home.createOrEditLabel">Create or edit a PaymentPlan</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : paymentPlanEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="payment-plan-id">ID</Label>
                  <AvInput id="payment-plan-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="payment-plan-shop">
                  Shop
                </Label>
                <AvField
                  id="payment-plan-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="chargeActivatedLabel">
                  <AvInput id="payment-plan-chargeActivated" type="checkbox" className="form-check-input" name="chargeActivated" />
                  Charge Activated
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="activationDateLabel" for="payment-plan-activationDate">
                  Activation Date
                </Label>
                <AvInput
                  id="payment-plan-activationDate"
                  type="datetime-local"
                  className="form-control"
                  name="activationDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.paymentPlanEntity.activationDate)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="recurringChargeIdLabel" for="payment-plan-recurringChargeId">
                  Recurring Charge Id
                </Label>
                <AvField id="payment-plan-recurringChargeId" type="string" className="form-control" name="recurringChargeId" />
              </AvGroup>
              <AvGroup>
                <Label id="lastEmailSentToMerchantLabel" for="payment-plan-lastEmailSentToMerchant">
                  Last Email Sent To Merchant
                </Label>
                <AvInput
                  id="payment-plan-lastEmailSentToMerchant"
                  type="datetime-local"
                  className="form-control"
                  name="lastEmailSentToMerchant"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.paymentPlanEntity.lastEmailSentToMerchant)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="numberOfEmailSentToMerchantLabel" for="payment-plan-numberOfEmailSentToMerchant">
                  Number Of Email Sent To Merchant
                </Label>
                <AvField
                  id="payment-plan-numberOfEmailSentToMerchant"
                  type="string"
                  className="form-control"
                  name="numberOfEmailSentToMerchant"
                />
              </AvGroup>
              <AvGroup>
                <Label id="basePlanLabel" for="payment-plan-basePlan">
                  Base Plan
                </Label>
                <AvInput
                  id="payment-plan-basePlan"
                  type="select"
                  className="form-control"
                  name="basePlan"
                  value={(!isNew && paymentPlanEntity.basePlan) || 'FREE'}
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
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="additionalDetailsLabel" for="payment-plan-additionalDetails">
                  Additional Details
                </Label>
                <AvInput id="payment-plan-additionalDetails" type="textarea" name="additionalDetails" />
              </AvGroup>
              <AvGroup>
                <Label id="trialDaysLabel" for="payment-plan-trialDays">
                  Trial Days
                </Label>
                <AvField id="payment-plan-trialDays" type="string" className="form-control" name="trialDays" />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="payment-plan-price">
                  Price
                </Label>
                <AvField id="payment-plan-price" type="string" className="form-control" name="price" />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="payment-plan-name">
                  Name
                </Label>
                <AvField id="payment-plan-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="planTypeLabel" for="payment-plan-planType">
                  Plan Type
                </Label>
                <AvInput
                  id="payment-plan-planType"
                  type="select"
                  className="form-control"
                  name="planType"
                  value={(!isNew && paymentPlanEntity.planType) || 'MONTHLY'}
                >
                  <option value="MONTHLY">MONTHLY</option>
                  <option value="YEARLY">YEARLY</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="billingTypeLabel" for="payment-plan-billingType">
                  Billing Type
                </Label>
                <AvInput
                  id="payment-plan-billingType"
                  type="select"
                  className="form-control"
                  name="billingType"
                  value={(!isNew && paymentPlanEntity.billingType) || 'SHOPIFY_RECURRING'}
                >
                  <option value="SHOPIFY_RECURRING">SHOPIFY_RECURRING</option>
                  <option value="SHOPIFY_USAGE">SHOPIFY_USAGE</option>
                  <option value="SHOPIFY_ONE_TIME">SHOPIFY_ONE_TIME</option>
                  <option value="SHOPIFY_ANNUAL">SHOPIFY_ANNUAL</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="basedOnLabel" for="payment-plan-basedOn">
                  Based On
                </Label>
                <AvInput
                  id="payment-plan-basedOn"
                  type="select"
                  className="form-control"
                  name="basedOn"
                  value={(!isNew && paymentPlanEntity.basedOn) || 'FIXED_PRICE'}
                >
                  <option value="FIXED_PRICE">FIXED_PRICE</option>
                  <option value="PRODUCT_COUNT">PRODUCT_COUNT</option>
                  <option value="SHOPIFY_PLAN">SHOPIFY_PLAN</option>
                  <option value="PRODUCT_COUNT_AND_SHOPIFY_PLAN">PRODUCT_COUNT_AND_SHOPIFY_PLAN</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="featuresLabel" for="payment-plan-features">
                  Features
                </Label>
                <AvInput id="payment-plan-features" type="textarea" name="features" />
              </AvGroup>
              <AvGroup check>
                <Label id="testChargeLabel">
                  <AvInput id="payment-plan-testCharge" type="checkbox" className="form-check-input" name="testCharge" />
                  Test Charge
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="billedDateLabel" for="payment-plan-billedDate">
                  Billed Date
                </Label>
                <AvInput
                  id="payment-plan-billedDate"
                  type="datetime-local"
                  className="form-control"
                  name="billedDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.paymentPlanEntity.billedDate)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="validChargeLabel">
                  <AvInput id="payment-plan-validCharge" type="checkbox" className="form-check-input" name="validCharge" />
                  Valid Charge
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="trialEndsOnLabel" for="payment-plan-trialEndsOn">
                  Trial Ends On
                </Label>
                <AvInput
                  id="payment-plan-trialEndsOn"
                  type="datetime-local"
                  className="form-control"
                  name="trialEndsOn"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.paymentPlanEntity.trialEndsOn)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="payment-plan-planInfo">Plan Info</Label>
                <AvInput id="payment-plan-planInfo" type="select" className="form-control" name="planInfo.id">
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
              <Button tag={Link} id="cancel-save" to="/admin/payment-plan" replace color="info">
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
  paymentPlanEntity: storeState.paymentPlan.entity,
  loading: storeState.paymentPlan.loading,
  updating: storeState.paymentPlan.updating,
  updateSuccess: storeState.paymentPlan.updateSuccess
});

const mapDispatchToProps = {
  getPlanInfos,
  getEntity,
  updateEntity,
//  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentPlanUpdate);
