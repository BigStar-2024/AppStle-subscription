import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShopCustomization from './shop-customization';
import ShopCustomizationDetail from './shop-customization-detail';
import ShopCustomizationUpdate from './shop-customization-update';
import ShopCustomizationDeleteDialog from './shop-customization-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShopCustomizationDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopCustomizationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopCustomizationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopCustomizationDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShopCustomization} />
    </Switch>
  </>
);

export default Routes;
