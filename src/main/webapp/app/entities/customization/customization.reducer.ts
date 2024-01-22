import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomization, defaultValue } from 'app/shared/model/customization.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMIZATION_LIST: 'customization/FETCH_CUSTOMIZATION_LIST',
  FETCH_CUSTOMIZATION: 'customization/FETCH_CUSTOMIZATION',
  CREATE_CUSTOMIZATION: 'customization/CREATE_CUSTOMIZATION',
  UPDATE_CUSTOMIZATION: 'customization/UPDATE_CUSTOMIZATION',
  DELETE_CUSTOMIZATION: 'customization/DELETE_CUSTOMIZATION',
  SET_BLOB: 'customization/SET_BLOB',
  RESET: 'customization/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomization>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CustomizationState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomizationState = initialState, action): CustomizationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMIZATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMIZATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMIZATION):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMIZATION):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMIZATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMIZATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMIZATION):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMIZATION):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMIZATION):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMIZATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMIZATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMIZATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMIZATION):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMIZATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMIZATION):
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

const apiUrl = 'api/customizations';

// Actions

export const getEntities: ICrudGetAllAction<ICustomization> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CUSTOMIZATION_LIST,
  payload: axios.get<ICustomization>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICustomization> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMIZATION,
    payload: axios.get<ICustomization>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomization> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMIZATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomization> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMIZATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomization> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMIZATION,
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
