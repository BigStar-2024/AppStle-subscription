import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionContractSettings, defaultValue } from 'app/shared/model/subscription-contract-settings.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONCONTRACTSETTINGS_LIST: 'subscriptionContractSettings/FETCH_SUBSCRIPTIONCONTRACTSETTINGS_LIST',
  FETCH_SUBSCRIPTIONCONTRACTSETTINGS: 'subscriptionContractSettings/FETCH_SUBSCRIPTIONCONTRACTSETTINGS',
  CREATE_SUBSCRIPTIONCONTRACTSETTINGS: 'subscriptionContractSettings/CREATE_SUBSCRIPTIONCONTRACTSETTINGS',
  UPDATE_SUBSCRIPTIONCONTRACTSETTINGS: 'subscriptionContractSettings/UPDATE_SUBSCRIPTIONCONTRACTSETTINGS',
  DELETE_SUBSCRIPTIONCONTRACTSETTINGS: 'subscriptionContractSettings/DELETE_SUBSCRIPTIONCONTRACTSETTINGS',
  RESET: 'subscriptionContractSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionContractSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubscriptionContractSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionContractSettingsState = initialState, action): SubscriptionContractSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTSETTINGS):
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

const apiUrl = 'api/subscription-contract-settings';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionContractSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTSETTINGS_LIST,
  payload: axios.get<ISubscriptionContractSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionContractSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTSETTINGS,
    payload: axios.get<ISubscriptionContractSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionContractSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionContractSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionContractSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTSETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
