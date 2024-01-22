import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop-info.reducer';
import { IShopInfo } from 'app/shared/model/shop-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopInfoDetail = (props: IShopInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shopInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ShopInfo [<b>{shopInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{shopInfoEntity.shop}</dd>
          <dt>
            <span id="apiKey">Api Key</span>
          </dt>
          <dd>{shopInfoEntity.apiKey}</dd>
          <dt>
            <span id="shopTimeZone">Shop Time Zone</span>
          </dt>
          <dd>{shopInfoEntity.shopTimeZone}</dd>
          <dt>
            <span id="shopifyPlanDisplayName">Shopify Plan Display Name</span>
          </dt>
          <dd>{shopInfoEntity.shopifyPlanDisplayName}</dd>
          <dt>
            <span id="shopifyPlanName">Shopify Plan Name</span>
          </dt>
          <dd>{shopInfoEntity.shopifyPlanName}</dd>
          <dt>
            <span id="searchResultPageId">Search Result Page Id</span>
          </dt>
          <dd>{shopInfoEntity.searchResultPageId}</dd>
          <dt>
            <span id="publicDomain">Public Domain</span>
          </dt>
          <dd>{shopInfoEntity.publicDomain}</dd>
          <dt>
            <span id="moneyFormat">Money Format</span>
          </dt>
          <dd>{shopInfoEntity.moneyFormat}</dd>
          <dt>
            <span id="currency">Currency</span>
          </dt>
          <dd>{shopInfoEntity.currency}</dd>
          <dt>
            <span id="onboardingSeen">Onboarding Seen</span>
          </dt>
          <dd>{shopInfoEntity.onboardingSeen ? 'true' : 'false'}</dd>
          <dt>
            <span id="devEnabled">Dev Enabled</span>
          </dt>
          <dd>{shopInfoEntity.devEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="firstTimeOrderTag">First Time Order Tag</span>
          </dt>
          <dd>{shopInfoEntity.firstTimeOrderTag}</dd>
          <dt>
            <span id="recurringOrderTag">Recurring Order Tag</span>
          </dt>
          <dd>{shopInfoEntity.recurringOrderTag}</dd>
          <dt>
            <span id="customerActiveSubscriptionTag">Customer Active Subscription Tag</span>
          </dt>
          <dd>{shopInfoEntity.customerActiveSubscriptionTag}</dd>
          <dt>
            <span id="customerInActiveSubscriptionTag">Customer In Active Subscription Tag</span>
          </dt>
          <dd>{shopInfoEntity.customerInActiveSubscriptionTag}</dd>
          <dt>
            <span id="customerPausedSubscriptionTag">Customer Paused Subscription Tag</span>
          </dt>
          <dd>{shopInfoEntity.customerPausedSubscriptionTag}</dd>
          <dt>
            <span id="emailCustomDomain">Email Custom Domain</span>
          </dt>
          <dd>{shopInfoEntity.emailCustomDomain}</dd>
          <dt>
            <span id="loyaltyLionToken">Loyalty Lion Token</span>
          </dt>
          <dd>{shopInfoEntity.loyaltyLionToken}</dd>
          <dt>
            <span id="loyaltyLionSecret">Loyalty Lion Secret</span>
          </dt>
          <dd>{shopInfoEntity.loyaltyLionSecret}</dd>
          <dt>
            <span id="loyaltyLionPoints">Loyalty Lion Points</span>
          </dt>
          <dd>{shopInfoEntity.loyaltyLionPoints}</dd>
          <dt>
            <span id="loyaltyLionMultiplier">Loyalty Lion Multiplier</span>
          </dt>
          <dd>{shopInfoEntity.loyaltyLionMultiplier}</dd>
          <dt>
            <span id="loyaltyLionEnabled">Loyalty Lion Enabled</span>
          </dt>
          <dd>{shopInfoEntity.loyaltyLionEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="growaveClientId">Growave Client Id</span>
          </dt>
          <dd>{shopInfoEntity.growaveClientId}</dd>
          <dt>
            <span id="growaveSecret">Growave Secret</span>
          </dt>
          <dd>{shopInfoEntity.growaveSecret}</dd>
          <dt>
            <span id="growavePoints">Growave Points</span>
          </dt>
          <dd>{shopInfoEntity.growavePoints}</dd>
          <dt>
            <span id="growaveMultiplier">Growave Multiplier</span>
          </dt>
          <dd>{shopInfoEntity.growaveMultiplier}</dd>
          <dt>
            <span id="growaveEnabled">Growave Enabled</span>
          </dt>
          <dd>{shopInfoEntity.growaveEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="yotpoApiKey">Yotpo Api Key</span>
          </dt>
          <dd>{shopInfoEntity.yotpoApiKey}</dd>
          <dt>
            <span id="yotpoGuid">Yotpo Guid</span>
          </dt>
          <dd>{shopInfoEntity.yotpoGuid}</dd>
          <dt>
            <span id="yotpoPoints">Yotpo Points</span>
          </dt>
          <dd>{shopInfoEntity.yotpoPoints}</dd>
          <dt>
            <span id="yotpoMultiplier">Yotpo Multiplier</span>
          </dt>
          <dd>{shopInfoEntity.yotpoMultiplier}</dd>
          <dt>
            <span id="yotpoEnabled">Yotpo Enabled</span>
          </dt>
          <dd>{shopInfoEntity.yotpoEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="klaviyoPublicApiKey">Klaviyo Public Api Key</span>
          </dt>
          <dd>{shopInfoEntity.klaviyoPublicApiKey}</dd>
          <dt>
            <span id="klaviyoApiKey">Klaviyo Api Key</span>
          </dt>
          <dd>{shopInfoEntity.klaviyoApiKey}</dd>
          <dt>
            <span id="omniSendApiKey">Omni Send Api Key</span>
          </dt>
          <dd>{shopInfoEntity.omniSendApiKey}</dd>
          <dt>
            <span id="manageSubscriptionsUrl">Manage Subscriptions Url</span>
          </dt>
          <dd>{shopInfoEntity.manageSubscriptionsUrl}</dd>
          <dt>
            <span id="zapierSubscriptionCreatedUrl">Zapier Subscription Created Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionCreatedUrl}</dd>
          <dt>
            <span id="zapierSubscriptionUpdatedUrl">Zapier Subscription Updated Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionUpdatedUrl}</dd>
          <dt>
            <span id="zapierRecurringOrderPlacedUrl">Zapier Recurring Order Placed Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierRecurringOrderPlacedUrl}</dd>
          <dt>
            <span id="zapierCustomerSubscriptionsUpdatedUrl">Zapier Customer Subscriptions Updated Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierCustomerSubscriptionsUpdatedUrl}</dd>
          <dt>
            <span id="zapierNextOrderUpdatedUrl">Zapier Next Order Updated Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierNextOrderUpdatedUrl}</dd>
          <dt>
            <span id="zapierOrderFrequencyUpdatedUrl">Zapier Order Frequency Updated Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierOrderFrequencyUpdatedUrl}</dd>
          <dt>
            <span id="zapierShippingAddressUpdatedUrl">Zapier Shipping Address Updated Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierShippingAddressUpdatedUrl}</dd>
          <dt>
            <span id="zapierSubscriptionCanceledUrl">Zapier Subscription Canceled Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionCanceledUrl}</dd>
          <dt>
            <span id="zapierSubscriptionPausedUrl">Zapier Subscription Paused Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionPausedUrl}</dd>
          <dt>
            <span id="zapierSubscriptionProductAddedUrl">Zapier Subscription Product Added Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionProductAddedUrl}</dd>
          <dt>
            <span id="zapierSubscriptionProductRemovedUrl">Zapier Subscription Product Removed Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionProductRemovedUrl}</dd>
          <dt>
            <span id="zapierSubscriptionActivatedUrl">Zapier Subscription Activated Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionActivatedUrl}</dd>
          <dt>
            <span id="zapierSubscriptionTransactionFailedUrl">Zapier Subscription Transaction Failed Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionTransactionFailedUrl}</dd>
          <dt>
            <span id="zapierSubscriptionUpcomingOrderUrl">Zapier Subscription Upcoming Order Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionUpcomingOrderUrl}</dd>
          <dt>
            <span id="zapierSubscriptionExpiringCreditCardUrl">Zapier Subscription Expiring Credit Card Url</span>
          </dt>
          <dd>{shopInfoEntity.zapierSubscriptionExpiringCreditCardUrl}</dd>
          <dt>
            <span id="advancedCustomerPortal">Advanced Customer Portal</span>
          </dt>
          <dd>{shopInfoEntity.advancedCustomerPortal ? 'true' : 'false'}</dd>
          <dt>
            <span id="transferOrderNotesToSubscription">Transfer Order Notes To Subscription</span>
          </dt>
          <dd>{shopInfoEntity.transferOrderNotesToSubscription ? 'true' : 'false'}</dd>
          <dt>
            <span id="transferOrderNoteAttributesToSubscription">Transfer Order Note Attributes To Subscription</span>
          </dt>
          <dd>{shopInfoEntity.transferOrderNoteAttributesToSubscription ? 'true' : 'false'}</dd>
          <dt>
            <span id="transferOrderLineItemAttributesToSubscription">Transfer Order Line Item Attributes To Subscription</span>
          </dt>
          <dd>{shopInfoEntity.transferOrderLineItemAttributesToSubscription ? 'true' : 'false'}</dd>
          <dt>
            <span id="customerPortalMode">Customer Portal Mode</span>
          </dt>
          <dd>{shopInfoEntity.customerPortalMode}</dd>
          <dt>
            <span id="recurringOrderTime">Recurring Order Time</span>
          </dt>
          <dd>{shopInfoEntity.recurringOrderTime}</dd>
          <dt>
            <span id="recurringOrderHour">Recurring Order Hour</span>
          </dt>
          <dd>{shopInfoEntity.recurringOrderHour}</dd>
          <dt>
            <span id="recurringOrderMinute">Recurring Order Minute</span>
          </dt>
          <dd>{shopInfoEntity.recurringOrderMinute}</dd>
          <dt>
            <span id="zoneOffsetHours">Zone Offset Hours</span>
          </dt>
          <dd>{shopInfoEntity.zoneOffsetHours}</dd>
          <dt>
            <span id="zoneOffsetMinutes">Zone Offset Minutes</span>
          </dt>
          <dd>{shopInfoEntity.zoneOffsetMinutes}</dd>
          <dt>
            <span id="localOrderHour">Local Order Hour</span>
          </dt>
          <dd>{shopInfoEntity.localOrderHour}</dd>
          <dt>
            <span id="localOrderMinute">Local Order Minute</span>
          </dt>
          <dd>{shopInfoEntity.localOrderMinute}</dd>
          <dt>
            <span id="stopImportProcess">Stop Import Process</span>
          </dt>
          <dd>{shopInfoEntity.stopImportProcess ? 'true' : 'false'}</dd>
          <dt>
            <span id="disableShippingPricingAutoCalculation">Disable Shipping Pricing Auto Calculation</span>
          </dt>
          <dd>{shopInfoEntity.disableShippingPricingAutoCalculation ? 'true' : 'false'}</dd>
          <dt>
            <span id="allowV1FormCustomerPortal">Allow V 1 Form Customer Portal</span>
          </dt>
          <dd>{shopInfoEntity.allowV1FormCustomerPortal ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableChangeFromNextBillingDate">Enable Change From Next Billing Date</span>
          </dt>
          <dd>{shopInfoEntity.enableChangeFromNextBillingDate ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableInventoryCheck">Enable Inventory Check</span>
          </dt>
          <dd>{shopInfoEntity.enableInventoryCheck ? 'true' : 'false'}</dd>
          <dt>
            <span id="whiteListed">White Listed</span>
          </dt>
          <dd>{shopInfoEntity.whiteListed ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableAddJSInterceptor">Enable Add JS Interceptor</span>
          </dt>
          <dd>{shopInfoEntity.enableAddJSInterceptor ? 'true' : 'false'}</dd>
          <dt>
            <span id="reBuyEnabled">Re Buy Enabled</span>
          </dt>
          <dd>{shopInfoEntity.reBuyEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="priceSyncEnabled">Price Sync Enabled</span>
          </dt>
          <dd>{shopInfoEntity.priceSyncEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="discountSyncEnabled">Discount Sync Enabled</span>
          </dt>
          <dd>{shopInfoEntity.discountSyncEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="skuSyncEnabled">Sku Sync Enabled</span>
          </dt>
          <dd>{shopInfoEntity.skuSyncEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="klaviyoListId">Klaviyo List Id</span>
          </dt>
          <dd>{shopInfoEntity.klaviyoListId}</dd>
          <dt>
            <span id="scriptVersion">Script Version</span>
          </dt>
          <dd>{shopInfoEntity.scriptVersion}</dd>
          <dt>
            <span id="buildBoxVersion">Build Box Version</span>
          </dt>
          <dd>{shopInfoEntity.buildBoxVersion}</dd>
          <dt>
            <span id="nextOrderDateAttributeKey">Next Order Date Attribute Key</span>
          </dt>
          <dd>{shopInfoEntity.nextOrderDateAttributeKey}</dd>
          <dt>
            <span id="nextOrderDateAttributeFormat">Next Order Date Attribute Format</span>
          </dt>
          <dd>{shopInfoEntity.nextOrderDateAttributeFormat}</dd>
          <dt>
            <span id="shopifyWebhookUrl">Shopify Webhook Url</span>
          </dt>
          <dd>{shopInfoEntity.shopifyWebhookUrl}</dd>
          <dt>
            <span id="verifiedEmailCustomDomain">Verified Email Custom Domain</span>
          </dt>
          <dd>{shopInfoEntity.verifiedEmailCustomDomain ? 'true' : 'false'}</dd>
          <dt>
            <span id="localOrderDayOfWeek">Local Order Day Of Week</span>
          </dt>
          <dd>{shopInfoEntity.localOrderDayOfWeek}</dd>
          <dt>
            <span id="storeFrontAccessToken">Store Front Access Token</span>
          </dt>
          <dd>{shopInfoEntity.storeFrontAccessToken}</dd>
          <dt>
            <span id="enableWebhook">Enable Webhook</span>
          </dt>
          <dd>{shopInfoEntity.enableWebhook ? 'true' : 'false'}</dd>
          <dt>
            <span id="onBoardingPaymentSeen">On Boarding Payment Seen</span>
          </dt>
          <dd>{shopInfoEntity.onBoardingPaymentSeen ? 'true' : 'false'}</dd>
          <dt>
            <span id="changeNextOrderDateOnBillingAttempt">Change Next Order Date On Billing Attempt</span>
          </dt>
          <dd>{shopInfoEntity.changeNextOrderDateOnBillingAttempt ? 'true' : 'false'}</dd>
          <dt>
            <span id="keepLineAttributes">Keep Line Attributes</span>
          </dt>
          <dd>{shopInfoEntity.keepLineAttributes ? 'true' : 'false'}</dd>
          <dt>
            <span id="passwordEnabled">Password Enabled</span>
          </dt>
          <dd>{shopInfoEntity.passwordEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="countryCode">Country Code</span>
          </dt>
          <dd>{shopInfoEntity.countryCode}</dd>
          <dt>
            <span id="countryName">Country Name</span>
          </dt>
          <dd>{shopInfoEntity.countryName}</dd>
          <dt>
            <span id="overwriteSetting">Overwrite Setting</span>
          </dt>
          <dd>{shopInfoEntity.overwriteSetting}</dd>
          <dt>
            <span id="mailchimpApiKey">Mailchimp Api Key</span>
          </dt>
          <dd>{shopInfoEntity.mailchimpApiKey}</dd>
          <dt>
            <span id="carryForwardLastOrderNote">Carry Forward Last Order Note</span>
          </dt>
          <dd>{shopInfoEntity.carryForwardLastOrderNote ? 'true' : 'false'}</dd>
          <dt>
            <span id="allowLocalDelivery">Allow Local Delivery</span>
          </dt>
          <dd>{shopInfoEntity.allowLocalDelivery ? 'true' : 'false'}</dd>
          <dt>
            <span id="allowLocalPickup">Allow Local Pickup</span>
          </dt>
          <dd>{shopInfoEntity.allowLocalPickup ? 'true' : 'false'}</dd>
          <dt>
            <span id="useShopifyAssets">Use Shopify Assets</span>
          </dt>
          <dd>{shopInfoEntity.useShopifyAssets ? 'true' : 'false'}</dd>
          <dt>
            <span id="zapietEnabled">Zapiet Enabled</span>
          </dt>
          <dd>{shopInfoEntity.zapietEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="mechanicEnabled">Mechanic Enabled</span>
          </dt>
          <dd>{shopInfoEntity.mechanicEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="enablePauseContractsAfterMaximumOrders">Pause Contracts After Completing Maximum Number Of Orders</span>
          </dt>
          <dd>{shopInfoEntity.enablePauseContractsAfterMaximumOrders ? 'true' : 'false'}</dd>
          <dt>
            <span id="appstleLoyaltyApiKey">Appstle Loyalty Api Key</span>
          </dt>
          <dd>{shopInfoEntity.appstleLoyaltyApiKey}</dd>
          <dt>
            <span id="eberLoyaltyApiKey">Eber Loyalty Api Key</span>
          </dt>
          <dd>{shopInfoEntity.eberLoyaltyApiKey}</dd>
          <dt>
            <span id="eberLoyaltyPoints">Eber Loyalty Points</span>
          </dt>
          <dd>{shopInfoEntity.eberLoyaltyPoints}</dd>
          <dt>
            <span id="eberLoyaltyMultiplier">Eber Loyalty Multiplier</span>
          </dt>
          <dd>{shopInfoEntity.eberLoyaltyMultiplier}</dd>
          <dt>
            <span id="shopifyFlowEnabled">Shopify Flow Enabled</span>
          </dt>
          <dd>{shopInfoEntity.shopifyFlowEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="overwriteAnchorDay">Overwrite Anchor Day</span>
          </dt>
          <dd>{shopInfoEntity.overwriteAnchorDay ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableChangeBillingPlan">Enable Change Billing Plan</span>
          </dt>
          <dd>{shopInfoEntity.enableChangeBillingPlan ? 'true' : 'false'}</dd>
          <dt>
            <span id="lockBillingPlanComments">Lock Billing Plan Comments</span>
          </dt>
          <dd>{shopInfoEntity.lockBillingPlanComments}</dd>
          <dt>
            <span id="eberLoyaltyEnabled">Eber Loyalty Enabled</span>
          </dt>
          <dd>{shopInfoEntity.eberLoyaltyEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="upcomingEmailBufferDays">Upcoming Email Buffer Days</span>
          </dt>
          <dd>{shopInfoEntity.upcomingEmailBufferDays}</dd>
          <dt>
            <span id="inventoryLocations">Inventory Locations</span>
          </dt>
          <dd>{shopInfoEntity.inventoryLocations}</dd>
          <dt>
            <span id="shipperHqApiKey">Shipper Hq Api Key</span>
          </dt>
          <dd>{shopInfoEntity.shipperHqApiKey}</dd>
          <dt>
            <span id="shipperHqAuthCode">Shipper Hq Auth Code</span>
          </dt>
          <dd>{shopInfoEntity.shipperHqAuthCode}</dd>
          <dt>
            <span id="shipperHqAccessToken">Shipper Hq Access Token</span>
          </dt>
          <dd>{shopInfoEntity.shipperHqAccessToken}</dd>
          <dt>
            <span id="ianaTimeZone">Iana Time Zone</span>
          </dt>
          <dd>{shopInfoEntity.ianaTimeZone}</dd>
          <dt>
            <span id="shipInsureEnabled">Ship Insure Enabled</span>
          </dt>
          <dd>{shopInfoEntity.shipInsureEnabled ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/admin/shop-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/shop-info/${shopInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shopInfo }: IRootState) => ({
  shopInfoEntity: shopInfo.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopInfoDetail);
