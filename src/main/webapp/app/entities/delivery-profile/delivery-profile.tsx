import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './delivery-profile.reducer';
import { IDeliveryProfile } from 'app/shared/model/delivery-profile.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDeliveryProfileProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const DeliveryProfile = (props: IDeliveryProfileProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { deliveryProfileList, match, loading } = props;
  return (
    <div>
      <h2 id="delivery-profile-heading">
        Delivery Profiles
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Delivery Profile
        </Link>
      </h2>
      <div className="table-responsive">
        {deliveryProfileList && deliveryProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Delivery Profile Id</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {deliveryProfileList.map((deliveryProfile, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${deliveryProfile.id}`} color="link" size="sm">
                      {deliveryProfile.id}
                    </Button>
                  </td>
                  <td>{deliveryProfile.shop}</td>
                  <td>{deliveryProfile.deliveryProfileId}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${deliveryProfile.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${deliveryProfile.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${deliveryProfile.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Delivery Profiles found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ deliveryProfile }: IRootState) => ({
  deliveryProfileList: deliveryProfile.entities,
  loading: deliveryProfile.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DeliveryProfile);
