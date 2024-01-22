import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './subscription-bundle-settings.reducer';
import { ISubscriptionBundleSettings } from 'app/shared/model/subscription-bundle-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionBundleSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionBundleSettingsUpdate = (props: ISubscriptionBundleSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionBundleSettingsEntity, loading, updating } = props;

  const { bundleTopHtml, bundleBottomHtml } = subscriptionBundleSettingsEntity;

  const handleClose = () => {
    props.history.push('/subscription-bundle-settings');
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
        ...subscriptionBundleSettingsEntity,
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
          <h2 id="subscriptionApp.subscriptionBundleSettings.home.createOrEditLabel">Create or edit a SubscriptionBundleSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionBundleSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-bundle-settings-id">ID</Label>
                  <AvInput id="subscription-bundle-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-bundle-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-bundle-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="selectedFrequencyLabelTextLabel" for="subscription-bundle-settings-selectedFrequencyLabelText">
                  Selected Frequency Label Text
                </Label>
                <AvField
                  id="subscription-bundle-settings-selectedFrequencyLabelText"
                  type="text"
                  name="selectedFrequencyLabelText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="addButtonTextLabel" for="subscription-bundle-settings-addButtonText">
                  Add Button Text
                </Label>
                <AvField
                  id="subscription-bundle-settings-addButtonText"
                  type="text"
                  name="addButtonText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="selectMinimumProductButtonTextLabel" for="subscription-bundle-settings-selectMinimumProductButtonText">
                  Select Minimum Product Button Text
                </Label>
                <AvField
                  id="subscription-bundle-settings-selectMinimumProductButtonText"
                  type="text"
                  name="selectMinimumProductButtonText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="productsToProceedTextLabel" for="subscription-bundle-settings-productsToProceedText">
                  Products To Proceed Text
                </Label>
                <AvField
                  id="subscription-bundle-settings-productsToProceedText"
                  type="text"
                  name="productsToProceedText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="proceedToCheckoutButtonTextLabel" for="subscription-bundle-settings-proceedToCheckoutButtonText">
                  Proceed To Checkout Button Text
                </Label>
                <AvField
                  id="subscription-bundle-settings-proceedToCheckoutButtonText"
                  type="text"
                  name="proceedToCheckoutButtonText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="myDeliveryTextLabel" for="subscription-bundle-settings-myDeliveryText">
                  My Delivery Text
                </Label>
                <AvField
                  id="subscription-bundle-settings-myDeliveryText"
                  type="text"
                  name="myDeliveryText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="bundleTopHtmlLabel" for="subscription-bundle-settings-bundleTopHtml">
                  Build-A-Box Top Html
                </Label>
                <AvInput id="subscription-bundle-settings-bundleTopHtml" type="textarea" name="bundleTopHtml" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleBottomHtmlLabel" for="subscription-bundle-settings-bundleBottomHtml">
                  Build-A-Box Bottom HTML
                </Label>
                <AvInput id="subscription-bundle-settings-bundleBottomHtml" type="textarea" name="bundleBottomHtml" />
              </AvGroup>
              <AvGroup>
                <Label id="failedToAddTitleTextLabel" for="subscription-bundle-settings-failedToAddTitleText">
                  Failed To Add Title Text
                </Label>
                <AvField id="subscription-bundle-settings-failedToAddTitleText" type="text" name="failedToAddTitleText" />
              </AvGroup>
              <AvGroup>
                <Label id="okBtnTextLabel" for="subscription-bundle-settings-okBtnText">
                  Ok Btn Text
                </Label>
                <AvField id="subscription-bundle-settings-okBtnText" type="text" name="okBtnText" />
              </AvGroup>
              <AvGroup>
                <Label id="failedToAddMsgTextLabel" for="subscription-bundle-settings-failedToAddMsgText">
                  Failed To Add Msg Text
                </Label>
                <AvField id="subscription-bundle-settings-failedToAddMsgText" type="text" name="failedToAddMsgText" />
              </AvGroup>
              <AvGroup>
                <Label id="buttonColorLabel" for="subscription-bundle-settings-buttonColor">
                  Button Color
                </Label>
                <AvField id="subscription-bundle-settings-buttonColor" type="text" name="buttonColor" />
              </AvGroup>
              <AvGroup>
                <Label id="backgroundColorLabel" for="subscription-bundle-settings-backgroundColor">
                  Background Color
                </Label>
                <AvField id="subscription-bundle-settings-backgroundColor" type="text" name="backgroundColor" />
              </AvGroup>
              <AvGroup>
                <Label id="pageBackgroundColorLabel" for="subscription-bundle-settings-pageBackgroundColor">
                  Page Background Color
                </Label>
                <AvField id="subscription-bundle-settings-pageBackgroundColor" type="text" name="pageBackgroundColor" />
              </AvGroup>
              <AvGroup>
                <Label id="buttonBackgroundColorLabel" for="subscription-bundle-settings-buttonBackgroundColor">
                  Button Background Color
                </Label>
                <AvField id="subscription-bundle-settings-buttonBackgroundColor" type="text" name="buttonBackgroundColor" />
              </AvGroup>
              <AvGroup>
                <Label id="ProductTitleFontColorLabel" for="subscription-bundle-settings-ProductTitleFontColor">
                  Product Title Font Color
                </Label>
                <AvField id="subscription-bundle-settings-ProductTitleFontColor" type="text" name="ProductTitleFontColor" />
              </AvGroup>
              <AvGroup>
                <Label id="variantNotAvailableLabel" for="subscription-bundle-settings-variantNotAvailable">
                  Variant Not Available
                </Label>
                <AvField id="subscription-bundle-settings-variantNotAvailable" type="text" name="variantNotAvailable" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleRedirectLabel" for="subscription-bundle-settings-bundleRedirect">
                  Bundle Redirect
                </Label>
                <AvInput
                  id="subscription-bundle-settings-bundleRedirect"
                  type="select"
                  className="form-control"
                  name="bundleRedirect"
                  value={(!isNew && subscriptionBundleSettingsEntity.bundleRedirect) || 'CART'}
                >
                  <option value="CART">CART</option>
                  <option value="CHECKOUT">CHECKOUT</option>
                  <option value="CUSTOM">CUSTOM</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="isBundleWithoutScrollLabel">
                  <AvInput
                    id="subscription-bundle-settings-isBundleWithoutScroll"
                    type="checkbox"
                    className="form-check-input"
                    name="isBundleWithoutScroll"
                  />
                  Is Bundle Without Scroll
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLengthLabel" for="subscription-bundle-settings-descriptionLength">
                  Description Length
                </Label>
                <AvField
                  id="subscription-bundle-settings-descriptionLength"
                  type="string"
                  className="form-control"
                  name="descriptionLength"
                />
              </AvGroup>
              <AvGroup>
                <Label id="currencySwitcherClassNameLabel" for="subscription-bundle-settings-currencySwitcherClassName">
                  Currency Switcher Class Name
                </Label>
                <AvField id="subscription-bundle-settings-currencySwitcherClassName" type="text" name="currencySwitcherClassName" />
              </AvGroup>
              <AvGroup>
                <Label id="productPriceFormatFieldLabel" for="subscription-bundle-settings-productPriceFormatField">
                  Product Price Format Field
                </Label>
                <AvField id="subscription-bundle-settings-productPriceFormatField" type="text" name="productPriceFormatField" />
              </AvGroup>
              <AvGroup>
                <Label id="customRedirectURLLabel" for="subscription-bundle-settings-customRedirectURL">
                  Custom Redirect URL
                </Label>
                <AvField id="subscription-bundle-settings-customRedirectURL" type="text" name="customRedirectURL" />
              </AvGroup>
              <AvGroup>
                <Label id="viewProductLabel" for="subscription-bundle-settings-viewProduct">
                  View Product
                </Label>
                <AvField id="subscription-bundle-settings-viewProduct" type="text" name="viewProduct" />
              </AvGroup>
              <AvGroup>
                <Label id="productDetailsLabel" for="subscription-bundle-settings-productDetails">
                  Product Details
                </Label>
                <AvField id="subscription-bundle-settings-productDetails" type="text" name="productDetails" />
              </AvGroup>
              <AvGroup>
                <Label id="editQuantityLabel" for="subscription-bundle-settings-editQuantity">
                  Edit Quantity
                </Label>
                <AvField id="subscription-bundle-settings-editQuantity" type="text" name="editQuantity" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLabel" for="subscription-bundle-settings-cart">
                  Cart
                </Label>
                <AvField id="subscription-bundle-settings-cart" type="text" name="cart" />
              </AvGroup>
              <AvGroup>
                <Label id="shoppingCartLabel" for="subscription-bundle-settings-shoppingCart">
                  Shopping Cart
                </Label>
                <AvField id="subscription-bundle-settings-shoppingCart" type="text" name="shoppingCart" />
              </AvGroup>
              <AvGroup>
                <Label id="titleLabel" for="subscription-bundle-settings-title">
                  Title
                </Label>
                <AvField id="subscription-bundle-settings-title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label id="tieredDiscountLabel" for="subscription-bundle-settings-tieredDiscount">
                  Tiered Discount
                </Label>
                <AvField id="subscription-bundle-settings-tieredDiscount" type="text" name="tieredDiscount" />
              </AvGroup>
              <AvGroup>
                <Label id="subtotalLabel" for="subscription-bundle-settings-subtotal">
                  Subtotal
                </Label>
                <AvField id="subscription-bundle-settings-subtotal" type="text" name="subtotal" />
              </AvGroup>
              <AvGroup>
                <Label id="checkoutMessageLabel" for="subscription-bundle-settings-checkoutMessage">
                  Checkout Message
                </Label>
                <AvField id="subscription-bundle-settings-checkoutMessage" type="text" name="checkoutMessage" />
              </AvGroup>
              <AvGroup>
                <Label id="continueShoppingLabel" for="subscription-bundle-settings-continueShopping">
                  Continue Shopping
                </Label>
                <AvField id="subscription-bundle-settings-continueShopping" type="text" name="continueShopping" />
              </AvGroup>
              <AvGroup>
                <Label id="spendAmountGetDiscountLabel" for="subscription-bundle-settings-spendAmountGetDiscount">
                  Spend Amount Get Discount
                </Label>
                <AvField id="subscription-bundle-settings-spendAmountGetDiscount" type="text" name="spendAmountGetDiscount" />
              </AvGroup>
              <AvGroup>
                <Label id="buyQuantityGetDiscountLabel" for="subscription-bundle-settings-buyQuantityGetDiscount">
                  Buy Quantity Get Discount
                </Label>
                <AvField id="subscription-bundle-settings-buyQuantityGetDiscount" type="text" name="buyQuantityGetDiscount" />
              </AvGroup>
              <AvGroup>
                <Label id="removeItemLabel" for="subscription-bundle-settings-removeItem">
                  Remove Item
                </Label>
                <AvField id="subscription-bundle-settings-removeItem" type="text" name="removeItem" />
              </AvGroup>
              <AvGroup check>
                <Label id="showCompareAtPriceLabel">
                  <AvInput
                    id="subscription-bundle-settings-showCompareAtPrice"
                    type="checkbox"
                    className="form-check-input"
                    name="showCompareAtPrice"
                  />
                  Show Compare At Price
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="enableRedirectToProductPageLabel" for="subscription-bundle-settings-enableRedirectToProductPage">
                  Enable Redirect To Product Page
                </Label>
<AvField id="subscription-bundle-settings-enableRedirectToProductPage" type="text" name="enableRedirectToProductPage" />
              </AvGroup>
              <AvGroup>
                <Label id="saveDiscountTextLabel" for="subscription-bundle-settings-saveDiscountText">
                  Save Discount Text
                </Label>
                <AvField id="subscription-bundle-settings-saveDiscountText" type="text" name="saveDiscountText" />
              </AvGroup>
              <AvGroup check>
                <Label id="disableProductDescriptionLabel">
                  <AvInput
                    id="subscription-bundle-settings-disableProductDescription"
                    type="checkbox"
                    className="form-check-input"
                    name="disableProductDescription"
                  />
                  Disable Product Description
                </Label>
              </AvGroup>
                            <AvGroup check>
                <Label id="enableCustomAdvancedFieldsLabel">
                  <AvInput
                    id="subscription-bundle-settings-enableCustomAdvancedFields"
                    type="checkbox"
                    className="form-check-input"
                    name="enableCustomAdvancedFields"
                  />
                  Enable Custom Advanced Fields
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="hideProductSearchBoxLabel">
                  <AvInput
                    id="subscription-bundle-settings-hideProductSearchBox"
                    type="checkbox"
                    className="form-check-input"
                    name="hideProductSearchBox"
                  />
                  Hide Product Search Box
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="productFilterConfigLabel" for="subscription-bundle-settings-productFilterConfig">
                  Product Filter Config
                </Label>
                <AvInput id="subscription-bundle-settings-productFilterConfig" type="textarea" name="productFilterConfig" />
              </AvGroup>
              <AvGroup check>
                <Label id="enableProductDetailButtonLabel">
                  <AvInput
                    id="subscription-bundle-settings-enableProductDetailButton"
                    type="checkbox"
                    className="form-check-input"
                    name="enableProductDetailButton"
                  />
                  Enable Product Detail Button
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableShowProductBasePriceLabel">
                  <AvInput
                    id="subscription-bundle-settings-enableShowProductBasePrice"
                    type="checkbox"
                    className="form-check-input"
                    name="enableShowProductBasePrice"
                  />
                  Enable Show Product Base Price
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableClearCartSelectedProductsLabel">
                  <AvInput
                    id="subscription-bundle-settings-enableClearCartSelectedProducts"
                    type="checkbox"
                    className="form-check-input"
                    name="enableClearCartSelectedProducts"
                  />
                  Enable Clear Cart Selected Products
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableSkieyBABHeaderLabel">
                  <AvInput
                    id="subscription-bundle-settings-enableSkieyBABHeader"
                    type="checkbox"
                    className="form-check-input"
                    name="enableSkieyBABHeader"
                  />
                  Enable Skiey BAB Header
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableOpeningSidebarLabel">
                  <AvInput
                    id="subscription-bundle-settings-enableOpeningSidebar"
                    type="checkbox"
                    className="form-check-input"
                    name="enableOpeningSidebar"
                  />
                  Enable Opening Sidebar
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="rightSidebarHTMLLabel" for="subscription-bundle-settings-rightSidebarHTML">
                  Right Sidebar HTML
                </Label>
                <AvInput id="subscription-bundle-settings-rightSidebarHTML" type="textarea" name="rightSidebarHTML" />
              </AvGroup>
              <AvGroup>
                <Label id="leftSidebarHTMLLabel" for="subscription-bundle-settings-leftSidebarHTML">
                  Left Sidebar HTML
                </Label>
                <AvInput id="subscription-bundle-settings-leftSidebarHTML" type="textarea" name="leftSidebarHTML" />
              </AvGroup>
              <AvGroup check>
                <Label id="isMergeIntoSingleBABVariantDropdownLabel">
                  <AvInput
                    id="subscription-bundle-settings-isMergeIntoSingleBABVariantDropdown"
                    type="checkbox"
                    className="form-check-input"
                    name="isMergeIntoSingleBABVariantDropdown"
                  />
                  Is Merge Into Single BAB Variant Dropdown
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-bundle-settings" replace color="info">
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
  subscriptionBundleSettingsEntity: storeState.subscriptionBundleSettings.entity,
  loading: storeState.subscriptionBundleSettings.loading,
  updating: storeState.subscriptionBundleSettings.updating,
  updateSuccess: storeState.subscriptionBundleSettings.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBundleSettingsUpdate);
