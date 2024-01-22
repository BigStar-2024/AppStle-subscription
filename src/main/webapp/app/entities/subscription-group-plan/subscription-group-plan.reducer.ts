import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, ISubscriptionGroupPlan } from 'app/shared/model/subscription-group-plan.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONGROUPPLAN_LIST: 'subscriptionGroupPlan/FETCH_SUBSCRIPTIONGROUPPLAN_LIST',
  FETCH_SUBSCRIPTIONGROUPPLAN: 'subscriptionGroupPlan/FETCH_SUBSCRIPTIONGROUPPLAN',
  CREATE_SUBSCRIPTIONGROUPPLAN: 'subscriptionGroupPlan/CREATE_SUBSCRIPTIONGROUPPLAN',
  UPDATE_SUBSCRIPTIONGROUPPLAN: 'subscriptionGroupPlan/UPDATE_SUBSCRIPTIONGROUPPLAN',
  DELETE_SUBSCRIPTIONGROUPPLAN: 'subscriptionGroupPlan/DELETE_SUBSCRIPTIONGROUPPLAN',
  SET_BLOB: 'subscriptionGroupPlan/SET_BLOB',
  RESET: 'subscriptionGroupPlan/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionGroupPlan>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubscriptionGroupPlanState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionGroupPlanState = initialState, action): SubscriptionGroupPlanState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONGROUPPLAN):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONGROUPPLAN):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONGROUPPLAN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONGROUPPLAN):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONGROUPPLAN):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONGROUPPLAN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONGROUPPLAN):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONGROUPPLAN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONGROUPPLAN):
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

const apiUrl = 'api/subscription-group-plans';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionGroupPlan> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN_LIST,
  payload: axios.get<ISubscriptionGroupPlan>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionGroupPlan> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN,
    payload: axios.get<ISubscriptionGroupPlan>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionGroupPlan> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONGROUPPLAN,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionGroupPlan> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONGROUPPLAN,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionGroupPlan> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONGROUPPLAN,
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

export const getSubscriptionPlanGroupV2: ICrudGetAction<ISubscriptionGroupPlan> = id => {
  const requestUrl = `api/v2/subscription-groups/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONGROUPPLAN,
    payload: axios.get<ISubscriptionGroupPlan>(requestUrl)
  };
};
