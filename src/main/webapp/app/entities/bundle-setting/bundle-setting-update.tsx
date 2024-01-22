import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './bundle-setting.reducer';
import { IBundleSetting } from 'app/shared/model/bundle-setting.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBundleSettingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BundleSettingUpdate = (props: IBundleSettingUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { bundleSettingEntity, loading, updating } = props;

  const { customCss } = bundleSettingEntity;

  const handleClose = () => {
    props.history.push('/bundle-setting' + props.location.search);
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
        ...bundleSettingEntity,
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
          <h2 id="subscriptionApp.bundleSetting.home.createOrEditLabel">Create or edit a BundleSetting</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bundleSettingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="bundle-setting-id">ID</Label>
                  <AvInput id="bundle-setting-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="bundle-setting-shop">
                  Shop
                </Label>
                <AvField
                  id="bundle-setting-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="showOnProductPageLabel">
                  <AvInput id="bundle-setting-showOnProductPage" type="checkbox" className="form-check-input" name="showOnProductPage" />
                  Show On Product Page
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="showMultipleOnProductPageLabel">
                  <AvInput
                    id="bundle-setting-showMultipleOnProductPage"
                    type="checkbox"
                    className="form-check-input"
                    name="showMultipleOnProductPage"
                  />
                  Show Multiple On Product Page
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="actionButtonColorLabel" for="bundle-setting-actionButtonColor">
                  Action Button Color
                </Label>
                <AvField id="bundle-setting-actionButtonColor" type="text" name="actionButtonColor" />
              </AvGroup>
              <AvGroup>
                <Label id="actionButtonFontColorLabel" for="bundle-setting-actionButtonFontColor">
                  Action Button Font Color
                </Label>
                <AvField id="bundle-setting-actionButtonFontColor" type="text" name="actionButtonFontColor" />
              </AvGroup>
              <AvGroup>
                <Label id="productTitleColorLabel" for="bundle-setting-productTitleColor">
                  Product Title Color
                </Label>
                <AvField id="bundle-setting-productTitleColor" type="text" name="productTitleColor" />
              </AvGroup>
              <AvGroup>
                <Label id="productPriceColorLabel" for="bundle-setting-productPriceColor">
                  Product Price Color
                </Label>
                <AvField id="bundle-setting-productPriceColor" type="text" name="productPriceColor" />
              </AvGroup>
              <AvGroup>
                <Label id="redirectToLabel" for="bundle-setting-redirectTo">
                  Redirect To
                </Label>
                <AvInput
                  id="bundle-setting-redirectTo"
                  type="select"
                  className="form-control"
                  name="redirectTo"
                  value={(!isNew && bundleSettingEntity.redirectTo) || 'CHECKOUT'}
                >
                  <option value="CHECKOUT">CHECKOUT</option>
                  <option value="CART">CART</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="showProductPriceLabel">
                  <AvInput id="bundle-setting-showProductPrice" type="checkbox" className="form-check-input" name="showProductPrice" />
                  Show Product Price
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="oneTimeDiscountLabel">
                  <AvInput id="bundle-setting-oneTimeDiscount" type="checkbox" className="form-check-input" name="oneTimeDiscount" />
                  One Time Discount
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="showDiscountInCartLabel">
                  <AvInput id="bundle-setting-showDiscountInCart" type="checkbox" className="form-check-input" name="showDiscountInCart" />
                  Show Discount In Cart
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="selectorLabel" for="bundle-setting-selector">
                  Selector
                </Label>
                <AvField id="bundle-setting-selector" type="text" name="selector" />
              </AvGroup>
              <AvGroup>
                <Label id="placementLabel" for="bundle-setting-placement">
                  Placement
                </Label>
                <AvInput
                  id="bundle-setting-placement"
                  type="select"
                  className="form-control"
                  name="placement"
                  value={(!isNew && bundleSettingEntity.placement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="customCssLabel" for="bundle-setting-customCss">
                  Custom Css
                </Label>
                <AvInput id="bundle-setting-customCss" type="textarea" name="customCss" />
              </AvGroup>
              <AvGroup>
                <Label id="variantLabel" for="bundle-setting-variant">
                  Variant
                </Label>
                <AvField id="bundle-setting-variant" type="text" name="variant" />
              </AvGroup>
              <AvGroup>
                <Label id="deliveryFrequencyLabel" for="bundle-setting-deliveryFrequency">
                  Delivery Frequency
                </Label>
                <AvField id="bundle-setting-deliveryFrequency" type="text" name="deliveryFrequency" />
              </AvGroup>
              <AvGroup>
                <Label id="perDeliveryLabel" for="bundle-setting-perDelivery">
                  Per Delivery
                </Label>
                <AvField id="bundle-setting-perDelivery" type="text" name="perDelivery" />
              </AvGroup>
              <AvGroup>
                <Label id="discountPopupHeaderLabel" for="bundle-setting-discountPopupHeader">
                  Discount Popup Header
                </Label>
                <AvField id="bundle-setting-discountPopupHeader" type="text" name="discountPopupHeader" />
              </AvGroup>
              <AvGroup>
                <Label id="discountPopupAmountLabel" for="bundle-setting-discountPopupAmount">
                  Discount Popup Amount
                </Label>
                <AvField id="bundle-setting-discountPopupAmount" type="text" name="discountPopupAmount" />
              </AvGroup>
              <AvGroup>
                <Label id="discountPopupCheckoutMessageLabel" for="bundle-setting-discountPopupCheckoutMessage">
                  Discount Popup Checkout Message
                </Label>
                <AvField id="bundle-setting-discountPopupCheckoutMessage" type="text" name="discountPopupCheckoutMessage" />
              </AvGroup>
              <AvGroup>
                <Label id="discountPopupBuyLabel" for="bundle-setting-discountPopupBuy">
                  Discount Popup Buy
                </Label>
                <AvField id="bundle-setting-discountPopupBuy" type="text" name="discountPopupBuy" />
              </AvGroup>
              <AvGroup>
                <Label id="discountPopupNoLabel" for="bundle-setting-discountPopupNo">
                  Discount Popup No
                </Label>
                <AvField id="bundle-setting-discountPopupNo" type="text" name="discountPopupNo" />
              </AvGroup>
              <AvGroup check>
                <Label id="showDiscountPopupLabel">
                  <AvInput id="bundle-setting-showDiscountPopup" type="checkbox" className="form-check-input" name="showDiscountPopup" />
                  Show Discount Popup
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/bundle-setting" replace color="info">
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
  bundleSettingEntity: storeState.bundleSetting.entity,
  loading: storeState.bundleSetting.loading,
  updating: storeState.bundleSetting.updating,
  updateSuccess: storeState.bundleSetting.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  //setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleSettingUpdate);
