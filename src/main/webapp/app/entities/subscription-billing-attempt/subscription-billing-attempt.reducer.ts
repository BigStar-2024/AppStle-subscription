import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, ISubscriptionBillingAttempt } from 'app/shared/model/subscription-billing-attempt.model';
import { getCustomerPortalEntity, getEntity as getSubscriptionEntity } from 'app/entities/subscriptions/subscription.reducer';
import { Dispatch } from 'redux';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONBILLINGATTEMPT_LIST: 'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPT_LIST',
  FETCH_SUBSCRIPTIONBILLINGATTEMPT: 'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPT',
  FETCH_SUBSCRIPTIONBILLINGATTEMPT_UPCOMINGORDER: 'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPT_UPCOMINGORDER',
  FETCH_SUBSCRIPTIONBILLINGATTEMPT_FAILURES: 'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPT_FAILURES',
  FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_FAILURES_LIST: 'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_FAILURES_LIST',
  FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SUCCESS_LIST: 'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SUCCESS_LIST',
  FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_UPCOMING_LIST: 'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_UPCOMING_LIST',
  FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIP_LIST: 'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIP_LIST',
  FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_DUNNING_MGMT_LIST:
    'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_DUNNING_MGMT_LIST',
  FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_INVENTORY_MGMT_LIST:
    'subscriptionBillingAttempt/FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_INVENTORY_MGMT_LIST',
  CREATE_SUBSCRIPTIONBILLINGATTEMPT: 'subscriptionBillingAttempt/CREATE_SUBSCRIPTIONBILLINGATTEMPT',
  UPDATE_SUBSCRIPTIONBILLINGATTEMPT: 'subscriptionBillingAttempt/UPDATE_SUBSCRIPTIONBILLINGATTEMPT',
  UPDATE_SUBSCRIPTIONBILLINGATTEMPT_FLAG: 'subscriptionBillingAttempt/UPDATE_SUBSCRIPTIONBILLINGATTEMPT_FLAG',
  SKIP_BILLING_ORDER: 'subscriptionBillingAttempt/SKIP_BILLING_ORDER',
  DELETE_SUBSCRIPTIONBILLINGATTEMPT: 'subscriptionBillingAttempt/DELETE_SUBSCRIPTIONBILLINGATTEMPT',
  RESET: 'subscriptionBillingAttempt/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionBillingAttempt>,
  failedBillingAttempt: [] as ReadonlyArray<ISubscriptionBillingAttempt>,
  successBillingAttempt: [] as ReadonlyArray<ISubscriptionBillingAttempt>,
  upcomingBillingAttempt: [] as ReadonlyArray<ISubscriptionBillingAttempt>,
  skipBillingAttempt: [] as ReadonlyArray<ISubscriptionBillingAttempt>,
  failedBillingAttemptDetail: [],
  successBillingAttemptDetail: [],
  upcomingBillingAttemptDetail: [],
  skipBillingAttemptDetail: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
  billingAttempted: false,
  updatingRetryingNeeded: false,
  updatingBillingAttempt: false,
  skippedBillingAttemptFlag: false,
  totalItems: 0,
  isUpcomingOrderUpdated: false,
  skipBillingAttemptDunningMgmt: [],
  skipBillingAttemptInventoryMgmt: [],
  failedOrderLoading: false,
  totalFailedBillingAttemptItems: 0,
  failedOrderLoadingList: false
};

export type SubscriptionBillingAttemptState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionBillingAttemptState = initialState, action): SubscriptionBillingAttemptState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_UPCOMINGORDER):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_FAILURES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SUCCESS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_UPCOMING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_DUNNING_MGMT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_INVENTORY_MGMT_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_FAILURES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        failedOrderLoading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONBILLINGATTEMPT):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONBILLINGATTEMPT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case REQUEST(ACTION_TYPES.SKIP_BILLING_ORDER):
      return {
        ...state,
        errorMessage: null,
        updating: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT):
      return {
        ...state,
        billingAttempted: false,
        updatingBillingAttempt: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT_FLAG):
      return {
        ...state,
        billingAttempted: false,
        updatingRetryingNeeded: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_UPCOMINGORDER):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_FAILURES):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONBILLINGATTEMPT):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONBILLINGATTEMPT):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_FAILURES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SUCCESS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_UPCOMING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_DUNNING_MGMT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_INVENTORY_MGMT_LIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        failedOrderLoading: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.SKIP_BILLING_ORDER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT_FLAG):
      return {
        ...state,
        updatingBillingAttempt: false,
        updatingRetryingNeeded: false,
        billingAttempted: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_UPCOMINGORDER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
        isUpcomingOrderUpdated: true
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_FAILURES):
      return {
        ...state,
        loading: false,
        failedOrderLoading: false,
        failedBillingAttempt: action.payload.data,
        totalFailedBillingAttemptItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_FAILURES_LIST):
      return {
        ...state,
        loading: false,
        failedBillingAttemptDetail: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_UPCOMING_LIST):
      return {
        ...state,
        loading: false,
        upcomingBillingAttemptDetail: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIP_LIST):
      return {
        ...state,
        loading: false,
        skipBillingAttemptDetail: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_DUNNING_MGMT_LIST):
      return {
        ...state,
        loading: false,
        skipBillingAttemptDunningMgmt: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_INVENTORY_MGMT_LIST):
      return {
        ...state,
        loading: false,
        skipBillingAttemptInventoryMgmt: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SUCCESS_LIST):
      return {
        ...state,
        loading: false,
        successBillingAttemptDetail: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.SKIP_BILLING_ORDER):
      return {
        ...state,
        updating: false,
        // updatingBillingAttempt: false,
        // billingAttempted: true,
        skippedBillingAttemptFlag: true,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONBILLINGATTEMPT):
      return {
        ...state,
        updating: false,
        updatingBillingAttempt: false,
        billingAttempted: true,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT):
      return {
        ...state,
        updatingBillingAttempt: false,
        billingAttempted: true
        // entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT_FLAG):
      return {
        ...state,
        updatingRetryingNeeded: false
        // entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONBILLINGATTEMPT):
      return {
        ...state,
        updating: false,
        updatingBillingAttempt: false,
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

const apiUrl = 'api/subscription-billing-attempts';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionBillingAttempt> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_LIST,
  payload: axios.get<ISubscriptionBillingAttempt>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionBillingAttempt> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT,
    payload: axios.get<ISubscriptionBillingAttempt>(requestUrl)
  };
};

export const getUpcomingOrderEntity = (contractId, customerId, shop) => {
  const requestUrl = `${apiUrl}/top-orders?contractId=${contractId}&customerId=${customerId}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_UPCOMINGORDER,
    payload: axios.get<ISubscriptionBillingAttempt>(requestUrl)
  };
};

export const getCustomerUpcomingOrderEntity = (contractId, customerId, api_key) => {
  const requestUrl = `api/subscription-billing-attempts/top-orders?contractId=${contractId}&${
    isNaN(customerId) ? 'customerUid' : 'customerId'
  }=${customerId}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_UPCOMINGORDER,
    payload: axios.get<ISubscriptionBillingAttempt>(requestUrl)
  };
};

export const getFailedOrderEntity = (contractId, customerId, shop, page, size) => {
  const requestUrl = `${apiUrl}/past-orders?contractId=${contractId}&customerId=${customerId}&shop=${shop}&page=${page}&size=${size}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPT_FAILURES,
    payload: axios.get<ISubscriptionBillingAttempt>(requestUrl)
  };
};

export const getSuccessPastOrderEntity = (page, size, filterVal, status) => {
  const requestUrl = `${apiUrl}/past-orders/report`;
  let params = {};
  params = {
    page: page,
    size: size,
    status: status
  };
  if (filterVal) {
    params = _.extend(params, filterVal);
  }
  params['cacheBuster'] = new Date().getTime();
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SUCCESS_LIST,
    payload: axios.get<ISubscriptionBillingAttempt>(`${requestUrl}`, { params: params })
  };
};

export const getFailedPastOrderEntity = (page, size, filterVal, status) => {
  const requestUrl = `${apiUrl}/past-orders/report`;
  let params = {};
  params = {
    page: page,
    size: size,
    status: status
  };
  if (filterVal) {
    params = _.extend(params, filterVal);
  }
  params['cacheBuster'] = new Date().getTime();
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_FAILURES_LIST,
    payload: axios.get<ISubscriptionBillingAttempt>(`${requestUrl}`, { params: params })
  };
};

export const getUpcomingOrderEntityForReport = (page, size, filterVal, status) => {
  const requestUrl = `${apiUrl}/past-orders/report`;
  let params = {};
  params = {
    page: page,
    size: size,
    status: status
  };
  if (filterVal) {
    params = _.extend(params, filterVal);
  }
  params['cacheBuster'] = new Date().getTime();
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_UPCOMING_LIST,
    payload: axios.get<ISubscriptionBillingAttempt>(`${requestUrl}`, { params: params })
  };
};

export const getSkipOrderEntityForReport = (page, size, filterVal, status) => {
  const requestUrl = `${apiUrl}/past-orders/report`;
  let params = {};
  params = {
    page: page,
    size: size,
    status: status
  };
  if (filterVal) {
    params = _.extend(params, filterVal);
  }
  params['cacheBuster'] = new Date().getTime();
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIP_LIST,
    payload: axios.get<ISubscriptionBillingAttempt>(`${requestUrl}`, { params: params })
  };
};

export const getSkippedDunningMgmtOrderEntityForReport = (page, size, filterVal, status) => {
  const requestUrl = `${apiUrl}/past-orders/report`;
  let params = {};
  params = {
    page: page,
    size: size,
    status: status
  };
  if (filterVal) {
    params = _.extend(params, filterVal);
  }
  params['cacheBuster'] = new Date().getTime();
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_DUNNING_MGMT_LIST,
    payload: axios.get<ISubscriptionBillingAttempt>(`${requestUrl}`, { params: params })
  };
};

export const getSkippedInventoryMgmtOrderEntityForReport = (page, size, filterVal, status) => {
  const requestUrl = `${apiUrl}/past-orders/report`;
  let params = {};
  params = {
    page: page,
    size: size,
    status: status
  };
  if (filterVal) {
    params = _.extend(params, filterVal);
  }
  params['cacheBuster'] = new Date().getTime();
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONBILLINGATTEMPTDETAILS_SKIPPED_INVENTORY_MGMT_LIST,
    payload: axios.get<ISubscriptionBillingAttempt>(`${requestUrl}`, { params: params })
  };
};

export const createEntity: ICrudPutAction<ISubscriptionBillingAttempt> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONBILLINGATTEMPT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionBillingAttempt> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const customerPortalupdateEntity: ICrudPutAction<ISubscriptionBillingAttempt> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT,
    payload: axios.put(`api/subscription-billing-attempts`, cleanEntity(entity))
  });
  return result;
};

export const subscriptionBillingAttemptsFlag = (allowRetry, orderId) => async dispatch => {
  const requestUrl = `${apiUrl}/${orderId}/retry-needed/${allowRetry}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT_FLAG,
    payload: axios.put(requestUrl, {})
  });
  return result;
};

export const updateAttemptBillingEntity = (id?: number, shop?: string, contractId?: number) => async (dispatch: any) => {
  const requestUrl = `${apiUrl}/attempt-billing/${id}?shop=${shop}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONBILLINGATTEMPT,
    payload: axios.put(requestUrl)
  });
  dispatch(getSubscriptionEntity(contractId.toString()));
  return result;
};

export const skipBillingOrder: ICrudPutAction<ISubscriptionBillingAttempt> = (
  id?,
  shop?,
  contractId?,
  customerId?,
  isPrepaid?
) => async dispatch => {
  const requestUrl = `${apiUrl}/skip-order/${id}?isPrepaid=${isPrepaid}&subscriptionContractId=${contractId}`;
  const result = await dispatch({
    type: ACTION_TYPES.SKIP_BILLING_ORDER,
    payload: axios.put(requestUrl)
  });
  dispatch(getSubscriptionEntity(contractId.toString()));
  dispatch(getUpcomingOrderEntity(contractId, customerId, shop));
  return result;
};

export const skipCustomerBillingOrder = (id?: number, contractId?: number, customerId?: number, isPrepaid?: boolean) => async (
  dispatch: Dispatch<any>
) => {
  const requestUrl = `${apiUrl}/skip-order/${id}?isPrepaid=${isPrepaid}&subscriptionContractId=${contractId}&isExternal=true`;
  // const requestUrl = `/api/external/v2/subscription-billing-attempts/skip-order/${id}?api_key=${window?.appstle_api_key}&subscriptionContractId=${contractId}&isPrepaid=${isPrepaid}`;
  // const result = await dispatch({
  //   type: ACTION_TYPES.SKIP_BILLING_ORDER,
  //   payload: axios.put(requestUrl)
  // });
  return axios
    .put(requestUrl)
    .then(res => {
      dispatch(getCustomerUpcomingOrderEntity(contractId, customerId, ''));
      dispatch(getCustomerPortalEntity(contractId.toString()));
      return res;
    })
    .catch(err => {
      return err;
    });
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionBillingAttempt> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONBILLINGATTEMPT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
