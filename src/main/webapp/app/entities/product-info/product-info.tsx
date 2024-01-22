import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './product-info.reducer';
import { IProductInfo } from 'app/shared/model/product-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ProductInfo = (props: IProductInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { productInfoList, match, loading } = props;
  return (
    <div>
      <h2 id="product-info-heading">
        Product Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Product Info
        </Link>
      </h2>
      <div className="table-responsive">
        {productInfoList && productInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Product Id</th>
                <th>Product Title</th>
                <th>Product Handle</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productInfoList.map((productInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${productInfo.id}`} color="link" size="sm">
                      {productInfo.id}
                    </Button>
                  </td>
                  <td>{productInfo.shop}</td>
                  <td>{productInfo.productId}</td>
                  <td>{productInfo.productTitle}</td>
                  <td>{productInfo.productHandle}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${productInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Product Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ productInfo }: IRootState) => ({
  productInfoList: productInfo.entities,
  loading: productInfo.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductInfo);
