import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomerPayment from './customer-payment';
import CustomerPaymentDetail from './customer-payment-detail';
import CustomerPaymentUpdate from './customer-payment-update';
import CustomerPaymentDeleteDialog from './customer-payment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomerPaymentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomerPaymentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomerPaymentDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomerPayment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomerPaymentDeleteDialog} />
  </>
);

export default Routes;
