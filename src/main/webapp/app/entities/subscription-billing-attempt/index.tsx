import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionBillingAttempt from './subscription-billing-attempt';
import SubscriptionBillingAttemptDetail from './subscription-billing-attempt-detail';
import SubscriptionBillingAttemptUpdate from './subscription-billing-attempt-update';
import SubscriptionBillingAttemptDeleteDialog from './subscription-billing-attempt-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionBillingAttemptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionBillingAttemptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionBillingAttemptDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionBillingAttempt} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionBillingAttemptDeleteDialog} />
  </>
);

export default Routes;
