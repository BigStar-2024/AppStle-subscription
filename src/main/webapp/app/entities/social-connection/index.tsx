import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SocialConnection from './social-connection';
import SocialConnectionDetail from './social-connection-detail';
import SocialConnectionUpdate from './social-connection-update';
import SocialConnectionDeleteDialog from './social-connection-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SocialConnectionDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SocialConnectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SocialConnectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SocialConnectionDetail} />
      <ErrorBoundaryRoute path={match.url} component={SocialConnection} />
    </Switch>
  </>
);

export default Routes;
