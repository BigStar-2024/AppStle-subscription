import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomerTriggerRule, defaultValue } from 'app/shared/model/customer-trigger-rule.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMERTRIGGERRULE_LIST: 'customerTriggerRule/FETCH_CUSTOMERTRIGGERRULE_LIST',
  FETCH_CUSTOMERTRIGGERRULE: 'customerTriggerRule/FETCH_CUSTOMERTRIGGERRULE',
  CREATE_CUSTOMERTRIGGERRULE: 'customerTriggerRule/CREATE_CUSTOMERTRIGGERRULE',
  UPDATE_CUSTOMERTRIGGERRULE: 'customerTriggerRule/UPDATE_CUSTOMERTRIGGERRULE',
  DELETE_CUSTOMERTRIGGERRULE: 'customerTriggerRule/DELETE_CUSTOMERTRIGGERRULE',
  SET_BLOB: 'customerTriggerRule/SET_BLOB',
  RESET: 'customerTriggerRule/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerTriggerRule>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CustomerTriggerRuleState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerTriggerRuleState = initialState, action): CustomerTriggerRuleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERTRIGGERRULE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERTRIGGERRULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERTRIGGERRULE):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERTRIGGERRULE):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERTRIGGERRULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERTRIGGERRULE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERTRIGGERRULE):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERTRIGGERRULE):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERTRIGGERRULE):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERTRIGGERRULE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERTRIGGERRULE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERTRIGGERRULE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERTRIGGERRULE):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERTRIGGERRULE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERTRIGGERRULE):
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

const apiUrl = 'api/customer-trigger-rules';

// Actions

export const getEntities: ICrudGetAllAction<ICustomerTriggerRule> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CUSTOMERTRIGGERRULE_LIST,
  payload: axios.get<ICustomerTriggerRule>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICustomerTriggerRule> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERTRIGGERRULE,
    payload: axios.get<ICustomerTriggerRule>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomerTriggerRule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERTRIGGERRULE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerTriggerRule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERTRIGGERRULE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerTriggerRule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERTRIGGERRULE,
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
