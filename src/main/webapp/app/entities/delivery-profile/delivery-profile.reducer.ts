import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IDeliveryProfile } from 'app/shared/model/delivery-profile.model';

export const ACTION_TYPES = {
  FETCH_DELIVERYPROFILE_LIST: 'deliveryProfile/FETCH_DELIVERYPROFILE_LIST',
  FETCH_DELIVERYPROFILE: 'deliveryProfile/FETCH_DELIVERYPROFILE',
  CREATE_DELIVERYPROFILE: 'deliveryProfile/CREATE_DELIVERYPROFILE',
  CREATE_FREEDELIVERYPROFILE: 'deliveryProfile/CREATE_FREEDELIVERYPROFILE',
  UPDATE_DELIVERYPROFILE: 'deliveryProfile/UPDATE_DELIVERYPROFILE',
  UPDATE_FREEDELIVERYPROFILE: 'deliveryProfile/UPDATE_FREEDELIVERYPROFILE',
  DELETE_DELIVERYPROFILE: 'deliveryProfile/DELETE_DELIVERYPROFILE',
  RESET: 'deliveryProfile/RESET',
  UPDATE_SHIPPING_PROFILE: 'deliveryProfile/UPDATE_SHIPPING_PROFILE'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDeliveryProfile>,
  entity: defaultValue,
  updating: false,
  deleting: false,
  updateSuccess: false
};

export type DeliveryProfileState = Readonly<typeof initialState>;

// Reducer

export default (state: DeliveryProfileState = initialState, action): DeliveryProfileState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DELIVERYPROFILE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DELIVERYPROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DELIVERYPROFILE):
    case REQUEST(ACTION_TYPES.CREATE_FREEDELIVERYPROFILE):
    case REQUEST(ACTION_TYPES.UPDATE_DELIVERYPROFILE):
    case REQUEST(ACTION_TYPES.UPDATE_FREEDELIVERYPROFILE):
    case REQUEST(ACTION_TYPES.DELETE_DELIVERYPROFILE):
      return {
        ...state,
        deleting: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_SHIPPING_PROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DELIVERYPROFILE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DELIVERYPROFILE):
    case FAILURE(ACTION_TYPES.CREATE_DELIVERYPROFILE):
    case FAILURE(ACTION_TYPES.CREATE_FREEDELIVERYPROFILE):
    case FAILURE(ACTION_TYPES.UPDATE_DELIVERYPROFILE):
    case FAILURE(ACTION_TYPES.UPDATE_FREEDELIVERYPROFILE):
    case FAILURE(ACTION_TYPES.DELETE_DELIVERYPROFILE):
      return {
        ...state,
        deleting: false
      };
    case FAILURE(ACTION_TYPES.UPDATE_SHIPPING_PROFILE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DELIVERYPROFILE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DELIVERYPROFILE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DELIVERYPROFILE):
    case SUCCESS(ACTION_TYPES.CREATE_FREEDELIVERYPROFILE):
    case SUCCESS(ACTION_TYPES.UPDATE_DELIVERYPROFILE):
    case SUCCESS(ACTION_TYPES.UPDATE_FREEDELIVERYPROFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DELIVERYPROFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        deleting: false,
        entity: {}
      };
    case ACTION_TYPES.UPDATE_SHIPPING_PROFILE:
      return {
        ...state,
        entity: action.payload
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/delivery-profiles';

// Actions

export const getEntities: ICrudGetAllAction<IDeliveryProfile> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DELIVERYPROFILE_LIST,
  payload: axios.get<IDeliveryProfile>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IDeliveryProfile> = id => {
  const requestUrl = `${apiUrl}/detailed/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DELIVERYPROFILE,
    payload: axios.get<IDeliveryProfile>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDeliveryProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DELIVERYPROFILE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const createShippingEntity: ICrudPutAction<IDeliveryProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FREEDELIVERYPROFILE,
    payload: axios.post(`${apiUrl}/create-shipping-profile`, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const createShippingEntityLatest: ICrudPutAction<IDeliveryProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FREEDELIVERYPROFILE,
    payload: axios.post(`${apiUrl}/v2/create-shipping-profile`, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateShippingEntity: ICrudPutAction<IDeliveryProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FREEDELIVERYPROFILE,
    payload: axios.put(`${apiUrl}/update-shipping-seller-group-ids`, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDeliveryProfile> = entity => async dispatch => {
  const requestURI = `${apiUrl}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DELIVERYPROFILE,
    payload: axios.put(requestURI, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDeliveryProfile> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DELIVERYPROFILE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const updateShippingProfile: any = data => dispatch => {
  return dispatch({
    type: ACTION_TYPES.UPDATE_SHIPPING_PROFILE,
    payload: data
  });
};
