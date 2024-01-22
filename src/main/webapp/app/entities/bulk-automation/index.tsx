import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BulkAutomation from './bulk-automation';
import BulkAutomationDetail from './bulk-automation-detail';
import BulkAutomationUpdate from './bulk-automation-update';
import BulkAutomationDeleteDialog from './bulk-automation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BulkAutomationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BulkAutomationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BulkAutomationDetail} />
      <ErrorBoundaryRoute path={match.url} component={BulkAutomation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BulkAutomationDeleteDialog} />
  </>
);

export default Routes;
