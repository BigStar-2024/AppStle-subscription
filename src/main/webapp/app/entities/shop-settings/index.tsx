import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShopSettings from './shop-settings';
import ShopSettingsDetail from './shop-settings-detail';
import ShopSettingsUpdate from './shop-settings-update';
import ShopSettingsDeleteDialog from './shop-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShopSettingsDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShopSettings} />
    </Switch>
  </>
);

export default Routes;
