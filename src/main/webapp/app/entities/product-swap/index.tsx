import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductSwap from './product-swap';
import ProductSwapDetail from './product-swap-detail';
import ProductSwapUpdate from './product-swap-update';
import ProductSwapDeleteDialog from './product-swap-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductSwapUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductSwapUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductSwapDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductSwap} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductSwapDeleteDialog} />
  </>
);

export default Routes;
