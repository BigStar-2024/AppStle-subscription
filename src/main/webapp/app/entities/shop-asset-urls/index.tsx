import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShopAssetUrls from './shop-asset-urls';
import ShopAssetUrlsDetail from './shop-asset-urls-detail';
import ShopAssetUrlsUpdate from './shop-asset-urls-update';
import ShopAssetUrlsDeleteDialog from './shop-asset-urls-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShopAssetUrlsDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopAssetUrlsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopAssetUrlsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopAssetUrlsDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShopAssetUrls} />
    </Switch>
  </>
);

export default Routes;
