import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductField from './product-field';
import ProductFieldDetail from './product-field-detail';
import ProductFieldUpdate from './product-field-update';
import ProductFieldDeleteDialog from './product-field-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductFieldDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductFieldUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductFieldUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductFieldDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductField} />
    </Switch>
  </>
);

export default Routes;
