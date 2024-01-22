import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICancellationManagement, defaultValue } from 'app/shared/model/cancellation-management.model';

export const ACTION_TYPES = {
  FETCH_CANCELLATIONMANAGEMENT_LIST: 'cancellationManagement/FETCH_CANCELLATIONMANAGEMENT_LIST',
  FETCH_CANCELLATIONMANAGEMENT: 'cancellationManagement/FETCH_CANCELLATIONMANAGEMENT',
  CREATE_CANCELLATIONMANAGEMENT: 'cancellationManagement/CREATE_CANCELLATIONMANAGEMENT',
  UPDATE_CANCELLATIONMANAGEMENT: 'cancellationManagement/UPDATE_CANCELLATIONMANAGEMENT',
  DELETE_CANCELLATIONMANAGEMENT: 'cancellationManagement/DELETE_CANCELLATIONMANAGEMENT',
  SET_BLOB: 'cancellationManagement/SET_BLOB',
  RESET: 'cancellationManagement/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICancellationManagement>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CancellationManagementState = Readonly<typeof initialState>;

// Reducer

export default (state: CancellationManagementState = initialState, action): CancellationManagementState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CANCELLATIONMANAGEMENT):
    case REQUEST(ACTION_TYPES.UPDATE_CANCELLATIONMANAGEMENT):
    case REQUEST(ACTION_TYPES.DELETE_CANCELLATIONMANAGEMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT):
    case FAILURE(ACTION_TYPES.CREATE_CANCELLATIONMANAGEMENT):
    case FAILURE(ACTION_TYPES.UPDATE_CANCELLATIONMANAGEMENT):
    case FAILURE(ACTION_TYPES.DELETE_CANCELLATIONMANAGEMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CANCELLATIONMANAGEMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_CANCELLATIONMANAGEMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CANCELLATIONMANAGEMENT):
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

const apiUrl = 'api/cancellation-managements';

// Actions

export const getEntities: ICrudGetAllAction<ICancellationManagement> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT_LIST,
  payload: axios.get<ICancellationManagement>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICancellationManagement> = (id, shop?) => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT,
    payload: axios.get<ICancellationManagement>(requestUrl)
  };
};

export const getCustomerPortalCancelEntity: ICrudGetAction<ICancellationManagement> = (id, api_key?) => {
  const requestUrl = `api/cancellation-managements/${id}`;
  // const requestUrl = `api/external/v2/cancellation-managements/${id}?api_key=${window?.appstle_api_key}`;
  return {
    type: ACTION_TYPES.FETCH_CANCELLATIONMANAGEMENT,
    payload: axios.get<ICancellationManagement>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICancellationManagement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CANCELLATIONMANAGEMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICancellationManagement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CANCELLATIONMANAGEMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICancellationManagement> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CANCELLATIONMANAGEMENT,
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
