import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAsyncUpdateEventProcessing, defaultValue } from 'app/shared/model/async-update-event-processing.model';

export const ACTION_TYPES = {
  FETCH_ASYNCUPDATEEVENTPROCESSING_LIST: 'asyncUpdateEventProcessing/FETCH_ASYNCUPDATEEVENTPROCESSING_LIST',
  FETCH_ASYNCUPDATEEVENTPROCESSING: 'asyncUpdateEventProcessing/FETCH_ASYNCUPDATEEVENTPROCESSING',
  CREATE_ASYNCUPDATEEVENTPROCESSING: 'asyncUpdateEventProcessing/CREATE_ASYNCUPDATEEVENTPROCESSING',
  UPDATE_ASYNCUPDATEEVENTPROCESSING: 'asyncUpdateEventProcessing/UPDATE_ASYNCUPDATEEVENTPROCESSING',
  DELETE_ASYNCUPDATEEVENTPROCESSING: 'asyncUpdateEventProcessing/DELETE_ASYNCUPDATEEVENTPROCESSING',
  SET_BLOB: 'asyncUpdateEventProcessing/SET_BLOB',
  RESET: 'asyncUpdateEventProcessing/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAsyncUpdateEventProcessing>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AsyncUpdateEventProcessingState = Readonly<typeof initialState>;

// Reducer

export default (state: AsyncUpdateEventProcessingState = initialState, action): AsyncUpdateEventProcessingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ASYNCUPDATEEVENTPROCESSING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ASYNCUPDATEEVENTPROCESSING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ASYNCUPDATEEVENTPROCESSING):
    case REQUEST(ACTION_TYPES.UPDATE_ASYNCUPDATEEVENTPROCESSING):
    case REQUEST(ACTION_TYPES.DELETE_ASYNCUPDATEEVENTPROCESSING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ASYNCUPDATEEVENTPROCESSING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ASYNCUPDATEEVENTPROCESSING):
    case FAILURE(ACTION_TYPES.CREATE_ASYNCUPDATEEVENTPROCESSING):
    case FAILURE(ACTION_TYPES.UPDATE_ASYNCUPDATEEVENTPROCESSING):
    case FAILURE(ACTION_TYPES.DELETE_ASYNCUPDATEEVENTPROCESSING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ASYNCUPDATEEVENTPROCESSING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ASYNCUPDATEEVENTPROCESSING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ASYNCUPDATEEVENTPROCESSING):
    case SUCCESS(ACTION_TYPES.UPDATE_ASYNCUPDATEEVENTPROCESSING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ASYNCUPDATEEVENTPROCESSING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/async-update-event-processings';

// Actions

export const getEntities: ICrudGetAllAction<IAsyncUpdateEventProcessing> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ASYNCUPDATEEVENTPROCESSING_LIST,
    payload: axios.get<IAsyncUpdateEventProcessing>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAsyncUpdateEventProcessing> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ASYNCUPDATEEVENTPROCESSING,
    payload: axios.get<IAsyncUpdateEventProcessing>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAsyncUpdateEventProcessing> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ASYNCUPDATEEVENTPROCESSING,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAsyncUpdateEventProcessing> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ASYNCUPDATEEVENTPROCESSING,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAsyncUpdateEventProcessing> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ASYNCUPDATEEVENTPROCESSING,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
