import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BundleRule from './bundle-rule';
import BundleRuleDetail from './bundle-rule-detail';
import BundleRuleUpdate from './bundle-rule-update';
import BundleRuleDeleteDialog from './bundle-rule-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BundleRuleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BundleRuleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BundleRuleDetail} />
      <ErrorBoundaryRoute path={match.url} component={BundleRule} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BundleRuleDeleteDialog} />
  </>
);

export default Routes;
