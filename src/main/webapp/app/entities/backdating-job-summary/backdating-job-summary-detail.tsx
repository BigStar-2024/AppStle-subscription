import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './backdating-job-summary.reducer';
import { IBackdatingJobSummary } from 'app/shared/model/backdating-job-summary.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBackdatingJobSummaryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BackdatingJobSummaryDetail = (props: IBackdatingJobSummaryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { backdatingJobSummaryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          BackdatingJobSummary [<b>{backdatingJobSummaryEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.shop}</dd>
          <dt>
            <span id="jobOrdersType">Job Orders Type</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.jobOrdersType}</dd>
          <dt>
            <span id="jobOrdersBeginDate">Job Orders Begin Date</span>
          </dt>
          <dd>
            {backdatingJobSummaryEntity.jobOrdersBeginDate ? (
              <TextFormat value={backdatingJobSummaryEntity.jobOrdersBeginDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="jobOrdersEndDate">Job Orders End Date</span>
          </dt>
          <dd>
            {backdatingJobSummaryEntity.jobOrdersEndDate ? (
              <TextFormat value={backdatingJobSummaryEntity.jobOrdersEndDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="jobRulesType">Job Rules Type</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.jobRulesType}</dd>
          <dt>
            <span id="triggerRuleIds">Trigger Rule Ids</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.triggerRuleIds}</dd>
          <dt>
            <span id="applicationChargeId">Application Charge Id</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.applicationChargeId}</dd>
          <dt>
            <span id="charge">Charge</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.charge}</dd>
          <dt>
            <span id="ordersCount">Orders Count</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.ordersCount}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.status}</dd>
          <dt>
            <span id="paymentAccepted">Payment Accepted</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.paymentAccepted ? 'true' : 'false'}</dd>
          <dt>
            <span id="orderMigrationIdentifier">Order Migration Identifier</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.orderMigrationIdentifier}</dd>
          <dt>
            <span id="totalOrdersCompleted">Total Orders Completed</span>
          </dt>
          <dd>{backdatingJobSummaryEntity.totalOrdersCompleted}</dd>
        </dl>
        <Button tag={Link} to="/backdating-job-summary" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/backdating-job-summary/${backdatingJobSummaryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ backdatingJobSummary }: IRootState) => ({
  backdatingJobSummaryEntity: backdatingJobSummary.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BackdatingJobSummaryDetail);
