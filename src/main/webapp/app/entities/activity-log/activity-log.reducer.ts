import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IActivityLog } from 'app/shared/model/activity-log.model';

export const ACTION_TYPES = {
  FETCH_ACTIVITYLOG_LIST: 'activityLog/FETCH_ACTIVITYLOG_LIST',
  FETCH_ACTIVITYLOG: 'activityLog/FETCH_ACTIVITYLOG',
  CREATE_ACTIVITYLOG: 'activityLog/CREATE_ACTIVITYLOG',
  UPDATE_ACTIVITYLOG: 'activityLog/UPDATE_ACTIVITYLOG',
  DELETE_ACTIVITYLOG: 'activityLog/DELETE_ACTIVITYLOG',
  FETCH_ACTIVITYLOG_FROM_SUBSCRIPTIONCONTRACT: 'activityLog/FETCH_ACTIVITYLOG_FROM_SUBSCRIPTIONCONTRACT',
  SET_BLOB: 'activityLog/SET_BLOB',
  EXPORT_ACTIVITY_LOGS: 'activityLog/EXPORT_ACTIVITY_LOGS',
  RESET: 'activityLog/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IActivityLog>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  exporting: false
};

export type ActivityLogState = Readonly<typeof initialState>;

// Reducer

export default (state: ActivityLogState = initialState, action): ActivityLogState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITYLOG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITYLOG):
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITYLOG_FROM_SUBSCRIPTIONCONTRACT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ACTIVITYLOG):
    case REQUEST(ACTION_TYPES.UPDATE_ACTIVITYLOG):
    case REQUEST(ACTION_TYPES.DELETE_ACTIVITYLOG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITYLOG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITYLOG):
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITYLOG_FROM_SUBSCRIPTIONCONTRACT):
    case FAILURE(ACTION_TYPES.CREATE_ACTIVITYLOG):
    case FAILURE(ACTION_TYPES.UPDATE_ACTIVITYLOG):
    case FAILURE(ACTION_TYPES.DELETE_ACTIVITYLOG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITYLOG_LIST):
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITYLOG_FROM_SUBSCRIPTIONCONTRACT):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITYLOG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACTIVITYLOG):
    case SUCCESS(ACTION_TYPES.UPDATE_ACTIVITYLOG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACTIVITYLOG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
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
    case REQUEST(ACTION_TYPES.EXPORT_ACTIVITY_LOGS):
      return {
        ...state,
        exporting: true
      };
    case FAILURE(ACTION_TYPES.EXPORT_ACTIVITY_LOGS):
      return {
        ...state,
        exporting: false
      };
    case SUCCESS(ACTION_TYPES.EXPORT_ACTIVITY_LOGS):
      return {
        ...state,
        exporting: false
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/activity-logs';

// Actions

export const getEntities: ICrudGetAllAction<IActivityLog> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYLOG_LIST,
    payload: axios.get<IActivityLog>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getFilteredEntities = queryParam => {
  const requestUrl = `${apiUrl}?${queryParam}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYLOG_LIST,
    payload: axios.get<IActivityLog>(`${requestUrl}`)
  };
};

export const getFilteredActivityLogEntities = (page, size, sort, filter) => {
  var params = prepareQueryParams(page, size, sort, filter);
  const requestUrl = `${apiUrl}${params ? `?${params}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYLOG_LIST,
    payload: axios.get<IActivityLog>(`${requestUrl}${sort ? '&' : '?'}`)
  };
};

export const getEntity: ICrudGetAction<IActivityLog> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYLOG,
    payload: axios.get<IActivityLog>(requestUrl)
  };
};

export const getActivityLogFromSubscription = activityLogCriteria => {
  const requestUrl = `${apiUrl}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYLOG_FROM_SUBSCRIPTIONCONTRACT,
    payload: axios.get(requestUrl, { params: { criteria: cleanEntity(activityLogCriteria) } })
  };
};

export const createEntity: ICrudPutAction<IActivityLog> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACTIVITYLOG,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IActivityLog> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACTIVITYLOG,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IActivityLog> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACTIVITYLOG,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

// export const exportActivityLogs = () => async dispatch => {
//   const requestUrl = `api/activity-logs/export/all`;
//   const result = await dispatch({
//     type: ACTION_TYPES.EXPORT_ACTIVITY_LOGS,
//     payload: axios.get(requestUrl)
//   });
//   return result;
// };

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

const prepareQueryParams = (page, size, sort, filter) => {
  const params = [];
  params.push(encodeURIComponent('page') + '=' + encodeURIComponent(page ? page : 0));

  if (size) {
    params.push(encodeURIComponent('size') + '=' + encodeURIComponent(size));
  }
  if (sort) {
    params.push(encodeURIComponent('sort') + '=' + encodeURIComponent(sort));
  }
  if (filter) {
    if (filter.entityType) {
      params.push(encodeURIComponent('entityType.in') + '=' + encodeURIComponent(filter.entityType));
    }

    if (filter.eventSource) {
      params.push(encodeURIComponent('eventSource.in') + '=' + encodeURIComponent(filter.eventSource));
    }
    if (filter.eventType) {
      params.push(encodeURIComponent('eventType.in') + '=' + encodeURIComponent(filter.eventType));
    }

    if (filter.status) {
      params.push(encodeURIComponent('status.in') + '=' + encodeURIComponent(filter.status));
    }

    if (filter.entityId) {
      params.push(encodeURIComponent('entityId.in') + '=' + encodeURIComponent(filter.entityId));
    }

    if (filter.fromCreatedDate) {
      params.push(
        encodeURIComponent('createAt.greaterThanOrEqual') + '=' + encodeURIComponent(new Date(filter.fromCreatedDate).toISOString())
      );
    }

    if (filter.toCreatedDate) {
      params.push(encodeURIComponent('createAt.lessThanOrEqual') + '=' + encodeURIComponent(new Date(filter.toCreatedDate).toISOString()));
    }

    params.push(encodeURIComponent('cacheBuster') + '=' + encodeURIComponent(`${new Date().getTime()}`));
    return params.join('&');
  }
};
