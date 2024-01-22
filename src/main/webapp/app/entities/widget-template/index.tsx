import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WidgetTemplate from './widget-template';
import WidgetTemplateDetail from './widget-template-detail';
import WidgetTemplateUpdate from './widget-template-update';
import WidgetTemplateDeleteDialog from './widget-template-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WidgetTemplateDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WidgetTemplateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WidgetTemplateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WidgetTemplateDetail} />
      <ErrorBoundaryRoute path={match.url} component={WidgetTemplate} />
    </Switch>
  </>
);

export default Routes;
