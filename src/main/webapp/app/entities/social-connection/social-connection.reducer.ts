import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISocialConnection, defaultValue } from 'app/shared/model/social-connection.model';

export const ACTION_TYPES = {
  FETCH_SOCIALCONNECTION_LIST: 'socialConnection/FETCH_SOCIALCONNECTION_LIST',
  FETCH_SOCIALCONNECTION: 'socialConnection/FETCH_SOCIALCONNECTION',
  CREATE_SOCIALCONNECTION: 'socialConnection/CREATE_SOCIALCONNECTION',
  UPDATE_SOCIALCONNECTION: 'socialConnection/UPDATE_SOCIALCONNECTION',
  DELETE_SOCIALCONNECTION: 'socialConnection/DELETE_SOCIALCONNECTION',
  RESET: 'socialConnection/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISocialConnection>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SocialConnectionState = Readonly<typeof initialState>;

// Reducer

export default (state: SocialConnectionState = initialState, action): SocialConnectionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SOCIALCONNECTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SOCIALCONNECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SOCIALCONNECTION):
    case REQUEST(ACTION_TYPES.UPDATE_SOCIALCONNECTION):
    case REQUEST(ACTION_TYPES.DELETE_SOCIALCONNECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SOCIALCONNECTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SOCIALCONNECTION):
    case FAILURE(ACTION_TYPES.CREATE_SOCIALCONNECTION):
    case FAILURE(ACTION_TYPES.UPDATE_SOCIALCONNECTION):
    case FAILURE(ACTION_TYPES.DELETE_SOCIALCONNECTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SOCIALCONNECTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SOCIALCONNECTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SOCIALCONNECTION):
    case SUCCESS(ACTION_TYPES.UPDATE_SOCIALCONNECTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SOCIALCONNECTION):
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

const apiUrl = 'api/social-connections';

// Actions

export const getEntities: ICrudGetAllAction<ISocialConnection> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SOCIALCONNECTION_LIST,
    payload: axios.get<ISocialConnection>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISocialConnection> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SOCIALCONNECTION,
    payload: axios.get<ISocialConnection>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISocialConnection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SOCIALCONNECTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISocialConnection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SOCIALCONNECTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISocialConnection> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SOCIALCONNECTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
