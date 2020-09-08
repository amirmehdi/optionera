import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInstrumentHistory, defaultValue } from 'app/shared/model/instrument-history.model';

export const ACTION_TYPES = {
  FETCH_INSTRUMENTHISTORY_LIST: 'instrumentHistory/FETCH_INSTRUMENTHISTORY_LIST',
  FETCH_INSTRUMENTHISTORY: 'instrumentHistory/FETCH_INSTRUMENTHISTORY',
  CREATE_INSTRUMENTHISTORY: 'instrumentHistory/CREATE_INSTRUMENTHISTORY',
  UPDATE_INSTRUMENTHISTORY: 'instrumentHistory/UPDATE_INSTRUMENTHISTORY',
  DELETE_INSTRUMENTHISTORY: 'instrumentHistory/DELETE_INSTRUMENTHISTORY',
  RESET: 'instrumentHistory/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInstrumentHistory>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type InstrumentHistoryState = Readonly<typeof initialState>;

// Reducer

export default (state: InstrumentHistoryState = initialState, action): InstrumentHistoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_INSTRUMENTHISTORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INSTRUMENTHISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INSTRUMENTHISTORY):
    case REQUEST(ACTION_TYPES.UPDATE_INSTRUMENTHISTORY):
    case REQUEST(ACTION_TYPES.DELETE_INSTRUMENTHISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_INSTRUMENTHISTORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INSTRUMENTHISTORY):
    case FAILURE(ACTION_TYPES.CREATE_INSTRUMENTHISTORY):
    case FAILURE(ACTION_TYPES.UPDATE_INSTRUMENTHISTORY):
    case FAILURE(ACTION_TYPES.DELETE_INSTRUMENTHISTORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_INSTRUMENTHISTORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_INSTRUMENTHISTORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INSTRUMENTHISTORY):
    case SUCCESS(ACTION_TYPES.UPDATE_INSTRUMENTHISTORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INSTRUMENTHISTORY):
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

const apiUrl = 'api/instrument-histories';

// Actions

export const getEntities: ICrudGetAllAction<IInstrumentHistory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INSTRUMENTHISTORY_LIST,
    payload: axios.get<IInstrumentHistory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IInstrumentHistory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INSTRUMENTHISTORY,
    payload: axios.get<IInstrumentHistory>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IInstrumentHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INSTRUMENTHISTORY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInstrumentHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INSTRUMENTHISTORY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInstrumentHistory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INSTRUMENTHISTORY,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
