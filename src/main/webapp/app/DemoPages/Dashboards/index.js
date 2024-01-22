import PlanInfoList from 'app/DemoPages/Dashboards/PaymentPlan/PlanInfoList';
import {differenceInDays} from 'app/shared/util/date-utils';
import React, {Fragment} from 'react';
import Loader from 'react-loaders';
import {connect} from 'react-redux';
import {Link, Redirect, Route, withRouter} from 'react-router-dom';
import AppFooter from 'app/DemoPages/AppFooter/';
import Feedback from './Feedback/Feedback';
import {Alert, Button, Row, Col} from 'reactstrap';
import withScreenSize from 'app/shared/util/withScreenSize';
// Layout
import AppHeader from '../../Layout/AppHeader/';
import AppSidebar from '../../Layout/AppSidebar/';
import {getSession} from 'app/shared/reducers/authentication';
import Setting from './Setting/Setting';
import {getEntities} from 'app/entities/shop-payment-info/shop-payment-info.reducer';

// Subscription Components
import CustomersList from 'app/DemoPages/Dashboards/Customers/CustomerList';
import CustomerDetail from 'app/DemoPages/Dashboards/Customers/CustomerDetails';
import SubscriptionList from 'app/DemoPages/Dashboards/Subscriptions/SubscriptionList';
import SubscriptionDetail from 'app/DemoPages/Dashboards/Subscriptions/SubscriptionDetail';
import SubscriptionGroupList from 'app/DemoPages/Dashboards/SubscriptionGroups/SubscriptionGroupList';
import CreateSubscriptionGroup from 'app/DemoPages/Dashboards/SubscriptionGroups/CreateSubscriptionGroup';

import CustomizeThemeIntegration from 'app/DemoPages/Dashboards/ThemeIntegration/CustomizeThemeIntegration';
import Integrations from 'app/DemoPages/Dashboards/Integrations/integrations';
import CustomerPortalCSS from 'app/DemoPages/Dashboards/CustomerPortalCss/CustomerPortalCSS';
import CustomBundleCSS from 'app/DemoPages/Dashboards/BundleCSS/CustomBundleCSS';
import CustomerPortalSetting
  from 'app/DemoPages/Dashboards/GeneralSetting/CustomizeWidgetSetting/CustomerPortalSetting';

import ShopSettings from 'app/DemoPages/Dashboards/GeneralSetting/ShopSettings/ShopSettings';

import Tutorials from 'app/DemoPages/Dashboards/Tutorials/Tutorials';

import API from 'app/DemoPages/Dashboards/GeneralSetting/API/API';

import Analytics from 'app/DemoPages/Dashboards/Analytics/Analytics.tsx';
import PaymentPlan from './PaymentPlan';
import {getPaymentPlanLimitInformation, getTestChargeInformation} from 'app/entities/payment-plan/payment-plan.reducer';

import EmailTemplates from 'app/DemoPages/Dashboards/EmailTemplateSetting/EmailTemplates';
import EmailTemplateDetails from './EmailTemplateSetting/EmailTemplateDetails';

import DunningManagement from 'app/DemoPages/Dashboards/GeneralSetting/DunningManagemnt/DunningManagement';
import ShopSetting from './GeneralSetting/ShopSetting/ShopSetting';

import ShippingProfileList from 'app/DemoPages/Dashboards/ShippingProfile/ShippingProfileList';
import SwapProduct from './AdvancedSetting/SwapProduct/SwapProduct';
import SwapProductList from './AdvancedSetting/SwapProduct/SwapProductList';
import CreateShippingprofile from './ShippingProfile/CreateShippingprofile';

import SellingPlanMemberInfoList from './SellingPlanMemberInfo/SellingPlanMemberInfoList';
import ManageSellingPlanMemberInfo from './SellingPlanMemberInfo/ManageSellingPlanMemberInfo';
import ViewShippingProfile from './ShippingProfile/ViewShippingProfile';
import EmailDomainSettings from 'app/DemoPages/Dashboards/EmailTemplateSetting/EmailDomainSettings';
import WidgetPlacement from 'app/DemoPages/Dashboards/WidgetPlacement/WidgetPlacement';
import ActivityUpdatesSummaryReports from 'app/DemoPages/Dashboards/ActivityUpdates/ActivityUpdatesSummaryReports';
import SubscriptionBundlingList from './SubscriptionBundling/SubscriptionBundlingList';
import ManageSubscriptionBundling from './SubscriptionBundling/ManageSubscriptionBundling';
import SmsTemplateSetting from 'app/DemoPages/Dashboards/SmsTemplateSetting/SmsTemplateSetting';
import SmsTemplateList from 'app/DemoPages/Dashboards/SmsTemplateSetting/SmsTemplateList';
import CancellationManagement from './GeneralSetting/CancellationManagement/CancellationManagementV2';
import MembershipDiscountList from './MembershipDiscount/MembershipDiscountList';
import MembershipDiscountManage from './MembershipDiscount/MembershipDiscountManage';
import Reports from './Report/Reports';
import SubscriptionBundleSetting from './GeneralSetting/SubscriptionBundleSetting/SubscriptionBundleSetting';
import {
  getEntity as getThemeSettings,
  updateThemeName,
  updateThemeSkipSetting,
  updateToV2
} from 'app/entities/theme-settings/theme-settings.reducer';
import {ShopifyThemeInstallationVersion} from 'app/shared/model/enumerations/shopify-theme-installation-version.model';
import AppEmbeddedPopUp from 'app/DemoPages/Dashboards/ThemePickerPopUp/AppEmbeddedPopUp';
import CartWidgetSettings from './GeneralSetting/CustomizeWidgetSetting/CartWidgetSettings';
import Recommendedapps from './RecommendedApps/Recommendedapps';
import SubscriptionAutomation from './AdvancedSetting/Automation/SubscriptionAutomation';
import SubscriptionActivityList from './ActivityLog/SubscriptionActivityList';
import QuickActions from 'app/DemoPages/Dashboards/QuickActions/QuickActions';
import CreateSubscriptionLoyalty from './SubscriptionGroups/CreateSubscriptionLoyalty';
import CreateSubscription from './Subscriptions/CreateSubscription';
import {BasedOn} from 'app/shared/model/enumerations/based-on.model';
import WidgetTypes from './WidgetTypes/WidgetTypes';
import MigrationPage from './MigrationPage/MigrationPage';
import QuickCheckout from './Subscriptions/QuickCheckout';
import BundlesList from 'app/DemoPages/Dashboards/Bundling/Bundles/BundlesList';
import UpdateBundles from 'app/DemoPages/Dashboards/Bundling/Bundles/UpdateBundles';
import DeleteBundleRule from 'app/DemoPages/Dashboards/Bundling/Bundles/bundle-rule-delete-dialog';
import BundleSettings from 'app/DemoPages/Dashboards/Bundling/Settings/BundleSettings';
import Dashboard from 'app/DemoPages/Dashboards/Dashboard/Dashboard';
import ProductPageWidget from './GeneralSetting/CustomizeWidgetSetting/ProductPageWidget';
import CartPageWidget from './GeneralSetting/CustomizeWidgetSetting/CartPageWidget';
import HomeAndCollectionPageWidget from './GeneralSetting/CustomizeWidgetSetting/HomeAndCollectionPageWidget';
import CustomerPortalSettingMigrate from './GeneralSetting/CustomizeWidgetSetting/CustomerPortalSettingMigrate';
import axios from 'axios';
import AppstleMenu from 'app/DemoPages/Dashboards/AppstleMenu/AppstleMenu';
import WebhookIntegration from 'app/DemoPages/Dashboards/WebhookIntegration/WebhookIntegration';
import BuildAppstleMenu from 'app/DemoPages/Dashboards/AppstleMenu/BuildAppstleMenu';
import { updateEntity as updateShopInfoEntity, getShopInfoEntity, setAppEmbed, getMissingAccessScopes } from 'app/entities/shop-info/shop-info.reducer';
import PaymentUpgradeModal from 'app/DemoPages/Components/PaymentUpgradeModal/PaymentUpgradeModal';
import { getShopifyShopInfo } from 'app/entities/shopify-shop-info/shopify-shop-info.reducer';
import CreateShippingProfileV2 from 'app/DemoPages/Dashboards/ShippingProfile/CreateShippingProfileV2';
import ViewShippingProfileV2 from 'app/DemoPages/Dashboards/ShippingProfile/ViewShippingProfileV2';
import EventData from 'app/DemoPages/Dashboards/EventData/EventData';
import BulkAutomationsActivityLog from 'app/entities/bulk-automation/BulkAutomationsActivityLog';
import BulkEmails from './EmailTemplateSetting/BulkEmails';
import BulkEmailDetails from './EmailTemplateSetting/BulkEmailDetails';
import EmbeddedExternalLink from './Utilities/EmbeddedExternalLink';
import { getEntity as getOnboardingInfo } from 'app/entities/onboarding-info/onboarding-info.reducer'
import FAQPage from './FAQ/FAQ';
import {getProfile} from '../../shared/reducers/application-profile';
import {getEntity as getShopInfo} from 'app/entities/shop-info/shop-info.reducer';
import {getPaymentPlanByShop} from 'app/entities/payment-plan/payment-plan.reducer';
import MySaveButton from './Utilities/MySaveButton';


class Dashboards extends React.Component {
  componentDidMount() {
    this.props.getSession();
    this.props.getPaymentPlanLimitInformation();
    this.props.getEntities();
    this.props.getTestChargeInformation();
    this.props.getShopifyShopInfo();
    this.props.getMissingAccessScopes();
    this.props.getOnboardingInfo();
    this.checkAppEmbeded();
    this.getWhiteListedStores();
    this.props.getProfile();
    this.props.getThemeSettings(-1);
    // this.props.getShopInfo(1);
    this.props.getPaymentPlanByShop();
  }

  state = {
    modal: true
  };

  getWhiteListedStores() {
    axios.get('api/miscellaneous/get-hard-white-listed-shop-names').then(res => {
      this.setState({...this.state, whiteListedStores: res.data})
    })
  }

  checkAppEmbeded() {
    axios.get('api/asset-key?assetKey=config/settings_data.json').then(res => {
      if (JSON.stringify(res.data).indexOf('appstle-subscription') === -1) {
        // setTimeout(this.checkAppEmbeded.bind(this), 3000);
        this.props.setAppEmbed(false);
      } else {
        Object.keys(res.data.current.blocks).forEach((key, index) => {
          if (res.data.current.blocks[key]?.['type'].indexOf('appstle-subscription') !== -1) {
            if (!res.data.current.blocks[key]?.['disabled']) {
              this.props.setAppEmbed(true);
            } else {
              // setTimeout(this.checkAppEmbeded.bind(this), 3000);
              this.props.setAppEmbed(false);
            }
          }
        });
      }
    });
  }

  render() {
    const {
      match,
      location,
      account,
      shopPaymentInfoEntity,
      paymentPlanLimit,
      paymentPlanLimitLoaded,
      themeSettingsEntity,
      themeLoading,
      convertedTestShopLoading,
      themeUpdating,
      convertedTestShop,
      shopInfo,
      shopInfoLoading,
      shopInfoUpdating,
      updateShopInfoEntity,
      isAppEmbed,
      missingAccessScopes
    } = this.props;

    if (!shopInfoLoading && !shopInfoUpdating && shopInfo.onboardingSeen === false && shopInfo.shop != null) {
      // Make sure redirected with the state property "onboarding"
      if (!location?.state?.onboarding) {
        return <Redirect to="/onboarding" />;
      }

      updateShopInfoEntity({
        ...shopInfo,
        onboardingSeen: true,
      });
    }

    let totalQuotaPercentUsed = 0;
    if (paymentPlanLimit.activeSubscriptionCount != null && paymentPlanLimit.planLimit != null) {
      totalQuotaPercentUsed = ((paymentPlanLimit.activeSubscriptionCount / paymentPlanLimit.planLimit) * 100).toFixed(0);
    }

    if (
      paymentPlanLimit?.planInfo?.basedOn == BasedOn.SUBSCRIPTION_ORDER_AMOUNT &&
      paymentPlanLimit.usedOrderAmount != null &&
      paymentPlanLimit.orderAmountLimit != null
    ) {
      totalQuotaPercentUsed = ((paymentPlanLimit.usedOrderAmount / paymentPlanLimit.orderAmountLimit) * 100).toFixed(0);
    }

    var createdDate = new Date(account.createdDate);
    var days = 14 - Math.floor(differenceInDays(createdDate));
    var trialExpired = paymentPlanLimit?.trialEndsOn ? Math.floor(differenceInDays(paymentPlanLimit?.trialEndsOn)) < -110 : false;

    let themeSettingsComponent = null;
    let firstTimeExperience = null;
    let appEmbeddedPopUpComponent = null;

    if (
      shopInfo &&
      !shopInfo.onboardingSeen &&
      !isAppEmbed &&
      themeLoading === false &&
      themeSettingsEntity !== null &&
      themeSettingsEntity.shopifyThemeInstallationVersion === ShopifyThemeInstallationVersion.V2
    ) {
      appEmbeddedPopUpComponent = <AppEmbeddedPopUp />;
    } else if (
      themeLoading === false &&
      themeSettingsEntity !== null &&
      ((themeSettingsEntity.themeName === 'floating-button' &&
          themeSettingsEntity.shopifyThemeInstallationVersion === ShopifyThemeInstallationVersion.V1) ||
        (themeSettingsEntity.shopifyThemeInstallationVersion === ShopifyThemeInstallationVersion.V2 &&
          !themeSettingsEntity.themeV2Saved)) &&
      themeSettingsEntity.skip_setting_theme === false
    ) {
      // themeSettingsComponent = (
      //   <ThemePickerPopUp
      //     themeUpdating={themeUpdating}
      //     onSave={themeName => this.props.updateThemeName(themeName)}
      //     onSkip={setting => this.props.updateThemeSkipSetting(setting)}
      //   />
      // );
      themeSettingsComponent = null;
    } else {
      firstTimeExperience = null;
    }

    const handleToggle = () => {
      this.setState({
        modal: !this.state.modal
      })
    }
    return (
      <Fragment>
        {/* <ThemeOptions/> */}
        <AppHeader/>
        {convertedTestShopLoading ? (
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-pulse-rise"/>
              </div>
              <h6 className="mt-5">
                Please wait while we configure your plan
                {/* <small>
                    Because this is a demonstration we load at once all the Components examples. This wouldn't happen in a real live app!
                  </small> */}
              </h6>
            </div>
          </div>
        ) : !convertedTestShop ||
        totalQuotaPercentUsed > 100 ||
        (paymentPlanLimitLoaded === true && paymentPlanLimit.planInfo === null) ||
        trialExpired ? (
          <PlanInfoList trialExpired={trialExpired} convertedTestShop={convertedTestShop} />
        ) : (
          <div className={'app-main'}>
            {(this?.state?.whiteListedStores?.indexOf(shopInfo?.shop) === -1) && appEmbeddedPopUpComponent}
            {themeSettingsComponent}
            {firstTimeExperience}
            <AppSidebar />
            <div className="app-main__outer">
              {days < 12 ? (
                <div className="text-center">
                  {/*<Card color={days < 1 ? 'danger' : days < 4 ? 'warning' : 'info'}>
                  <CardBody>
                    <b style={{ color: 'white' }}>
                      You have {days} {days === 1 ? ' day' : ' days'} left in your trial period
                    </b>
                  </CardBody>
                </Card>*/}
                </div>
              ) : null}

              <PaymentUpgradeModal/>

              {paymentPlanLimit.orderAmountLimit && (((paymentPlanLimit.usedOrderAmount / paymentPlanLimit.orderAmountLimit) * 100).toFixed(0) > 90) ? (
                <Alert className="mb-0" color="danger">
                  <h6>
                    <b>Important Note:</b>
                  </h6>
                  <p>
                    You monthly subscription quota will be exhausted soon. To enjoy uninterrupted experience,
                    please{' '}
                    <Link to="/dashboards/billing">upgrade here.</Link>
                  </p>
                </Alert>
              ) : (
                ''
              )}

              {missingAccessScopes && missingAccessScopes.length > 0 && (
                <Alert className="mb-0" color="warning">
                  <h6>
                    <b>Important Note:</b>
                  </h6>
                  <p>
                    We have added some new features in our app related to location based inventory check. To use app to its full potential, app will need access to few more permisions.
                    Please{' '}
                    <EmbeddedExternalLink href={`https://subscription-admin.appstle.com/oauth/subscription/authenticate?shop=${shopInfo.shop}`}>grant permissions here.</EmbeddedExternalLink>
                  </p>
                </Alert>
              )}

              {themeSettingsEntity && themeSettingsEntity.shopifyThemeInstallationVersion === ShopifyThemeInstallationVersion.V1 && (
                <Alert className="mb-0" color="danger">
                  <h6>
                    <b>Urgent Action Required:</b>
                  </h6>
                  <Row>
                    <Col md={9}>
                    <div>
                        You shop is using our old deprecated version of script which is discontinued now. Please update to our latest version of script which is Shopify theme 2.0 compatible.
                      </div>
                      <div>
                        After updating, please enable our app extension from your theme customisation.
                      </div>
                      <div>
                        After updating the version, please check if the subscription widget is working properly on the product page.
                      </div>
                      <div>
                        If you still face any issues after updating to the latest version, please reach out to our support team.
                      </div>
                    </Col>
                    <Col md={3}>
                      <MySaveButton
                        className="float-right"
                        updating={themeUpdating}
                        onClick ={()=> this.props.updateToV2()}
                        updatingText="Updating"
                        text="Update to latest version"
                      ></MySaveButton>
                    </Col>
                  </Row>

                </Alert>
              )}




              <div className="app-main__inner">
                {/* Customers Route */}
                <Route path={`${match.url}/customers`} component={CustomersList} exact/>
                <Route path={`${match.url}/customers/:id/edit`} component={CustomerDetail} exact/>

                {/* Subscription Contracts Route*/}
                <Route path={`${match.url}/subscriptions`} component={SubscriptionList} exact/>
                <Route path={`${match.url}/subscriptions/create-subscription`} component={CreateSubscription} exact/>
                <Route path={`${match.url}/subscription/:id/detail`} component={SubscriptionDetail} exact/>
                {/* Selling Plan Route */}
                <Route path={`${match.url}/advanced-selling-plans`} component={SellingPlanMemberInfoList} exact/>
                <Route
                  path={`${match.url}/selling-plans/:subscriptionId/:sellingPlanId/edit`}
                  component={ManageSellingPlanMemberInfo}
                  exact
                />

                <Route path={`${match.url}/events`} component={EventData} exact/>

                {/* Subscription Bundling Route */}
                <Route path={`${match.url}/subscription-bundling`} component={SubscriptionBundlingList} exact/>
                <Route path={`${match.url}/subscription-bundling/:id/edit`} component={ManageSubscriptionBundling} exact/>
                <Route path={`${match.url}/subscription-bundling/:cloneId/clone`} component={ManageSubscriptionBundling} exact/>
                <Route path={`${match.url}/subscription-bundling/new`} component={ManageSubscriptionBundling} exact/>
                {/* Subscription Plans Route */}
                <Route path={`${match.url}/subscription-plan`} component={SubscriptionGroupList} exact/>
                <Route path={`${match.url}/subscription-plan/:id/edit`} component={CreateSubscriptionGroup} exact/>
                <Route path={`${match.url}/subscription-plan/new`} component={CreateSubscriptionGroup} exact/>
                <Route path={`${match.url}/subscription-plan/new/:duplicateid`} component={CreateSubscriptionGroup}
                       exact/>
                <Route path={`${match.url}/loyalty-plan/:id/edit`} component={CreateSubscriptionLoyalty} exact/>

                <Route path={`${match.url}/subscription-group/new`} component={CreateSubscriptionGroup} exact/>

                <Route path={`${match.url}/product-page-widget`} component={ProductPageWidget} exact/>
                <Route path={`${match.url}/cart-page-widget`} component={CartPageWidget} exact/>
                <Route path={`${match.url}/home-collection-page-widget`} component={HomeAndCollectionPageWidget} exact/>

                <Route path={`${match.url}/widget-placement`} component={WidgetPlacement} exact/>
                <Route path={`${match.url}/migration`} component={MigrationPage} exact/>

                <Route path={`${match.url}/widget-types`} component={WidgetTypes} exact/>
                <Route path={`${match.url}/customer-portal-css`} component={CustomerPortalCSS} exact/>
                <Route path={`${match.url}/customize-customer-portal`} component={CustomerPortalSetting} exact/>
                <Route path={`${match.url}/customize-customer-portal-migrate`} component={CustomerPortalSettingMigrate}
                       exact/>
                <Route path={`${match.url}/subscription-bundle`} component={SubscriptionBundleSetting} exact/>
                <Route path={`${match.url}/customize-bundle-css`} component={CustomBundleCSS} exact/>
                <Route path={`${match.url}/customize-cart-widget`} component={CartWidgetSettings} exact/>

                <Route path={`${match.url}/customize-shop`} component={ShopSettings} exact/>
                <Route path={`${match.url}/api`} component={API} exact/>

                <Route path={`${match.url}/customize-theme-integration`} component={CustomizeThemeIntegration} exact/>
                <Route path={`${match.url}/webhook-integration`} component={WebhookIntegration} exact/>

                <Route path={`${match.url}/integrations`} component={Integrations} exact/>
                <Route path={`${match.url}/settings`} component={Setting}/>

                <Route path={`${match.url}/billing`} component={PaymentPlan} exact/>
                <Route path={`${match.url}/analytics`} component={Analytics}/>
                <Route path={`${match.url}/feedback`} component={Feedback}/>

                {/* Membership Discount Route */}
                <Route path={`${match.url}/membership-discount`} component={MembershipDiscountList} exact/>
                <Route path={`${match.url}/membership-discount/:id/edit`} component={MembershipDiscountManage} exact/>
                <Route path={`${match.url}/membership-discount/new`} component={MembershipDiscountManage} exact/>

                {/* { Email template settings Route } */}
                <Route path={`${match.url}/email-templates`} component={EmailTemplates} exact/>
                <Route path={`${match.url}/email-templates/:id/edit`} component={EmailTemplateDetails} exact/>

                <Route path={`${match.url}/bulk-emails`} component={BulkEmails} exact/>
                <Route path={`${match.url}/bulk-emails/:id/edit`} component={BulkEmailDetails} exact/>

                {/* { Sms template settings Route } */}
                <Route path={`${match.url}/sms-settings`} component={SmsTemplateList} exact/>
                <Route path={`${match.url}/sms-settings/:id/edit`} component={SmsTemplateSetting} exact/>

                {/* { Dunninig  Management settings Route } */}
                <Route path={`${match.url}/dunning-management`} component={DunningManagement} exact/>
                {/* <Route path={`${match.url}/dunning-management/:id/edit`} component={EmailTemplateDetails} exact /> */}

                <Route path={`${match.url}/shop-setting`} component={ShopSetting} exact/>

                <Route path={`${match.url}/shipping-profile`} component={ShippingProfileList} exact/>
                <Route path={`${match.url}/shipping-profile/:id/edit`} component={CreateShippingprofile} exact/>
                <Route path={`${match.url}/shipping-profile/new`} component={CreateShippingprofile} exact/>
                <Route path={`${match.url}/shipping-profile/:id/details`} component={ViewShippingProfile} exact/>


                <Route path={`${match.url}/shipping-profile-v2/:id/edit`} component={CreateShippingProfileV2} exact/>
                <Route path={`${match.url}/shipping-profile-v2/new`} component={CreateShippingProfileV2} exact/>
                <Route path={`${match.url}/shipping-profile-v2/:id/details`} component={ViewShippingProfileV2} exact/>

                <Route path={`${match.url}/swap-product`} component={SwapProductList} exact/>
                <Route path={`${match.url}/summary-reports`} component={ActivityUpdatesSummaryReports} exact/>

                <Route path={`${match.url}/swap-product/new`} component={SwapProduct} exact/>
                <Route path={`${match.url}/swap-product/:id/edit`} component={SwapProduct} exact/>

                <Route path={`${match.url}/email-domain-settings`} component={EmailDomainSettings} exact/>

                <Route path={`${match.url}/cancellation-management`} component={CancellationManagement} exact/>

                <Route path={`${match.url}/tutorials`} component={Tutorials} exact/>

                <Route path={`${match.url}/report`} component={Reports} exact/>

                <Route path={`${match.url}/recommended-apps`} component={Recommendedapps} exact/>
                <Route path={`${match.url}/subscription-automation`} component={SubscriptionAutomation} exact/>

                <Route path={`${match.url}/activity-log/:id`} component={SubscriptionActivityList} exact/>
                <Route path={`${match.url}/quick-actions`} component={QuickActions} exact/>
                <Route path={`${match.url}/quick-checkout`} component={QuickCheckout} exact/>


                <Route path={`${match.url}/bundles`} component={BundlesList} exact/>
                <Route path={`${match.url}/bundles/add`} component={UpdateBundles} exact/>
                <Route path={`${match.url}/bundles/:id/edit`} component={UpdateBundles} exact/>
                <Route path={`${match.url}/bundles/:id/delete`} component={DeleteBundleRule} exact/>


                <Route path={`${match.url}/bundle-settings`} component={BundleSettings} exact/>

                <Route path={`${match.url}/appstle-menu-configure`} component={BuildAppstleMenu} exact/>
                <Route path={`${match.url}/appstle-menu-labels-settings`} component={AppstleMenu} exact/>
                <Route path={`${match.url}/bulk-automations-activity`} component={BulkAutomationsActivityLog} exact/>
                <Route path={`${match.url}/faq`} component={FAQPage} exact />
              </div>
              <AppFooter/>
            </div>
          </div>
        )}
      </Fragment>
    );
  }
}

const mapStateToProp = state => ({
  shopPaymentInfoEntity: state.shopPaymentInfo.entities,
  account: state.authentication.account,
  paymentPlanLimit: state.paymentPlan.paymentPlanLimit,
  paymentPlanLimitLoaded: state.paymentPlan.paymentPlanLimitLoaded,
  themeSettingsEntity: state.themeSettings.entity,
  themeLoading: state.themeSettings.loading,
  themeUpdating: state.themeSettings.updating,
  convertedTestShop: state.paymentPlan.convertedTestShop,
  convertedTestShopLoading: state.paymentPlan.fetchTestChargeLoading,
  shopInfo: state.shopInfo.entity,
  shopInfoLoading: state.shopInfo.loading,
  shopInfoUpdating: state.shopInfo.updating,
  isAppEmbed: state.shopInfo.isAppEmbed,
  missingAccessScopes: state.shopInfo.missingAccessScopes
});

const mapDispatchToProps = {
  getSession,
  getPaymentPlanLimitInformation,
  getEntities,
  getThemeSettings,
  updateThemeName,
  updateThemeSkipSetting,
  updateToV2,
  getTestChargeInformation,
  getShopInfoEntity,
  updateShopInfoEntity,
  getShopifyShopInfo,
  setAppEmbed,
  getMissingAccessScopes,
  getOnboardingInfo,
  getProfile,
  getShopInfo,
  getPaymentPlanByShop,
  getOnboardingInfo
};

export default withRouter(withScreenSize(connect(mapStateToProp, mapDispatchToProps)(Dashboards)));
