import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomerPortalDynamicScript, defaultValue } from 'app/shared/model/customer-portal-dynamic-script.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMERPORTALDYNAMICSCRIPT_LIST: 'customerPortalDynamicScript/FETCH_CUSTOMERPORTALDYNAMICSCRIPT_LIST',
  FETCH_CUSTOMERPORTALDYNAMICSCRIPT: 'customerPortalDynamicScript/FETCH_CUSTOMERPORTALDYNAMICSCRIPT',
  CREATE_CUSTOMERPORTALDYNAMICSCRIPT: 'customerPortalDynamicScript/CREATE_CUSTOMERPORTALDYNAMICSCRIPT',
  UPDATE_CUSTOMERPORTALDYNAMICSCRIPT: 'customerPortalDynamicScript/UPDATE_CUSTOMERPORTALDYNAMICSCRIPT',
  DELETE_CUSTOMERPORTALDYNAMICSCRIPT: 'customerPortalDynamicScript/DELETE_CUSTOMERPORTALDYNAMICSCRIPT',
  SET_BLOB: 'customerPortalDynamicScript/SET_BLOB',
  RESET: 'customerPortalDynamicScript/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerPortalDynamicScript>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CustomerPortalDynamicScriptState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerPortalDynamicScriptState = initialState, action): CustomerPortalDynamicScriptState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERPORTALDYNAMICSCRIPT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERPORTALDYNAMICSCRIPT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERPORTALDYNAMICSCRIPT):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERPORTALDYNAMICSCRIPT):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERPORTALDYNAMICSCRIPT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERPORTALDYNAMICSCRIPT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERPORTALDYNAMICSCRIPT):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERPORTALDYNAMICSCRIPT):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERPORTALDYNAMICSCRIPT):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERPORTALDYNAMICSCRIPT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERPORTALDYNAMICSCRIPT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERPORTALDYNAMICSCRIPT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERPORTALDYNAMICSCRIPT):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERPORTALDYNAMICSCRIPT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERPORTALDYNAMICSCRIPT):
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

const apiUrl = 'api/customer-portal-dynamic-scripts';

// Actions

export const getEntities: ICrudGetAllAction<ICustomerPortalDynamicScript> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CUSTOMERPORTALDYNAMICSCRIPT_LIST,
  payload: axios.get<ICustomerPortalDynamicScript>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICustomerPortalDynamicScript> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERPORTALDYNAMICSCRIPT,
    payload: axios.get<ICustomerPortalDynamicScript>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomerPortalDynamicScript> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERPORTALDYNAMICSCRIPT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerPortalDynamicScript> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERPORTALDYNAMICSCRIPT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerPortalDynamicScript> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERPORTALDYNAMICSCRIPT,
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
