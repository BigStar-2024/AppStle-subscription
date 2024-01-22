import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './plan-info.reducer';
import { IPlanInfo } from 'app/shared/model/plan-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlanInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const PlanInfo = (props: IPlanInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { planInfoList, match } = props;
  return (
    <div>
      <h2 id="plan-info-heading">
        Plan Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Plan Info
        </Link>
      </h2>
      <div className="table-responsive">
        {planInfoList && planInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Archived</th>
                <th>Plan Type</th>
                <th>Billing Type</th>
                <th>Based On</th>
                <th>Additional Details</th>
                <th>Features</th>
                <th>Trial Days</th>
                <th>Base Plan</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {planInfoList.map((planInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${planInfo.id}`} color="link" size="sm">
                      {planInfo.id}
                    </Button>
                  </td>
                  <td>{planInfo.name}</td>
                  <td>{planInfo.price}</td>
                  <td>{planInfo.archived ? 'true' : 'false'}</td>
                  <td>{planInfo.planType}</td>
                  <td>{planInfo.billingType}</td>
                  <td>{planInfo.basedOn}</td>
                  <td>{planInfo.additionalDetails}</td>
                  <td>{planInfo.features}</td>
                  <td>{planInfo.trialDays}</td>
                  <td>{planInfo.basePlan}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${planInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      {planInfo.archived &&
                       <>
                        <Button tag={Link} to={`${match.url}/${planInfo.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${planInfo.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </>
                      }
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Plan Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ planInfo }: IRootState) => ({
  planInfoList: planInfo.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PlanInfo);
