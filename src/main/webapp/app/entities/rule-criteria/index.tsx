import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RuleCriteria from './rule-criteria';
import RuleCriteriaDetail from './rule-criteria-detail';
import RuleCriteriaUpdate from './rule-criteria-update';
import RuleCriteriaDeleteDialog from './rule-criteria-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RuleCriteriaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RuleCriteriaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RuleCriteriaDetail} />
      <ErrorBoundaryRoute path={match.url} component={RuleCriteria} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RuleCriteriaDeleteDialog} />
  </>
);

export default Routes;
