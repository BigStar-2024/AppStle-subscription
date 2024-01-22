import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRuleCriteria, defaultValue } from 'app/shared/model/rule-criteria.model';

export const ACTION_TYPES = {
  FETCH_RULECRITERIA_LIST: 'ruleCriteria/FETCH_RULECRITERIA_LIST',
  FETCH_RULECRITERIA: 'ruleCriteria/FETCH_RULECRITERIA',
  CREATE_RULECRITERIA: 'ruleCriteria/CREATE_RULECRITERIA',
  UPDATE_RULECRITERIA: 'ruleCriteria/UPDATE_RULECRITERIA',
  DELETE_RULECRITERIA: 'ruleCriteria/DELETE_RULECRITERIA',
  RESET: 'ruleCriteria/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRuleCriteria>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RuleCriteriaState = Readonly<typeof initialState>;

// Reducer

export default (state: RuleCriteriaState = initialState, action): RuleCriteriaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RULECRITERIA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RULECRITERIA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RULECRITERIA):
    case REQUEST(ACTION_TYPES.UPDATE_RULECRITERIA):
    case REQUEST(ACTION_TYPES.DELETE_RULECRITERIA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RULECRITERIA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RULECRITERIA):
    case FAILURE(ACTION_TYPES.CREATE_RULECRITERIA):
    case FAILURE(ACTION_TYPES.UPDATE_RULECRITERIA):
    case FAILURE(ACTION_TYPES.DELETE_RULECRITERIA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RULECRITERIA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RULECRITERIA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RULECRITERIA):
    case SUCCESS(ACTION_TYPES.UPDATE_RULECRITERIA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RULECRITERIA):
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

const apiUrl = 'api/rule-criteria';

// Actions

export const getEntities: ICrudGetAllAction<IRuleCriteria> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RULECRITERIA_LIST,
  payload: axios.get<IRuleCriteria>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRuleCriteria> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RULECRITERIA,
    payload: axios.get<IRuleCriteria>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRuleCriteria> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RULECRITERIA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRuleCriteria> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RULECRITERIA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRuleCriteria> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RULECRITERIA,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
