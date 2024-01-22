import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISmsTemplateSetting, defaultValue } from 'app/shared/model/sms-template-setting.model';

export const ACTION_TYPES = {
  FETCH_SMSTEMPLATESETTING_LIST: 'smsTemplateSetting/FETCH_SMSTEMPLATESETTING_LIST',
  FETCH_SMSTEMPLATESETTING: 'smsTemplateSetting/FETCH_SMSTEMPLATESETTING',
  CREATE_SMSTEMPLATESETTING: 'smsTemplateSetting/CREATE_SMSTEMPLATESETTING',
  UPDATE_SMSTEMPLATESETTING: 'smsTemplateSetting/UPDATE_SMSTEMPLATESETTING',
  DELETE_SMSTEMPLATESETTING: 'smsTemplateSetting/DELETE_SMSTEMPLATESETTING',
  SET_BLOB: 'smsTemplateSetting/SET_BLOB',
  RESET: 'smsTemplateSetting/RESET',
  UPDATE_CHECKBOX: 'automation/UPDATE_CHECKBOX',
  FETCH_SMS_PREVIEW_TEXT: 'automation/FETCH_SMS_PREVIEW_TEXT'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISmsTemplateSetting>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
  smsPreviewText: ''
};

export type SmsTemplateSettingState = Readonly<typeof initialState>;

// Reducer

export default (state: SmsTemplateSettingState = initialState, action): SmsTemplateSettingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SMSTEMPLATESETTING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SMSTEMPLATESETTING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SMSTEMPLATESETTING):
    case REQUEST(ACTION_TYPES.UPDATE_SMSTEMPLATESETTING):
    case REQUEST(ACTION_TYPES.DELETE_SMSTEMPLATESETTING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SMSTEMPLATESETTING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SMSTEMPLATESETTING):
    case FAILURE(ACTION_TYPES.CREATE_SMSTEMPLATESETTING):
    case FAILURE(ACTION_TYPES.UPDATE_SMSTEMPLATESETTING):
    case FAILURE(ACTION_TYPES.DELETE_SMSTEMPLATESETTING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SMS_PREVIEW_TEXT):
      return {
        ...state,
        smsPreviewText: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SMSTEMPLATESETTING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SMSTEMPLATESETTING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SMSTEMPLATESETTING):
    case SUCCESS(ACTION_TYPES.UPDATE_SMSTEMPLATESETTING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SMSTEMPLATESETTING):
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
    case ACTION_TYPES.UPDATE_CHECKBOX:
      return {
        ...state,
        entities: state.entities.map(entity => {
          if (parseInt(action.payload.id) === entity.id) {
            return { ...entity, sendSmsDisabled: action.payload.checked };
          } else {
            return { ...entity };
          }
        })
      };
    default:
      return state;
  }
};

const apiUrl = 'api/sms-template-settings';

// Actions

export const getEntities: ICrudGetAllAction<ISmsTemplateSetting> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SMSTEMPLATESETTING_LIST,
  payload: axios.get<ISmsTemplateSetting>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISmsTemplateSetting> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SMSTEMPLATESETTING,
    payload: axios.get<ISmsTemplateSetting>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISmsTemplateSetting> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SMSTEMPLATESETTING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISmsTemplateSetting> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SMSTEMPLATESETTING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISmsTemplateSetting> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SMSTEMPLATESETTING,
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

export const updateCheckbox = (id, checked) => ({
  type: ACTION_TYPES.UPDATE_CHECKBOX,
  payload: {
    id,
    checked
  }
});

export const fetchSMSPreviewText = formState => async dispatch => {
  const requestUrl = `${apiUrl}/preview`;
  const result = await dispatch({
    type: ACTION_TYPES.FETCH_SMS_PREVIEW_TEXT,
    payload: axios.post(requestUrl, formState)
  });
  return result;
};
