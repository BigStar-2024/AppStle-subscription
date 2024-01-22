import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PaymentPlanActivity from './payment-plan-activity';
import PaymentPlanActivityDetail from './payment-plan-activity-detail';
import PaymentPlanActivityUpdate from './payment-plan-activity-update';
import PaymentPlanActivityDeleteDialog from './payment-plan-activity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PaymentPlanActivityDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaymentPlanActivityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaymentPlanActivityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaymentPlanActivityDetail} />
      <ErrorBoundaryRoute path={match.url} component={PaymentPlanActivity} />
    </Switch>
  </>
);

export default Routes;
