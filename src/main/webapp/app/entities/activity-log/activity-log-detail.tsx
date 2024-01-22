import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './activity-log.reducer';
import { IActivityLog } from 'app/shared/model/activity-log.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActivityLogDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActivityLogDetail = (props: IActivityLogDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { activityLogEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ActivityLog [<b>{activityLogEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{activityLogEntity.shop}</dd>
          <dt>
            <span id="entityId">Entity Id</span>
          </dt>
          <dd>{activityLogEntity.entityId}</dd>
          <dt>
            <span id="entityType">Entity Type</span>
          </dt>
          <dd>{activityLogEntity.entityType}</dd>
          <dt>
            <span id="eventSource">Event Source</span>
          </dt>
          <dd>{activityLogEntity.eventSource}</dd>
          <dt>
            <span id="eventType">Event Type</span>
          </dt>
          <dd>{activityLogEntity.eventType}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{activityLogEntity.status}</dd>
          <dt>
            <span id="createAt">Create At</span>
          </dt>
          <dd>
            <TextFormat value={activityLogEntity.createAt} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="additionalInfo">Additional Info</span>
          </dt>
          <dd>{activityLogEntity.additionalInfo}</dd>
        </dl>
        <Button tag={Link} to="/activity-log" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activity-log/${activityLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ activityLog }: IRootState) => ({
  activityLogEntity: activityLog.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActivityLogDetail);
