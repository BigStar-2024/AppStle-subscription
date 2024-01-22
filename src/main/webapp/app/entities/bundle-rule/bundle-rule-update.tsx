import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './bundle-rule.reducer';
import { IBundleRule } from 'app/shared/model/bundle-rule.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBundleRuleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BundleRuleUpdate = (props: IBundleRuleUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { bundleRuleEntity, loading, updating } = props;

  const { products, variants } = bundleRuleEntity;

  const handleClose = () => {
    props.history.push('/bundle-rule' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    if (errors.length === 0) {
      const entity = {
        ...bundleRuleEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="subscriptionApp.bundleRule.home.createOrEditLabel">Create or edit a BundleRule</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bundleRuleEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="bundle-rule-id">ID</Label>
                  <AvInput id="bundle-rule-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="bundle-rule-shop">
                  Shop
                </Label>
                <AvField
                  id="bundle-rule-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="bundle-rule-name">
                  Name
                </Label>
                <AvField id="bundle-rule-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="titleLabel" for="bundle-rule-title">
                  Title
                </Label>
                <AvField id="bundle-rule-title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="bundle-rule-description">
                  Description
                </Label>
                <AvField id="bundle-rule-description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="priceSummaryLabel" for="bundle-rule-priceSummary">
                  Price Summary
                </Label>
                <AvField id="bundle-rule-priceSummary" type="text" name="priceSummary" />
              </AvGroup>
              <AvGroup>
                <Label id="actionButtonTextLabel" for="bundle-rule-actionButtonText">
                  Action Button Text
                </Label>
                <AvField id="bundle-rule-actionButtonText" type="text" name="actionButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="actionButtonDescriptionLabel" for="bundle-rule-actionButtonDescription">
                  Action Button Description
                </Label>
                <AvField id="bundle-rule-actionButtonDescription" type="text" name="actionButtonDescription" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="bundle-rule-status">
                  Status
                </Label>
                <AvInput
                  id="bundle-rule-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && bundleRuleEntity.status) || 'ACTIVE'}
                >
                  <option value="ACTIVE">ACTIVE</option>
                  <option value="PAUSED">PAUSED</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="showBundleWidgetLabel">
                  <AvInput id="bundle-rule-showBundleWidget" type="checkbox" className="form-check-input" name="showBundleWidget" />
                  Show Bundle Widget
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="customerIncludeTagsLabel" for="bundle-rule-customerIncludeTags">
                  Customer Include Tags
                </Label>
                <AvField id="bundle-rule-customerIncludeTags" type="text" name="customerIncludeTags" />
              </AvGroup>
              <AvGroup>
                <Label id="startDateLabel" for="bundle-rule-startDate">
                  Start Date
                </Label>
                <AvInput
                  id="bundle-rule-startDate"
                  type="datetime-local"
                  className="form-control"
                  name="startDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.bundleRuleEntity.startDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endDateLabel" for="bundle-rule-endDate">
                  End Date
                </Label>
                <AvInput
                  id="bundle-rule-endDate"
                  type="datetime-local"
                  className="form-control"
                  name="endDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.bundleRuleEntity.endDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="discountTypeLabel" for="bundle-rule-discountType">
                  Discount Type
                </Label>
                <AvInput
                  id="bundle-rule-discountType"
                  type="select"
                  className="form-control"
                  name="discountType"
                  value={(!isNew && bundleRuleEntity.discountType) || 'PERCENTAGE'}
                >
                  <option value="PERCENTAGE">PERCENTAGE</option>
                  <option value="FIXED_AMOUNT">FIXED_AMOUNT</option>
                  <option value="FIXED_BUNDLE_AMOUNT">FIXED_BUNDLE_AMOUNT</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="discountValueLabel" for="bundle-rule-discountValue">
                  Discount Value
                </Label>
                <AvField id="bundle-rule-discountValue" type="string" className="form-control" name="discountValue" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleLevelLabel" for="bundle-rule-bundleLevel">
                  Bundle Level
                </Label>
                <AvInput
                  id="bundle-rule-bundleLevel"
                  type="select"
                  className="form-control"
                  name="bundleLevel"
                  value={(!isNew && bundleRuleEntity.bundleLevel) || 'PRODUCT'}
                >
                  <option value="PRODUCT">PRODUCT</option>
                  <option value="VARIANT">VARIANT</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="productsLabel" for="bundle-rule-products">
                  Products
                </Label>
                <AvInput id="bundle-rule-products" type="textarea" name="products" />
              </AvGroup>
              <AvGroup>
                <Label id="variantsLabel" for="bundle-rule-variants">
                  Variants
                </Label>
                <AvInput id="bundle-rule-variants" type="textarea" name="variants" />
              </AvGroup>
              <AvGroup>
                <Label id="discountConditionLabel" for="bundle-rule-discountCondition">
                  Discount Condition
                </Label>
                <AvInput
                  id="bundle-rule-discountCondition"
                  type="select"
                  className="form-control"
                  name="discountCondition"
                  value={(!isNew && bundleRuleEntity.discountCondition) || 'BUY_ALL'}
                >
                  <option value="BUY_ALL">BUY_ALL</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="sequenceNoLabel" for="bundle-rule-sequenceNo">
                  Sequence No
                </Label>
                <AvField id="bundle-rule-sequenceNo" type="string" className="form-control" name="sequenceNo" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleTypeLabel" for="bundle-rule-bundleType">
                  Bundle Type
                </Label>
                <AvInput
                  id="bundle-rule-bundleType"
                  type="select"
                  className="form-control"
                  name="bundleType"
                  value={(!isNew && bundleRuleEntity.bundleType) || 'MIX_AND_MATCH'}
                >
                  <option value="MIX_AND_MATCH">MIX_AND_MATCH</option>
                  <option value="CLASSIC">CLASSIC</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="showCombinedSellingPlanLabel">
                  <AvInput
                    id="bundle-rule-showCombinedSellingPlan"
                    type="checkbox"
                    className="form-check-input"
                    name="showCombinedSellingPlan"
                  />
                  Show Combined Selling Plan
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="selectSubscriptionByDefaultLabel">
                  <AvInput
                    id="bundle-rule-selectSubscriptionByDefault"
                    type="checkbox"
                    className="form-check-input"
                    name="selectSubscriptionByDefault"
                  />
                  Select Subscription By Default
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="minimumNumberOfItemsLabel" for="bundle-rule-minimumNumberOfItems">
                  Minimum Number Of Items
                </Label>
                <AvField id="bundle-rule-minimumNumberOfItems" type="string" className="form-control" name="minimumNumberOfItems" />
              </AvGroup>
              <AvGroup>
                <Label id="maximumNumberOfItemsLabel" for="bundle-rule-maximumNumberOfItems">
                  Maximum Number Of Items
                </Label>
                <AvField id="bundle-rule-maximumNumberOfItems" type="string" className="form-control" name="maximumNumberOfItems" />
              </AvGroup>
              <AvGroup>
                <Label id="maxQuantityLabel" for="bundle-rule-maxQuantity">
                  Max Quantity
                </Label>
                <AvField id="bundle-rule-maxQuantity" type="string" className="form-control" name="maxQuantity" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/bundle-rule" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  bundleRuleEntity: storeState.bundleRule.entity,
  loading: storeState.bundleRule.loading,
  updating: storeState.bundleRule.updating,
  updateSuccess: storeState.bundleRule.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleRuleUpdate);
