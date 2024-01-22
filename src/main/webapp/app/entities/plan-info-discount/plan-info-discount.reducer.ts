import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPlanInfoDiscount, defaultValue } from 'app/shared/model/plan-info-discount.model';

export const ACTION_TYPES = {
  FETCH_PLANINFODISCOUNT_LIST: 'planInfoDiscount/FETCH_PLANINFODISCOUNT_LIST',
  FETCH_PLANINFODISCOUNT: 'planInfoDiscount/FETCH_PLANINFODISCOUNT',
  CREATE_PLANINFODISCOUNT: 'planInfoDiscount/CREATE_PLANINFODISCOUNT',
  UPDATE_PLANINFODISCOUNT: 'planInfoDiscount/UPDATE_PLANINFODISCOUNT',
  DELETE_PLANINFODISCOUNT: 'planInfoDiscount/DELETE_PLANINFODISCOUNT',
  VALID_DISCOUNT_CODE: 'planInfoDiscount/VALID_DISCOUNT_CODE',
  SET_BLOB: 'planInfoDiscount/SET_BLOB',
  RESET: 'planInfoDiscount/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPlanInfoDiscount>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  validatingDIscountCode: false
};

export type PlanInfoDiscountState = Readonly<typeof initialState>;

// Reducer

export default (state: PlanInfoDiscountState = initialState, action): PlanInfoDiscountState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PLANINFODISCOUNT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PLANINFODISCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.VALID_DISCOUNT_CODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
        validatingDIscountCode: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PLANINFODISCOUNT):
    case REQUEST(ACTION_TYPES.UPDATE_PLANINFODISCOUNT):
    case REQUEST(ACTION_TYPES.DELETE_PLANINFODISCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PLANINFODISCOUNT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PLANINFODISCOUNT):
    case FAILURE(ACTION_TYPES.CREATE_PLANINFODISCOUNT):
    case FAILURE(ACTION_TYPES.UPDATE_PLANINFODISCOUNT):
    case FAILURE(ACTION_TYPES.DELETE_PLANINFODISCOUNT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.VALID_DISCOUNT_CODE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
        validatingDIscountCode: false
      };
    case SUCCESS(ACTION_TYPES.FETCH_PLANINFODISCOUNT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PLANINFODISCOUNT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.VALID_DISCOUNT_CODE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
        validatingDIscountCode: false
      };
    case SUCCESS(ACTION_TYPES.CREATE_PLANINFODISCOUNT):
    case SUCCESS(ACTION_TYPES.UPDATE_PLANINFODISCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PLANINFODISCOUNT):
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

const apiUrl = 'api/plan-info-discounts';

// Actions

export const getEntities: ICrudGetAllAction<IPlanInfoDiscount> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PLANINFODISCOUNT_LIST,
    payload: axios.get<IPlanInfoDiscount>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPlanInfoDiscount> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PLANINFODISCOUNT,
    payload: axios.get<IPlanInfoDiscount>(requestUrl)
  };
};

export const validateDiscountCode: ICrudGetAllAction<IPlanInfoDiscount> = discountCode => ({
  type: ACTION_TYPES.VALID_DISCOUNT_CODE,
  payload: axios.get<IPlanInfoDiscount>(`${apiUrl}/validate${discountCode ? `?discountCode=${discountCode}` : ''}`)
});

export const createEntity: ICrudPutAction<IPlanInfoDiscount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PLANINFODISCOUNT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPlanInfoDiscount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PLANINFODISCOUNT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPlanInfoDiscount> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PLANINFODISCOUNT,
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
