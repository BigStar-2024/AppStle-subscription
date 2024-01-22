import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionContractDetails from './subscription-contract-details';
import SubscriptionContractDetailsDetail from './subscription-contract-details-detail';
import SubscriptionContractDetailsUpdate from './subscription-contract-details-update';
import SubscriptionContractDetailsDeleteDialog from './subscription-contract-details-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionContractDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionContractDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionContractDetailsDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionContractDetails} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionContractDetailsDeleteDialog} />
  </>
);

export default Routes;
