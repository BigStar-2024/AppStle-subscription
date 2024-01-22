import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITriggerRule, defaultValue } from 'app/shared/model/trigger-rule.model';

export const ACTION_TYPES = {
  FETCH_TRIGGERRULE_LIST: 'triggerRule/FETCH_TRIGGERRULE_LIST',
  FETCH_TRIGGERRULE: 'triggerRule/FETCH_TRIGGERRULE',
  CREATE_TRIGGERRULE: 'triggerRule/CREATE_TRIGGERRULE',
  UPDATE_TRIGGERRULE: 'triggerRule/UPDATE_TRIGGERRULE',
  DELETE_TRIGGERRULE: 'triggerRule/DELETE_TRIGGERRULE',
  CRITERIA_FORM_VALUE_TRIGGERRULE: 'triggerRule/CRITERIA_FORM_VALUE_TRIGGERRULE',
  UPDATE_FORM_VALUE_TRIGGERRULE: 'triggerRule/UPDATE_FORM_VALUE_TRIGGERRULE',
  SET_BLOB: 'triggerRule/SET_BLOB',
  RESET: 'triggerRule/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITriggerRule>,
  entity: JSON.parse(JSON.stringify(defaultValue)),
  updating: false,
  updateSuccess: false,
  totalItems: 0
};

export type TriggerRuleState = Readonly<typeof initialState>;

// Reducer

export default (state: TriggerRuleState = initialState, action): TriggerRuleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TRIGGERRULE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TRIGGERRULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TRIGGERRULE):
    case REQUEST(ACTION_TYPES.UPDATE_TRIGGERRULE):
    case REQUEST(ACTION_TYPES.DELETE_TRIGGERRULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TRIGGERRULE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TRIGGERRULE):
    case FAILURE(ACTION_TYPES.CREATE_TRIGGERRULE):
    case FAILURE(ACTION_TYPES.UPDATE_TRIGGERRULE):
    case FAILURE(ACTION_TYPES.DELETE_TRIGGERRULE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TRIGGERRULE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_TRIGGERRULE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TRIGGERRULE):
    case SUCCESS(ACTION_TYPES.UPDATE_TRIGGERRULE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TRIGGERRULE):
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
    case ACTION_TYPES.CRITERIA_FORM_VALUE_TRIGGERRULE:
      const formData = action.payload;
      return {
        ...state,
        entity: { ...state.entity, handlerData: { ...state.entity.handlerData, ...formData } }
      };
    case ACTION_TYPES.UPDATE_FORM_VALUE_TRIGGERRULE:
      const form = action.payload;
      return {
        ...state,
        entity: { ...state.entity, ...form }
      };
    case ACTION_TYPES.RESET:
      return JSON.parse(JSON.stringify(initialState));
    default:
      return state;
  }
};

const apiUrl = 'api/trigger-rules';

// Actions

export const getEntities: ICrudGetAllAction<ITriggerRule> = (page = 0, size = 10, sort = 'ACTIVE&sort=id,desc') => {
  const requestUrl = `${apiUrl}?page=${page}&size=${size}&status=${sort}&cacheBuster=${new Date().getTime()}`;
  return {
    type: ACTION_TYPES.FETCH_TRIGGERRULE_LIST,
    payload: axios.get<ITriggerRule>(requestUrl)
  };
};

export const deactivateEntity: ICrudPutAction<ITriggerRule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/deactivate/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.FETCH_TRIGGERRULE,
    payload: axios.post<ITriggerRule>(requestUrl, cleanEntity(id))
  });
  dispatch(getEntities(0, 10, 'ACTIVE&sort=id,desc'));
  return result;
};

export const activateEntity: ICrudPutAction<ITriggerRule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/activate/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.FETCH_TRIGGERRULE,
    payload: axios.post<ITriggerRule>(requestUrl, cleanEntity(id))
  });
  dispatch(getEntities(0, 10, 'DEACTIVATE&sort=id,desc'));
  return result;
};

export const getEntity: ICrudGetAction<ITriggerRule> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TRIGGERRULE,
    payload: axios.get<ITriggerRule>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITriggerRule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TRIGGERRULE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITriggerRule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TRIGGERRULE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITriggerRule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TRIGGERRULE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const onChangeCriteria: any = criteriaData => dispatch => {
  return dispatch({
    type: ACTION_TYPES.CRITERIA_FORM_VALUE_TRIGGERRULE,
    payload: criteriaData
  });
};
export const onChangeForm: any = formData => dispatch => {
  return dispatch({
    type: ACTION_TYPES.UPDATE_FORM_VALUE_TRIGGERRULE,
    payload: formData
  });
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
