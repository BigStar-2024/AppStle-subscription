import React, {Fragment} from 'react';
import {connect} from 'react-redux';
import {Redirect, Route, withRouter} from 'react-router-dom';
import AppFooter from 'app/DemoPages/AppFooter/';
import {Alert, Row, Col} from 'reactstrap';
import withScreenSize from 'app/shared/util/withScreenSize';
// Layout
import AppHeader from '../../Layout/AppHeader/';
import AppSidebar from '../../Layout/AppSidebar/';
import {
  updateToV2
} from 'app/entities/theme-settings/theme-settings.reducer';
import {ShopifyThemeInstallationVersion} from 'app/shared/model/enumerations/shopify-theme-installation-version.model';
import Dashboard from 'app/DemoPages/Dashboards/Dashboard/Dashboard';
import axios from 'axios';
import { updateEntity as updateShopInfoEntity, setAppEmbed } from 'app/entities/shop-info/shop-info.reducer';
import { getEntity as getOnboardingInfo } from 'app/entities/onboarding-info/onboarding-info.reducer'
import MySaveButton from './Utilities/MySaveButton';


class MainDashobard extends React.Component {
  componentDidMount() {
    // this.props.getOnboardingInfo();
    // this.checkAppEmbeded();
  }

  state = {
    modal: true
  };

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
      shopInfo,
      shopInfoLoading,
      shopInfoUpdating,
      updateShopInfoEntity,
      isAppEmbed,
      themeSettingsEntity,
      themeUpdating
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

    return (
      <Fragment>
        {/* <ThemeOptions/> */}
        <AppHeader/>
        <div className={'app-main'}>

          <AppSidebar />
          <div className="app-main__outer">

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
              <Route path={`${match.url}/dashboard`} component={Dashboard} exact/>
            </div>
            <AppFooter/>
          </div>
        </div>
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
  updateShopInfoEntity,
  setAppEmbed,
  getOnboardingInfo,
  updateToV2
};

export default withRouter(withScreenSize(connect(mapStateToProp, mapDispatchToProps)(MainDashobard)));
