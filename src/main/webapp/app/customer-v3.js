import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { HashRouter } from 'react-router-dom';
import { bindActionCreators } from 'redux';
// import '../static/theme/assets/base.scss';

// import 'bootstrap/dist/css/bootstrap.min.css';
import setupAxiosInterceptors from './config/axios-interceptor';
import initStore from './config/store';
import Main from './CustomerV3/Main';
import * as serviceWorker from './serviceWorker';
import { clearAuthentication } from './shared/reducers/authentication';
import { IS_LOCALHOST } from './config/api-config';
import ErrorBoundary from 'app/shared/error/error-boundary-customer-portal_v3';
import '../static/theme/assets/base_customer_v3.scss';

const store = initStore();

if (IS_LOCALHOST !== true) {
  // eslint-disable-next-line no-multi-assign
  console.log = console.warn = console.error = () => {};
}

const rootElement = document.getElementById('root');
const root = createRoot(rootElement)

const actions = bindActionCreators({ clearAuthentication }, store.dispatch);
setupAxiosInterceptors(() => {
  actions.clearAuthentication('login.error.unauthorized');
  root.render(
    <div className="text-center">
    </div>
  );
});
// while (rootElement.firstChild) {
//   rootElement.removeChild(rootElement.firstChild);
// }

if (IS_LOCALHOST !== true) {
  // Sentry.init({ dsn: 'https://6bb23760e6b34ea9bfa8cf1a41d0e3f7@sentry.io/1504343' });
}

var loaderElement = document.querySelectorAll('#appstle_initialLoader');
loaderElement.forEach(function(item) {
      item.remove()
})

const renderApp = Component => {
  root.render(
    <Provider store={store}>
      <HashRouter>
       {/* <ErrorBoundary> */}
          <Component />
       {/* </ErrorBoundary> */}
      </HashRouter>
    </Provider>
  );
};

renderApp(Main);

if (module.hot) {
  module.hot.accept('./CustomerV3/Main', () => {
    const NextApp = require('./CustomerV3/Main').default;
    renderApp(NextApp);
  });
}
serviceWorker.unregister();
