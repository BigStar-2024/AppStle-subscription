import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-bundling.reducer';
import { ISubscriptionBundling } from 'app/shared/model/subscription-bundling.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionBundlingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionBundling = (props: ISubscriptionBundlingProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { subscriptionBundlingList, match, loading } = props;
  return (
    <div>
      <h2 id="subscription-bundling-heading">
        Subscription Bundlings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Subscription Bundling
        </Link>
      </h2>
      <div className="table-responsive">
        {subscriptionBundlingList && subscriptionBundlingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Subscription Bundling Enabled</th>
                <th>Subscription Id</th>
                <th>Min Product Count</th>
                <th>Max Product Count</th>
                <th>Discount</th>
                <th>Unique Ref</th>
                <th>Bundle Redirect</th>
                <th>Custom Redirect URL</th>
                <th>Min Order Amount</th>
                <th>Tiered Discount</th>
                <th>Product View Style</th>
                <th>Build A Box Type</th>
                <th>Single Product Settings</th>g
                <th>Build-A-Box Top Html</th>
                <th>Build-A-Box Bottom Html</th>
                <th>Proceed To Checkout Button Text</th>
                <th>Choose Products Text</th>
                <th>Name</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionBundlingList.map((subscriptionBundling, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionBundling.id}`} color="link" size="sm">
                      {subscriptionBundling.id}
                    </Button>
                  </td>
                  <td>{subscriptionBundling.shop}</td>
                  <td>{subscriptionBundling.subscriptionBundlingEnabled ? 'true' : 'false'}</td>
                  <td>{subscriptionBundling.subscriptionId}</td>
                  <td>{subscriptionBundling.minProductCount}</td>
                  <td>{subscriptionBundling.maxProductCount}</td>
                  <td>{subscriptionBundling.discount}</td>
                  <td>{subscriptionBundling.uniqueRef}</td>
                  <td>{subscriptionBundling.bundleRedirect}</td>
                  <td>{subscriptionBundling.customRedirectURL}</td>
                  <td>{subscriptionBundling.minOrderAmount}</td>
                  <td>{subscriptionBundling.tieredDiscount}</td>
                  <td>{subscriptionBundling.productViewStyle}</td>
                  <td>{subscriptionBundling.buildABoxType}</td>
                  <td>{subscriptionBundling.singleProductSettings}</td>
                  <td>{subscriptionBundling.bundleTopHtml}</td>
                  <td>{subscriptionBundling.bundleBottomHtml}</td>
                  <td>{subscriptionBundling.proceedToCheckoutButtonText}</td>
                  <td>{subscriptionBundling.chooseProductsText}</td>
                  <td>{subscriptionBundling.name}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionBundling.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionBundling.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionBundling.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Subscription Bundlings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionBundling }: IRootState) => ({
  subscriptionBundlingList: subscriptionBundling.entities,
  loading: subscriptionBundling.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBundling);
