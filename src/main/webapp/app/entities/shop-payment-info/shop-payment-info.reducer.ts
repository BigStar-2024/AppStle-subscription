import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShopPaymentInfo, defaultValue } from 'app/shared/model/shop-payment-info.model';

export const ACTION_TYPES = {
  FETCH_SHOPPAYMENTINFO_LIST: 'shopPaymentInfo/FETCH_SHOPPAYMENTINFO_LIST',
  FETCH_SHOPPAYMENTINFO: 'shopPaymentInfo/FETCH_SHOPPAYMENTINFO',
  CREATE_SHOPPAYMENTINFO: 'shopPaymentInfo/CREATE_SHOPPAYMENTINFO',
  UPDATE_SHOPPAYMENTINFO: 'shopPaymentInfo/UPDATE_SHOPPAYMENTINFO',
  DELETE_SHOPPAYMENTINFO: 'shopPaymentInfo/DELETE_SHOPPAYMENTINFO',
  RESET: 'shopPaymentInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as IShopPaymentInfo,
  entity: JSON.parse(JSON.stringify(defaultValue)),
  updating: false,
  updateSuccess: false
};

export type ShopPaymentInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopPaymentInfoState = initialState, action): ShopPaymentInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHOPPAYMENTINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPPAYMENTINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPPAYMENTINFO):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPPAYMENTINFO):
    case REQUEST(ACTION_TYPES.DELETE_SHOPPAYMENTINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPPAYMENTINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPPAYMENTINFO):
    case FAILURE(ACTION_TYPES.CREATE_SHOPPAYMENTINFO):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPPAYMENTINFO):
    case FAILURE(ACTION_TYPES.DELETE_SHOPPAYMENTINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPPAYMENTINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPPAYMENTINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPPAYMENTINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPPAYMENTINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPPAYMENTINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/shop-payment-info';

// Actions

export const getEntities: ICrudGetAllAction<IShopPaymentInfo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SHOPPAYMENTINFO_LIST,
  payload: axios.get<IShopPaymentInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IShopPaymentInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPPAYMENTINFO,
    payload: axios.get<IShopPaymentInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopPaymentInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPPAYMENTINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopPaymentInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPPAYMENTINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShopPaymentInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPPAYMENTINFO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const getThemes = () => ({
  type: ACTION_TYPES.RESET
});
