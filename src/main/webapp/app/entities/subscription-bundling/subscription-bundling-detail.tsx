import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-bundling.reducer';
import { ISubscriptionBundling } from 'app/shared/model/subscription-bundling.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionBundlingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionBundlingDetail = (props: ISubscriptionBundlingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionBundlingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionBundling [<b>{subscriptionBundlingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionBundlingEntity.shop}</dd>
          <dt>
            <span id="subscriptionBundlingEnabled">Subscription Bundling Enabled</span>
          </dt>
          <dd>{subscriptionBundlingEntity.subscriptionBundlingEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="subscriptionId">Subscription Id</span>
          </dt>
          <dd>{subscriptionBundlingEntity.subscriptionId}</dd>
          <dt>
            <span id="minProductCount">Min Product Count</span>
          </dt>
          <dd>{subscriptionBundlingEntity.minProductCount}</dd>
          <dt>
            <span id="maxProductCount">Max Product Count</span>
          </dt>
          <dd>{subscriptionBundlingEntity.maxProductCount}</dd>
          <dt>
            <span id="discount">Discount</span>
          </dt>
          <dd>{subscriptionBundlingEntity.discount}</dd>
          <dt>
            <span id="uniqueRef">Unique Ref</span>
          </dt>
          <dd>{subscriptionBundlingEntity.uniqueRef}</dd>
          <dt>
            <span id="bundleRedirect">Bundle Redirect</span>
          </dt>
          <dd>{subscriptionBundlingEntity.bundleRedirect}</dd>
          <dt>
            <span id="customRedirectURL">Custom Redirect URL</span>
          </dt>
          <dd>{subscriptionBundlingEntity.customRedirectURL}</dd>
          <dt>
            <span id="minOrderAmount">Min Order Amount</span>
          </dt>
          <dd>{subscriptionBundlingEntity.minOrderAmount}</dd>
          <dt>
            <span id="tieredDiscount">Tiered Discount</span>
          </dt>
          <dd>{subscriptionBundlingEntity.tieredDiscount}</dd>
          <dt>
            <span id="productViewStyle">Product View Style</span>
          </dt>
          <dd>{subscriptionBundlingEntity.productViewStyle}</dd>
          <dt>
            <span id="buildABoxType">Build A Box Type</span>
          </dt>
          <dd>{subscriptionBundlingEntity.buildABoxType}</dd>
          <dt>
            <span id="singleProductSettings">Single Product Settings</span>
          </dt>
          <dd>{subscriptionBundlingEntity.singleProductSettings}</dd>
          <dt>
            <span id="bundleTopHtml">Build-A-Box Top Html</span>
          </dt>
          <dd>{subscriptionBundlingEntity.bundleTopHtml}</dd>
          <dt>
            <span id="bundleBottomHtml">Build-A-Box Bottom HTML</span>
          </dt>
          <dd>{subscriptionBundlingEntity.bundleBottomHtml}</dd>
          <dt>
            <span id="proceedToCheckoutButtonText">Proceed To Checkout Button Text</span>
          </dt>
          <dd>{subscriptionBundlingEntity.proceedToCheckoutButtonText}</dd>
          <dt>
            <span id="chooseProductsText">Choose Products Text</span>
          </dt>
          <dd>{subscriptionBundlingEntity.chooseProductsText}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{subscriptionBundlingEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/subscription-bundling" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-bundling/${subscriptionBundlingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionBundling }: IRootState) => ({
  subscriptionBundlingEntity: subscriptionBundling.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBundlingDetail);
