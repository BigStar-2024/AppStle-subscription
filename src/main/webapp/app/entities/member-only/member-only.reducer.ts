import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMemberOnly, defaultValue } from 'app/shared/model/member-only.model';

export const ACTION_TYPES = {
  FETCH_MEMBERONLY_LIST: 'memberOnly/FETCH_MEMBERONLY_LIST',
  FETCH_MEMBERONLY: 'memberOnly/FETCH_MEMBERONLY',
  CREATE_MEMBERONLY: 'memberOnly/CREATE_MEMBERONLY',
  UPDATE_MEMBERONLY: 'memberOnly/UPDATE_MEMBERONLY',
  DELETE_MEMBERONLY: 'memberOnly/DELETE_MEMBERONLY',
  RESET: 'memberOnly/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMemberOnly>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MemberOnlyState = Readonly<typeof initialState>;

// Reducer

export default (state: MemberOnlyState = initialState, action): MemberOnlyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEMBERONLY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEMBERONLY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MEMBERONLY):
    case REQUEST(ACTION_TYPES.UPDATE_MEMBERONLY):
    case REQUEST(ACTION_TYPES.DELETE_MEMBERONLY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MEMBERONLY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEMBERONLY):
    case FAILURE(ACTION_TYPES.CREATE_MEMBERONLY):
    case FAILURE(ACTION_TYPES.UPDATE_MEMBERONLY):
    case FAILURE(ACTION_TYPES.DELETE_MEMBERONLY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMBERONLY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMBERONLY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEMBERONLY):
    case SUCCESS(ACTION_TYPES.UPDATE_MEMBERONLY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEMBERONLY):
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

const apiUrl = 'api/member-onlies';

// Actions

export const getEntities: ICrudGetAllAction<IMemberOnly> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEMBERONLY_LIST,
  payload: axios.get<IMemberOnly>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMemberOnly> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEMBERONLY,
    payload: axios.get<IMemberOnly>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMemberOnly> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEMBERONLY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMemberOnly> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEMBERONLY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMemberOnly> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEMBERONLY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
