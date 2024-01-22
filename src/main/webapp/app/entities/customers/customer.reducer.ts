import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomers, defaultValue } from 'app/shared/model/customer.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMER_LIST: 'customer/FETCH_CUSTOMER_LIST',
  FETCH_CUSTOMER: 'customer/FETCH_CUSTOMER',
  CREATE_CUSTOMER: 'customer/CREATE_CUSTOMER',
  UPDATE_CUSTOMER: 'customer/UPDATE_CUSTOMER',
  DELETE_CUSTOMER: 'customer/DELETE_CUSTOMER',
  SET_BLOB: 'customer/SET_BLOB',
  RESET: 'customer/RESET',
  FETCH_SHOPIFY_CUSTOMER_BY_SEARCH: 'customer/FETCH_SHOPIFY_CUSTOMER_BY_SEARCH',
  GET_SHOPIFY_CUSTOMER_DETAILS: 'customer/GET_SHOPIFY_CUSTOMER_DETAILS',
  GET_SHOPIFY_CUSTOMER_PAYMENT_DETAILS: 'customer/GET_SHOPIFY_CUSTOMER_PAYMENT_DETAILS',
  CUSTOMER_DETAILS_RESET: 'customer/CUSTOMER_DETAILS_RESET',
  CUSTOMER_SHPPING_COUNTRY_CODES: 'customer/COUNTRY_CODES',
  FETCH_VALID_CUSTOMER: 'customer/FETCH_VALID_CUSTOMER'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomers>,
  entity: JSON.parse(JSON.stringify(defaultValue)),
  updating: false,
  updateSuccess: false,
  customer: { customer: [], pageInfo: {} },
  customerDetails: {},
  customerPaymentDetails: {},
  countriesCodes: [],
  showRedirectMessage: false,
  validContractId: []
};

export type CustomerState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerState = initialState, action): CustomerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMER):
    case REQUEST(ACTION_TYPES.CUSTOMER_SHPPING_COUNTRY_CODES):
    case REQUEST(ACTION_TYPES.FETCH_SHOPIFY_CUSTOMER_BY_SEARCH):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMER):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMER):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMER):
    case REQUEST(ACTION_TYPES.GET_SHOPIFY_CUSTOMER_DETAILS):
    case REQUEST(ACTION_TYPES.GET_SHOPIFY_CUSTOMER_PAYMENT_DETAILS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMER_LIST):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMER):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMER):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMER):
    case FAILURE(ACTION_TYPES.CUSTOMER_SHPPING_COUNTRY_CODES):
    case FAILURE(ACTION_TYPES.FETCH_SHOPIFY_CUSTOMER_BY_SEARCH):
    case FAILURE(ACTION_TYPES.GET_SHOPIFY_CUSTOMER_DETAILS):
    case FAILURE(ACTION_TYPES.GET_SHOPIFY_CUSTOMER_PAYMENT_DETAILS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMER):
      return {
        ...state,
        loading: false,
        showRedirectMessage: true
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMER):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CUSTOMER_SHPPING_COUNTRY_CODES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        countriesCodes: action.payload.data
      };

    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPIFY_CUSTOMER_BY_SEARCH):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: true,
        customer: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.GET_SHOPIFY_CUSTOMER_DETAILS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        customerDetails: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.GET_SHOPIFY_CUSTOMER_PAYMENT_DETAILS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        customerPaymentDetails: action.payload.data
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    case ACTION_TYPES.CUSTOMER_DETAILS_RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/subscription-customers';

// Actions

export const getEntities: ICrudGetAllAction<ICustomers> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CUSTOMER_LIST,
  payload: axios.get<ICustomers>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});
export const getShipingCountryCodes = () => ({
  type: ACTION_TYPES.CUSTOMER_SHPPING_COUNTRY_CODES,
  payload: axios.get(`/api/data/countries`)
});
export const getEntity: ICrudGetAction<ICustomers> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMER,
    payload: axios.get<ICustomers>(requestUrl)
  };
};

export const getCustomerPortalEntity: ICrudGetAction<ICustomers> = (id, api_key?) => {
  // const requestUrl = `api/external/v2/subscription-customers/${id}?api_key=${window?.appstle_api_key}`;
  const requestUrl = `api/subscription-customers/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMER,
    payload: axios.get<ICustomers>(requestUrl)
  };
};

export const getvalidCustomerEntity: ICrudGetAction<ICustomers> = (id, apiKey?) => {
  // const requestUrl = `api/external/v2/subscription-customers/valid/${id}?api_key=${window?.appstle_api_key}`;
  const requestUrl = `api/subscription-customers/valid/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMER,
    payload: axios.get<ICustomers>(requestUrl)
  };
};

// export const createEntity: ICrudPutAction<ICustomers> = entity => async dispatch => {
//   const result = await dispatch({
//     type: ACTION_TYPES.CREATE_CUSTOMER,
//     payload: axios.post(apiUrl, cleanEntity(entity))
//   });
//   dispatch(getEntities());
//   return result;
// };

// export const updateEntity: ICrudPutAction<ICustomers> = entity => async dispatch => {
//   const result = await dispatch({
//     type: ACTION_TYPES.UPDATE_CUSTOMER,
//     payload: axios.put(apiUrl, cleanEntity(entity))
//   });
//   return result;
// };

// export const deleteEntity: ICrudDeleteAction<ICustomers> = id => async dispatch => {
//   const requestUrl = `${apiUrl}/${id}`;
//   const result = await dispatch({
//     type: ACTION_TYPES.DELETE_CUSTOMER,
//     payload: axios.delete(requestUrl)
//   });
//   dispatch(getEntities());
//   return result;
// };

// export const setBlob = (name, data, contentType?) => ({
//   type: ACTION_TYPES.SET_BLOB,
//   payload: {
//     name,
//     data,
//     contentType
//   }
// });

// export const reset = () => ({
//   type: ACTION_TYPES.RESET
// });

export const customerDetailsReset = () => ({
  type: ACTION_TYPES.CUSTOMER_DETAILS_RESET
});

// Actions
// let cancelToken;
export const getShopifyCustomerBySearch: ICrudGetAllAction<ICustomers> = sort => {
  const requestUrl = `/api/subscription-contract-details/shopify/customer${sort}`;
  // if (typeof cancelToken != typeof undefined) {
  //   cancelToken.cancel('Operation canceled due to new request.');
  // }
  // cancelToken = axios.CancelToken.source();
  return {
    type: ACTION_TYPES.FETCH_SHOPIFY_CUSTOMER_BY_SEARCH,
    payload: axios.get<ICustomers>(requestUrl)
  };
};
export const getShopifyCustomerDetails = sort => {
  const requestUrl = `/api/subscription-contract-details/shopify/customer${sort}`;
  return {
    type: ACTION_TYPES.GET_SHOPIFY_CUSTOMER_DETAILS,
    payload: axios.get(requestUrl)
  };
};
export const getShopifyCustomerPaymentDetails = sort => {
  const requestUrl = `/api/subscription-contract-details/shopify/customer${sort}`;
  return {
    type: ACTION_TYPES.GET_SHOPIFY_CUSTOMER_PAYMENT_DETAILS,
    payload: axios.get(requestUrl)
  };
};
