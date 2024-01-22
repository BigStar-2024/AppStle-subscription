import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './membership-discount.reducer';
import { IMembershipDiscount } from 'app/shared/model/membership-discount.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMembershipDiscountProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const MembershipDiscount = (props: IMembershipDiscountProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { membershipDiscountList, match, loading } = props;
  return (
    <div>
      <h2 id="membership-discount-heading">
        Membership Discounts
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Membership Discount
        </Link>
      </h2>
      <div className="table-responsive">
        {membershipDiscountList && membershipDiscountList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Title</th>
                <th>Discount</th>
                <th>Customer Tags</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {membershipDiscountList.map((membershipDiscount, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${membershipDiscount.id}`} color="link" size="sm">
                      {membershipDiscount.id}
                    </Button>
                  </td>
                  <td>{membershipDiscount.shop}</td>
                  <td>{membershipDiscount.title}</td>
                  <td>{membershipDiscount.discount}</td>
                  <td>{membershipDiscount.customerTags}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${membershipDiscount.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${membershipDiscount.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${membershipDiscount.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Membership Discounts found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ membershipDiscount }: IRootState) => ({
  membershipDiscountList: membershipDiscount.entities,
  loading: membershipDiscount.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscount);
