import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './product-swap.reducer';
import { IProductSwap } from 'app/shared/model/product-swap.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductSwapProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ProductSwap = (props: IProductSwapProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { productSwapList, match } = props;
  return (
    <div>
      <h2 id="product-swap-heading">
        Product Swaps
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Product Swap
        </Link>
      </h2>
      <div className="table-responsive">
        {productSwapList && productSwapList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Source Variants</th>
                <th>Destination Variants</th>
                <th>Updated First Order</th>
                <th>Check For Every Recurring Order</th>
                <th>Name</th>
                <th>Change Next Order Date By</th>
                <th>For Billing Cycle</th>
                <th>Carry Discount Forward</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productSwapList.map((productSwap, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${productSwap.id}`} color="link" size="sm">
                      {productSwap.id}
                    </Button>
                  </td>
                  <td>{productSwap.shop}</td>
                  <td>{productSwap.sourceVariants}</td>
                  <td>{productSwap.destinationVariants}</td>
                  <td>{productSwap.updatedFirstOrder ? 'true' : 'false'}</td>
                  <td>{productSwap.checkForEveryRecurringOrder ? 'true' : 'false'}</td>
                  <td>{productSwap.name}</td>
                  <td>{productSwap.changeNextOrderDateBy}</td>
                  <td>{productSwap.forBillingCycle}</td>
                  <td>{productSwap.carryDiscountForward ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${productSwap.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productSwap.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productSwap.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Product Swaps found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ productSwap }: IRootState) => ({
  productSwapList: productSwap.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductSwap);
