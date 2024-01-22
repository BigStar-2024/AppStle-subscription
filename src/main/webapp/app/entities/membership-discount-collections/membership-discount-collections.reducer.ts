import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMembershipDiscountCollections, defaultValue } from 'app/shared/model/membership-discount-collections.model';

export const ACTION_TYPES = {
  FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS_LIST: 'membershipDiscountCollections/FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS_LIST',
  FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS: 'membershipDiscountCollections/FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS',
  CREATE_MEMBERSHIPDISCOUNTCOLLECTIONS: 'membershipDiscountCollections/CREATE_MEMBERSHIPDISCOUNTCOLLECTIONS',
  UPDATE_MEMBERSHIPDISCOUNTCOLLECTIONS: 'membershipDiscountCollections/UPDATE_MEMBERSHIPDISCOUNTCOLLECTIONS',
  DELETE_MEMBERSHIPDISCOUNTCOLLECTIONS: 'membershipDiscountCollections/DELETE_MEMBERSHIPDISCOUNTCOLLECTIONS',
  RESET: 'membershipDiscountCollections/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMembershipDiscountCollections>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type MembershipDiscountCollectionsState = Readonly<typeof initialState>;

// Reducer

export default (state: MembershipDiscountCollectionsState = initialState, action): MembershipDiscountCollectionsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNTCOLLECTIONS):
    case REQUEST(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNTCOLLECTIONS):
    case REQUEST(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNTCOLLECTIONS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS):
    case FAILURE(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNTCOLLECTIONS):
    case FAILURE(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNTCOLLECTIONS):
    case FAILURE(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNTCOLLECTIONS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNTCOLLECTIONS):
    case SUCCESS(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNTCOLLECTIONS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNTCOLLECTIONS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/membership-discount-collections';

// Actions

export const getEntities: ICrudGetAllAction<IMembershipDiscountCollections> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS_LIST,
  payload: axios.get<IMembershipDiscountCollections>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IMembershipDiscountCollections> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTCOLLECTIONS,
    payload: axios.get<IMembershipDiscountCollections>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMembershipDiscountCollections> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNTCOLLECTIONS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMembershipDiscountCollections> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNTCOLLECTIONS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMembershipDiscountCollections> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNTCOLLECTIONS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
