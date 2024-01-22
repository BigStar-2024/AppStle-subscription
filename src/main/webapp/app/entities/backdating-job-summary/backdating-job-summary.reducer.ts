import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBackdatingJobSummary, defaultValue } from 'app/shared/model/backdating-job-summary.model';

export const ACTION_TYPES = {
  FETCH_BACKDATINGJOBSUMMARY_LIST: 'backdatingJobSummary/FETCH_BACKDATINGJOBSUMMARY_LIST',
  FETCH_BACKDATINGJOBSUMMARY: 'backdatingJobSummary/FETCH_BACKDATINGJOBSUMMARY',
  CREATE_BACKDATINGJOBSUMMARY: 'backdatingJobSummary/CREATE_BACKDATINGJOBSUMMARY',
  UPDATE_BACKDATINGJOBSUMMARY: 'backdatingJobSummary/UPDATE_BACKDATINGJOBSUMMARY',
  DELETE_BACKDATINGJOBSUMMARY: 'backdatingJobSummary/DELETE_BACKDATINGJOBSUMMARY',
  UPDATE_FORM_BACKDATINGJOBSUMMARY: 'backdatingJobSummary/UPDATE_FORM_BACKDATINGJOBSUMMARY',
  FETCH_ORDERS_COUNT: 'backdatingJobSummary/FETCH_ORDERS_COUNT',
  RESET: 'backdatingJobSummary/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBackdatingJobSummary>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type BackdatingJobSummaryState = Readonly<typeof initialState>;

// Reducer

export default (state: BackdatingJobSummaryState = initialState, action): BackdatingJobSummaryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BACKDATINGJOBSUMMARY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BACKDATINGJOBSUMMARY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BACKDATINGJOBSUMMARY):
    case REQUEST(ACTION_TYPES.UPDATE_BACKDATINGJOBSUMMARY):
    case REQUEST(ACTION_TYPES.DELETE_BACKDATINGJOBSUMMARY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BACKDATINGJOBSUMMARY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BACKDATINGJOBSUMMARY):
    case FAILURE(ACTION_TYPES.CREATE_BACKDATINGJOBSUMMARY):
    case FAILURE(ACTION_TYPES.UPDATE_BACKDATINGJOBSUMMARY):
    case FAILURE(ACTION_TYPES.DELETE_BACKDATINGJOBSUMMARY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BACKDATINGJOBSUMMARY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_BACKDATINGJOBSUMMARY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BACKDATINGJOBSUMMARY):
    case SUCCESS(ACTION_TYPES.UPDATE_BACKDATINGJOBSUMMARY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BACKDATINGJOBSUMMARY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.UPDATE_FORM_BACKDATINGJOBSUMMARY:
      const form = action.payload;
      return {
        ...state,
        entity: { ...state.entity, ...form }
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERS_COUNT):
      return {
        ...state,
        entity: { ...state.entity, ordersCount: action.payload?.data?.orderCount }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/backdating-job-summaries';

// Actions

export const getEntities: ICrudGetAllAction<IBackdatingJobSummary> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_BACKDATINGJOBSUMMARY_LIST,
  payload: axios.get<IBackdatingJobSummary>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IBackdatingJobSummary> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BACKDATINGJOBSUMMARY,
    payload: axios.get<IBackdatingJobSummary>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBackdatingJobSummary> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BACKDATINGJOBSUMMARY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBackdatingJobSummary> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BACKDATINGJOBSUMMARY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBackdatingJobSummary> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BACKDATINGJOBSUMMARY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const onChangeForm: any = formData => dispatch => {
  return dispatch({
    type: ACTION_TYPES.UPDATE_FORM_BACKDATINGJOBSUMMARY,
    payload: formData
  });
};

export const getOrdersCount: any = dates => async dispatch => {
  const requestUrl =
    dates?.start && dates?.end && dates?.start !== 'Invalid date' && dates?.end !== 'Invalid date'
      ? `${apiUrl}/order-count?start=${dates?.start}&end=${dates?.end}`
      : `${apiUrl}/order-count`;
  return await dispatch({
    type: ACTION_TYPES.FETCH_ORDERS_COUNT,
    payload: axios.get(requestUrl)
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
