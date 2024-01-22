import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './activity-updates-settings.reducer';
import { IActivityUpdatesSettings } from 'app/shared/model/activity-updates-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActivityUpdatesSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActivityUpdatesSettingsDetail = (props: IActivityUpdatesSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { activityUpdatesSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ActivityUpdatesSettings [<b>{activityUpdatesSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{activityUpdatesSettingsEntity.shop}</dd>
          <dt>
            <span id="summaryReportEnabled">Summary Report Enabled</span>
          </dt>
          <dd>{activityUpdatesSettingsEntity.summaryReportEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="summaryReportFrequency">Summary Report Frequency</span>
          </dt>
          <dd>{activityUpdatesSettingsEntity.summaryReportFrequency}</dd>
          <dt>
            <span id="summaryReportDeliverToEmail">Summary Report Deliver To Email</span>
          </dt>
          <dd>{activityUpdatesSettingsEntity.summaryReportDeliverToEmail}</dd>
          <dt>
            <span id="summaryReportTimePeriod">Summary Report Time Period</span>
          </dt>
          <dd>{activityUpdatesSettingsEntity.summaryReportTimePeriod}</dd>
          <dt>
            <span id="summaryReportLastSent">Summary Report Last Sent</span>
          </dt>
          <dd>
            {activityUpdatesSettingsEntity.summaryReportLastSent ? (
              <TextFormat value={activityUpdatesSettingsEntity.summaryReportLastSent} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="summaryReportProcessing">Summary Report Processing</span>
          </dt>
          <dd>{activityUpdatesSettingsEntity.summaryReportProcessing ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/activity-updates-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activity-updates-settings/${activityUpdatesSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ activityUpdatesSettings }: IRootState) => ({
  activityUpdatesSettingsEntity: activityUpdatesSettings.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActivityUpdatesSettingsDetail);
