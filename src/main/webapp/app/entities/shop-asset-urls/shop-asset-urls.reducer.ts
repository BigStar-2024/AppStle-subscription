import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShopAssetUrls, defaultValue } from 'app/shared/model/shop-asset-urls.model';

export const ACTION_TYPES = {
  FETCH_SHOPASSETURLS_LIST: 'shopAssetUrls/FETCH_SHOPASSETURLS_LIST',
  FETCH_SHOPASSETURLS: 'shopAssetUrls/FETCH_SHOPASSETURLS',
  CREATE_SHOPASSETURLS: 'shopAssetUrls/CREATE_SHOPASSETURLS',
  UPDATE_SHOPASSETURLS: 'shopAssetUrls/UPDATE_SHOPASSETURLS',
  DELETE_SHOPASSETURLS: 'shopAssetUrls/DELETE_SHOPASSETURLS',
  RESET: 'shopAssetUrls/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShopAssetUrls>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ShopAssetUrlsState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopAssetUrlsState = initialState, action): ShopAssetUrlsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHOPASSETURLS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPASSETURLS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPASSETURLS):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPASSETURLS):
    case REQUEST(ACTION_TYPES.DELETE_SHOPASSETURLS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPASSETURLS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPASSETURLS):
    case FAILURE(ACTION_TYPES.CREATE_SHOPASSETURLS):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPASSETURLS):
    case FAILURE(ACTION_TYPES.DELETE_SHOPASSETURLS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPASSETURLS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPASSETURLS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPASSETURLS):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPASSETURLS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPASSETURLS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/shop-asset-urls';

// Actions

export const getEntities: ICrudGetAllAction<IShopAssetUrls> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SHOPASSETURLS_LIST,
  payload: axios.get<IShopAssetUrls>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IShopAssetUrls> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPASSETURLS,
    payload: axios.get<IShopAssetUrls>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopAssetUrls> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPASSETURLS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopAssetUrls> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPASSETURLS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShopAssetUrls> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPASSETURLS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
