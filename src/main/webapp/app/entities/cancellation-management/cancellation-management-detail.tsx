import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './cancellation-management.reducer';
import { ICancellationManagement } from 'app/shared/model/cancellation-management.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICancellationManagementDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CancellationManagementDetail = (props: ICancellationManagementDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { cancellationManagementEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CancellationManagement [<b>{cancellationManagementEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{cancellationManagementEntity.shop}</dd>
          <dt>
            <span id="cancellationType">Cancellation Type</span>
          </dt>
          <dd>{cancellationManagementEntity.cancellationType}</dd>
          <dt>
            <span id="cancellationInstructionsText">Cancellation Instructions Text</span>
          </dt>
          <dd>{cancellationManagementEntity.cancellationInstructionsText}</dd>
          <dt>
            <span id="cancellationReasonsJSON">Cancellation Reasons JSON</span>
          </dt>
          <dd>{cancellationManagementEntity.cancellationReasonsJSON}</dd>
          <dt>
            <span id="pauseInstructionsText">Pause Instructions Text</span>
          </dt>
          <dd>{cancellationManagementEntity.pauseInstructionsText}</dd>
          <dt>
            <span id="pauseDurationCycle">Pause Duration Cycle</span>
          </dt>
          <dd>{cancellationManagementEntity.pauseDurationCycle}</dd>
          <dt>
            <span id="enableDiscountEmail">Enable Discount Email</span>
          </dt>
          <dd>{cancellationManagementEntity.enableDiscountEmail ? 'true' : 'false'}</dd>
          <dt>
            <span id="discountEmailAddress">Discount Email Address</span>
          </dt>
          <dd>{cancellationManagementEntity.discountEmailAddress}</dd>
        </dl>
        <Button tag={Link} to="/cancellation-management" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cancellation-management/${cancellationManagementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ cancellationManagement }: IRootState) => ({
  cancellationManagementEntity: cancellationManagement.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CancellationManagementDetail);
