import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVariantInfo, defaultValue } from 'app/shared/model/variant-info.model';

export const ACTION_TYPES = {
  FETCH_VARIANTINFO_LIST: 'variantInfo/FETCH_VARIANTINFO_LIST',
  FETCH_VARIANTINFO: 'variantInfo/FETCH_VARIANTINFO',
  CREATE_VARIANTINFO: 'variantInfo/CREATE_VARIANTINFO',
  UPDATE_VARIANTINFO: 'variantInfo/UPDATE_VARIANTINFO',
  DELETE_VARIANTINFO: 'variantInfo/DELETE_VARIANTINFO',
  RESET: 'variantInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVariantInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type VariantInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: VariantInfoState = initialState, action): VariantInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_VARIANTINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VARIANTINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VARIANTINFO):
    case REQUEST(ACTION_TYPES.UPDATE_VARIANTINFO):
    case REQUEST(ACTION_TYPES.DELETE_VARIANTINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_VARIANTINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VARIANTINFO):
    case FAILURE(ACTION_TYPES.CREATE_VARIANTINFO):
    case FAILURE(ACTION_TYPES.UPDATE_VARIANTINFO):
    case FAILURE(ACTION_TYPES.DELETE_VARIANTINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_VARIANTINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_VARIANTINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VARIANTINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_VARIANTINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VARIANTINFO):
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

const apiUrl = 'api/variant-infos';

// Actions

export const getEntities: ICrudGetAllAction<IVariantInfo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_VARIANTINFO_LIST,
  payload: axios.get<IVariantInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IVariantInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VARIANTINFO,
    payload: axios.get<IVariantInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IVariantInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VARIANTINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IVariantInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VARIANTINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVariantInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VARIANTINFO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
