import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TriggerRule from './trigger-rule';
import TriggerRuleDetail from './trigger-rule-detail';
import TriggerRuleUpdate from './trigger-rule-update';
import TriggerRuleDeleteDialog from './trigger-rule-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TriggerRuleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TriggerRuleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TriggerRuleDetail} />
      <ErrorBoundaryRoute path={match.url} component={TriggerRule} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TriggerRuleDeleteDialog} />
  </>
);

export default Routes;
