import cx from 'classnames';
import React, {Fragment} from 'react';
import {connect} from 'react-redux';
import ResizeDetector from 'react-resize-detector';
import {withRouter} from 'react-router-dom';
import {AUTHORITIES} from '../../../config/constants';
import {hasAnyAuthority} from '../../../shared/auth/private-route';
import {getProfile} from '../../../shared/reducers/application-profile';
import {getSession} from '../../../shared/reducers/authentication';

import {getPaymentPlanByShop, getPaymentPlanLimitInformation} from 'app/entities/payment-plan/payment-plan.reducer';
import PlanInfoList from 'app/DemoPages/Dashboards/PaymentPlan/PlanInfoList';
import {getShopifyShopInfo} from '../../../entities/shopify-shop-info/shopify-shop-info.reducer';

class PaymentPlan extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      closedSmallerSidebar: false
    };
  }

  componentDidMount() {
    this.props.getSession();
    this.props.getProfile();
    this.props.paymentPlanByShop();
    this.props.getPaymentPlanLimitInformation();
  }

  componentWillReceiveProps = newProps => {
    const {isAuthenticated} = this.props;
    if (!isAuthenticated && newProps.isAuthenticated) {
    }
    if (newProps.isIndexing) {
      setTimeout(interval => {
      }, 20000);
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
      paymentPlanEntity,
      paymentPlanLoading,
      shopifyShopInfo
    } = this.props;


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
              <PlanInfoList convertedTestShop={this.props.convertedTestShop}/>
            </div>
          </Fragment>
        )}
      />
    );
  }
}

const mapStateToProp = function (state) {
  console.log('state.shopInfo.entity.publicDomain=' + state.shopInfo.entity.publicDomain);

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
    themes: state.shopInfo.themes,
    paymentPlanEntity: state.paymentPlan.entity,
    paymentPlanLoading: state.paymentPlan.loading,
    paymentPlanUpdating: state.paymentPlan.updating,
    loadedShopInfo: state.shopInfo.loadedShopInfo,
    shopInfo: state.shopInfo.entity,
    convertedTestShop: state.paymentPlan.convertedTestShop,

  };
};

const mapDispatchToProps = {
  getSession,
  getProfile,
  getPaymentPlanLimitInformation,
  paymentPlanByShop: getPaymentPlanByShop,
  getShopifyShopInfo
};

export default withRouter(connect(mapStateToProp, mapDispatchToProps)(PaymentPlan));
