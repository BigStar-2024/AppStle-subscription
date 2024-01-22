import axios from 'axios';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IAppstleMenuSettings } from 'app/shared/model/appstle-menu-settings.model';

export const ACTION_TYPES = {
  FETCH_APPSTLEMENUSETTINGS_LIST: 'appstleMenuSettings/FETCH_APPSTLEMENUSETTINGS_LIST',
  FETCH_COLLECTIONS_LIST: 'appstleMenuSettings/FETCH_COLLECTIONS_LIST',
  FETCH_APPSTLEMENUSETTINGS: 'appstleMenuSettings/FETCH_APPSTLEMENUSETTINGS',
  CREATE_APPSTLEMENUSETTINGS: 'appstleMenuSettings/CREATE_APPSTLEMENUSETTINGS',
  UPDATE_APPSTLEMENUSETTINGS: 'appstleMenuSettings/UPDATE_APPSTLEMENUSETTINGS',
  DELETE_APPSTLEMENUSETTINGS: 'appstleMenuSettings/DELETE_APPSTLEMENUSETTINGS',
  SET_BLOB: 'appstleMenuSettings/SET_BLOB',
  RESET: 'appstleMenuSettings/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAppstleMenuSettings>,
  collections: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AppstleMenuAdminState = Readonly<typeof initialState>;

// Reducer

export default (state: AppstleMenuAdminState = initialState, action): AppstleMenuAdminState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COLLECTIONS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_APPSTLEMENUSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_APPSTLEMENUSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_APPSTLEMENUSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_COLLECTIONS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_APPSTLEMENUSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_APPSTLEMENUSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_APPSTLEMENUSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTIONS_LIST):
      return {
        ...state,
        loading: false,
        collections: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPSTLEMENUSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_APPSTLEMENUSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_APPSTLEMENUSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_APPSTLEMENUSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/appstle-menu';

// Actions

export const getCollections = (searchText = null) => {
  const requestUrl = `${apiUrl}/get-all-collections?searchText=${searchText}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTIONS_LIST,
    payload: axios.get<IAppstleMenuSettings>(`${requestUrl}`),
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
