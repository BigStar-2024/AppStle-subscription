import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProcessedOrderInfo, defaultValue } from 'app/shared/model/processed-order-info.model';

export const ACTION_TYPES = {
  FETCH_PROCESSEDORDERINFO_LIST: 'processedOrderInfo/FETCH_PROCESSEDORDERINFO_LIST',
  FETCH_PROCESSEDORDERINFO: 'processedOrderInfo/FETCH_PROCESSEDORDERINFO',
  CREATE_PROCESSEDORDERINFO: 'processedOrderInfo/CREATE_PROCESSEDORDERINFO',
  UPDATE_PROCESSEDORDERINFO: 'processedOrderInfo/UPDATE_PROCESSEDORDERINFO',
  DELETE_PROCESSEDORDERINFO: 'processedOrderInfo/DELETE_PROCESSEDORDERINFO',
  SET_BLOB: 'processedOrderInfo/SET_BLOB',
  RESET: 'processedOrderInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProcessedOrderInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
  totalItems: 0
};

export type ProcessedOrderInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: ProcessedOrderInfoState = initialState, action): ProcessedOrderInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PROCESSEDORDERINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROCESSEDORDERINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PROCESSEDORDERINFO):
    case REQUEST(ACTION_TYPES.UPDATE_PROCESSEDORDERINFO):
    case REQUEST(ACTION_TYPES.DELETE_PROCESSEDORDERINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PROCESSEDORDERINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROCESSEDORDERINFO):
    case FAILURE(ACTION_TYPES.CREATE_PROCESSEDORDERINFO):
    case FAILURE(ACTION_TYPES.UPDATE_PROCESSEDORDERINFO):
    case FAILURE(ACTION_TYPES.DELETE_PROCESSEDORDERINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROCESSEDORDERINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROCESSEDORDERINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROCESSEDORDERINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_PROCESSEDORDERINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROCESSEDORDERINFO):
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

const apiUrl = 'api/processed-order-infos';

// Actions

export const getEntities: ICrudGetAllAction<IProcessedOrderInfo> = (page, size, sort) => {
  const requestUrl = `${apiUrl}?page=${page}&size=${size}&sort=${sort}&cacheBuster=${new Date().getTime()}`;
  return {
    type: ACTION_TYPES.FETCH_PROCESSEDORDERINFO_LIST,
    payload: axios.get<IProcessedOrderInfo>(requestUrl)
  };
};

export const getEntity: ICrudGetAction<IProcessedOrderInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROCESSEDORDERINFO,
    payload: axios.get<IProcessedOrderInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProcessedOrderInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROCESSEDORDERINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProcessedOrderInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROCESSEDORDERINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProcessedOrderInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROCESSEDORDERINFO,
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
