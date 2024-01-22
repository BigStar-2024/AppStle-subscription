import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionCustomCss from './subscription-custom-css';
import SubscriptionCustomCssDetail from './subscription-custom-css-detail';
import SubscriptionCustomCssUpdate from './subscription-custom-css-update';
import SubscriptionCustomCssDeleteDialog from './subscription-custom-css-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionCustomCssUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionCustomCssUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionCustomCssDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionCustomCss} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionCustomCssDeleteDialog} />
  </>
);

export default Routes;
