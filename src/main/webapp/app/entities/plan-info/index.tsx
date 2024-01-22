import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PlanInfo from './plan-info';
import PlanInfoDetail from './plan-info-detail';
import PlanInfoUpdate from './plan-info-update';
import PlanInfoDeleteDialog from './plan-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PlanInfoDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PlanInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PlanInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PlanInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={PlanInfo} />
    </Switch>
  </>
);

export default Routes;
