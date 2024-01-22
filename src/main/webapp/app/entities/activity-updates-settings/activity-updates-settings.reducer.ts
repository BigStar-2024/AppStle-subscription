import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IActivityUpdatesSettings, defaultValue } from 'app/shared/model/activity-updates-settings.model';

export const ACTION_TYPES = {
  FETCH_ACTIVITYUPDATESSETTINGS_LIST: 'activityUpdatesSettings/FETCH_ACTIVITYUPDATESSETTINGS_LIST',
  FETCH_ACTIVITYUPDATESSETTINGS: 'activityUpdatesSettings/FETCH_ACTIVITYUPDATESSETTINGS',
  CREATE_ACTIVITYUPDATESSETTINGS: 'activityUpdatesSettings/CREATE_ACTIVITYUPDATESSETTINGS',
  UPDATE_ACTIVITYUPDATESSETTINGS: 'activityUpdatesSettings/UPDATE_ACTIVITYUPDATESSETTINGS',
  DELETE_ACTIVITYUPDATESSETTINGS: 'activityUpdatesSettings/DELETE_ACTIVITYUPDATESSETTINGS',
  RESET: 'activityUpdatesSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ActivityUpdatesSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: ActivityUpdatesSettingsState = initialState, action): ActivityUpdatesSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITYUPDATESSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITYUPDATESSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ACTIVITYUPDATESSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_ACTIVITYUPDATESSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_ACTIVITYUPDATESSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITYUPDATESSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITYUPDATESSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_ACTIVITYUPDATESSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_ACTIVITYUPDATESSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_ACTIVITYUPDATESSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITYUPDATESSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITYUPDATESSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACTIVITYUPDATESSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_ACTIVITYUPDATESSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACTIVITYUPDATESSETTINGS):
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

const apiUrl = 'api/activity-updates-settings';

// Actions

export const getEntities: ICrudGetAllAction<IActivityUpdatesSettings> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYUPDATESSETTINGS_LIST,
    payload: axios.get<IActivityUpdatesSettings>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IActivityUpdatesSettings> = () => {
  const requestUrl = `api/activity-updates-setting`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYUPDATESSETTINGS,
    payload: axios.get<IActivityUpdatesSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IActivityUpdatesSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACTIVITYUPDATESSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IActivityUpdatesSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACTIVITYUPDATESSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IActivityUpdatesSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACTIVITYUPDATESSETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
