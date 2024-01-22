import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import cx from 'classnames';

import ReactCSSTransitionGroup from 'react-addons-css-transition-group';

import PerfectScrollbar from 'react-perfect-scrollbar';
import Nav from '../AppNav/VerticalNavWrapper';
import HeaderLogo from '../AppLogo';
import withScreenSize from "app/shared/util/withScreenSize"

import { setEnableMobileMenu } from '../../reducers/ThemeOptions';

class AppSidebar extends Component {
  state = {};

  toggleMobileSidebar = () => {
    const { enableMobileMenu, setEnableMobileMenu } = this.props;
    setEnableMobileMenu(!enableMobileMenu);
  };

  render() {
    const { backgroundColor, enableBackgroundImage, enableSidebarShadow, backgroundImage, backgroundImageOpacity } = this.props;
    const size = this.props.size;
    const isInsideShopify = ((window.location.href.includes("myshopify.com") || (window?.location?.ancestorOrigins?.[0] ? window?.location?.ancestorOrigins[0]?.includes("myshopify.com") : false)) && size.width >= 993);

    return (
      <Fragment>
        <div className="sidebar-mobile-overlay" onClick={this.toggleMobileSidebar} />
        <ReactCSSTransitionGroup
          component="div"
          className={cx('app-sidebar', backgroundColor, { 'sidebar-shadow': enableSidebarShadow })}
          transitionName="SidebarAnimation"
          transitionAppear
          transitionAppearTimeout={1500}
          transitionEnter={false}
          transitionLeave={false}
          style={(isInsideShopify) ? {height: "108vh"} : {}}
        >
          <HeaderLogo />
          <PerfectScrollbar>
            <div className="app-sidebar__inner">
              <Nav account={this.props.account} />
            </div>
          </PerfectScrollbar>
          <div
            className={cx('app-sidebar-bg', backgroundImageOpacity)}
            style={{
              backgroundImage: enableBackgroundImage ? `url(${backgroundImage})` : null
            }}
          />
        </ReactCSSTransitionGroup>
      </Fragment>
    );
  }
}

const mapStateToProps = state => ({
  enableBackgroundImage: state.ThemeOptions.enableBackgroundImage,
  enableSidebarShadow: state.ThemeOptions.enableSidebarShadow,
  enableMobileMenu: state.ThemeOptions.enableMobileMenu,
  backgroundColor: state.ThemeOptions.backgroundColor,
  backgroundImage: state.ThemeOptions.backgroundImage,
  backgroundImageOpacity: state.ThemeOptions.backgroundImageOpacity,
  account: state.authentication.account
});

const mapDispatchToProps = dispatch => ({
  setEnableMobileMenu: enable => dispatch(setEnableMobileMenu(enable))
});

export default withScreenSize(connect(mapStateToProps, mapDispatchToProps)(AppSidebar));
