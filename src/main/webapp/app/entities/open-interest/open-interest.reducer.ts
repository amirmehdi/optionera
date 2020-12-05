import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOpenInterest, defaultValue } from 'app/shared/model/open-interest.model';

export const ACTION_TYPES = {
  FETCH_OPENINTEREST_LIST: 'openInterest/FETCH_OPENINTEREST_LIST',
  FETCH_OPENINTEREST: 'openInterest/FETCH_OPENINTEREST',
  CREATE_OPENINTEREST: 'openInterest/CREATE_OPENINTEREST',
  UPDATE_OPENINTEREST: 'openInterest/UPDATE_OPENINTEREST',
  DELETE_OPENINTEREST: 'openInterest/DELETE_OPENINTEREST',
  RESET: 'openInterest/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOpenInterest>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OpenInterestState = Readonly<typeof initialState>;

// Reducer

export default (state: OpenInterestState = initialState, action): OpenInterestState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_OPENINTEREST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_OPENINTEREST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_OPENINTEREST):
    case REQUEST(ACTION_TYPES.UPDATE_OPENINTEREST):
    case REQUEST(ACTION_TYPES.DELETE_OPENINTEREST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_OPENINTEREST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_OPENINTEREST):
    case FAILURE(ACTION_TYPES.CREATE_OPENINTEREST):
    case FAILURE(ACTION_TYPES.UPDATE_OPENINTEREST):
    case FAILURE(ACTION_TYPES.DELETE_OPENINTEREST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_OPENINTEREST_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_OPENINTEREST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_OPENINTEREST):
    case SUCCESS(ACTION_TYPES.UPDATE_OPENINTEREST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_OPENINTEREST):
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

const apiUrl = 'api/open-interests';

// Actions

export const getEntities: ICrudGetAllAction<IOpenInterest> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_OPENINTEREST_LIST,
    payload: axios.get<IOpenInterest>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOpenInterest> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_OPENINTEREST,
    payload: axios.get<IOpenInterest>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOpenInterest> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_OPENINTEREST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IOpenInterest> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_OPENINTEREST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOpenInterest> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_OPENINTEREST,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
