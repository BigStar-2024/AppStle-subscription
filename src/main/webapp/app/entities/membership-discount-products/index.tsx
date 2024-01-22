import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MembershipDiscountProducts from './membership-discount-products';
import MembershipDiscountProductsDetail from './membership-discount-products-detail';
import MembershipDiscountProductsUpdate from './membership-discount-products-update';
import MembershipDiscountProductsDeleteDialog from './membership-discount-products-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MembershipDiscountProductsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MembershipDiscountProductsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MembershipDiscountProductsDetail} />
      <ErrorBoundaryRoute path={match.url} component={MembershipDiscountProducts} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MembershipDiscountProductsDeleteDialog} />
  </>
);

export default Routes;
