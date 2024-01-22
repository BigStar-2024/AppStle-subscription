import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, ISubscriptionContractDetails } from 'app/shared/model/subscription-contract-details.model';
import _ from 'lodash';
import { IImportSubscriptionContract } from 'app/shared/model/import-subscription-contract.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONCONTRACTDETAILS_LIST: 'subscriptionContractDetails/FETCH_SUBSCRIPTIONCONTRACTDETAILS_LIST',
  FETCH_SUBSCRIPTIONCONTRACTDETAILS: 'subscriptionContractDetails/FETCH_SUBSCRIPTIONCONTRACTDETAILS',
  FETCH_SUBSCRIPTIONCONTRACTDETAILS_BY_CONTRACT_ID: 'subscriptionContractDetails/FETCH_SUBSCRIPTIONCONTRACTDETAILS_BY_CONTRACT_ID',
  CREATE_SUBSCRIPTIONCONTRACTDETAILS: 'subscriptionContractDetails/CREATE_SUBSCRIPTIONCONTRACTDETAILS',
  IMPORT_SUBSCRIPTIONCONTRACTDETAILS: 'subscriptionContractDetails/IMPORT_SUBSCRIPTIONCONTRACTDETAILS',
  UPDATE_SUBSCRIPTIONCONTRACTDETAILS: 'subscriptionContractDetails/UPDATE_SUBSCRIPTIONCONTRACTDETAILS',
  UPDATE_CUSTOMER_INFO: 'subscriptionContractDetails/UPDATE_CUSTOMER_INFO',
  DELETE_SUBSCRIPTIONCONTRACTDETAILS: 'subscriptionContractDetails/DELETE_SUBSCRIPTIONCONTRACTDETAILS',
  RESET: 'subscriptionContractDetails/RESET',
  UPDATE_PRODUCT_IN_CONTRACT: 'subscriptionContractDetails/UPDATE_PRODUCT_IN_CONTRACT',
  EXPORT_SUBSCRIPTION_CONTRACT: 'subscriptionContractDetails/EXPORT_SUBSCRIPTION_CONTRACT',
  FETCH_UPCOMING_ORDER_LIST: 'subscriptionContractDetails/FETCH_UPCOMING_ORDER_LIST',
  FETCH_PRODUCTREVENUE_LIST: 'subscriptionContractDetails/FETCH_PRODUCTREVENUE_LIST',
  FETCH_SPLIT_SUBSCRIPTION_CONTRACT: 'subscriptionContractDetails/FETCH_SPLIT_SUBSCRIPTION_CONTRACT',
  SPLIT_CONTRACT: 'subscriptionContractDetails/SPLIT_CONTRACT',
  UPDATE_ATTRIBUTE: 'subscriptionContractDetails/UPDATE_ATTRIBUTE',
  UPDATE_SUBSCRIPTION_CONTRACT_STATUS: 'subscriptionContractDetails/UPDATE_SUBSCRIPTION_CONTRACT_STATUS'
};

const initialState = {
  loading: false,
  exporting: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionContractDetails>,
  productRevenueData: [],
  contracts: [],
  entity: defaultValue,
  splitContractDetails: null,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  upcomingOrders: null,
  productRevenueLoading: false,
  splitContractUpdated: false,
  updatingCustomerInfo: false,
  updatingAttribute: false
};

export type SubscriptionContractDetailsState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionContractDetailsState = initialState, action): SubscriptionContractDetailsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_BY_CONTRACT_ID):
    case REQUEST(ACTION_TYPES.FETCH_UPCOMING_ORDER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTREVENUE_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
        productRevenueLoading: true
      };
    case REQUEST(ACTION_TYPES.EXPORT_SUBSCRIPTION_CONTRACT):
      return {
        ...state,
        exporting: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTDETAILS):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTDETAILS):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTDETAILS):
    case REQUEST(ACTION_TYPES.IMPORT_SUBSCRIPTIONCONTRACTDETAILS):
    case REQUEST(ACTION_TYPES.FETCH_SPLIT_SUBSCRIPTION_CONTRACT):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTION_CONTRACT_STATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMER_INFO):
      return {
        ...state,
        updatingCustomerInfo: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_ATTRIBUTE):
      return {
        ...state,
        updatingAttribute: true
      };
    case REQUEST(ACTION_TYPES.SPLIT_CONTRACT):
      return {
        ...state,
        errorMessage: null,
        splitContractUpdated: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_BY_CONTRACT_ID):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTDETAILS):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTDETAILS):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMER_INFO):
    case FAILURE(ACTION_TYPES.UPDATE_ATTRIBUTE):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTDETAILS):
    case FAILURE(ACTION_TYPES.IMPORT_SUBSCRIPTIONCONTRACTDETAILS):
    case FAILURE(ACTION_TYPES.FETCH_UPCOMING_ORDER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTREVENUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SPLIT_SUBSCRIPTION_CONTRACT):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTION_CONTRACT_STATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        productRevenueLoading: false,
        updatingCustomerInfo: false,
        updatingAttribute: false,
        errorMessage: action.payload
      };

    case FAILURE(ACTION_TYPES.SPLIT_CONTRACT):
      return {
        ...state,
        loading: false,
        updating: false,
        splitContractUpdated: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.EXPORT_SUBSCRIPTION_CONTRACT):
      return {
        ...state,
        exporting: false
      };
    case SUCCESS(ACTION_TYPES.EXPORT_SUBSCRIPTION_CONTRACT):
      return {
        ...state,
        exporting: false
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS):
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_BY_CONTRACT_ID):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_UPCOMING_ORDER_LIST):
      return {
        ...state,
        loading: false,
        upcomingOrders: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTREVENUE_LIST):
      return {
        ...state,
        loading: false,
        productRevenueData: action.payload.data,
        productRevenueLoading: false
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTDETAILS):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTDETAILS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };

    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMER_INFO):
      return {
        ...state,
        updateSuccess: true,
        updatingCustomerInfo: false
      };

    case SUCCESS(ACTION_TYPES.UPDATE_ATTRIBUTE):
      return {
        ...state,
        updateSuccess: true,
        updatingAttribute: false
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTDETAILS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.IMPORT_SUBSCRIPTIONCONTRACTDETAILS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.FETCH_SPLIT_SUBSCRIPTION_CONTRACT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        contracts: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.SPLIT_CONTRACT):
      return {
        ...state,
        updating: false,
        splitContractUpdated: true,
        splitContractDetails: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTION_CONTRACT_STATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {
          ...state.entity,
          status: action.payload.data.status
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/subscription-contract-details';
const apiUrlWithoutPagination = 'api/subscription-contract-details/without-pagination';

// Actions

export const getEntities = (page, size, sort, filterVal) => {
  const requestUrl = `${apiUrl}`;
  let params = {};
  if (sort) {
    params = {
      page: page,
      size: size,
      sort: sort
    };
    if (filterVal) {
      params = _.extend(params, filterVal);
    }
  }
  params['cacheBuster'] = new Date().getTime();
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_LIST,
    payload: axios.get<ISubscriptionContractDetails>(`${requestUrl}`, { params: params })
  };
};

export const getEntitiesWithoutPagination: ICrudGetAllAction<ISubscriptionContractDetails> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_LIST,
  payload: axios.get<ISubscriptionContractDetails>(`${apiUrlWithoutPagination}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionContractDetails> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.get<ISubscriptionContractDetails>(requestUrl)
  };
};

export const getSubscriptionContractDetailsByContractId: ICrudGetAction<ISubscriptionContractDetails> = id => {
  const requestUrl = `${apiUrl}/subscription-contract-details-by-contract-id/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONCONTRACTDETAILS_BY_CONTRACT_ID,
    payload: axios.get<ISubscriptionContractDetails>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionContractDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const importEntity: ICrudPutAction<IImportSubscriptionContract> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.IMPORT_SUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.post('api/miscellaneous/import-subscription-contract', cleanEntity(entity))
  });

  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionContractDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionContractDetails> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const updateProductOnContract = (contractId, lineId, quantity, shop, variantId) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-line-item?contractId=${contractId}&lineId=${lineId}&quantity=${quantity}&variantId=${variantId}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCT_IN_CONTRACT,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const exportSubscriptionContract = () => async dispatch => {
  const requestUrl = `api/subscription-contract-details/export/all`;
  const result = await dispatch({
    type: ACTION_TYPES.EXPORT_SUBSCRIPTION_CONTRACT,
    payload: axios.get(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const getUpcomingOrderEntityList = id => {
  const requestUrl = `${apiUrl}/subscription-fulfillments/${id}`;
  return {
    type: ACTION_TYPES.FETCH_UPCOMING_ORDER_LIST,
    payload: axios.get<ISubscriptionContractDetails>(requestUrl)
  };
};

export const getUpcomingOrderEntityListExternal = id => {
  const requestUrl = `${apiUrl}/subscription-fulfillments/${id}`;
  // const requestUrl = `api/external/v2/subscription-contract-details/subscription-fulfillments/${id}?api_key=${window?.appstle_api_key}`;
  return {
    type: ACTION_TYPES.FETCH_UPCOMING_ORDER_LIST,
    payload: axios.get<ISubscriptionContractDetails>(requestUrl)
  };
};

export const getProductRevenueFromSubscriptionContracts = (filterBy, selectedDays, fromDay, toDay) => ({
  type: ACTION_TYPES.FETCH_PRODUCTREVENUE_LIST,
  payload: axios.get<ISubscriptionContractDetails>(`${apiUrl}/productRevenue-analytics`, {
    params: { filterBy, days: selectedDays, fromDay, toDay }
  })
});

export const splitSubscriptionContract = (contractId, isSplitContract, attemptBilling, lineIds) => ({
  type: ACTION_TYPES.SPLIT_CONTRACT,
  payload: axios.post<ISubscriptionContractDetails>(
    `${apiUrl}/split-existing-contract?contractId=${contractId}&isSplitContract=${isSplitContract}&attemptBilling=${attemptBilling}`,
    lineIds
  )
});
export const updateCustomerInfo = customer => ({
  type: ACTION_TYPES.UPDATE_CUSTOMER_INFO,
  payload: axios.post<ISubscriptionContractDetails>(`api/subscription-customers/update-customer-info`, customer)
});

export const updateSubscriptionsAttributes = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ATTRIBUTE,
    payload: axios.post<ISubscriptionContractDetails>(`api/update-custom-note-attributes`, entity)
  });
  return result;
};

export const updateSubscriptionContractStatus = (contractId: number, status: string, pauseDurationCycle?: number) => async dispatch => {
  const requestUrl = `api/subscription-contracts-update-status?contractId=${contractId}&status=${status}&isExternal=true${
    pauseDurationCycle ? '&pauseDurationCycle=' + pauseDurationCycle : ''
  }`;

  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTION_CONTRACT_STATUS,
    payload: axios.put(requestUrl)
  });

  return result;
};
