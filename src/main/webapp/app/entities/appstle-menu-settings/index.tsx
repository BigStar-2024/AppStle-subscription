import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AppstleMenuSettings from './appstle-menu-settings';
import AppstleMenuSettingsDetail from './appstle-menu-settings-detail';
import AppstleMenuSettingsUpdate from './appstle-menu-settings-update';
import AppstleMenuSettingsDeleteDialog from './appstle-menu-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AppstleMenuSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AppstleMenuSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AppstleMenuSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={AppstleMenuSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AppstleMenuSettingsDeleteDialog} />
  </>
);

export default Routes;
