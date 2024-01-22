import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer-payment.reducer';
import { ICustomerPayment } from 'app/shared/model/customer-payment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerPaymentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerPaymentDetail = (props: ICustomerPaymentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { customerPaymentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CustomerPayment [<b>{customerPaymentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{customerPaymentEntity.shop}</dd>
          <dt>
            <span id="adminGraphqlApiId">Admin Graphql Api Id</span>
          </dt>
          <dd>{customerPaymentEntity.adminGraphqlApiId}</dd>
          <dt>
            <span id="token">Token</span>
          </dt>
          <dd>{customerPaymentEntity.token}</dd>
          <dt>
            <span id="customerId">Customer Id</span>
          </dt>
          <dd>{customerPaymentEntity.customerId}</dd>
          <dt>
            <span id="adminGraphqlApiCustomerId">Admin Graphql Api Customer Id</span>
          </dt>
          <dd>{customerPaymentEntity.adminGraphqlApiCustomerId}</dd>
          <dt>
            <span id="instrumentType">Instrument Type</span>
          </dt>
          <dd>{customerPaymentEntity.instrumentType}</dd>
          <dt>
            <span id="paymentInstrumentLastDigits">Payment Instrument Last Digits</span>
          </dt>
          <dd>{customerPaymentEntity.paymentInstrumentLastDigits}</dd>
          <dt>
            <span id="paymentInstrumentMonth">Payment Instrument Month</span>
          </dt>
          <dd>{customerPaymentEntity.paymentInstrumentMonth}</dd>
          <dt>
            <span id="paymentInstrumentYear">Payment Instrument Year</span>
          </dt>
          <dd>{customerPaymentEntity.paymentInstrumentYear}</dd>
          <dt>
            <span id="paymentInstrumentName">Payment Instrument Name</span>
          </dt>
          <dd>{customerPaymentEntity.paymentInstrumentName}</dd>
          <dt>
            <span id="paymentInstrumentsBrand">Payment Instruments Brand</span>
          </dt>
          <dd>{customerPaymentEntity.paymentInstrumentsBrand}</dd>
          <dt>
            <span id="customerUid">Customer Uid</span>
          </dt>
          <dd>{customerPaymentEntity.customerUid}</dd>
          <dt>
            <span id="cardExpiryNotificationCounter">Card Expiry Notification Counter</span>
          </dt>
          <dd>{customerPaymentEntity.cardExpiryNotificationCounter}</dd>
          <dt>
            <span id="cardExpiryNotificationFirstSent">Card Expiry Notification First Sent</span>
          </dt>
          <dd>
            {customerPaymentEntity.cardExpiryNotificationFirstSent ? (
              <TextFormat value={customerPaymentEntity.cardExpiryNotificationFirstSent} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="cardExpiryNotificationLastSent">Card Expiry Notification Last Sent</span>
          </dt>
          <dd>
            {customerPaymentEntity.cardExpiryNotificationLastSent ? (
              <TextFormat value={customerPaymentEntity.cardExpiryNotificationLastSent} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="cardExpireEmailSentStatus">Card Expire Email Sent Status</span>
          </dt>
          <dd>{customerPaymentEntity.cardExpireEmailSentStatus}</dd>
          <dt>
            <span id="cardExpireSmsSentStatus">Card Expire Sms Sent Status</span>
          </dt>
          <dd>{customerPaymentEntity.cardExpireSmsSentStatus}</dd>
        </dl>
        <Button tag={Link} to="/customer-payment" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer-payment/${customerPaymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ customerPayment }: IRootState) => ({
  customerPaymentEntity: customerPayment.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPaymentDetail);
