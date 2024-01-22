import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ActivityLog from './activity-log';
import ActivityLogDetail from './activity-log-detail';
import ActivityLogUpdate from './activity-log-update';
import ActivityLogDeleteDialog from './activity-log-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ActivityLogDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ActivityLogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ActivityLogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ActivityLogDetail} />
      <ErrorBoundaryRoute path={match.url} component={ActivityLog} />
    </Switch>
  </>
);

export default Routes;
