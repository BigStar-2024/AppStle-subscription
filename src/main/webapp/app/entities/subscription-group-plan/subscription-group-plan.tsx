import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-group-plan.reducer';
import { ISubscriptionGroupPlan } from 'app/shared/model/subscription-group-plan.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionGroupPlanProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionGroupPlan = (props: ISubscriptionGroupPlanProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { subscriptionGroupPlanList, match, loading } = props;
  return (
    <div>
      <h2 id="subscription-group-plan-heading">
        Subscription Group Plans
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Subscription Group Plan
        </Link>
      </h2>
      <div className="table-responsive">
        {subscriptionGroupPlanList && subscriptionGroupPlanList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Group Name</th>
                <th>Subscription Id</th>
                <th>Product Count</th>
                <th>Product Variant Count</th>
                <th>Info Json</th>
                <th>Product Ids</th>
                <th>Variant Ids</th>
                <th>Variant Product Ids</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionGroupPlanList.map((subscriptionGroupPlan, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionGroupPlan.id}`} color="link" size="sm">
                      {subscriptionGroupPlan.id}
                    </Button>
                  </td>
                  <td>{subscriptionGroupPlan.shop}</td>
                  <td>{subscriptionGroupPlan.groupName}</td>
                  <td>{subscriptionGroupPlan.subscriptionId}</td>
                  <td>{subscriptionGroupPlan.productCount}</td>
                  <td>{subscriptionGroupPlan.productVariantCount}</td>
                  <td>{subscriptionGroupPlan.infoJson}</td>
                  <td>{subscriptionGroupPlan.productIds}</td>
                  <td>{subscriptionGroupPlan.variantIds}</td>
                  <td>{subscriptionGroupPlan.variantProductIds}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionGroupPlan.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionGroupPlan.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionGroupPlan.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Subscription Group Plans found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionGroupPlan }: IRootState) => ({
  subscriptionGroupPlanList: subscriptionGroupPlan.entities,
  loading: subscriptionGroupPlan.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionGroupPlan);
