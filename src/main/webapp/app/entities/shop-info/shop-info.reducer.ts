import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IShopInfo } from 'app/shared/model/shop-info.model';

export const ACTION_TYPES = {
  FETCH_SHOPINFO_LIST: 'shopInfo/FETCH_SHOPINFO_LIST',
  FETCH_EMAIL_CUSTOM_DOMAIN: 'shopInfo/FETCH_EMAIL_CUSTOM_DOMAIN',
  FETCH_SHOPINFO: 'shopInfo/FETCH_SHOPINFO',
  CREATE_SHOPINFO: 'shopInfo/CREATE_SHOPINFO',
  UPDATE_SHOPINFO: 'shopInfo/UPDATE_SHOPINFO',
  DELETE_SHOPINFO: 'shopInfo/DELETE_SHOPINFO',
  CREATE_KLAVIYO_SAMPLE_TEMPLATE: 'shopInfo/CREATE_KLAVIYO_SAMPLE_TEMPLATE',
  RESET: 'shopInfo/RESET',
  TRIGGER_KLAVIYO_SAMPLE_EVENT: 'shopInfo/TRIGGER_KLAVIYO_SAMPLE_EVENT',
  REGENERATE_APIKEY: 'shopInfo/REGENERATE_APIKEY',
  FETCH_WEBHOOK_PORTAL: 'shopInfo/FETCH_WEBHOOK_PORTAL',
  SET_APP_EMBED: 'shopSettings/SET_APP_EMBED',
  FETCH_MISSING_ACCESS_SCOPE: 'shopSettings/FETCH_MISSING_ACCESS_SCOPE'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShopInfo>,
  entity: defaultValue,
  updating: false,
  klaviyoTemplate: null,
  klaviyoEventTriggered: null,
  customDomain: null,
  updateSuccess: false,
  updatingAPIKey: false,
  webhookPortal: null,
  loadingWebhookPortal: false,
  isAppEmbed: true,
  missingAccessScopes: []
};

export type ShopInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopInfoState = initialState, action): ShopInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHOPINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPINFO):
    case REQUEST(ACTION_TYPES.FETCH_EMAIL_CUSTOM_DOMAIN):
    case REQUEST(ACTION_TYPES.TRIGGER_KLAVIYO_SAMPLE_EVENT):
    case REQUEST(ACTION_TYPES.FETCH_MISSING_ACCESS_SCOPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.FETCH_WEBHOOK_PORTAL):
      return {
        ...state,
        webhookPortal: null,
        loadingWebhookPortal: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPINFO):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPINFO):
    case REQUEST(ACTION_TYPES.DELETE_SHOPINFO):
    case REQUEST(ACTION_TYPES.CREATE_KLAVIYO_SAMPLE_TEMPLATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case REQUEST(ACTION_TYPES.REGENERATE_APIKEY):
      return {
        ...state,
        updatingAPIKey: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPINFO):
    case FAILURE(ACTION_TYPES.CREATE_SHOPINFO):
    case FAILURE(ACTION_TYPES.CREATE_KLAVIYO_SAMPLE_TEMPLATE):
    case FAILURE(ACTION_TYPES.TRIGGER_KLAVIYO_SAMPLE_EVENT):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPINFO):
    case FAILURE(ACTION_TYPES.REGENERATE_APIKEY):
    case FAILURE(ACTION_TYPES.DELETE_SHOPINFO):
    case FAILURE(ACTION_TYPES.FETCH_EMAIL_CUSTOM_DOMAIN):
    case FAILURE(ACTION_TYPES.FETCH_MISSING_ACCESS_SCOPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        updatingAPIKey: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.FETCH_WEBHOOK_PORTAL):
      return {
        ...state,
        loadingWebhookPortal: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_WEBHOOK_PORTAL):
      return {
        ...state,
        loadingWebhookPortal: false,
        webhookPortal: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMAIL_CUSTOM_DOMAIN):
      return {
        ...state,
        loading: false,
        customDomain: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPINFO):
      return {
        ...state,
        loading: false,
        entity: {
          ...action.payload.data,
          localOrderMinute: convertValue(action.payload.data, 'localOrderMinute'),
          localOrderHour: convertValue(action.payload.data, 'localOrderHour'),
          zoneOffsetMinutes: convertValue(action.payload.data, 'zoneOffsetMinutes'),
          zoneOffsetHours: convertValue(action.payload.data, 'zoneOffsetHours')
        }
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {
          ...action.payload.data,
          localOrderMinute: convertValue(action.payload.data, 'localOrderMinute'),
          localOrderHour: convertValue(action.payload.data, 'localOrderHour'),
          zoneOffsetMinutes: convertValue(action.payload.data, 'zoneOffsetMinutes'),
          zoneOffsetHours: convertValue(action.payload.data, 'zoneOffsetHours')
        }
      };
    case SUCCESS(ACTION_TYPES.REGENERATE_APIKEY):
      return {
        ...state,
        updatingAPIKey: false,
        updateSuccess: true,
        updating: false,
        entity: {
          ...action.payload.data,
          localOrderMinute: convertValue(action.payload.data, 'localOrderMinute'),
          localOrderHour: convertValue(action.payload.data, 'localOrderHour'),
          zoneOffsetMinutes: convertValue(action.payload.data, 'zoneOffsetMinutes'),
          zoneOffsetHours: convertValue(action.payload.data, 'zoneOffsetHours')
        }
      };
    case SUCCESS(ACTION_TYPES.CREATE_KLAVIYO_SAMPLE_TEMPLATE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        klaviyoTemplate: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.TRIGGER_KLAVIYO_SAMPLE_EVENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        klaviyoEventTriggered: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.FETCH_MISSING_ACCESS_SCOPE):
      return {
        ...state,
        loading: false,
        missingAccessScopes: action.payload.data
      };
    case ACTION_TYPES.SET_APP_EMBED:
      return {
        ...state,
        isAppEmbed: action.payload
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
        isAppEmbed: state.isAppEmbed
      };
    default:
      return state;
  }
};

const apiUrl = 'api/shop-infos';

// Actions

export const getEntities: ICrudGetAllAction<IShopInfo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SHOPINFO_LIST,
  payload: axios.get<IShopInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IShopInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPINFO,
    payload: axios.get<IShopInfo>(requestUrl)
  };
};

export const getShopInfoByCurrentLogin: ICrudGetAction<IShopInfo> = () => {
  const requestUrl = `api/shop-infos-by-current-login`;
  return {
    type: ACTION_TYPES.FETCH_SHOPINFO,
    payload: axios.get<IShopInfo>(requestUrl)
  };
};

export const getShopInfoEntity: ICrudGetAction<IShopInfo> = shop => {
  const requestUrl = `api/shop-infos-by-shop/${shop}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPINFO,
    payload: axios.get<IShopInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEmailCustomDomain());
  dispatch(triggerKlavioSampleEvent());
  return result;
};

export const regenerateApiKeyEntity: ICrudPutAction<IShopInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.REGENERATE_APIKEY,
    payload: axios.put(`api/shop-info/regenerate-key`)
  });
  return result;
};

export const triggerKlavioSampleEvent = () => {
  const requestUrl = `api/klaviyo/trigger-sample-event`;
  return {
    type: ACTION_TYPES.TRIGGER_KLAVIYO_SAMPLE_EVENT,
    payload: axios.get(`${requestUrl}`)
  };
};

export const createKlavioSampleTemplate = () => {
  const requestUrl = `api/klaviyo/create-sample-template`;
  return {
    type: ACTION_TYPES.CREATE_KLAVIYO_SAMPLE_TEMPLATE,
    payload: axios.post(`${requestUrl}`)
  };
};

export const deleteEntity: ICrudDeleteAction<IShopInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPINFO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const getEmailCustomDomain = () => {
  const requestUrl = `${apiUrl}/email-custom-domain`;
  return {
    type: ACTION_TYPES.FETCH_EMAIL_CUSTOM_DOMAIN,
    payload: axios.get(`${requestUrl}`)
  };
};

export const getWebhookPortal = () => {
  const requestUrl = `${apiUrl}/webhook-portal`;
  return {
    type: ACTION_TYPES.FETCH_WEBHOOK_PORTAL,
    payload: axios.get(`${requestUrl}`)
  };
};

export const getThemes = () => ({
  type: ACTION_TYPES.RESET
});

const convertValue = (data: any, filedName: any) => {
  let value = '';
  if (data[filedName] === 0) {
    value = String(data[filedName]);
  } else if (data[filedName]) {
    value = String(data[filedName]);
  } else {
    value = '';
  }
  return value;
};

export const setAppEmbed = (isAppEmbed: boolean) => {
  return {
    type: ACTION_TYPES.SET_APP_EMBED,
    payload: isAppEmbed
  };
};

export const getMissingAccessScopes = () => {
  const requestUrl = `api/shop-missing-access-scopes`;
  return {
    type: ACTION_TYPES.FETCH_MISSING_ACCESS_SCOPE,
    payload: axios.get(`${requestUrl}`)
  };
};
