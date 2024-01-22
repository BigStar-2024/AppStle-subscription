import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomerPortalSettings from './customer-portal-settings';
import CustomerPortalSettingsDetail from './customer-portal-settings-detail';
import CustomerPortalSettingsUpdate from './customer-portal-settings-update';
import CustomerPortalSettingsDeleteDialog from './customer-portal-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomerPortalSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomerPortalSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomerPortalSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomerPortalSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomerPortalSettingsDeleteDialog} />
  </>
);

export default Routes;
