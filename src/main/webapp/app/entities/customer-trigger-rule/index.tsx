import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomerTriggerRule from './customer-trigger-rule';
import CustomerTriggerRuleDetail from './customer-trigger-rule-detail';
import CustomerTriggerRuleUpdate from './customer-trigger-rule-update';
import CustomerTriggerRuleDeleteDialog from './customer-trigger-rule-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomerTriggerRuleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomerTriggerRuleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomerTriggerRuleDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomerTriggerRule} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomerTriggerRuleDeleteDialog} />
  </>
);

export default Routes;
