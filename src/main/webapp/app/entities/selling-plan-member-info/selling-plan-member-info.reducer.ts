import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISellingPlanMemberInfo, defaultValue } from 'app/shared/model/selling-plan-member-info.model';
import _ from 'lodash';

export const ACTION_TYPES = {
  FETCH_SELLINGPLANMEMBERINFO_LIST: 'sellingPlanMemberInfo/FETCH_SELLINGPLANMEMBERINFO_LIST',
  FETCH_SELLINGPLANMEMBERINFO: 'sellingPlanMemberInfo/FETCH_SELLINGPLANMEMBERINFO',
  CREATE_SELLINGPLANMEMBERINFO: 'sellingPlanMemberInfo/CREATE_SELLINGPLANMEMBERINFO',
  UPDATE_SELLINGPLANMEMBERINFO: 'sellingPlanMemberInfo/UPDATE_SELLINGPLANMEMBERINFO',
  DELETE_SELLINGPLANMEMBERINFO: 'sellingPlanMemberInfo/DELETE_SELLINGPLANMEMBERINFO',
  RESET: 'sellingPlanMemberInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISellingPlanMemberInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SellingPlanMemberInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: SellingPlanMemberInfoState = initialState, action): SellingPlanMemberInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SELLINGPLANMEMBERINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SELLINGPLANMEMBERINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SELLINGPLANMEMBERINFO):
    case REQUEST(ACTION_TYPES.UPDATE_SELLINGPLANMEMBERINFO):
    case REQUEST(ACTION_TYPES.DELETE_SELLINGPLANMEMBERINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SELLINGPLANMEMBERINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SELLINGPLANMEMBERINFO):
    case FAILURE(ACTION_TYPES.CREATE_SELLINGPLANMEMBERINFO):
    case FAILURE(ACTION_TYPES.UPDATE_SELLINGPLANMEMBERINFO):
    case FAILURE(ACTION_TYPES.DELETE_SELLINGPLANMEMBERINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SELLINGPLANMEMBERINFO_LIST):
      let list = [];
      let subscriptionGroups = action.payload[0].data;
      let sellingPlanMemberInfos = action.payload[1].data;
      const defaultData = {
        enableMemberInclusiveTag: false,
        memberInclusiveTags: '',
        enableMemberExclusiveTag: false,
        memberExclusiveTags: ''
      };
      _.each(subscriptionGroups, o => {
        try {
          let json = JSON.parse(o.infoJson);
          _.each(json.subscriptionPlans, sp => {
            let spId = _.last(_.split(sp.id, '/'));
            let findSP = _.find(sellingPlanMemberInfos, msp => msp.sellingPlanId == spId);
            let spData = findSP ? findSP : defaultData;
            list.push({
              ...sp,
              groupName: o.groupName,
              groupId: o.id,
              sellingPlanId: spId,
              subscriptionId: o.subscriptionId,
              ...spData
            });
          });
        } catch (error) {}
      });
      return {
        ...state,
        loading: false,
        entities: list
      };
    case SUCCESS(ACTION_TYPES.FETCH_SELLINGPLANMEMBERINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SELLINGPLANMEMBERINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_SELLINGPLANMEMBERINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SELLINGPLANMEMBERINFO):
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

const apiUrl = 'api/selling-plan-member-infos';

// Actions

export const getEntities = (page, size, sort) => {
  let processes = [];
  processes.push(axios.get(`${apiUrl}?cacheBuster=${new Date().getTime()}`));
  processes.push(axios.get(`${apiUrl}/get-info?cacheBuster=${new Date().getTime()}`));
  return {
    type: ACTION_TYPES.FETCH_SELLINGPLANMEMBERINFO_LIST,
    payload: Promise.all(processes)
  };
};

export const getEntity: ICrudGetAction<ISellingPlanMemberInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SELLINGPLANMEMBERINFO,
    payload: axios.get<ISellingPlanMemberInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISellingPlanMemberInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SELLINGPLANMEMBERINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISellingPlanMemberInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SELLINGPLANMEMBERINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISellingPlanMemberInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SELLINGPLANMEMBERINFO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
