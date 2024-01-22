import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './shop-info.reducer';
import { IShopInfo } from 'app/shared/model/shop-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShopInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopInfoUpdate = (props: IShopInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { shopInfoEntity, loading, updating } = props;

  const { shopifyWebhookUrl } = shopInfoEntity;

  const handleClose = () => {
    props.history.push('/admin/shop-info');
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
        ...shopInfoEntity,
        ...values,
        localOrderDayOfWeek: values.localOrderDayOfWeek || null
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
          <h2 id="subscriptionApp.shopInfo.home.createOrEditLabel">Create or edit a ShopInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : shopInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="shop-info-id">ID</Label>
                  <AvInput id="shop-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="shop-info-shop">
                  Shop
                </Label>
                <AvField
                  id="shop-info-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="apiKeyLabel" for="shop-info-apiKey">
                  Api Key
                </Label>
                <AvField
                  id="shop-info-apiKey"
                  type="text"
                  name="apiKey"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="shopTimeZoneLabel" for="shop-info-shopTimeZone">
                  Shop Time Zone
                </Label>
                <AvField id="shop-info-shopTimeZone" type="text" name="shopTimeZone" />
              </AvGroup>
              <AvGroup>
                <Label id="shopifyPlanDisplayNameLabel" for="shop-info-shopifyPlanDisplayName">
                  Shopify Plan Display Name
                </Label>
                <AvField id="shop-info-shopifyPlanDisplayName" type="text" name="shopifyPlanDisplayName" />
              </AvGroup>
              <AvGroup>
                <Label id="shopifyPlanNameLabel" for="shop-info-shopifyPlanName">
                  Shopify Plan Name
                </Label>
                <AvField id="shop-info-shopifyPlanName" type="text" name="shopifyPlanName" />
              </AvGroup>
              <AvGroup>
                <Label id="searchResultPageIdLabel" for="shop-info-searchResultPageId">
                  Search Result Page Id
                </Label>
                <AvField id="shop-info-searchResultPageId" type="string" className="form-control" name="searchResultPageId" />
              </AvGroup>
              <AvGroup>
                <Label id="publicDomainLabel" for="shop-info-publicDomain">
                  Public Domain
                </Label>
                <AvField id="shop-info-publicDomain" type="text" name="publicDomain" />
              </AvGroup>
              <AvGroup>
                <Label id="moneyFormatLabel" for="shop-info-moneyFormat">
                  Money Format
                </Label>
                <AvField id="shop-info-moneyFormat" type="text" name="moneyFormat" />
              </AvGroup>
              <AvGroup>
                <Label id="currencyLabel" for="shop-info-currency">
                  Currency
                </Label>
                <AvField id="shop-info-currency" type="text" name="currency" />
              </AvGroup>
              <AvGroup check>
                <Label id="onboardingSeenLabel">
                  <AvInput id="shop-info-onboardingSeen" type="checkbox" className="form-check-input" name="onboardingSeen" />
                  Onboarding Seen
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="devEnabledLabel">
                  <AvInput id="shop-info-devEnabled" type="checkbox" className="form-check-input" name="devEnabled" />
                  Dev Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="firstTimeOrderTagLabel" for="shop-info-firstTimeOrderTag">
                  First Time Order Tag
                </Label>
                <AvField id="shop-info-firstTimeOrderTag" type="text" name="firstTimeOrderTag" />
              </AvGroup>
              <AvGroup>
                <Label id="recurringOrderTagLabel" for="shop-info-recurringOrderTag">
                  Recurring Order Tag
                </Label>
                <AvField id="shop-info-recurringOrderTag" type="text" name="recurringOrderTag" />
              </AvGroup>
              <AvGroup>
                <Label id="customerActiveSubscriptionTagLabel" for="shop-info-customerActiveSubscriptionTag">
                  Customer Active Subscription Tag
                </Label>
                <AvField id="shop-info-customerActiveSubscriptionTag" type="text" name="customerActiveSubscriptionTag" />
              </AvGroup>
              <AvGroup>
                <Label id="customerInActiveSubscriptionTagLabel" for="shop-info-customerInActiveSubscriptionTag">
                  Customer In Active Subscription Tag
                </Label>
                <AvField id="shop-info-customerInActiveSubscriptionTag" type="text" name="customerInActiveSubscriptionTag" />
              </AvGroup>
              <AvGroup>
                <Label id="customerPausedSubscriptionTagLabel" for="shop-info-customerPausedSubscriptionTag">
                  Customer Paused Subscription Tag
                </Label>
                <AvField id="shop-info-customerPausedSubscriptionTag" type="text" name="customerPausedSubscriptionTag" />
              </AvGroup>
              <AvGroup>
                <Label id="emailCustomDomainLabel" for="shop-info-emailCustomDomain">
                  Email Custom Domain
                </Label>
                <AvField id="shop-info-emailCustomDomain" type="text" name="emailCustomDomain" />
              </AvGroup>
              <AvGroup>
                <Label id="loyaltyLionTokenLabel" for="shop-info-loyaltyLionToken">
                  Loyalty Lion Token
                </Label>
                <AvField id="shop-info-loyaltyLionToken" type="text" name="loyaltyLionToken" />
              </AvGroup>
              <AvGroup>
                <Label id="loyaltyLionSecretLabel" for="shop-info-loyaltyLionSecret">
                  Loyalty Lion Secret
                </Label>
                <AvField id="shop-info-loyaltyLionSecret" type="text" name="loyaltyLionSecret" />
              </AvGroup>
              <AvGroup>
                <Label id="loyaltyLionPointsLabel" for="shop-info-loyaltyLionPoints">
                  Loyalty Lion Points
                </Label>
                <AvField id="shop-info-loyaltyLionPoints" type="string" className="form-control" name="loyaltyLionPoints" />
              </AvGroup>
              <AvGroup>
                <Label id="loyaltyLionMultiplierLabel" for="shop-info-loyaltyLionMultiplier">
                  Loyalty Lion Multiplier
                </Label>
                <AvField id="shop-info-loyaltyLionMultiplier" type="string" className="form-control" name="loyaltyLionMultiplier" />
              </AvGroup>
              <AvGroup check>
                <Label id="loyaltyLionEnabledLabel">
                  <AvInput id="shop-info-loyaltyLionEnabled" type="checkbox" className="form-check-input" name="loyaltyLionEnabled" />
                  Loyalty Lion Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="growaveClientIdLabel" for="shop-info-growaveClientId">
                  Growave Client Id
                </Label>
                <AvField id="shop-info-growaveClientId" type="text" name="growaveClientId" />
              </AvGroup>
              <AvGroup>
                <Label id="growaveSecretLabel" for="shop-info-growaveSecret">
                  Growave Secret
                </Label>
                <AvField id="shop-info-growaveSecret" type="text" name="growaveSecret" />
              </AvGroup>
              <AvGroup>
                <Label id="growavePointsLabel" for="shop-info-growavePoints">
                  Growave Points
                </Label>
                <AvField id="shop-info-growavePoints" type="string" className="form-control" name="growavePoints" />
              </AvGroup>
              <AvGroup>
                <Label id="growaveMultiplierLabel" for="shop-info-growaveMultiplier">
                  Growave Multiplier
                </Label>
                <AvField id="shop-info-growaveMultiplier" type="string" className="form-control" name="growaveMultiplier" />
              </AvGroup>
              <AvGroup check>
                <Label id="growaveEnabledLabel">
                  <AvInput id="shop-info-growaveEnabled" type="checkbox" className="form-check-input" name="growaveEnabled" />
                  Growave Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="yotpoApiKeyLabel" for="shop-info-yotpoApiKey">
                  Yotpo Api Key
                </Label>
                <AvField id="shop-info-yotpoApiKey" type="text" name="yotpoApiKey" />
              </AvGroup>
              <AvGroup>
                <Label id="yotpoGuidLabel" for="shop-info-yotpoGuid">
                  Yotpo Guid
                </Label>
                <AvField id="shop-info-yotpoGuid" type="text" name="yotpoGuid" />
              </AvGroup>
              <AvGroup>
                <Label id="yotpoPointsLabel" for="shop-info-yotpoPoints">
                  Yotpo Points
                </Label>
                <AvField id="shop-info-yotpoPoints" type="string" className="form-control" name="yotpoPoints" />
              </AvGroup>
              <AvGroup>
                <Label id="yotpoMultiplierLabel" for="shop-info-yotpoMultiplier">
                  Yotpo Multiplier
                </Label>
                <AvField id="shop-info-yotpoMultiplier" type="string" className="form-control" name="yotpoMultiplier" />
              </AvGroup>
              <AvGroup check>
                <Label id="yotpoEnabledLabel">
                  <AvInput id="shop-info-yotpoEnabled" type="checkbox" className="form-check-input" name="yotpoEnabled" />
                  Yotpo Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="klaviyoPublicApiKeyLabel" for="shop-info-klaviyoPublicApiKey">
                  Klaviyo Public Api Key
                </Label>
                <AvField id="shop-info-klaviyoPublicApiKey" type="text" name="klaviyoPublicApiKey" />
              </AvGroup>
              <AvGroup>
                <Label id="klaviyoApiKeyLabel" for="shop-info-klaviyoApiKey">
                  Klaviyo Api Key
                </Label>
                <AvField id="shop-info-klaviyoApiKey" type="text" name="klaviyoApiKey" />
              </AvGroup>
              <AvGroup>
                <Label id="omniSendApiKeyLabel" for="shop-info-omniSendApiKey">
                  Omni Send Api Key
                </Label>
                <AvField id="shop-info-omniSendApiKey" type="text" name="omniSendApiKey" />
              </AvGroup>
              <AvGroup>
                <Label id="manageSubscriptionsUrlLabel" for="shop-info-manageSubscriptionsUrl">
                  Manage Subscriptions Url
                </Label>
                <AvField id="shop-info-manageSubscriptionsUrl" type="text" name="manageSubscriptionsUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionCreatedUrlLabel" for="shop-info-zapierSubscriptionCreatedUrl">
                  Zapier Subscription Created Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionCreatedUrl" type="text" name="zapierSubscriptionCreatedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionUpdatedUrlLabel" for="shop-info-zapierSubscriptionUpdatedUrl">
                  Zapier Subscription Updated Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionUpdatedUrl" type="text" name="zapierSubscriptionUpdatedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierRecurringOrderPlacedUrlLabel" for="shop-info-zapierRecurringOrderPlacedUrl">
                  Zapier Recurring Order Placed Url
                </Label>
                <AvField id="shop-info-zapierRecurringOrderPlacedUrl" type="text" name="zapierRecurringOrderPlacedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierCustomerSubscriptionsUpdatedUrlLabel" for="shop-info-zapierCustomerSubscriptionsUpdatedUrl">
                  Zapier Customer Subscriptions Updated Url
                </Label>
                <AvField id="shop-info-zapierCustomerSubscriptionsUpdatedUrl" type="text" name="zapierCustomerSubscriptionsUpdatedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierNextOrderUpdatedUrlLabel" for="shop-info-zapierNextOrderUpdatedUrl">
                  Zapier Next Order Updated Url
                </Label>
                <AvField id="shop-info-zapierNextOrderUpdatedUrl" type="text" name="zapierNextOrderUpdatedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierOrderFrequencyUpdatedUrlLabel" for="shop-info-zapierOrderFrequencyUpdatedUrl">
                  Zapier Order Frequency Updated Url
                </Label>
                <AvField id="shop-info-zapierOrderFrequencyUpdatedUrl" type="text" name="zapierOrderFrequencyUpdatedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierShippingAddressUpdatedUrlLabel" for="shop-info-zapierShippingAddressUpdatedUrl">
                  Zapier Shipping Address Updated Url
                </Label>
                <AvField id="shop-info-zapierShippingAddressUpdatedUrl" type="text" name="zapierShippingAddressUpdatedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionCanceledUrlLabel" for="shop-info-zapierSubscriptionCanceledUrl">
                  Zapier Subscription Canceled Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionCanceledUrl" type="text" name="zapierSubscriptionCanceledUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionPausedUrlLabel" for="shop-info-zapierSubscriptionPausedUrl">
                  Zapier Subscription Paused Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionPausedUrl" type="text" name="zapierSubscriptionPausedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionProductAddedUrlLabel" for="shop-info-zapierSubscriptionProductAddedUrl">
                  Zapier Subscription Product Added Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionProductAddedUrl" type="text" name="zapierSubscriptionProductAddedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionProductRemovedUrlLabel" for="shop-info-zapierSubscriptionProductRemovedUrl">
                  Zapier Subscription Product Removed Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionProductRemovedUrl" type="text" name="zapierSubscriptionProductRemovedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionActivatedUrlLabel" for="shop-info-zapierSubscriptionActivatedUrl">
                  Zapier Subscription Activated Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionActivatedUrl" type="text" name="zapierSubscriptionActivatedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionTransactionFailedUrlLabel" for="shop-info-zapierSubscriptionTransactionFailedUrl">
                  Zapier Subscription Transaction Failed Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionTransactionFailedUrl" type="text" name="zapierSubscriptionTransactionFailedUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionUpcomingOrderUrlLabel" for="shop-info-zapierSubscriptionUpcomingOrderUrl">
                  Zapier Subscription Upcoming Order Url
                </Label>
                <AvField id="shop-info-zapierSubscriptionUpcomingOrderUrl" type="text" name="zapierSubscriptionUpcomingOrderUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="zapierSubscriptionExpiringCreditCardUrlLabel" for="shop-info-zapierSubscriptionExpiringCreditCardUrl">
                  Zapier Subscription Expiring Credit Card Url
                </Label>
                <AvField
                  id="shop-info-zapierSubscriptionExpiringCreditCardUrl"
                  type="text"
                  name="zapierSubscriptionExpiringCreditCardUrl"
                />
              </AvGroup>
              <AvGroup check>
                <Label id="advancedCustomerPortalLabel">
                  <AvInput
                    id="shop-info-advancedCustomerPortal"
                    type="checkbox"
                    className="form-check-input"
                    name="advancedCustomerPortal"
                  />
                  Advanced Customer Portal
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="transferOrderNotesToSubscriptionLabel">
                  <AvInput
                    id="shop-info-transferOrderNotesToSubscription"
                    type="checkbox"
                    className="form-check-input"
                    name="transferOrderNotesToSubscription"
                  />
                  Transfer Order Notes To Subscription
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="transferOrderNoteAttributesToSubscriptionLabel">
                  <AvInput
                    id="shop-info-transferOrderNoteAttributesToSubscription"
                    type="checkbox"
                    className="form-check-input"
                    name="transferOrderNoteAttributesToSubscription"
                  />
                  Transfer Order Note Attributes To Subscription
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="transferOrderLineItemAttributesToSubscriptionLabel">
                  <AvInput
                    id="shop-info-transferOrderLineItemAttributesToSubscription"
                    type="checkbox"
                    className="form-check-input"
                    name="transferOrderLineItemAttributesToSubscription"
                  />
                  Transfer Order Line Item Attributes To Subscription
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="customerPortalModeLabel" for="shop-info-customerPortalMode">
                  Customer Portal Mode
                </Label>
                <AvInput
                  id="shop-info-customerPortalMode"
                  type="select"
                  className="form-control"
                  name="customerPortalMode"
                  value={(!isNew && shopInfoEntity.customerPortalMode) || 'IFRAME'}
                >
                  <option value="IFRAME">IFRAME</option>
                  <option value="NO_IFRAME">NO_IFRAME</option>
                  <option value="V3">V3</option>
                  <option value="V3_IFRAME">V3_IFRAME</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="recurringOrderTimeLabel" for="shop-info-recurringOrderTime">
                  Recurring Order Time
                </Label>
                <AvField id="shop-info-recurringOrderTime" type="text" name="recurringOrderTime" />
              </AvGroup>
              <AvGroup>
                <Label id="recurringOrderHourLabel" for="shop-info-recurringOrderHour">
                  Recurring Order Hour
                </Label>
                <AvField id="shop-info-recurringOrderHour" type="string" className="form-control" name="recurringOrderHour" />
              </AvGroup>
              <AvGroup>
                <Label id="recurringOrderMinuteLabel" for="shop-info-recurringOrderMinute">
                  Recurring Order Minute
                </Label>
                <AvField id="shop-info-recurringOrderMinute" type="string" className="form-control" name="recurringOrderMinute" />
              </AvGroup>
              <AvGroup>
                <Label id="zoneOffsetHoursLabel" for="shop-info-zoneOffsetHours">
                  Zone Offset Hours
                </Label>
                <AvField id="shop-info-zoneOffsetHours" type="string" className="form-control" name="zoneOffsetHours" />
              </AvGroup>
              <AvGroup>
                <Label id="zoneOffsetMinutesLabel" for="shop-info-zoneOffsetMinutes">
                  Zone Offset Minutes
                </Label>
                <AvField id="shop-info-zoneOffsetMinutes" type="string" className="form-control" name="zoneOffsetMinutes" />
              </AvGroup>
              <AvGroup>
                <Label id="localOrderHourLabel" for="shop-info-localOrderHour">
                  Local Order Hour
                </Label>
                <AvField id="shop-info-localOrderHour" type="string" className="form-control" name="localOrderHour" />
              </AvGroup>
              <AvGroup>
                <Label id="localOrderMinuteLabel" for="shop-info-localOrderMinute">
                  Local Order Minute
                </Label>
                <AvField id="shop-info-localOrderMinute" type="string" className="form-control" name="localOrderMinute" />
              </AvGroup>
              <AvGroup check>
                <Label id="stopImportProcessLabel">
                  <AvInput id="shop-info-stopImportProcess" type="checkbox" className="form-check-input" name="stopImportProcess" />
                  Stop Import Process
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="disableShippingPricingAutoCalculationLabel">
                  <AvInput
                    id="shop-info-disableShippingPricingAutoCalculation"
                    type="checkbox"
                    className="form-check-input"
                    name="disableShippingPricingAutoCalculation"
                  />
                  Disable Shipping Pricing Auto Calculation
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="allowV1FormCustomerPortalLabel">
                  <AvInput
                    id="shop-info-allowV1FormCustomerPortal"
                    type="checkbox"
                    className="form-check-input"
                    name="allowV1FormCustomerPortal"
                  />
                  Allow V 1 Form Customer Portal
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableChangeFromNextBillingDateLabel">
                  <AvInput
                    id="shop-info-enableChangeFromNextBillingDate"
                    type="checkbox"
                    className="form-check-input"
                    name="enableChangeFromNextBillingDate"
                  />
                  Enable Change From Next Billing Date
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableInventoryCheckLabel">
                  <AvInput id="shop-info-enableInventoryCheck" type="checkbox" className="form-check-input" name="enableInventoryCheck" />
                  Enable Inventory Check
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="whiteListedLabel">
                  <AvInput id="shop-info-whiteListed" type="checkbox" className="form-check-input" name="whiteListed" />
                  White Listed
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableAddJSInterceptorLabel">
                  <AvInput
                    id="shop-info-enableAddJSInterceptor"
                    type="checkbox"
                    className="form-check-input"
                    name="enableAddJSInterceptor"
                  />
                  Enable Add JS Interceptor
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="reBuyEnabledLabel">
                  <AvInput id="shop-info-reBuyEnabled" type="checkbox" className="form-check-input" name="reBuyEnabled" />
                  Re Buy Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="priceSyncEnabledLabel">
                  <AvInput id="shop-info-priceSyncEnabled" type="checkbox" className="form-check-input" name="priceSyncEnabled" />
                  Price Sync Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="discountSyncEnabledLabel">
                  <AvInput id="shop-info-discountSyncEnabled" type="checkbox" className="form-check-input" name="discountSyncEnabled" />
                  Discount Sync Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="skuSyncEnabledLabel">
                  <AvInput id="shop-info-skuSyncEnabled" type="checkbox" className="form-check-input" name="skuSyncEnabled" />
                  Sku Sync Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="klaviyoListIdLabel" for="shop-info-klaviyoListId">
                  Klaviyo List Id
                </Label>
                <AvField id="shop-info-klaviyoListId" type="text" name="klaviyoListId" />
              </AvGroup>
              <AvGroup>
                <Label id="scriptVersionLabel" for="shop-info-scriptVersion">
                  Script Version
                </Label>
                <AvInput
                  id="shop-info-scriptVersion"
                  type="select"
                  className="form-control"
                  name="scriptVersion"
                  value={(!isNew && shopInfoEntity.scriptVersion) || 'V1'}
                >
                  <option value="V1">V1</option>
                  <option value="V2">V2</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="buildBoxVersionLabel" for="shop-info-buildBoxVersion">
                  Build Box Version
                </Label>
                <AvInput
                  id="shop-info-buildBoxVersion"
                  type="select"
                  className="form-control"
                  name="buildBoxVersion"
                  value={(!isNew && shopInfoEntity.buildBoxVersion) || 'V1'}
                >
                  <option value="V1">V1</option>
                  <option value="V2">V2</option>
                  <option value="V2_IFRAME">V2_IFRAME</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="nextOrderDateAttributeKeyLabel" for="shop-info-nextOrderDateAttributeKey">
                  Next Order Date Attribute Key
                </Label>
                <AvField id="shop-info-nextOrderDateAttributeKey" type="text" name="nextOrderDateAttributeKey" />
              </AvGroup>
              <AvGroup>
                <Label id="nextOrderDateAttributeFormatLabel" for="shop-info-nextOrderDateAttributeFormat">
                  Next Order Date Attribute Format
                </Label>
                <AvField id="shop-info-nextOrderDateAttributeFormat" type="text" name="nextOrderDateAttributeFormat" />
              </AvGroup>
              <AvGroup>
                <Label id="shopifyWebhookUrlLabel" for="shop-info-shopifyWebhookUrl">
                  Shopify Webhook Url
                </Label>
                <AvInput id="shop-info-shopifyWebhookUrl" type="textarea" name="shopifyWebhookUrl" />
              </AvGroup>
              <AvGroup check>
                <Label id="verifiedEmailCustomDomainLabel">
                  <AvInput
                    id="shop-info-verifiedEmailCustomDomain"
                    type="checkbox"
                    className="form-check-input"
                    name="verifiedEmailCustomDomain"
                  />
                  Verified Email Custom Domain
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="localOrderDayOfWeekLabel" for="shop-info-localOrderDayOfWeek">
                  Local Order Day Of Week
                </Label>
                <AvInput
                  id="shop-info-localOrderDayOfWeek"
                  type="select"
                  className="form-control"
                  name="localOrderDayOfWeek"
                  value={(!isNew && shopInfoEntity.localOrderDayOfWeek) || ""}
                >
                  <option value="">-- Select --</option>
                  <option value="MONDAY">MONDAY</option>
                  <option value="TUESDAY">TUESDAY</option>
                  <option value="WEDNESDAY">WEDNESDAY</option>
                  <option value="THURSDAY">THURSDAY</option>
                  <option value="FRIDAY">FRIDAY</option>
                  <option value="SATURDAY">SATURDAY</option>
                  <option value="SUNDAY">SUNDAY</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="storeFrontAccessTokenLabel" for="shop-info-storeFrontAccessToken">
                  Store Front Access Token
                </Label>
                <AvField id="shop-info-storeFrontAccessToken" type="text" name="storeFrontAccessToken" />
              </AvGroup>
              <AvGroup check>
                <Label id="enableWebhookLabel">
                  <AvInput id="shop-info-enableWebhook" type="checkbox" className="form-check-input" name="enableWebhook" />
                  Enable Webhook
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="onBoardingPaymentSeenLabel">
                  <AvInput id="shop-info-onBoardingPaymentSeen" type="checkbox" className="form-check-input" name="onBoardingPaymentSeen" />
                  On Boarding Payment Seen
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="changeNextOrderDateOnBillingAttemptLabel">
                  <AvInput
                    id="shop-info-changeNextOrderDateOnBillingAttempt"
                    type="checkbox"
                    className="form-check-input"
                    name="changeNextOrderDateOnBillingAttempt"
                  />
                  Change Next Order Date On Billing Attempt
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="keepLineAttributesLabel">
                  <AvInput id="shop-info-keepLineAttributes" type="checkbox" className="form-check-input" name="keepLineAttributes" />
                  Keep Line Attributes
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="passwordEnabledLabel">
                  <AvInput id="shop-info-passwordEnabled" type="checkbox" className="form-check-input" name="passwordEnabled" />
                  Password Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="countryCodeLabel" for="shop-info-countryCode">
                  Country Code
                </Label>
                <AvField id="shop-info-countryCode" type="text" name="countryCode" />
              </AvGroup>
              <AvGroup>
                <Label id="countryNameLabel" for="shop-info-countryName">
                  Country Name
                </Label>
                <AvField id="shop-info-countryName" type="text" name="countryName" />
              </AvGroup>
              <AvGroup>
                <Label id="overwriteSettingLabel" for="shop-info-overwriteSetting">
                  Overwrite Setting
                </Label>
                <AvInput id="shop-info-overwriteSetting" type="textarea" name="overwriteSetting" />
              </AvGroup>
              <AvGroup>
                <Label id="mailchimpApiKeyLabel" for="shop-info-mailchimpApiKey">
                  Mailchimp Api Key
                </Label>
                <AvField id="shop-info-mailchimpApiKey" type="text" name="mailchimpApiKey" />
              </AvGroup>
              <AvGroup check>
                <Label id="carryForwardLastOrderNoteLabel">
                  <AvInput
                    id="shop-info-carryForwardLastOrderNote"
                    type="checkbox"
                    className="form-check-input"
                    name="carryForwardLastOrderNote"
                  />
                  Carry Forward Last Order Note
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="allowLocalDeliveryLabel">
                  <AvInput id="shop-info-allowLocalDelivery" type="checkbox" className="form-check-input" name="allowLocalDelivery" />
                  Allow Local Delivery
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="allowLocalPickupLabel">
                  <AvInput id="shop-info-allowLocalPickup" type="checkbox" className="form-check-input" name="allowLocalPickup" />
                  Allow Local Pickup
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="useShopifyAssetsLabel">
                  <AvInput id="shop-info-useShopifyAssets" type="checkbox" className="form-check-input" name="useShopifyAssets" />
                  Use Shopify Assets
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="zapietEnabledLabel">
                  <AvInput id="shop-info-zapietEnabled" type="checkbox" className="form-check-input" name="zapietEnabled" />
                  Zapiet Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="mechanicEnabledLabel">
                  <AvInput id="shop-info-mechanicEnabled" type="checkbox" className="form-check-input" name="mechanicEnabled" />
                  Mechanic Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enablePauseContractsAfterMaximumOrdersLabel">
                  <AvInput id="shop-info-enablePauseContractsAfterMaximumOrders" type="checkbox" className="form-check-input" name="enablePauseContractsAfterMaximumOrders" />
                  Pause Contracts After Completing Maximum Number Of Orders
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="appstleLoyaltyApiKeyLabel" for="shop-info-appstleLoyaltyApiKey">
                  Appstle Loyalty Api Key
                </Label>
                <AvField id="shop-info-appstleLoyaltyApiKey" type="text" name="appstleLoyaltyApiKey" />
              </AvGroup>
              <AvGroup>
                <Label id="eberLoyaltyApiKeyLabel" for="shop-info-eberLoyaltyApiKey">
                  Eber Loyalty Api Key
                </Label>
                <AvField id="shop-info-eberLoyaltyApiKey" type="text" name="eberLoyaltyApiKey" />
              </AvGroup>
              <AvGroup>
                <Label id="eberLoyaltyPointsLabel" for="shop-info-eberLoyaltyPoints">
                  Eber Loyalty Points
                </Label>
                <AvField id="shop-info-eberLoyaltyPoints" type="string" className="form-control" name="eberLoyaltyPoints" />
              </AvGroup>
              <AvGroup>
                <Label id="eberLoyaltyMultiplierLabel" for="shop-info-eberLoyaltyMultiplier">
                  Eber Loyalty Multiplier
                </Label>
                <AvField id="shop-info-eberLoyaltyMultiplier" type="string" className="form-control" name="eberLoyaltyMultiplier" />
              </AvGroup>
              <AvGroup check>
                <Label id="shopifyFlowEnabledLabel">
                  <AvInput id="shop-info-shopifyFlowEnabled" type="checkbox" className="form-check-input" name="shopifyFlowEnabled" />
                  Shopify Flow Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="overwriteAnchorDayLabel">
                  <AvInput id="shop-info-overwriteAnchorDay" type="checkbox" className="form-check-input" name="overwriteAnchorDay" />
                  Overwrite Anchor Day
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableChangeBillingPlanLabel">
                  <AvInput
                    id="shop-info-enableChangeBillingPlan"
                    type="checkbox"
                    className="form-check-input"
                    name="enableChangeBillingPlan"
                  />
                  Enable Change Billing Plan
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="lockBillingPlanCommentsLabel" for="shop-info-lockBillingPlanComments">
                  Lock Billing Plan Comments
                </Label>
                <AvField id="shop-info-lockBillingPlanComments" type="text" name="lockBillingPlanComments" />
              </AvGroup>
              <AvGroup check>
                <Label id="eberLoyaltyEnabledLabel">
                  <AvInput id="shop-info-eberLoyaltyEnabled" type="checkbox" className="form-check-input" name="eberLoyaltyEnabled" />
                  Eber Loyalty Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="upcomingEmailBufferDaysLabel" for="shop-info-upcomingEmailBufferDays">
                  Upcoming Email Buffer Days
                </Label>
                <AvField id="shop-info-upcomingEmailBufferDays" type="string" className="form-control" name="upcomingEmailBufferDays" />
              </AvGroup>
              <AvGroup>
                <Label id="inventoryLocationsLabel" for="shop-info-inventoryLocations">
                  Inventory Locations
                </Label>
                <AvField id="shop-info-inventoryLocations" type="text" name="inventoryLocations" />
              </AvGroup>
              <AvGroup>
                <Label id="shipperHqApiKeyLabel" for="shop-info-shipperHqApiKey">
                  Shipper Hq Api Key
                </Label>
                <AvField id="shop-info-shipperHqApiKey" type="text" name="shipperHqApiKey" />
              </AvGroup>
              <AvGroup>
                <Label id="shipperHqAuthCodeLabel" for="shop-info-shipperHqAuthCode">
                  Shipper Hq Auth Code
                </Label>
                <AvField id="shop-info-shipperHqAuthCode" type="text" name="shipperHqAuthCode" />
              </AvGroup>
              <AvGroup>
                <Label id="shipperHqAccessTokenLabel" for="shop-info-shipperHqAccessToken">
                  Shipper Hq Access Token
                </Label>
                <AvField id="shop-info-shipperHqAccessToken" type="text" name="shipperHqAccessToken" />
              </AvGroup>
              <AvGroup>
                <Label id="ianaTimeZoneLabel" for="shop-info-ianaTimeZone">
                  Iana Time Zone
                </Label>
                <AvField id="shop-info-ianaTimeZone" type="text" name="ianaTimeZone" />
              </AvGroup>
              <AvGroup check>
                <Label id="shipInsureEnabledLabel">
                  <AvInput id="shop-info-shipInsureEnabled" type="checkbox" className="form-check-input" name="shipInsureEnabled" />
                  Ship Insure Enabled
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/shop-info" replace color="info">
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
  shopInfoEntity: storeState.shopInfo.entity,
  loading: storeState.shopInfo.loading,
  updating: storeState.shopInfo.updating,
  updateSuccess: storeState.shopInfo.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(ShopInfoUpdate);
