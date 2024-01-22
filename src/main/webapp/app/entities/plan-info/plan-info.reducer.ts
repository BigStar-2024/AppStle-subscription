import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPlanInfo, defaultValue } from 'app/shared/model/plan-info.model';

export const ACTION_TYPES = {
  FETCH_PLANINFO_LIST: 'planInfo/FETCH_PLANINFO_LIST',
  FETCH_PLANINFO: 'planInfo/FETCH_PLANINFO',
  CREATE_PLANINFO: 'planInfo/CREATE_PLANINFO',
  UPDATE_PLANINFO: 'planInfo/UPDATE_PLANINFO',
  DELETE_PLANINFO: 'planInfo/DELETE_PLANINFO',
  SET_BLOB: 'planInfo/SET_BLOB',
  RESET: 'planInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPlanInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
  planInfoListLoaded: false,
  validRequesting: false
};

export type PlanInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: PlanInfoState = initialState, action): PlanInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PLANINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PLANINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PLANINFO):
    case REQUEST(ACTION_TYPES.UPDATE_PLANINFO):
    case REQUEST(ACTION_TYPES.DELETE_PLANINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PLANINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PLANINFO):
    case FAILURE(ACTION_TYPES.CREATE_PLANINFO):
    case FAILURE(ACTION_TYPES.UPDATE_PLANINFO):
    case FAILURE(ACTION_TYPES.DELETE_PLANINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PLANINFO_LIST):
      return {
        ...state,
        loading: false,
        planInfoListLoaded: true,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PLANINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PLANINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_PLANINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PLANINFO):
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

const apiUrl = 'api/plan-infos';
const apiValidPlansUrl = 'api/shop-plan-infos';

// Actions

export const getEntities: ICrudGetAllAction<IPlanInfo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PLANINFO_LIST,
  payload: axios.get<IPlanInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getAllValidPlanInfos: ICrudGetAllAction<IPlanInfo> = discountCode => ({
  type: ACTION_TYPES.FETCH_PLANINFO_LIST,
  payload: axios.get<IPlanInfo>(`${apiValidPlansUrl}${discountCode ? `?discountCode=${discountCode}` : ''}`)
});

export const getEntity: ICrudGetAction<IPlanInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PLANINFO,
    payload: axios.get<IPlanInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPlanInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PLANINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPlanInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PLANINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPlanInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PLANINFO,
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
