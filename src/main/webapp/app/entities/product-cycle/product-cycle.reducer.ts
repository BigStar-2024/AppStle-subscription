import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductCycle, defaultValue } from 'app/shared/model/product-cycle.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTCYCLE_LIST: 'productCycle/FETCH_PRODUCTCYCLE_LIST',
  FETCH_PRODUCTCYCLE: 'productCycle/FETCH_PRODUCTCYCLE',
  CREATE_PRODUCTCYCLE: 'productCycle/CREATE_PRODUCTCYCLE',
  UPDATE_PRODUCTCYCLE: 'productCycle/UPDATE_PRODUCTCYCLE',
  DELETE_PRODUCTCYCLE: 'productCycle/DELETE_PRODUCTCYCLE',
  RESET: 'productCycle/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductCycle>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ProductCycleState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductCycleState = initialState, action): ProductCycleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTCYCLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTCYCLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTCYCLE):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTCYCLE):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTCYCLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTCYCLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTCYCLE):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTCYCLE):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTCYCLE):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTCYCLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTCYCLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTCYCLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTCYCLE):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTCYCLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTCYCLE):
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

const apiUrl = 'api/product-cycles';

// Actions

export const getEntities: ICrudGetAllAction<IProductCycle> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PRODUCTCYCLE_LIST,
  payload: axios.get<IProductCycle>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IProductCycle> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTCYCLE,
    payload: axios.get<IProductCycle>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProductCycle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTCYCLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductCycle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTCYCLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductCycle> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTCYCLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
