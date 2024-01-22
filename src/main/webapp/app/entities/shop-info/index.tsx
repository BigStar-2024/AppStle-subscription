import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShopInfo from './shop-info';
import ShopInfoDetail from './shop-info-detail';
import ShopInfoUpdate from './shop-info-update';
import ShopInfoDeleteDialog from './shop-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShopInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShopInfoDeleteDialog} />
  </>
);

export default Routes;
