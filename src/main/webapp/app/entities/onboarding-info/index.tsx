import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OnboardingInfo from './onboarding-info';
import OnboardingInfoDetail from './onboarding-info-detail';
import OnboardingInfoUpdate from './onboarding-info-update';
import OnboardingInfoDeleteDialog from './onboarding-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OnboardingInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OnboardingInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OnboardingInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={OnboardingInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OnboardingInfoDeleteDialog} />
  </>
);

export default Routes;
