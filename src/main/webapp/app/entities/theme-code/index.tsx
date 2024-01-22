import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ThemeCode from './theme-code';
import ThemeCodeDetail from './theme-code-detail';
import ThemeCodeUpdate from './theme-code-update';
import ThemeCodeDeleteDialog from './theme-code-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ThemeCodeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ThemeCodeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ThemeCodeDetail} />
      <ErrorBoundaryRoute path={match.url} component={ThemeCode} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ThemeCodeDeleteDialog} />
  </>
);

export default Routes;
