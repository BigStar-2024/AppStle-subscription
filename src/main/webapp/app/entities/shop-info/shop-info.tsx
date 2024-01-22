import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './shop-info.reducer';
import { IShopInfo } from 'app/shared/model/shop-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ShopInfo = (props: IShopInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { shopInfoList, match, loading } = props;
  return (
    <div>
      <h2 id="shop-info-heading">
        Shop Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Shop Info
        </Link>
      </h2>
      <div className="table-responsive">
        {shopInfoList && shopInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Api Key</th>
                <th>Shop Time Zone</th>
                <th>Shopify Plan Display Name</th>
                <th>Shopify Plan Name</th>
                <th>Search Result Page Id</th>
                <th>Public Domain</th>
                <th>Money Format</th>
                <th>Currency</th>
                <th>Onboarding Seen</th>
                <th>Dev Enabled</th>
                <th>First Time Order Tag</th>
                <th>Recurring Order Tag</th>
                <th>Customer Active Subscription Tag</th>
                <th>Customer In Active Subscription Tag</th>
                <th>Customer Paused Subscription Tag</th>
                <th>Email Custom Domain</th>
                <th>Loyalty Lion Token</th>
                <th>Loyalty Lion Secret</th>
                <th>Loyalty Lion Points</th>
                <th>Loyalty Lion Multiplier</th>
                <th>Loyalty Lion Enabled</th>
                <th>Growave Client Id</th>
                <th>Growave Secret</th>
                <th>Growave Points</th>
                <th>Growave Multiplier</th>
                <th>Growave Enabled</th>
                <th>Yotpo Api Key</th>
                <th>Yotpo Guid</th>
                <th>Yotpo Points</th>
                <th>Yotpo Multiplier</th>
                <th>Yotpo Enabled</th>
                <th>Klaviyo Public Api Key</th>
                <th>Klaviyo Api Key</th>
                <th>Omni Send Api Key</th>
                <th>Manage Subscriptions Url</th>
                <th>Zapier Subscription Created Url</th>
                <th>Zapier Subscription Updated Url</th>
                <th>Zapier Recurring Order Placed Url</th>
                <th>Zapier Customer Subscriptions Updated Url</th>
                <th>Zapier Next Order Updated Url</th>
                <th>Zapier Order Frequency Updated Url</th>
                <th>Zapier Shipping Address Updated Url</th>
                <th>Zapier Subscription Canceled Url</th>
                <th>Zapier Subscription Paused Url</th>
                <th>Zapier Subscription Product Added Url</th>
                <th>Zapier Subscription Product Removed Url</th>
                <th>Zapier Subscription Activated Url</th>
                <th>Zapier Subscription Transaction Failed Url</th>
                <th>Zapier Subscription Upcoming Order Url</th>
                <th>Zapier Subscription Expiring Credit Card Url</th>
                <th>Advanced Customer Portal</th>
                <th>Transfer Order Notes To Subscription</th>
                <th>Transfer Order Note Attributes To Subscription</th>
                <th>Transfer Order Line Item Attributes To Subscription</th>
                <th>Customer Portal Mode</th>
                <th>Recurring Order Time</th>
                <th>Recurring Order Hour</th>
                <th>Recurring Order Minute</th>
                <th>Zone Offset Hours</th>
                <th>Zone Offset Minutes</th>
                <th>Local Order Hour</th>
                <th>Local Order Minute</th>
                <th>Stop Import Process</th>
                <th>Disable Shipping Pricing Auto Calculation</th>
                <th>Allow V 1 Form Customer Portal</th>
                <th>Enable Change From Next Billing Date</th>
                <th>Enable Inventory Check</th>
                <th>White Listed</th>
                <th>Enable Add JS Interceptor</th>
                <th>Re Buy Enabled</th>
                <th>Price Sync Enabled</th>
                <th>Discount Sync Enabled</th>
                <th>Sku Sync Enabled</th>
                <th>Klaviyo List Id</th>
                <th>Script Version</th>
                <th>Build Box Version</th>
                <th>Next Order Date Attribute Key</th>
                <th>Next Order Date Attribute Format</th>
                <th>Shopify Webhook Url</th>
                <th>Verified Email Custom Domain</th>
                <th>Local Order Day Of Week</th>
                <th>Store Front Access Token</th>
                <th>Enable Webhook</th>
                <th>On Boarding Payment Seen</th>
                <th>Change Next Order Date On Billing Attempt</th>
                <th>Keep Line Attributes</th>
                <th>Password Enabled</th>
                <th>Country Code</th>
                <th>Country Name</th>
                <th>Overwrite Setting</th>
                <th>Mailchimp Api Key</th>
                <th>Carry Forward Last Order Note</th>
                <th>Allow Local Delivery</th>
                <th>Allow Local Pickup</th>
                <th>Use Shopify Assets</th>
                <th>Zapiet Enabled</th>
                <th>Mechanic Enabled</th>
                <th>Pause Contracts After Completing Maximum Number Of Orders</th>
                <th>Appstle Loyalty Api Key</th>
                <th>Eber Loyalty Api Key</th>
                <th>Eber Loyalty Points</th>
                <th>Eber Loyalty Multiplier</th>
                <th>Shopify Flow Enabled</th>
                <th>Overwrite Anchor Day</th>
                <th>Enable Change Billing Plan</th>
                <th>Lock Billing Plan Comments</th>
                <th>Eber Loyalty Enabled</th>
                <th>Upcoming Email Buffer Days</th>
                <th>Inventory Locations</th>
                <th>Shipper Hq Api Key</th>
                <th>Shipper Hq Auth Code</th>
                <th>Shipper Hq Access Token</th>
                <th>Iana Time Zone</th>
                <th>Ship Insure Enabled</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shopInfoList.map((shopInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${shopInfo.id}`} color="link" size="sm">
                      {shopInfo.id}
                    </Button>
                  </td>
                  <td>{shopInfo.shop}</td>
                  <td>{shopInfo.apiKey}</td>
                  <td>{shopInfo.shopTimeZone}</td>
                  <td>{shopInfo.shopifyPlanDisplayName}</td>
                  <td>{shopInfo.shopifyPlanName}</td>
                  <td>{shopInfo.searchResultPageId}</td>
                  <td>{shopInfo.publicDomain}</td>
                  <td>{shopInfo.moneyFormat}</td>
                  <td>{shopInfo.currency}</td>
                  <td>{shopInfo.onboardingSeen ? 'true' : 'false'}</td>
                  <td>{shopInfo.devEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.firstTimeOrderTag}</td>
                  <td>{shopInfo.recurringOrderTag}</td>
                  <td>{shopInfo.customerActiveSubscriptionTag}</td>
                  <td>{shopInfo.customerInActiveSubscriptionTag}</td>
                  <td>{shopInfo.customerPausedSubscriptionTag}</td>
                  <td>{shopInfo.emailCustomDomain}</td>
                  <td>{shopInfo.loyaltyLionToken}</td>
                  <td>{shopInfo.loyaltyLionSecret}</td>
                  <td>{shopInfo.loyaltyLionPoints}</td>
                  <td>{shopInfo.loyaltyLionMultiplier}</td>
                  <td>{shopInfo.loyaltyLionEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.growaveClientId}</td>
                  <td>{shopInfo.growaveSecret}</td>
                  <td>{shopInfo.growavePoints}</td>
                  <td>{shopInfo.growaveMultiplier}</td>
                  <td>{shopInfo.growaveEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.yotpoApiKey}</td>
                  <td>{shopInfo.yotpoGuid}</td>
                  <td>{shopInfo.yotpoPoints}</td>
                  <td>{shopInfo.yotpoMultiplier}</td>
                  <td>{shopInfo.yotpoEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.klaviyoPublicApiKey}</td>
                  <td>{shopInfo.klaviyoApiKey}</td>
                  <td>{shopInfo.omniSendApiKey}</td>
                  <td>{shopInfo.manageSubscriptionsUrl}</td>
                  <td>{shopInfo.zapierSubscriptionCreatedUrl}</td>
                  <td>{shopInfo.zapierSubscriptionUpdatedUrl}</td>
                  <td>{shopInfo.zapierRecurringOrderPlacedUrl}</td>
                  <td>{shopInfo.zapierCustomerSubscriptionsUpdatedUrl}</td>
                  <td>{shopInfo.zapierNextOrderUpdatedUrl}</td>
                  <td>{shopInfo.zapierOrderFrequencyUpdatedUrl}</td>
                  <td>{shopInfo.zapierShippingAddressUpdatedUrl}</td>
                  <td>{shopInfo.zapierSubscriptionCanceledUrl}</td>
                  <td>{shopInfo.zapierSubscriptionPausedUrl}</td>
                  <td>{shopInfo.zapierSubscriptionProductAddedUrl}</td>
                  <td>{shopInfo.zapierSubscriptionProductRemovedUrl}</td>
                  <td>{shopInfo.zapierSubscriptionActivatedUrl}</td>
                  <td>{shopInfo.zapierSubscriptionTransactionFailedUrl}</td>
                  <td>{shopInfo.zapierSubscriptionUpcomingOrderUrl}</td>
                  <td>{shopInfo.zapierSubscriptionExpiringCreditCardUrl}</td>
                  <td>{shopInfo.advancedCustomerPortal ? 'true' : 'false'}</td>
                  <td>{shopInfo.transferOrderNotesToSubscription ? 'true' : 'false'}</td>
                  <td>{shopInfo.transferOrderNoteAttributesToSubscription ? 'true' : 'false'}</td>
                  <td>{shopInfo.transferOrderLineItemAttributesToSubscription ? 'true' : 'false'}</td>
                  <td>{shopInfo.customerPortalMode}</td>
                  <td>{shopInfo.recurringOrderTime}</td>
                  <td>{shopInfo.recurringOrderHour}</td>
                  <td>{shopInfo.recurringOrderMinute}</td>
                  <td>{shopInfo.zoneOffsetHours}</td>
                  <td>{shopInfo.zoneOffsetMinutes}</td>
                  <td>{shopInfo.localOrderHour}</td>
                  <td>{shopInfo.localOrderMinute}</td>
                  <td>{shopInfo.stopImportProcess ? 'true' : 'false'}</td>
                  <td>{shopInfo.disableShippingPricingAutoCalculation ? 'true' : 'false'}</td>
                  <td>{shopInfo.allowV1FormCustomerPortal ? 'true' : 'false'}</td>
                  <td>{shopInfo.enableChangeFromNextBillingDate ? 'true' : 'false'}</td>
                  <td>{shopInfo.enableInventoryCheck ? 'true' : 'false'}</td>
                  <td>{shopInfo.whiteListed ? 'true' : 'false'}</td>
                  <td>{shopInfo.enableAddJSInterceptor ? 'true' : 'false'}</td>
                  <td>{shopInfo.reBuyEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.priceSyncEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.discountSyncEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.skuSyncEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.klaviyoListId}</td>
                  <td>{shopInfo.scriptVersion}</td>
                  <td>{shopInfo.buildBoxVersion}</td>
                  <td>{shopInfo.nextOrderDateAttributeKey}</td>
                  <td>{shopInfo.nextOrderDateAttributeFormat}</td>
                  <td>{shopInfo.shopifyWebhookUrl}</td>
                  <td>{shopInfo.verifiedEmailCustomDomain ? 'true' : 'false'}</td>
                  <td>{shopInfo.localOrderDayOfWeek}</td>
                  <td>{shopInfo.storeFrontAccessToken}</td>
                  <td>{shopInfo.enableWebhook ? 'true' : 'false'}</td>
                  <td>{shopInfo.onBoardingPaymentSeen ? 'true' : 'false'}</td>
                  <td>{shopInfo.changeNextOrderDateOnBillingAttempt ? 'true' : 'false'}</td>
                  <td>{shopInfo.keepLineAttributes ? 'true' : 'false'}</td>
                  <td>{shopInfo.passwordEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.countryCode}</td>
                  <td>{shopInfo.countryName}</td>
                  <td>{shopInfo.overwriteSetting}</td>
                  <td>{shopInfo.mailchimpApiKey}</td>
                  <td>{shopInfo.carryForwardLastOrderNote ? 'true' : 'false'}</td>
                  <td>{shopInfo.allowLocalDelivery ? 'true' : 'false'}</td>
                  <td>{shopInfo.allowLocalPickup ? 'true' : 'false'}</td>
                  <td>{shopInfo.useShopifyAssets ? 'true' : 'false'}</td>
                  <td>{shopInfo.zapietEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.mechanicEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.enablePauseContractsAfterMaximumOrders ? 'true' : 'false'}</td>
                  <td>{shopInfo.appstleLoyaltyApiKey}</td>
                  <td>{shopInfo.eberLoyaltyApiKey}</td>
                  <td>{shopInfo.eberLoyaltyPoints}</td>
                  <td>{shopInfo.eberLoyaltyMultiplier}</td>
                  <td>{shopInfo.shopifyFlowEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.overwriteAnchorDay ? 'true' : 'false'}</td>
                  <td>{shopInfo.enableChangeBillingPlan ? 'true' : 'false'}</td>
                  <td>{shopInfo.lockBillingPlanComments}</td>
                  <td>{shopInfo.eberLoyaltyEnabled ? 'true' : 'false'}</td>
                  <td>{shopInfo.upcomingEmailBufferDays}</td>
                  <td>{shopInfo.inventoryLocations}</td>
                  <td>{shopInfo.shipperHqApiKey}</td>
                  <td>{shopInfo.shipperHqAuthCode}</td>
                  <td>{shopInfo.shipperHqAccessToken}</td>
                  <td>{shopInfo.ianaTimeZone}</td>
                  <td>{shopInfo.shipInsureEnabled ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${shopInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shopInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shopInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Shop Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ shopInfo }: IRootState) => ({
  shopInfoList: shopInfo.entities,
  loading: shopInfo.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopInfo);
