import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import {
  defaultSubscriptionAnalyticsValue,
  defaultSubscriptionCanceledAnalyticsValue,
  defaultSubscriptionOrderAnalyticsValue,
  defaultValue,
  IAnalytics
} from 'app/shared/model/analytics.model';

export const ACTION_TYPES = {
  FETCH_ANALYTICS_LIST: 'analytics/FETCH_ANALYTICS_LIST',
  FETCH_ANALYTICS: 'analytics/FETCH_ANALYTICS',
  CREATE_ANALYTICS: 'analytics/CREATE_ANALYTICS',
  UPDATE_ANALYTICS: 'analytics/UPDATE_ANALYTICS',
  DELETE_ANALYTICS: 'analytics/DELETE_ANALYTICS',
  FETCH_V2_ANALYTICS_SUBSCRIPTIONS: 'analytics/FETCH_V2_ANALYTICS_SUBSCRIPTIONS',
  FETCH_V2_ANALYTICS_CANCELED_SUBSCRIPTIONS: 'analytics/FETCH_V2_ANALYTICS_CANCELED_SUBSCRIPTIONS',
  FETCH_V2_ANALYTICS_ORDER_SUBSCRIPTIONS: 'analytics/FETCH_V2_ANALYTICS_ORDER_SUBSCRIPTIONS',
  RESET: 'analytics/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnalytics>,
  entity: defaultValue,
  subscriptionsAnalytics: defaultSubscriptionAnalyticsValue,
  loadingSubscriptionsAnalytics: false,
  subscriptionsCanceledAnalytics: defaultSubscriptionCanceledAnalyticsValue,
  loadingSubscriptionsCanceledAnalytics: false,
  subscriptionsOrderAnalytics: defaultSubscriptionOrderAnalyticsValue,
  loadingSubscriptionsOrderAnalytics: false,
  updating: false,
  updateSuccess: false
};

export type AnalyticsState = Readonly<typeof initialState>;

// Reducer

export default (state: AnalyticsState = initialState, action): AnalyticsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ANALYTICS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANALYTICS):
    case REQUEST(ACTION_TYPES.FETCH_V2_ANALYTICS_SUBSCRIPTIONS):
    case REQUEST(ACTION_TYPES.FETCH_V2_ANALYTICS_CANCELED_SUBSCRIPTIONS):
    case REQUEST(ACTION_TYPES.FETCH_V2_ANALYTICS_ORDER_SUBSCRIPTIONS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loadingSubscriptionsAnalytics: true,
        loadingSubscriptionsCanceledAnalytics: true,
        loadingSubscriptionsOrderAnalytics: true,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ANALYTICS):
    case REQUEST(ACTION_TYPES.UPDATE_ANALYTICS):
    case REQUEST(ACTION_TYPES.DELETE_ANALYTICS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ANALYTICS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANALYTICS):
    case FAILURE(ACTION_TYPES.FETCH_V2_ANALYTICS_SUBSCRIPTIONS):
    case FAILURE(ACTION_TYPES.FETCH_V2_ANALYTICS_CANCELED_SUBSCRIPTIONS):
    case FAILURE(ACTION_TYPES.FETCH_V2_ANALYTICS_ORDER_SUBSCRIPTIONS):
    case FAILURE(ACTION_TYPES.CREATE_ANALYTICS):
    case FAILURE(ACTION_TYPES.UPDATE_ANALYTICS):
    case FAILURE(ACTION_TYPES.DELETE_ANALYTICS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        loadingSubscriptionsAnalytics: false,
        loadingSubscriptionsCanceledAnalytics: false,
        loadingSubscriptionsOrderAnalytics: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANALYTICS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANALYTICS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };

    case SUCCESS(ACTION_TYPES.FETCH_V2_ANALYTICS_SUBSCRIPTIONS):
      return {
        ...state,
        loading: false,
        loadingSubscriptionsAnalytics: false,
        subscriptionsAnalytics: action.payload.data
      };

    case SUCCESS(ACTION_TYPES.FETCH_V2_ANALYTICS_CANCELED_SUBSCRIPTIONS):
      return {
        ...state,
        loading: false,
        loadingSubscriptionsCanceledAnalytics: false,
        subscriptionsCanceledAnalytics: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_V2_ANALYTICS_ORDER_SUBSCRIPTIONS):
      return {
        ...state,
        loading: false,
        loadingSubscriptionsOrderAnalytics: false,
        subscriptionsOrderAnalytics: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANALYTICS):
    case SUCCESS(ACTION_TYPES.UPDATE_ANALYTICS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANALYTICS):
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

const apiUrl = 'api/analytics';

// Actions

export const getEntities: ICrudGetAllAction<IAnalytics> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ANALYTICS_LIST,
  payload: axios.get<IAnalytics>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAnalytics> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANALYTICS,
    payload: axios.get<IAnalytics>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAnalytics> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANALYTICS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnalytics> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANALYTICS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnalytics> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANALYTICS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const getV2SubscriptionAnalytics = (filterBy, days, fromDay, toDay) => {
  const requestUrl = `api/conversion-analytics/subscriptions-analytics`;
  return {
    type: ACTION_TYPES.FETCH_V2_ANALYTICS_SUBSCRIPTIONS,
    payload: axios.get<IAnalytics>(requestUrl, {
      params: { filterBy, days, fromDay, toDay }
    })
  };
};

export const getV2SubscriptionCanceledAnalytics = (filterBy, days, fromDay, toDay) => {
  const requestUrl = `api/conversion-analytics/subscriptions-canceled-analytics`;
  return {
    type: ACTION_TYPES.FETCH_V2_ANALYTICS_CANCELED_SUBSCRIPTIONS,
    payload: axios.get<IAnalytics>(requestUrl, {
      params: { filterBy, days, fromDay, toDay }
    })
  };
};

export const getV2SubscriptionOrderAnalytics = (filterBy, days, fromDay, toDay) => {
  const requestUrl = `api/conversion-analytics/subscriptions-order-analytics`;
  return {
    type: ACTION_TYPES.FETCH_V2_ANALYTICS_ORDER_SUBSCRIPTIONS,
    payload: axios.get<IAnalytics>(requestUrl, {
      params: { filterBy, days, fromDay, toDay }
    })
  };
};
