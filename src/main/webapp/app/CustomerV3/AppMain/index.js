import React, {Fragment, lazy, Suspense, useEffect} from 'react';
import Loader from 'react-loaders';
import {Redirect, Route} from 'react-router-dom';
import {Slide, ToastContainer} from 'react-toastify';


const Subscription = lazy(() => import('../Subscription/index'));

const AppMain = () => {
  useEffect(() => {
    if (window?.location?.search) {
      window.sessionStorage.setItem('appstle_url_params', window.location.search);
    }
  }, []);
  return (
    <Fragment>
      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-grid-beat"/>
              </div>
              <div style={{ margin: '10% 0 0 43%', flexDirection: 'column' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <div class="appstle_preloader appstle_loader--big"></div>
          </div>
            </div>
          </div>
        }
      >
        <Route path="/subscriptions" component={Subscription}/>
      </Suspense>

      <Route exact path="/" render={() => <Redirect to={"/subscriptions/list"} />} />
      {/* <ToastContainer transition={Slide}/> */}
    </Fragment>
  );
};

export default AppMain;
