import { isPromise } from 'react-jhipster';
import { toast } from 'react-toastify';

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER,
};

export const addErrorAlert = (message, key?, data?) => {
  toast.error(message, options);
};
export default () => next => action => {
  // If not a promise, continue on
  if (!isPromise(action.payload)) {
    return next(action);
  }

  /**
   *
   * The notification middleware serves to dispatch the initial pending promise to
   * the promise middleware, but adds a `then` and `catch.
   */
  return next(action)
    .then(response => {
      if (action.meta && action.meta.successMessage) {
        toast.success(action.meta.successMessage, options);
      } else if (response && response.action && response.action.payload && response.action.payload.headers) {
        const headers = response.action.payload.headers;
        let alert: string = null;
        Object.entries(headers).forEach(([k, v]: [string, string]) => {
          if (k.toLowerCase().endsWith('app-alert')) {
            alert = v;
          }
        });
        if (alert) {
          toast.success(alert, options);
        }
      }
      return Promise.resolve(response);
    })
    .catch(error => {
      if (action.meta && action.meta.errorMessage) {
        toast.error(action.meta.errorMessage, options);
      } else if (error && error.response) {
        const response = error.response;
        const data = response.data;
        if (!(response.status === 401 && (error.message === '' || (data && data.path && data.path.includes('/api/account'))))) {
          let i;
          switch (response.status) {
            // connection refused, server not reachable
            case 0:
              addErrorAlert('Server not reachable', 'error.server.not.reachable');
              break;

            case 400: {
              const headers = Object.entries(response.headers);
              let errorHeader = null;
              let entityKey = null;
              headers.forEach(([k, v]: [string, string]) => {
                if (k.toLowerCase().endsWith('app-error')) {
                  errorHeader = v;
                } else if (k.toLowerCase().endsWith('app-params')) {
                  entityKey = v;
                }
              });
              if (errorHeader) {
                const entityName = entityKey;
                addErrorAlert(errorHeader, errorHeader, { entityName });
              } else if (data !== '' && data.fieldErrors) {
                const fieldErrors = data.fieldErrors;
                for (i = 0; i < fieldErrors.length; i++) {
                  const fieldError = fieldErrors[i];
                  if (['Min', 'Max', 'DecimalMin', 'DecimalMax'].includes(fieldError.message)) {
                    fieldError.message = 'Size';
                  }
                  // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
                  const convertedField = fieldError.field.replace(/\[\d*\]/g, '[]');
                  const fieldName = convertedField.charAt(0).toUpperCase() + convertedField.slice(1);
                  addErrorAlert(`Error on field "${fieldName}"`, `error.${fieldError.message}`, { fieldName });
                }
              } else if (data !== '' && data.message === 'error.customerTokenExpired') {
              } else if (data !== '' && data.message) {
                addErrorAlert(data.message, data.message, data.params);
              } else {
                addErrorAlert(data);
              }
              break;
            }
            case 404:
              if (!error.config.url.match('api/subscription-contract-details/customer/')) {
                addErrorAlert('Not found', 'error.url.not.found');
              }
              break;

            case 502:
              addErrorAlert('Code Deployment in progress. Please refresh. It will not impact anything in the store front.');
              break;

            default:
              if (data !== '' && data.message) {
                addErrorAlert(data.message);
              } else {
                addErrorAlert(data);
              }
          }
        }
      } else if (error && error.config && error.config.url === 'api/account' && error.config.method === 'get') {
        /* eslint-disable no-console */
        console.log('Authentication Error: Trying to access url api/account with GET.');
      } else if (error && error.message === 'Operation canceled due to new request.') {
      } else if (error && error.message) {
        toast.error(error.message, options);
      } else {
        toast.error('Unknown error!', options);
      }
      return Promise.reject(error);
    });
};
