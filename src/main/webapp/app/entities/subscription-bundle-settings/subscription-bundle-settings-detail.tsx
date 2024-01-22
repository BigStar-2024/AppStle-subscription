import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-bundle-settings.reducer';
import { ISubscriptionBundleSettings } from 'app/shared/model/subscription-bundle-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionBundleSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionBundleSettingsDetail = (props: ISubscriptionBundleSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionBundleSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionBundleSettings [<b>{subscriptionBundleSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.shop}</dd>
          <dt>
            <span id="selectedFrequencyLabelText">Selected Frequency Label Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.selectedFrequencyLabelText}</dd>
          <dt>
            <span id="addButtonText">Add Button Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.addButtonText}</dd>
          <dt>
            <span id="selectMinimumProductButtonText">Select Minimum Product Button Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.selectMinimumProductButtonText}</dd>
          <dt>
            <span id="productsToProceedText">Products To Proceed Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.productsToProceedText}</dd>
          <dt>
            <span id="proceedToCheckoutButtonText">Proceed To Checkout Button Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.proceedToCheckoutButtonText}</dd>
          <dt>
            <span id="myDeliveryText">My Delivery Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.myDeliveryText}</dd>
          <dt>
            <span id="bundleTopHtml">Build-A-Box Top Html</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.bundleTopHtml}</dd>
          <dt>
            <span id="bundleBottomHtml">Build-A-Box Bottom HTML</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.bundleBottomHtml}</dd>
          <dt>
            <span id="failedToAddTitleText">Failed To Add Title Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.failedToAddTitleText}</dd>
          <dt>
            <span id="okBtnText">Ok Btn Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.okBtnText}</dd>
          <dt>
            <span id="failedToAddMsgText">Failed To Add Msg Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.failedToAddMsgText}</dd>
          <dt>
            <span id="buttonColor">Button Color</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.buttonColor}</dd>
          <dt>
            <span id="backgroundColor">Background Color</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.backgroundColor}</dd>
          <dt>
            <span id="pageBackgroundColor">Page Background Color</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.pageBackgroundColor}</dd>
          <dt>
            <span id="buttonBackgroundColor">Button Background Color</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.buttonBackgroundColor}</dd>
          <dt>
            <span id="ProductTitleFontColor">Product Title Font Color</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.ProductTitleFontColor}</dd>
          <dt>
            <span id="variantNotAvailable">Variant Not Available</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.variantNotAvailable}</dd>
          <dt>
            <span id="bundleRedirect">Bundle Redirect</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.bundleRedirect}</dd>
          <dt>
            <span id="isBundleWithoutScroll">Is Bundle Without Scroll</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.isBundleWithoutScroll ? 'true' : 'false'}</dd>
          <dt>
            <span id="descriptionLength">Description Length</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.descriptionLength}</dd>
          <dt>
            <span id="currencySwitcherClassName">Currency Switcher Class Name</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.currencySwitcherClassName}</dd>
          <dt>
            <span id="productPriceFormatField">Product Price Format Field</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.productPriceFormatField}</dd>
          <dt>
            <span id="customRedirectURL">Custom Redirect URL</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.customRedirectURL}</dd>
          <dt>
            <span id="viewProduct">View Product</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.viewProduct}</dd>
          <dt>
            <span id="productDetails">Product Details</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.productDetails}</dd>
          <dt>
            <span id="editQuantity">Edit Quantity</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.editQuantity}</dd>
          <dt>
            <span id="cart">Cart</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.cart}</dd>
          <dt>
            <span id="shoppingCart">Shopping Cart</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.shoppingCart}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.title}</dd>
          <dt>
            <span id="tieredDiscount">Tiered Discount</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.tieredDiscount}</dd>
          <dt>
            <span id="subtotal">Subtotal</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.subtotal}</dd>
          <dt>
            <span id="checkoutMessage">Checkout Message</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.checkoutMessage}</dd>
          <dt>
            <span id="continueShopping">Continue Shopping</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.continueShopping}</dd>
          <dt>
            <span id="spendAmountGetDiscount">Spend Amount Get Discount</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.spendAmountGetDiscount}</dd>
          <dt>
            <span id="buyQuantityGetDiscount">Buy Quantity Get Discount</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.buyQuantityGetDiscount}</dd>
          <dt>
            <span id="removeItem">Remove Item</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.removeItem}</dd>
          <dt>
            <span id="showCompareAtPrice">Show Compare At Price</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.showCompareAtPrice ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableRedirectToProductPage">Enable Redirect To Product Page</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.enableRedirectToProductPage}</dd>
          <dt>
            <span id="saveDiscountText">Save Discount Text</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.saveDiscountText}</dd>
          <dt>
            <span id="disableProductDescription">Disable Product Description</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.disableProductDescription ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableCustomAdvancedFields">Enable Custom Advanced Fields</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.enableCustomAdvancedFields ? 'true' : 'false'}</dd>
          <dt>
            <span id="hideProductSearchBox">Hide Product Search Box</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.hideProductSearchBox ? 'true' : 'false'}</dd>
          <dt>
            <span id="productFilterConfig">Product Filter Config</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.productFilterConfig}</dd>
          <dt>
            <span id="enableProductDetailButton">Enable Product Detail Button</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.enableProductDetailButton ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableShowProductBasePrice">Enable Show Product Base Price</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.enableShowProductBasePrice ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableClearCartSelectedProducts">Enable Clear Cart Selected Products</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.enableClearCartSelectedProducts ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableSkieyBABHeader">Enable Skiey BAB Header</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.enableSkieyBABHeader ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableOpeningSidebar">Enable Opening Sidebar</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.enableOpeningSidebar ? 'true' : 'false'}</dd>
          <dt>
            <span id="rightSidebarHTML">Right Sidebar HTML</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.rightSidebarHTML}</dd>
          <dt>
            <span id="leftSidebarHTML">Left Sidebar HTML</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.leftSidebarHTML}</dd>
          <dt>
            <span id="isMergeIntoSingleBABVariantDropdown">Is Merge Into Single BAB Variant Dropdown</span>
          </dt>
          <dd>{subscriptionBundleSettingsEntity.isMergeIntoSingleBABVariantDropdown ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/subscription-bundle-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-bundle-settings/${subscriptionBundleSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionBundleSettings }: IRootState) => ({
  subscriptionBundleSettingsEntity: subscriptionBundleSettings.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBundleSettingsDetail);
