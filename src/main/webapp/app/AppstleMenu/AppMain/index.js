import React, {Fragment, lazy, Suspense} from 'react';
import Loader from 'react-loaders';
import {Route, Switch} from 'react-router-dom';
import {useMainContext} from "app/AppstleMenu/context";
import {Toaster} from 'react-hot-toast';

const AppstleMenu = lazy(() => import('../AppstleMenu/index'));

const AppMain = () => {
  const {loading} = useMainContext();
  return (
    <Fragment>
      <Suspense
        fallback={
          <div className="loader-container">
            <div className="loader-container-inner">
              <div className="text-center">
                <Loader type="ball-grid-beat"/>
              </div>
              <div style={{margin: '10% 0 0 43%', flexDirection: 'column'}}
                   className="loader-wrapper d-flex justify-content-center align-items-center">
                <div className="appstle_preloader appstle_loader--big"></div>
              </div>
            </div>
          </div>
        }
      >
        <Switch>
          <Route path="/" component={AppstleMenu}/>
        </Switch>
      </Suspense>
      <Toaster position="top-center" reverseOrder={false}/>
    </Fragment>
  );
};

export default AppMain;
