import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionContractProcessing, defaultValue } from 'app/shared/model/subscription-contract-processing.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONCONTRACTPROCESSING_LIST: 'subscriptionContractProcessing/FETCH_SUBSCRIPTIONCONTRACTPROCESSING_LIST',
  FETCH_SUBSCRIPTIONCONTRACTPROCESSING: 'subscriptionContractProcessing/FETCH_SUBSCRIPTIONCONTRACTPROCESSING',
  CREATE_SUBSCRIPTIONCONTRACTPROCESSING: 'subscriptionContractProcessing/CREATE_SUBSCRIPTIONCONTRACTPROCESSING',
  UPDATE_SUBSCRIPTIONCONTRACTPROCESSING: 'subscriptionContractProcessing/UPDATE_SUBSCRIPTIONCONTRACTPROCESSING',
  DELETE_SUBSCRIPTIONCONTRACTPROCESSING: 'subscriptionContractProcessing/DELETE_SUBSCRIPTIONCONTRACTPROCESSING',
  RESET: 'subscriptionContractProcessing/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionContractProcessing>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SubscriptionContractProcessingState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionContractProcessingState = initialState, action): SubscriptionContractProcessingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTPROCESSING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTPROCESSING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTPROCESSING):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTPROCESSING):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTPROCESSING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTPROCESSING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTPROCESSING):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTPROCESSING):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTPROCESSING):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTPROCESSING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTPROCESSING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTPROCESSING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTPROCESSING):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTPROCESSING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTPROCESSING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/subscription-contract-processings';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionContractProcessing> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTPROCESSING_LIST,
    payload: axios.get<ISubscriptionContractProcessing>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISubscriptionContractProcessing> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTPROCESSING,
    payload: axios.get<ISubscriptionContractProcessing>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISubscriptionContractProcessing> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTPROCESSING,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionContractProcessing> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTPROCESSING,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionContractProcessing> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTPROCESSING,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
