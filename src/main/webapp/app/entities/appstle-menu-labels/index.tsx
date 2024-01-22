import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AppstleMenuLabels from './appstle-menu-labels';
import AppstleMenuLabelsDetail from './appstle-menu-labels-detail';
import AppstleMenuLabelsUpdate from './appstle-menu-labels-update';
import AppstleMenuLabelsDeleteDialog from './appstle-menu-labels-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AppstleMenuLabelsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AppstleMenuLabelsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AppstleMenuLabelsDetail} />
      <ErrorBoundaryRoute path={match.url} component={AppstleMenuLabels} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AppstleMenuLabelsDeleteDialog} />
  </>
);

export default Routes;
