import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShopCustomization, defaultValue } from 'app/shared/model/shop-customization.model';

export const ACTION_TYPES = {
  FETCH_SHOPCUSTOMIZATION_LIST: 'shopCustomization/FETCH_SHOPCUSTOMIZATION_LIST',
  FETCH_SHOPCUSTOMIZATION_FIELD_LIST: 'shopCustomization/FETCH_SHOPCUSTOMIZATION_FIELD_LIST',
  FETCH_SHOPCUSTOMIZATION: 'shopCustomization/FETCH_SHOPCUSTOMIZATION',
  FETCH_SHOPCUSTOMIZATION_CSS: 'shopCustomization/FETCH_SHOPCUSTOMIZATION_CSS',
  CREATE_SHOPCUSTOMIZATION: 'shopCustomization/CREATE_SHOPCUSTOMIZATION',
  UPDATE_SHOPCUSTOMIZATION: 'shopCustomization/UPDATE_SHOPCUSTOMIZATION',
  DELETE_SHOPCUSTOMIZATION: 'shopCustomization/DELETE_SHOPCUSTOMIZATION',
  RESET: 'shopCustomization/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShopCustomization>,
  shopCustomizationFields: [],
  entity: defaultValue,
  shopCustomizationCss: [],
  updating: false,
  updateSuccess: false
};

export type ShopCustomizationState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopCustomizationState = initialState, action): ShopCustomizationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_FIELD_LIST):
      return {
        ...state,
        loading: true
      };
    case REQUEST(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_CSS):
    case REQUEST(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPCUSTOMIZATION):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPCUSTOMIZATION):
    case REQUEST(ACTION_TYPES.DELETE_SHOPCUSTOMIZATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_FIELD_LIST):
      return {
        ...state,
        loading: false
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_CSS):
    case FAILURE(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION):
    case FAILURE(ACTION_TYPES.CREATE_SHOPCUSTOMIZATION):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPCUSTOMIZATION):
    case FAILURE(ACTION_TYPES.DELETE_SHOPCUSTOMIZATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_FIELD_LIST):
      return {
        ...state,
        loading: false,
        shopCustomizationFields: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_CSS):
      return {
        ...state,
        loading: false,
        shopCustomizationCss: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPCUSTOMIZATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPCUSTOMIZATION):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPCUSTOMIZATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPCUSTOMIZATION):
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

const apiUrl = 'api/shop-customizations';

// Actions

export const getEntities: ICrudGetAllAction<IShopCustomization> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_LIST,
  payload: axios.get<IShopCustomization>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntitiesByCategory = category => ({
  type: ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_FIELD_LIST,
  payload: axios.get(`${apiUrl}/category/${category}`)
});

export const getEntity: ICrudGetAction<IShopCustomization> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPCUSTOMIZATION,
    payload: axios.get<IShopCustomization>(requestUrl)
  };
};

export const getShopCustomizationCSS = category => {
  const requestUrl = `api/shop-customizations/css/${category}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPCUSTOMIZATION_CSS,
    payload: axios.get<IShopCustomization>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopCustomization> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPCUSTOMIZATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopCustomization> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPCUSTOMIZATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShopCustomization> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPCUSTOMIZATION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
