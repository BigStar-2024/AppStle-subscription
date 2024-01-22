import axios from 'axios';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';
import { ICrudGetAllAction } from 'react-jhipster';
import { IDeliveryProfile } from 'app/shared/model/delivery-profile.model';

export const ACTION_TYPES = {
  FETCH_DELIVERYPROFILE_LIST: 'activityLog/FETCH_DELIVERYPROFILE_LIST',
  FETCH_SHIPPING_PROFILE: 'activityLog/FETCH_SHIPPING_PROFILE',
  CREATE_SHIPPING_PROFILE: 'activityLog/CREATE_SHIPPING_PROFILE',
  UPDATE_SHIPPING_PROFILE: 'activityLog/UPDATE_SHIPPING_PROFILE',
  RESET: 'activityLog/RESET'
};

export const defaultDeliveryMethod = {
  definitionType: 'OWN',
  deliveryConditionType: 'PRICE',
  amount: 0,
  currencyCode: null,
  carrierServiceId: null,
  name: null,
  priceConditions: [],
  weightConditions: [],
  weightUnit: 'KILOGRAMS',
  carrierPercentageFee: 0,
  carrierFixedFee: 0
};

export const defaultDeliveryZone = { countries: [], restOfWorld: true, deliveryMethods: [defaultDeliveryMethod] };

export const defaultValue = {
  name: null,
  id: null,
  sellingPlanGroups: [],
  locations: [],
  zones: [defaultDeliveryZone]
};

const initialState = {
  loading: false,
  errorMessage: null,
  entity: defaultValue,
  updating: false,
  saving: false,
  updateSuccess: false,
  entities: []
};

export type ShippingProfileState = Readonly<typeof initialState>;

// Reducer

export default (state: ShippingProfileState = initialState, action): ShippingProfileState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DELIVERYPROFILE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHIPPING_PROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHIPPING_PROFILE):
      return {
        ...state,
        saving: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_SHIPPING_PROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.CREATE_SHIPPING_PROFILE):
    case FAILURE(ACTION_TYPES.UPDATE_SHIPPING_PROFILE):
    case FAILURE(ACTION_TYPES.FETCH_SHIPPING_PROFILE):
    case FAILURE(ACTION_TYPES.FETCH_DELIVERYPROFILE_LIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIPPING_PROFILE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHIPPING_PROFILE):
      return {
        ...state,
        saving: false
      };
    case SUCCESS(ACTION_TYPES.UPDATE_SHIPPING_PROFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DELIVERYPROFILE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const getShippingProfileV3 = id => {
  const requestUrl = `api/delivery-profiles/v3/detailed/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHIPPING_PROFILE,
    payload: axios.get(`${requestUrl}`)
  };
};

export const saveShippingProfileV3 = entity => {
  const requestUrl = `api/delivery-profiles/v3/create-shipping-profile`;
  return {
    type: ACTION_TYPES.CREATE_SHIPPING_PROFILE,
    payload: axios.post(`${requestUrl}`, entity)
  };
};

export const updateShippingProfileV3 = entity => {
  const requestUrl = `api/delivery-profiles/v3`;
  return {
    type: ACTION_TYPES.UPDATE_SHIPPING_PROFILE,
    payload: axios.put(`${requestUrl}`, entity)
  };
};

export const saveShippingProfileV4 = entity => {
  const requestUrl = `api/delivery-profiles/v4/create-shipping-profile`;
  return {
    type: ACTION_TYPES.CREATE_SHIPPING_PROFILE,
    payload: axios.post(`${requestUrl}`, entity)
  };
};

export const updateShippingProfileV4 = entity => {
  const requestUrl = `api/delivery-profiles/v4`;
  return {
    type: ACTION_TYPES.UPDATE_SHIPPING_PROFILE,
    payload: axios.put(`${requestUrl}`, entity)
  };
};

export const getDeliveryProfiles: ICrudGetAllAction<IDeliveryProfile> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DELIVERYPROFILE_LIST,
  payload: axios.get<IDeliveryProfile>(`api/delivery-profiles/v3?cacheBuster=${new Date().getTime()}`)
});
