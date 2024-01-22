import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionGroupPlan from './subscription-group-plan';
import SubscriptionGroupPlanDetail from './subscription-group-plan-detail';
import SubscriptionGroupPlanUpdate from './subscription-group-plan-update';
import SubscriptionGroupPlanDeleteDialog from './subscription-group-plan-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionGroupPlanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionGroupPlanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionGroupPlanDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionGroupPlan} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionGroupPlanDeleteDialog} />
  </>
);

export default Routes;
