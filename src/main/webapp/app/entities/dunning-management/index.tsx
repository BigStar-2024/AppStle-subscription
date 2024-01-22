import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DunningManagement from './dunning-management';
import DunningManagementDetail from './dunning-management-detail';
import DunningManagementUpdate from './dunning-management-update';
import DunningManagementDeleteDialog from './dunning-management-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DunningManagementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DunningManagementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DunningManagementDetail} />
      <ErrorBoundaryRoute path={match.url} component={DunningManagement} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DunningManagementDeleteDialog} />
  </>
);

export default Routes;
