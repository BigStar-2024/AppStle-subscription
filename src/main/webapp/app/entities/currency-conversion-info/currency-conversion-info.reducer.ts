import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICurrencyConversionInfo, defaultValue } from 'app/shared/model/currency-conversion-info.model';

export const ACTION_TYPES = {
  FETCH_CURRENCYCONVERSIONINFO_LIST: 'currencyConversionInfo/FETCH_CURRENCYCONVERSIONINFO_LIST',
  FETCH_CURRENCYCONVERSIONINFO: 'currencyConversionInfo/FETCH_CURRENCYCONVERSIONINFO',
  CREATE_CURRENCYCONVERSIONINFO: 'currencyConversionInfo/CREATE_CURRENCYCONVERSIONINFO',
  UPDATE_CURRENCYCONVERSIONINFO: 'currencyConversionInfo/UPDATE_CURRENCYCONVERSIONINFO',
  DELETE_CURRENCYCONVERSIONINFO: 'currencyConversionInfo/DELETE_CURRENCYCONVERSIONINFO',
  RESET: 'currencyConversionInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICurrencyConversionInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CurrencyConversionInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: CurrencyConversionInfoState = initialState, action): CurrencyConversionInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CURRENCYCONVERSIONINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CURRENCYCONVERSIONINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CURRENCYCONVERSIONINFO):
    case REQUEST(ACTION_TYPES.UPDATE_CURRENCYCONVERSIONINFO):
    case REQUEST(ACTION_TYPES.DELETE_CURRENCYCONVERSIONINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CURRENCYCONVERSIONINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CURRENCYCONVERSIONINFO):
    case FAILURE(ACTION_TYPES.CREATE_CURRENCYCONVERSIONINFO):
    case FAILURE(ACTION_TYPES.UPDATE_CURRENCYCONVERSIONINFO):
    case FAILURE(ACTION_TYPES.DELETE_CURRENCYCONVERSIONINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENCYCONVERSIONINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENCYCONVERSIONINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CURRENCYCONVERSIONINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_CURRENCYCONVERSIONINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CURRENCYCONVERSIONINFO):
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

const apiUrl = 'api/currency-conversion-infos';

// Actions

export const getEntities: ICrudGetAllAction<ICurrencyConversionInfo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CURRENCYCONVERSIONINFO_LIST,
  payload: axios.get<ICurrencyConversionInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICurrencyConversionInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CURRENCYCONVERSIONINFO,
    payload: axios.get<ICurrencyConversionInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICurrencyConversionInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CURRENCYCONVERSIONINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICurrencyConversionInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CURRENCYCONVERSIONINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICurrencyConversionInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CURRENCYCONVERSIONINFO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
