import React, { useMemo } from 'react';
import { createRoot } from 'react-dom/client';
import {Storage} from 'react-jhipster';
import {Provider} from 'react-redux';
import {BrowserRouter, useHistory, useLocation} from 'react-router-dom';
import { createTheme, ThemeProvider } from '@mui/material';
import {bindActionCreators} from 'redux';
import '../static/theme/assets/base.scss';
import Cookies from 'universal-cookie';
import setupAxiosInterceptors from './config/axios-interceptor';
import DevTools from './config/devtools';
import initStore from './config/store';
import Main from './DemoPages/Main';
import * as serviceWorker from './serviceWorker';
import {clearAuthentication} from './shared/reducers/authentication';
import {IS_LOCALHOST} from './config/api-config';
import 'react-tagsinput/react-tagsinput.css';
import {Provider as AppBridgeProvider} from '@shopify/app-bridge-react';
import { API_KEY, SERVER_API_URL } from 'app/config/constants';
// import '../static/theme../static/theme/assets/base.scss'
const cookies = new Cookies();
const devTools = process.env.NODE_ENV === 'development' ? <DevTools/> : null;

const store = initStore();
const theme = createTheme();

if (IS_LOCALHOST !== true) {
  // eslint-disable-next-line no-multi-assign
  console.log = console.warn = console.error = () => {
  };
}

const actions = bindActionCreators({clearAuthentication}, store.dispatch);
setupAxiosInterceptors(() => {
  actions.clearAuthentication('login.error.unauthorized');

  const root = createRoot(rootElement)

  root.render(
    <div className="text-center">
      Login unsuccessful
      <div>
        Kindly relaunch <b>Subscription</b> via Shopify admin. Please make sure your browser is not in incognito mode.
      </div>
    </div>
  );
});

const rootElement = document.getElementById('root');

try {
  if (cookies.get('jhi-authenticationToken') !== undefined && cookies.get('jhi-authenticationToken') != null) {
    Storage.session.set('jhi-authenticationToken', cookies.get('jhi-authenticationToken'));
  }
} catch (e) {

}

try {
  if (cookies.get('jhi-shop') !== undefined && cookies.get('jhi-shop') !== null) {
    Storage.session.set('jhi-shop', cookies.get('jhi-shop'));
  }
} catch (e) {

}


if (IS_LOCALHOST !== true) {
  // Sentry.init({ dsn: 'https://6bb23760e6b34ea9bfa8cf1a41d0e3f7@sentry.io/1504343' });
}

var urlParams = new URLSearchParams(window.location.search);
var host = urlParams.get('host');
var shop = urlParams.get('shop');
window.app = null;

if (shop) {
  window.__SHOP = shop;
}

if (host) {
  window.__SHOPIFY_DEV_HOST = host;
}

const renderApp = Component => {
  const root = createRoot(rootElement);

  root.render(
    <Provider store={store}>
      <ThemeProvider theme={theme}>
        <BrowserRouter>
          {((window['__SHOPIFY_DEV_HOST'] ? <MyApp Component={Component} shopifyHost={window['__SHOPIFY_DEV_HOST']} /> : <Component />))}
        </BrowserRouter>
      </ThemeProvider>
    </Provider>
  );
};

export function MyApp( {Component, shopifyHost}) {
  const navigate = useHistory();
  const location = useLocation();
  const history = useMemo(
    () => ({replace: (path) => navigate.replace(path)}),
    [navigate],
  );

  const router = useMemo(
    () => ({
      location,
      history,
    }),
    [location, history],
  );

  return (
    <AppBridgeProvider
    config={{ apiKey: API_KEY, host: shopifyHost}}
      router={router}
    >
       <Component/>
    </AppBridgeProvider>
  );
}

renderApp(Main);

if (module.hot) {
  module.hot.accept('./DemoPages/Main', () => {
    const NextApp = require('./DemoPages/Main').default;
    renderApp(NextApp);
  });
}
serviceWorker.unregister();
