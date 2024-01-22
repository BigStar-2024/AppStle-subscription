import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IThemeSettings, defaultValue } from 'app/shared/model/theme-settings.model';

export const ACTION_TYPES = {
  FETCH_THEMESETTINGS_LIST: 'themeSettings/FETCH_THEMESETTINGS_LIST',
  FETCH_THEMESETTINGS: 'themeSettings/FETCH_THEMESETTINGS',
  CREATE_THEMESETTINGS: 'themeSettings/CREATE_THEMESETTINGS',
  UPDATE_THEMESETTINGS: 'themeSettings/UPDATE_THEMESETTINGS',
  DELETE_THEMESETTINGS: 'themeSettings/DELETE_THEMESETTINGS',
  GENERATE_SCRIPTS_FOR_THEME_THEMESETTINGS: 'themeSettings/GENERATE_SCRIPTS_FOR_THEME_THEMESETTINGS',
  SET_BLOB: 'themeSettings/SET_BLOB',
  RESET: 'themeSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IThemeSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ThemeSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: ThemeSettingsState = initialState, action): ThemeSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_THEMESETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_THEMESETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_THEMESETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_THEMESETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_THEMESETTINGS):
    case REQUEST(ACTION_TYPES.GENERATE_SCRIPTS_FOR_THEME_THEMESETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_THEMESETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_THEMESETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_THEMESETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_THEMESETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_THEMESETTINGS):
    case FAILURE(ACTION_TYPES.GENERATE_SCRIPTS_FOR_THEME_THEMESETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_THEMESETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        entity: action.payload.data[0]
      };
    case SUCCESS(ACTION_TYPES.FETCH_THEMESETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_THEMESETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_THEMESETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_THEMESETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.GENERATE_SCRIPTS_FOR_THEME_THEMESETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true
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

const apiUrl = 'api/theme-settings';

// Actions

export const getEntities: ICrudGetAllAction<IThemeSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_THEMESETTINGS_LIST,
  payload: axios.get<IThemeSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IThemeSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_THEMESETTINGS,
    payload: axios.get<IThemeSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IThemeSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_THEMESETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IThemeSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_THEMESETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IThemeSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_THEMESETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const updateThemeName: ICrudPutAction<IThemeSettings> = themeName => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_THEMESETTINGS,
    payload: axios.put(apiUrl + '/theme/' + themeName)
  });
  dispatch(getEntities());
  return result;
};

export const updateToV2: ICrudPutAction<IThemeSettings> = () => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_THEMESETTINGS,
    payload: axios.put(apiUrl + '/update-to-v2')
  });
  dispatch(getEntities());
  return result;
};

export const updateThemeSkipSetting: ICrudPutAction<IThemeSettings> = setting => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_THEMESETTINGS,
    payload: axios.put(apiUrl + '/setting/' + setting)
  });
  dispatch(getEntities());
  return result;
};

export const generateScriptsForTheme = themeId => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.GENERATE_SCRIPTS_FOR_THEME_THEMESETTINGS,
    payload: axios.get(`${apiUrl}/generate-scripts-for-theme?themeId=${themeId}`)
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
