import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAppstleMenuLabels, defaultValue } from 'app/shared/model/appstle-menu-labels.model';

export const ACTION_TYPES = {
  FETCH_APPSTLEMENULABELS_LIST: 'appstleMenuLabels/FETCH_APPSTLEMENULABELS_LIST',
  FETCH_APPSTLEMENULABELS: 'appstleMenuLabels/FETCH_APPSTLEMENULABELS',
  CREATE_APPSTLEMENULABELS: 'appstleMenuLabels/CREATE_APPSTLEMENULABELS',
  UPDATE_APPSTLEMENULABELS: 'appstleMenuLabels/UPDATE_APPSTLEMENULABELS',
  DELETE_APPSTLEMENULABELS: 'appstleMenuLabels/DELETE_APPSTLEMENULABELS',
  SET_BLOB: 'appstleMenuLabels/SET_BLOB',
  RESET: 'appstleMenuLabels/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAppstleMenuLabels>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AppstleMenuLabelsState = Readonly<typeof initialState>;

// Reducer

export default (state: AppstleMenuLabelsState = initialState, action): AppstleMenuLabelsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_APPSTLEMENULABELS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPSTLEMENULABELS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_APPSTLEMENULABELS):
    case REQUEST(ACTION_TYPES.UPDATE_APPSTLEMENULABELS):
    case REQUEST(ACTION_TYPES.DELETE_APPSTLEMENULABELS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_APPSTLEMENULABELS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPSTLEMENULABELS):
    case FAILURE(ACTION_TYPES.CREATE_APPSTLEMENULABELS):
    case FAILURE(ACTION_TYPES.UPDATE_APPSTLEMENULABELS):
    case FAILURE(ACTION_TYPES.DELETE_APPSTLEMENULABELS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPSTLEMENULABELS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPSTLEMENULABELS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_APPSTLEMENULABELS):
    case SUCCESS(ACTION_TYPES.UPDATE_APPSTLEMENULABELS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_APPSTLEMENULABELS):
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

const apiUrl = 'api/appstle-menu-labels';

// Actions

export const getEntities: ICrudGetAllAction<IAppstleMenuLabels> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_APPSTLEMENULABELS_LIST,
    payload: axios.get<IAppstleMenuLabels>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAppstleMenuLabels> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_APPSTLEMENULABELS,
    payload: axios.get<IAppstleMenuLabels>(requestUrl)
  };
};

export const getAppstleMenuLabelsByShop: ICrudGetAction<IAppstleMenuLabels> = () => {
  const requestUrl = `${apiUrl}/shop`;
  return {
    type: ACTION_TYPES.FETCH_APPSTLEMENULABELS,
    payload: axios.get<IAppstleMenuLabels>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAppstleMenuLabels> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_APPSTLEMENULABELS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAppstleMenuLabels> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_APPSTLEMENULABELS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAppstleMenuLabels> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_APPSTLEMENULABELS,
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
