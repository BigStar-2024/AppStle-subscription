import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './subscription-widget-settings.reducer';
import { ISubscriptionWidgetSettings } from 'app/shared/model/subscription-widget-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionWidgetSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionWidgetSettingsUpdate = (props: ISubscriptionWidgetSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionWidgetSettingsEntity, loading, updating } = props;

  const {
    tooltipDesctiption,
    tooltipDescriptionOnPrepaidPlan,
    tooltipDescriptionOnMultipleDiscount,
    tooltipDescriptionCustomization,
    subscriptionPriceDisplayText,
    loyaltyPerkDescriptionText
  } = subscriptionWidgetSettingsEntity;

  const handleClose = () => {
    props.history.push('/subscription-widget-settings');
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
    if (errors.length === 0) {
      const entity = {
        ...subscriptionWidgetSettingsEntity,
        ...values
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
          <h2 id="subscriptionApp.subscriptionWidgetSettings.home.createOrEditLabel">Create or edit a SubscriptionWidgetSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionWidgetSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-widget-settings-id">ID</Label>
                  <AvInput id="subscription-widget-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-widget-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-widget-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="oneTimePurchaseTextLabel" for="subscription-widget-settings-oneTimePurchaseText">
                  One Time Purchase Text
                </Label>
                <AvField id="subscription-widget-settings-oneTimePurchaseText" type="text" name="oneTimePurchaseText" />
              </AvGroup>
              <AvGroup>
                <Label id="deliveryTextLabel" for="subscription-widget-settings-deliveryText">
                  Delivery Text
                </Label>
                <AvField id="subscription-widget-settings-deliveryText" type="text" name="deliveryText" />
              </AvGroup>
              <AvGroup>
                <Label id="purchaseOptionsTextLabel" for="subscription-widget-settings-purchaseOptionsText">
                  Purchase Options Text
                </Label>
                <AvField id="subscription-widget-settings-purchaseOptionsText" type="text" name="purchaseOptionsText" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionOptionTextLabel" for="subscription-widget-settings-subscriptionOptionText">
                  Subscription Option Text
                </Label>
                <AvField id="subscription-widget-settings-subscriptionOptionText" type="text" name="subscriptionOptionText" />
              </AvGroup>
              <AvGroup>
                <Label id="sellingPlanSelectTitleLabel" for="subscription-widget-settings-sellingPlanSelectTitle">
                  Selling Plan Select Title
                </Label>
                <AvField id="subscription-widget-settings-sellingPlanSelectTitle" type="text" name="sellingPlanSelectTitle" />
              </AvGroup>
              <AvGroup>
                <Label id="tooltipTitleLabel" for="subscription-widget-settings-tooltipTitle">
                  Tooltip Title
                </Label>
                <AvField id="subscription-widget-settings-tooltipTitle" type="text" name="tooltipTitle" />
              </AvGroup>
              <AvGroup>
                <Label id="tooltipDesctiptionLabel" for="subscription-widget-settings-tooltipDesctiption">
                  Tooltip Desctiption
                </Label>
                <AvInput id="subscription-widget-settings-tooltipDesctiption" type="textarea" name="tooltipDesctiption" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionWidgetMarginTopLabel" for="subscription-widget-settings-subscriptionWidgetMarginTop">
                  Subscription Widget Margin Top
                </Label>
                <AvField id="subscription-widget-settings-subscriptionWidgetMarginTop" type="text" name="subscriptionWidgetMarginTop" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionWidgetMarginBottomLabel" for="subscription-widget-settings-subscriptionWidgetMarginBottom">
                  Subscription Widget Margin Bottom
                </Label>
                <AvField
                  id="subscription-widget-settings-subscriptionWidgetMarginBottom"
                  type="text"
                  name="subscriptionWidgetMarginBottom"
                />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionWrapperBorderWidthLabel" for="subscription-widget-settings-subscriptionWrapperBorderWidth">
                  Subscription Wrapper Border Width
                </Label>
                <AvField
                  id="subscription-widget-settings-subscriptionWrapperBorderWidth"
                  type="text"
                  name="subscriptionWrapperBorderWidth"
                />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionWrapperBorderColorLabel" for="subscription-widget-settings-subscriptionWrapperBorderColor">
                  Subscription Wrapper Border Color
                </Label>
                <AvField
                  id="subscription-widget-settings-subscriptionWrapperBorderColor"
                  type="text"
                  name="subscriptionWrapperBorderColor"
                />
              </AvGroup>
              <AvGroup>
                <Label id="circleBorderColorLabel" for="subscription-widget-settings-circleBorderColor">
                  Circle Border Color
                </Label>
                <AvField id="subscription-widget-settings-circleBorderColor" type="text" name="circleBorderColor" />
              </AvGroup>
              <AvGroup>
                <Label id="dotBackgroundColorLabel" for="subscription-widget-settings-dotBackgroundColor">
                  Dot Background Color
                </Label>
                <AvField id="subscription-widget-settings-dotBackgroundColor" type="text" name="dotBackgroundColor" />
              </AvGroup>
              <AvGroup>
                <Label id="selectPaddingTopLabel" for="subscription-widget-settings-selectPaddingTop">
                  Select Padding Top
                </Label>
                <AvField id="subscription-widget-settings-selectPaddingTop" type="text" name="selectPaddingTop" />
              </AvGroup>
              <AvGroup>
                <Label id="selectPaddingBottomLabel" for="subscription-widget-settings-selectPaddingBottom">
                  Select Padding Bottom
                </Label>
                <AvField id="subscription-widget-settings-selectPaddingBottom" type="text" name="selectPaddingBottom" />
              </AvGroup>
              <AvGroup>
                <Label id="selectPaddingLeftLabel" for="subscription-widget-settings-selectPaddingLeft">
                  Select Padding Left
                </Label>
                <AvField id="subscription-widget-settings-selectPaddingLeft" type="text" name="selectPaddingLeft" />
              </AvGroup>
              <AvGroup>
                <Label id="selectPaddingRightLabel" for="subscription-widget-settings-selectPaddingRight">
                  Select Padding Right
                </Label>
                <AvField id="subscription-widget-settings-selectPaddingRight" type="text" name="selectPaddingRight" />
              </AvGroup>
              <AvGroup>
                <Label id="selectBorderWidthLabel" for="subscription-widget-settings-selectBorderWidth">
                  Select Border Width
                </Label>
                <AvField id="subscription-widget-settings-selectBorderWidth" type="text" name="selectBorderWidth" />
              </AvGroup>
              <AvGroup>
                <Label id="selectBorderStyleLabel" for="subscription-widget-settings-selectBorderStyle">
                  Select Border Style
                </Label>
                <AvField id="subscription-widget-settings-selectBorderStyle" type="text" name="selectBorderStyle" />
              </AvGroup>
              <AvGroup>
                <Label id="selectBorderColorLabel" for="subscription-widget-settings-selectBorderColor">
                  Select Border Color
                </Label>
                <AvField id="subscription-widget-settings-selectBorderColor" type="text" name="selectBorderColor" />
              </AvGroup>
              <AvGroup>
                <Label id="selectBorderRadiusLabel" for="subscription-widget-settings-selectBorderRadius">
                  Select Border Radius
                </Label>
                <AvField id="subscription-widget-settings-selectBorderRadius" type="text" name="selectBorderRadius" />
              </AvGroup>
              <AvGroup>
                <Label id="tooltipSubscriptionSvgFillLabel" for="subscription-widget-settings-tooltipSubscriptionSvgFill">
                  Tooltip Subscription Svg Fill
                </Label>
                <AvField id="subscription-widget-settings-tooltipSubscriptionSvgFill" type="text" name="tooltipSubscriptionSvgFill" />
              </AvGroup>
              <AvGroup>
                <Label id="tooltipColorLabel" for="subscription-widget-settings-tooltipColor">
                  Tooltip Color
                </Label>
                <AvField id="subscription-widget-settings-tooltipColor" type="text" name="tooltipColor" />
              </AvGroup>
              <AvGroup>
                <Label id="tooltipBackgroundColorLabel" for="subscription-widget-settings-tooltipBackgroundColor">
                  Tooltip Background Color
                </Label>
                <AvField id="subscription-widget-settings-tooltipBackgroundColor" type="text" name="tooltipBackgroundColor" />
              </AvGroup>
              <AvGroup>
                <Label id="tooltipBorderTopColorBorderTopColorLabel" for="subscription-widget-settings-tooltipBorderTopColorBorderTopColor">
                  Tooltip Border Top Color Border Top Color
                </Label>
                <AvField
                  id="subscription-widget-settings-tooltipBorderTopColorBorderTopColor"
                  type="text"
                  name="tooltipBorderTopColorBorderTopColor"
                />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionFinalPriceColorLabel" for="subscription-widget-settings-subscriptionFinalPriceColor">
                  Subscription Final Price Color
                </Label>
                <AvField id="subscription-widget-settings-subscriptionFinalPriceColor" type="text" name="subscriptionFinalPriceColor" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionWidgetTextColorLabel" for="subscription-widget-settings-subscriptionWidgetTextColor">
                  Subscription Widget Text Color
                </Label>
                <AvField id="subscription-widget-settings-subscriptionWidgetTextColor" type="text" name="subscriptionWidgetTextColor" />
              </AvGroup>
              <AvGroup>
                <Label id="orderStatusManageSubscriptionTitleLabel" for="subscription-widget-settings-orderStatusManageSubscriptionTitle">
                  Order Status Manage Subscription Title
                </Label>
                <AvField
                  id="subscription-widget-settings-orderStatusManageSubscriptionTitle"
                  type="text"
                  name="orderStatusManageSubscriptionTitle"
                />
              </AvGroup>
              <AvGroup>
                <Label
                  id="orderStatusManageSubscriptionDescriptionLabel"
                  for="subscription-widget-settings-orderStatusManageSubscriptionDescription"
                >
                  Order Status Manage Subscription Description
                </Label>
                <AvField
                  id="subscription-widget-settings-orderStatusManageSubscriptionDescription"
                  type="text"
                  name="orderStatusManageSubscriptionDescription"
                />
              </AvGroup>
              <AvGroup>
                <Label
                  id="orderStatusManageSubscriptionButtonTextLabel"
                  for="subscription-widget-settings-orderStatusManageSubscriptionButtonText"
                >
                  Order Status Manage Subscription Button Text
                </Label>
                <AvField
                  id="subscription-widget-settings-orderStatusManageSubscriptionButtonText"
                  type="text"
                  name="orderStatusManageSubscriptionButtonText"
                />
              </AvGroup>
              <AvGroup check>
                <Label id="showTooltipOnClickLabel">
                  <AvInput
                    id="subscription-widget-settings-showTooltipOnClick"
                    type="checkbox"
                    className="form-check-input"
                    name="showTooltipOnClick"
                  />
                  Show Tooltip On Click
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="subscriptionOptionSelectedByDefaultLabel">
                  <AvInput
                    id="subscription-widget-settings-subscriptionOptionSelectedByDefault"
                    type="checkbox"
                    className="form-check-input"
                    name="subscriptionOptionSelectedByDefault"
                  />
                  Subscription Option Selected By Default
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="widgetEnabledLabel">
                  <AvInput
                    id="subscription-widget-settings-widgetEnabled"
                    type="checkbox"
                    className="form-check-input"
                    name="widgetEnabled"
                  />
                  Widget Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="showTooltipLabel">
                  <AvInput id="subscription-widget-settings-showTooltip" type="checkbox" className="form-check-input" name="showTooltip" />
                  Show Tooltip
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="sellingPlanTitleTextLabel" for="subscription-widget-settings-sellingPlanTitleText">
                  Selling Plan Title Text
                </Label>
                <AvField id="subscription-widget-settings-sellingPlanTitleText" type="text" name="sellingPlanTitleText" />
              </AvGroup>
              <AvGroup>
                <Label id="oneTimePriceTextLabel" for="subscription-widget-settings-oneTimePriceText">
                  One Time Price Text
                </Label>
                <AvField id="subscription-widget-settings-oneTimePriceText" type="text" name="oneTimePriceText" />
              </AvGroup>
              <AvGroup>
                <Label
                  id="selectedPayAsYouGoSellingPlanPriceTextLabel"
                  for="subscription-widget-settings-selectedPayAsYouGoSellingPlanPriceText"
                >
                  Selected Pay As You Go Selling Plan Price Text
                </Label>
                <AvField
                  id="subscription-widget-settings-selectedPayAsYouGoSellingPlanPriceText"
                  type="text"
                  name="selectedPayAsYouGoSellingPlanPriceText"
                />
              </AvGroup>
              <AvGroup>
                <Label id="selectedPrepaidSellingPlanPriceTextLabel" for="subscription-widget-settings-selectedPrepaidSellingPlanPriceText">
                  Selected Prepaid Selling Plan Price Text
                </Label>
                <AvField
                  id="subscription-widget-settings-selectedPrepaidSellingPlanPriceText"
                  type="text"
                  name="selectedPrepaidSellingPlanPriceText"
                />
              </AvGroup>
              <AvGroup>
                <Label id="selectedDiscountFormatLabel" for="subscription-widget-settings-selectedDiscountFormat">
                  Selected Discount Format
                </Label>
                <AvField id="subscription-widget-settings-selectedDiscountFormat" type="text" name="selectedDiscountFormat" />
              </AvGroup>
              <AvGroup>
                <Label id="manageSubscriptionBtnFormatLabel" for="subscription-widget-settings-manageSubscriptionBtnFormat">
                  Manage Subscription Btn Format
                </Label>
                <AvField id="subscription-widget-settings-manageSubscriptionBtnFormat" type="text" name="manageSubscriptionBtnFormat" />
              </AvGroup>
              <AvGroup>
                <Label id="tooltipDescriptionOnPrepaidPlanLabel" for="subscription-widget-settings-tooltipDescriptionOnPrepaidPlan">
                  Tooltip Description On Prepaid Plan
                </Label>
                <AvInput
                  id="subscription-widget-settings-tooltipDescriptionOnPrepaidPlan"
                  type="textarea"
                  name="tooltipDescriptionOnPrepaidPlan"
                />
              </AvGroup>
              <AvGroup>
                <Label
                  id="tooltipDescriptionOnMultipleDiscountLabel"
                  for="subscription-widget-settings-tooltipDescriptionOnMultipleDiscount"
                >
                  Tooltip Description On Multiple Discount
                </Label>
                <AvInput
                  id="subscription-widget-settings-tooltipDescriptionOnMultipleDiscount"
                  type="textarea"
                  name="tooltipDescriptionOnMultipleDiscount"
                />
              </AvGroup>
              <AvGroup>
                <Label id="tooltipDescriptionCustomizationLabel" for="subscription-widget-settings-tooltipDescriptionCustomization">
                  Tooltip Description Customization
                </Label>
                <AvInput
                  id="subscription-widget-settings-tooltipDescriptionCustomization"
                  type="textarea"
                  name="tooltipDescriptionCustomization"
                />
              </AvGroup>
              <AvGroup check>
                <Label id="showStaticTooltipLabel">
                  <AvInput
                    id="subscription-widget-settings-showStaticTooltip"
                    type="checkbox"
                    className="form-check-input"
                    name="showStaticTooltip"
                  />
                  Show Static Tooltip
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="showAppstleLinkLabel">
                  <AvInput
                    id="subscription-widget-settings-showAppstleLink"
                    type="checkbox"
                    className="form-check-input"
                    name="showAppstleLink"
                  />
                  Show Appstle Link
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionPriceDisplayTextLabel" for="subscription-widget-settings-subscriptionPriceDisplayText">
                  Subscription Price Display Text
                </Label>
                <AvInput
                  id="subscription-widget-settings-subscriptionPriceDisplayText"
                  type="textarea"
                  name="subscriptionPriceDisplayText"
                />
              </AvGroup>
              <AvGroup check>
                <Label id="sortByDefaultSequenceLabel">
                  <AvInput
                    id="subscription-widget-settings-sortByDefaultSequence"
                    type="checkbox"
                    className="form-check-input"
                    name="sortByDefaultSequence"
                  />
                  Sort By Default Sequence
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="showSubOptionBeforeOneTimeLabel">
                  <AvInput
                    id="subscription-widget-settings-showSubOptionBeforeOneTime"
                    type="checkbox"
                    className="form-check-input"
                    name="showSubOptionBeforeOneTime"
                  />
                  Show Sub Option Before One Time
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="showCheckoutSubscriptionBtnLabel">
                  <AvInput
                    id="subscription-widget-settings-showCheckoutSubscriptionBtn"
                    type="checkbox"
                    className="form-check-input"
                    name="showCheckoutSubscriptionBtn"
                  />
                  Show Checkout Subscription Btn
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="totalPricePerDeliveryTextLabel" for="subscription-widget-settings-totalPricePerDeliveryText">
                  Total Price Per Delivery Text
                </Label>
                <AvField id="subscription-widget-settings-totalPricePerDeliveryText" type="text" name="totalPricePerDeliveryText" />
              </AvGroup>
              <AvGroup check>
                <Label id="widgetEnabledOnSoldVariantLabel">
                  <AvInput
                    id="subscription-widget-settings-widgetEnabledOnSoldVariant"
                    type="checkbox"
                    className="form-check-input"
                    name="widgetEnabledOnSoldVariant"
                  />
                  Widget Enabled On Sold Variant
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableCartWidgetFeatureLabel">
                  <AvInput
                    id="subscription-widget-settings-enableCartWidgetFeature"
                    type="checkbox"
                    className="form-check-input"
                    name="enableCartWidgetFeature"
                  />
                  Enable Cart Widget Feature
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="switchRadioButtonWidgetLabel">
                  <AvInput
                    id="subscription-widget-settings-switchRadioButtonWidget"
                    type="checkbox"
                    className="form-check-input"
                    name="switchRadioButtonWidget"
                  />
                  Switch Radio Button Widget
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="formMappingAttributeNameLabel" for="subscription-widget-settings-formMappingAttributeName">
                  Form Mapping Attribute Name
                </Label>
                <AvField id="subscription-widget-settings-formMappingAttributeName" type="text" name="formMappingAttributeName" />
              </AvGroup>
              <AvGroup>
                <Label id="formMappingAttributeSelectorLabel" for="subscription-widget-settings-formMappingAttributeSelector">
                  Form Mapping Attribute Selector
                </Label>
                <AvField id="subscription-widget-settings-formMappingAttributeSelector" type="text" name="formMappingAttributeSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="quickViewModalPollingSelectorLabel" for="subscription-widget-settings-quickViewModalPollingSelector">
                  Quick View Modal Polling Selector
                </Label>
                <AvField id="subscription-widget-settings-quickViewModalPollingSelector" type="text" name="quickViewModalPollingSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="updatePriceOnQuantityChangeLabel" for="subscription-widget-settings-updatePriceOnQuantityChange">
                  Update Price On Quantity Change
                </Label>
                <AvField id="subscription-widget-settings-updatePriceOnQuantityChange" type="text" name="updatePriceOnQuantityChange" />
              </AvGroup>
              <AvGroup>
                <Label id="widgetParentSelectorLabel" for="subscription-widget-settings-widgetParentSelector">
                  Widget Parent Selector
                </Label>
                <AvField id="subscription-widget-settings-widgetParentSelector" type="text" name="widgetParentSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="quantitySelectorLabel" for="subscription-widget-settings-quantitySelector">
                  Quantity Selector
                </Label>
                <AvField id="subscription-widget-settings-quantitySelector" type="text" name="quantitySelector" />
              </AvGroup>
              <AvGroup>
                <Label id="loyaltyDetailsLabelTextLabel" for="subscription-widget-settings-loyaltyDetailsLabelText">
                  Loyalty Details Label Text
                </Label>
                <AvField id="subscription-widget-settings-loyaltyDetailsLabelText" type="text" name="loyaltyDetailsLabelText" />
              </AvGroup>
              <AvGroup>
                <Label id="loyaltyPerkDescriptionTextLabel" for="subscription-widget-settings-loyaltyPerkDescriptionText">
                  Loyalty Perk Description Text
                </Label>
                <AvInput id="subscription-widget-settings-loyaltyPerkDescriptionText" type="textarea" name="loyaltyPerkDescriptionText" />
              </AvGroup>
              <AvGroup check>
                <Label id="detectVariantFromURLParamsLabel">
                  <AvInput
                    id="subscription-widget-settings-detectVariantFromURLParams"
                    type="checkbox"
                    className="form-check-input"
                    name="detectVariantFromURLParams"
                  />
                  Detect Variant From URL Params
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="disableQueryParamsUpdateLabel">
                  <AvInput
                    id="subscription-widget-settings-disableQueryParamsUpdate"
                    type="checkbox"
                    className="form-check-input"
                    name="disableQueryParamsUpdate"
                  />
                  Disable Query Params Update
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-widget-settings" replace color="info">
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
  subscriptionWidgetSettingsEntity: storeState.subscriptionWidgetSettings.entity,
  loading: storeState.subscriptionWidgetSettings.loading,
  updating: storeState.subscriptionWidgetSettings.updating,
  updateSuccess: storeState.subscriptionWidgetSettings.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionWidgetSettingsUpdate);
