import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MembershipDiscount from './membership-discount';
import MembershipDiscountDetail from './membership-discount-detail';
import MembershipDiscountUpdate from './membership-discount-update';
import MembershipDiscountDeleteDialog from './membership-discount-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MembershipDiscountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MembershipDiscountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MembershipDiscountDetail} />
      <ErrorBoundaryRoute path={match.url} component={MembershipDiscount} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MembershipDiscountDeleteDialog} />
  </>
);

export default Routes;
