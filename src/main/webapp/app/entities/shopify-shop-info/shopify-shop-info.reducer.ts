import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_SHOPIFY_SHOP_INFO: 'shopify/FETCH_SHOPIFY_SHOP_INFO'
};

const initialState = {
  loading: false,
  shopifyShopInfo: null,
  errorMessage: null
};

export type ShopifyShopInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopifyShopInfoState = initialState, action): ShopifyShopInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHOPIFY_SHOP_INFO):
      return {
        ...state,
        loading: true,
        errorMessage: null
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPIFY_SHOP_INFO):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPIFY_SHOP_INFO):
      return {
        ...state,
        loading: false,
        shopifyShopInfo: action.payload.data
      };
    default:
      return state;
  }
};

const apiUrl = 'api/shopify-shop-info';

export const getShopifyShopInfo = () => {
  const requestUrl = `${apiUrl}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPIFY_SHOP_INFO,
    payload: axios.get(requestUrl)
  };
};
