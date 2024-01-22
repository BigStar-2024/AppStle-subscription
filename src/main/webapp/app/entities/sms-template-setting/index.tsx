import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SmsTemplateSetting from './sms-template-setting';
import SmsTemplateSettingDetail from './sms-template-setting-detail';
import SmsTemplateSettingUpdate from './sms-template-setting-update';
import SmsTemplateSettingDeleteDialog from './sms-template-setting-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SmsTemplateSettingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SmsTemplateSettingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SmsTemplateSettingDetail} />
      <ErrorBoundaryRoute path={match.url} component={SmsTemplateSetting} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SmsTemplateSettingDeleteDialog} />
  </>
);

export default Routes;
