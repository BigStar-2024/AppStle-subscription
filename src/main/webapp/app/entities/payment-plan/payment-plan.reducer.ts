import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { defaultPaymentPlanInformation, defaultValue, IPaymentPlan } from 'app/shared/model/payment-plan.model';

export const ACTION_TYPES = {
  FETCH_PAYMENTPLAN_LIST: 'paymentPlan/FETCH_PAYMENTPLAN_LIST',
  FETCH_PAYMENTPLAN: 'paymentPlan/FETCH_PAYMENTPLAN',
  CREATE_PAYMENTPLAN: 'paymentPlan/CREATE_PAYMENTPLAN',
  UPDATE_PAYMENTPLAN: 'paymentPlan/UPDATE_PAYMENTPLAN',
  DELETE_PAYMENTPLAN: 'paymentPlan/DELETE_PAYMENTPLAN',
  FETCH_PAYMENT_PLAN_LIMIT_INFORMATION: 'paymentPlan/FETCH_PAYMENT_PLAN_LIMIT_INFORMATION',
  FETCH_TEST_CHARGE_INFORMATION: 'paymentPlan/FETCH_TEST_CHARGE_INFORMATION',
  RESET: 'paymentPlan/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPaymentPlan>,
  entity: defaultValue,
  paymentPlanLimit: defaultPaymentPlanInformation,
  paymentPlanLimitLoaded: false,
  convertedTestShop: false,
  updating: false,
  updateSuccess: false,
  fetchTestChargeLoading: false
};

export type PaymentPlanState = Readonly<typeof initialState>;

// Reducer

export default (state: PaymentPlanState = initialState, action): PaymentPlanState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTPLAN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTPLAN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.FETCH_PAYMENT_PLAN_LIMIT_INFORMATION):
      return {
        ...state,
        paymentPlanLimitLoaded: false
      };
    case REQUEST(ACTION_TYPES.FETCH_TEST_CHARGE_INFORMATION):
    case REQUEST(ACTION_TYPES.CREATE_PAYMENTPLAN):
    case REQUEST(ACTION_TYPES.UPDATE_PAYMENTPLAN):
    case REQUEST(ACTION_TYPES.DELETE_PAYMENTPLAN):
      return {
        ...state,
        errorMessage: null,
        fetchTestChargeLoading: true,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTPLAN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTPLAN):
    case FAILURE(ACTION_TYPES.CREATE_PAYMENTPLAN):
    case FAILURE(ACTION_TYPES.UPDATE_PAYMENTPLAN):
    case FAILURE(ACTION_TYPES.DELETE_PAYMENTPLAN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.FETCH_PAYMENT_PLAN_LIMIT_INFORMATION):
      return {
        ...state,
        paymentPlanLimitLoaded: false
      };
    case FAILURE(ACTION_TYPES.FETCH_TEST_CHARGE_INFORMATION):
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTPLAN_LIST):
      return {
        ...state,
        loading: false,
        fetchTestChargeLoading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENT_PLAN_LIMIT_INFORMATION):
      return {
        ...state,
        loading: false,
        paymentPlanLimitLoaded: true,
        paymentPlanLimit: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEST_CHARGE_INFORMATION):
      return {
        ...state,
        loading: false,
        fetchTestChargeLoading: false,
        convertedTestShop: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTPLAN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAYMENTPLAN):
    case SUCCESS(ACTION_TYPES.UPDATE_PAYMENTPLAN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAYMENTPLAN):
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

const apiUrl = 'api/payment-plans';
const shopApiUrl = 'api/shop-payment-plan';

// Actions

export const getEntities: ICrudGetAllAction<IPaymentPlan> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PAYMENTPLAN_LIST,
  payload: axios.get<IPaymentPlan>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPaymentPlan> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTPLAN,
    payload: axios.get<IPaymentPlan>(requestUrl)
  };
};

export const getPaymentPlanByShop: ICrudGetAction<IPaymentPlan> = () => {
  const requestUrl = `${shopApiUrl}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTPLAN,
    payload: axios.get<IPaymentPlan>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPaymentPlan> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAYMENTPLAN,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  //   dispatch(getPaymentPlanDetails(''));
  return result;
};

export const updateEntity: ICrudPutAction<IPaymentPlan> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTPLAN,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPaymentPlan> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAYMENTPLAN,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const getPaymentPlanLimitInformation = () => {
  const requestUrl = `${apiUrl}/payment-plan-limit-information`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENT_PLAN_LIMIT_INFORMATION,
    payload: axios.get(requestUrl)
  };
};

export const getTestChargeInformation = () => {
  const requestUrl = `${apiUrl}/test-charge-information`;
  return {
    type: ACTION_TYPES.FETCH_TEST_CHARGE_INFORMATION,
    payload: axios.get(requestUrl)
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
