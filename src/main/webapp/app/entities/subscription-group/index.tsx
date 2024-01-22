import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionGroup from './subscription-group';
import SubscriptionGroupDetail from './subscription-group-detail';
import SubscriptionGroupUpdate from './subscription-group-update';
import SubscriptionGroupDeleteDialog from './subscription-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionGroupDeleteDialog} />
  </>
);

export default Routes;
