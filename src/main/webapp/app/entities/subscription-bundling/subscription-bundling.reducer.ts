import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, ISubscriptionBundling } from 'app/shared/model/subscription-bundling.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONBUNDLING_LIST: 'subscriptionBundling/FETCH_SUBSCRIPTIONBUNDLING_LIST',
  FETCH_SUBSCRIPTIONBUNDLING: 'subscriptionBundling/FETCH_SUBSCRIPTIONBUNDLING',
  CREATE_SUBSCRIPTIONBUNDLING: 'subscriptionBundling/CREATE_SUBSCRIPTIONBUNDLING',
  UPDATE_SUBSCRIPTIONBUNDLING: 'subscriptionBundling/UPDATE_SUBSCRIPTIONBUNDLING',
  DELETE_SUBSCRIPTIONBUNDLING: 'subscriptionBundling/DELETE_SUBSCRIPTIONBUNDLING',
  RESET: 'subscriptionBundling/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionBundling>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubscriptionBundlingState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionBundlingState = initialState, action): SubscriptionBundlingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONBUNDLING):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLING):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONBUNDLING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONBUNDLING):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLING):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONBUNDLING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONBUNDLING):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONBUNDLING):
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

const apiUrl = 'api/subscription-bundlings';

// Actions

export const getEntities = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING_LIST,
  payload: axios.get(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionBundling> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING,
    payload: axios.get<ISubscriptionBundling>(requestUrl)
  };
};
export const getBundlingByToken: ICrudGetAction<ISubscriptionBundling> = token => {
  const requestUrl = `${apiUrl}/by-token/${token}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING,
    payload: axios.get<ISubscriptionBundling>(requestUrl)
  };
};
export const getBundlingListsBySingleProduct: ICrudGetAction<ISubscriptionBundling> = () => {
  const requestUrl = `${apiUrl}/single-product`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLING_LIST,
    payload: axios.get<ISubscriptionBundling>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionBundling> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONBUNDLING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionBundling> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const changeBuildABoxStatus = (buildABoxId, status) => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLING,
    payload: axios.put(`${apiUrl}/changeBuildABoxStatus/${buildABoxId}/${status}`)
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionBundling> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONBUNDLING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
