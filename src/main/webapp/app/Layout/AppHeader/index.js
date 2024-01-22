import React, { Fragment } from 'react';
import cx from 'classnames';

import { connect } from 'react-redux';

import ReactCSSTransitionGroup from 'react-addons-css-transition-group';

import HeaderLogo from '../AppLogo';

import SearchBox from './Components/SearchBox';
import UserBox from './Components/UserBox';

import { Progress } from 'react-sweet-progress';
import { BasedOn } from 'app/shared/model/enumerations/based-on.model';
import useScreenSize from "use-screen-size";
import {Link} from "react-router-dom";

const Header = ({ headerBackgroundColor, enableMobileMenuSmall, enableHeaderShadow, account, paymentPlanLimit }) => {
    const size = useScreenSize();
    let totalQuotaPercentUsed = 0;
    if (paymentPlanLimit.activeSubscriptionCount != null && paymentPlanLimit.planLimit != null) {
      totalQuotaPercentUsed = ((paymentPlanLimit.activeSubscriptionCount / paymentPlanLimit.planLimit) * 100).toFixed(0);
    }

    if (paymentPlanLimit?.planInfo?.basedOn == BasedOn.SUBSCRIPTION_ORDER_AMOUNT && paymentPlanLimit.usedOrderAmount != null && paymentPlanLimit.orderAmountLimit != null) {
      totalQuotaPercentUsed = ((paymentPlanLimit.usedOrderAmount / paymentPlanLimit.orderAmountLimit) * 100).toFixed(0);
    }

    if(account == null) {
      return null;
    }
  // const isInsideShopify = ((window.location.href.includes("myshopify.com") || (window?.location?.ancestorOrigins?.[0] ? window?.location?.ancestorOrigins[0]?.includes("myshopify.com") : false)) && size.width >= 993);
    return (
      <Fragment>
        {
          <ReactCSSTransitionGroup
            component="div"
            className={cx('app-header', headerBackgroundColor, { 'header-shadow': enableHeaderShadow })}
            transitionName="HeaderAnimation"
            transitionAppear
            transitionAppearTimeout={1500}
            transitionEnter={false}
            transitionLeave={false}
          >
            <HeaderLogo />
            <div className={cx('app-header__content', { 'header-mobile-open': enableMobileMenuSmall })}>
              <div className="app-header-left">
                  <SearchBox/>
                {/* <MegaMenu/> */}
              </div>

              <div className="app-header-right d-none d-md-flex">
                {paymentPlanLimit.activeSubscriptionCount !== null && paymentPlanLimit.planLimit !== null && (
                  <>
                    <div className={'pl-4 pr-4'}>
                      <Progress
                        className="progress-bar-animated-alt"
                        percent={totalQuotaPercentUsed > 100 ? 100 : totalQuotaPercentUsed}
                        theme={{
                          error: {
                            symbol: (
                              <b>
                                {totalQuotaPercentUsed > 100 ? 100 : totalQuotaPercentUsed}
                                {'%'}
                              </b>
                            ),
                            color: 'red'
                          },
                          default: {
                            symbol: (
                              <b>
                                {totalQuotaPercentUsed > 100 ? 100 : totalQuotaPercentUsed}
                                {'%'}
                              </b>
                            ),
                            color: 'blue'
                          },
                          active: {
                            symbol: (
                              <b>
                                {totalQuotaPercentUsed > 100 ? 100 : totalQuotaPercentUsed}
                                {'%'}
                              </b>
                            ),
                            color: 'orange'
                          },
                          success: {
                            symbol: (
                              <b>
                                {totalQuotaPercentUsed > 100 ? 100 : totalQuotaPercentUsed}
                                {'%'}
                              </b>
                            ),
                            color: 'red'
                          }
                        }}
                      />
                      <div>
                        <span>You have used {paymentPlanLimit.activeSubscriptionCount}&nbsp;of limit {paymentPlanLimit.planLimit} Subscriptions. <Link to="/dashboards/billing">Upgrade.</Link></span>
                      </div>
                    </div>

                    {/* {totalQuotaPercentUsed > 90 ? (
                    <div className={'pl-4 pr-4'}>
                      To avoid disruption please <a href="#/dashboards/billing">upgrade your plan</a>.
                    </div>
                  ) : null} */}
                  </>
                )}

                {paymentPlanLimit?.planInfo?.basedOn == BasedOn.SUBSCRIPTION_ORDER_AMOUNT && paymentPlanLimit.usedOrderAmount !== null && paymentPlanLimit.orderAmountLimit !== null && (
                  <>
                    <div className={'pl-4 pr-4'}>
                      <Progress
                        className="progress-bar-animated-alt"
                        percent={totalQuotaPercentUsed > 100 ? 100 : totalQuotaPercentUsed}
                        theme={{
                          error: {
                            // symbol: (
                            //   <b>
                            //     {totalQuotaPercentUsed > 100 ? paymentPlanLimit.orderAmountLimit : paymentPlanLimit.usedOrderAmount}{' USD'}
                            //   </b>
                            // ),
                            color: 'red'
                          },
                          default: {
                            // symbol: (
                            //   <b>
                            //     {totalQuotaPercentUsed > 100 ? paymentPlanLimit.orderAmountLimit : paymentPlanLimit.usedOrderAmount}{' USD'}
                            //   </b>
                            // ),
                            color: 'blue'
                          },
                          active: {
                            // symbol: (
                            //   <b>
                            //     {totalQuotaPercentUsed > 100 ? paymentPlanLimit.orderAmountLimit : paymentPlanLimit.usedOrderAmount}{' USD'}
                            //   </b>
                            // ),
                            color: 'orange'
                          },
                          success: {
                            // symbol: (
                            //   <b>
                            //     {totalQuotaPercentUsed > 100 ? paymentPlanLimit.orderAmountLimit : paymentPlanLimit.usedOrderAmount}{' USD'}
                            //   </b>
                            // ),
                            color: 'red'
                          }
                        }}
                      />
                      <div>
                        <span className='headerSubscriptionText'>Monthly subscription: ${paymentPlanLimit.usedOrderAmount?.toLocaleString()}/${paymentPlanLimit.orderAmountLimit?.toLocaleString()}. <Link to="/dashboards/billing">Upgrade.</Link></span>
                      </div>
                    </div>

                    {/* {totalQuotaPercentUsed > 90 ? (
                    <div className={'pl-4 pr-4'}>
                      To avoid disruption please <a href="#/dashboards/billing">upgrade your plan</a>.
                    </div>
                  ) : null} */}
                  </>
                )}

                {/*<HeaderDots account={account} />*/}
                <UserBox shop={account.login} totalSalesGenerated={account.totalSalesGenerated} />
                {/* <HeaderRightDrawer/> */}
              </div>
            </div>
          </ReactCSSTransitionGroup>
        }
      </Fragment>
    );

}

const mapStateToProps = state => ({
  enableHeaderShadow: state.ThemeOptions.enableHeaderShadow,
  closedSmallerSidebar: state.ThemeOptions.closedSmallerSidebar,
  headerBackgroundColor: state.ThemeOptions.headerBackgroundColor,
  enableMobileMenuSmall: state.ThemeOptions.enableMobileMenuSmall,
  paymentPlanLimit: state.paymentPlan.paymentPlanLimit,
  account: state.authentication.account,
});

const mapDispatchToProps = dispatch => ({});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header);
