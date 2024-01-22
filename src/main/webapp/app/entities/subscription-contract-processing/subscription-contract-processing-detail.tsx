import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-contract-processing.reducer';
import { ISubscriptionContractProcessing } from 'app/shared/model/subscription-contract-processing.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionContractProcessingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractProcessingDetail = (props: ISubscriptionContractProcessingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionContractProcessingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionContractProcessing [<b>{subscriptionContractProcessingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="contractId">Contract Id</span>
          </dt>
          <dd>{subscriptionContractProcessingEntity.contractId}</dd>
          <dt>
            <span id="attemptCount">Attempt Count</span>
          </dt>
          <dd>{subscriptionContractProcessingEntity.attemptCount}</dd>
        </dl>
        <Button tag={Link} to="/subscription-contract-processing" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-contract-processing/${subscriptionContractProcessingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionContractProcessing }: IRootState) => ({
  subscriptionContractProcessingEntity: subscriptionContractProcessing.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractProcessingDetail);
