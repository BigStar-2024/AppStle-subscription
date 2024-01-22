import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MemberOnly from './member-only';
import MemberOnlyDetail from './member-only-detail';
import MemberOnlyUpdate from './member-only-update';
import MemberOnlyDeleteDialog from './member-only-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MemberOnlyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MemberOnlyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MemberOnlyDetail} />
      <ErrorBoundaryRoute path={match.url} component={MemberOnly} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MemberOnlyDeleteDialog} />
  </>
);

export default Routes;
