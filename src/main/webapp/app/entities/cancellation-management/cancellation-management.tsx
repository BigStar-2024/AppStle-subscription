import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './cancellation-management.reducer';
import { ICancellationManagement } from 'app/shared/model/cancellation-management.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICancellationManagementProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CancellationManagement = (props: ICancellationManagementProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { cancellationManagementList, match, loading } = props;
  return (
    <div>
      <h2 id="cancellation-management-heading">
        Cancellation Managements
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Cancellation Management
        </Link>
      </h2>
      <div className="table-responsive">
        {cancellationManagementList && cancellationManagementList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Cancellation Type</th>
                <th>Cancellation Instructions Text</th>
                <th>Cancellation Reasons JSON</th>
                <th>Pause Instructions Text</th>
                <th>Pause Duration Cycle</th>
                <th>Enable Discount Email</th>
                <th>Discount Email Address</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cancellationManagementList.map((cancellationManagement, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${cancellationManagement.id}`} color="link" size="sm">
                      {cancellationManagement.id}
                    </Button>
                  </td>
                  <td>{cancellationManagement.shop}</td>
                  <td>{cancellationManagement.cancellationType}</td>
                  <td>{cancellationManagement.cancellationInstructionsText}</td>
                  <td>{cancellationManagement.cancellationReasonsJSON}</td>
                  <td>{cancellationManagement.pauseInstructionsText}</td>
                  <td>{cancellationManagement.pauseDurationCycle}</td>
                  <td>{cancellationManagement.enableDiscountEmail ? 'true' : 'false'}</td>
                  <td>{cancellationManagement.discountEmailAddress}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${cancellationManagement.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${cancellationManagement.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${cancellationManagement.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Cancellation Managements found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ cancellationManagement }: IRootState) => ({
  cancellationManagementList: cancellationManagement.entities,
  loading: cancellationManagement.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CancellationManagement);
