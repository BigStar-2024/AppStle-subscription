import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SocialConnection from './social-connection';

import ShopInfo from './shop-info';

import PlanInfo from './plan-info';
import PaymentPlan from './payment-plan';

import ProductField from './product-field';
import UsergroupRule from './usergroup-rule';

import OrderInfo from './order-info';
import ShopSettings from './shop-settings';
import ProcessedOrderInfo from './processed-order-info';
import TriggerRule from './trigger-rule';
import BackdatingJobSummary from './backdating-job-summary';
import CustomerTriggerRule from './customer-trigger-rule';
import CustomerProcessedOrderInfo from './customer-processed-order-info';
import ThemeCode from './theme-code';
import ThemeSettings from './theme-settings';
import SubscriptionGroup from './subscription-group';
import SubscriptionContractDetails from './subscription-contract-details';
import SubscriptionWidgetSettings from './subscription-widget-settings';
import SubscriptionBillingAttempt from './subscription-billing-attempt';
import Analytics from './analytics';
import SubscriptionCustomCss from './subscription-custom-css';
import CustomerPortalSettings from './customer-portal-settings';
import EmailTemplateSetting from './email-template-setting';
import CustomerPayment from './customer-payment';
import EmailTemplate from './email-template';
import DunningManagement from './dunning-management';
import SubscriptionContractSettings from './subscription-contract-settings';
import SubscriptionGroupPlan from './subscription-group-plan';
import DeliveryProfile from './delivery-profile';
import ProductSwap from './product-swap';
import ProductCycle from './product-cycle';
import MemberOnly from './member-only';
import SellingPlanMemberInfo from './selling-plan-member-info';
import ActivityUpdatesSettings from './activity-updates-settings';
import SubscriptionBundling from './subscription-bundling';
import ProductInfo from './product-info';
import SmsTemplateSetting from './sms-template-setting';
import CancellationManagement from './cancellation-management';
import MembershipDiscount from './membership-discount';
import MembershipDiscountProducts from './membership-discount-products';
import MembershipDiscountCollections from './membership-discount-collections';
import SubscriptionBundleSettings from './subscription-bundle-settings';
import SubscriptionContractOneOff from './subscription-contract-one-off';
import CartWidgetSettings from './cart-widget-settings';
import CustomerPortalDynamicScript from './customer-portal-dynamic-script';
import BundleDynamicScript from './bundle-dynamic-script';
import ActivityLog from './activity-log';
import PlanInfoDiscount from './plan-info-discount';
import CurrencyConversionInfo from './currency-conversion-info';
import WidgetTemplate from './widget-template';
import BulkAutomation from './bulk-automation';
import BundleRule from './bundle-rule';
import BundleSetting from './bundle-setting';
import ShopLabel from './shop-label';
import ShopAssetUrls from './shop-asset-urls';
import AppstleMenuSettings from './appstle-menu-settings';
import AppstleMenuLabels from './appstle-menu-labels';
import VariantInfo from './variant-info';
import Customization from './customization';
import ShopCustomization from './shop-customization';
import OnboardingInfo from './onboarding-info';
import AsyncUpdateEventProcessing from './async-update-event-processing';
import SubscriptionContractProcessing from './subscription-contract-processing';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/social-connection`} component={SocialConnection} />

      <ErrorBoundaryRoute path={`${match.url}/shop-info`} component={ShopInfo} />

      <ErrorBoundaryRoute path={`${match.url}/plan-info`} component={PlanInfo} />
      <ErrorBoundaryRoute path={`${match.url}/payment-plan`} component={PaymentPlan} />

      <ErrorBoundaryRoute path={`${match.url}/product-field`} component={ProductField} />
      <ErrorBoundaryRoute path={`${match.url}/usergroup-rule`} component={UsergroupRule} />

      <ErrorBoundaryRoute path={`${match.url}/shop-info`} component={ShopInfo} />
      <ErrorBoundaryRoute path={`${match.url}/shop-settings`} component={ShopSettings} />
      <ErrorBoundaryRoute path={`${match.url}/theme-code`} component={ThemeCode} />
      <ErrorBoundaryRoute path={`${match.url}/theme-settings`} component={ThemeSettings} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-group`} component={SubscriptionGroup} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-contract-details`} component={SubscriptionContractDetails} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-widget-settings`} component={SubscriptionWidgetSettings} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-billing-attempt`} component={SubscriptionBillingAttempt} />
      <ErrorBoundaryRoute path={`${match.url}/analytics`} component={Analytics} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-custom-css`} component={SubscriptionCustomCss} />
      <ErrorBoundaryRoute path={`${match.url}/customer-portal-settings`} component={CustomerPortalSettings} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-bundle-settings`} component={SubscriptionBundleSettings} />
      <ErrorBoundaryRoute path={`${match.url}/email-template-setting`} component={EmailTemplateSetting} />
      <ErrorBoundaryRoute path={`${match.url}/customer-payment`} component={CustomerPayment} />
      <ErrorBoundaryRoute path={`${match.url}/email-template`} component={EmailTemplate} />
      <ErrorBoundaryRoute path={`${match.url}/dunning-management`} component={DunningManagement} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-contract-settings`} component={SubscriptionContractSettings} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-group-plan`} component={SubscriptionGroupPlan} />
      <ErrorBoundaryRoute path={`${match.url}/delivery-profile`} component={DeliveryProfile} />
      <ErrorBoundaryRoute path={`${match.url}/product-swap`} component={ProductSwap} />
      <ErrorBoundaryRoute path={`${match.url}/product-cycle`} component={ProductCycle} />
      <ErrorBoundaryRoute path={`${match.url}/member-only`} component={MemberOnly} />
      <ErrorBoundaryRoute path={`${match.url}/selling-plan-member-info`} component={SellingPlanMemberInfo} />
      <ErrorBoundaryRoute path={`${match.url}/activity-updates-settings`} component={ActivityUpdatesSettings} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-bundling`} component={SubscriptionBundling} />
      <ErrorBoundaryRoute path={`${match.url}/product-info`} component={ProductInfo} />
      <ErrorBoundaryRoute path={`${match.url}/sms-template-setting`} component={SmsTemplateSetting} />
      <ErrorBoundaryRoute path={`${match.url}/cancellation-management`} component={CancellationManagement} />
      <ErrorBoundaryRoute path={`${match.url}/membership-discount`} component={MembershipDiscount} />
      <ErrorBoundaryRoute path={`${match.url}/membership-discount-products`} component={MembershipDiscountProducts} />
      <ErrorBoundaryRoute path={`${match.url}/membership-discount-collections`} component={MembershipDiscountCollections} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-custom-css`} component={SubscriptionCustomCss} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-contract-one-off`} component={SubscriptionContractOneOff} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-contract-one-off`} component={SubscriptionContractOneOff} />
      <ErrorBoundaryRoute path={`${match.url}/cart-widget-settings`} component={CartWidgetSettings} />
      <ErrorBoundaryRoute path={`${match.url}/customer-portal-dynamic-script`} component={CustomerPortalDynamicScript} />
      <ErrorBoundaryRoute path={`${match.url}/bundle-dynamic-script`} component={BundleDynamicScript} />
      <ErrorBoundaryRoute path={`${match.url}/activity-log`} component={ActivityLog} />
      <ErrorBoundaryRoute path={`${match.url}/plan-info-discount`} component={PlanInfoDiscount} />
      <ErrorBoundaryRoute path={`${match.url}/currency-conversion-info`} component={CurrencyConversionInfo} />
      <ErrorBoundaryRoute path={`${match.url}/widget-template`} component={WidgetTemplate} />
      <ErrorBoundaryRoute path={`${match.url}/order-info`} component={OrderInfo} />
      <ErrorBoundaryRoute path={`${match.url}/bulk-automation`} component={BulkAutomation} />
      <ErrorBoundaryRoute path={`${match.url}/bundle-rule`} component={BundleRule} />
      <ErrorBoundaryRoute path={`${match.url}/bundle-setting`} component={BundleSetting} />
      <ErrorBoundaryRoute path={`${match.url}/shop-label`} component={ShopLabel} />
      <ErrorBoundaryRoute path={`${match.url}/shop-asset-urls`} component={ShopAssetUrls} />
      <ErrorBoundaryRoute path={`${match.url}/appstle-menu-settings`} component={AppstleMenuSettings} />
      <ErrorBoundaryRoute path={`${match.url}/appstle-menu-labels`} component={AppstleMenuLabels} />
      <ErrorBoundaryRoute path={`${match.url}/variant-info`} component={VariantInfo} />
      <ErrorBoundaryRoute path={`${match.url}/customization`} component={Customization} />
      <ErrorBoundaryRoute path={`${match.url}/shop-customization`} component={ShopCustomization} />
      <ErrorBoundaryRoute path={`${match.url}/onboarding-info`} component={OnboardingInfo} />
      <ErrorBoundaryRoute path={`${match.url}async-update-event-processing`} component={AsyncUpdateEventProcessing} />
      <ErrorBoundaryRoute path={`${match.url}subscription-contract-processing`} component={SubscriptionContractProcessing} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
