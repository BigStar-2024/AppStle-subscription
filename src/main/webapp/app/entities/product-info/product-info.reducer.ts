import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductInfo, defaultValue } from 'app/shared/model/product-info.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTINFO_LIST: 'productInfo/FETCH_PRODUCTINFO_LIST',
  FETCH_PRODUCTINFO: 'productInfo/FETCH_PRODUCTINFO',
  FETCH_PRODUCT_FILTER_DATA: 'productInfo/FETCH_PRODUCT_FILTER_DATA',
  CREATE_PRODUCTINFO: 'productInfo/CREATE_PRODUCTINFO',
  UPDATE_PRODUCTINFO: 'productInfo/UPDATE_PRODUCTINFO',
  DELETE_PRODUCTINFO: 'productInfo/DELETE_PRODUCTINFO',
  RESET: 'productInfo/RESET'
};

const initialState = {
  loading: false,
  loadingProductFilter: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductInfo>,
  entity: defaultValue,
  productFilterData: null,
  updating: false,
  updateSuccess: false
};

export type ProductInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductInfoState = initialState, action): ProductInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCT_FILTER_DATA):
      return {
        ...state,
        loadingProductFilter: true
      };
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTINFO):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTINFO):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCT_FILTER_DATA):
      return {
        ...state,
        loadingProductFilter: false
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTINFO):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTINFO):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTINFO):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCT_FILTER_DATA):
      return {
        ...state,
        productFilterData: action.payload.data,
        loadingProductFilter: false
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/product-infos';

// Actions

export const getEntities: ICrudGetAllAction<IProductInfo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PRODUCTINFO_LIST,
  payload: axios.get<IProductInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IProductInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTINFO,
    payload: axios.get<IProductInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProductInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTINFO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const getProductFilterData = () => async dispatch => {
  const requestUrl = `${apiUrl}/product-filter-data`;
  const result = await dispatch({
    type: ACTION_TYPES.FETCH_PRODUCT_FILTER_DATA,
    payload: axios.get(requestUrl)
  });
  return result;
};
