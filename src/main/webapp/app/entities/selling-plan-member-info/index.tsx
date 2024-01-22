import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SellingPlanMemberInfo from './selling-plan-member-info';
import SellingPlanMemberInfoDetail from './selling-plan-member-info-detail';
import SellingPlanMemberInfoUpdate from './selling-plan-member-info-update';
import SellingPlanMemberInfoDeleteDialog from './selling-plan-member-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SellingPlanMemberInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SellingPlanMemberInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SellingPlanMemberInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={SellingPlanMemberInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SellingPlanMemberInfoDeleteDialog} />
  </>
);

export default Routes;
