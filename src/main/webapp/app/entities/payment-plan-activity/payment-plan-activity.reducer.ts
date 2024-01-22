import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPaymentPlanActivity, defaultValue } from 'app/shared/model/payment-plan-activity.model';

export const ACTION_TYPES = {
  FETCH_PAYMENTPLANACTIVITY_LIST: 'paymentPlanActivity/FETCH_PAYMENTPLANACTIVITY_LIST',
  FETCH_PAYMENTPLANACTIVITY: 'paymentPlanActivity/FETCH_PAYMENTPLANACTIVITY',
  CREATE_PAYMENTPLANACTIVITY: 'paymentPlanActivity/CREATE_PAYMENTPLANACTIVITY',
  UPDATE_PAYMENTPLANACTIVITY: 'paymentPlanActivity/UPDATE_PAYMENTPLANACTIVITY',
  DELETE_PAYMENTPLANACTIVITY: 'paymentPlanActivity/DELETE_PAYMENTPLANACTIVITY',
  SET_BLOB: 'paymentPlanActivity/SET_BLOB',
  RESET: 'paymentPlanActivity/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPaymentPlanActivity>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PaymentPlanActivityState = Readonly<typeof initialState>;

// Reducer

export default (state: PaymentPlanActivityState = initialState, action): PaymentPlanActivityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTPLANACTIVITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTPLANACTIVITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PAYMENTPLANACTIVITY):
    case REQUEST(ACTION_TYPES.UPDATE_PAYMENTPLANACTIVITY):
    case REQUEST(ACTION_TYPES.DELETE_PAYMENTPLANACTIVITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTPLANACTIVITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTPLANACTIVITY):
    case FAILURE(ACTION_TYPES.CREATE_PAYMENTPLANACTIVITY):
    case FAILURE(ACTION_TYPES.UPDATE_PAYMENTPLANACTIVITY):
    case FAILURE(ACTION_TYPES.DELETE_PAYMENTPLANACTIVITY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTPLANACTIVITY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTPLANACTIVITY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAYMENTPLANACTIVITY):
    case SUCCESS(ACTION_TYPES.UPDATE_PAYMENTPLANACTIVITY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAYMENTPLANACTIVITY):
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

const apiUrl = 'api/payment-plan-activities';

// Actions

export const getEntities: ICrudGetAllAction<IPaymentPlanActivity> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTPLANACTIVITY_LIST,
    payload: axios.get<IPaymentPlanActivity>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPaymentPlanActivity> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTPLANACTIVITY,
    payload: axios.get<IPaymentPlanActivity>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPaymentPlanActivity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAYMENTPLANACTIVITY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPaymentPlanActivity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTPLANACTIVITY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPaymentPlanActivity> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAYMENTPLANACTIVITY,
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
