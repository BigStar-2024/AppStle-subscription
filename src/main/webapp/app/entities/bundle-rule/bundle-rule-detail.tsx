import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './bundle-rule.reducer';
import { IBundleRule } from 'app/shared/model/bundle-rule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBundleRuleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BundleRuleDetail = (props: IBundleRuleDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bundleRuleEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          BundleRule [<b>{bundleRuleEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{bundleRuleEntity.shop}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{bundleRuleEntity.name}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{bundleRuleEntity.title}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{bundleRuleEntity.description}</dd>
          <dt>
            <span id="priceSummary">Price Summary</span>
          </dt>
          <dd>{bundleRuleEntity.priceSummary}</dd>
          <dt>
            <span id="actionButtonText">Action Button Text</span>
          </dt>
          <dd>{bundleRuleEntity.actionButtonText}</dd>
          <dt>
            <span id="actionButtonDescription">Action Button Description</span>
          </dt>
          <dd>{bundleRuleEntity.actionButtonDescription}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{bundleRuleEntity.status}</dd>
          <dt>
            <span id="showBundleWidget">Show Bundle Widget</span>
          </dt>
          <dd>{bundleRuleEntity.showBundleWidget ? 'true' : 'false'}</dd>
          <dt>
            <span id="customerIncludeTags">Customer Include Tags</span>
          </dt>
          <dd>{bundleRuleEntity.customerIncludeTags}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>
            {bundleRuleEntity.startDate ? <TextFormat value={bundleRuleEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>{bundleRuleEntity.endDate ? <TextFormat value={bundleRuleEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="discountType">Discount Type</span>
          </dt>
          <dd>{bundleRuleEntity.discountType}</dd>
          <dt>
            <span id="discountValue">Discount Value</span>
          </dt>
          <dd>{bundleRuleEntity.discountValue}</dd>
          <dt>
            <span id="bundleLevel">Bundle Level</span>
          </dt>
          <dd>{bundleRuleEntity.bundleLevel}</dd>
          <dt>
            <span id="products">Products</span>
          </dt>
          <dd>{bundleRuleEntity.products}</dd>
          <dt>
            <span id="variants">Variants</span>
          </dt>
          <dd>{bundleRuleEntity.variants}</dd>
          <dt>
            <span id="discountCondition">Discount Condition</span>
          </dt>
          <dd>{bundleRuleEntity.discountCondition}</dd>
          <dt>
            <span id="sequenceNo">Sequence No</span>
          </dt>
          <dd>{bundleRuleEntity.sequenceNo}</dd>
          <dt>
            <span id="bundleType">Bundle Type</span>
          </dt>
          <dd>{bundleRuleEntity.bundleType}</dd>
          <dt>
            <span id="showCombinedSellingPlan">Show Combined Selling Plan</span>
          </dt>
          <dd>{bundleRuleEntity.showCombinedSellingPlan ? 'true' : 'false'}</dd>
          <dt>
            <span id="selectSubscriptionByDefault">Select Subscription By Default</span>
          </dt>
          <dd>{bundleRuleEntity.selectSubscriptionByDefault ? 'true' : 'false'}</dd>
          <dt>
            <span id="minimumNumberOfItems">Minimum Number Of Items</span>
          </dt>
          <dd>{bundleRuleEntity.minimumNumberOfItems}</dd>
          <dt>
            <span id="maximumNumberOfItems">Maximum Number Of Items</span>
          </dt>
          <dd>{bundleRuleEntity.maximumNumberOfItems}</dd>
          <dt>
            <span id="maxQuantity">Max Quantity</span>
          </dt>
          <dd>{bundleRuleEntity.maxQuantity}</dd>
        </dl>
        <Button tag={Link} to="/bundle-rule" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bundle-rule/${bundleRuleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bundleRule }: IRootState) => ({
  bundleRuleEntity: bundleRule.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleRuleDetail);
