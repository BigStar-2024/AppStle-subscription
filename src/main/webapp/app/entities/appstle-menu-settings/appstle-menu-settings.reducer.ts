import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IAppstleMenuSettings } from 'app/shared/model/appstle-menu-settings.model';

export const ACTION_TYPES = {
  FETCH_APPSTLEMENUSETTINGS_LIST: 'appstleMenuSettings/FETCH_APPSTLEMENUSETTINGS_LIST',
  FETCH_APPSTLEMENUSETTINGS: 'appstleMenuSettings/FETCH_APPSTLEMENUSETTINGS',
  CREATE_APPSTLEMENUSETTINGS: 'appstleMenuSettings/CREATE_APPSTLEMENUSETTINGS',
  UPDATE_APPSTLEMENUSETTINGS: 'appstleMenuSettings/UPDATE_APPSTLEMENUSETTINGS',
  DELETE_APPSTLEMENUSETTINGS: 'appstleMenuSettings/DELETE_APPSTLEMENUSETTINGS',
  SET_BLOB: 'appstleMenuSettings/SET_BLOB',
  RESET: 'appstleMenuSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAppstleMenuSettings>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AppstleMenuSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: AppstleMenuSettingsState = initialState, action): AppstleMenuSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_APPSTLEMENUSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_APPSTLEMENUSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_APPSTLEMENUSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_APPSTLEMENUSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_APPSTLEMENUSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_APPSTLEMENUSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_APPSTLEMENUSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_APPSTLEMENUSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_APPSTLEMENUSETTINGS):
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

const apiUrl = 'api/appstle-menu-settings';

// Actions

export const getEntities: ICrudGetAllAction<IAppstleMenuSettings> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS_LIST,
    payload: axios.get<IAppstleMenuSettings>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAppstleMenuSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS,
    payload: axios.get<IAppstleMenuSettings>(requestUrl)
  };
};
export const getAppstleMenuSettingsByShop: ICrudGetAction<IAppstleMenuSettings> = id => {
  const requestUrl = `${apiUrl}/by-shop`;
  return {
    type: ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS,
    payload: axios.get<IAppstleMenuSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAppstleMenuSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_APPSTLEMENUSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAppstleMenuSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_APPSTLEMENUSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAppstleMenuSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_APPSTLEMENUSETTINGS,
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
