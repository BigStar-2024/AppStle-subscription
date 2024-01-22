import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IThemeCode, defaultValue } from 'app/shared/model/theme-code.model';

export const ACTION_TYPES = {
  FETCH_THEMECODE_LIST: 'themeCode/FETCH_THEMECODE_LIST',
  FETCH_THEMECODE_LIST_LITE: 'themeCode/FETCH_THEMECODE_LIST_LITE',
  FETCH_THEMECODE: 'themeCode/FETCH_THEMECODE',
  CREATE_THEMECODE: 'themeCode/CREATE_THEMECODE',
  UPDATE_THEMECODE: 'themeCode/UPDATE_THEMECODE',
  DELETE_THEMECODE: 'themeCode/DELETE_THEMECODE',
  SET_BLOB: 'themeCode/SET_BLOB',
  RESET: 'themeCode/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IThemeCode>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ThemeCodeState = Readonly<typeof initialState>;

// Reducer

export default (state: ThemeCodeState = initialState, action): ThemeCodeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_THEMECODE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_THEMECODE_LIST_LITE):
    case REQUEST(ACTION_TYPES.FETCH_THEMECODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_THEMECODE):
    case REQUEST(ACTION_TYPES.UPDATE_THEMECODE):
    case REQUEST(ACTION_TYPES.DELETE_THEMECODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_THEMECODE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_THEMECODE_LIST_LITE):
    case FAILURE(ACTION_TYPES.FETCH_THEMECODE):
    case FAILURE(ACTION_TYPES.CREATE_THEMECODE):
    case FAILURE(ACTION_TYPES.UPDATE_THEMECODE):
    case FAILURE(ACTION_TYPES.DELETE_THEMECODE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_THEMECODE_LIST):
    case SUCCESS(ACTION_TYPES.FETCH_THEMECODE_LIST_LITE):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_THEMECODE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_THEMECODE):
    case SUCCESS(ACTION_TYPES.UPDATE_THEMECODE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_THEMECODE):
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

const apiUrl = 'api/theme-codes';

// Actions

export const getEntities: ICrudGetAllAction<IThemeCode> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_THEMECODE_LIST,
  payload: axios.get<IThemeCode>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntitiesLite: ICrudGetAllAction<IThemeCode> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_THEMECODE_LIST_LITE,
  payload: axios.get<IThemeCode>(`${apiUrl}-lite?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IThemeCode> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_THEMECODE,
    payload: axios.get<IThemeCode>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IThemeCode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_THEMECODE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IThemeCode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_THEMECODE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IThemeCode> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_THEMECODE,
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
