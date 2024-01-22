import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMembershipDiscount, defaultValue } from 'app/shared/model/membership-discount.model';
import _ from "lodash";

export const ACTION_TYPES = {
  FETCH_MEMBERSHIPDISCOUNT_LIST: 'membershipDiscount/FETCH_MEMBERSHIPDISCOUNT_LIST',
  FETCH_MEMBERSHIPDISCOUNT: 'membershipDiscount/FETCH_MEMBERSHIPDISCOUNT',
  CREATE_MEMBERSHIPDISCOUNT: 'membershipDiscount/CREATE_MEMBERSHIPDISCOUNT',
  UPDATE_MEMBERSHIPDISCOUNT: 'membershipDiscount/UPDATE_MEMBERSHIPDISCOUNT',
  DELETE_MEMBERSHIPDISCOUNT: 'membershipDiscount/DELETE_MEMBERSHIPDISCOUNT',
  RESET: 'membershipDiscount/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMembershipDiscount>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type MembershipDiscountState = Readonly<typeof initialState>;

// Reducer

export default (state: MembershipDiscountState = initialState, action): MembershipDiscountState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNT):
    case REQUEST(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNT):
    case REQUEST(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNT):
    case FAILURE(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNT):
    case FAILURE(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNT):
    case FAILURE(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNT):
      let products = [];
      let data = action.payload.data;
      if (data?.membershipDiscountProducts.length > 0) {
        _.each(data?.membershipDiscountProducts, o => {
          products.push({
            id: o.productId,
            title: o.productTitle
          })
        })
      }
      data.productIds = JSON.stringify(products);

      if (data.membershipDiscount.customerTags) {
        data.customerTagList = _.map(_.split(data.membershipDiscount.customerTags, ","), v => {
          return { label: v, value: v }
        })
      }
      return {
        ...state,
        loading: false,
        entity: data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNT):
    case SUCCESS(ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNT):
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

const apiUrl = 'api/membership-discounts';

// Actions

export const getEntities: ICrudGetAllAction<IMembershipDiscount> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNT_LIST,
  payload: axios.get<IMembershipDiscount>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IMembershipDiscount> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEMBERSHIPDISCOUNT,
    payload: axios.get<IMembershipDiscount>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMembershipDiscount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEMBERSHIPDISCOUNT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMembershipDiscount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEMBERSHIPDISCOUNT,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMembershipDiscount> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEMBERSHIPDISCOUNT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
