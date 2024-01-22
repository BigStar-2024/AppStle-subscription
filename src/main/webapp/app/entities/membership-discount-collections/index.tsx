import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MembershipDiscountCollections from './membership-discount-collections';
import MembershipDiscountCollectionsDetail from './membership-discount-collections-detail';
import MembershipDiscountCollectionsUpdate from './membership-discount-collections-update';
import MembershipDiscountCollectionsDeleteDialog from './membership-discount-collections-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MembershipDiscountCollectionsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MembershipDiscountCollectionsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MembershipDiscountCollectionsDetail} />
      <ErrorBoundaryRoute path={match.url} component={MembershipDiscountCollections} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MembershipDiscountCollectionsDeleteDialog} />
  </>
);

export default Routes;
