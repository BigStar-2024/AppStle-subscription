import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICarrierService, defaultValue } from 'app/shared/model/carrier-service.model';

export const ACTION_TYPES = {
  FETCH_CARRIERService_LIST: 'carrierService/FETCH_CARRIERService_LIST'
};

const initialState = {
  loading: false,
  errorMessage: null,
  carrierServiceOptions: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CarrierServiceState = Readonly<typeof initialState>;

// Reducer

export default (state: CarrierServiceState = initialState, action): CarrierServiceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CARRIERService_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CARRIERService_LIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARRIERService_LIST):
      let options = action.payload.data?.carrierServiceOptions?.map(item => {
        return { value: item, label: item };
      });
      return {
        ...state,
        loading: false,
        carrierServiceOptions: options
      };
    default:
      return state;
  }
};

const apiUrl = '/api/data/carrier-services';

// Actions

export const getCarrierServiceOptions: ICrudGetAllAction<ICarrierService> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CARRIERService_LIST,
  payload: axios.get<ICarrierService>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});
