import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './bulk-automation.reducer';
import { IBulkAutomation } from 'app/shared/model/bulk-automation.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBulkAutomationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BulkAutomationDetail = (props: IBulkAutomationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bulkAutomationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          BulkAutomation [<b>{bulkAutomationEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{bulkAutomationEntity.shop}</dd>
          <dt>
            <span id="automationType">Automation Type</span>
          </dt>
          <dd>{bulkAutomationEntity.automationType}</dd>
          <dt>
            <span id="running">Running</span>
          </dt>
          <dd>{bulkAutomationEntity.running ? 'true' : 'false'}</dd>
          <dt>
            <span id="startTime">Start Time</span>
          </dt>
          <dd>
            <TextFormat value={bulkAutomationEntity.startTime} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="endTime">End Time</span>
          </dt>
          <dd>
            <TextFormat value={bulkAutomationEntity.endTime} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="requestInfo">Request Info</span>
          </dt>
          <dd>{bulkAutomationEntity.requestInfo}</dd>
          <dt>
            <span id="errorInfo">Error Info</span>
          </dt>
          <dd>{bulkAutomationEntity.errorInfo}</dd>
          <dt>
            <span id="currentExecution">Current Execution</span>
          </dt>
          <dd>{bulkAutomationEntity.currentExecution}</dd>
        </dl>
        <Button tag={Link} to="/admin/bulk-automation" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/bulk-automation/${bulkAutomationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bulkAutomation }: IRootState) => ({
  bulkAutomationEntity: bulkAutomation.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BulkAutomationDetail);
