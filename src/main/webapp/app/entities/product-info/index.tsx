import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductInfo from './product-info';
import ProductInfoDetail from './product-info-detail';
import ProductInfoUpdate from './product-info-update';
import ProductInfoDeleteDialog from './product-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductInfoDeleteDialog} />
  </>
);

export default Routes;
