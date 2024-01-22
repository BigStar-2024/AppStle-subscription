import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShopLabel from './shop-label';
import ShopLabelDetail from './shop-label-detail';
import ShopLabelUpdate from './shop-label-update';
import ShopLabelDeleteDialog from './shop-label-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopLabelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopLabelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopLabelDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShopLabel} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShopLabelDeleteDialog} />
  </>
);

export default Routes;
