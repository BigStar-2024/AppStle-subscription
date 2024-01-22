import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-contract-settings.reducer';
import { ISubscriptionContractSettings } from 'app/shared/model/subscription-contract-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionContractSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionContractSettings = (props: ISubscriptionContractSettingsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { subscriptionContractSettingsList, match, loading } = props;
  return (
    <div>
      <h2 id="subscription-contract-settings-heading">
        Subscription Contract Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Subscription Contract Settings
        </Link>
      </h2>
      <div className="table-responsive">
        {subscriptionContractSettingsList && subscriptionContractSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Product Id</th>
                <th>Ends On Count</th>
                <th>Ends On Interval</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionContractSettingsList.map((subscriptionContractSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionContractSettings.id}`} color="link" size="sm">
                      {subscriptionContractSettings.id}
                    </Button>
                  </td>
                  <td>{subscriptionContractSettings.shop}</td>
                  <td>{subscriptionContractSettings.productId}</td>
                  <td>{subscriptionContractSettings.endsOnCount}</td>
                  <td>{subscriptionContractSettings.endsOnInterval}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionContractSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionContractSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionContractSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Subscription Contract Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionContractSettings }: IRootState) => ({
  subscriptionContractSettingsList: subscriptionContractSettings.entities,
  loading: subscriptionContractSettings.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractSettings);
