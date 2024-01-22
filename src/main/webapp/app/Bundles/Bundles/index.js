import React, {Fragment} from 'react';
import {connect} from 'react-redux';
import {Route, withRouter} from 'react-router-dom';

import Bundles from "app/Bundles/Bundles/Bundles";
import {
  getEntity as getEntityForCustomerPortal
} from "app/entities/subscription-custom-css/subscription-custom-css.reducer";
import {
  getShopInfoEntity
} from "app/entities/shop-info/shop-info.reducer";

class Dashboards extends React.Component {
  async componentDidMount() {
    // await this.props.getShopInfoEntity(Shopify?.shop);
    this.props.getEntityForCustomerPortal(0);
    document.querySelector('body').classList.add("appstle-customer-portal")
  }

  render() {
    const {match} = this.props;


    return (
      <Fragment>
        <div className="app-main">
          <style>
            {this.props?.subCustomCSSEntity?.bundlingCSS?.replaceAll("\n", "").replaceAll('"', '')}
          </style>
          <div className="app-main__outer">
            <div>
              <Route path={`${match.url}`} component={Bundles} exact/>
            </div>
          </div>
        </div>
      </Fragment>
    );
  }
}

const mapStateToProp = state => ({
  subCustomCSSEntity: state.subscriptionCustomCss.entity,
  shopInfoEntity: state.shopInfo.entity
});

const mapDispatchToProps = {
  getEntityForCustomerPortal,
  getShopInfoEntity
};

export default withRouter(connect(mapStateToProp, mapDispatchToProps)(Dashboards));
