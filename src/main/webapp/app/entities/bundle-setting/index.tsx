import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BundleSetting from './bundle-setting';
import BundleSettingDetail from './bundle-setting-detail';
import BundleSettingUpdate from './bundle-setting-update';
import BundleSettingDeleteDialog from './bundle-setting-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BundleSettingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BundleSettingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BundleSettingDetail} />
      <ErrorBoundaryRoute path={match.url} component={BundleSetting} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BundleSettingDeleteDialog} />
  </>
);

export default Routes;
