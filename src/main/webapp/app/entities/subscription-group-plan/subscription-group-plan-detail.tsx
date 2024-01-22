import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-group-plan.reducer';
import { ISubscriptionGroupPlan } from 'app/shared/model/subscription-group-plan.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionGroupPlanDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionGroupPlanDetail = (props: ISubscriptionGroupPlanDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionGroupPlanEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionGroupPlan [<b>{subscriptionGroupPlanEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.shop}</dd>
          <dt>
            <span id="groupName">Group Name</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.groupName}</dd>
          <dt>
            <span id="subscriptionId">Subscription Id</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.subscriptionId}</dd>
          <dt>
            <span id="productCount">Product Count</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.productCount}</dd>
          <dt>
            <span id="productVariantCount">Product Variant Count</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.productVariantCount}</dd>
          <dt>
            <span id="infoJson">Info Json</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.infoJson}</dd>
          <dt>
            <span id="productIds">Product Ids</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.productIds}</dd>
          <dt>
            <span id="variantIds">Variant Ids</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.variantIds}</dd>
          <dt>
            <span id="variantProductIds">Variant Product Ids</span>
          </dt>
          <dd>{subscriptionGroupPlanEntity.variantProductIds}</dd>
        </dl>
        <Button tag={Link} to="/subscription-group-plan" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-group-plan/${subscriptionGroupPlanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionGroupPlan }: IRootState) => ({
  subscriptionGroupPlanEntity: subscriptionGroupPlan.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionGroupPlanDetail);
