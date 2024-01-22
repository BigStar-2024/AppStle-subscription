import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './onboarding-info.reducer';
import { IOnboardingInfo } from 'app/shared/model/onboarding-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOnboardingInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const OnboardingInfo = (props: IOnboardingInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { onboardingInfoList, match, loading } = props;
  return (
    <div>
      <h2 id="onboarding-info-heading">
        Onboarding Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Onboarding Info
        </Link>
      </h2>
      <div className="table-responsive">
        {onboardingInfoList && onboardingInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Uncompleted Checklist Steps</th>
                <th>Completed Checklist Steps</th>
                <th>Checklist Completed</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {onboardingInfoList.map((onboardingInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${onboardingInfo.id}`} color="link" size="sm">
                      {onboardingInfo.id}
                    </Button>
                  </td>
                  <td>{onboardingInfo.shop}</td>
                  <td>{onboardingInfo.uncompletedChecklistSteps}</td>
                  <td>{onboardingInfo.completedChecklistSteps}</td>
                  <td>{onboardingInfo.checklistCompleted ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${onboardingInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${onboardingInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${onboardingInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Onboarding Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ onboardingInfo }: IRootState) => ({
  onboardingInfoList: onboardingInfo.entities,
  loading: onboardingInfo.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OnboardingInfo);
