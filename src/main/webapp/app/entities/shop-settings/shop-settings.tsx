import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './shop-settings.reducer';
import { IShopSettings } from 'app/shared/model/shop-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ShopSettings = (props: IShopSettingsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { shopSettingsList, match } = props;
  return (
    <div>
      <h2 id="shop-settings-heading">
        Shop Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Shop Settings
        </Link>
      </h2>
      <div className="table-responsive">
        {shopSettingsList && shopSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Tagging Enabled</th>
                <th>Delay Tagging</th>
                <th>Delayed Tagging Value</th>
                <th>Delayed Tagging Unit</th>
                <th>Order Status</th>
                <th>Payment Status</th>
                <th>Fulfillment Status</th>
                <th>Price Sync Enabled</th>
                <th>Sku Sync Enabled</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shopSettingsList.map((shopSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${shopSettings.id}`} color="link" size="sm">
                      {shopSettings.id}
                    </Button>
                  </td>
                  <td>{shopSettings.shop}</td>
                  <td>{shopSettings.taggingEnabled ? 'true' : 'false'}</td>
                  <td>{shopSettings.delayTagging ? 'true' : 'false'}</td>
                  <td>{shopSettings.delayedTaggingValue}</td>
                  <td>{shopSettings.delayedTaggingUnit}</td>
                  <td>{shopSettings.orderStatus}</td>
                  <td>{shopSettings.paymentStatus}</td>
                  <td>{shopSettings.fulfillmentStatus}</td>
                  <td>{shopSettings.priceSyncEnabled ? 'true' : 'false'}</td>
                  <td>{shopSettings.skuSyncEnabled ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${shopSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shopSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shopSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Shop Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ shopSettings }: IRootState) => ({
  shopSettingsList: shopSettings.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopSettings);
