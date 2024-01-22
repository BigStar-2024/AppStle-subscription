import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EmailTemplateSetting from './email-template-setting';
import EmailTemplateSettingDetail from './email-template-setting-detail';
import EmailTemplateSettingUpdate from './email-template-setting-update';
import EmailTemplateSettingDeleteDialog from './email-template-setting-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmailTemplateSettingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmailTemplateSettingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmailTemplateSettingDetail} />
      <ErrorBoundaryRoute path={match.url} component={EmailTemplateSetting} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EmailTemplateSettingDeleteDialog} />
  </>
);

export default Routes;
