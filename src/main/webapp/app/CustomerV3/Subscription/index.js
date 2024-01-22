import React from 'react';
import { connect } from 'react-redux';
import { Route, withRouter } from 'react-router-dom';

import SubscriptionDetails from './SubscriptionDetails';
import SubscriptionsList from 'app/CustomerV3/Subscription/SubscriptionsList';
import {
  getEntity as getEntityForCustomerPortal
} from 'app/entities/subscription-custom-css/subscription-custom-css.reducer';

import shopCustomization, {
  getShopCustomizationCSS as getShopCustomizationCSS
} from 'app/entities/shop-customization/shop-customization.reducer';

import { getCustomerPortalSettingEntity } from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import './appstle-subscription.scss';
import { getShopInfoByCurrentLogin } from "app/entities/shop-info/shop-info.reducer";

class Subscription extends React.Component {
  componentDidMount() {
    this.props.getEntityForCustomerPortal(0);
    this.props.getCustomerPortalSettingEntity(0);
    this.props.getShopInfoByCurrentLogin();
    this.props.getShopCustomizationCSS("CUSTOMER_PORTAL");
  }

  render() {
    const { match } = this.props;
    // document.querySelector("body").classList.add("appstle-customer-portal")

    return (
      <div className="as-bg-gray-100 as-cp-wrapper">
        <div
          className={`app-main appstle-customer-portal ${this?.props?.customerPortalSettingEntity?.enableTabletForceView &&
            'force-tablet-view'} as-container as-mx-auto as-px-4 as-py-10`}
        >
          {(this.props?.shopCustomizationCSS && this.props.shopCustomizationCSS.length > 0) &&
            <style>
              {this.props?.shopCustomizationCSS?.map(css =>
                css?.replaceAll('\n', '').replaceAll('"', '')
              )}
            </style>
          }
          <style>{this.props?.subCustomCSSEntity?.customerPoratlCSS?.replaceAll('\n', '').replaceAll('"', '')}</style>
          <div className="app-main__outer">
            <div className="app-main__inner">
              <Route path={`${match.url}/list`} component={SubscriptionsList} />
              <Route path={`${match.url}/:id/detail`} component={SubscriptionDetails} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProp = state => ({
  subCustomCSSEntity: state.subscriptionCustomCss.entity,
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  shopCustomizationCSS: state.shopCustomization.shopCustomizationCss,
});

const mapDispatchToProps = {
  getEntityForCustomerPortal,
  getCustomerPortalSettingEntity,
  getShopInfoByCurrentLogin,
  getShopCustomizationCSS
};

export default withRouter(connect(mapStateToProp, mapDispatchToProps)(Subscription));
