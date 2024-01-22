import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './payment-plan-activity.reducer';
import { IPaymentPlanActivity } from 'app/shared/model/payment-plan-activity.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IPaymentPlanActivityProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const PaymentPlanActivity = (props: IPaymentPlanActivityProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
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

  const { paymentPlanActivityList, match, totalItems } = props;
  return (
    <div>
      <h2 id="payment-plan-activity-heading">
        Payment Plan Activities
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Payment Plan Activity
        </Link>
      </h2>
      <div className="table-responsive">
        {paymentPlanActivityList && paymentPlanActivityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shop')}>
                  Shop <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('chargeActivated')}>
                  Charge Activated <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('activationDate')}>
                  Activation Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('recurringChargeId')}>
                  Recurring Charge Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastEmailSentToMerchant')}>
                  Last Email Sent To Merchant <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('numberOfEmailSentToMerchant')}>
                  Number Of Email Sent To Merchant <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('basePlan')}>
                  Base Plan <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('additionalDetails')}>
                  Additional Details <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('trialDays')}>
                  Trial Days <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('price')}>
                  Price <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('planType')}>
                  Plan Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('billingType')}>
                  Billing Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('basedOn')}>
                  Based On <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('features')}>
                  Features <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('testCharge')}>
                  Test Charge <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('billedDate')}>
                  Billed Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('validCharge')}>
                  Valid Charge <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('trialEndsOn')}>
                  Trial Ends On <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shopFrozen')}>
                  Shop Frozen <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('paymentPlanEvent')}>
                  Payment Plan Event <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Plan Info <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentPlanActivityList.map((paymentPlanActivity, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${paymentPlanActivity.id}`} color="link" size="sm">
                      {paymentPlanActivity.id}
                    </Button>
                  </td>
                  <td>{paymentPlanActivity.shop}</td>
                  <td>{paymentPlanActivity.chargeActivated ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={paymentPlanActivity.activationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{paymentPlanActivity.recurringChargeId}</td>
                  <td>
                    <TextFormat type="date" value={paymentPlanActivity.lastEmailSentToMerchant} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{paymentPlanActivity.numberOfEmailSentToMerchant}</td>
                  <td>{paymentPlanActivity.basePlan}</td>
                  <td>{paymentPlanActivity.additionalDetails}</td>
                  <td>{paymentPlanActivity.trialDays}</td>
                  <td>{paymentPlanActivity.price}</td>
                  <td>{paymentPlanActivity.name}</td>
                  <td>{paymentPlanActivity.planType}</td>
                  <td>{paymentPlanActivity.billingType}</td>
                  <td>{paymentPlanActivity.basedOn}</td>
                  <td>{paymentPlanActivity.features}</td>
                  <td>{paymentPlanActivity.testCharge ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={paymentPlanActivity.billedDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{paymentPlanActivity.validCharge ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={paymentPlanActivity.trialEndsOn} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{paymentPlanActivity.shopFrozen ? 'true' : 'false'}</td>
                  <td>{paymentPlanActivity.paymentPlanEvent}</td>
                  <td>
                    {paymentPlanActivity.planInfo ? (
                      <Link to={`plan-info/${paymentPlanActivity.planInfo.id}`}>{paymentPlanActivity.planInfo.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${paymentPlanActivity.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${paymentPlanActivity.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${paymentPlanActivity.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          <div className="alert alert-warning">No Payment Plan Activities found</div>
        )}
      </div>
      <div className={paymentPlanActivityList && paymentPlanActivityList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ paymentPlanActivity }: IRootState) => ({
  paymentPlanActivityList: paymentPlanActivity.entities,
  totalItems: paymentPlanActivity.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentPlanActivity);
