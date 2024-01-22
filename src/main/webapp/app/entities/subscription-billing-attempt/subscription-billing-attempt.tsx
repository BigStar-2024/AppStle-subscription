import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-billing-attempt.reducer';
import { ISubscriptionBillingAttempt } from 'app/shared/model/subscription-billing-attempt.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionBillingAttemptProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionBillingAttempt = (props: ISubscriptionBillingAttemptProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { subscriptionBillingAttemptList, match } = props;
  return (
    <div>
      <h2 id="subscription-billing-attempt-heading">
        Subscription Billing Attempts
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Subscription Billing Attempt
        </Link>
      </h2>
      <div className="table-responsive">
        {subscriptionBillingAttemptList && subscriptionBillingAttemptList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Billing Attempt Id</th>
                <th>Status</th>
                <th>Billing Date</th>
                <th>Contract Id</th>
                <th>Attempt Count</th>
                <th>Attempt Time</th>
                <th>Graph Order Id</th>
                <th>Order Id</th>
                <th>Order Amount</th>
                <th>Order Name</th>
                <th>Retrying Needed</th>
                <th>Transaction Failed Email Sent Status</th>
                <th>Upcoming Order Email Sent Status</th>
                <th>Apply Usage Charge</th>
                <th>Recurring Charge Id</th>
                <th>Transaction Rate</th>
                <th>Usage Charge Status</th>
                <th>Transaction Failed Sms Sent Status</th>
                <th>Upcoming Order Sms Sent Status</th>
                <th>Billing Attempt Response Message</th>
                <th>Progress Attempt Count</th>
                <th>Order Note</th>
                <th>Security Challenge Sent Status</th>
                <th>Order Amount USD</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionBillingAttemptList.map((subscriptionBillingAttempt, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionBillingAttempt.id}`} color="link" size="sm">
                      {subscriptionBillingAttempt.id}
                    </Button>
                  </td>
                  <td>{subscriptionBillingAttempt.shop}</td>
                  <td>{subscriptionBillingAttempt.billingAttemptId}</td>
                  <td>{subscriptionBillingAttempt.status}</td>
                  <td>
                    <TextFormat type="date" value={subscriptionBillingAttempt.billingDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{subscriptionBillingAttempt.contractId}</td>
                  <td>{subscriptionBillingAttempt.attemptCount}</td>
                  <td>
                    <TextFormat type="date" value={subscriptionBillingAttempt.attemptTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{subscriptionBillingAttempt.graphOrderId}</td>
                  <td>{subscriptionBillingAttempt.orderId}</td>
                  <td>{subscriptionBillingAttempt.orderAmount}</td>
                  <td>{subscriptionBillingAttempt.orderName}</td>
                  <td>{subscriptionBillingAttempt.retryingNeeded ? 'true' : 'false'}</td>
                  <td>{subscriptionBillingAttempt.transactionFailedEmailSentStatus}</td>
                  <td>{subscriptionBillingAttempt.upcomingOrderEmailSentStatus}</td>
                  <td>{subscriptionBillingAttempt.applyUsageCharge ? 'true' : 'false'}</td>
                  <td>{subscriptionBillingAttempt.recurringChargeId}</td>
                  <td>{subscriptionBillingAttempt.transactionRate}</td>
                  <td>{subscriptionBillingAttempt.usageChargeStatus}</td>
                  <td>{subscriptionBillingAttempt.transactionFailedSmsSentStatus}</td>
                  <td>{subscriptionBillingAttempt.upcomingOrderSmsSentStatus}</td>
                  <td>{subscriptionBillingAttempt.billingAttemptResponseMessage}</td>
                  <td>{subscriptionBillingAttempt.progressAttemptCount}</td>
                  <td>{subscriptionBillingAttempt.orderNote}</td>
                  <td>{subscriptionBillingAttempt.securityChallengeSentStatus}</td>
                  <td>{subscriptionBillingAttempt.orderAmountUSD}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionBillingAttempt.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionBillingAttempt.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionBillingAttempt.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Subscription Billing Attempts found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionBillingAttempt }: IRootState) => ({
  subscriptionBillingAttemptList: subscriptionBillingAttempt.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBillingAttempt);
