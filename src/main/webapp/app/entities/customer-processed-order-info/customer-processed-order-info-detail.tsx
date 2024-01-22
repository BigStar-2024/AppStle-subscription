import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer-processed-order-info.reducer';
import { ICustomerProcessedOrderInfo } from 'app/shared/model/customer-processed-order-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerProcessedOrderInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerProcessedOrderInfoDetail = (props: ICustomerProcessedOrderInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { customerProcessedOrderInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CustomerProcessedOrderInfo [<b>{customerProcessedOrderInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.shop}</dd>
          <dt>
            <span id="orderId">Order Id</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.orderId}</dd>
          <dt>
            <span id="orderNumber">Order Number</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.orderNumber}</dd>
          <dt>
            <span id="customerNumber">Customer Number</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.customerNumber}</dd>
          <dt>
            <span id="topicName">Topic Name</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.topicName}</dd>
          <dt>
            <span id="processedTime">Processed Time</span>
          </dt>
          <dd>
            {customerProcessedOrderInfoEntity.processedTime ? (
              <TextFormat value={customerProcessedOrderInfoEntity.processedTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="triggerRuleInfoJson">Trigger Rule Info Json</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.triggerRuleInfoJson}</dd>
          <dt>
            <span id="attachedTags">Attached Tags</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.attachedTags}</dd>
          <dt>
            <span id="attachedTagsToNote">Attached Tags To Note</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.attachedTagsToNote}</dd>
          <dt>
            <span id="removedTags">Removed Tags</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.removedTags}</dd>
          <dt>
            <span id="removedTagsAfter">Removed Tags After</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.removedTagsAfter}</dd>
          <dt>
            <span id="removedTagsBefore">Removed Tags Before</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.removedTagsBefore}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.lastName}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.status}</dd>
          <dt>
            <span id="delayedTaggingValue">Delayed Tagging Value</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.delayedTaggingValue}</dd>
          <dt>
            <span id="delayedTaggingUnit">Delayed Tagging Unit</span>
          </dt>
          <dd>{customerProcessedOrderInfoEntity.delayedTaggingUnit}</dd>
        </dl>
        <Button tag={Link} to="/customer-processed-order-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer-processed-order-info/${customerProcessedOrderInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ customerProcessedOrderInfo }: IRootState) => ({
  customerProcessedOrderInfoEntity: customerProcessedOrderInfo.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerProcessedOrderInfoDetail);
