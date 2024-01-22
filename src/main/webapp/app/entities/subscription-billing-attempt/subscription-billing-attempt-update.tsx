import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './subscription-billing-attempt.reducer';
import { ISubscriptionBillingAttempt } from 'app/shared/model/subscription-billing-attempt.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionBillingAttemptUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionBillingAttemptUpdate = (props: ISubscriptionBillingAttemptUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionBillingAttemptEntity, loading, updating } = props;

  const { billingAttemptResponseMessage, orderNote } = subscriptionBillingAttemptEntity;

  const handleClose = () => {
    props.history.push('/subscription-billing-attempt');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
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
    values.billingDate = convertDateTimeToServer(values.billingDate);
    values.attemptTime = convertDateTimeToServer(values.attemptTime);

    if (errors.length === 0) {
      const entity = {
        ...subscriptionBillingAttemptEntity,
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
          <h2 id="subscriptionApp.subscriptionBillingAttempt.home.createOrEditLabel">Create or edit a SubscriptionBillingAttempt</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionBillingAttemptEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-billing-attempt-id">ID</Label>
                  <AvInput id="subscription-billing-attempt-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-billing-attempt-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-billing-attempt-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="billingAttemptIdLabel" for="subscription-billing-attempt-billingAttemptId">
                  Billing Attempt Id
                </Label>
                <AvField id="subscription-billing-attempt-billingAttemptId" type="text" name="billingAttemptId" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="subscription-billing-attempt-status">
                  Status
                </Label>
                <AvInput
                  id="subscription-billing-attempt-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && subscriptionBillingAttemptEntity.status) || 'SUCCESS'}
                >
                  <option value="SUCCESS">SUCCESS</option>
                  <option value="FAILURE">FAILURE</option>
                  <option value="REQUESTING">REQUESTING</option>
                  <option value="PROGRESS">PROGRESS</option>
                  <option value="QUEUED">QUEUED</option>
                  <option value="SKIPPED">SKIPPED</option>
                  <option value="SOCIAL_CONNECTION_NULL">SOCIAL_CONNECTION_NULL</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                  <option value="CONTRACT_ENDED">CONTRACT_ENDED</option>
                  <option value="CONTRACT_PAUSED">CONTRACT_PAUSED</option>
                  <option value="AUTO_CHARGE_DISABLED">AUTO_CHARGE_DISABLED</option>
                  <option value="SHOPIFY_EXCEPTION">SHOPIFY_EXCEPTION</option>
                  <option value="SKIPPED_DUNNING_MGMT">SKIPPED_DUNNING_MGMT</option>
                  <option value="SKIPPED_INVENTORY_MGMT">SKIPPED_INVENTORY_MGMT</option>
                  <option value="IMMEDIATE_TRIGGERED">IMMEDIATE_TRIGGERED</option>
                  <option value="SECURITY_CHALLENGE">SECURITY_CHALLENGE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="billingDateLabel" for="subscription-billing-attempt-billingDate">
                  Billing Date
                </Label>
                <AvInput
                  id="subscription-billing-attempt-billingDate"
                  type="datetime-local"
                  className="form-control"
                  name="billingDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.subscriptionBillingAttemptEntity.billingDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="contractIdLabel" for="subscription-billing-attempt-contractId">
                  Contract Id
                </Label>
                <AvField id="subscription-billing-attempt-contractId" type="string" className="form-control" name="contractId" />
              </AvGroup>
              <AvGroup>
                <Label id="attemptCountLabel" for="subscription-billing-attempt-attemptCount">
                  Attempt Count
                </Label>
                <AvField id="subscription-billing-attempt-attemptCount" type="string" className="form-control" name="attemptCount" />
              </AvGroup>
              <AvGroup>
                <Label id="attemptTimeLabel" for="subscription-billing-attempt-attemptTime">
                  Attempt Time
                </Label>
                <AvInput
                  id="subscription-billing-attempt-attemptTime"
                  type="datetime-local"
                  className="form-control"
                  name="attemptTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.subscriptionBillingAttemptEntity.attemptTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="graphOrderIdLabel" for="subscription-billing-attempt-graphOrderId">
                  Graph Order Id
                </Label>
                <AvField id="subscription-billing-attempt-graphOrderId" type="text" name="graphOrderId" />
              </AvGroup>
              <AvGroup>
                <Label id="orderIdLabel" for="subscription-billing-attempt-orderId">
                  Order Id
                </Label>
                <AvField id="subscription-billing-attempt-orderId" type="string" className="form-control" name="orderId" />
              </AvGroup>
              <AvGroup>
                <Label id="orderAmountLabel" for="subscription-billing-attempt-orderAmount">
                  Order Amount
                </Label>
                <AvField id="subscription-billing-attempt-orderAmount" type="string" className="form-control" name="orderAmount" />
              </AvGroup>
              <AvGroup>
                <Label id="orderNameLabel" for="subscription-billing-attempt-orderName">
                  Order Name
                </Label>
                <AvField id="subscription-billing-attempt-orderName" type="text" name="orderName" />
              </AvGroup>
              <AvGroup check>
                <Label id="retryingNeededLabel">
                  <AvInput
                    id="subscription-billing-attempt-retryingNeeded"
                    type="checkbox"
                    className="form-check-input"
                    name="retryingNeeded"
                  />
                  Retrying Needed
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="transactionFailedEmailSentStatusLabel" for="subscription-billing-attempt-transactionFailedEmailSentStatus">
                  Transaction Failed Email Sent Status
                </Label>
                <AvInput
                  id="subscription-billing-attempt-transactionFailedEmailSentStatus"
                  type="select"
                  className="form-control"
                  name="transactionFailedEmailSentStatus"
                  value={(!isNew && subscriptionBillingAttemptEntity.transactionFailedEmailSentStatus) || 'SENT'}
                >
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="EMAIL_SETTINGS_DISABLED">EMAIL_SETTINGS_DISABLED</option>
                  <option value="CUSTOMER_PAYMENT_EMPTY">CUSTOMER_PAYMENT_EMPTY</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="upcomingOrderEmailSentStatusLabel" for="subscription-billing-attempt-upcomingOrderEmailSentStatus">
                  Upcoming Order Email Sent Status
                </Label>
                <AvInput
                  id="subscription-billing-attempt-upcomingOrderEmailSentStatus"
                  type="select"
                  className="form-control"
                  name="upcomingOrderEmailSentStatus"
                  value={(!isNew && subscriptionBillingAttemptEntity.upcomingOrderEmailSentStatus) || 'SENT'}
                >
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="EMAIL_SETTINGS_DISABLED">EMAIL_SETTINGS_DISABLED</option>
                  <option value="CUSTOMER_PAYMENT_EMPTY">CUSTOMER_PAYMENT_EMPTY</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                  <option value="STOP_FROM_CONTRACT">STOP_FROM_CONTRACT</option>
                  <option value="CONTRACT_PAUSED">CONTRACT_PAUSED</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="applyUsageChargeLabel">
                  <AvInput
                    id="subscription-billing-attempt-applyUsageCharge"
                    type="checkbox"
                    className="form-check-input"
                    name="applyUsageCharge"
                  />
                  Apply Usage Charge
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="recurringChargeIdLabel" for="subscription-billing-attempt-recurringChargeId">
                  Recurring Charge Id
                </Label>
                <AvField
                  id="subscription-billing-attempt-recurringChargeId"
                  type="string"
                  className="form-control"
                  name="recurringChargeId"
                />
              </AvGroup>
              <AvGroup>
                <Label id="transactionRateLabel" for="subscription-billing-attempt-transactionRate">
                  Transaction Rate
                </Label>
                <AvField id="subscription-billing-attempt-transactionRate" type="string" className="form-control" name="transactionRate" />
              </AvGroup>
              <AvGroup>
                <Label id="usageChargeStatusLabel" for="subscription-billing-attempt-usageChargeStatus">
                  Usage Charge Status
                </Label>
                <AvInput
                  id="subscription-billing-attempt-usageChargeStatus"
                  type="select"
                  className="form-control"
                  name="usageChargeStatus"
                  value={(!isNew && subscriptionBillingAttemptEntity.usageChargeStatus) || 'SUCCESS'}
                >
                  <option value="SUCCESS">SUCCESS</option>
                  <option value="FAILED">FAILED</option>
                  <option value="TO_BE_TRIED">TO_BE_TRIED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="transactionFailedSmsSentStatusLabel" for="subscription-billing-attempt-transactionFailedSmsSentStatus">
                  Transaction Failed Sms Sent Status
                </Label>
                <AvInput
                  id="subscription-billing-attempt-transactionFailedSmsSentStatus"
                  type="select"
                  className="form-control"
                  name="transactionFailedSmsSentStatus"
                  value={(!isNew && subscriptionBillingAttemptEntity.transactionFailedSmsSentStatus) || 'SENT'}
                >
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="SMS_SETTINGS_DISABLED">SMS_SETTINGS_DISABLED</option>
                  <option value="CUSTOMER_PAYMENT_EMPTY">CUSTOMER_PAYMENT_EMPTY</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                  <option value="PHONE_NUMBER_EMPTY">PHONE_NUMBER_EMPTY</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="upcomingOrderSmsSentStatusLabel" for="subscription-billing-attempt-upcomingOrderSmsSentStatus">
                  Upcoming Order Sms Sent Status
                </Label>
                <AvInput
                  id="subscription-billing-attempt-upcomingOrderSmsSentStatus"
                  type="select"
                  className="form-control"
                  name="upcomingOrderSmsSentStatus"
                  value={(!isNew && subscriptionBillingAttemptEntity.upcomingOrderSmsSentStatus) || 'SENT'}
                >
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="SMS_SETTINGS_DISABLED">SMS_SETTINGS_DISABLED</option>
                  <option value="CUSTOMER_PAYMENT_EMPTY">CUSTOMER_PAYMENT_EMPTY</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                  <option value="STOP_FROM_CONTRACT">STOP_FROM_CONTRACT</option>
                  <option value="CONTRACT_PAUSED">CONTRACT_PAUSED</option>
                  <option value="PHONE_NUMBER_EMPTY">PHONE_NUMBER_EMPTY</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="billingAttemptResponseMessageLabel" for="subscription-billing-attempt-billingAttemptResponseMessage">
                  Billing Attempt Response Message
                </Label>
                <AvInput
                  id="subscription-billing-attempt-billingAttemptResponseMessage"
                  type="textarea"
                  name="billingAttemptResponseMessage"
                />
              </AvGroup>
              <AvGroup>
                <Label id="progressAttemptCountLabel" for="subscription-billing-attempt-progressAttemptCount">
                  Progress Attempt Count
                </Label>
                <AvField
                  id="subscription-billing-attempt-progressAttemptCount"
                  type="string"
                  className="form-control"
                  name="progressAttemptCount"
                />
              </AvGroup>
              <AvGroup>
                <Label id="orderNoteLabel" for="subscription-billing-attempt-orderNote">
                  Order Note
                </Label>
                <AvInput id="subscription-billing-attempt-orderNote" type="textarea" name="orderNote" />
              </AvGroup>
              <AvGroup>
                <Label id="securityChallengeSentStatusLabel" for="subscription-billing-attempt-securityChallengeSentStatus">
                  Security Challenge Sent Status
                </Label>
                <AvInput
                  id="subscription-billing-attempt-securityChallengeSentStatus"
                  type="select"
                  className="form-control"
                  name="securityChallengeSentStatus"
                  value={(!isNew && subscriptionBillingAttemptEntity.securityChallengeSentStatus) || 'SENT'}
                >
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="EMAIL_SETTINGS_DISABLED">EMAIL_SETTINGS_DISABLED</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="orderAmountUSDLabel" for="subscription-billing-attempt-orderAmountUSD">
                  Order Amount USD
                </Label>
                <AvField id="subscription-billing-attempt-orderAmountUSD" type="string" className="form-control" name="orderAmountUSD" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-billing-attempt" replace color="info">
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
  subscriptionBillingAttemptEntity: storeState.subscriptionBillingAttempt.entity,
  loading: storeState.subscriptionBillingAttempt.loading,
  updating: storeState.subscriptionBillingAttempt.updating,
  updateSuccess: storeState.subscriptionBillingAttempt.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
//  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBillingAttemptUpdate);
