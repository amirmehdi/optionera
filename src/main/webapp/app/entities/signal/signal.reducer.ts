import axios from 'axios';
import {
  ICrudDeleteAction,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  loadMoreDataWhenScrolled,
  parseHeaderForLinks
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, ISignal } from 'app/shared/model/signal.model';
import { IOrder } from 'app/shared/model/order.model';

export const ACTION_TYPES = {
  FETCH_SIGNAL_LIST: 'signal/FETCH_SIGNAL_LIST',
  FETCH_SIGNAL: 'signal/FETCH_SIGNAL',
  CREATE_SIGNAL: 'signal/CREATE_SIGNAL',
  UPDATE_SIGNAL: 'signal/UPDATE_SIGNAL',
  DELETE_SIGNAL: 'signal/DELETE_SIGNAL',
  FETCH_ORDER: 'order/FETCH_ORDER',
  RESET: 'signal/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISignal>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SignalState = Readonly<typeof initialState>;

// Reducer

export default (state: SignalState = initialState, action): SignalState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SIGNAL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SIGNAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SIGNAL):
    case REQUEST(ACTION_TYPES.UPDATE_SIGNAL):
    case REQUEST(ACTION_TYPES.DELETE_SIGNAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SIGNAL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SIGNAL):
    case FAILURE(ACTION_TYPES.CREATE_SIGNAL):
    case FAILURE(ACTION_TYPES.UPDATE_SIGNAL):
    case FAILURE(ACTION_TYPES.DELETE_SIGNAL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SIGNAL_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_SIGNAL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SIGNAL):
    case SUCCESS(ACTION_TYPES.UPDATE_SIGNAL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SIGNAL):
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

const apiUrl = 'api/signals';

// Actions

export const getEntities: ICrudGetAllAction<ISignal> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SIGNAL_LIST,
    payload: axios.get<ISignal>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISignal> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SIGNAL,
    payload: axios.get<ISignal>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISignal> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SIGNAL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const sendOrder: ICrudGetAction<IOrder> = id => {
  const requestUrl = `${apiUrl}/tg-order/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORDER,
    payload: axios.post<IOrder>(requestUrl)
  };
};

export const updateEntity: ICrudPutAction<ISignal> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SIGNAL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISignal> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SIGNAL,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
