import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionBundleSettings from './subscription-bundle-settings';
import SubscriptionBundleSettingsDetail from './subscription-bundle-settings-detail';
import SubscriptionBundleSettingsUpdate from './subscription-bundle-settings-update';
import SubscriptionBundleSettingsDeleteDialog from './subscription-bundle-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionBundleSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionBundleSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionBundleSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionBundleSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionBundleSettingsDeleteDialog} />
  </>
);

export default Routes;
