import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrderInfo from './order-info';
import OrderInfoDetail from './order-info-detail';
import OrderInfoUpdate from './order-info-update';
import OrderInfoDeleteDialog from './order-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OrderInfoDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrderInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrderInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrderInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrderInfo} />
    </Switch>
  </>
);

export default Routes;
