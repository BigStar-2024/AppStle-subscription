import { API_KEY, SERVER_API_URL } from 'app/config/constants';
import axios from 'axios';
import { Storage } from 'react-jhipster';
import { IS_LOCALHOST } from './api-config';
import { getSessionToken } from '@shopify/app-bridge-utils';
import createApp from '@shopify/app-bridge';

const TIMEOUT = 1 * 60 * 1000 * 2;
axios.defaults.timeout = TIMEOUT;
let baseUrl = null;
try {
  const externalCustomerPortalToken =
    Storage.local.get('external-customer-portal-token') || Storage.session.get('external-customer-portal-token');
  const externalBundleToken = Storage.local.get('external-bundle-token') || Storage.session.get('external-bundle-token');
  const appstleAppProxyPathPrefix = window?.RSConfig?.appstle_app_proxy_path_prefix;
  const shopPublicDomain = window?.appstle_public_domain;

  if (!IS_LOCALHOST && typeof Shopify !== undefined && Shopify.shop && shopPublicDomain) {
    baseUrl = 'https://subscription-admin.appstle.com/';
    if (window['isAppstleCustomerPortal']) {
      baseUrl = `${window.top.location.origin}/apps/subscriptions/cp/`;
    }
    if (externalBundleToken && window['isAppstleBuildABox']) {
      baseUrl = `${window.top.location.origin}/apps/subscriptions/bb/`;
    }
    if (window?.appstle_app_proxy_path_prefix && window['isAppstleCustomerPortal']) {
      baseUrl = `${window.top.location.origin}/${window?.appstle_app_proxy_path_prefix}/cp/`;
    }
    if (window?.appstle_app_proxy_path_prefix && externalBundleToken && window['isAppstleBuildABox']) {
      baseUrl = `${window.top.location.origin}/${window.appstle_app_proxy_path_prefix}/bb/`;
    }
  }
} catch (error) {}
axios.defaults.baseURL = baseUrl || SERVER_API_URL;

const setupAxiosInterceptors = onUnauthenticated => {
  const onRequestSuccess = async config => {
    let sessionToken = null;

    try {
      if (!window['app'] && window.sessionStorage && sessionStorage['host']) {
        console.log("came here -> !window['app'] && sessionStorage['host']");
        window['app'] = createApp({
          apiKey: API_KEY,
          host: sessionStorage['host']
        });
      }
    } catch (e) {}

    try {
      console.log('axios host=' + window['__SHOPIFY_DEV_HOST']);
      if (!window['app'] && window['__SHOPIFY_DEV_HOST']) {
        console.log("came here -> !window['app'] && sessionStorage['host']");
        window['app'] = createApp({
          apiKey: API_KEY,
          host: window['__SHOPIFY_DEV_HOST']
        });
      }
    } catch (e) {}

    if (window['app']) {
      try {
        sessionToken = await getSessionToken(window['app']);
      } catch (e) {}
    }

    let token = null;
    try {
      token = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
    } catch (e) {}

    if (sessionToken) {
      config.headers['ShopifyAuthorization'] = `Bearer ${sessionToken}`;
    } else if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    /*const customerPortalToken = Storage.local.get('customer-portal-token') || Storage.session.get('customer-portal-token');
                if (customerPortalToken) {
                  config.headers.Authorization = `Bearer ${customerPortalToken}`;
                }

                const bundleToken = Storage.local.get('bundle-token') || Storage.session.get('bundle-token');
                if (bundleToken) {
                  config.headers.Authorization = `Bearer ${bundleToken}`;
                }*/

    try {
      const externalCustomerPortalToken =
        Storage.local.get('external-customer-portal-token') || Storage.session.get('external-customer-portal-token');
      if (externalCustomerPortalToken) {
        console.log('came here -> if (externalCustomerPortalToken)');
        config.params = {
          ...config.params,
          token: externalCustomerPortalToken
        };
      }
    } catch (e) {}

    try {
      const externalBundleToken = Storage.local.get('external-bundle-token') || Storage.session.get('external-bundle-token');
      if (externalBundleToken) {
        config.headers.ExternalAuthorization = `Bearer ${externalBundleToken}`;
      }
    } catch (e) {}

    if (IS_LOCALHOST) {
      config.headers.Username = 'appstle-jaydeep-35.myshopify.com';
      config.headers.Password = 'j500fNXr3o';
      config.headers.Shop = 'appstle-jaydeep-35.myshopify.com';
    }
    // console.log('Starting Request', JSON.stringify(config, null, 2));
    return config;
  };
  const onResponseSuccess = response => {
    // console.log('Response', JSON.stringify(response, null, 2));
    return response;
  };
  const onResponseError = err => {
    // console.log('Error Response', JSON.stringify(err, null, 2));
    const status = err?.status || err?.response?.status;
    if (status === 403 || status === 401) {
      onUnauthenticated();
    }
    return Promise.reject(err);
  };
  axios.interceptors.request.use(onRequestSuccess);
  axios.interceptors.response.use(onResponseSuccess, onResponseError);
};

export default setupAxiosInterceptors;
