import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionCustomCss, defaultValue } from 'app/shared/model/subscription-custom-css.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONCUSTOMCSS_LIST: 'subscriptionCustomCss/FETCH_SUBSCRIPTIONCUSTOMCSS_LIST',
  FETCH_SUBSCRIPTIONCUSTOMCSS: 'subscriptionCustomCss/FETCH_SUBSCRIPTIONCUSTOMCSS',
  CREATE_SUBSCRIPTIONCUSTOMCSS: 'subscriptionCustomCss/CREATE_SUBSCRIPTIONCUSTOMCSS',
  UPDATE_SUBSCRIPTIONCUSTOMCSS: 'subscriptionCustomCss/UPDATE_SUBSCRIPTIONCUSTOMCSS',
  DELETE_SUBSCRIPTIONCUSTOMCSS: 'subscriptionCustomCss/DELETE_SUBSCRIPTIONCUSTOMCSS',
  SET_BLOB: 'subscriptionCustomCss/SET_BLOB',
  RESET: 'subscriptionCustomCss/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionCustomCss>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubscriptionCustomCssState = Readonly<typeof initialState>;

// Reducer

export default (
  state: SubscriptionCustomCssState = initialState,
  action: { type: any; payload: { data: any; name?: any; contentType?: any } }
): SubscriptionCustomCssState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONCUSTOMCSS):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONCUSTOMCSS):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONCUSTOMCSS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONCUSTOMCSS):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONCUSTOMCSS):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONCUSTOMCSS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONCUSTOMCSS):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONCUSTOMCSS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONCUSTOMCSS):
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

const apiUrl = 'api/subscription-custom-csses';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionCustomCss> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS_LIST,
  payload: axios.get<ISubscriptionCustomCss>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionCustomCss> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS,
    payload: axios.get<ISubscriptionCustomCss>(requestUrl)
  };
};

export const getEntityForCustomerPortal = (id: any, api_key: any) => {
  const requestUrl = `api/subscription-custom-csses/${id}`;
  // const requestUrl = `api/external/v2/subscription-custom-csses/${id}?api_key=${window?.appstle_api_key}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCUSTOMCSS,
    payload: axios.get<ISubscriptionCustomCss>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionCustomCss> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONCUSTOMCSS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionCustomCss> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONCUSTOMCSS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionCustomCss> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONCUSTOMCSS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name: any, data: any, contentType?: any) => ({
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
