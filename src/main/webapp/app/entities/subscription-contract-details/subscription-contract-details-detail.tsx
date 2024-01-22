import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-contract-details.reducer';
import { ISubscriptionContractDetails } from 'app/shared/model/subscription-contract-details.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionContractDetailsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractDetailsDetail = (props: ISubscriptionContractDetailsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionContractDetailsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionContractDetails [<b>{subscriptionContractDetailsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.shop}</dd>
          <dt>
            <span id="graphSubscriptionContractId">Graph Subscription Contract Id</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.graphSubscriptionContractId}</dd>
          <dt>
            <span id="subscriptionContractId">Subscription Contract Id</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.subscriptionContractId}</dd>
          <dt>
            <span id="billingPolicyInterval">Billing Policy Interval</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.billingPolicyInterval}</dd>
          <dt>
            <span id="billingPolicyIntervalCount">Billing Policy Interval Count</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.billingPolicyIntervalCount}</dd>
          <dt>
            <span id="currencyCode">Currency Code</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.currencyCode}</dd>
          <dt>
            <span id="customerId">Customer Id</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.customerId}</dd>
          <dt>
            <span id="graphCustomerId">Graph Customer Id</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.graphCustomerId}</dd>
          <dt>
            <span id="deliveryPolicyInterval">Delivery Policy Interval</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.deliveryPolicyInterval}</dd>
          <dt>
            <span id="deliveryPolicyIntervalCount">Delivery Policy Interval Count</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.deliveryPolicyIntervalCount}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.status}</dd>
          <dt>
            <span id="graphOrderId">Graph Order Id</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.graphOrderId}</dd>
          <dt>
            <span id="orderId">Order Id</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.orderId}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {subscriptionContractDetailsEntity.createdAt ? (
              <TextFormat value={subscriptionContractDetailsEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>
            {subscriptionContractDetailsEntity.updatedAt ? (
              <TextFormat value={subscriptionContractDetailsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="nextBillingDate">Next Billing Date</span>
          </dt>
          <dd>
            {subscriptionContractDetailsEntity.nextBillingDate ? (
              <TextFormat value={subscriptionContractDetailsEntity.nextBillingDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="orderAmount">Order Amount</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.orderAmount}</dd>
          <dt>
            <span id="orderName">Order Name</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.orderName}</dd>
          <dt>
            <span id="customerName">Customer Name</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.customerName}</dd>
          <dt>
            <span id="customerEmail">Customer Email</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.customerEmail}</dd>
          <dt>
            <span id="subscriptionCreatedEmailSent">Subscription Created Email Sent</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.subscriptionCreatedEmailSent ? 'true' : 'false'}</dd>
          <dt>
            <span id="endsAt">Ends At</span>
          </dt>
          <dd>
            {subscriptionContractDetailsEntity.endsAt ? (
              <TextFormat value={subscriptionContractDetailsEntity.endsAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="startsAt">Starts At</span>
          </dt>
          <dd>
            {subscriptionContractDetailsEntity.startsAt ? (
              <TextFormat value={subscriptionContractDetailsEntity.startsAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="subscriptionCreatedEmailSentStatus">Subscription Created Email Sent Status</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.subscriptionCreatedEmailSentStatus}</dd>
          <dt>
            <span id="minCycles">Min Cycles</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.minCycles}</dd>
          <dt>
            <span id="maxCycles">Max Cycles</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.maxCycles}</dd>
          <dt>
            <span id="customerFirstName">Customer First Name</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.customerFirstName}</dd>
          <dt>
            <span id="customerLastName">Customer Last Name</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.customerLastName}</dd>
          <dt>
            <span id="autoCharge">Auto Charge</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.autoCharge ? 'true' : 'false'}</dd>
          <dt>
            <span id="importedId">Imported Id</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.importedId}</dd>
          <dt>
            <span id="stopUpComingOrderEmail">Stop Up Coming Order Email</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.stopUpComingOrderEmail ? 'true' : 'false'}</dd>
          <dt>
            <span id="pausedFromActive">Paused From Active</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.pausedFromActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="subscriptionCreatedSmsSentStatus">Subscription Created Sms Sent Status</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.subscriptionCreatedSmsSentStatus}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.phone}</dd>
          <dt>
            <span id="activatedOn">Activated On</span>
          </dt>
          <dd>
            {subscriptionContractDetailsEntity.activatedOn ? (
              <TextFormat value={subscriptionContractDetailsEntity.activatedOn} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="pausedOn">Paused On</span>
          </dt>
          <dd>
            {subscriptionContractDetailsEntity.pausedOn ? (
              <TextFormat value={subscriptionContractDetailsEntity.pausedOn} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="cancelledOn">Cancelled On</span>
          </dt>
          <dd>
            {subscriptionContractDetailsEntity.cancelledOn ? (
              <TextFormat value={subscriptionContractDetailsEntity.cancelledOn} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="contractDetailsJSON">Contract Details JSON</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.contractDetailsJSON}</dd>
          <dt>
            <span id="cancellationFeedback">Cancellation Feedback</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.cancellationFeedback}</dd>
          <dt>
            <span id="orderNote">Order Note</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.orderNote}</dd>
          <dt>
            <span id="orderNoteAttributes">Order Note Attributes</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.orderNoteAttributes}</dd>
          <dt>
            <span id="allowDeliveryPriceOverride">Allow Delivery Price Override</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.allowDeliveryPriceOverride ? 'true' : 'false'}</dd>
          <dt>
            <span id="disableFixEmptyQueue">Disable Fix Empty Queue</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.disableFixEmptyQueue ? 'true' : 'false'}</dd>
          <dt>
            <span id="orderAmountUSD">Order Amount USD</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.orderAmountUSD}</dd>
          <dt>
            <span id="originType">Origin Type</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.originType}</dd>
          <dt>
            <span id="originalContractId">Original Contract Id</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.originalContractId}</dd>
          <dt>
            <span id="cancellationNote">Cancellation Note</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.cancellationNote}</dd>
          <dt>
            <span id="subscriptionType">Subscription Type</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.subscriptionType}</dd>
          <dt>
            <span id="subscriptionTypeIdentifier">Subscription Type Identifier</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.subscriptionTypeIdentifier}</dd>
          <dt>
            <span id="upcomingEmailBufferDays">Upcoming Email Buffer Days</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.upcomingEmailBufferDays}</dd>
          <dt>
            <span id="upcomingEmailTaskUrl">Upcoming Email Task Url</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.upcomingEmailTaskUrl}</dd>
          <dt>
            <span id="contractAmount">Contract Amount</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.contractAmount}</dd>
          <dt>
            <span id="contractAmountUSD">Contract Amount USD</span>
          </dt>
          <dd>{subscriptionContractDetailsEntity.contractAmountUSD}</dd>
        </dl>
        <Button tag={Link} to="/subscription-contract-details" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-contract-details/${subscriptionContractDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionContractDetails }: IRootState) => ({
  subscriptionContractDetailsEntity: subscriptionContractDetails.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractDetailsDetail);
