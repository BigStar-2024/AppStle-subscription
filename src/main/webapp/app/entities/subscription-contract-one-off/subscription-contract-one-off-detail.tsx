import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-contract-one-off.reducer';
import { ISubscriptionContractOneOff } from 'app/shared/model/subscription-contract-one-off.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionContractOneOffDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractOneOffDetail = (props: ISubscriptionContractOneOffDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionContractOneOffEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionContractOneOff [<b>{subscriptionContractOneOffEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionContractOneOffEntity.shop}</dd>
          <dt>
            <span id="billingAttemptId">Billing Attempt Id</span>
          </dt>
          <dd>{subscriptionContractOneOffEntity.billingAttemptId}</dd>
          <dt>
            <span id="subscriptionContractId">Subscription Contract Id</span>
          </dt>
          <dd>{subscriptionContractOneOffEntity.subscriptionContractId}</dd>
          <dt>
            <span id="variantId">Variant Id</span>
          </dt>
          <dd>{subscriptionContractOneOffEntity.variantId}</dd>
          <dt>
            <span id="variantHandle">Variant Handle</span>
          </dt>
          <dd>{subscriptionContractOneOffEntity.variantHandle}</dd>
          <dt>
            <span id="quantity">Quantity</span>
          </dt>
          <dd>{subscriptionContractOneOffEntity.quantity}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{subscriptionContractOneOffEntity.price}</dd>
        </dl>
        <Button tag={Link} to="/subscription-contract-one-off" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-contract-one-off/${subscriptionContractOneOffEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionContractOneOff }: IRootState) => ({
  subscriptionContractOneOffEntity: subscriptionContractOneOff.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractOneOffDetail);
