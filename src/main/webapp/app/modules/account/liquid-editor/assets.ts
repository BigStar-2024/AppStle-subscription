/* tslint:disable */
import axios from 'axios';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  GET_ASSET: 'asset/GET_ASSET',
  SAVE_ASSET: 'asset/SAVE_ASSET',
  GET_ASSET_KEYS: 'asset/GET_ASSET_KEYS',
  GET_THEME_DETAILS: 'asset/GET_THEME_DETAILS',
  GET_ALL_THEME_DETAILS: 'asset/GET_ALL_THEME_DETAILS'
};

const initialState = {
  loading: false,
  errorMessage: null,
  asset: null,
  assetKeys: [],
  themeName: '',
  allThemes: []
};

// Reducer

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_ASSET):
      return {
        ...state,
        errorMessage: null,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_ASSET):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.GET_ASSET):
      return {
        ...state,
        loading: false,
        asset: action.payload.data
      };

    case REQUEST(ACTION_TYPES.SAVE_ASSET):
      return {
        ...state,
        errorMessage: null,
        loading: true
      };
    case FAILURE(ACTION_TYPES.SAVE_ASSET):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SAVE_ASSET):
      return {
        ...state,
        loading: false,
        asset: action.payload.data
      };

    case REQUEST(ACTION_TYPES.GET_ASSET_KEYS):
      return {
        ...state,
        errorMessage: null,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_ASSET_KEYS):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.GET_ASSET_KEYS):
      return {
        ...state,
        loading: false,
        assetKeys: action.payload.data
      };

    case REQUEST(ACTION_TYPES.GET_THEME_DETAILS):
      return {
        ...state,
        errorMessage: null,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_THEME_DETAILS):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.GET_THEME_DETAILS):
      return {
        ...state,
        loading: false,
        themeName: action.payload.data
      };

    case REQUEST(ACTION_TYPES.GET_ALL_THEME_DETAILS):
      return {
        ...state,
        errorMessage: null,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_ALL_THEME_DETAILS):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.GET_ALL_THEME_DETAILS):
      return {
        ...state,
        loading: false,
        allThemes: action.payload.data
      };
    default:
      return state;
  }
};

export const getAsset = assetKey => ({
  type: ACTION_TYPES.GET_ASSET,
  payload: axios.get(`api/asset-key?assetKey=${assetKey}`)
});

export const saveAsset = (assetKey, assetValue) => ({
  type: ACTION_TYPES.SAVE_ASSET,
  payload: axios.post(`api/asset-key`, { assetKey: assetKey, value: assetValue })
});

export const getAssetKeys = () => ({
  type: ACTION_TYPES.GET_ASSET_KEYS,
  payload: axios.get(`api/asset-keys`)
});

export const getThemeDetails = () => ({
  type: ACTION_TYPES.GET_THEME_DETAILS,
  payload: axios.get(`api/theme-details`)
});

export const getAllThemeDetails = () => ({
  type: ACTION_TYPES.GET_ALL_THEME_DETAILS,
  payload: axios.get(`api/all-theme-details`)
});
