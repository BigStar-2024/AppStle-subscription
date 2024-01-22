import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductSwap, defaultValue } from 'app/shared/model/product-swap.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTSWAP_LIST: 'productSwap/FETCH_PRODUCTSWAP_LIST',
  FETCH_PRODUCTSWAP: 'productSwap/FETCH_PRODUCTSWAP',
  CREATE_PRODUCTSWAP: 'productSwap/CREATE_PRODUCTSWAP',
  UPDATE_PRODUCTSWAP: 'productSwap/UPDATE_PRODUCTSWAP',
  DELETE_PRODUCTSWAP: 'productSwap/DELETE_PRODUCTSWAP',
  SET_BLOB: 'productSwap/SET_BLOB',
  RESET: 'productSwap/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductSwap>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ProductSwapState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductSwapState = initialState, action): ProductSwapState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTSWAP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTSWAP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTSWAP):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTSWAP):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTSWAP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTSWAP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTSWAP):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTSWAP):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTSWAP):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTSWAP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTSWAP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTSWAP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTSWAP):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTSWAP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTSWAP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
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
    default:
      return state;
  }
};

const apiUrl = 'api/product-swaps';

// Actions

export const getEntities: ICrudGetAllAction<IProductSwap> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PRODUCTSWAP_LIST,
  payload: axios.get<IProductSwap>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IProductSwap> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTSWAP,
    payload: axios.get<IProductSwap>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProductSwap> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTSWAP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductSwap> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTSWAP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductSwap> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTSWAP,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
