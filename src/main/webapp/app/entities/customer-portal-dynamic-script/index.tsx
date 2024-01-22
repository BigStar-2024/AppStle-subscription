import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomerPortalDynamicScript from './customer-portal-dynamic-script';
import CustomerPortalDynamicScriptDetail from './customer-portal-dynamic-script-detail';
import CustomerPortalDynamicScriptUpdate from './customer-portal-dynamic-script-update';
import CustomerPortalDynamicScriptDeleteDialog from './customer-portal-dynamic-script-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomerPortalDynamicScriptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomerPortalDynamicScriptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomerPortalDynamicScriptDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomerPortalDynamicScript} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomerPortalDynamicScriptDeleteDialog} />
  </>
);

export default Routes;
