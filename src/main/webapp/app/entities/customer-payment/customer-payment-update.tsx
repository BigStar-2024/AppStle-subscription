import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './customer-payment.reducer';
import { ICustomerPayment } from 'app/shared/model/customer-payment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomerPaymentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerPaymentUpdate = (props: ICustomerPaymentUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { customerPaymentEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/customer-payment');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.cardExpiryNotificationFirstSent = convertDateTimeToServer(values.cardExpiryNotificationFirstSent);
    values.cardExpiryNotificationLastSent = convertDateTimeToServer(values.cardExpiryNotificationLastSent);

    if (errors.length === 0) {
      const entity = {
        ...customerPaymentEntity,
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
          <h2 id="subscriptionApp.customerPayment.home.createOrEditLabel">Create or edit a CustomerPayment</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : customerPaymentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="customer-payment-id">ID</Label>
                  <AvInput id="customer-payment-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="customer-payment-shop">
                  Shop
                </Label>
                <AvField id="customer-payment-shop" type="text" name="shop" />
              </AvGroup>
              <AvGroup>
                <Label id="adminGraphqlApiIdLabel" for="customer-payment-adminGraphqlApiId">
                  Admin Graphql Api Id
                </Label>
                <AvField id="customer-payment-adminGraphqlApiId" type="text" name="adminGraphqlApiId" />
              </AvGroup>
              <AvGroup>
                <Label id="tokenLabel" for="customer-payment-token">
                  Token
                </Label>
                <AvField id="customer-payment-token" type="text" name="token" />
              </AvGroup>
              <AvGroup>
                <Label id="customerIdLabel" for="customer-payment-customerId">
                  Customer Id
                </Label>
                <AvField id="customer-payment-customerId" type="string" className="form-control" name="customerId" />
              </AvGroup>
              <AvGroup>
                <Label id="adminGraphqlApiCustomerIdLabel" for="customer-payment-adminGraphqlApiCustomerId">
                  Admin Graphql Api Customer Id
                </Label>
                <AvField id="customer-payment-adminGraphqlApiCustomerId" type="text" name="adminGraphqlApiCustomerId" />
              </AvGroup>
              <AvGroup>
                <Label id="instrumentTypeLabel" for="customer-payment-instrumentType">
                  Instrument Type
                </Label>
                <AvField id="customer-payment-instrumentType" type="text" name="instrumentType" />
              </AvGroup>
              <AvGroup>
                <Label id="paymentInstrumentLastDigitsLabel" for="customer-payment-paymentInstrumentLastDigits">
                  Payment Instrument Last Digits
                </Label>
                <AvField
                  id="customer-payment-paymentInstrumentLastDigits"
                  type="string"
                  className="form-control"
                  name="paymentInstrumentLastDigits"
                />
              </AvGroup>
              <AvGroup>
                <Label id="paymentInstrumentMonthLabel" for="customer-payment-paymentInstrumentMonth">
                  Payment Instrument Month
                </Label>
                <AvField
                  id="customer-payment-paymentInstrumentMonth"
                  type="string"
                  className="form-control"
                  name="paymentInstrumentMonth"
                />
              </AvGroup>
              <AvGroup>
                <Label id="paymentInstrumentYearLabel" for="customer-payment-paymentInstrumentYear">
                  Payment Instrument Year
                </Label>
                <AvField id="customer-payment-paymentInstrumentYear" type="string" className="form-control" name="paymentInstrumentYear" />
              </AvGroup>
              <AvGroup>
                <Label id="paymentInstrumentNameLabel" for="customer-payment-paymentInstrumentName">
                  Payment Instrument Name
                </Label>
                <AvField id="customer-payment-paymentInstrumentName" type="text" name="paymentInstrumentName" />
              </AvGroup>
              <AvGroup>
                <Label id="paymentInstrumentsBrandLabel" for="customer-payment-paymentInstrumentsBrand">
                  Payment Instruments Brand
                </Label>
                <AvField id="customer-payment-paymentInstrumentsBrand" type="text" name="paymentInstrumentsBrand" />
              </AvGroup>
              <AvGroup>
                <Label id="customerUidLabel" for="customer-payment-customerUid">
                  Customer Uid
                </Label>
                <AvField id="customer-payment-customerUid" type="text" name="customerUid" />
              </AvGroup>
              <AvGroup>
                <Label id="cardExpiryNotificationCounterLabel" for="customer-payment-cardExpiryNotificationCounter">
                  Card Expiry Notification Counter
                </Label>
                <AvField
                  id="customer-payment-cardExpiryNotificationCounter"
                  type="string"
                  className="form-control"
                  name="cardExpiryNotificationCounter"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cardExpiryNotificationFirstSentLabel" for="customer-payment-cardExpiryNotificationFirstSent">
                  Card Expiry Notification First Sent
                </Label>
                <AvInput
                  id="customer-payment-cardExpiryNotificationFirstSent"
                  type="datetime-local"
                  className="form-control"
                  name="cardExpiryNotificationFirstSent"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={
                    isNew
                      ? displayDefaultDateTime()
                      : convertDateTimeFromServer(props.customerPaymentEntity.cardExpiryNotificationFirstSent)
                  }
                />
              </AvGroup>
              <AvGroup>
                <Label id="cardExpiryNotificationLastSentLabel" for="customer-payment-cardExpiryNotificationLastSent">
                  Card Expiry Notification Last Sent
                </Label>
                <AvInput
                  id="customer-payment-cardExpiryNotificationLastSent"
                  type="datetime-local"
                  className="form-control"
                  name="cardExpiryNotificationLastSent"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={
                    isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.customerPaymentEntity.cardExpiryNotificationLastSent)
                  }
                />
              </AvGroup>
              <AvGroup>
                <Label id="cardExpireEmailSentStatusLabel" for="customer-payment-cardExpireEmailSentStatus">
                  Card Expire Email Sent Status
                </Label>
                <AvInput
                  id="customer-payment-cardExpireEmailSentStatus"
                  type="select"
                  className="form-control"
                  name="cardExpireEmailSentStatus"
                  value={(!isNew && customerPaymentEntity.cardExpireEmailSentStatus) || 'FIRST_ATTEMPT'}
                >
                  <option value="FIRST_ATTEMPT">FIRST_ATTEMPT</option>
                  <option value="SECOND_ATTEMPT">SECOND_ATTEMPT</option>
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="EMAIL_SETTINGS_DISABLED">EMAIL_SETTINGS_DISABLED</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="cardExpireSmsSentStatusLabel" for="customer-payment-cardExpireSmsSentStatus">
                  Card Expire Sms Sent Status
                </Label>
                <AvInput
                  id="customer-payment-cardExpireSmsSentStatus"
                  type="select"
                  className="form-control"
                  name="cardExpireSmsSentStatus"
                  value={(!isNew && customerPaymentEntity.cardExpireSmsSentStatus) || 'FIRST_ATTEMPT'}
                >
                  <option value="FIRST_ATTEMPT">FIRST_ATTEMPT</option>
                  <option value="SECOND_ATTEMPT">SECOND_ATTEMPT</option>
                  <option value="SENT">SENT</option>
                  <option value="UNSENT">UNSENT</option>
                  <option value="FAILED">FAILED</option>
                  <option value="SMS_SETTINGS_DISABLED">SMS_SETTINGS_DISABLED</option>
                  <option value="CONTRACT_CANCELLED">CONTRACT_CANCELLED</option>
                  <option value="PHONE_NUMBER_EMPTY">PHONE_NUMBER_EMPTY</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/customer-payment" replace color="info">
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
  customerPaymentEntity: storeState.customerPayment.entity,
  loading: storeState.customerPayment.loading,
  updating: storeState.customerPayment.updating,
  updateSuccess: storeState.customerPayment.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPaymentUpdate);
