import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomerProcessedOrderInfo, defaultValue } from 'app/shared/model/customer-processed-order-info.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMERPROCESSEDORDERINFO_LIST: 'customerProcessedOrderInfo/FETCH_CUSTOMERPROCESSEDORDERINFO_LIST',
  FETCH_CUSTOMERPROCESSEDORDERINFO: 'customerProcessedOrderInfo/FETCH_CUSTOMERPROCESSEDORDERINFO',
  CREATE_CUSTOMERPROCESSEDORDERINFO: 'customerProcessedOrderInfo/CREATE_CUSTOMERPROCESSEDORDERINFO',
  UPDATE_CUSTOMERPROCESSEDORDERINFO: 'customerProcessedOrderInfo/UPDATE_CUSTOMERPROCESSEDORDERINFO',
  DELETE_CUSTOMERPROCESSEDORDERINFO: 'customerProcessedOrderInfo/DELETE_CUSTOMERPROCESSEDORDERINFO',
  SET_BLOB: 'customerProcessedOrderInfo/SET_BLOB',
  RESET: 'customerProcessedOrderInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerProcessedOrderInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CustomerProcessedOrderInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerProcessedOrderInfoState = initialState, action): CustomerProcessedOrderInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERPROCESSEDORDERINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERPROCESSEDORDERINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERPROCESSEDORDERINFO):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERPROCESSEDORDERINFO):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERPROCESSEDORDERINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERPROCESSEDORDERINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERPROCESSEDORDERINFO):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERPROCESSEDORDERINFO):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERPROCESSEDORDERINFO):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERPROCESSEDORDERINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERPROCESSEDORDERINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERPROCESSEDORDERINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERPROCESSEDORDERINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERPROCESSEDORDERINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERPROCESSEDORDERINFO):
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

const apiUrl = 'api/customer-processed-order-infos';

// Actions

export const getEntities: ICrudGetAllAction<ICustomerProcessedOrderInfo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CUSTOMERPROCESSEDORDERINFO_LIST,
  payload: axios.get<ICustomerProcessedOrderInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICustomerProcessedOrderInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERPROCESSEDORDERINFO,
    payload: axios.get<ICustomerProcessedOrderInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomerProcessedOrderInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERPROCESSEDORDERINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerProcessedOrderInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERPROCESSEDORDERINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerProcessedOrderInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERPROCESSEDORDERINFO,
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
