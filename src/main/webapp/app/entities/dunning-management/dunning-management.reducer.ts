import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDunningManagement, defaultValue } from 'app/shared/model/dunning-management.model';

export const ACTION_TYPES = {
  FETCH_DUNNINGMANAGEMENT_LIST: 'dunningManagement/FETCH_DUNNINGMANAGEMENT_LIST',
  FETCH_DUNNINGMANAGEMENT: 'dunningManagement/FETCH_DUNNINGMANAGEMENT',
  CREATE_DUNNINGMANAGEMENT: 'dunningManagement/CREATE_DUNNINGMANAGEMENT',
  UPDATE_DUNNINGMANAGEMENT: 'dunningManagement/UPDATE_DUNNINGMANAGEMENT',
  DELETE_DUNNINGMANAGEMENT: 'dunningManagement/DELETE_DUNNINGMANAGEMENT',
  RESET: 'dunningManagement/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDunningManagement>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type DunningManagementState = Readonly<typeof initialState>;

// Reducer

export default (state: DunningManagementState = initialState, action): DunningManagementState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DUNNINGMANAGEMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DUNNINGMANAGEMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DUNNINGMANAGEMENT):
    case REQUEST(ACTION_TYPES.UPDATE_DUNNINGMANAGEMENT):
    case REQUEST(ACTION_TYPES.DELETE_DUNNINGMANAGEMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DUNNINGMANAGEMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DUNNINGMANAGEMENT):
    case FAILURE(ACTION_TYPES.CREATE_DUNNINGMANAGEMENT):
    case FAILURE(ACTION_TYPES.UPDATE_DUNNINGMANAGEMENT):
    case FAILURE(ACTION_TYPES.DELETE_DUNNINGMANAGEMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DUNNINGMANAGEMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DUNNINGMANAGEMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DUNNINGMANAGEMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_DUNNINGMANAGEMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DUNNINGMANAGEMENT):
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

const apiUrl = 'api/dunning-managements';

// Actions

export const getEntities: ICrudGetAllAction<IDunningManagement> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DUNNINGMANAGEMENT_LIST,
  payload: axios.get<IDunningManagement>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IDunningManagement> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DUNNINGMANAGEMENT,
    payload: axios.get<IDunningManagement>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDunningManagement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DUNNINGMANAGEMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDunningManagement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DUNNINGMANAGEMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDunningManagement> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DUNNINGMANAGEMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
