import React, { Fragment, lazy, Suspense } from 'react';
import Loader from 'react-loaders';
import { Redirect, Route, Switch } from 'react-router-dom';
import { Slide, ToastContainer } from 'react-toastify';


const Bundles = lazy(() => import('../Bundles/index'));

const AppMain = () => {
  return (
    <Fragment>
      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-grid-beat" />
              </div>
              <div style={{ margin: '10% 0 0 43%', flexDirection: 'column' }} className="loader-wrapper d-flex justify-content-center align-items-center">
                <div class="appstle_preloader appstle_loader--big"></div>
              </div>
            </div>
          </div>
        }
      >
      <Switch>
        <Route path="/" exact={true} component={Bundles} />
        <Route path="/:bundleSlug" exact={true} component={Bundles} />
      </Switch>
      </Suspense>

      <ToastContainer transition={Slide} />
    </Fragment>
  );
};

export default AppMain;
