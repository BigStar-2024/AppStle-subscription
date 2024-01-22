import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomerPayment, defaultValue } from 'app/shared/model/customer-payment.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMERPAYMENT_LIST: 'customerPayment/FETCH_CUSTOMERPAYMENT_LIST',
  FETCH_CUSTOMERPAYMENT: 'customerPayment/FETCH_CUSTOMERPAYMENT',
  CREATE_CUSTOMERPAYMENT: 'customerPayment/CREATE_CUSTOMERPAYMENT',
  UPDATE_CUSTOMERPAYMENT: 'customerPayment/UPDATE_CUSTOMERPAYMENT',
  DELETE_CUSTOMERPAYMENT: 'customerPayment/DELETE_CUSTOMERPAYMENT',
  RESET: 'customerPayment/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerPayment>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CustomerPaymentState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerPaymentState = initialState, action): CustomerPaymentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERPAYMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERPAYMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERPAYMENT):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERPAYMENT):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERPAYMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERPAYMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERPAYMENT):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERPAYMENT):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERPAYMENT):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERPAYMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERPAYMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERPAYMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERPAYMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERPAYMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERPAYMENT):
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

const apiUrl = 'api/customer-payments';

// Actions

export const getEntities: ICrudGetAllAction<ICustomerPayment> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CUSTOMERPAYMENT_LIST,
  payload: axios.get<ICustomerPayment>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICustomerPayment> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERPAYMENT,
    payload: axios.get<ICustomerPayment>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomerPayment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERPAYMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerPayment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERPAYMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerPayment> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERPAYMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
