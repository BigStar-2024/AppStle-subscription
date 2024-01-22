import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBundleSetting, defaultValue } from 'app/shared/model/bundle-setting.model';

export const ACTION_TYPES = {
  FETCH_BUNDLESETTING_LIST: 'bundleSetting/FETCH_BUNDLESETTING_LIST',
  FETCH_BUNDLESETTING: 'bundleSetting/FETCH_BUNDLESETTING',
  FETCH_BUNDLESETTING_BY_SHOP: 'bundleSetting/FETCH_BUNDLESETTING_BY_SHOP',
  CREATE_BUNDLESETTING: 'bundleSetting/CREATE_BUNDLESETTING',
  UPDATE_BUNDLESETTING: 'bundleSetting/UPDATE_BUNDLESETTING',
  DELETE_BUNDLESETTING: 'bundleSetting/DELETE_BUNDLESETTING',
  RESET: 'bundleSetting/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBundleSetting>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type BundleSettingState = Readonly<typeof initialState>;

// Reducer

export default (state: BundleSettingState = initialState, action): BundleSettingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BUNDLESETTING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BUNDLESETTING):
    case REQUEST(ACTION_TYPES.FETCH_BUNDLESETTING_BY_SHOP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BUNDLESETTING):
    case REQUEST(ACTION_TYPES.UPDATE_BUNDLESETTING):
    case REQUEST(ACTION_TYPES.DELETE_BUNDLESETTING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BUNDLESETTING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BUNDLESETTING):
    case FAILURE(ACTION_TYPES.CREATE_BUNDLESETTING):
    case FAILURE(ACTION_TYPES.UPDATE_BUNDLESETTING):
    case FAILURE(ACTION_TYPES.DELETE_BUNDLESETTING):
    case FAILURE(ACTION_TYPES.FETCH_BUNDLESETTING_BY_SHOP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BUNDLESETTING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_BUNDLESETTING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };

    case SUCCESS(ACTION_TYPES.FETCH_BUNDLESETTING_BY_SHOP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BUNDLESETTING):
    case SUCCESS(ACTION_TYPES.UPDATE_BUNDLESETTING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BUNDLESETTING):
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

const apiUrl = 'api/bundle-settings';

// Actions

export const getEntities: ICrudGetAllAction<IBundleSetting> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BUNDLESETTING_LIST,
    payload: axios.get<IBundleSetting>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IBundleSetting> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BUNDLESETTING,
    payload: axios.get<IBundleSetting>(requestUrl)
  };
};
export const getBundleSettingsByShop: ICrudGetAction<IBundleSetting> = id => {
  const requestUrl = `${apiUrl}/by-shop`;
  return {
    type: ACTION_TYPES.FETCH_BUNDLESETTING_BY_SHOP,
    payload: axios.get<IBundleSetting>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBundleSetting> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BUNDLESETTING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBundleSetting> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BUNDLESETTING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBundleSetting> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BUNDLESETTING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
