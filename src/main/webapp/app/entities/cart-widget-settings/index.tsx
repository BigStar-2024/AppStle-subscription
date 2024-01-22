import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CartWidgetSettings from './cart-widget-settings';
import CartWidgetSettingsDetail from './cart-widget-settings-detail';
import CartWidgetSettingsUpdate from './cart-widget-settings-update';
import CartWidgetSettingsDeleteDialog from './cart-widget-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CartWidgetSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CartWidgetSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CartWidgetSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={CartWidgetSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CartWidgetSettingsDeleteDialog} />
  </>
);

export default Routes;
