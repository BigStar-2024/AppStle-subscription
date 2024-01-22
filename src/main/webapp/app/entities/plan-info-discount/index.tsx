import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PlanInfoDiscount from './plan-info-discount';
import PlanInfoDiscountDetail from './plan-info-discount-detail';
import PlanInfoDiscountUpdate from './plan-info-discount-update';
import PlanInfoDiscountDeleteDialog from './plan-info-discount-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PlanInfoDiscountDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PlanInfoDiscountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PlanInfoDiscountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PlanInfoDiscountDetail} />
      <ErrorBoundaryRoute path={match.url} component={PlanInfoDiscount} />
    </Switch>
  </>
);

export default Routes;
