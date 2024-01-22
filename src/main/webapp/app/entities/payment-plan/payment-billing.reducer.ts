import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPaymentPlan, defaultValue } from 'app/shared/model/payment-plan.model';

export const ACTION_TYPES = {
  FETCH_BILLING_URL: 'paymentPlan/FETCH_BILLING_URL',
  RESET_BILLING_URL: 'paymentPlan/RESET_BILLING_URL'
};

const initialState = {
  loading: false,
  confirmationUrl: null,
  errorMessage: null
};

export type BillingUrlState = Readonly<typeof initialState>;

// Reducer

export default (state: BillingUrlState = initialState, action): BillingUrlState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BILLING_URL):
      return {
        ...state,
        loading: true,
        errorMessage: null
      };
    case FAILURE(ACTION_TYPES.FETCH_BILLING_URL):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BILLING_URL):
      return {
        ...state,
        loading: false,
        confirmationUrl: action.payload.data
      };
    case ACTION_TYPES.RESET_BILLING_URL:
      return {
        ...state,
        confirmationUrl: null
      };
    default:
      return state;
  }
};

const billingUrl = 'api/shop-billing-confirmation-url';

export const getBillingUrl = (planId, discountCoupon = '', host = '') => {
  const requestUrl = `${billingUrl}/${planId}`;
  return {
    type: ACTION_TYPES.FETCH_BILLING_URL,
    payload: axios.get(requestUrl, { params: { discountCode: discountCoupon, host: host } })
  };
};

export const resetBillingUrl = () => {
  return {
    type: ACTION_TYPES.RESET_BILLING_URL,
    payload: 'RESET_URL'
  };
};
