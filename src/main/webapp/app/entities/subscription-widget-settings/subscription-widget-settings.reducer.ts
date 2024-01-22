import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionWidgetSettings, defaultValue } from 'app/shared/model/subscription-widget-settings.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONWIDGETSETTINGS_LIST: 'subscriptionWidgetSettings/FETCH_SUBSCRIPTIONWIDGETSETTINGS_LIST',
  FETCH_SUBSCRIPTIONWIDGETSETTINGS: 'subscriptionWidgetSettings/FETCH_SUBSCRIPTIONWIDGETSETTINGS',
  CREATE_SUBSCRIPTIONWIDGETSETTINGS: 'subscriptionWidgetSettings/CREATE_SUBSCRIPTIONWIDGETSETTINGS',
  UPDATE_SUBSCRIPTIONWIDGETSETTINGS: 'subscriptionWidgetSettings/UPDATE_SUBSCRIPTIONWIDGETSETTINGS',
  DELETE_SUBSCRIPTIONWIDGETSETTINGS: 'subscriptionWidgetSettings/DELETE_SUBSCRIPTIONWIDGETSETTINGS',
  SET_BLOB: 'subscriptionWidgetSettings/SET_BLOB',
  RESET: 'subscriptionWidgetSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionWidgetSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubscriptionWidgetSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionWidgetSettingsState = initialState, action): SubscriptionWidgetSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONWIDGETSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONWIDGETSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONWIDGETSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONWIDGETSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONWIDGETSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONWIDGETSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONWIDGETSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONWIDGETSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONWIDGETSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONWIDGETSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONWIDGETSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONWIDGETSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONWIDGETSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONWIDGETSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONWIDGETSETTINGS):
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

const apiUrl = 'api/subscription-widget-settings';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionWidgetSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONWIDGETSETTINGS_LIST,
  payload: axios.get<ISubscriptionWidgetSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionWidgetSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONWIDGETSETTINGS,
    payload: axios.get<ISubscriptionWidgetSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionWidgetSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONWIDGETSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionWidgetSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONWIDGETSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionWidgetSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONWIDGETSETTINGS,
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
