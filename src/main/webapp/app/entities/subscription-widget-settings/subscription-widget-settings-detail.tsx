import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-widget-settings.reducer';
import { ISubscriptionWidgetSettings } from 'app/shared/model/subscription-widget-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionWidgetSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionWidgetSettingsDetail = (props: ISubscriptionWidgetSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionWidgetSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionWidgetSettings [<b>{subscriptionWidgetSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.shop}</dd>
          <dt>
            <span id="oneTimePurchaseText">One Time Purchase Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.oneTimePurchaseText}</dd>
          <dt>
            <span id="deliveryText">Delivery Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.deliveryText}</dd>
          <dt>
            <span id="purchaseOptionsText">Purchase Options Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.purchaseOptionsText}</dd>
          <dt>
            <span id="subscriptionOptionText">Subscription Option Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionOptionText}</dd>
          <dt>
            <span id="sellingPlanSelectTitle">Selling Plan Select Title</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.sellingPlanSelectTitle}</dd>
          <dt>
            <span id="tooltipTitle">Tooltip Title</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipTitle}</dd>
          <dt>
            <span id="tooltipDesctiption">Tooltip Desctiption</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipDesctiption}</dd>
          <dt>
            <span id="subscriptionWidgetMarginTop">Subscription Widget Margin Top</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionWidgetMarginTop}</dd>
          <dt>
            <span id="subscriptionWidgetMarginBottom">Subscription Widget Margin Bottom</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionWidgetMarginBottom}</dd>
          <dt>
            <span id="subscriptionWrapperBorderWidth">Subscription Wrapper Border Width</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionWrapperBorderWidth}</dd>
          <dt>
            <span id="subscriptionWrapperBorderColor">Subscription Wrapper Border Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionWrapperBorderColor}</dd>
          <dt>
            <span id="circleBorderColor">Circle Border Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.circleBorderColor}</dd>
          <dt>
            <span id="dotBackgroundColor">Dot Background Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.dotBackgroundColor}</dd>
          <dt>
            <span id="selectPaddingTop">Select Padding Top</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectPaddingTop}</dd>
          <dt>
            <span id="selectPaddingBottom">Select Padding Bottom</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectPaddingBottom}</dd>
          <dt>
            <span id="selectPaddingLeft">Select Padding Left</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectPaddingLeft}</dd>
          <dt>
            <span id="selectPaddingRight">Select Padding Right</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectPaddingRight}</dd>
          <dt>
            <span id="selectBorderWidth">Select Border Width</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectBorderWidth}</dd>
          <dt>
            <span id="selectBorderStyle">Select Border Style</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectBorderStyle}</dd>
          <dt>
            <span id="selectBorderColor">Select Border Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectBorderColor}</dd>
          <dt>
            <span id="selectBorderRadius">Select Border Radius</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectBorderRadius}</dd>
          <dt>
            <span id="tooltipSubscriptionSvgFill">Tooltip Subscription Svg Fill</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipSubscriptionSvgFill}</dd>
          <dt>
            <span id="tooltipColor">Tooltip Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipColor}</dd>
          <dt>
            <span id="tooltipBackgroundColor">Tooltip Background Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipBackgroundColor}</dd>
          <dt>
            <span id="tooltipBorderTopColorBorderTopColor">Tooltip Border Top Color Border Top Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipBorderTopColorBorderTopColor}</dd>
          <dt>
            <span id="subscriptionFinalPriceColor">Subscription Final Price Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionFinalPriceColor}</dd>
          <dt>
            <span id="subscriptionWidgetTextColor">Subscription Widget Text Color</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionWidgetTextColor}</dd>
          <dt>
            <span id="orderStatusManageSubscriptionTitle">Order Status Manage Subscription Title</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.orderStatusManageSubscriptionTitle}</dd>
          <dt>
            <span id="orderStatusManageSubscriptionDescription">Order Status Manage Subscription Description</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.orderStatusManageSubscriptionDescription}</dd>
          <dt>
            <span id="orderStatusManageSubscriptionButtonText">Order Status Manage Subscription Button Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.orderStatusManageSubscriptionButtonText}</dd>
          <dt>
            <span id="showTooltipOnClick">Show Tooltip On Click</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.showTooltipOnClick ? 'true' : 'false'}</dd>
          <dt>
            <span id="subscriptionOptionSelectedByDefault">Subscription Option Selected By Default</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionOptionSelectedByDefault ? 'true' : 'false'}</dd>
          <dt>
            <span id="widgetEnabled">Widget Enabled</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.widgetEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="showTooltip">Show Tooltip</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.showTooltip ? 'true' : 'false'}</dd>
          <dt>
            <span id="sellingPlanTitleText">Selling Plan Title Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.sellingPlanTitleText}</dd>
          <dt>
            <span id="oneTimePriceText">One Time Price Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.oneTimePriceText}</dd>
          <dt>
            <span id="selectedPayAsYouGoSellingPlanPriceText">Selected Pay As You Go Selling Plan Price Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectedPayAsYouGoSellingPlanPriceText}</dd>
          <dt>
            <span id="selectedPrepaidSellingPlanPriceText">Selected Prepaid Selling Plan Price Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectedPrepaidSellingPlanPriceText}</dd>
          <dt>
            <span id="selectedDiscountFormat">Selected Discount Format</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.selectedDiscountFormat}</dd>
          <dt>
            <span id="manageSubscriptionBtnFormat">Manage Subscription Btn Format</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.manageSubscriptionBtnFormat}</dd>
          <dt>
            <span id="tooltipDescriptionOnPrepaidPlan">Tooltip Description On Prepaid Plan</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipDescriptionOnPrepaidPlan}</dd>
          <dt>
            <span id="tooltipDescriptionOnMultipleDiscount">Tooltip Description On Multiple Discount</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipDescriptionOnMultipleDiscount}</dd>
          <dt>
            <span id="tooltipDescriptionCustomization">Tooltip Description Customization</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.tooltipDescriptionCustomization}</dd>
          <dt>
            <span id="showStaticTooltip">Show Static Tooltip</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.showStaticTooltip ? 'true' : 'false'}</dd>
          <dt>
            <span id="showAppstleLink">Show Appstle Link</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.showAppstleLink ? 'true' : 'false'}</dd>
          <dt>
            <span id="subscriptionPriceDisplayText">Subscription Price Display Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.subscriptionPriceDisplayText}</dd>
          <dt>
            <span id="sortByDefaultSequence">Sort By Default Sequence</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.sortByDefaultSequence ? 'true' : 'false'}</dd>
          <dt>
            <span id="showSubOptionBeforeOneTime">Show Sub Option Before One Time</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.showSubOptionBeforeOneTime ? 'true' : 'false'}</dd>
          <dt>
            <span id="showCheckoutSubscriptionBtn">Show Checkout Subscription Btn</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.showCheckoutSubscriptionBtn ? 'true' : 'false'}</dd>
          <dt>
            <span id="totalPricePerDeliveryText">Total Price Per Delivery Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.totalPricePerDeliveryText}</dd>
          <dt>
            <span id="widgetEnabledOnSoldVariant">Widget Enabled On Sold Variant</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.widgetEnabledOnSoldVariant ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableCartWidgetFeature">Enable Cart Widget Feature</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.enableCartWidgetFeature ? 'true' : 'false'}</dd>
          <dt>
            <span id="switchRadioButtonWidget">Switch Radio Button Widget</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.switchRadioButtonWidget ? 'true' : 'false'}</dd>
          <dt>
            <span id="formMappingAttributeName">Form Mapping Attribute Name</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.formMappingAttributeName}</dd>
          <dt>
            <span id="formMappingAttributeSelector">Form Mapping Attribute Selector</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.formMappingAttributeSelector}</dd>
          <dt>
            <span id="quickViewModalPollingSelector">Quick View Modal Polling Selector</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.quickViewModalPollingSelector}</dd>
          <dt>
            <span id="updatePriceOnQuantityChange">Update Price On Quantity Change</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.updatePriceOnQuantityChange}</dd>
          <dt>
            <span id="widgetParentSelector">Widget Parent Selector</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.widgetParentSelector}</dd>
          <dt>
            <span id="quantitySelector">Quantity Selector</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.quantitySelector}</dd>
          <dt>
            <span id="loyaltyDetailsLabelText">Loyalty Details Label Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.loyaltyDetailsLabelText}</dd>
          <dt>
            <span id="loyaltyPerkDescriptionText">Loyalty Perk Description Text</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.loyaltyPerkDescriptionText}</dd>
          <dt>
            <span id="detectVariantFromURLParams">Detect Variant From URL Params</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.detectVariantFromURLParams ? 'true' : 'false'}</dd>
          <dt>
            <span id="disableQueryParamsUpdate">Disable Query Params Update</span>
          </dt>
          <dd>{subscriptionWidgetSettingsEntity.disableQueryParamsUpdate ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/subscription-widget-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-widget-settings/${subscriptionWidgetSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionWidgetSettings }: IRootState) => ({
  subscriptionWidgetSettingsEntity: subscriptionWidgetSettings.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionWidgetSettingsDetail);
