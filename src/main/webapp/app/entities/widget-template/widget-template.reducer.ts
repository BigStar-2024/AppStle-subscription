import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IWidgetTemplate, defaultValue } from 'app/shared/model/widget-template.model';

export const ACTION_TYPES = {
  FETCH_WIDGETTEMPLATE_LIST: 'widgetTemplate/FETCH_WIDGETTEMPLATE_LIST',
  FETCH_WIDGETTEMPLATE: 'widgetTemplate/FETCH_WIDGETTEMPLATE',
  CREATE_WIDGETTEMPLATE: 'widgetTemplate/CREATE_WIDGETTEMPLATE',
  UPDATE_WIDGETTEMPLATE: 'widgetTemplate/UPDATE_WIDGETTEMPLATE',
  DELETE_WIDGETTEMPLATE: 'widgetTemplate/DELETE_WIDGETTEMPLATE',
  SET_BLOB: 'widgetTemplate/SET_BLOB',
  RESET: 'widgetTemplate/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IWidgetTemplate>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type WidgetTemplateState = Readonly<typeof initialState>;

// Reducer

export default (state: WidgetTemplateState = initialState, action): WidgetTemplateState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_WIDGETTEMPLATE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_WIDGETTEMPLATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_WIDGETTEMPLATE):
    case REQUEST(ACTION_TYPES.UPDATE_WIDGETTEMPLATE):
    case REQUEST(ACTION_TYPES.DELETE_WIDGETTEMPLATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_WIDGETTEMPLATE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_WIDGETTEMPLATE):
    case FAILURE(ACTION_TYPES.CREATE_WIDGETTEMPLATE):
    case FAILURE(ACTION_TYPES.UPDATE_WIDGETTEMPLATE):
    case FAILURE(ACTION_TYPES.DELETE_WIDGETTEMPLATE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_WIDGETTEMPLATE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_WIDGETTEMPLATE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_WIDGETTEMPLATE):
    case SUCCESS(ACTION_TYPES.UPDATE_WIDGETTEMPLATE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_WIDGETTEMPLATE):
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

const apiUrl = 'api/widget-templates';

// Actions

export const getEntities: ICrudGetAllAction<IWidgetTemplate> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_WIDGETTEMPLATE_LIST,
  payload: axios.get<IWidgetTemplate>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IWidgetTemplate> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_WIDGETTEMPLATE,
    payload: axios.get<IWidgetTemplate>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IWidgetTemplate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_WIDGETTEMPLATE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IWidgetTemplate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_WIDGETTEMPLATE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IWidgetTemplate> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_WIDGETTEMPLATE,
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
