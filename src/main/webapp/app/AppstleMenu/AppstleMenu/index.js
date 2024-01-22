import React, {Fragment, useEffect} from 'react';
import {connect} from 'react-redux';
import {getShopInfoEntity} from 'app/entities/shop-info/shop-info.reducer';

import Header from './header/header';
import ProductPage from './pages/productPage/productPage';
import BundlePage from './pages/bundles/bundlePage';
import {useMainContext} from 'app/AppstleMenu/context';

const Dashboards = props => {
  const {match} = props;
  const {selectedFilterMenu, appstleMenuLabels} = useMainContext();

  useEffect(() => {
  }, []);
  return (
    <Fragment>
      <div className="app-main">
        <style>
          {appstleMenuLabels?.customCss?.replaceAll("\n", "").replaceAll('"', '')}
        </style>
        <div className="app-main__outer">
          <Header/>
          <div>
            {selectedFilterMenu != null && selectedFilterMenu.menuType === 'ONE_TIME' ? (
              <ProductPage/>
            ) : selectedFilterMenu != null && selectedFilterMenu.menuType === 'SUBSCRIBE' ? (
              <ProductPage/>
            ) : selectedFilterMenu != null && selectedFilterMenu.menuType === 'BUNDLE' ? (
              <BundlePage/>
            ) : (
              <ProductPage/>
            )}
          </div>
        </div>
      </div>
    </Fragment>
  );
};

const mapStateToProp = state => ({
  subCustomCSSEntity: state.subscriptionCustomCss.entity,
  shopInfoEntity: state.shopInfo.entity
});

const mapDispatchToProps = {
  getShopInfoEntity
};

export default connect(mapStateToProp, mapDispatchToProps)(Dashboards);
