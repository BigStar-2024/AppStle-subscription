import React, {Fragment} from 'react';
import {connect} from 'react-redux';
import {Route, withRouter} from 'react-router-dom';

import SubscriptionDetails from './SubscriptionDetails';
import SubscriptionsList from "app/Customer/Subscription/SubscriptionsList";
import {
  getEntity as getEntityForCustomerPortal
} from "app/entities/subscription-custom-css/subscription-custom-css.reducer";
import {
  getShopInfoEntity
} from "app/entities/shop-info/shop-info.reducer";


class Subscription extends React.Component {
  componentDidMount() {
    this.props.getEntityForCustomerPortal(0);
  }


  render() {
    const {match} = this.props;
    // document.querySelector("body").classList.add("appstle-customer-portal")

    return (
      <Fragment>
        <div className="app-main appstle-customer-portal">
          <style>
            {this.props?.subCustomCSSEntity?.customerPoratlCSS?.replaceAll("\n", "").replaceAll('"', '')}
          </style>
          <div className="app-main__outer">
            <div className="app-main__inner">
              <Route path={`${match.url}/list`} component={SubscriptionsList}  />
              <Route path={`${match.url}/:id/detail`} component={SubscriptionDetails} />
            </div>
          </div>
        </div>
      </Fragment>
    );
  }
}

const mapStateToProp = state => ({
  subCustomCSSEntity: state.subscriptionCustomCss.entity
});

const mapDispatchToProps = {
  getEntityForCustomerPortal
};

export default withRouter(connect(mapStateToProp, mapDispatchToProps)(Subscription));
