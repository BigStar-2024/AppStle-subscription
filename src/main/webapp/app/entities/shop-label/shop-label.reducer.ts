import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShopLabel, defaultValue } from 'app/shared/model/shop-label.model';

export const ACTION_TYPES = {
  FETCH_SHOPLABEL_LIST: 'shopLabel/FETCH_SHOPLABEL_LIST',
  FETCH_SHOPLABEL: 'shopLabel/FETCH_SHOPLABEL',
  CREATE_SHOPLABEL: 'shopLabel/CREATE_SHOPLABEL',
  UPDATE_SHOPLABEL: 'shopLabel/UPDATE_SHOPLABEL',
  DELETE_SHOPLABEL: 'shopLabel/DELETE_SHOPLABEL',
  SET_BLOB: 'shopLabel/SET_BLOB',
  RESET: 'shopLabel/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShopLabel>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ShopLabelState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopLabelState = initialState, action): ShopLabelState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHOPLABEL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPLABEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPLABEL):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPLABEL):
    case REQUEST(ACTION_TYPES.DELETE_SHOPLABEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPLABEL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPLABEL):
    case FAILURE(ACTION_TYPES.CREATE_SHOPLABEL):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPLABEL):
    case FAILURE(ACTION_TYPES.DELETE_SHOPLABEL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPLABEL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPLABEL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPLABEL):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPLABEL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPLABEL):
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

const apiUrl = 'api/shop-labels';

// Actions

export const getEntities: ICrudGetAllAction<IShopLabel> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPLABEL_LIST,
    payload: axios.get<IShopLabel>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IShopLabel> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPLABEL,
    payload: axios.get<IShopLabel>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopLabel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPLABEL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopLabel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPLABEL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShopLabel> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPLABEL,
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
