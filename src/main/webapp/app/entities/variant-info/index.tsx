import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VariantInfo from './variant-info';
import VariantInfoDetail from './variant-info-detail';
import VariantInfoUpdate from './variant-info-update';
import VariantInfoDeleteDialog from './variant-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VariantInfoDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VariantInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VariantInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VariantInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={VariantInfo} />
    </Switch>
  </>
);

export default Routes;
