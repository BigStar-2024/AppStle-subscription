import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductVariant, defaultValue } from 'app/shared/model/product-variant.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTVariant_LIST: 'productVariant/FETCH_PRODUCTVariant_LIST',
};

const initialState = {
  loading: false,
  errorMessage: null,
  prdVariantOptions: { products: [], pageInfo: {} } as IProductVariant,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ProductVariantState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductVariantState = initialState, action): ProductVariantState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVariant_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVariant_LIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVariant_LIST):
      // debugger
      // let products = [...state.prdVariantOptions?.products,...action.payload.data.products];
      // let pageInfo = action.payload.data.pageInfo;
      return {
        ...state,
        loading: false,
        prdVariantOptions: action.payload.data,
      };
    default:
      return state;
  }
};

const apiUrl = '/api/data/product-variants';

// Actions
let cancelToken;
export const getPrdVariantOptions: ICrudGetAllAction<IProductVariant> = sort => {
  const requestUrl = `${apiUrl}?cursor=${sort}&cacheBuster=${new Date().getTime()}`;

  if (typeof cancelToken != typeof undefined) {
    cancelToken.cancel('Operation canceled due to new request.');
  }
  cancelToken = axios.CancelToken.source();

  const getAndHandleCancel = new Promise<AxiosResponse<IProductVariant>>(resolve => {
    axios
      .get<IProductVariant>(requestUrl, { cancelToken: cancelToken.token })
      .then(result => {
        resolve(result);
      })
      .catch(e => {
        console.warn(e.message);
      });
  });

  return {
    type: ACTION_TYPES.FETCH_PRODUCTVariant_LIST,
    payload: getAndHandleCancel,
  };
};
