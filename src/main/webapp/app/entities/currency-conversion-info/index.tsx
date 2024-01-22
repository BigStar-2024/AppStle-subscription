import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CurrencyConversionInfo from './currency-conversion-info';
import CurrencyConversionInfoDetail from './currency-conversion-info-detail';
import CurrencyConversionInfoUpdate from './currency-conversion-info-update';
import CurrencyConversionInfoDeleteDialog from './currency-conversion-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CurrencyConversionInfoDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CurrencyConversionInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CurrencyConversionInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CurrencyConversionInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={CurrencyConversionInfo} />
    </Switch>
  </>
);

export default Routes;
