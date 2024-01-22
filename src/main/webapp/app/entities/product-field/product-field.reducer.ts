import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductField, defaultValue } from 'app/shared/model/product-field.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTFIELD_LIST: 'productField/FETCH_PRODUCTFIELD_LIST',
  FETCH_PRODUCTFIELD: 'productField/FETCH_PRODUCTFIELD',
  CREATE_PRODUCTFIELD: 'productField/CREATE_PRODUCTFIELD',
  UPDATE_PRODUCTFIELD: 'productField/UPDATE_PRODUCTFIELD',
  DELETE_PRODUCTFIELD: 'productField/DELETE_PRODUCTFIELD',
  RESET: 'productField/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductField>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ProductFieldState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductFieldState = initialState, action): ProductFieldState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTFIELD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTFIELD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTFIELD):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTFIELD):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTFIELD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTFIELD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTFIELD):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTFIELD):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTFIELD):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTFIELD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTFIELD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTFIELD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTFIELD):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTFIELD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTFIELD):
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

const apiUrl = 'api/product-fields';

// Actions

export const getEntities: ICrudGetAllAction<IProductField> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PRODUCTFIELD_LIST,
  payload: axios.get<IProductField>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IProductField> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTFIELD,
    payload: axios.get<IProductField>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProductField> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTFIELD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity = entities => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTFIELD,
    payload: axios.put(apiUrl, entities)
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductField> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTFIELD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
