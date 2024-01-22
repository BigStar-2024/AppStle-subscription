import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './product-cycle.reducer';
import { IProductCycle } from 'app/shared/model/product-cycle.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductCycleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ProductCycle = (props: IProductCycleProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { productCycleList, match, loading } = props;
  return (
    <div>
      <h2 id="product-cycle-heading">
        Product Cycles
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Product Cycle
        </Link>
      </h2>
      <div className="table-responsive">
        {productCycleList && productCycleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Source Product</th>
                <th>Destination Products</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productCycleList.map((productCycle, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${productCycle.id}`} color="link" size="sm">
                      {productCycle.id}
                    </Button>
                  </td>
                  <td>{productCycle.shop}</td>
                  <td>{productCycle.sourceProduct}</td>
                  <td>{productCycle.destinationProducts}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${productCycle.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productCycle.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productCycle.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Product Cycles found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ productCycle }: IRootState) => ({
  productCycleList: productCycle.entities,
  loading: productCycle.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductCycle);
