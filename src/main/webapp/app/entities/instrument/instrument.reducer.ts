import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInstrument, defaultValue } from 'app/shared/model/instrument.model';

export const ACTION_TYPES = {
  FETCH_INSTRUMENT_LIST: 'instrument/FETCH_INSTRUMENT_LIST',
  FETCH_INSTRUMENT: 'instrument/FETCH_INSTRUMENT',
  CREATE_INSTRUMENT: 'instrument/CREATE_INSTRUMENT',
  UPDATE_INSTRUMENT: 'instrument/UPDATE_INSTRUMENT',
  DELETE_INSTRUMENT: 'instrument/DELETE_INSTRUMENT',
  RESET: 'instrument/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInstrument>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type InstrumentState = Readonly<typeof initialState>;

// Reducer

export default (state: InstrumentState = initialState, action): InstrumentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_INSTRUMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INSTRUMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INSTRUMENT):
    case REQUEST(ACTION_TYPES.UPDATE_INSTRUMENT):
    case REQUEST(ACTION_TYPES.DELETE_INSTRUMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_INSTRUMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INSTRUMENT):
    case FAILURE(ACTION_TYPES.CREATE_INSTRUMENT):
    case FAILURE(ACTION_TYPES.UPDATE_INSTRUMENT):
    case FAILURE(ACTION_TYPES.DELETE_INSTRUMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_INSTRUMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_INSTRUMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INSTRUMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_INSTRUMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INSTRUMENT):
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

const apiUrl = 'api/instruments';

// Actions

export const getEntities: ICrudGetAllAction<IInstrument> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INSTRUMENT_LIST,
    payload: axios.get<IInstrument>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IInstrument> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INSTRUMENT,
    payload: axios.get<IInstrument>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IInstrument> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INSTRUMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInstrument> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INSTRUMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInstrument> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INSTRUMENT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
