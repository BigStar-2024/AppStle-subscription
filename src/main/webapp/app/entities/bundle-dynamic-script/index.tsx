import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BundleDynamicScript from './bundle-dynamic-script';
import BundleDynamicScriptDetail from './bundle-dynamic-script-detail';
import BundleDynamicScriptUpdate from './bundle-dynamic-script-update';
import BundleDynamicScriptDeleteDialog from './bundle-dynamic-script-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BundleDynamicScriptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BundleDynamicScriptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BundleDynamicScriptDetail} />
      <ErrorBoundaryRoute path={match.url} component={BundleDynamicScript} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BundleDynamicScriptDeleteDialog} />
  </>
);

export default Routes;
