import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrderInfo, defaultValue } from 'app/shared/model/order-info.model';

export const ACTION_TYPES = {
  FETCH_ORDERINFO_LIST: 'orderInfo/FETCH_ORDERINFO_LIST',
  FETCH_ORDERINFO: 'orderInfo/FETCH_ORDERINFO',
  CREATE_ORDERINFO: 'orderInfo/CREATE_ORDERINFO',
  UPDATE_ORDERINFO: 'orderInfo/UPDATE_ORDERINFO',
  DELETE_ORDERINFO: 'orderInfo/DELETE_ORDERINFO',
  SET_BLOB: 'orderInfo/SET_BLOB',
  RESET: 'orderInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrderInfo>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OrderInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: OrderInfoState = initialState, action): OrderInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ORDERINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORDERINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ORDERINFO):
    case REQUEST(ACTION_TYPES.UPDATE_ORDERINFO):
    case REQUEST(ACTION_TYPES.DELETE_ORDERINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ORDERINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORDERINFO):
    case FAILURE(ACTION_TYPES.CREATE_ORDERINFO):
    case FAILURE(ACTION_TYPES.UPDATE_ORDERINFO):
    case FAILURE(ACTION_TYPES.DELETE_ORDERINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORDERINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_ORDERINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORDERINFO):
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

const apiUrl = 'api/order-infos';

// Actions

export const getEntities: ICrudGetAllAction<IOrderInfo> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERINFO_LIST,
    payload: axios.get<IOrderInfo>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOrderInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERINFO,
    payload: axios.get<IOrderInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOrderInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORDERINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrderInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORDERINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrderInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORDERINFO,
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
