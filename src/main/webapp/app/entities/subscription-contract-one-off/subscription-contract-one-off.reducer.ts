import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionContractOneOff, defaultValue } from 'app/shared/model/subscription-contract-one-off.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONCONTRACTONEOFF_LIST: 'subscriptionContractOneOff/FETCH_SUBSCRIPTIONCONTRACTONEOFF_LIST',
  FETCH_SUBSCRIPTIONCONTRACTONEOFF: 'subscriptionContractOneOff/FETCH_SUBSCRIPTIONCONTRACTONEOFF',
  CREATE_SUBSCRIPTIONCONTRACTONEOFF: 'subscriptionContractOneOff/CREATE_SUBSCRIPTIONCONTRACTONEOFF',
  UPDATE_SUBSCRIPTIONCONTRACTONEOFF: 'subscriptionContractOneOff/UPDATE_SUBSCRIPTIONCONTRACTONEOFF',
  DELETE_SUBSCRIPTIONCONTRACTONEOFF: 'subscriptionContractOneOff/DELETE_SUBSCRIPTIONCONTRACTONEOFF',
  RESET: 'subscriptionContractOneOff/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionContractOneOff>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SubscriptionContractOneOffState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionContractOneOffState = initialState, action): SubscriptionContractOneOffState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTONEOFF_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTONEOFF):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTONEOFF):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTONEOFF):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTONEOFF):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTONEOFF_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTONEOFF):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTONEOFF):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTONEOFF):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTONEOFF):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTONEOFF_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTONEOFF):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTONEOFF):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTONEOFF):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTONEOFF):
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

const apiUrl = 'api/subscription-contract-one-offs';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionContractOneOff> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTONEOFF_LIST,
    payload: axios.get<ISubscriptionContractOneOff>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISubscriptionContractOneOff> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTONEOFF,
    payload: axios.get<ISubscriptionContractOneOff>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionContractOneOff> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTONEOFF,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionContractOneOff> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTONEOFF,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionContractOneOff> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTONEOFF,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
