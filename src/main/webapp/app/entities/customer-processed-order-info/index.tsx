import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomerProcessedOrderInfo from './customer-processed-order-info';
import CustomerProcessedOrderInfoDetail from './customer-processed-order-info-detail';
import CustomerProcessedOrderInfoUpdate from './customer-processed-order-info-update';
import CustomerProcessedOrderInfoDeleteDialog from './customer-processed-order-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomerProcessedOrderInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomerProcessedOrderInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomerProcessedOrderInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomerProcessedOrderInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomerProcessedOrderInfoDeleteDialog} />
  </>
);

export default Routes;
