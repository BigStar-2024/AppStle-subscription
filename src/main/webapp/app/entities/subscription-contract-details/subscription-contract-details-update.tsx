import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './subscription-contract-details.reducer';
import { ISubscriptionContractDetails } from 'app/shared/model/subscription-contract-details.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionContractDetailsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractDetailsUpdate = (props: ISubscriptionContractDetailsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionContractDetailsEntity, loading, updating } = props;

  const { contractDetailsJSON, cancellationFeedback, orderNote, orderNoteAttributes } = subscriptionContractDetailsEntity;

  const handleClose = () => {
    props.history.push('/subscription-contract-details' + props.location.search);
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.nextBillingDate = convertDateTimeToServer(values.nextBillingDate);
    values.endsAt = convertDateTimeToServer(values.endsAt);
    values.startsAt = convertDateTimeToServer(values.startsAt);
    values.activatedOn = convertDateTimeToServer(values.activatedOn);
    values.pausedOn = convertDateTimeToServer(values.pausedOn);
    values.cancelledOn = convertDateTimeToServer(values.cancelledOn);

    if (errors.length === 0) {
      const entity = {
        ...subscriptionContractDetailsEntity,
        ...values,
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
          <h2 id="subscriptionApp.subscriptionContractDetails.home.createOrEditLabel">Create or edit a SubscriptionContractDetails</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionContractDetailsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-contract-details-id">ID</Label>
                  <AvInput id="subscription-contract-details-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-contract-details-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-contract-details-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="graphSubscriptionContractIdLabel" for="subscription-contract-details-graphSubscriptionContractId">
                  Graph Subscription Contract Id
                </Label>
                <AvField id="subscription-contract-details-graphSubscriptionContractId" type="text" name="graphSubscriptionContractId" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionContractIdLabel" for="subscription-contract-details-subscriptionContractId">
                  Subscription Contract Id
                </Label>
                <AvField
                  id="subscription-contract-details-subscriptionContractId"
                  type="string"
                  className="form-control"
                  name="subscriptionContractId"
                />
              </AvGroup>
              <AvGroup>
                <Label id="billingPolicyIntervalLabel" for="subscription-contract-details-billingPolicyInterval">
                  Billing Policy Interval
                </Label>
                <AvField id="subscription-contract-details-billingPolicyInterval" type="text" name="billingPolicyInterval" />
              </AvGroup>
              <AvGroup>
                <Label id="billingPolicyIntervalCountLabel" for="subscription-contract-details-billingPolicyIntervalCount">
                  Billing Policy Interval Count
                </Label>
                <AvField
                  id="subscription-contract-details-billingPolicyIntervalCount"
                  type="string"
                  className="form-control"
                  name="billingPolicyIntervalCount"
                />
              </AvGroup>
              <AvGroup>
                <Label id="currencyCodeLabel" for="subscription-contract-details-currencyCode">
                  Currency Code
                </Label>
                <AvField id="subscription-contract-details-currencyCode" type="text" name="currencyCode" />
              </AvGroup>
              <AvGroup>
                <Label id="customerIdLabel" for="subscription-contract-details-customerId">
                  Customer Id
                </Label>
                <AvField id="subscription-contract-details-customerId" type="string" className="form-control" name="customerId" />
              </AvGroup>
              <AvGroup>
                <Label id="graphCustomerIdLabel" for="subscription-contract-details-graphCustomerId">
                  Graph Customer Id
                </Label>
                <AvField id="subscription-contract-details-graphCustomerId" type="text" name="graphCustomerId" />
              </AvGroup>
              <AvGroup>
                <Label id="deliveryPolicyIntervalLabel" for="subscription-contract-details-deliveryPolicyInterval">
                  Delivery Policy Interval
                </Label>
                <AvField id="subscription-contract-details-deliveryPolicyInterval" type="text" name="deliveryPolicyInterval" />
              </AvGroup>
              <AvGroup>
                <Label id="deliveryPolicyIntervalCountLabel" for="subscription-contract-details-deliveryPolicyIntervalCount">
                  Delivery Policy Interval Count
                </Label>
                <AvField
                  id="subscription-contract-details-deliveryPolicyIntervalCount"
                  type="string"
                  className="form-control"
                  name="deliveryPolicyIntervalCount"
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="subscription-contract-details-status">
                  Status
                </Label>
                <AvField id="subscription-contract-details-status" type="text" name="status" />
              </AvGroup>
              <AvGroup>
                <Label id="graphOrderIdLabel" for="subscription-contract-details-graphOrderId">
                  Graph Order Id
                </Label>
                <AvField id="subscription-contract-details-graphOrderId" type="text" name="graphOrderId" />
              </AvGroup>
              <AvGroup>
                <Label id="orderIdLabel" for="subscription-contract-details-orderId">
                  Order Id
                </Label>
                <AvField id="subscription-contract-details-orderId" type="string" className="form-control" name="orderId" />
              </AvGroup>
              <AvGroup>
                <Label id="createdAtLabel" for="subscription-contract-details-createdAt">
                  Created At
                </Label>
                <AvInput
                  id="subscription-contract-details-createdAt"
                  type="datetime-local"
                  className="form-control"
                  name="createdAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.subscriptionContractDetailsEntity.createdAt)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedAtLabel" for="subscription-contract-details-updatedAt">
                  Updated At
                </Label>
                <AvInput
                  id="subscription-contract-details-updatedAt"
                  type="datetime-local"
                  className="form-control"
                  name="updatedAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.subscriptionContractDetailsEntity.updatedAt)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nextBillingDateLabel" for="subscription-contract-details-nextBillingDate">
                  Next Billing Date
                </Label>
                <AvInput
                  id="subscription-contract-details-nextBillingDate"
                  type="datetime-local"
                  className="form-control"
                  name="nextBillingDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={
                    isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.subscriptionContractDetailsEntity.nextBillingDate)
                  }
                />
              </AvGroup>
              <AvGroup>
                <Label id="orderAmountLabel" for="subscription-contract-details-orderAmount">
                  Order Amount
                </Label>
                <AvField id="subscription-contract-details-orderAmount" type="string" className="form-control" name="orderAmount" />
              </AvGroup>
              <AvGroup>
                <Label id="orderNameLabel" for="subscription-contract-details-orderName">
                  Order Name
                </Label>
                <AvField id="subscription-contract-details-orderName" type="text" name="orderName" />
              </AvGroup>
              <AvGroup>
                <Label id="customerNameLabel" for="subscription-contract-details-customerName">
                  Customer Name
                </Label>
                <AvField id="subscription-contract-details-customerName" type="text" name="customerName" />
              </AvGroup>
              <AvGroup>
                <Label id="customerEmailLabel" for="subscription-contract-details-customerEmail">
                  Customer Email
                </Label>
                <AvField id="subscription-contract-details-customerEmail" type="text" name="customerEmail" />
              </AvGroup>
              <AvGroup check>
                <Label id="subscriptionCreatedEmailSentLabel">
                  <AvInput
                    id="subscription-contract-details-subscriptionCreatedEmailSent"
                    type="checkbox"
                    className="form-check-input"
                    name="subscriptionCreatedEmailSent"
                  />
                  Subscription Created Email Sent
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="endsAtLabel" for="subscription-contract-details-endsAt">
                  Ends At
                </Label>
                <AvInput
                  id="subscription-contract-details-endsAt"
                  type="datetime-local"
                  className="form-control"
                  name="endsAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.subscriptionContractDetailsEntity.endsAt)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="startsAtLabel" for="subscription-contract-details-startsAt">
                  Starts At
                </Label>
                <AvInput
                  id="subscription-contract-details-startsAt"
                  type="datetime-local"
                  className="form-control"
                  name="startsAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.subscriptionContractDetailsEntity.startsAt)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionCreatedEmailSentStatusLabel" for="subscription-contract-details-subscriptionCreatedEmailSentStatus">
                  Subscription Created Email Sent Status
                </Label>
                <AvInput
                  id="subscription-contract-details-subscriptionCreatedEmailSentStatus"
                  type="select"
                  className="form-control"
                  name="subscriptionCreatedEmailSentStatus"
                  value={(!isNew && subscriptionContractDetailsEntity.subscriptionCreatedEmailSentStatus) || 'SENT'}
                >
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="EMAIL_SETTINGS_DISABLED">EMAIL_SETTINGS_DISABLED</option>
                  <option value="CUSTOMER_PAYMENT_EMPTY">CUSTOMER_PAYMENT_EMPTY</option>
                  <option value="CONTRACT_PAUSED_STATUS">CONTRACT_PAUSED_STATUS</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="minCyclesLabel" for="subscription-contract-details-minCycles">
                  Min Cycles
                </Label>
                <AvField id="subscription-contract-details-minCycles" type="string" className="form-control" name="minCycles" />
              </AvGroup>
              <AvGroup>
                <Label id="maxCyclesLabel" for="subscription-contract-details-maxCycles">
                  Max Cycles
                </Label>
                <AvField id="subscription-contract-details-maxCycles" type="string" className="form-control" name="maxCycles" />
              </AvGroup>
              <AvGroup>
                <Label id="customerFirstNameLabel" for="subscription-contract-details-customerFirstName">
                  Customer First Name
                </Label>
                <AvField id="subscription-contract-details-customerFirstName" type="text" name="customerFirstName" />
              </AvGroup>
              <AvGroup>
                <Label id="customerLastNameLabel" for="subscription-contract-details-customerLastName">
                  Customer Last Name
                </Label>
                <AvField id="subscription-contract-details-customerLastName" type="text" name="customerLastName" />
              </AvGroup>
              <AvGroup check>
                <Label id="autoChargeLabel">
                  <AvInput id="subscription-contract-details-autoCharge" type="checkbox" className="form-check-input" name="autoCharge" />
                  Auto Charge
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="importedIdLabel" for="subscription-contract-details-importedId">
                  Imported Id
                </Label>
                <AvField id="subscription-contract-details-importedId" type="text" name="importedId" />
              </AvGroup>
              <AvGroup check>
                <Label id="stopUpComingOrderEmailLabel">
                  <AvInput
                    id="subscription-contract-details-stopUpComingOrderEmail"
                    type="checkbox"
                    className="form-check-input"
                    name="stopUpComingOrderEmail"
                  />
                  Stop Up Coming Order Email
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="pausedFromActiveLabel">
                  <AvInput
                    id="subscription-contract-details-pausedFromActive"
                    type="checkbox"
                    className="form-check-input"
                    name="pausedFromActive"
                  />
                  Paused From Active
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionCreatedSmsSentStatusLabel" for="subscription-contract-details-subscriptionCreatedSmsSentStatus">
                  Subscription Created Sms Sent Status
                </Label>
                <AvInput
                  id="subscription-contract-details-subscriptionCreatedSmsSentStatus"
                  type="select"
                  className="form-control"
                  name="subscriptionCreatedSmsSentStatus"
                  value={(!isNew && subscriptionContractDetailsEntity.subscriptionCreatedSmsSentStatus) || 'SENT'}
                >
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="SMS_SETTINGS_DISABLED">SMS_SETTINGS_DISABLED</option>
                  <option value="CUSTOMER_PAYMENT_EMPTY">CUSTOMER_PAYMENT_EMPTY</option>
                  <option value="CONTRACT_PAUSED_STATUS">CONTRACT_PAUSED_STATUS</option>
                  <option value="PHONE_NUMBER_EMPTY">PHONE_NUMBER_EMPTY</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="subscription-contract-details-phone">
                  Phone
                </Label>
                <AvField id="subscription-contract-details-phone" type="text" name="phone" />
              </AvGroup>
              <AvGroup>
                <Label id="activatedOnLabel" for="subscription-contract-details-activatedOn">
                  Activated On
                </Label>
                <AvInput
                  id="subscription-contract-details-activatedOn"
                  type="datetime-local"
                  className="form-control"
                  name="activatedOn"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.subscriptionContractDetailsEntity.activatedOn)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="pausedOnLabel" for="subscription-contract-details-pausedOn">
                  Paused On
                </Label>
                <AvInput
                  id="subscription-contract-details-pausedOn"
                  type="datetime-local"
                  className="form-control"
                  name="pausedOn"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.subscriptionContractDetailsEntity.pausedOn)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cancelledOnLabel" for="subscription-contract-details-cancelledOn">
                  Cancelled On
                </Label>
                <AvInput
                  id="subscription-contract-details-cancelledOn"
                  type="datetime-local"
                  className="form-control"
                  name="cancelledOn"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.subscriptionContractDetailsEntity.cancelledOn)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="contractDetailsJSONLabel" for="subscription-contract-details-contractDetailsJSON">
                  Contract Details JSON
                </Label>
                <AvInput id="subscription-contract-details-contractDetailsJSON" type="textarea" name="contractDetailsJSON" />
              </AvGroup>
              <AvGroup>
                <Label id="cancellationFeedbackLabel" for="subscription-contract-details-cancellationFeedback">
                  Cancellation Feedback
                </Label>
                <AvInput id="subscription-contract-details-cancellationFeedback" type="textarea" name="cancellationFeedback" />
              </AvGroup>
              <AvGroup>
                <Label id="orderNoteLabel" for="subscription-contract-details-orderNote">
                  Order Note
                </Label>
                <AvInput id="subscription-contract-details-orderNote" type="textarea" name="orderNote" />
              </AvGroup>
              <AvGroup>
                <Label id="orderNoteAttributesLabel" for="subscription-contract-details-orderNoteAttributes">
                  Order Note Attributes
                </Label>
                <AvInput id="subscription-contract-details-orderNoteAttributes" type="textarea" name="orderNoteAttributes" />
              </AvGroup>
              <AvGroup check>
                <Label id="allowDeliveryPriceOverrideLabel">
                  <AvInput
                    id="subscription-contract-details-allowDeliveryPriceOverride"
                    type="checkbox"
                    className="form-check-input"
                    name="allowDeliveryPriceOverride"
                  />
                  Allow Delivery Price Override
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="disableFixEmptyQueueLabel">
                  <AvInput
                    id="subscription-contract-details-disableFixEmptyQueue"
                    type="checkbox"
                    className="form-check-input"
                    name="disableFixEmptyQueue"
                  />
                  Disable Fix Empty Queue
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="orderAmountUSDLabel" for="subscription-contract-details-orderAmountUSD">
                  Order Amount USD
                </Label>
                <AvField id="subscription-contract-details-orderAmountUSD" type="string" className="form-control" name="orderAmountUSD" />
              </AvGroup>
              <AvGroup>
                <Label id="originTypeLabel" for="subscription-contract-details-originType">
                  Origin Type
                </Label>
                <AvInput
                  id="subscription-contract-details-originType"
                  type="select"
                  className="form-control"
                  name="originType"
                  value={(!isNew && subscriptionContractDetailsEntity.originType) || 'STORE_FRONT'}
                >
                  <option value="STORE_FRONT">STORE_FRONT</option>
                  <option value="IMPORTED">IMPORTED</option>
                  <option value="SPLIT_ATTEMPT_BILLING">SPLIT_ATTEMPT_BILLING</option>
                  <option value="SPLIT_CONTRACT">SPLIT_CONTRACT</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="originalContractIdLabel" for="subscription-contract-details-originalContractId">
                  Original Contract Id
                </Label>
                <AvField
                  id="subscription-contract-details-originalContractId"
                  type="string"
                  className="form-control"
                  name="originalContractId"
                />
              </AvGroup>
              <AvGroup>
                <Label id="cancellationNoteLabel" for="subscription-contract-details-cancellationNote">
                  Cancellation Note
                </Label>
                <AvField id="subscription-contract-details-cancellationNote" type="text" name="cancellationNote" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionTypeLabel" for="subscription-contract-details-subscriptionType">
                  Subscription Type
                </Label>
                <AvInput
                  id="subscription-contract-details-subscriptionType"
                  type="select"
                  className="form-control"
                  name="subscriptionType"
                  value={(!isNew && subscriptionContractDetailsEntity.subscriptionType) || 'REGULAR_SUBSCRIPTION'}
                >
                  <option value="REGULAR_SUBSCRIPTION">REGULAR_SUBSCRIPTION</option>
                  <option value="BUILD_A_BOX_CLASSIC">BUILD_A_BOX_CLASSIC</option>
                  <option value="BUILD_A_BOX_SINGLE_PRODUCT">BUILD_A_BOX_SINGLE_PRODUCT</option>
                  <option value="BUNDLING_CLASSIC">BUNDLING_CLASSIC</option>
                  <option value="BUNDLING_MIX_AND_MATCH">BUNDLING_MIX_AND_MATCH</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionTypeIdentifierLabel" for="subscription-contract-details-subscriptionTypeIdentifier">
                  Subscription Type Identifier
                </Label>
                <AvField id="subscription-contract-details-subscriptionTypeIdentifier" type="text" name="subscriptionTypeIdentifier" />
              </AvGroup>
              <AvGroup>
                <Label id="upcomingEmailBufferDaysLabel" for="subscription-contract-details-upcomingEmailBufferDays">
                  Upcoming Email Buffer Days
                </Label>
                <AvField
                  id="subscription-contract-details-upcomingEmailBufferDays"
                  type="string"
                  className="form-control"
                  name="upcomingEmailBufferDays"
                />
              </AvGroup>
              <AvGroup>
                <Label id="upcomingEmailTaskUrlLabel" for="subscription-contract-details-upcomingEmailTaskUrl">
                  Upcoming Email Task Url
                </Label>
                <AvField id="subscription-contract-details-upcomingEmailTaskUrl" type="text" name="upcomingEmailTaskUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="contractAmountLabel" for="subscription-contract-details-contractAmount">
                  Contract Amount
                </Label>
                <AvField id="subscription-contract-details-contractAmount" type="string" className="form-control" name="contractAmount" />
              </AvGroup>
              <AvGroup>
                <Label id="contractAmountUSDLabel" for="subscription-contract-details-contractAmountUSD">
                  Contract Amount USD
                </Label>
                <AvField
                  id="subscription-contract-details-contractAmountUSD"
                  type="string"
                  className="form-control"
                  name="contractAmountUSD"
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-contract-details" replace color="info">
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
  subscriptionContractDetailsEntity: storeState.subscriptionContractDetails.entity,
  loading: storeState.subscriptionContractDetails.loading,
  updating: storeState.subscriptionContractDetails.updating,
  updateSuccess: storeState.subscriptionContractDetails.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
//  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractDetailsUpdate);
