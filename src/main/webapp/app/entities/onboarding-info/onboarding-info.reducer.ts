import axios from 'axios';
import { ICrudGetAllAction, ICrudPutAction, IPayloadResult, IPayload } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOnboardingInfo, defaultValue } from 'app/shared/model/onboarding-info.model';
import OnboardingChecklistStep from 'app/shared/model/enumerations/onboarding-checklist-step.model';
import { IRootState } from 'app/shared/reducers';

export const ACTION_TYPES = {
  FETCH_ONBOARDINGINFO_LIST: 'onboardingInfo/FETCH_ONBOARDINGINFO_LIST',
  FETCH_ONBOARDINGINFO: 'onboardingInfo/FETCH_ONBOARDINGINFO',
  CREATE_ONBOARDINGINFO: 'onboardingInfo/CREATE_ONBOARDINGINFO',
  UPDATE_ONBOARDINGINFO: 'onboardingInfo/UPDATE_ONBOARDINGINFO',
  DELETE_ONBOARDINGINFO: 'onboardingInfo/DELETE_ONBOARDINGINFO',
  RESET: 'onboardingInfo/RESET',
  COMPLETE_CHECKLIST_ITEM: 'onboardingInfo/COMPLETE_CHECKLIST_ITEM',
  MARK_CHECKLIST_COMPLETED: 'onboardingInfo/MARK_CHECKLIST_COMPLETED'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOnboardingInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type OnboardingInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: OnboardingInfoState = initialState, action): OnboardingInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ONBOARDINGINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ONBOARDINGINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ONBOARDINGINFO):
    case REQUEST(ACTION_TYPES.UPDATE_ONBOARDINGINFO):
    case REQUEST(ACTION_TYPES.DELETE_ONBOARDINGINFO):
    case REQUEST(ACTION_TYPES.COMPLETE_CHECKLIST_ITEM):
    case REQUEST(ACTION_TYPES.MARK_CHECKLIST_COMPLETED):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ONBOARDINGINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ONBOARDINGINFO):
    case FAILURE(ACTION_TYPES.CREATE_ONBOARDINGINFO):
    case FAILURE(ACTION_TYPES.UPDATE_ONBOARDINGINFO):
    case FAILURE(ACTION_TYPES.DELETE_ONBOARDINGINFO):
    case FAILURE(ACTION_TYPES.COMPLETE_CHECKLIST_ITEM):
    case FAILURE(ACTION_TYPES.MARK_CHECKLIST_COMPLETED):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ONBOARDINGINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ONBOARDINGINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ONBOARDINGINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_ONBOARDINGINFO):
    case SUCCESS(ACTION_TYPES.COMPLETE_CHECKLIST_ITEM):
    case SUCCESS(ACTION_TYPES.MARK_CHECKLIST_COMPLETED):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ONBOARDINGINFO):
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

const apiUrl = 'api/onboarding-infos';

// Actions

export const getEntities: ICrudGetAllAction<IOnboardingInfo> = () => ({
  type: ACTION_TYPES.FETCH_ONBOARDINGINFO_LIST,
  payload: axios.get<IOnboardingInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: () => IPayload<IOnboardingInfo> = () => {
  const requestUrl = `${apiUrl}/0`;
  return {
    type: ACTION_TYPES.FETCH_ONBOARDINGINFO,
    payload: axios.get<IOnboardingInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOnboardingInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ONBOARDINGINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOnboardingInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ONBOARDINGINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: () => IPayload<IOnboardingInfo> | IPayloadResult<IOnboardingInfo> = () => async dispatch => {
  const requestUrl = `${apiUrl}/0`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ONBOARDINGINFO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

/**
 * Both of the below actions are using current entity in state, since each shop
 * will only have a single OnboardingInfo entity. Make sure there is a current
 * existing entity with getEntity.
 */

/**
 * Action for marking checklist item as completed or uncompleted by moving it
 * from uncompleted checklist to completed checklist
 */
export const completeChecklistItem = (step: OnboardingChecklistStep, isCompleted = true) => async (
  dispatch: any,
  getState: () => IRootState
) => {
  if (getState().onboardingInfo.entity.id == null) {
    await dispatch(getEntity());
  }

  const newEntity: IOnboardingInfo = { ...getState().onboardingInfo.entity };

  if (newEntity.completedChecklistSteps.includes(step) === isCompleted) return newEntity;

  if (isCompleted) {
    if (!newEntity.completedChecklistSteps.includes(step)) newEntity.completedChecklistSteps.push(step);
    if (newEntity.uncompletedChecklistSteps.includes(step))
      newEntity.uncompletedChecklistSteps = newEntity.uncompletedChecklistSteps.filter(s => s !== step);
  } else {
    if (!newEntity.uncompletedChecklistSteps.includes(step)) newEntity.uncompletedChecklistSteps.push(step);
    if (newEntity.completedChecklistSteps.includes(step))
      newEntity.completedChecklistSteps = newEntity.completedChecklistSteps.filter(s => s !== step);
  }
  const result = await dispatch({
    type: ACTION_TYPES.COMPLETE_CHECKLIST_ITEM,
    payload: axios.put(apiUrl, cleanEntity(newEntity))
  });
  return result;
};

export const updateChecklistCompleted = (isCompleted = true) => async (dispatch: any, getState: () => IRootState) => {
  if (getState().onboardingInfo.entity.id == null) {
    await dispatch(getEntity());
  }

  const currentEntity = getState().onboardingInfo.entity;

  if (currentEntity.checklistCompleted === isCompleted) return currentEntity;

  const newEntity = { ...currentEntity, checklistCompleted: isCompleted };

  const result = await dispatch({
    type: ACTION_TYPES.MARK_CHECKLIST_COMPLETED,
    payload: axios.put(apiUrl, cleanEntity(newEntity))
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
