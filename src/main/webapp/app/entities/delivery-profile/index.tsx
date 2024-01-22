import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DeliveryProfile from './delivery-profile';
import DeliveryProfileDetail from './delivery-profile-detail';
import DeliveryProfileUpdate from './delivery-profile-update';
import DeliveryProfileDeleteDialog from './delivery-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DeliveryProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DeliveryProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DeliveryProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={DeliveryProfile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DeliveryProfileDeleteDialog} />
  </>
);

export default Routes;
