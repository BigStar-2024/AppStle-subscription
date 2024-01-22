import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionBundleSettings, defaultValue } from 'app/shared/model/subscription-bundle-settings.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONBUNDLESETTINGS_LIST: 'subscriptionBundleSettings/FETCH_SUBSCRIPTIONBUNDLESETTINGS_LIST',
  FETCH_SUBSCRIPTIONBUNDLESETTINGS: 'subscriptionBundleSettings/FETCH_SUBSCRIPTIONBUNDLESETTINGS',
  CREATE_SUBSCRIPTIONBUNDLESETTINGS: 'subscriptionBundleSettings/CREATE_SUBSCRIPTIONBUNDLESETTINGS',
  UPDATE_SUBSCRIPTIONBUNDLESETTINGS: 'subscriptionBundleSettings/UPDATE_SUBSCRIPTIONBUNDLESETTINGS',
  DELETE_SUBSCRIPTIONBUNDLESETTINGS: 'subscriptionBundleSettings/DELETE_SUBSCRIPTIONBUNDLESETTINGS',
  RESET: 'subscriptionBundleSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionBundleSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubscriptionBundleSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionBundleSettingsState = initialState, action): SubscriptionBundleSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONBUNDLESETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLESETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONBUNDLESETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONBUNDLESETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLESETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONBUNDLESETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONBUNDLESETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLESETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONBUNDLESETTINGS):
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

const apiUrl = 'api/subscription-bundle-settings';
const externalApiUrl = 'api/external/v2/subscription-bundle-settings';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionBundleSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS_LIST,
  payload: axios.get<ISubscriptionBundleSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionBundleSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS,
    payload: axios.get<ISubscriptionBundleSettings>(requestUrl)
  };
};

export const getEntityByShop: ICrudGetAction<ISubscriptionBundleSettings> = shop => {
  const requestUrl = `api/subscription-bundle-settings/${1}`;
  //   const requestUrl = `api/external/v2/subscription-bundle-settings/${1}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBUNDLESETTINGS,
    payload: axios.get<ISubscriptionBundleSettings>(`${requestUrl}`)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionBundleSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONBUNDLESETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionBundleSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONBUNDLESETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionBundleSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONBUNDLESETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
