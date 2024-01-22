import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserSubscriptionContractDetails, defaultValue } from 'app/shared/model/user-subscription-contract-details.model';
import _ from 'lodash';

export const ACTION_TYPES = {
  FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_LIST: 'userSubscriptionContractDetails/FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_LIST',
  FETCH_USERSUBSCRIPTIONCONTRACTDETAILS: 'userSubscriptionContractDetails/FETCH_USERSUBSCRIPTIONCONTRACTDETAILS',
  FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_ORDER_NOTE: 'userSubscriptionContractDetails/FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_ORDER_NOTE',
  CREATE_USERSUBSCRIPTIONCONTRACTDETAILS: 'userSubscriptionContractDetails/CREATE_USERSUBSCRIPTIONCONTRACTDETAILS',
  UPDATE_USERSUBSCRIPTIONCONTRACTDETAILS: 'userSubscriptionContractDetails/UPDATE_USERSUBSCRIPTIONCONTRACTDETAILS',
  DELETE_USERSUBSCRIPTIONCONTRACTDETAILS: 'userSubscriptionContractDetails/DELETE_USERSUBSCRIPTIONCONTRACTDETAILS',
  RESET: 'userSubscriptionContractDetails/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserSubscriptionContractDetails>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  orderNote: '',
  updateSuccess: false
};

export type UserSubscriptionContractDetailsState = Readonly<typeof initialState>;

// Reducer

export default (state: UserSubscriptionContractDetailsState = initialState, action): UserSubscriptionContractDetailsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS):
    case REQUEST(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_ORDER_NOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERSUBSCRIPTIONCONTRACTDETAILS):
    case REQUEST(ACTION_TYPES.UPDATE_USERSUBSCRIPTIONCONTRACTDETAILS):
    case REQUEST(ACTION_TYPES.DELETE_USERSUBSCRIPTIONCONTRACTDETAILS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS):
    case FAILURE(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_ORDER_NOTE):
    case FAILURE(ACTION_TYPES.CREATE_USERSUBSCRIPTIONCONTRACTDETAILS):
    case FAILURE(ACTION_TYPES.UPDATE_USERSUBSCRIPTIONCONTRACTDETAILS):
    case FAILURE(ACTION_TYPES.DELETE_USERSUBSCRIPTIONCONTRACTDETAILS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERSUBSCRIPTIONCONTRACTDETAILS):
    case SUCCESS(ACTION_TYPES.UPDATE_USERSUBSCRIPTIONCONTRACTDETAILS):
    case SUCCESS(ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_ORDER_NOTE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERSUBSCRIPTIONCONTRACTDETAILS):
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

const apiUrl = 'api/subscription-contract-details';

// Actions

export const getEntities = (page, size, sort, filterVal) => {
  const requestUrl = `${apiUrl}/customers`;
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
    type: ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS_LIST,
    payload: axios.get(requestUrl, { params: params })
  };
};

export const getEntity: ICrudGetAction<IUserSubscriptionContractDetails> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.get<IUserSubscriptionContractDetails>(requestUrl)
  };
};

export const getSubscriptionContractDetailsByContractId = contractId => {
  const requestUrl = `${apiUrl}/customer/${contractId}`;
  return {
    type: ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.get(requestUrl)
  };
};

export const getCustomerSubscriptionContractDetailsByContractId = contractId => {
  const requestUrl = `api/subscription-contract-details/customer/${contractId}`;
  return {
    type: ACTION_TYPES.FETCH_USERSUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.get(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserSubscriptionContractDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERSUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserSubscriptionContractDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERSUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserSubscriptionContractDetails> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERSUBSCRIPTIONCONTRACTDETAILS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
