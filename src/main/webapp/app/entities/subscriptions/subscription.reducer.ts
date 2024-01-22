import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscription, defaultValue } from 'app/shared/model/subscription.model';
import { getEntities as SubscriptionContractGetEntities } from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTION_LIST: 'subscription/FETCH_SUBSCRIPTION_LIST',
  FETCH_SUBSCRIPTION: 'subscription/FETCH_SUBSCRIPTION',
  CREATE_SUBSCRIPTION: 'subscription/CREATE_SUBSCRIPTION',
  UPDATE_SUBSCRIPTION: 'subscription/UPDATE_SUBSCRIPTION',
  UPDATE_BILLING_INTERVAL: 'subscription/UPDATE_BILLING_INTERVAL',
  UPDATE_DELIVERY_INTERVAL: 'subscription/UPDATE_DELIVERY_INTERVAL',
  UPDATE_DELIVERY_METHOD_NAME: 'subscription/UPDATE_DELIVERY_METHOD_NAME',
  UPDATE_BILLING_DATE: 'subscription/UPDATE_BILLING_DATE',
  UPDATE_DELIVERYPRICE: 'subscription/UPDATE_DELIVERYPRICE',
  UPDATE_MINCYCLE: 'subscription/UPDATE_MINCYCLE',
  UPDATE_MAXCYCLE: 'subscription/UPDATE_MAXCYCLE',
  UPDATE_LINEITEM: 'subscription/UPDATE_LINEITEM',
  UPDATE_SHIPPING_ADDRESS: 'subscription/UPDATE_SHIPPING_ADDRESS',
  UPDATE_PAYMENTDETAIL: 'subscription/UPDATE_PAYMENTDETAIL',
  DELETE_SUBSCRIPTION: 'subscription/DELETE_SUBSCRIPTION',
  SET_BLOB: 'subscription/SET_BLOB',
  RESET: 'subscription/RESET',
  REMOVE_DISCOUNT: 'subscriptionContractDetails/REMOVE_DISCOUNT',
  REMOVE_PRODUCT: 'subscriptionContractDetails/REMOVE_PRODUCT',
  APPLY_DISCOUNT: 'subscriptionContractDetails/APPLY_DISCOUNT',
  ADD_DISCOUNT_CODE: 'subscriptionContractDetails/ADD_DISCOUNT_CODE',
  DELETE_ONE_OFF_VARIANT: 'subscriptionContractDetails/DELETE_ONE_OFF_VARIANT',
  GET_FREEZE_STATUS: 'subscriptionContractDetails/GET_FREEZE_STATUS'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscription>,
  entity: JSON.parse(JSON.stringify(defaultValue)),
  updating: false,
  subscriptionLoading: false,
  updateSuccess: false,
  updatingPayment: false,
  updatingBilling: false,
  updatePaymentSuccess: false,
  updatingBillingDate: false,
  updatingShippingAddress: false,
  updatingDeliveryPrice: false,
  updatingMinCycles: false,
  updatingMaxCycles: false,
  updatingDeliveryInterval: false,
  removeDiscountInProgress: false,
  removeDiscountSuccess: false,
  addDiscountCodeSuccess: false,
  addDiscountCodeInProgress: false,
  removeProductInProgress: false,
  removeProductSuccess: false,
  oneoffDeleteInProgress: false,
  updatedBillingInterval: false,
  isSubscriptionEntityUpdated: false,
  applyDiscountSuccess: false,
  updatingDeliveryMethodName: false,
  discountAppliedData: {},
  deleteSubscriptionMessage: {},
  subscriptionContractFreezeStatus: false,
  subscriptionContractFreezeStatusMessage: ''
};

export type SubscriptionState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionState = initialState, action): SubscriptionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        subscriptionLoading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTION):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTION):
    case REQUEST(ACTION_TYPES.UPDATE_LINEITEM):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case REQUEST(ACTION_TYPES.APPLY_DISCOUNT):
    case REQUEST(ACTION_TYPES.REMOVE_DISCOUNT):
    case REQUEST(ACTION_TYPES.REMOVE_PRODUCT):
      return {
        ...state,
        errorMessage: null,
        removeDiscountSuccess: false,
        removeDiscountInProgress: true,
        removeProductInProgress: true
      };
    case REQUEST(ACTION_TYPES.ADD_DISCOUNT_CODE):
      return {
        ...state,
        errorMessage: null,
        addDiscountCodeSuccess: false,
        addDiscountCodeInProgress: true
      };

    case REQUEST(ACTION_TYPES.UPDATE_PAYMENTDETAIL):
      return {
        ...state,
        errorMessage: null,
        updatePaymentSuccess: false,
        updatingPayment: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_BILLING_INTERVAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updatingBilling: true,
        updatedBillingInterval: false
      };
    case REQUEST(ACTION_TYPES.UPDATE_DELIVERY_INTERVAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updatingDeliveryInterval: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_DELIVERY_METHOD_NAME):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updatingDeliveryMethodName: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_BILLING_DATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updatingBillingDate: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_DELIVERYPRICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updatingDeliveryPrice: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_MINCYCLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updatingMinCycles: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_MAXCYCLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updatingMaxCycles: true
      };
    case REQUEST(ACTION_TYPES.UPDATE_SHIPPING_ADDRESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updatingShippingAddress: true
      };
    case REQUEST(ACTION_TYPES.DELETE_ONE_OFF_VARIANT):
      return {
        ...state,
        oneoffDeleteInProgress: true
      };
    case REQUEST(ACTION_TYPES.GET_FREEZE_STATUS):
      return {
        ...state,
        subscriptionContractFreezeStatus: false
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTION):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTION):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTION):
    case FAILURE(ACTION_TYPES.UPDATE_LINEITEM):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTION):
      return {
        ...state,
        loading: false,
        subscriptionLoading: false,
        updating: false,
        updateSuccess: false,
        deleteSubscriptionMessage: action.payload,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.APPLY_DISCOUNT):
    case FAILURE(ACTION_TYPES.REMOVE_DISCOUNT):
    case FAILURE(ACTION_TYPES.REMOVE_PRODUCT):
      return {
        ...state,
        errorMessage: action.payload,
        removeDiscountSuccess: false,
        removeDiscountInProgress: false,
        removeProductInProgress: false
      };
    case FAILURE(ACTION_TYPES.ADD_DISCOUNT_CODE):
      return {
        ...state,
        errorMessage: action.payload,
        addDiscountCodeSuccess: false,
        addDiscountCodeInProgress: false
      };
    case FAILURE(ACTION_TYPES.UPDATE_PAYMENTDETAIL):
      return {
        ...state,
        loading: false,
        updatingPayment: false,
        updatePaymentSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_BILLING_INTERVAL):
      return {
        ...state,
        loading: false,
        updatingBilling: false,
        updateSuccess: false,
        updatedBillingInterval: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_DELIVERY_INTERVAL):
      return {
        ...state,
        loading: false,
        updatingDeliveryInterval: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_DELIVERY_METHOD_NAME):
      return {
        ...state,
        loading: false,
        updatingDeliveryMethodName: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_BILLING_DATE):
      return {
        ...state,
        loading: false,
        updatingBillingDate: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_DELIVERYPRICE):
      return {
        ...state,
        loading: false,
        updatingDeliveryPrice: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_MINCYCLE):
      return {
        ...state,
        loading: false,
        updatingMinCycles: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_MAXCYCLE):
      return {
        ...state,
        loading: false,
        updatingMaxCycles: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.UPDATE_SHIPPING_ADDRESS):
      return {
        ...state,
        loading: false,
        updatingShippingAddress: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.GET_FREEZE_STATUS):
      return {
        ...state,
        subscriptionContractFreezeStatus: false
      };
    case FAILURE(ACTION_TYPES.DELETE_ONE_OFF_VARIANT):
      return {
        ...state,
        oneoffDeleteInProgress: false
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTION):
      return {
        ...state,
        loading: false,
        subscriptionLoading: false,
        entity: action.payload.data,
        isSubscriptionEntityUpdated: true
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTION):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.UPDATE_LINEITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_BILLING_INTERVAL):
      return {
        ...state,
        updatingBilling: false,
        updateSuccess: true,
        updatedBillingInterval: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_DELIVERY_METHOD_NAME):
      return {
        ...state,
        updatingDeliveryMethodName: false,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_DELIVERY_INTERVAL):
      return {
        ...state,
        updatingDeliveryInterval: false,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_BILLING_DATE):
      return {
        ...state,
        updatingBillingDate: false,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_DELIVERYPRICE):
      return {
        ...state,
        updatingDeliveryPrice: false,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_MINCYCLE):
      return {
        ...state,
        updatingMinCycles: false,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_MAXCYCLE):
      return {
        ...state,
        updatingMaxCycles: false,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_SHIPPING_ADDRESS):
      return {
        ...state,
        updatingShippingAddress: false,
        updateSuccess: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_PAYMENTDETAIL):
      return {
        ...state,
        updatingPayment: false,
        updatePaymentSuccess: true
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        deleteSubscriptionMessage: action.payload.data,
        entity: {}
      };
    case SUCCESS(ACTION_TYPES.REMOVE_DISCOUNT):
    case SUCCESS(ACTION_TYPES.REMOVE_PRODUCT):
      return {
        ...state,
        removeDiscountSuccess: true,
        removeDiscountInProgress: false,
        removeProductSuccess: true
      };
    case SUCCESS(ACTION_TYPES.APPLY_DISCOUNT):
      return {
        ...state,
        applyDiscountSuccess: true,
        removeDiscountSuccess: true,
        removeDiscountInProgress: false,
        removeProductSuccess: true,
        discountAppliedData: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.ADD_DISCOUNT_CODE):
      return {
        ...state,
        addDiscountCodeSuccess: true,
        addDiscountCodeInProgress: false
      };
    case SUCCESS(ACTION_TYPES.DELETE_ONE_OFF_VARIANT):
      return {
        ...state,
        oneoffDeleteInProgress: false
      };
    case SUCCESS(ACTION_TYPES.GET_FREEZE_STATUS):
      return {
        ...state,
        subscriptionContractFreezeStatus: action.payload.data?.freezeStatus,
        subscriptionContractFreezeStatusMessage: action.payload.data?.errorMessage
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

const apiUrl = 'api/subscription-contracts';

// Actions

export const getEntities: ICrudGetAllAction<ISubscription> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTION_LIST,
  payload: axios.get<ISubscription>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscription> = id => {
  const requestUrl = `${apiUrl}/contract/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTION,
    payload: axios.get<ISubscription>(requestUrl)
  };
};

export const getCustomerPortalEntity: ICrudGetAction<ISubscription> = (id, api_key?) => {
  const requestUrl = `${apiUrl}/contract-external/${id}?isExternal=true`;
  //   const requestUrl = `api/external/v2/subscription-contracts/contract-external/${id}?api_key=${window?.appstle_api_key}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTION,
    payload: axios.get<ISubscription>(requestUrl)
  };
};

export const deleteOneOffProducts = (variantId, contractId, billingAttemptId, appstle_api_key?) => async dispatch => {
  const requestUrl = `api/subscription-contract-one-offs-by-contractId-and-billing-attempt-id?billingAttemptId=${billingAttemptId}&contractId=${contractId}&variantId=${variantId}&isExternal=true`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ONE_OFF_VARIANT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getCustomerPortalEntity(contractId.toString()));
  return result;
};

// export const createEntity: ICrudPutAction<ISubscription> = entity => async dispatch => {
//   const result = await dispatch({
//     type: ACTION_TYPES.CREATE_SUBSCRIPTION,
//     payload: axios.post(apiUrl, cleanEntity(entity))
//   });
//   dispatch(getEntities());
//   return result;
// };

export const updateBillingInterval: ICrudPutAction<ISubscription> = (contractId?, interval?, intervalCount?, shop?) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-billing-interval?contractId=${contractId}&interval=${interval}&intervalCount=${intervalCount}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BILLING_INTERVAL,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateDeliveryInterval: ICrudPutAction<ISubscription> = (contractId?, interval?, intervalCount?, shop?) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-delivery-interval?contractId=${contractId}&deliveryInterval=${interval}&deliveryIntervalCount=${intervalCount}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DELIVERY_INTERVAL,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateDeliveryMethodName: ICrudPutAction<ISubscription> = (contractId?, deliveryMethodName?) => async dispatch => {
  const encodedDeliveryMethodName = encodeURIComponent(deliveryMethodName);

  const requestUrl = `/api/subscription-contracts-update-delivery-method?contractId=${contractId}&delivery-method-name=${encodedDeliveryMethodName}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DELIVERY_METHOD_NAME,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateCustomerBillingInterval: ICrudPutAction<ISubscription> = (
  contractId?,
  interval?,
  intervalCount?,
  shop?
) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-billing-interval?contractId=${contractId}&interval=${interval}&intervalCount=${intervalCount}&isExternal=true`;
  // const requestUrl = `/api/external/v2/subscription-contracts-update-billing-interval?contractId=${contractId}&interval=${interval}&api_key=${window?.appstle_api_key}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BILLING_DATE,
    payload: axios.put(requestUrl)
  });
  dispatch(getCustomerPortalEntity(contractId.toString()));
  return result;
};

export const updateBillingDate: ICrudPutAction<ISubscription> = (contractId?, nextBillingDate?, shop?) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-billing-date?contractId=${contractId}&nextBillingDate=${nextBillingDate}&shop=${shop}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BILLING_DATE,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateCustomerBillingDate: ICrudPutAction<ISubscription> = (contractId?, nextBillingDate?, api_key?) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-billing-date?contractId=${contractId}&nextBillingDate=${nextBillingDate}&isExternal=true`;
  // const requestUrl = `api/external/v2/subscription-contracts-update-billing-date?contractId=${contractId}&nextBillingDate=${nextBillingDate}&api_key=${window?.appstle_api_key}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BILLING_DATE,
    payload: axios.put(requestUrl)
  });
  dispatch(getCustomerPortalEntity(contractId.toString()));
  return result;
};

export const updateShippingAddress: ICrudPutAction<ISubscription> = (entity, contractId?, shop?) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-shipping-address?contractId=${contractId}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHIPPING_ADDRESS,
    payload: axios.put(requestUrl, entity)
  });
  dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateCustomerShippingAddress = (entity: any, contractId: number, api_key?) => async (dispatch: any) => {
  const requestUrl = `/api/subscription-contracts-update-shipping-address?contractId=${contractId}&isExternal=true`;
  //   const requestUrl = `api/external/v2/subscription-contracts-update-shipping-address?contractId=${contractId}&api_key=${window?.appstle_api_key}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHIPPING_ADDRESS,
    payload: axios.put(requestUrl, entity).catch(err => err)
  });
  dispatch(getCustomerPortalEntity(contractId.toString()));
  return result;
};

export const updatePaymentDetail: ICrudPutAction<ISubscription> = (contractId?, shop?) => async dispatch => {
  const requestUrl = `api/subscription-contracts-update-payment-method?contractId=${contractId}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTDETAIL,
    payload: axios.put(requestUrl)
  });
  return result;
};

export const updateCustomerPaymentDetail: ICrudPutAction<ISubscription> = (contractId?, api_key?) => async dispatch => {
  const requestUrl = `api/subscription-contracts-update-payment-method?contractId=${contractId}&isExternal=true`;
  //   const requestUrl = `api/external/v2/subscription-contracts-update-payment-method?contractId=${contractId}&api_key=${window?.appstle_api_key}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTDETAIL,
    payload: axios.put(requestUrl)
  });
  return result;
};

export const updateCustomerExistingPaymentDetail: ICrudPutAction<ISubscription> = (contractId?, paymentMethodId?) => async dispatch => {
  const requestUrl = `api/subscription-contracts-update-existing-payment-method?contractId=${contractId}&paymentMethodId=${paymentMethodId}&isExternal=true`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTDETAIL,
    payload: axios.put(requestUrl)
  });
  return result;
};

export const updateDeliveryPrice: ICrudPutAction<ISubscription> = (contractId?, deliveryPrice?, shop?) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-delivery-price?contractId=${contractId}&deliveryPrice=${deliveryPrice}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DELIVERYPRICE,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateMinCycles: ICrudPutAction<ISubscription> = (contractId?, minCycles?, shop?) => async dispatch => {
  const requestUrl = `api/subscription-contracts-update-min-cycles?contractId=${contractId}&minCycles=${minCycles}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MINCYCLE,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateMaxCycles: ICrudPutAction<ISubscription> = (contractId?, maxCycles?, shop?) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-max-cycles?contractId=${contractId}&maxCycles=${maxCycles}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MAXCYCLE,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateLineItems = (contractId, price, isPricePerUnit = false, lineId, selectedVariant, quantity, shop) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-update-line-item?contractId=${contractId}&price=${price}&isPricePerUnit=${isPricePerUnit}&lineId=${lineId}&quantity=${quantity}&variantId=${selectedVariant}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LINEITEM,
    payload: axios.put(requestUrl)
  });
  // dispatch(getEntity(contractId.toString()));
  return result;
};

export const updateCustomerDeliveryPrice: ICrudPutAction<ISubscription> = (contractId?, deliveryPrice?, api_key?) => async dispatch => {
  const requestUrl = `api/subscription-contracts-update-delivery-price?contractId=${contractId}&deliveryPrice=${deliveryPrice}&isExternal=true`;
  //   const requestUrl = `api/external/v2/subscription-contracts-update-delivery-price?contractId=${contractId}&deliveryPrice=${deliveryPrice}&api_key=${window?.appstle_api_key}`;
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DELIVERYPRICE,
    payload: axios.put(requestUrl)
  });
  dispatch(getCustomerPortalEntity(contractId.toString()));
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscription> = (id, shop?) => async dispatch => {
  const requestUrl = `${apiUrl}/internal/${id}?shop=${shop}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntity(id));
  return result;
};

export const removeDiscount = (contractId, discountId, shop) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-remove-discount?contractId=${contractId}&discountId=${discountId}`;
  const result = await dispatch({
    type: ACTION_TYPES.REMOVE_DISCOUNT,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId));
  return result;
};

export const removeDiscountOnCustomerPortal = (contractId, discountId, api_key) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-remove-discount?contractId=${contractId}&discountId=${discountId}`;
  // const requestUrl = `/api/external/v2/subscription-contracts-remove-discount?contractId=${contractId}&discountId=${discountId}&api_key=${window?.appstle_api_key}`;
  const result = await dispatch({
    type: ACTION_TYPES.REMOVE_DISCOUNT,
    payload: axios.put(requestUrl)
  });
  dispatch(getCustomerPortalEntity(contractId.toString()));
  return result;
};

export const removeProduct = (contractId, lineId, shop) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}`;
  const result = await dispatch({
    type: ACTION_TYPES.REMOVE_PRODUCT,
    payload: axios.put(requestUrl)
  });
  // dispatch(getEntity(contractId));
  // dispatch(SubscriptionContractGetEntities());
  return result;
};

export const applyDiscount = (contractId, discountCode, shop) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-apply-discount?contractId=${contractId}&discountCode=${encodeURIComponent(discountCode)}`;
  const result = await dispatch({
    type: ACTION_TYPES.APPLY_DISCOUNT,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId));
  return result;
};

export const applyDiscountOnCustomerPortal = (contractId, discountCode, api_key) => async dispatch => {
  const requestUrl = `/api/subscription-contracts-apply-discount?contractId=${contractId}&discountCode=${encodeURIComponent(
    discountCode
  )}&isExternal=true`;
  //   const requestUrl = `/api/external/v2/subscription-contracts-apply-discount?contractId=${contractId}&discountCode=${discountCode}&api_key=${window?.appstle_api_key}`;
  const result = await dispatch({
    type: ACTION_TYPES.APPLY_DISCOUNT,
    payload: axios.put(requestUrl)
  });
  dispatch(getCustomerPortalEntity(contractId.toString()));
  return result;
};

export const addDiscountCode = (contractId: string, values: any) => async (dispatch: any) => {
  const requestParams = { contractId, ...values };
  const requestUrl = `/api/subscription-contracts-add-discount?${new URLSearchParams(requestParams).toString()}`;
  const result = await dispatch({
    type: ACTION_TYPES.ADD_DISCOUNT_CODE,
    payload: axios.put(requestUrl)
  });
  dispatch(getEntity(contractId));
  return result;
};

export const deleteCustomerEntity = (id: number, cansellationReasons?: any) => async (dispatch: any) => {
  const requestUrl = `/api/subscription-contracts/${id}?isExternal=true${
    cansellationReasons?.cancellationFeedback ? `&cancellationFeedback=${cansellationReasons?.cancellationFeedback}` : ``
  }${cansellationReasons?.cancellationTextAreaFeedback ? `&cancellationNote=${cansellationReasons?.cancellationTextAreaFeedback}` : ``}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getCustomerPortalEntity(id));
  return result;
};

export const getSubscriptionFreezeStatus = contractId => async dispatch => {
  const requestUrl = `api/subscription-contracts-freeze-status-detail?contractId=${contractId}`;
  const result = await dispatch({
    type: ACTION_TYPES.GET_FREEZE_STATUS,
    payload: axios.put(requestUrl)
  });
  return result;
};
// export const setBlob = (name, data, contentType?) => ({
//   type: ACTION_TYPES.SET_BLOB,
//   payload: {
//     name,
//     data,
//     contentType
//   }
// });

// export const reset = () => ({
//   type: ACTION_TYPES.RESET
// });
