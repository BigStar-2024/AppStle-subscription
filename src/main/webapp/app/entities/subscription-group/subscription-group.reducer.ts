import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionGroup, defaultValue } from 'app/shared/model/subscription-group.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONGROUP_LIST: 'subscriptionGroup/FETCH_SUBSCRIPTIONGROUP_LIST',
  FETCH_SUBSCRIPTIONGROUP_USED_PRODUT_LIST: 'subscriptionGroup/FETCH_SUBSCRIPTIONGROUP_USED_PRODUT_LIST',
  FETCH_SUBSCRIPTIONGROUP: 'subscriptionGroup/FETCH_SUBSCRIPTIONGROUP',
  SYNC_SUBSCRIPTIONGROUP: 'subscriptionGroup/SYNC_SUBSCRIPTIONGROUP',
  FETCH_SUBSCRIPTION_GROUP_PRODUCTS: 'subscriptionGroup/FETCH_SUBSCRIPTION_GROUP_PRODUCTS',
  FETCH_SUBSCRIPTION_GROUP_VARIANTS: 'subscriptionGroup/FETCH_SUBSCRIPTION_GROUP_VARIANTS',
  CREATE_SUBSCRIPTIONGROUP: 'subscriptionGroup/CREATE_SUBSCRIPTIONGROUP',
  UPDATE_SUBSCRIPTIONGROUP: 'subscriptionGroup/UPDATE_SUBSCRIPTIONGROUP',
  UPDATE_SUBSCRIPTION_GROUP_PRODUCTS: 'subscriptionGroup/UPDATE_SUBSCRIPTION_GROUP_PRODUCTS',
  DELETE_SUBSCRIPTION_GROUP_PRODUCTS: 'subscriptionGroup/DELETE_SUBSCRIPTION_GROUP_PRODUCTS',
  DELETE_SUBSCRIPTIONGROUP: 'subscriptionGroup/DELETE_SUBSCRIPTIONGROUP',
  CRITERIA_FORM_VALUE_TRIGGERRULE: 'triggerRule/CRITERIA_FORM_VALUE_TRIGGERRULE',
  IMPORT_PLAN_PRODUCT_CSV: 'triggerRule/IMPORT_PLAN_PRODUCT_CSV',
  RESET: 'subscriptionGroup/RESET',
  FETCH_SELLINGPLANS: 'subscriptionGroup/FETCH_SELLINGPLANS',
  UPDATE_STATE_ENTITY: 'subscriptionGroup/UPDATE_STATE_ENTITY',
  UPDATE_PAYMENT_METHODS: 'subscriptionGroup/UPDATE_PAYMENT_METHODS',
  FETCH_CURRENT_CYCLE: 'subscriptionGroup/FETCH_CURRENT_CYCLE',
};

const initialState = {
  loading: false,
  syncLoading: false,
  deleting: false,
  productsLoading: false,
  variantsLoading: false,
  sellingPlanProductsData: [],
  sellingPlanVariantsData: [],
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionGroup>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
  usedProductIds: [],
  sellingPlanData: [],
  customerPaymentMethods: [],
  currentCycle: undefined,
  currentCycleLoaded: false,
  updatingProducts: false,
};

export type SubscriptionGroupState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionGroupState = initialState, action): SubscriptionGroupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.SYNC_SUBSCRIPTIONGROUP):
      return {
        ...state,
        errorMessage: null,
        syncLoading: true,
      };
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTION_GROUP_PRODUCTS):
      return {
        ...state,
        productsLoading: true,
      };
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTION_GROUP_VARIANTS):
      return {
        ...state,
        variantsLoading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONGROUP):
      return {
        ...state,
        updating: true,
      };
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONGROUP):
      return {
        ...state,
        updating: true,
      };
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTION_GROUP_PRODUCTS):
      return {
        ...state,
        updatingProducts: true,
      };
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTION_GROUP_PRODUCTS):
      return {
        ...state,
        deleting: true,
      };
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONGROUP):
    case REQUEST(ACTION_TYPES.IMPORT_PLAN_PRODUCT_CSV):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTION_GROUP_PRODUCTS):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTION_GROUP_VARIANTS):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP):
    case FAILURE(ACTION_TYPES.SYNC_SUBSCRIPTIONGROUP):
      return {
        ...state,
        syncLoading: false,
      };
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONGROUP):
      return {
        ...state,
        updating: false,
      };
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONGROUP):
      return {
        ...state,
        updating: false,
      };
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTION_GROUP_PRODUCTS):
      return {
        ...state,
        updatingProducts: false,
      };
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTION_GROUP_PRODUCTS):
      return {
        ...state,
        deleting: false,
      };
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONGROUP):
    case FAILURE(ACTION_TYPES.IMPORT_PLAN_PRODUCT_CSV):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.SYNC_SUBSCRIPTIONGROUP):
      return {
        ...state,
        syncLoading: false,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP):
      const updatedSubscriptionPlanValues = action.payload.data?.subscriptionPlans?.map(data => ({
        ...data,
        formFieldJsonArray: JSON.parse(data?.formFieldJson || null),
      }));
      if (action?.payload?.data?.subscriptionPlans) {
        action.payload.data.subscriptionPlans = updatedSubscriptionPlanValues;
      }
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTION_GROUP_PRODUCTS):
      return {
        ...state,
        productsLoading: false,
        sellingPlanProductsData: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTION_GROUP_VARIANTS):
      return {
        ...state,
        variantsLoading: false,
        sellingPlanVariantsData: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONGROUP):
      return {
        ...state,
        updateSuccess: true,
        updating: false,
      };
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTION_GROUP_PRODUCTS):
      return {
        ...state,
        updatingProducts: false,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTION_GROUP_PRODUCTS):
      return {
        ...state,
        deleting: false,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP_USED_PRODUT_LIST):
      return {
        ...state,
        usedProductIds: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.CRITERIA_FORM_VALUE_TRIGGERRULE:
      const formData = action.payload;
      return {
        ...state,
        entity: { ...state.entity, productIds: formData.productIds },
      };
    case ACTION_TYPES.UPDATE_STATE_ENTITY:
      return {
        ...state,
        entity: { ...state.entity, ...action.payload },
      };
    case ACTION_TYPES.IMPORT_PLAN_PRODUCT_CSV:
      return {
        ...state,
        updating: false,
        updateSuccess: true,
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
        entity: { ...defaultValue },
      };
    case SUCCESS(ACTION_TYPES.FETCH_SELLINGPLANS):
      return {
        ...state,
        sellingPlanData: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENT_CYCLE):
      return {
        ...state,
        currentCycle: action.payload.data,
        currentCycleLoaded: true,
      };
    case ACTION_TYPES.UPDATE_PAYMENT_METHODS:
      return {
        ...state,
        customerPaymentMethods: action.payload,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/v2/subscription-groups';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionGroup> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP_LIST,
  payload: axios.get<ISubscriptionGroup>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ISubscriptionGroup> = id => {
  const requestUrl = `${apiUrl}/detail/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP,
    payload: axios.get<ISubscriptionGroup>(requestUrl),
  };
};

export const getSubscriptionGroupProducts = (id: String, next: Boolean, cursor: any) => {
  const requestUrl = `${apiUrl}/products/${id}?next=${next}&cursor=${cursor}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTION_GROUP_PRODUCTS,
    payload: axios.get(requestUrl),
  };
};

export const getSubscriptionGroupVariants = (id: String, next: Boolean, cursor: any) => {
  const requestUrl = `${apiUrl}/variants/${id}?next=${next}&cursor=${cursor}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTION_GROUP_VARIANTS,
    payload: axios.get(requestUrl),
  };
};

export const updateProductEntity: any = (id: any, entity: any) => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTION_GROUP_PRODUCTS,
    payload: axios.put(`${apiUrl}/products-update/${id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteProductEntity: any = (id: any, entity: any) => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTION_GROUP_PRODUCTS,
    payload: axios.put(`${apiUrl}/products-delete/${id}`, cleanEntity(entity)),
  });
  return result;
};

export const getSellingPlans = () => {
  const requestUrl = `api/subscription-groups/all-selling-plans`;
  return {
    type: ACTION_TYPES.FETCH_SELLINGPLANS,
    payload: axios.get(requestUrl),
  };
};

export const getCurrentCycle = contractId => {
  const requestUrl = `api/subscription-contract-details/current-cycle/${contractId}`;
  return {
    type: ACTION_TYPES.FETCH_CURRENT_CYCLE,
    payload: axios.get(requestUrl),
  };
};

export const getUsedProducts: ICrudGetAction<ISubscriptionGroup> = id => {
  const requestUrl = `${apiUrl}/all-used-products${id ? '/' + id : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONGROUP_USED_PRODUT_LIST,
    payload: axios.get<ISubscriptionGroup>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISubscriptionGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONGROUP,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const bulkAttachProductsInSubscriptionGroups = formData => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONGROUP,
    payload: axios.post(`api/miscellaneous/bulk-attach-products-in-subscription-groups`, formData),
  });
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONGROUP,
    payload: axios.put(`${apiUrl}/details`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionGroup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONGROUP,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const onChangeCriteria: any = criteriaData => dispatch => {
  return dispatch({
    type: ACTION_TYPES.CRITERIA_FORM_VALUE_TRIGGERRULE,
    payload: criteriaData,
  });
};
export const updateStateEntity: any = data => dispatch => {
  return dispatch({
    type: ACTION_TYPES.UPDATE_STATE_ENTITY,
    payload: data,
  });
};
export const updatePaymentMethodToState: any = methods => dispatch => {
  return dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENT_METHODS,
    payload: methods,
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

export const syncEntity = async (id, dispatch) => {
  const requestUrl = `${apiUrl}/${id}/sync`;
  return dispatch({
    type: ACTION_TYPES.SYNC_SUBSCRIPTIONGROUP,
    payload: axios.get(requestUrl),
  });
};
