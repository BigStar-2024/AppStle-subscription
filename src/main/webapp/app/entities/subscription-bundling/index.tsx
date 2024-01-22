import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionBundling from './subscription-bundling';
import SubscriptionBundlingDetail from './subscription-bundling-detail';
import SubscriptionBundlingUpdate from './subscription-bundling-update';
import SubscriptionBundlingDeleteDialog from './subscription-bundling-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionBundlingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionBundlingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionBundlingDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionBundling} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionBundlingDeleteDialog} />
  </>
);

export default Routes;
