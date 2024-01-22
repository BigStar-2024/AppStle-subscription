import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './activity-log.reducer';
import { IActivityLog } from 'app/shared/model/activity-log.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IActivityLogUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActivityLogUpdate = (props: IActivityLogUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { activityLogEntity, loading, updating } = props;

  const { additionalInfo } = activityLogEntity;

  const handleClose = () => {
    props.history.push('/activity-log' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
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
    values.createAt = convertDateTimeToServer(values.createAt);

    if (errors.length === 0) {
      const entity = {
        ...activityLogEntity,
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
          <h2 id="subscriptionApp.activityLog.home.createOrEditLabel">Create or edit a ActivityLog</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : activityLogEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="activity-log-id">ID</Label>
                  <AvInput id="activity-log-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="activity-log-shop">
                  Shop
                </Label>
                <AvField id="activity-log-shop" type="text" name="shop" />
              </AvGroup>
              <AvGroup>
                <Label id="entityIdLabel" for="activity-log-entityId">
                  Entity Id
                </Label>
                <AvField id="activity-log-entityId" type="string" className="form-control" name="entityId" />
              </AvGroup>
              <AvGroup>
                <Label id="entityTypeLabel" for="activity-log-entityType">
                  Entity Type
                </Label>
                <AvInput
                  id="activity-log-entityType"
                  type="select"
                  className="form-control"
                  name="entityType"
                  value={(!isNew && activityLogEntity.entityType) || 'SUBSCRIPTION_BILLING_ATTEMPT'}
                >
                  <option value="SUBSCRIPTION_BILLING_ATTEMPT">SUBSCRIPTION_BILLING_ATTEMPT</option>
                  <option value="SUBSCRIPTION_CONTRACT_DETAILS">SUBSCRIPTION_CONTRACT_DETAILS</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="eventSourceLabel" for="activity-log-eventSource">
                  Event Source
                </Label>
                <AvInput
                  id="activity-log-eventSource"
                  type="select"
                  className="form-control"
                  name="eventSource"
                  value={(!isNew && activityLogEntity.eventSource) || 'CUSTOMER_PORTAL'}
                >
                  <option value="CUSTOMER_PORTAL">CUSTOMER_PORTAL</option>
                  <option value="MERCHANT_PORTAL">MERCHANT_PORTAL</option>
                  <option value="SHOPIFY_EVENT">SHOPIFY_EVENT</option>
                  <option value="SYSTEM_EVENT">SYSTEM_EVENT</option>
                  <option value="MERCHANT_PORTAL_BULK_AUTOMATION">MERCHANT_PORTAL_BULK_AUTOMATION</option>
                  <option value="MERCHANT_EXTERNAL_API">MERCHANT_EXTERNAL_API</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="eventTypeLabel" for="activity-log-eventType">
                  Event Type
                </Label>
                <AvInput
                  id="activity-log-eventType"
                  type="select"
                  className="form-control"
                  name="eventType"
                  value={(!isNew && activityLogEntity.eventType) || 'NEXT_BILLING_DATE_CHANGE'}
                >
                  <option value="NEXT_BILLING_DATE_CHANGE">NEXT_BILLING_DATE_CHANGE</option>
                  <option value="NEXT_BILLING_TIME_CHANGE">NEXT_BILLING_TIME_CHANGE</option>
                  <option value="BILLING_INTERVAL_CHANGE">BILLING_INTERVAL_CHANGE</option>
                  <option value="DELIVERY_INTERVAL_CHANGE">DELIVERY_INTERVAL_CHANGE</option>
                  <option value="BILLING_ATTEMPT_TRIGGERED">BILLING_ATTEMPT_TRIGGERED</option>
                  <option value="BILLING_ATTEMPT_SKIPPED">BILLING_ATTEMPT_SKIPPED</option>
                  <option value="PRODUCT_ADD">PRODUCT_ADD</option>
                  <option value="PRODUCT_REMOVE">PRODUCT_REMOVE</option>
                  <option value="PRODUCT_REPLACE">PRODUCT_REPLACE</option>
                  <option value="PRODUCT_QUANTITY_CHANGE">PRODUCT_QUANTITY_CHANGE</option>
                  <option value="PRODUCT_PRICE_CHANGE">PRODUCT_PRICE_CHANGE</option>
                  <option value="PRODUCT_PRICING_POLICY_CHANGE">PRODUCT_PRICING_POLICY_CHANGE</option>
                  <option value="PRODUCT_SELLING_PLAN_CHANGE">PRODUCT_SELLING_PLAN_CHANGE</option>
                  <option value="CONTRACT_PAUSED">CONTRACT_PAUSED</option>
                  <option value="CONTRACT_ACTIVATED">CONTRACT_ACTIVATED</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                  <option value="DELIVERY_METHOD_UPDATED">DELIVERY_METHOD_UPDATED</option>
                  <option value="SYSTEM_UPDATED_DELIVERY_PRICE">SYSTEM_UPDATED_DELIVERY_PRICE</option>
                  <option value="MANUAL_DELIVERY_PRICE_UPDATED">MANUAL_DELIVERY_PRICE_UPDATED</option>
                  <option value="DELIVERY_PRICE_OVERRIDE_CHANGED">DELIVERY_PRICE_OVERRIDE_CHANGED</option>
                  <option value="SHIPPING_ADDRESS_CHANGE">SHIPPING_ADDRESS_CHANGE</option>
                  <option value="SEND_SUBSCRIPTION_CREATED_EMAIL">SEND_SUBSCRIPTION_CREATED_EMAIL</option>
                  <option value="SEND_TRANSACTION_FAILED_EMAIL">SEND_TRANSACTION_FAILED_EMAIL</option>
                  <option value="SEND_UPCOMING_ORDER_EMAIL">SEND_UPCOMING_ORDER_EMAIL</option>
                  <option value="SEND_EXPIRING_CREDIT_CARD_EMAIL">SEND_EXPIRING_CREDIT_CARD_EMAIL</option>
                  <option value="SEND_SHIPPING_ADDRESS_UPDATED_EMAIL">SEND_SHIPPING_ADDRESS_UPDATED_EMAIL</option>
                  <option value="SEND_ORDER_FREQUENCY_UPDATED_EMAIL">SEND_ORDER_FREQUENCY_UPDATED_EMAIL</option>
                  <option value="SEND_NEXT_ORDER_DATE_UPDATED_EMAIL">SEND_NEXT_ORDER_DATE_UPDATED_EMAIL</option>
                  <option value="SEND_SUBSCRIPTION_PAUSED_EMAIL">SEND_SUBSCRIPTION_PAUSED_EMAIL</option>
                  <option value="SEND_SUBSCRIPTION_CANCELED_EMAIL">SEND_SUBSCRIPTION_CANCELED_EMAIL</option>
                  <option value="SEND_SUBSCRIPTION_RESUMED_EMAIL">SEND_SUBSCRIPTION_RESUMED_EMAIL</option>
                  <option value="SEND_SUBSCRIPTION_PRODUCT_ADDED_EMAIL">SEND_SUBSCRIPTION_PRODUCT_ADDED_EMAIL</option>
                  <option value="SEND_SUBSCRIPTION_PRODUCT_REMOVED_EMAIL">SEND_SUBSCRIPTION_PRODUCT_REMOVED_EMAIL</option>
                  <option value="SEND_SUBSCRIPTION_PRODUCT_REPLACED_EMAIL">SEND_SUBSCRIPTION_PRODUCT_REPLACED_EMAIL</option>
                  <option value="ONE_TIME_PURCHASE_PRODUCT_ADDED">ONE_TIME_PURCHASE_PRODUCT_ADDED</option>
                  <option value="ONE_TIME_PURCHASE_PRODUCT_REMOVED">ONE_TIME_PURCHASE_PRODUCT_REMOVED</option>
                  <option value="ONE_TIME_PURCHASE_PRODUCT_UPDATE">ONE_TIME_PURCHASE_PRODUCT_UPDATE</option>
                  <option value="SEND_SECURITY_CHALLENGE_EMAIL">SEND_SECURITY_CHALLENGE_EMAIL</option>
                  <option value="DISCOUNT_APPLIED">DISCOUNT_APPLIED</option>
                  <option value="DISCOUNT_REMOVED">DISCOUNT_REMOVED</option>
                  <option value="DUNNING_CANCELLED">DUNNING_CANCELLED</option>
                  <option value="DUNNING_SKIPPED">DUNNING_SKIPPED</option>
                  <option value="DUNNING_PAUSED">DUNNING_PAUSED</option>
                  <option value="DUNNING_ATTEMPT_SKIPPED">DUNNING_ATTEMPT_SKIPPED</option>
                  <option value="BILLING_ATTEMPT_NOTIFICATION">BILLING_ATTEMPT_NOTIFICATION</option>
                  <option value="SEND_SUBSCRIPTION_MANAGEMENT_LINK">SEND_SUBSCRIPTION_MANAGEMENT_LINK</option>
                  <option value="PRICE_CHANGE_SYNC">PRICE_CHANGE_SYNC</option>
                  <option value="UPDATE_QUEUED_ATTEMPTS">UPDATE_QUEUED_ATTEMPTS</option>
                  <option value="WEBHOOK">WEBHOOK</option>
                  <option value="SPLIT_CONTRACT">SPLIT_CONTRACT</option>
                  <option value="SWITCH_PAYMENT_METHODS">SWITCH_PAYMENT_METHODS</option>
                  <option value="SEND_OUT_OF_STOCK_EMAIL">SEND_OUT_OF_STOCK_EMAIL</option>
                  <option value="CONTRACT_CREATED">CONTRACT_CREATED</option>
                  <option value="ANCHOR_DAY_REMOVED">ANCHOR_DAY_REMOVED</option>
                  <option value="UPCOMING_ORDER_UPDATED">UPCOMING_ORDER_UPDATED</option>
                  <option value="SUBSCRIPTION_CONTRACT_UPDATED">SUBSCRIPTION_CONTRACT_UPDATED</option>
                  <option value="BILLING_ATTEMPT_UPDATED">BILLING_ATTEMPT_UPDATED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="activity-log-status">
                  Status
                </Label>
                <AvInput
                  id="activity-log-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && activityLogEntity.status) || 'SUCCESS'}
                >
                  <option value="SUCCESS">SUCCESS</option>
                  <option value="FAILURE">FAILURE</option>
                  <option value="INFO">INFO</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="createAtLabel" for="activity-log-createAt">
                  Create At
                </Label>
                <AvInput
                  id="activity-log-createAt"
                  type="datetime-local"
                  className="form-control"
                  name="createAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.activityLogEntity.createAt)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="additionalInfoLabel" for="activity-log-additionalInfo">
                  Additional Info
                </Label>
                <AvInput id="activity-log-additionalInfo" type="textarea" name="additionalInfo" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/activity-log" replace color="info">
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
  activityLogEntity: storeState.activityLog.entity,
  loading: storeState.activityLog.loading,
  updating: storeState.activityLog.updating,
  updateSuccess: storeState.activityLog.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActivityLogUpdate);
