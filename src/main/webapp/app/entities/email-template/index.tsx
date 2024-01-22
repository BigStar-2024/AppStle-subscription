import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EmailTemplate from './email-template';
import EmailTemplateDetail from './email-template-detail';
import EmailTemplateUpdate from './email-template-update';
import EmailTemplateDeleteDialog from './email-template-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmailTemplateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmailTemplateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmailTemplateDetail} />
      <ErrorBoundaryRoute path={match.url} component={EmailTemplate} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EmailTemplateDeleteDialog} />
  </>
);

export default Routes;
