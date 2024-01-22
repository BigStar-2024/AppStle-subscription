import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUsergroupRule, defaultValue } from 'app/shared/model/usergroup-rule.model';

export const ACTION_TYPES = {
  FETCH_USERGROUPRULE_LIST: 'usergroupRule/FETCH_USERGROUPRULE_LIST',
  FETCH_USERGROUPRULE: 'usergroupRule/FETCH_USERGROUPRULE',
  CREATE_USERGROUPRULE: 'usergroupRule/CREATE_USERGROUPRULE',
  UPDATE_USERGROUPRULE: 'usergroupRule/UPDATE_USERGROUPRULE',
  DELETE_USERGROUPRULE: 'usergroupRule/DELETE_USERGROUPRULE',
  RESET: 'usergroupRule/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUsergroupRule>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type UsergroupRuleState = Readonly<typeof initialState>;

// Reducer

export default (state: UsergroupRuleState = initialState, action): UsergroupRuleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERGROUPRULE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERGROUPRULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERGROUPRULE):
    case REQUEST(ACTION_TYPES.UPDATE_USERGROUPRULE):
    case REQUEST(ACTION_TYPES.DELETE_USERGROUPRULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_USERGROUPRULE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERGROUPRULE):
    case FAILURE(ACTION_TYPES.CREATE_USERGROUPRULE):
    case FAILURE(ACTION_TYPES.UPDATE_USERGROUPRULE):
    case FAILURE(ACTION_TYPES.DELETE_USERGROUPRULE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERGROUPRULE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERGROUPRULE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERGROUPRULE):
    case SUCCESS(ACTION_TYPES.UPDATE_USERGROUPRULE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERGROUPRULE):
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

const apiUrl = 'api/usergroup-rules';

// Actions

export const getEntities: ICrudGetAllAction<IUsergroupRule> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_USERGROUPRULE_LIST,
  payload: axios.get<IUsergroupRule>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IUsergroupRule> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERGROUPRULE,
    payload: axios.get<IUsergroupRule>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUsergroupRule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERGROUPRULE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUsergroupRule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERGROUPRULE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUsergroupRule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERGROUPRULE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
