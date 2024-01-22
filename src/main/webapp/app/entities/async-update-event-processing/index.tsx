import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AsyncUpdateEventProcessing from './async-update-event-processing';
import AsyncUpdateEventProcessingDetail from './async-update-event-processing-detail';
import AsyncUpdateEventProcessingUpdate from './async-update-event-processing-update';
import AsyncUpdateEventProcessingDeleteDialog from './async-update-event-processing-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AsyncUpdateEventProcessingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AsyncUpdateEventProcessingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AsyncUpdateEventProcessingDetail} />
      <ErrorBoundaryRoute path={match.url} component={AsyncUpdateEventProcessing} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AsyncUpdateEventProcessingDeleteDialog} />
  </>
);

export default Routes;
