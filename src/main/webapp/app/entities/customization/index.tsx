import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Customization from './customization';
import CustomizationDetail from './customization-detail';
import CustomizationUpdate from './customization-update';
import CustomizationDeleteDialog from './customization-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomizationDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomizationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomizationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomizationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Customization} />
    </Switch>
  </>
);

export default Routes;
