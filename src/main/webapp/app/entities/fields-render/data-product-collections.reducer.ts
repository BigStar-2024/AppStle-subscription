import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductCollection, defaultValue } from 'app/shared/model/product-collection.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTCollection_LIST: 'productCollection/FETCH_PRODUCTCollection_LIST'
};

const initialState = {
  loading: false,
  errorMessage: null,
  prdCollectionOptions: { products: [], pageInfo: {} } as IProductCollection,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ProductCollectionState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductCollectionState = initialState, action): ProductCollectionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTCollection_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTCollection_LIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTCollection_LIST):
      return {
        ...state,
        loading: false,
        prdCollectionOptions: action.payload.data
      };
    default:
      return state;
  }
};

const apiUrl = '/api/data/product-collections';

// Actions

export const getPrdCollectionOptions: ICrudGetAllAction<IProductCollection> = sort => {
  const requestUrl = `${apiUrl}?cursor=${sort}&cacheBuster=${new Date().getTime()}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTCollection_LIST,
    payload: axios.get<IProductCollection>(requestUrl)
  };
};
