import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order-info.reducer';
import { IOrderInfo } from 'app/shared/model/order-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderInfoDetail = (props: IOrderInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          OrderInfo [<b>{orderInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{orderInfoEntity.shop}</dd>
          <dt>
            <span id="orderId">Order Id</span>
          </dt>
          <dd>{orderInfoEntity.orderId}</dd>
          <dt>
            <span id="linesJson">Lines Json</span>
          </dt>
          <dd>{orderInfoEntity.linesJson}</dd>
        </dl>
        <Button tag={Link} to="/order-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-info/${orderInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ orderInfo }: IRootState) => ({
  orderInfoEntity: orderInfo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderInfoDetail);
