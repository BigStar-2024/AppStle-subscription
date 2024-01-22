import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './shop-customization.reducer';
import { IShopCustomization } from 'app/shared/model/shop-customization.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopCustomizationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ShopCustomization = (props: IShopCustomizationProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { shopCustomizationList, match } = props;
  return (
    <div>
      <h2 id="shop-customization-heading">
        Shop Customizations
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Shop Customization
        </Link>
      </h2>
      <div className="table-responsive">
        {shopCustomizationList && shopCustomizationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Label Id</th>
                <th>Value</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shopCustomizationList.map((shopCustomization, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${shopCustomization.id}`} color="link" size="sm">
                      {shopCustomization.id}
                    </Button>
                  </td>
                  <td>{shopCustomization.shop}</td>
                  <td>{shopCustomization.labelId}</td>
                  <td>{shopCustomization.value}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${shopCustomization.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shopCustomization.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shopCustomization.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Shop Customizations found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ shopCustomization }: IRootState) => ({
  shopCustomizationList: shopCustomization.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopCustomization);
