import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PaymentPlan from './payment-plan';
import PaymentPlanDetail from './payment-plan-detail';
import PaymentPlanUpdate from './payment-plan-update';
import PaymentPlanDeleteDialog from './payment-plan-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaymentPlanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaymentPlanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaymentPlanDetail} />
      <ErrorBoundaryRoute path={match.url} component={PaymentPlan} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PaymentPlanDeleteDialog} />
  </>
);

export default Routes;
