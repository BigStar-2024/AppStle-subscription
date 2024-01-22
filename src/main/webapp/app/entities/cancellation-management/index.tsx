import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CancellationManagement from './cancellation-management';
import CancellationManagementDetail from './cancellation-management-detail';
import CancellationManagementUpdate from './cancellation-management-update';
import CancellationManagementDeleteDialog from './cancellation-management-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CancellationManagementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CancellationManagementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CancellationManagementDetail} />
      <ErrorBoundaryRoute path={match.url} component={CancellationManagement} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CancellationManagementDeleteDialog} />
  </>
);

export default Routes;
