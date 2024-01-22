import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './membership-discount-products.reducer';
import { IMembershipDiscountProducts } from 'app/shared/model/membership-discount-products.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMembershipDiscountProductsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const MembershipDiscountProducts = (props: IMembershipDiscountProductsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { membershipDiscountProductsList, match, loading } = props;
  return (
    <div>
      <h2 id="membership-discount-products-heading">
        Membership Discount Products
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Membership Discount Products
        </Link>
      </h2>
      <div className="table-responsive">
        {membershipDiscountProductsList && membershipDiscountProductsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Membership Discount Id</th>
                <th>Product Id</th>
                <th>Product Title</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {membershipDiscountProductsList.map((membershipDiscountProducts, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${membershipDiscountProducts.id}`} color="link" size="sm">
                      {membershipDiscountProducts.id}
                    </Button>
                  </td>
                  <td>{membershipDiscountProducts.shop}</td>
                  <td>{membershipDiscountProducts.membershipDiscountId}</td>
                  <td>{membershipDiscountProducts.productId}</td>
                  <td>{membershipDiscountProducts.productTitle}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${membershipDiscountProducts.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${membershipDiscountProducts.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${membershipDiscountProducts.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Membership Discount Products found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ membershipDiscountProducts }: IRootState) => ({
  membershipDiscountProductsList: membershipDiscountProducts.entities,
  loading: membershipDiscountProducts.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscountProducts);
