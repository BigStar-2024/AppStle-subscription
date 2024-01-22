import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './async-update-event-processing.reducer';
import { IAsyncUpdateEventProcessing } from 'app/shared/model/async-update-event-processing.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAsyncUpdateEventProcessingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AsyncUpdateEventProcessingDetail = (props: IAsyncUpdateEventProcessingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { asyncUpdateEventProcessingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          AsyncUpdateEventProcessing [<b>{asyncUpdateEventProcessingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="subscriptionContractId">Subscription Contract Id</span>
          </dt>
          <dd>{asyncUpdateEventProcessingEntity.subscriptionContractId}</dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>
            {asyncUpdateEventProcessingEntity.lastUpdated ? (
              <TextFormat value={asyncUpdateEventProcessingEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="tagModelJson">Tag Model Json</span>
          </dt>
          <dd>{asyncUpdateEventProcessingEntity.tagModelJson}</dd>
          <dt>
            <span id="firstTimeOrderTags">First Time Order Tags</span>
          </dt>
          <dd>{asyncUpdateEventProcessingEntity.firstTimeOrderTags}</dd>
        </dl>
        <Button tag={Link} to="/async-update-event-processing" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/async-update-event-processing/${asyncUpdateEventProcessingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ asyncUpdateEventProcessing }: IRootState) => ({
  asyncUpdateEventProcessingEntity: asyncUpdateEventProcessing.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AsyncUpdateEventProcessingDetail);
