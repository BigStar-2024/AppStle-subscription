import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './subscription-bundling.reducer';
import { ISubscriptionBundling } from 'app/shared/model/subscription-bundling.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionBundlingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionBundlingUpdate = (props: ISubscriptionBundlingUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionBundlingEntity, loading, updating } = props;

  const { tieredDiscount, singleProductSettings } = subscriptionBundlingEntity;

  const handleClose = () => {
    props.history.push('/subscription-bundling');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
//    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
//    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...subscriptionBundlingEntity,
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
          <h2 id="subscriptionApp.subscriptionBundling.home.createOrEditLabel">Create or edit a SubscriptionBundling</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionBundlingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-bundling-id">ID</Label>
                  <AvInput id="subscription-bundling-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-bundling-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-bundling-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="subscriptionBundlingEnabledLabel">
                  <AvInput
                    id="subscription-bundling-subscriptionBundlingEnabled"
                    type="checkbox"
                    className="form-check-input"
                    name="subscriptionBundlingEnabled"
                  />
                  Subscription Bundling Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionIdLabel" for="subscription-bundling-subscriptionId">
                  Subscription Id
                </Label>
                <AvField id="subscription-bundling-subscriptionId" type="string" className="form-control" name="subscriptionId" />
              </AvGroup>
              <AvGroup>
                <Label id="minProductCountLabel" for="subscription-bundling-minProductCount">
                  Min Product Count
                </Label>
                <AvField id="subscription-bundling-minProductCount" type="string" className="form-control" name="minProductCount" />
              </AvGroup>
              <AvGroup>
                <Label id="maxProductCountLabel" for="subscription-bundling-maxProductCount">
                  Max Product Count
                </Label>
                <AvField id="subscription-bundling-maxProductCount" type="string" className="form-control" name="maxProductCount" />
              </AvGroup>
              <AvGroup>
                <Label id="discountLabel" for="subscription-bundling-discount">
                  Discount
                </Label>
                <AvField id="subscription-bundling-discount" type="string" className="form-control" name="discount" />
              </AvGroup>
              <AvGroup>
                <Label id="uniqueRefLabel" for="subscription-bundling-uniqueRef">
                  Unique Ref
                </Label>
                <AvField id="subscription-bundling-uniqueRef" type="text" name="uniqueRef" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleRedirectLabel" for="subscription-bundling-bundleRedirect">
                  Bundle Redirect
                </Label>
                <AvInput
                  id="subscription-bundling-bundleRedirect"
                  type="select"
                  className="form-control"
                  name="bundleRedirect"
                  value={(!isNew && subscriptionBundlingEntity.bundleRedirect) || 'CART'}
                >
                  <option value="CART">CART</option>
                  <option value="CHECKOUT">CHECKOUT</option>
                  <option value="CUSTOM">CUSTOM</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="customRedirectURLLabel" for="subscription-bundling-customRedirectURL">
                  Custom Redirect URL
                </Label>
                <AvField id="subscription-bundling-customRedirectURL" type="text" name="customRedirectURL" />
              </AvGroup>
              <AvGroup>
                <Label id="minOrderAmountLabel" for="subscription-bundling-minOrderAmount">
                  Min Order Amount
                </Label>
                <AvField id="subscription-bundling-minOrderAmount" type="string" className="form-control" name="minOrderAmount" />
              </AvGroup>
              <AvGroup>
                <Label id="tieredDiscountLabel" for="subscription-bundling-tieredDiscount">
                  Tiered Discount
                </Label>
                <AvInput id="subscription-bundling-tieredDiscount" type="textarea" name="tieredDiscount" />
              </AvGroup>
              <AvGroup>
                <Label id="productViewStyleLabel" for="subscription-bundling-productViewStyle">
                  Product View Style
                </Label>
                <AvInput
                  id="subscription-bundling-productViewStyle"
                  type="select"
                  className="form-control"
                  name="productViewStyle"
                  value={(!isNew && subscriptionBundlingEntity.productViewStyle) || 'QUICK_ADD'}
                >
                  <option value="QUICK_ADD">QUICK_ADD</option>
                  <option value="VIEW_DETAILS">VIEW_DETAILS</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="buildABoxTypeLabel" for="subscription-bundling-buildABoxType">
                  Build A Box Type
                </Label>
                <AvInput
                  id="subscription-bundling-buildABoxType"
                  type="select"
                  className="form-control"
                  name="buildABoxType"
                  value={(!isNew && subscriptionBundlingEntity.buildABoxType) || 'CLASSIC'}
                >
                  <option value="CLASSIC">CLASSIC</option>
                  <option value="SINGLE_PRODUCT">SINGLE_PRODUCT</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="singleProductSettingsLabel" for="subscription-bundling-singleProductSettings">
                  Single Product Settings
                </Label>
                <AvInput id="subscription-bundling-singleProductSettings" type="textarea" name="singleProductSettings" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleTopHtmlLabel" for="subscription-bundling-bundleTopHtml">
                  Build-A-Box Top Html
                </Label>
                <AvInput id="subscription-bundling-bundleTopHtml" type="textarea" name="bundleTopHtml" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleBottomHtmlLabel" for="subscription-bundling-bundleBottomHtml">
                  Build-A-Box Bottom HTML
                </Label>
                <AvInput id="subscription-bundling-bundleBottomHtml" type="textarea" name="bundleBottomHtml" />
              </AvGroup>
              <AvGroup>
                <Label id="proceedToCheckoutButtonTextLabel" for="subscription-bundling-proceedToCheckoutButtonText">
                  Proceed To Checkout Button Text
                </Label>
                <AvField id="subscription-bundling-proceedToCheckoutButtonText" type="text" name="proceedToCheckoutButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="chooseProductsTextLabel" for="subscription-bundling-chooseProductsText">
                  Choose Products Text
                </Label>
                <AvField id="subscription-bundling-chooseProductsText" type="text" name="chooseProductsText" />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="subscription-bundling-name">
                  Name
                </Label>
                <AvField id="subscription-bundling-name" type="text" name="name" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-bundling" replace color="info">
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
  subscriptionBundlingEntity: storeState.subscriptionBundling.entity,
  loading: storeState.subscriptionBundling.loading,
  updating: storeState.subscriptionBundling.updating,
  updateSuccess: storeState.subscriptionBundling.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
//  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBundlingUpdate);
