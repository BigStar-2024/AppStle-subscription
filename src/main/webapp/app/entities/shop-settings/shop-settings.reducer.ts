import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShopSettings, defaultValue } from 'app/shared/model/shop-settings.model';

export const ACTION_TYPES = {
  FETCH_SHOPSETTINGS_LIST: 'shopSettings/FETCH_SHOPSETTINGS_LIST',
  FETCH_SHOPSETTINGS: 'shopSettings/FETCH_SHOPSETTINGS',
  CREATE_SHOPSETTINGS: 'shopSettings/CREATE_SHOPSETTINGS',
  UPDATE_SHOPSETTINGS: 'shopSettings/UPDATE_SHOPSETTINGS',
  DELETE_SHOPSETTINGS: 'shopSettings/DELETE_SHOPSETTINGS',
  RESET: 'shopSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShopSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ShopSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopSettingsState = initialState, action): ShopSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHOPSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_SHOPSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_SHOPSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_SHOPSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPSETTINGS):
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

const apiUrl = 'api/shop-settings';

// Actions

export const getEntities: ICrudGetAllAction<IShopSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SHOPSETTINGS_LIST,
  payload: axios.get<IShopSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IShopSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPSETTINGS,
    payload: axios.get<IShopSettings>(requestUrl)
  };
};

export const getEntityByShop = () => {
  const requestUrl = `${apiUrl}/shop`;
  return {
    type: ACTION_TYPES.FETCH_SHOPSETTINGS,
    payload: axios.get<IShopSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShopSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPSETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
