import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './shop-asset-urls.reducer';
import { IShopAssetUrls } from 'app/shared/model/shop-asset-urls.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopAssetUrlsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ShopAssetUrls = (props: IShopAssetUrlsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { shopAssetUrlsList, match } = props;
  return (
    <div>
      <h2 id="shop-asset-urls-heading">
        Shop Asset Urls
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Shop Asset Urls
        </Link>
      </h2>
      <div className="table-responsive">
        {shopAssetUrlsList && shopAssetUrlsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Vendor Javascript</th>
                <th>Vendor Css</th>
                <th>Customer Javascript</th>
                <th>Customer Css</th>
                <th>Bundle Javascript</th>
                <th>Bundle Css</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shopAssetUrlsList.map((shopAssetUrls, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${shopAssetUrls.id}`} color="link" size="sm">
                      {shopAssetUrls.id}
                    </Button>
                  </td>
                  <td>{shopAssetUrls.shop}</td>
                  <td>{shopAssetUrls.vendorJavascript}</td>
                  <td>{shopAssetUrls.vendorCss}</td>
                  <td>{shopAssetUrls.customerJavascript}</td>
                  <td>{shopAssetUrls.customerCss}</td>
                  <td>{shopAssetUrls.bundleJavascript}</td>
                  <td>{shopAssetUrls.bundleCss}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${shopAssetUrls.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shopAssetUrls.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shopAssetUrls.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Shop Asset Urls found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ shopAssetUrls }: IRootState) => ({
  shopAssetUrlsList: shopAssetUrls.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopAssetUrls);
