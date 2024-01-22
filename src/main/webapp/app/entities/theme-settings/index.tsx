import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ThemeSettings from './theme-settings';
import ThemeSettingsDetail from './theme-settings-detail';
import ThemeSettingsUpdate from './theme-settings-update';
import ThemeSettingsDeleteDialog from './theme-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ThemeSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ThemeSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ThemeSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={ThemeSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ThemeSettingsDeleteDialog} />
  </>
);

export default Routes;
