import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './customer-payment.reducer';
import { ICustomerPayment } from 'app/shared/model/customer-payment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerPaymentProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CustomerPayment = (props: ICustomerPaymentProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { customerPaymentList, match, loading } = props;
  return (
    <div>
      <h2 id="customer-payment-heading">
        Customer Payments
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Customer Payment
        </Link>
      </h2>
      <div className="table-responsive">
        {customerPaymentList && customerPaymentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Admin Graphql Api Id</th>
                <th>Token</th>
                <th>Customer Id</th>
                <th>Admin Graphql Api Customer Id</th>
                <th>Instrument Type</th>
                <th>Payment Instrument Last Digits</th>
                <th>Payment Instrument Month</th>
                <th>Payment Instrument Year</th>
                <th>Payment Instrument Name</th>
                <th>Payment Instruments Brand</th>
                <th>Customer Uid</th>
                <th>Card Expiry Notification Counter</th>
                <th>Card Expiry Notification First Sent</th>
                <th>Card Expiry Notification Last Sent</th>
                <th>Card Expire Email Sent Status</th>
                <th>Card Expire Sms Sent Status</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerPaymentList.map((customerPayment, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customerPayment.id}`} color="link" size="sm">
                      {customerPayment.id}
                    </Button>
                  </td>
                  <td>{customerPayment.shop}</td>
                  <td>{customerPayment.adminGraphqlApiId}</td>
                  <td>{customerPayment.token}</td>
                  <td>{customerPayment.customerId}</td>
                  <td>{customerPayment.adminGraphqlApiCustomerId}</td>
                  <td>{customerPayment.instrumentType}</td>
                  <td>{customerPayment.paymentInstrumentLastDigits}</td>
                  <td>{customerPayment.paymentInstrumentMonth}</td>
                  <td>{customerPayment.paymentInstrumentYear}</td>
                  <td>{customerPayment.paymentInstrumentName}</td>
                  <td>{customerPayment.paymentInstrumentsBrand}</td>
                  <td>{customerPayment.customerUid}</td>
                  <td>{customerPayment.cardExpiryNotificationCounter}</td>
                  <td>
                    {customerPayment.cardExpiryNotificationFirstSent ? (
                      <TextFormat type="date" value={customerPayment.cardExpiryNotificationFirstSent} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {customerPayment.cardExpiryNotificationLastSent ? (
                      <TextFormat type="date" value={customerPayment.cardExpiryNotificationLastSent} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{customerPayment.cardExpireEmailSentStatus}</td>
                  <td>{customerPayment.cardExpireSmsSentStatus}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customerPayment.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerPayment.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerPayment.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Customer Payments found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ customerPayment }: IRootState) => ({
  customerPaymentList: customerPayment.entities,
  loading: customerPayment.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPayment);
