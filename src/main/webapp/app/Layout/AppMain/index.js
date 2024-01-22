import React, { Fragment, lazy, Suspense } from 'react';
import Loader from 'react-loaders';
import { Redirect, Route } from 'react-router-dom';
import { Slide, ToastContainer } from 'react-toastify';
import OnboardingExperience from 'app/DemoPages/Main/OnboardingExperience';


const Dashboards = lazy(() => import('../../DemoPages/Dashboards'));
const MainDashboard = lazy(() => import('../../DemoPages/Dashboards/MainDashobard'));

const AppMain = () => {
  return (
    <Fragment>
      {/* Components */}

      {/*<Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-pulse-rise" />
              </div>
              <h6 className="mt-5">
                Please wait while we load all the Components examples
                <small>
                  Because this is a demonstration we load at once all the Components examples. This wouldn't happen in a real live app!
                </small>
              </h6>
            </div>
          </div>
        }
      >
        <Route path="/components" component={Components} />
      </Suspense>

       Forms

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-pulse-rise" />
              </div>
              <h6 className="mt-5">
                Please wait while we load all the Forms examples
                <small>
                  Because this is a demonstration we load at once all the Forms examples. This wouldn't happen in a real live app!
                </small>
              </h6>
            </div>
          </div>
        }
      >
        <Route path="/forms" component={Forms} />
      </Suspense>

       Charts

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-rotate" />
              </div>
              <h6 className="mt-3">
                Please wait while we load all the Charts examples
                <small>
                  Because this is a demonstration we load at once all the Charts examples. This wouldn't happen in a real live app!
                </small>
              </h6>
            </div>
          </div>
        }
      >
        <Route path="/charts" component={Charts} />
      </Suspense>

       Tables

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-pulse-rise" />
              </div>
              <h6 className="mt-5">
                Please wait while we load all the Tables examples
                <small>
                  Because this is a demonstration we load at once all the Tables examples. This wouldn't happen in a real live app!
                </small>
              </h6>
            </div>
          </div>
        }
      >
        <Route path="/tables" component={Tables} />
      </Suspense>

       Elements

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="line-scale" />
              </div>
              <h6 className="mt-3">
                Please wait while we load all the Elements examples
                <small>
                  Because this is a demonstration we load at once all the Elements examples. This wouldn't happen in a real live app!
                </small>
              </h6>
            </div>
          </div>
        }
      >
        <Route path="/elements" component={Elements} />
      </Suspense>

       Dashboard Widgets

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-pulse-sync" />
              </div>
              <h6 className="mt-3">
                Please wait while we load all the Dashboard Widgets examples
                <small>
                  Because this is a demonstration we load at once all the Dashboard Widgets examples. This wouldn't happen in a real live
                  app!
                </small>
              </h6>
            </div>
          </div>
        }
      >
        <Route path="/widgets" component={Widgets} />
      </Suspense>

       Pages

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="line-scale-party" />
              </div>
              <h6 className="mt-3">
                Please wait while we load all the Pages examples
                <small>
                  Because this is a demonstration we load at once all the Pages examples. This wouldn't happen in a real live app!
                </small>
              </h6>
            </div>
          </div>
        }
      >
        <Route path="/pages" component={UserPages} />
      </Suspense>

       Applications

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-pulse" />
              </div>
              <h6 className="mt-3">
                Please wait while we load all the Applications examples
                <small>
                  Because this is a demonstration we load at once all the Applications examples. This wouldn't happen in a real live app!
                </small>
              </h6>
            </div>
          </div>
        }
      >
        <Route path="/apps" component={Applications} />
      </Suspense>*/}

      {/* Dashboards */}

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-grid-beat" />
              </div>
              <h6 className="mt-3">Please wait while we load the app</h6>
            </div>
          </div>
        }
      >
        <Route path="/dashboards" component={Dashboards} />
        <Route path="/onboarding" component={OnboardingExperience}></Route>
      </Suspense>

      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-grid-beat" />
              </div>
              <h6 className="mt-3">Please wait while we load the app</h6>
            </div>
          </div>
        }
      >
        <Route path="/main" component={MainDashboard} />
      </Suspense>

      {/* <Route exact path="/onboarding" component={OnboardingExperience}></Route> */}

      {/*<Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-grid-beat" />
              </div>
              <h6 className="mt-3">Please wait while we load the app</h6>
            </div>
          </div>
        }
      >
        <Route path="/support" component={Support} />
      </Suspense>*/}

      <Route exact path="/" render={() => <Redirect to="/main/dashboard" />} />
      <ToastContainer transition={Slide}/>
    </Fragment>
  );
};

export default AppMain;
