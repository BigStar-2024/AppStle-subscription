import React, { Fragment, lazy, Suspense } from 'react';
// import Loader from 'react-loaders';
import { Redirect, Route, Switch } from 'react-router-dom';
import { Slide, ToastContainer } from 'react-toastify';
import Loader from '../Bundles/Loader';


const Bundles = lazy(() => import('../Bundles/index'));

const AppMain = () => {
  return (
    <Fragment>
      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div style={{ margin: '10% 0 0 43%', flexDirection: 'column' }} className="loader-wrapper d-flex justify-content-center align-items-center">
                <Loader /> <span className="as-ml-2 as-text-sm">Please wait...</span>
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
