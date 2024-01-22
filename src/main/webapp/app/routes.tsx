import React from 'react';
import { Switch } from 'react-router-dom';
import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import Entities from 'app/entities';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import LiquidEditor from "app/modules/account/liquid-editor/LiquidEditor";
import ImportCSV from "app/modules/account/ImportCSV";
import ImportPlanProductCSV from "app/modules/account/ImportPlanProductCSV";
import AutoSubscriptionPlanCreate from "app/modules/account/AutoSubscriptionPlanCreate";
import LockBillingPlan from "app/modules/account/lock-billing-plan/LockBillingPlan";

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => <div>loading ...</div>
});

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => <div>loading ...</div>
});

const Routes = () => (
  <div className="view-routes">
    <Switch>
      <ErrorBoundaryRoute path="/admin/login" component={Login} />
      <ErrorBoundaryRoute path="/admin/logout" component={Logout} />
      <ErrorBoundaryRoute path="/admin/account/register" component={Register} />
      <ErrorBoundaryRoute path="/admin/account/activate/:key?" component={Activate} />
      <PrivateRoute path="/admin/liquid-editor" component={LiquidEditor} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute path="/admin/account/reset/request" component={PasswordResetInit} />
      <PrivateRoute path="/admin/import-csv" component={ImportCSV} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/admin/import-plan-product-csv" component={ImportPlanProductCSV} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/admin/lock-billing-plan" component={LockBillingPlan} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/admin/auto-subscription-plan" component={AutoSubscriptionPlanCreate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute path="/admin/account/reset/finish/:key?" component={PasswordResetFinish} />
      <PrivateRoute path="/admin/admin" component={Admin} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/admin/account" component={Account} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute path="/admin/" exact component={Home} />
      <PrivateRoute path="/admin/" component={Entities} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute component={PageNotFound} />
    </Switch>
  </div>
);

export default Routes;
