import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionContractOneOff from './subscription-contract-one-off';
import SubscriptionContractOneOffDetail from './subscription-contract-one-off-detail';
import SubscriptionContractOneOffUpdate from './subscription-contract-one-off-update';
import SubscriptionContractOneOffDeleteDialog from './subscription-contract-one-off-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionContractOneOffUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionContractOneOffUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionContractOneOffDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionContractOneOff} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionContractOneOffDeleteDialog} />
  </>
);

export default Routes;
