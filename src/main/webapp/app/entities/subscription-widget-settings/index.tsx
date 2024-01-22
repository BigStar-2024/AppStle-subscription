import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionWidgetSettings from './subscription-widget-settings';
import SubscriptionWidgetSettingsDetail from './subscription-widget-settings-detail';
import SubscriptionWidgetSettingsUpdate from './subscription-widget-settings-update';
import SubscriptionWidgetSettingsDeleteDialog from './subscription-widget-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionWidgetSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionWidgetSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionWidgetSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionWidgetSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionWidgetSettingsDeleteDialog} />
  </>
);

export default Routes;
