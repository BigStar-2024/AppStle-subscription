import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Analytics from './analytics';
import AnalyticsDetail from './analytics-detail';
import AnalyticsUpdate from './analytics-update';
import AnalyticsDeleteDialog from './analytics-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnalyticsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnalyticsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnalyticsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Analytics} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnalyticsDeleteDialog} />
  </>
);

export default Routes;
