import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BackdatingJobSummary from './backdating-job-summary';
import BackdatingJobSummaryDetail from './backdating-job-summary-detail';
import BackdatingJobSummaryUpdate from './backdating-job-summary-update';
import BackdatingJobSummaryDeleteDialog from './backdating-job-summary-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BackdatingJobSummaryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BackdatingJobSummaryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BackdatingJobSummaryDetail} />
      <ErrorBoundaryRoute path={match.url} component={BackdatingJobSummary} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BackdatingJobSummaryDeleteDialog} />
  </>
);

export default Routes;
