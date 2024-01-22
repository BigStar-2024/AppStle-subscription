import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-group.reducer';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionGroupProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionGroup = (props: ISubscriptionGroupProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { subscriptionGroupList, match, loading } = props;
  return (
    <div>
      <h2 id="subscription-group-heading">
        Subscription Plans
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Subscription Plan
        </Link>
      </h2>
      <div className="table-responsive">
        {subscriptionGroupList && subscriptionGroupList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Product Count</th>
                <th>Product Variant Count</th>
                <th>Frequency Count</th>
                <th>Frequency Interval</th>
                <th>Frequency Name</th>
                <th>Group Name</th>
                <th>Product Ids</th>
                <th>Discount Offer</th>
                <th>Discount Type</th>
                <th>Discount Enabled</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionGroupList.map((subscriptionGroup, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionGroup.id}`} color="link" size="sm">
                      {subscriptionGroup.id}
                    </Button>
                  </td>
                  <td>{subscriptionGroup.productCount}</td>
                  <td>{subscriptionGroup.productVariantCount}</td>
                  <td>{subscriptionGroup.frequencyCount}</td>
                  <td>{subscriptionGroup.frequencyInterval}</td>
                  <td>{subscriptionGroup.frequencyName}</td>
                  <td>{subscriptionGroup.groupName}</td>
                  <td>{subscriptionGroup.productIds}</td>
                  <td>{subscriptionGroup.discountOffer}</td>
                  <td>{subscriptionGroup.discountType}</td>
                  <td>{subscriptionGroup.discountEnabled ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionGroup.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionGroup.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionGroup.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Subscription Plans found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionGroup }: IRootState) => ({
  subscriptionGroupList: subscriptionGroup.entities,
  loading: subscriptionGroup.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionGroup);
