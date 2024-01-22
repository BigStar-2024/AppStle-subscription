import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UsergroupRule from './usergroup-rule';
import UsergroupRuleDetail from './usergroup-rule-detail';
import UsergroupRuleUpdate from './usergroup-rule-update';
import UsergroupRuleDeleteDialog from './usergroup-rule-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UsergroupRuleDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UsergroupRuleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UsergroupRuleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UsergroupRuleDetail} />
      <ErrorBoundaryRoute path={match.url} component={UsergroupRule} />
    </Switch>
  </>
);

export default Routes;
