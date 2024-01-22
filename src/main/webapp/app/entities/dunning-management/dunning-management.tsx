import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './dunning-management.reducer';
import { IDunningManagement } from 'app/shared/model/dunning-management.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDunningManagementProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const DunningManagement = (props: IDunningManagementProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { dunningManagementList, match, loading } = props;
  return (
    <div>
      <h2 id="dunning-management-heading">
        Dunning Managements
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Dunning Management
        </Link>
      </h2>
      <div className="table-responsive">
        {dunningManagementList && dunningManagementList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Retry Attempts</th>
                <th>Days Before Retrying</th>
                <th>Max Number Of Failures</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dunningManagementList.map((dunningManagement, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${dunningManagement.id}`} color="link" size="sm">
                      {dunningManagement.id}
                    </Button>
                  </td>
                  <td>{dunningManagement.shop}</td>
                  <td>{dunningManagement.retryAttempts}</td>
                  <td>{dunningManagement.daysBeforeRetrying}</td>
                  <td>{dunningManagement.maxNumberOfFailures}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${dunningManagement.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${dunningManagement.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${dunningManagement.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Dunning Managements found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ dunningManagement }: IRootState) => ({
  dunningManagementList: dunningManagement.entities,
  loading: dunningManagement.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DunningManagement);
