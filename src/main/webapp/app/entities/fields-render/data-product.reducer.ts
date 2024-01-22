import axios from 'axios';
import { ICrudGetAllAction } from 'react-jhipster';

import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IProduct } from 'app/shared/model/product.model';

export const ACTION_TYPES = {
  FETCH_PRODUCT_LIST: 'product/FETCH_PRODUCT_LIST',
  RESET_PRODUCT_LIST: 'product/RESET_PRODUCT_LIST'
};

const initialState = {
  loading: false,
  errorMessage: null,
  productOptions: { products: [], pageInfo: {} } as IProduct,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ProductState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductState = initialState, action): ProductState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCT_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCT_LIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCT_LIST):
      return {
        ...state,
        loading: false,
        productOptions: action.payload.data
      };
    case ACTION_TYPES.RESET_PRODUCT_LIST:
      return {
        ...state,
        loading: false,
        productOptions: action.payload
      };
    default:
      return state;
  }
};

const apiUrl = '/api/data/products';
const externalApiUrl = '/api/data/external/v2/products';

// Actions
let cancelToken;
export const getProductOptions: ICrudGetAllAction<IProduct> = sort => {
  const requestUrl = `${apiUrl}?cursor=${sort}&cacheBuster=${new Date().getTime()}`;

  if (typeof cancelToken != typeof undefined) {
    cancelToken.cancel('Operation canceled due to new request.');
  }

  cancelToken = axios.CancelToken.source();
  return {
    type: ACTION_TYPES.FETCH_PRODUCT_LIST,
    payload: axios.get<IProduct>(requestUrl, { cancelToken: cancelToken.token })
  };
};

export const getProductOptionsExternal: ICrudGetAllAction<IProduct> = sort => {
  //   const requestUrl = `${externalApiUrl}?cursor=${sort}&cacheBuster=${new Date().getTime()}`;
  const requestUrl = `${apiUrl}?cursor=${sort}&cacheBuster=${new Date().getTime()}`;
  if (typeof cancelToken != typeof undefined) {
    cancelToken.cancel('Operation canceled due to new request.');
  }

  cancelToken = axios.CancelToken.source();
  return {
    type: ACTION_TYPES.FETCH_PRODUCT_LIST,
    payload: axios.get<IProduct>(requestUrl, { cancelToken: cancelToken.token })
  };
};

export const getProducts: ICrudGetAllAction<IProduct> = sort => {
  const requestUrl = `${apiUrl}1?${sort}&cacheBuster=${new Date().getTime()}`;
  if (typeof cancelToken != typeof undefined) {
    cancelToken.cancel('Operation canceled due to new request.');
  }

  cancelToken = axios.CancelToken.source();
  return {
    type: ACTION_TYPES.FETCH_PRODUCT_LIST,
    payload: axios.get<IProduct>(requestUrl, { cancelToken: cancelToken.token })
  };
};

export const resetProductOptions = () => {
  return {
    type: ACTION_TYPES.RESET_PRODUCT_LIST,
    payload: { products: [], pageInfo: {} }
  };
};
