import axios from 'axios';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_LOCATIONS: 'activityLog/FETCH_LOCATIONS',
  FETCH_COUNTRIES: 'activityLog/FETCH_COUNTRIES',
  FETCH_AVAILABLE_CARRIER: 'activityLog/FETCH_AVAILABLE_CARRIER',
  FETCH_SHIPPING_PROFILE: 'activityLog/FETCH_SHIPPING_PROFILE',
  CREATE_SHIPPING_PROFILE: 'activityLog/CREATE_SHIPPING_PROFILE',
  UPDATE_SHIPPING_PROFILE: 'activityLog/UPDATE_SHIPPING_PROFILE',
  GET_COUNTRIES_LIST: 'activityLog/GET_COUNTRIES_LIST',
  RESET: 'activityLog/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  locations: [],
  countries: [],
  carriers: [],
  updating: false
};

export type HelperState = Readonly<typeof initialState>;

// Reducer

export default (state: HelperState = initialState, action): HelperState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LOCATIONS):
    case REQUEST(ACTION_TYPES.FETCH_COUNTRIES):
    case REQUEST(ACTION_TYPES.FETCH_AVAILABLE_CARRIER):
    case REQUEST(ACTION_TYPES.GET_COUNTRIES_LIST):
      return {
        ...state,
        errorMessage: null,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LOCATIONS):
    case FAILURE(ACTION_TYPES.FETCH_COUNTRIES):
    case FAILURE(ACTION_TYPES.FETCH_AVAILABLE_CARRIER):
    case FAILURE(ACTION_TYPES.GET_COUNTRIES_LIST):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOCATIONS):
      return {
        ...state,
        loading: false,
        locations: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COUNTRIES):
      return {
        ...state,
        loading: false,
        countries: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AVAILABLE_CARRIER):
      return {
        ...state,
        loading: false,
        carriers: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.GET_COUNTRIES_LIST):
      return {
        ...state,
        loading: false,
        countries: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

export const getLocations = () => {
  const requestUrl = `api/delivery-profiles/get-locations`;
  return {
    type: ACTION_TYPES.FETCH_LOCATIONS,
    payload: axios.get(`${requestUrl}`)
  };
};
export const getAvailableCarrier = () => {
  const requestUrl = `api/data/available-carriers`;
  return {
    type: ACTION_TYPES.FETCH_AVAILABLE_CARRIER,
    payload: axios.get(`${requestUrl}`)
  };
};

export const getCountries = () => {
  const requestUrl = `api/data/countries`;
  return {
    type: ACTION_TYPES.GET_COUNTRIES_LIST,
    payload: axios.get(`${requestUrl}`)
  };
};
