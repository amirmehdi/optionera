import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBourseCode, defaultValue } from 'app/shared/model/bourse-code.model';

export const ACTION_TYPES = {
  FETCH_BOURSECODE_LIST: 'bourseCode/FETCH_BOURSECODE_LIST',
  FETCH_BOURSECODE: 'bourseCode/FETCH_BOURSECODE',
  CREATE_BOURSECODE: 'bourseCode/CREATE_BOURSECODE',
  UPDATE_BOURSECODE: 'bourseCode/UPDATE_BOURSECODE',
  DELETE_BOURSECODE: 'bourseCode/DELETE_BOURSECODE',
  RESET: 'bourseCode/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBourseCode>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type BourseCodeState = Readonly<typeof initialState>;

// Reducer

export default (state: BourseCodeState = initialState, action): BourseCodeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BOURSECODE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BOURSECODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BOURSECODE):
    case REQUEST(ACTION_TYPES.UPDATE_BOURSECODE):
    case REQUEST(ACTION_TYPES.DELETE_BOURSECODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BOURSECODE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BOURSECODE):
    case FAILURE(ACTION_TYPES.CREATE_BOURSECODE):
    case FAILURE(ACTION_TYPES.UPDATE_BOURSECODE):
    case FAILURE(ACTION_TYPES.DELETE_BOURSECODE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOURSECODE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOURSECODE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BOURSECODE):
    case SUCCESS(ACTION_TYPES.UPDATE_BOURSECODE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BOURSECODE):
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

const apiUrl = 'api/bourse-codes';

// Actions

export const getEntities: ICrudGetAllAction<IBourseCode> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BOURSECODE_LIST,
    payload: axios.get<IBourseCode>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IBourseCode> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BOURSECODE,
    payload: axios.get<IBourseCode>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBourseCode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BOURSECODE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBourseCode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BOURSECODE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBourseCode> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BOURSECODE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
