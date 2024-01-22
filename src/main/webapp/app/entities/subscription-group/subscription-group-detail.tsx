import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-group.reducer';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionGroupDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionGroupDetail = (props: ISubscriptionGroupDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionGroupEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionGroup [<b>{subscriptionGroupEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="productCount">Product Count</span>
          </dt>
          <dd>{subscriptionGroupEntity.productCount}</dd>
          <dt>
            <span id="productVariantCount">Product Variant Count</span>
          </dt>
          <dd>{subscriptionGroupEntity.productVariantCount}</dd>
          <dt>
            <span id="frequencyCount">Frequency Count</span>
          </dt>
          <dd>{subscriptionGroupEntity.frequencyCount}</dd>
          <dt>
            <span id="frequencyInterval">Frequency Interval</span>
          </dt>
          <dd>{subscriptionGroupEntity.frequencyInterval}</dd>
          <dt>
            <span id="frequencyName">Frequency Name</span>
          </dt>
          <dd>{subscriptionGroupEntity.frequencyName}</dd>
          <dt>
            <span id="groupName">Group Name</span>
          </dt>
          <dd>{subscriptionGroupEntity.groupName}</dd>
          <dt>
            <span id="productIds">Product Ids</span>
          </dt>
          <dd>{subscriptionGroupEntity.productIds}</dd>
          <dt>
            <span id="discountOffer">Discount Offer</span>
          </dt>
          <dd>{subscriptionGroupEntity.discountOffer}</dd>
          <dt>
            <span id="discountType">Discount Type</span>
          </dt>
          <dd>{subscriptionGroupEntity.discountType}</dd>
          <dt>
            <span id="discountEnabled">Discount Enabled</span>
          </dt>
          <dd>{subscriptionGroupEntity.discountEnabled ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/subscription-group" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-group/${subscriptionGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionGroup }: IRootState) => ({
  subscriptionGroupEntity: subscriptionGroup.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionGroupDetail);
