import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBundleDynamicScript, defaultValue } from 'app/shared/model/bundle-dynamic-script.model';

export const ACTION_TYPES = {
  FETCH_BUNDLEDYNAMICSCRIPT_LIST: 'bundleDynamicScript/FETCH_BUNDLEDYNAMICSCRIPT_LIST',
  FETCH_BUNDLEDYNAMICSCRIPT: 'bundleDynamicScript/FETCH_BUNDLEDYNAMICSCRIPT',
  CREATE_BUNDLEDYNAMICSCRIPT: 'bundleDynamicScript/CREATE_BUNDLEDYNAMICSCRIPT',
  UPDATE_BUNDLEDYNAMICSCRIPT: 'bundleDynamicScript/UPDATE_BUNDLEDYNAMICSCRIPT',
  DELETE_BUNDLEDYNAMICSCRIPT: 'bundleDynamicScript/DELETE_BUNDLEDYNAMICSCRIPT',
  SET_BLOB: 'bundleDynamicScript/SET_BLOB',
  RESET: 'bundleDynamicScript/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBundleDynamicScript>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type BundleDynamicScriptState = Readonly<typeof initialState>;

// Reducer

export default (state: BundleDynamicScriptState = initialState, action): BundleDynamicScriptState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BUNDLEDYNAMICSCRIPT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BUNDLEDYNAMICSCRIPT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BUNDLEDYNAMICSCRIPT):
    case REQUEST(ACTION_TYPES.UPDATE_BUNDLEDYNAMICSCRIPT):
    case REQUEST(ACTION_TYPES.DELETE_BUNDLEDYNAMICSCRIPT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BUNDLEDYNAMICSCRIPT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BUNDLEDYNAMICSCRIPT):
    case FAILURE(ACTION_TYPES.CREATE_BUNDLEDYNAMICSCRIPT):
    case FAILURE(ACTION_TYPES.UPDATE_BUNDLEDYNAMICSCRIPT):
    case FAILURE(ACTION_TYPES.DELETE_BUNDLEDYNAMICSCRIPT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BUNDLEDYNAMICSCRIPT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_BUNDLEDYNAMICSCRIPT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BUNDLEDYNAMICSCRIPT):
    case SUCCESS(ACTION_TYPES.UPDATE_BUNDLEDYNAMICSCRIPT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BUNDLEDYNAMICSCRIPT):
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

const apiUrl = 'api/bundle-dynamic-scripts';

// Actions

export const getEntities: ICrudGetAllAction<IBundleDynamicScript> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_BUNDLEDYNAMICSCRIPT_LIST,
  payload: axios.get<IBundleDynamicScript>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IBundleDynamicScript> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BUNDLEDYNAMICSCRIPT,
    payload: axios.get<IBundleDynamicScript>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBundleDynamicScript> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BUNDLEDYNAMICSCRIPT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBundleDynamicScript> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BUNDLEDYNAMICSCRIPT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBundleDynamicScript> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BUNDLEDYNAMICSCRIPT,
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
