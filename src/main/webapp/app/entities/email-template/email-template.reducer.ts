import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEmailTemplate, defaultValue } from 'app/shared/model/email-template.model';

export const ACTION_TYPES = {
  FETCH_EMAILTEMPLATE_LIST: 'emailTemplate/FETCH_EMAILTEMPLATE_LIST',
  FETCH_EMAILTEMPLATE: 'emailTemplate/FETCH_EMAILTEMPLATE',
  CREATE_EMAILTEMPLATE: 'emailTemplate/CREATE_EMAILTEMPLATE',
  UPDATE_EMAILTEMPLATE: 'emailTemplate/UPDATE_EMAILTEMPLATE',
  DELETE_EMAILTEMPLATE: 'emailTemplate/DELETE_EMAILTEMPLATE',
  SET_BLOB: 'emailTemplate/SET_BLOB',
  RESET: 'emailTemplate/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEmailTemplate>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type EmailTemplateState = Readonly<typeof initialState>;

// Reducer

export default (state: EmailTemplateState = initialState, action): EmailTemplateState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EMAILTEMPLATE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EMAILTEMPLATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EMAILTEMPLATE):
    case REQUEST(ACTION_TYPES.UPDATE_EMAILTEMPLATE):
    case REQUEST(ACTION_TYPES.DELETE_EMAILTEMPLATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EMAILTEMPLATE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EMAILTEMPLATE):
    case FAILURE(ACTION_TYPES.CREATE_EMAILTEMPLATE):
    case FAILURE(ACTION_TYPES.UPDATE_EMAILTEMPLATE):
    case FAILURE(ACTION_TYPES.DELETE_EMAILTEMPLATE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMAILTEMPLATE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMAILTEMPLATE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EMAILTEMPLATE):
    case SUCCESS(ACTION_TYPES.UPDATE_EMAILTEMPLATE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EMAILTEMPLATE):
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

const apiUrl = 'api/email-templates';

// Actions

export const getEntities: ICrudGetAllAction<IEmailTemplate> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_EMAILTEMPLATE_LIST,
  payload: axios.get<IEmailTemplate>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IEmailTemplate> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EMAILTEMPLATE,
    payload: axios.get<IEmailTemplate>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEmailTemplate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EMAILTEMPLATE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEmailTemplate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EMAILTEMPLATE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEmailTemplate> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EMAILTEMPLATE,
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
