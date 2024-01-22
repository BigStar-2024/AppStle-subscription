import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductCycle from './product-cycle';
import ProductCycleDetail from './product-cycle-detail';
import ProductCycleUpdate from './product-cycle-update';
import ProductCycleDeleteDialog from './product-cycle-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductCycleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductCycleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductCycleDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductCycle} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductCycleDeleteDialog} />
  </>
);

export default Routes;
