import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionContractProcessing from './subscription-contract-processing';
import SubscriptionContractProcessingDetail from './subscription-contract-processing-detail';
import SubscriptionContractProcessingUpdate from './subscription-contract-processing-update';
import SubscriptionContractProcessingDeleteDialog from './subscription-contract-processing-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionContractProcessingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionContractProcessingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionContractProcessingDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionContractProcessing} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionContractProcessingDeleteDialog} />
  </>
);

export default Routes;
