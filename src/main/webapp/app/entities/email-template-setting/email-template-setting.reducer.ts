import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IEmailTemplateSetting } from 'app/shared/model/email-template-setting.model';

export const ACTION_TYPES = {
  FETCH_EMAILTEMPLATESETTING_LIST: 'emailTemplateSetting/FETCH_EMAILTEMPLATESETTING_LIST',
  FETCH_EMAILTEMPLATESETTING: 'emailTemplateSetting/FETCH_EMAILTEMPLATESETTING',
  CREATE_EMAILTEMPLATESETTING: 'emailTemplateSetting/CREATE_EMAILTEMPLATESETTING',
  UPDATE_EMAILTEMPLATESETTING: 'emailTemplateSetting/UPDATE_EMAILTEMPLATESETTING',
  DELETE_EMAILTEMPLATESETTING: 'emailTemplateSetting/DELETE_EMAILTEMPLATESETTING',
  RESET_EMAILTEMPLATESETTING: 'emailTemplateSetting/RESET_EMAILTEMPLATESETTING',
  SET_BLOB: 'emailTemplateSetting/SET_BLOB',
  RESET: 'emailTemplateSetting/RESET',
  UPDATE_CHECKBOX: 'emailTemplateSetting/UPDATE_CHECKBOX',
  FETCH_EMAILTEMPLATE_PREVIEW: 'emailTemplateSetting/FETCH_EMAILTEMPLATE_PREVIEW',
  UPDATE_BULK_EMAILTEMPLATESETTING: 'emailTemplateSetting/UPDATE_BULK_EMAILTEMPLATESETTING'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEmailTemplateSetting>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  emailTemplatePreviewLoading: false,
  emailTemplatePreviewHTML: '',
  resetEmailTemplateSetting: false
};

export type EmailTemplateSettingState = Readonly<typeof initialState>;

// Reducer

export default (state: EmailTemplateSettingState = initialState, action): EmailTemplateSettingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EMAILTEMPLATESETTING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EMAILTEMPLATESETTING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EMAILTEMPLATESETTING):
    case REQUEST(ACTION_TYPES.UPDATE_EMAILTEMPLATESETTING):
    case REQUEST(ACTION_TYPES.DELETE_EMAILTEMPLATESETTING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case REQUEST(ACTION_TYPES.FETCH_EMAILTEMPLATE_PREVIEW):
      return {
        ...state,
        emailTemplatePreviewLoading: true
      };
    case REQUEST(ACTION_TYPES.RESET_EMAILTEMPLATESETTING):
      return {
        ...state,
        resetEmailTemplateSetting: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EMAILTEMPLATESETTING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EMAILTEMPLATESETTING):
    case FAILURE(ACTION_TYPES.CREATE_EMAILTEMPLATESETTING):
    case FAILURE(ACTION_TYPES.UPDATE_EMAILTEMPLATESETTING):
    case FAILURE(ACTION_TYPES.DELETE_EMAILTEMPLATESETTING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.FETCH_EMAILTEMPLATE_PREVIEW):
      return {
        ...state,
        emailTemplatePreviewLoading: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.RESET_EMAILTEMPLATESETTING):
      return {
        ...state,
        resetEmailTemplateSetting: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMAILTEMPLATESETTING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMAILTEMPLATESETTING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EMAILTEMPLATESETTING):
    case SUCCESS(ACTION_TYPES.UPDATE_EMAILTEMPLATESETTING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.UPDATE_BULK_EMAILTEMPLATESETTING):
    case SUCCESS(ACTION_TYPES.DELETE_EMAILTEMPLATESETTING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMAILTEMPLATE_PREVIEW):
      return {
        ...state,
        emailTemplatePreviewLoading: false,
        emailTemplatePreviewHTML: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.RESET_EMAILTEMPLATESETTING):
      return {
        ...state,
        resetEmailTemplateSetting: false
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
            return { ...entity, sendEmailDisabled: action.payload.checked };
          } else {
            return { ...entity };
          }
        })
      };
    default:
      return state;
  }
};

const apiUrl = 'api/email-template-settings';

// Actions

export const getEntities: ICrudGetAllAction<IEmailTemplateSetting> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EMAILTEMPLATESETTING_LIST,
    payload: axios.get<IEmailTemplateSetting>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEmailTemplateSetting> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EMAILTEMPLATESETTING,
    payload: axios.get<IEmailTemplateSetting>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEmailTemplateSetting> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EMAILTEMPLATESETTING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEmailTemplateSetting> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EMAILTEMPLATESETTING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEmailTemplateSetting> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EMAILTEMPLATESETTING,
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

export const resetEmailTemplateSettings = (emailTemplateId, emailSettingType) => async dispatch => {
  const requestUrl = `${apiUrl}/reset/${emailTemplateId}?emailSettingType=${emailSettingType}`;
  const result = await dispatch({
    type: ACTION_TYPES.RESET_EMAILTEMPLATESETTING,
    payload: axios.post(requestUrl)
  });
  return result;
};

// let cancelToken;
export const fetchEmailTemplatePreviewHTML = formState => async dispatch => {
  const requestUrl = `${apiUrl}/preview`;
  // if (typeof cancelToken != typeof undefined) {
  //   cancelToken.cancel('Operation canceled due to new request.');
  // }
  // cancelToken = axios.CancelToken.source();
  const result = await dispatch({
    type: ACTION_TYPES.FETCH_EMAILTEMPLATE_PREVIEW,
    payload: axios.post(requestUrl, formState)
  });
  return result;
};

export const updateBulkEmailTemplateEntity = (propertyName, propertyValue) => async dispatch => {
  const requestUrl = `${apiUrl}/update-bulk-email-templates-property`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BULK_EMAILTEMPLATESETTING,
    payload: axios.put(requestUrl, { propertyName, propertyValue })
  });
  return result;
};
