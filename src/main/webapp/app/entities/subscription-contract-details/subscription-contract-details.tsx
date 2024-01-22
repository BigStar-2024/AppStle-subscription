import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-contract-details.reducer';
import { ISubscriptionContractDetails } from 'app/shared/model/subscription-contract-details.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
// vvv Import does not exist
// import { } from 'app/shared/u.toString()t as I

export interface ISubscriptionContractDetailsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionContractDetails = (props: ISubscriptionContractDetailsProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE.toString()) as IPaginationBaseState);

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`, undefined);
  };

  useEffect(() => {
    getAllEntities();
  }, []);

  const sortEntities = () => {
    getAllEntities();
    props.history.push(
      `${props.location.pathname}?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
    );
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage
    });

  const { subscriptionContractDetailsList, match, totalItems } = props;
  return (
    <div>
      <h2 id="subscription-contract-details-heading">
        Subscription Contract Details
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Subscription Contract Details
        </Link>
      </h2>
      <div className="table-responsive">
        {subscriptionContractDetailsList && subscriptionContractDetailsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shop')}>
                  Shop <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('graphSubscriptionContractId')}>
                  Graph Subscription Contract Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('subscriptionContractId')}>
                  Subscription Contract Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('billingPolicyInterval')}>
                  Billing Policy Interval <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('billingPolicyIntervalCount')}>
                  Billing Policy Interval Count <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('currencyCode')}>
                  Currency Code <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('customerId')}>
                  Customer Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('graphCustomerId')}>
                  Graph Customer Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('deliveryPolicyInterval')}>
                  Delivery Policy Interval <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('deliveryPolicyIntervalCount')}>
                  Delivery Policy Interval Count <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('graphOrderId')}>
                  Graph Order Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('orderId')}>
                  Order Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  Created At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  Updated At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('nextBillingDate')}>
                  Next Billing Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('orderAmount')}>
                  Order Amount <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('orderName')}>
                  Order Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('customerName')}>
                  Customer Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('customerEmail')}>
                  Customer Email <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('subscriptionCreatedEmailSent')}>
                  Subscription Created Email Sent <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('endsAt')}>
                  Ends At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('startsAt')}>
                  Starts At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('subscriptionCreatedEmailSentStatus')}>
                  Subscription Created Email Sent Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('minCycles')}>
                  Min Cycles <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('maxCycles')}>
                  Max Cycles <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('customerFirstName')}>
                  Customer First Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('customerLastName')}>
                  Customer Last Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('autoCharge')}>
                  Auto Charge <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('importedId')}>
                  Imported Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stopUpComingOrderEmail')}>
                  Stop Up Coming Order Email <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pausedFromActive')}>
                  Paused From Active <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('subscriptionCreatedSmsSentStatus')}>
                  Subscription Created Sms Sent Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  Phone <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('activatedOn')}>
                  Activated On <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pausedOn')}>
                  Paused On <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('cancelledOn')}>
                  Cancelled On <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('contractDetailsJSON')}>
                  Contract Details JSON <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('cancellationFeedback')}>
                  Cancellation Feedback <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('orderNote')}>
                  Order Note <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('orderNoteAttributes')}>
                  Order Note Attributes <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('allowDeliveryPriceOverride')}>
                  Allow Delivery Price Override <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('disableFixEmptyQueue')}>
                  Disable Fix Empty Queue <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('orderAmountUSD')}>
                  Order Amount USD <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('originType')}>
                  Origin Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('originalContractId')}>
                  Original Contract Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('cancellationNote')}>
                  Cancellation Note <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionContractDetailsList.map((subscriptionContractDetails, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionContractDetails.id}`} color="link" size="sm">
                      {subscriptionContractDetails.id}
                    </Button>
                  </td>
                  <td>{subscriptionContractDetails.shop}</td>
                  <td>{subscriptionContractDetails.graphSubscriptionContractId}</td>
                  <td>{subscriptionContractDetails.subscriptionContractId}</td>
                  <td>{subscriptionContractDetails.billingPolicyInterval}</td>
                  <td>{subscriptionContractDetails.billingPolicyIntervalCount}</td>
                  <td>{subscriptionContractDetails.currencyCode}</td>
                  <td>{subscriptionContractDetails.customerId}</td>
                  <td>{subscriptionContractDetails.graphCustomerId}</td>
                  <td>{subscriptionContractDetails.deliveryPolicyInterval}</td>
                  <td>{subscriptionContractDetails.deliveryPolicyIntervalCount}</td>
                  <td>{subscriptionContractDetails.status}</td>
                  <td>{subscriptionContractDetails.graphOrderId}</td>
                  <td>{subscriptionContractDetails.orderId}</td>
                  <td>
                    <TextFormat type="date" value={subscriptionContractDetails.createdAt} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={subscriptionContractDetails.updatedAt} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={subscriptionContractDetails.nextBillingDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{subscriptionContractDetails.orderAmount}</td>
                  <td>{subscriptionContractDetails.orderName}</td>
                  <td>{subscriptionContractDetails.customerName}</td>
                  <td>{subscriptionContractDetails.customerEmail}</td>
                  <td>{subscriptionContractDetails.subscriptionCreatedEmailSent ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={subscriptionContractDetails.endsAt} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={subscriptionContractDetails.startsAt} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{subscriptionContractDetails.subscriptionCreatedEmailSentStatus}</td>
                  <td>{subscriptionContractDetails.minCycles}</td>
                  <td>{subscriptionContractDetails.maxCycles}</td>
                  <td>{subscriptionContractDetails.customerFirstName}</td>
                  <td>{subscriptionContractDetails.customerLastName}</td>
                  <td>{subscriptionContractDetails.autoCharge ? 'true' : 'false'}</td>
                  <td>{subscriptionContractDetails.importedId}</td>
                  <td>{subscriptionContractDetails.stopUpComingOrderEmail ? 'true' : 'false'}</td>
                  <td>{subscriptionContractDetails.pausedFromActive ? 'true' : 'false'}</td>
                  <td>{subscriptionContractDetails.subscriptionCreatedSmsSentStatus}</td>
                  <td>{subscriptionContractDetails.phone}</td>
                  <td>
                    <TextFormat type="date" value={subscriptionContractDetails.activatedOn} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={subscriptionContractDetails.pausedOn} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={subscriptionContractDetails.cancelledOn} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{subscriptionContractDetails.contractDetailsJSON}</td>
                  <td>{subscriptionContractDetails.cancellationFeedback}</td>
                  <td>{subscriptionContractDetails.orderNote}</td>
                  <td>{subscriptionContractDetails.orderNoteAttributes}</td>
                  <td>{subscriptionContractDetails.allowDeliveryPriceOverride ? 'true' : 'false'}</td>
                  <td>{subscriptionContractDetails.disableFixEmptyQueue ? 'true' : 'false'}</td>
                  <td>{subscriptionContractDetails.orderAmountUSD}</td>
                  <td>{subscriptionContractDetails.originType}</td>
                  <td>{subscriptionContractDetails.originalContractId}</td>
                  <td>{subscriptionContractDetails.cancellationNote}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionContractDetails.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${subscriptionContractDetails.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${subscriptionContractDetails.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Subscription Contract Details found</div>
        )}
      </div>
      <div className={subscriptionContractDetailsList && subscriptionContractDetailsList.length > 0 ? '' : 'd-none'}>
        <Row className="justify-content-center">
          <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
        </Row>
        <Row className="justify-content-center">
          <JhiPagination
            activePage={paginationState.activePage}
            onSelect={handlePagination}
            maxButtons={5}
            itemsPerPage={paginationState.itemsPerPage}
            totalItems={props.totalItems}
          />
        </Row>
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionContractDetails }: IRootState) => ({
  subscriptionContractDetailsList: subscriptionContractDetails.entities,
  totalItems: subscriptionContractDetails.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractDetails);
