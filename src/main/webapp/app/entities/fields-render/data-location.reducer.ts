import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILocation, defaultValue } from 'app/shared/model/location.model';

export const ACTION_TYPES = {
  FETCH_LOCATION_LIST: 'location/FETCH_LOCATION_LIST'
};

const initialState = {
  loading: false,
  errorMessage: null,
  addressOptions: [] as ReadonlyArray<ILocation>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LocationState = Readonly<typeof initialState>;

// Reducer

export default (state: LocationState = initialState, action): LocationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LOCATION_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LOCATION_LIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOCATION_LIST):
      let options = action.payload.data.map(item => {
        return { label: item.name, value: item.id };
      });
      return {
        ...state,
        loading: false,
        addressOptions: options
      };
    default:
      return state;
  }
};

const apiUrl = '/api/data/locations';

// Actions

export const getAddressOptions: ICrudGetAllAction<ILocation> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LOCATION_LIST,
  payload: axios.get<ILocation>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});
