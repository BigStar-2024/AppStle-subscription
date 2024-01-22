import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-billing-attempt.reducer';
import { ISubscriptionBillingAttempt } from 'app/shared/model/subscription-billing-attempt.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionBillingAttemptDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionBillingAttemptDetail = (props: ISubscriptionBillingAttemptDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionBillingAttemptEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionBillingAttempt [<b>{subscriptionBillingAttemptEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.shop}</dd>
          <dt>
            <span id="billingAttemptId">Billing Attempt Id</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.billingAttemptId}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.status}</dd>
          <dt>
            <span id="billingDate">Billing Date</span>
          </dt>
          <dd>
            <TextFormat value={subscriptionBillingAttemptEntity.billingDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="contractId">Contract Id</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.contractId}</dd>
          <dt>
            <span id="attemptCount">Attempt Count</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.attemptCount}</dd>
          <dt>
            <span id="attemptTime">Attempt Time</span>
          </dt>
          <dd>
            <TextFormat value={subscriptionBillingAttemptEntity.attemptTime} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="graphOrderId">Graph Order Id</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.graphOrderId}</dd>
          <dt>
            <span id="orderId">Order Id</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.orderId}</dd>
          <dt>
            <span id="orderAmount">Order Amount</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.orderAmount}</dd>
          <dt>
            <span id="orderName">Order Name</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.orderName}</dd>
          <dt>
            <span id="retryingNeeded">Retrying Needed</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.retryingNeeded ? 'true' : 'false'}</dd>
          <dt>
            <span id="transactionFailedEmailSentStatus">Transaction Failed Email Sent Status</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.transactionFailedEmailSentStatus}</dd>
          <dt>
            <span id="upcomingOrderEmailSentStatus">Upcoming Order Email Sent Status</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.upcomingOrderEmailSentStatus}</dd>
          <dt>
            <span id="applyUsageCharge">Apply Usage Charge</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.applyUsageCharge ? 'true' : 'false'}</dd>
          <dt>
            <span id="recurringChargeId">Recurring Charge Id</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.recurringChargeId}</dd>
          <dt>
            <span id="transactionRate">Transaction Rate</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.transactionRate}</dd>
          <dt>
            <span id="usageChargeStatus">Usage Charge Status</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.usageChargeStatus}</dd>
          <dt>
            <span id="transactionFailedSmsSentStatus">Transaction Failed Sms Sent Status</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.transactionFailedSmsSentStatus}</dd>
          <dt>
            <span id="upcomingOrderSmsSentStatus">Upcoming Order Sms Sent Status</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.upcomingOrderSmsSentStatus}</dd>
          <dt>
            <span id="billingAttemptResponseMessage">Billing Attempt Response Message</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.billingAttemptResponseMessage}</dd>
          <dt>
            <span id="progressAttemptCount">Progress Attempt Count</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.progressAttemptCount}</dd>
          <dt>
            <span id="orderNote">Order Note</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.orderNote}</dd>
          <dt>
            <span id="securityChallengeSentStatus">Security Challenge Sent Status</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.securityChallengeSentStatus}</dd>
          <dt>
            <span id="orderAmountUSD">Order Amount USD</span>
          </dt>
          <dd>{subscriptionBillingAttemptEntity.orderAmountUSD}</dd>
        </dl>
        <Button tag={Link} to="/subscription-billing-attempt" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-billing-attempt/${subscriptionBillingAttemptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionBillingAttempt }: IRootState) => ({
  subscriptionBillingAttemptEntity: subscriptionBillingAttempt.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBillingAttemptDetail);
