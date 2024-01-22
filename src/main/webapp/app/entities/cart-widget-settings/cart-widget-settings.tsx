import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './cart-widget-settings.reducer';
import { ICartWidgetSettings } from 'app/shared/model/cart-widget-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICartWidgetSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CartWidgetSettings = (props: ICartWidgetSettingsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { cartWidgetSettingsList, match, loading } = props;
  return (
    <div>
      <h2 id="cart-widget-settings-heading">
        Cart Widget Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Cart Widget Settings
        </Link>
      </h2>
      <div className="table-responsive">
        {cartWidgetSettingsList && cartWidgetSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Enable Cart Widget Settings</th>
                <th>Cart Widget Setting Approach</th>
                <th>Cart Row Selector</th>
                <th>Cart Row Placement</th>
                <th>Cart Line Item Selector</th>
                <th>Cart Line Item Placement</th>
                <th>Cart Form Selector</th>
                <th>Appstel Custome Selector</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cartWidgetSettingsList.map((cartWidgetSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${cartWidgetSettings.id}`} color="link" size="sm">
                      {cartWidgetSettings.id}
                    </Button>
                  </td>
                  <td>{cartWidgetSettings.shop}</td>
                  <td>{cartWidgetSettings.enable_cart_widget_settings ? 'true' : 'false'}</td>
                  <td>{cartWidgetSettings.cartWidgetSettingApproach}</td>
                  <td>{cartWidgetSettings.cartRowSelector}</td>
                  <td>{cartWidgetSettings.cartRowPlacement}</td>
                  <td>{cartWidgetSettings.cartLineItemSelector}</td>
                  <td>{cartWidgetSettings.cartLineItemPlacement}</td>
                  <td>{cartWidgetSettings.cartFormSelector}</td>
                  <td>{cartWidgetSettings.appstelCustomeSelector}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${cartWidgetSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${cartWidgetSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${cartWidgetSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Cart Widget Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ cartWidgetSettings }: IRootState) => ({
  cartWidgetSettingsList: cartWidgetSettings.entities,
  loading: cartWidgetSettings.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CartWidgetSettings);
