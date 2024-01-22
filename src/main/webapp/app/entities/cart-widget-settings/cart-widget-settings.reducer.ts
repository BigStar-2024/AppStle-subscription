import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICartWidgetSettings, defaultValue } from 'app/shared/model/cart-widget-settings.model';

export const ACTION_TYPES = {
  FETCH_CARTWIDGETSETTINGS_LIST: 'cartWidgetSettings/FETCH_CARTWIDGETSETTINGS_LIST',
  FETCH_CARTWIDGETSETTINGS: 'cartWidgetSettings/FETCH_CARTWIDGETSETTINGS',
  CREATE_CARTWIDGETSETTINGS: 'cartWidgetSettings/CREATE_CARTWIDGETSETTINGS',
  UPDATE_CARTWIDGETSETTINGS: 'cartWidgetSettings/UPDATE_CARTWIDGETSETTINGS',
  DELETE_CARTWIDGETSETTINGS: 'cartWidgetSettings/DELETE_CARTWIDGETSETTINGS',
  SET_BLOB: 'cartWidgetSettings/SET_BLOB',
  RESET: 'cartWidgetSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICartWidgetSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CartWidgetSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: CartWidgetSettingsState = initialState, action): CartWidgetSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CARTWIDGETSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CARTWIDGETSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CARTWIDGETSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_CARTWIDGETSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_CARTWIDGETSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CARTWIDGETSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CARTWIDGETSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_CARTWIDGETSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_CARTWIDGETSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_CARTWIDGETSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARTWIDGETSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARTWIDGETSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CARTWIDGETSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_CARTWIDGETSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CARTWIDGETSETTINGS):
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

const apiUrl = 'api/cart-widget-settings';

// Actions

export const getEntities: ICrudGetAllAction<ICartWidgetSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CARTWIDGETSETTINGS_LIST,
  payload: axios.get<ICartWidgetSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICartWidgetSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CARTWIDGETSETTINGS,
    payload: axios.get<ICartWidgetSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICartWidgetSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CARTWIDGETSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICartWidgetSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CARTWIDGETSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICartWidgetSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CARTWIDGETSETTINGS,
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
