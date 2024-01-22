import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBulkAutomation, defaultValue } from 'app/shared/model/bulk-automation.model';

export const ACTION_TYPES = {
  FETCH_BULKAUTOMATION_LIST: 'bulkAutomation/FETCH_BULKAUTOMATION_LIST',
  FETCH_BULKAUTOMATION: 'bulkAutomation/FETCH_BULKAUTOMATION',
  CREATE_BULKAUTOMATION: 'bulkAutomation/CREATE_BULKAUTOMATION',
  UPDATE_BULKAUTOMATION: 'bulkAutomation/UPDATE_BULKAUTOMATION',
  DELETE_BULKAUTOMATION: 'bulkAutomation/DELETE_BULKAUTOMATION',
  SET_BLOB: 'bulkAutomation/SET_BLOB',
  RESET: 'bulkAutomation/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBulkAutomation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type BulkAutomationState = Readonly<typeof initialState>;

// Reducer

export default (state: BulkAutomationState = initialState, action): BulkAutomationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BULKAUTOMATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BULKAUTOMATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BULKAUTOMATION):
    case REQUEST(ACTION_TYPES.UPDATE_BULKAUTOMATION):
    case REQUEST(ACTION_TYPES.DELETE_BULKAUTOMATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BULKAUTOMATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BULKAUTOMATION):
    case FAILURE(ACTION_TYPES.CREATE_BULKAUTOMATION):
    case FAILURE(ACTION_TYPES.UPDATE_BULKAUTOMATION):
    case FAILURE(ACTION_TYPES.DELETE_BULKAUTOMATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BULKAUTOMATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_BULKAUTOMATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BULKAUTOMATION):
    case SUCCESS(ACTION_TYPES.UPDATE_BULKAUTOMATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BULKAUTOMATION):
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

const apiUrl = 'api/bulk-automations';

// Actions

export const getEntities: ICrudGetAllAction<IBulkAutomation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BULKAUTOMATION_LIST,
    payload: axios.get<IBulkAutomation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IBulkAutomation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BULKAUTOMATION,
    payload: axios.get<IBulkAutomation>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBulkAutomation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BULKAUTOMATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBulkAutomation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BULKAUTOMATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBulkAutomation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BULKAUTOMATION,
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
