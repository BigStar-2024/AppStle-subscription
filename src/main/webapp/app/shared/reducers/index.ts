import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';
import assets from 'app/modules/account/liquid-editor/assets';

import authentication, { AuthenticationState } from './authentication';
import appstleMenuCustomerState from '../../AppstleMenu/AppstleMenu/reducers/appstle-menu.reducer';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
import ThemeOptions from '../../reducers/ThemeOptions';
// prettier-ignore
import socialConnection, {SocialConnectionState} from 'app/entities/social-connection/social-connection.reducer';
// prettier-ignore
import planInfo, {PlanInfoState} from 'app/entities/plan-info/plan-info.reducer';
// prettier-ignore
import paymentPlan, {PaymentPlanState} from 'app/entities/payment-plan/payment-plan.reducer';

import shopInfo, { ShopInfoState } from 'app/entities/shop-info/shop-info.reducer';

import shopPaymentInfo, { ShopPaymentInfoState } from 'app/entities/shop-payment-info/shop-payment-info.reducer';

import shopifyShopInfo, { ShopifyShopInfoState } from '../../entities/shopify-shop-info/shopify-shop-info.reducer';

import billingUrl, { BillingUrlState } from 'app/entities/payment-plan/payment-billing.reducer';
// prettier-ignore
// prettier-ignore
import orderInfo, {OrderInfoState} from 'app/entities/order-info/order-info.reducer';
// prettier-ignore
import shopSettings, {ShopSettingsState} from 'app/entities/shop-settings/shop-settings.reducer';
// prettier-ignore
import processedOrderInfo, {
  ProcessedOrderInfoState
} from 'app/entities/processed-order-info/processed-order-info.reducer';
// prettier-ignore
import triggerRule, {TriggerRuleState} from 'app/entities/trigger-rule/trigger-rule.reducer';
// prettier-ignore
import backdatingJobSummary, {
  BackdatingJobSummaryState
} from 'app/entities/backdating-job-summary/backdating-job-summary.reducer';

import ruleCriteria from 'app/entities/rule-criteria/rule-criteria.reducer';
import location from 'app/entities/fields-render/data-location.reducer';
import carrierService from 'app/entities/fields-render/data-carrier-service.reducer';
import prdCollection from 'app/entities/fields-render/data-product-collections.reducer';
import prdVariant from 'app/entities/fields-render/data-product-variant.reducer';
import product from 'app/entities/fields-render/data-product.reducer';

// prettier-ignore
import customerTriggerRule, {
  CustomerTriggerRuleState
} from 'app/entities/customer-trigger-rule/customer-trigger-rule.reducer';
// prettier-ignore
import customerProcessedOrderInfo, {
  CustomerProcessedOrderInfoState
} from 'app/entities/customer-processed-order-info/customer-processed-order-info.reducer';
import customer, { CustomerState } from 'app/entities/customers/customer.reducer';
import subscription, { SubscriptionState } from 'app/entities/subscriptions/subscription.reducer';

// prettier-ignore
import themeCode, {ThemeCodeState} from 'app/entities/theme-code/theme-code.reducer';
// prettier-ignore
import themeSettings, {ThemeSettingsState} from 'app/entities/theme-settings/theme-settings.reducer';
// prettier-ignore
import subscriptionGroup, {SubscriptionGroupState} from 'app/entities/subscription-group/subscription-group.reducer';
// prettier-ignore
import subscriptionContractDetails, {
  SubscriptionContractDetailsState
} from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
// prettier-ignore
import userSubscriptionContractDetails, {
  UserSubscriptionContractDetailsState
} from 'app/entities/subscription-contract-details/user-subscription-contract-details.reducer';
// prettier-ignore
import subscriptionWidgetSettings, {
  SubscriptionWidgetSettingsState
} from 'app/entities/subscription-widget-settings/subscription-widget-settings.reducer';
// prettier-ignore
import subscriptionBillingAttempt, {
  SubscriptionBillingAttemptState
} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
// prettier-ignore
import analytics, {AnalyticsState} from 'app/entities/analytics/analytics.reducer';
// prettier-ignore
import subscriptionCustomCss, {
  SubscriptionCustomCssState
} from 'app/entities/subscription-custom-css/subscription-custom-css.reducer';
// prettier-ignore
import customerPortalSettings, {
  CustomerPortalSettingsState
} from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
// prettier-ignore
import emailTemplateSetting, {
  EmailTemplateSettingState
} from 'app/entities/email-template-setting/email-template-setting.reducer';
// prettier-ignore
import customerPayment, {CustomerPaymentState} from 'app/entities/customer-payment/customer-payment.reducer';
// prettier-ignore
import emailTemplate, {EmailTemplateState} from 'app/entities/email-template/email-template.reducer';
// prettier-ignore
import dunningManagement, {DunningManagementState} from 'app/entities/dunning-management/dunning-management.reducer';
// prettier-ignore
import subscriptionContractSettings, {
  SubscriptionContractSettingsState
} from 'app/entities/subscription-contract-settings/subscription-contract-settings.reducer';
// prettier-ignore
import subscriptionGroupPlan, {
  SubscriptionGroupPlanState
} from 'app/entities/subscription-group-plan/subscription-group-plan.reducer';
// prettier-ignore
import deliveryProfile, {DeliveryProfileState} from 'app/entities/delivery-profile/delivery-profile.reducer';
// prettier-ignore
import productSwap, {ProductSwapState} from 'app/entities/product-swap/product-swap.reducer';
// prettier-ignore
import productCycle, {ProductCycleState} from 'app/entities/product-cycle/product-cycle.reducer';
// prettier-ignore
import memberOnly, {MemberOnlyState} from 'app/entities/member-only/member-only.reducer';
// prettier-ignore
import sellingPlanMemberInfo, {
  SellingPlanMemberInfoState
} from 'app/entities/selling-plan-member-info/selling-plan-member-info.reducer';
// prettier-ignore
import activityUpdatesSettings, {
  ActivityUpdatesSettingsState
} from 'app/entities/activity-updates-settings/activity-updates-settings.reducer';
// prettier-ignore
import subscriptionBundling, {
  SubscriptionBundlingState
} from 'app/entities/subscription-bundling/subscription-bundling.reducer';
// prettier-ignore
import productInfo, {ProductInfoState} from 'app/entities/product-info/product-info.reducer';
// prettier-ignore
import smsTemplateSetting, {
  SmsTemplateSettingState
} from 'app/entities/sms-template-setting/sms-template-setting.reducer';
// prettier-ignore
import cancellationManagement, {
  CancellationManagementState
} from 'app/entities/cancellation-management/cancellation-management.reducer';
// prettier-ignore
import membershipDiscount, {
  MembershipDiscountState
} from 'app/entities/membership-discount/membership-discount.reducer';
// prettier-ignore
import membershipDiscountProducts, {
  MembershipDiscountProductsState
} from 'app/entities/membership-discount-products/membership-discount-products.reducer';
// prettier-ignore
import membershipDiscountCollections, {
  MembershipDiscountCollectionsState
} from 'app/entities/membership-discount-collections/membership-discount-collections.reducer';
// prettier-ignore
import subscriptionBundleSettings, {
  SubscriptionBundleSettingsState
} from 'app/entities/subscription-bundle-settings/subscription-bundle-settings.reducer';
// prettier-ignore
import subscriptionContractOneOff, {
  SubscriptionContractOneOffState
} from 'app/entities/subscription-contract-one-off/subscription-contract-one-off.reducer';
// prettier-ignore
import cartWidgetSettings, {
  CartWidgetSettingsState
} from 'app/entities/cart-widget-settings/cart-widget-settings.reducer';
// prettier-ignore
import customerPortalDynamicScript, {
  CustomerPortalDynamicScriptState
} from 'app/entities/customer-portal-dynamic-script/customer-portal-dynamic-script.reducer';
// prettier-ignore
import bundleDynamicScript, {
  BundleDynamicScriptState
} from 'app/entities/bundle-dynamic-script/bundle-dynamic-script.reducer';
// prettier-ignore
import activityLog, {ActivityLogState} from 'app/entities/activity-log/activity-log.reducer';
// prettier-ignore
import planInfoDiscount, {PlanInfoDiscountState} from 'app/entities/plan-info-discount/plan-info-discount.reducer';
// prettier-ignore
import currencyConversionInfo, {
  CurrencyConversionInfoState
} from 'app/entities/currency-conversion-info/currency-conversion-info.reducer';
// prettier-ignore
import widgetTemplate, {WidgetTemplateState} from 'app/entities/widget-template/widget-template.reducer';
// prettier-ignore
import bulkAutomation, {BulkAutomationState} from 'app/entities/bulk-automation/bulk-automation.reducer';
// prettier-ignore
import bundleRule, {BundleRuleState} from 'app/entities/bundle-rule/bundle-rule.reducer';
// prettier-ignore
import bundleSetting, {BundleSettingState} from 'app/entities/bundle-setting/bundle-setting.reducer';
// prettier-ignore
import shopLabel, {ShopLabelState} from 'app/entities/shop-label/shop-label.reducer';
// prettier-ignore
import shopAssetUrls, {ShopAssetUrlsState} from 'app/entities/shop-asset-urls/shop-asset-urls.reducer';
// prettier-ignore
import appstleMenuSettings, {
  AppstleMenuSettingsState
} from 'app/entities/appstle-menu-settings/appstle-menu-settings.reducer';

import appstleMenuAdmin, { AppstleMenuAdminState } from 'app/entities/appstle-menu-settings/appstle-menu-admin.reducer';

// prettier-ignore
import appstleMenuLabels, {AppstleMenuLabelsState} from 'app/entities/appstle-menu-labels/appstle-menu-labels.reducer';

import helperActions, { HelperState } from 'app/entities/shipping-profile/helper-data.reducer';
import shippingProfile, { ShippingProfileState } from 'app/entities/shipping-profile/shipping-profile.reducer';

// prettier-ignore
import variantInfo, {
  VariantInfoState
} from 'app/entities/variant-info/variant-info.reducer';
// prettier-ignore
import customization, {
  CustomizationState
} from 'app/entities/customization/customization.reducer';
// prettier-ignore
import shopCustomization, {
  ShopCustomizationState
} from 'app/entities/shop-customization/shop-customization.reducer';
// prettier-ignore
import onboardingInfo, {
  OnboardingInfoState
} from 'app/entities/onboarding-info/onboarding-info.reducer';
// prettier-ignore
import asyncUpdateEventProcessing, {
  AsyncUpdateEventProcessingState
} from 'app/entities/async-update-event-processing/async-update-event-processing.reducer';
// prettier-ignore
import subscriptionContractProcessing, {
  SubscriptionContractProcessingState
} from 'app/entities/subscription-contract-processing/subscription-contract-processing.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly ThemeOptions: any;
  readonly socialConnection: SocialConnectionState;
  readonly planInfo: PlanInfoState;
  readonly paymentPlan: PaymentPlanState;
  readonly billingUrl: BillingUrlState;
  readonly shopInfo: ShopInfoState;
  readonly shopifyShopInfo: ShopifyShopInfoState;
  readonly orderInfo: OrderInfoState;
  readonly shopSettings: ShopSettingsState;
  readonly processedOrderInfo: ProcessedOrderInfoState;
  readonly triggerRule: TriggerRuleState;
  readonly backdatingJobSummary: BackdatingJobSummaryState;
  readonly customerTriggerRule: CustomerTriggerRuleState;
  readonly customerProcessedOrderInfo: CustomerProcessedOrderInfoState;
  readonly themeCode: ThemeCodeState;
  readonly themeSettings: ThemeSettingsState;
  readonly customer: CustomerState;
  readonly subscription: SubscriptionState;
  readonly subscriptionGroup: SubscriptionGroupState;
  readonly subscriptionContractDetails: SubscriptionContractDetailsState;
  readonly userSubscriptionContractDetails: UserSubscriptionContractDetailsState;
  readonly subscriptionWidgetSettings: SubscriptionWidgetSettingsState;
  readonly subscriptionBillingAttempt: SubscriptionBillingAttemptState;
  readonly analytics: AnalyticsState;
  readonly subscriptionCustomCss: SubscriptionCustomCssState;
  readonly customerPortalSettings: CustomerPortalSettingsState;
  readonly emailTemplateSetting: EmailTemplateSettingState;
  readonly shopPaymentInfo: ShopPaymentInfoState;
  readonly customerPayment: CustomerPaymentState;
  readonly emailTemplate: EmailTemplateState;
  readonly dunningManagement: DunningManagementState;
  readonly subscriptionContractSettings: SubscriptionContractSettingsState;
  readonly subscriptionGroupPlan: SubscriptionGroupPlanState;
  readonly deliveryProfile: DeliveryProfileState;
  readonly productSwap: ProductSwapState;
  readonly productCycle: ProductCycleState;
  readonly memberOnly: MemberOnlyState;
  readonly sellingPlanMemberInfo: SellingPlanMemberInfoState;
  readonly activityUpdatesSettings: ActivityUpdatesSettingsState;
  readonly subscriptionBundling: SubscriptionBundlingState;
  readonly productInfo: ProductInfoState;
  readonly smsTemplateSetting: SmsTemplateSettingState;
  readonly cancellationManagement: CancellationManagementState;
  readonly membershipDiscount: MembershipDiscountState;
  readonly membershipDiscountProducts: MembershipDiscountProductsState;
  readonly membershipDiscountCollections: MembershipDiscountCollectionsState;
  readonly subscriptionBundleSettings: SubscriptionBundleSettingsState;
  readonly subscriptionContractOneOff: SubscriptionContractOneOffState;
  readonly cartWidgetSettings: CartWidgetSettingsState;
  readonly customerPortalDynamicScript: CustomerPortalDynamicScriptState;
  readonly bundleDynamicScript: BundleDynamicScriptState;
  readonly activityLog: ActivityLogState;
  readonly planInfoDiscount: PlanInfoDiscountState;
  readonly currencyConversionInfo: CurrencyConversionInfoState;
  readonly widgetTemplate: WidgetTemplateState;
  readonly bulkAutomation: BulkAutomationState;
  readonly bundleRule: BundleRuleState;
  readonly bundleSetting: BundleSettingState;
  readonly shopLabel: ShopLabelState;
  readonly shopAssetUrls: ShopAssetUrlsState;
  readonly appstleMenuSettings: AppstleMenuSettingsState;
  readonly appstleMenuAdmin: AppstleMenuAdminState;
  readonly appstleMenuLabels: AppstleMenuLabelsState;
  readonly helperActions: HelperState;
  readonly shippingProfile: ShippingProfileState;
  readonly variantInfo: VariantInfoState;
  readonly customization: CustomizationState;
  readonly shopCustomization: ShopCustomizationState;
  readonly onboardingInfo: OnboardingInfoState;
  readonly asyncUpdateEventProcessing: AsyncUpdateEventProcessingState;
  readonly subscriptionContractProcessing: SubscriptionContractProcessingState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
  readonly assets: any;
  readonly form: any;
  readonly ruleCriteria: any;
  readonly location: any;
  readonly carrierService: any;
  readonly prdCollection: any;
  readonly prdVariant: any;
  readonly product: any;
  readonly appstleMenuCustomerState: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  appstleMenuCustomerState,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  ThemeOptions,
  shopInfo,
  socialConnection,
  planInfo,
  paymentPlan,
  billingUrl,
  shopifyShopInfo,
  orderInfo,
  shopSettings,
  processedOrderInfo,
  triggerRule,
  backdatingJobSummary,
  customerTriggerRule,
  customerProcessedOrderInfo,
  themeCode,
  themeSettings,
  subscriptionGroup,
  subscriptionContractDetails,
  userSubscriptionContractDetails,
  subscriptionWidgetSettings,
  subscriptionBillingAttempt,
  analytics,
  subscriptionCustomCss,
  customerPortalSettings,
  emailTemplateSetting,
  customerPayment,
  emailTemplate,
  dunningManagement,
  subscriptionContractSettings,
  subscriptionGroupPlan,
  deliveryProfile,
  productSwap,
  productCycle,
  memberOnly,
  sellingPlanMemberInfo,
  activityUpdatesSettings,
  subscriptionBundling,
  productInfo,
  smsTemplateSetting,
  cancellationManagement,
  membershipDiscount,
  membershipDiscountProducts,
  membershipDiscountCollections,
  subscriptionBundleSettings,
  subscriptionContractOneOff,
  cartWidgetSettings,
  customerPortalDynamicScript,
  bundleDynamicScript,
  activityLog,
  planInfoDiscount,
  currencyConversionInfo,
  widgetTemplate,
  bulkAutomation,
  bundleRule,
  bundleSetting,
  shopLabel,
  shopAssetUrls,
  appstleMenuSettings,
  appstleMenuAdmin,
  appstleMenuLabels,
  helperActions,
  shippingProfile,
  variantInfo,
  customization,
  shopCustomization,
  onboardingInfo,
  asyncUpdateEventProcessing,
  subscriptionContractProcessing,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
  assets,
  form: formReducer,
  ruleCriteria,
  location,
  carrierService,
  prdCollection,
  prdVariant,
  product,
  customer,
  subscription,
  shopPaymentInfo,
});

export default rootReducer;
