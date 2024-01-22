import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProcessedOrderInfo from './processed-order-info';
import ProcessedOrderInfoDetail from './processed-order-info-detail';
import ProcessedOrderInfoUpdate from './processed-order-info-update';
import ProcessedOrderInfoDeleteDialog from './processed-order-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProcessedOrderInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProcessedOrderInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProcessedOrderInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProcessedOrderInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProcessedOrderInfoDeleteDialog} />
  </>
);

export default Routes;
