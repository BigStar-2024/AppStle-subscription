import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './processed-order-info.reducer';
import { IProcessedOrderInfo } from 'app/shared/model/processed-order-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProcessedOrderInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProcessedOrderInfoDetail = (props: IProcessedOrderInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { processedOrderInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ProcessedOrderInfo [<b>{processedOrderInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{processedOrderInfoEntity.shop}</dd>
          <dt>
            <span id="orderId">Order Id</span>
          </dt>
          <dd>{processedOrderInfoEntity.orderId}</dd>
          <dt>
            <span id="orderNumber">Order Number</span>
          </dt>
          <dd>{processedOrderInfoEntity.orderNumber}</dd>
          <dt>
            <span id="topicName">Topic Name</span>
          </dt>
          <dd>{processedOrderInfoEntity.topicName}</dd>
          <dt>
            <span id="processedTime">Processed Time</span>
          </dt>
          <dd>
            {processedOrderInfoEntity.processedTime ? (
              <TextFormat value={processedOrderInfoEntity.processedTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="triggerRuleInfoJson">Trigger Rule Info Json</span>
          </dt>
          <dd>{processedOrderInfoEntity.triggerRuleInfoJson}</dd>
          <dt>
            <span id="attachedTags">Attached Tags</span>
          </dt>
          <dd>{processedOrderInfoEntity.attachedTags}</dd>
          <dt>
            <span id="attachedTagsToNote">Attached Tags To Note</span>
          </dt>
          <dd>{processedOrderInfoEntity.attachedTagsToNote}</dd>
          <dt>
            <span id="removedTags">Removed Tags</span>
          </dt>
          <dd>{processedOrderInfoEntity.removedTags}</dd>
          <dt>
            <span id="removedTagsAfter">Removed Tags After</span>
          </dt>
          <dd>{processedOrderInfoEntity.removedTagsAfter}</dd>
          <dt>
            <span id="removedTagsBefore">Removed Tags Before</span>
          </dt>
          <dd>{processedOrderInfoEntity.removedTagsBefore}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{processedOrderInfoEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{processedOrderInfoEntity.lastName}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{processedOrderInfoEntity.status}</dd>
          <dt>
            <span id="delayedTaggingValue">Delayed Tagging Value</span>
          </dt>
          <dd>{processedOrderInfoEntity.delayedTaggingValue}</dd>
          <dt>
            <span id="delayedTaggingUnit">Delayed Tagging Unit</span>
          </dt>
          <dd>{processedOrderInfoEntity.delayedTaggingUnit}</dd>
        </dl>
        <Button tag={Link} to="/processed-order-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/processed-order-info/${processedOrderInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ processedOrderInfo }: IRootState) => ({
  processedOrderInfoEntity: processedOrderInfo.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProcessedOrderInfoDetail);
