import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomerPortalSettings, defaultValue } from 'app/shared/model/customer-portal-settings.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMERPORTALSETTINGS_LIST: 'customerPortalSettings/FETCH_CUSTOMERPORTALSETTINGS_LIST',
  FETCH_CUSTOMERPORTALSETTINGS: 'customerPortalSettings/FETCH_CUSTOMERPORTALSETTINGS',
  CREATE_CUSTOMERPORTALSETTINGS: 'customerPortalSettings/CREATE_CUSTOMERPORTALSETTINGS',
  UPDATE_CUSTOMERPORTALSETTINGS: 'customerPortalSettings/UPDATE_CUSTOMERPORTALSETTINGS',
  DELETE_CUSTOMERPORTALSETTINGS: 'customerPortalSettings/DELETE_CUSTOMERPORTALSETTINGS',
  RESET: 'customerPortalSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerPortalSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CustomerPortalSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerPortalSettingsState = initialState, action): CustomerPortalSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERPORTALSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERPORTALSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERPORTALSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERPORTALSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERPORTALSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERPORTALSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERPORTALSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERPORTALSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERPORTALSETTINGS):
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

const apiUrl = 'api/customer-portal-settings';

// Actions

export const getEntities: ICrudGetAllAction<ICustomerPortalSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS_LIST,
  payload: axios.get<ICustomerPortalSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICustomerPortalSettings> = (id, shop?) => {
  const requestUrl = `api/customer-portal-settings/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS,
    payload: axios.get<ICustomerPortalSettings>(requestUrl)
  };
};

export const getCustomerPortalSettingEntity: ICrudGetAction<ICustomerPortalSettings> = (id, api_key?) => {
  const requestUrl = `api/customer-portal-settings/${id}`;
  //   const requestUrl = `api/external/v2/customer-portal-settings/${id}?api_key=${window?.appstle_api_key}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERPORTALSETTINGS,
    payload: axios.get<ICustomerPortalSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomerPortalSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERPORTALSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerPortalSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERPORTALSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerPortalSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERPORTALSETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
