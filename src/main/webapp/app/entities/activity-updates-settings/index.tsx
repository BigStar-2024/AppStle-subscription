import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ActivityUpdatesSettings from './activity-updates-settings';
import ActivityUpdatesSettingsDetail from './activity-updates-settings-detail';
import ActivityUpdatesSettingsUpdate from './activity-updates-settings-update';
import ActivityUpdatesSettingsDeleteDialog from './activity-updates-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ActivityUpdatesSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ActivityUpdatesSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ActivityUpdatesSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={ActivityUpdatesSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ActivityUpdatesSettingsDeleteDialog} />
  </>
);

export default Routes;
