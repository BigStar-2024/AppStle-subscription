import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionContractSettings from './subscription-contract-settings';
import SubscriptionContractSettingsDetail from './subscription-contract-settings-detail';
import SubscriptionContractSettingsUpdate from './subscription-contract-settings-update';
import SubscriptionContractSettingsDeleteDialog from './subscription-contract-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionContractSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionContractSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionContractSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionContractSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionContractSettingsDeleteDialog} />
  </>
);

export default Routes;
