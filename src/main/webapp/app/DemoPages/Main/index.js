import cx from 'classnames';
import React, {Fragment} from 'react';
import {connect} from 'react-redux';
import ResizeDetector from 'react-resize-detector';
import {withRouter} from 'react-router-dom';
import {AUTHORITIES} from '../../config/constants';
import AppMain from '../../Layout/AppMain';
import {hasAnyAuthority} from '../../shared/auth/private-route';
import {getProfile} from '../../shared/reducers/application-profile';
import {getSession} from '../../shared/reducers/authentication';
import {getEntity} from 'app/entities/shop-info/shop-info.reducer';
import {
  getEntity as getThemeSettings,
  updateThemeName,
  updateThemeSkipSetting
} from 'app/entities/theme-settings/theme-settings.reducer';
import {getPaymentPlanByShop} from 'app/entities/payment-plan/payment-plan.reducer';
import {differenceInDays} from 'app/shared/util/date-utils';


class Main extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      closedSmallerSidebar: false
    };
  }

  componentDidMount() {
    this.props.getSession();
    // this.props.getProfile();
    this.props.getThemeSettings(-1);
    this.props.getEntity(1);
    // this.props.paymentPlanByShop();
  }

  componentWillReceiveProps = newProps => {
    const {isAuthenticated} = this.props;
    if (!isAuthenticated && newProps.isAuthenticated) {
    }
    if (newProps.isIndexing) {
    }
  };

  updateSettingEntity = entity => {
    if (entity.hasOwnProperty('enableInstantSearchWidget')) {
    } else if (entity.hasOwnProperty('enableSearchResultsWidget')) {
    }
  };

  render() {
    const {
      colorScheme,
      enableFixedHeader,
      enableFixedSidebar,
      enableFixedFooter,
      enableClosedSidebar,
      closedSmallerSidebar,
      enableMobileMenu,
      enablePageTabsAlt,
      account,
      paymentPlanLimit,
      paymentPlanLimitLoaded,
      paymentPlanEntity,
      themeSettingsEntity,
      themeLoading,
      themeUpdating,
      shopInfo,
      shopPaymentInfoEntity
    } = this.props;

    const user = {
      shop: account.login,
      user_id: account.login,
      external_id: account.login,
      email: account.email,
      dynamoPassword: account.dynamodbPassword,
      subscription_app_status: 'Installed'
    };

    if (shopInfo != null) {
      user.shopifyPlanName = shopInfo.shopifyPlanName;
      user.shopifyPlanDisplayName = shopInfo.shopifyPlanDisplayName;
    }

    if (
      themeLoading === false &&
      themeSettingsEntity !== null &&
      (themeSettingsEntity.themeName !== 'floating-button' && themeSettingsEntity.skip_setting_theme === false && paymentPlanEntity?.planInfo)
    ) {
      user.theme_detected = true;
    }

    if (shopPaymentInfoEntity && shopPaymentInfoEntity.paymentSettings && !shopPaymentInfoEntity?.paymentSettings?.supportedDigitalWallets?.includes("SHOPIFY_PAY")) {
      user.shopify_payments_enabled = false;
    } else {
      user.shopify_payments_enabled = true;
    }

    if (paymentPlanLimit != null && paymentPlanLimit.planInfo != null && paymentPlanLimit.planInfo.name != null) {
      user.appstle_plan_name = paymentPlanLimit.planInfo.name;

      if (paymentPlanLimit.usedOrderAmount != null) {
        user.user_order_amount = paymentPlanLimit.usedOrderAmount;
      }

      if (paymentPlanLimit.orderAmountLimit != null) {
        user.order_amount_limit = paymentPlanLimit.orderAmountLimit;
      }
    }

    window.APPSTLE_USER = user;

    var createdDate = new Date(account.createdDate);
    var days = differenceInDays(createdDate);

    /*if (paymentPlanLimitLoaded === true && paymentPlanLimit.planInfo === null) {
      return (
        <>
          <PlanInfoList/>
        </>
      );
    }*/


    return (
      <ResizeDetector
        handleWidth
        render={({width}) => (
          <Fragment>
            <div
              className={cx(
                `app-container app-theme-${colorScheme}`,
                {'fixed-header': enableFixedHeader},
                {'fixed-sidebar': enableFixedSidebar || width < 1250},
                {'fixed-footer': enableFixedFooter},
                {'closed-sidebar': enableClosedSidebar || width < 1250},
                {'closed-sidebar-mobile': closedSmallerSidebar || width < 1250},
                {'sidebar-mobile-open': enableMobileMenu},
                {'body-tabs-shadow-btn': enablePageTabsAlt}
              )}
            >
              <AppMain/>
            </div>
            {/*<Intercom appID="x4xb7xfy" {...user} />*/}
          </Fragment>
        )}
      />
    );
  }
}

const mapStateToProp = function (state) {

  return {
    colorScheme: state.ThemeOptions.colorScheme,
    enableFixedHeader: state.ThemeOptions.enableFixedHeader,
    enableMobileMenu: state.ThemeOptions.enableMobileMenu,
    enableFixedFooter: state.ThemeOptions.enableFixedFooter,
    enableFixedSidebar: state.ThemeOptions.enableFixedSidebar,
    enableClosedSidebar: state.ThemeOptions.enableClosedSidebar,
    enablePageTabsAlt: state.ThemeOptions.enablePageTabsAlt,
    isAuthenticated: state.authentication.isAuthenticated,
    isAdmin: hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]),
    ribbonEnv: state.applicationProfile.ribbonEnv,
    isInProduction: state.applicationProfile.inProduction,
    isSwaggerEnabled: state.applicationProfile.isSwaggerEnabled,
    account: state.authentication.account,
    shopUrl: state.shopInfo.entity.publicDomain,
    paymentPlanEntity: state.paymentPlan.entity,
    paymentPlanLoading: state.paymentPlan.loading,
    paymentPlanUpdating: state.paymentPlan.updating,
    paymentPlanLimit: state.paymentPlan.paymentPlanLimit,
    paymentPlanLimitLoaded: state.paymentPlan.paymentPlanLimitLoaded,
    shopifyShopInfo: state.shopifyShopInfo,
    loadedShopInfo: state.shopInfo.loadedShopInfo,
    shopInfo: state.shopInfo.entity,
    themeSettingsEntity: state.themeSettings.entity,
    themeLoading: state.themeSettings.loading,
    themeUpdating: state.themeSettings.updating,
    shopPaymentInfoEntity: state.shopPaymentInfo.entities,
  };
};

const mapDispatchToProps = {
  getSession,
  getProfile,
  getEntity,
  paymentPlanByShop: getPaymentPlanByShop,
  getThemeSettings,
  updateThemeName,
  updateThemeSkipSetting,
};

export default withRouter(connect(mapStateToProp, mapDispatchToProps)(Main));
