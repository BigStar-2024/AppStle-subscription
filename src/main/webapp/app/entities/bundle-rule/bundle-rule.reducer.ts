import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBundleRule, defaultValue } from 'app/shared/model/bundle-rule.model';

export const ACTION_TYPES = {
  FETCH_BUNDLERULE_LIST: 'bundleRule/FETCH_BUNDLERULE_LIST',
  FETCH_BUNDLERULE: 'bundleRule/FETCH_BUNDLERULE',
  CREATE_BUNDLERULE: 'bundleRule/CREATE_BUNDLERULE',
  UPDATE_BUNDLERULE: 'bundleRule/UPDATE_BUNDLERULE',
  DELETE_BUNDLERULE: 'bundleRule/DELETE_BUNDLERULE',
  UPDATE_STATUS: 'offer/UPDATE_STATUS',
  SET_BLOB: 'bundleRule/SET_BLOB',
  RESET: 'bundleRule/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBundleRule>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  updateStatusSuccess: false
};

export type BundleRuleState = Readonly<typeof initialState>;

// Reducer

export default (state: BundleRuleState = initialState, action): BundleRuleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BUNDLERULE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BUNDLERULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BUNDLERULE):
    case REQUEST(ACTION_TYPES.UPDATE_BUNDLERULE):
    case REQUEST(ACTION_TYPES.DELETE_BUNDLERULE):
    case REQUEST(ACTION_TYPES.UPDATE_STATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updateStatusSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BUNDLERULE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BUNDLERULE):
    case FAILURE(ACTION_TYPES.CREATE_BUNDLERULE):
    case FAILURE(ACTION_TYPES.UPDATE_BUNDLERULE):
    case FAILURE(ACTION_TYPES.DELETE_BUNDLERULE):
    case FAILURE(ACTION_TYPES.UPDATE_STATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        updateStatusSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BUNDLERULE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_BUNDLERULE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BUNDLERULE):
    case SUCCESS(ACTION_TYPES.UPDATE_BUNDLERULE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BUNDLERULE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.UPDATE_STATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: false,
        updateStatusSuccess: true,
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

const apiUrl = 'api/bundle-rules';

// Actions

export const getEntities: ICrudGetAllAction<IBundleRule> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BUNDLERULE_LIST,
    payload: axios.get<IBundleRule>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getBundleRulesByShop: ICrudGetAllAction<IBundleRule> = (page, size, sort) => {
  const requestUrl = `${apiUrl}/by-shop${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BUNDLERULE_LIST,
    payload: axios.get<IBundleRule>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IBundleRule> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BUNDLERULE,
    payload: axios.get<IBundleRule>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBundleRule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BUNDLERULE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getBundleRulesByShop());
  return result;
};

export const updateEntity: ICrudPutAction<IBundleRule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BUNDLERULE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBundleRule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BUNDLERULE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getBundleRulesByShop());
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

export const updateIndex = (id, sourceIndex, destinationIndex) => ({
  type: ACTION_TYPES.UPDATE_STATUS,
  payload: axios.put<IBundleRule>(`${apiUrl}/update-index/${id}/${sourceIndex}/${destinationIndex}?cacheBuster=${new Date().getTime()}`)
});

export const updateStatus = (id, status) => ({
  type: ACTION_TYPES.UPDATE_STATUS,
  payload: axios.put<IBundleRule>(`${apiUrl}/update-status/${id}/${status}?cacheBuster=${new Date().getTime()}`)
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
