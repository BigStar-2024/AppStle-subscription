import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { Route, withRouter } from 'react-router-dom';

import Bundles from "app/BundlesV2/Bundles/Bundles";
import {
  getEntity as getEntityForCustomerPortal
} from "app/entities/subscription-custom-css/subscription-custom-css.reducer";
import {
  getShopInfoEntity
} from "app/entities/shop-info/shop-info.reducer";

import shopCustomization, {
  getShopCustomizationCSS as getShopCustomizationCSS
} from 'app/entities/shop-customization/shop-customization.reducer';

class Dashboards extends React.Component {
  async componentDidMount() {
    // await this.props.getShopInfoEntity(Shopify?.shop);
    this.props.getEntityForCustomerPortal(0);
    this.props.getShopCustomizationCSS("BUILD_A_BOX");
    // document.querySelector('body').classList.add("appstle-customer-portal")
  }

  render() {
    const { match } = this.props;


    return (
      <Fragment>
        <div className="app-main">
          {(this.props?.shopCustomizationCSS && this.props.shopCustomizationCSS.length > 0) &&
            <style>
              {this.props?.shopCustomizationCSS?.map(css =>
                css?.replaceAll('\n', '').replaceAll('"', '')
              )}
            </style>
          }
          <style>
            {this.props?.subCustomCSSEntity?.bundlingCSS?.replaceAll("\n", "").replaceAll('"', '')}
          </style>
          <div className="app-main__outer">
            <div>
              <Route path={`${match.url}`} component={Bundles} exact />
            </div>
          </div>
        </div>
      </Fragment>
    );
  }
}

const mapStateToProp = state => ({
  subCustomCSSEntity: state.subscriptionCustomCss.entity,
  shopInfoEntity: state.shopInfo.entity,
  shopCustomizationCSS: state.shopCustomization.shopCustomizationCss,
});

const mapDispatchToProps = {
  getEntityForCustomerPortal,
  getShopInfoEntity,
  getShopCustomizationCSS
};

export default withRouter(connect(mapStateToProp, mapDispatchToProps)(Dashboards));
