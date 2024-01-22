import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './backdating-job-summary.reducer';
import { IBackdatingJobSummary } from 'app/shared/model/backdating-job-summary.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBackdatingJobSummaryProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const BackdatingJobSummary = (props: IBackdatingJobSummaryProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { backdatingJobSummaryList, match, loading } = props;
  return (
    <div>
      <h2 id="backdating-job-summary-heading">
        Backdating Job Summaries
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Backdating Job Summary
        </Link>
      </h2>
      <div className="table-responsive">
        {backdatingJobSummaryList && backdatingJobSummaryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Job Orders Type</th>
                <th>Job Orders Begin Date</th>
                <th>Job Orders End Date</th>
                <th>Job Rules Type</th>
                <th>Trigger Rule Ids</th>
                <th>Application Charge Id</th>
                <th>Charge</th>
                <th>Orders Count</th>
                <th>Status</th>
                <th>Payment Accepted</th>
                <th>Order Migration Identifier</th>
                <th>Total Orders Completed</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {backdatingJobSummaryList.map((backdatingJobSummary, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${backdatingJobSummary.id}`} color="link" size="sm">
                      {backdatingJobSummary.id}
                    </Button>
                  </td>
                  <td>{backdatingJobSummary.shop}</td>
                  <td>{backdatingJobSummary.jobOrdersType}</td>
                  <td>
                    {backdatingJobSummary.jobOrdersBeginDate ? (
                      <TextFormat type="date" value={backdatingJobSummary.jobOrdersBeginDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {backdatingJobSummary.jobOrdersEndDate ? (
                      <TextFormat type="date" value={backdatingJobSummary.jobOrdersEndDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{backdatingJobSummary.jobRulesType}</td>
                  <td>{backdatingJobSummary.triggerRuleIds}</td>
                  <td>{backdatingJobSummary.applicationChargeId}</td>
                  <td>{backdatingJobSummary.charge}</td>
                  <td>{backdatingJobSummary.ordersCount}</td>
                  <td>{backdatingJobSummary.status}</td>
                  <td>{backdatingJobSummary.paymentAccepted ? 'true' : 'false'}</td>
                  <td>{backdatingJobSummary.orderMigrationIdentifier}</td>
                  <td>{backdatingJobSummary.totalOrdersCompleted}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${backdatingJobSummary.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${backdatingJobSummary.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${backdatingJobSummary.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Backdating Job Summaries found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ backdatingJobSummary }: IRootState) => ({
  backdatingJobSummaryList: backdatingJobSummary.entities,
  loading: backdatingJobSummary.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BackdatingJobSummary);
