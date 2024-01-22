import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMembershipDiscountProducts, defaultValue } from 'app/shared/model/membership-discount-products.model';

export const ACTION_TYPES = {
  FETCH_MEMBERSHIPDISCOUNTPRODUCTS_LIST: 'membershipDiscountProducts/FETCH_MEMBERSHIPDISCOUNTPRODUCTS_LIST',
  FETCH_MEMBERSHIPDISCOUNTPRODUCTS: 'membershipDiscountProducts/FETCH_MEMBERSHIPDISCOUNTPRODUCTS',
  CREATE_MEMBERSHIPDISCOUNTPRODUCTS: 'membershipDiscountProducts/CREATE_MEMBERSHIPDISCOUNTPRODUCTS',
  UPDATE_MEMBERSHIPDISCOUNTPRODUCTS: 'membershipDiscountProducts/UPDATE_MEMBERSHIPDISCOUNTPRODUCTS',
  DELETE_MEMBERSHIPDISCOUNTPRODUCTS: 'membershipDiscountProducts/DELETE_MEMBERSHIPDISCOUNTPRODUCTS',
  RESET: 'membershipDiscountProducts/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMembershipDiscountProducts>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type MembershipDiscountProductsState = Readonly<typeof initialState>;

// Reducer

export default (state: MembershipDiscountProductsState = initialState, action): MembershipDiscountProductsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTPRODUCTS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTPRODUCTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNTPRODUCTS):
    case REQUEST(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNTPRODUCTS):
    case REQUEST(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNTPRODUCTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTPRODUCTS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTPRODUCTS):
    case FAILURE(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNTPRODUCTS):
    case FAILURE(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNTPRODUCTS):
    case FAILURE(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNTPRODUCTS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTPRODUCTS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTPRODUCTS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNTPRODUCTS):
    case SUCCESS(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNTPRODUCTS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNTPRODUCTS):
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

const apiUrl = 'api/membership-discount-products';

// Actions

export const getEntities: ICrudGetAllAction<IMembershipDiscountProducts> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTPRODUCTS_LIST,
  payload: axios.get<IMembershipDiscountProducts>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IMembershipDiscountProducts> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNTPRODUCTS,
    payload: axios.get<IMembershipDiscountProducts>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMembershipDiscountProducts> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNTPRODUCTS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMembershipDiscountProducts> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNTPRODUCTS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMembershipDiscountProducts> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNTPRODUCTS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
