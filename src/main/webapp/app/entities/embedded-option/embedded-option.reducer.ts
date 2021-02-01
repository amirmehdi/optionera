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

import { IEmbeddedOption, defaultValue } from 'app/shared/model/embedded-option.model';

export const ACTION_TYPES = {
  FETCH_EMBEDDEDOPTION_LIST: 'embeddedOption/FETCH_EMBEDDEDOPTION_LIST',
  FETCH_EMBEDDEDOPTION: 'embeddedOption/FETCH_EMBEDDEDOPTION',
  CREATE_EMBEDDEDOPTION: 'embeddedOption/CREATE_EMBEDDEDOPTION',
  UPDATE_EMBEDDEDOPTION: 'embeddedOption/UPDATE_EMBEDDEDOPTION',
  DELETE_EMBEDDEDOPTION: 'embeddedOption/DELETE_EMBEDDEDOPTION',
  RESET: 'embeddedOption/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEmbeddedOption>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EmbeddedOptionState = Readonly<typeof initialState>;

// Reducer

export default (state: EmbeddedOptionState = initialState, action): EmbeddedOptionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EMBEDDEDOPTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EMBEDDEDOPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EMBEDDEDOPTION):
    case REQUEST(ACTION_TYPES.UPDATE_EMBEDDEDOPTION):
    case REQUEST(ACTION_TYPES.DELETE_EMBEDDEDOPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EMBEDDEDOPTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EMBEDDEDOPTION):
    case FAILURE(ACTION_TYPES.CREATE_EMBEDDEDOPTION):
    case FAILURE(ACTION_TYPES.UPDATE_EMBEDDEDOPTION):
    case FAILURE(ACTION_TYPES.DELETE_EMBEDDEDOPTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMBEDDEDOPTION_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_EMBEDDEDOPTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EMBEDDEDOPTION):
    case SUCCESS(ACTION_TYPES.UPDATE_EMBEDDEDOPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EMBEDDEDOPTION):
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

const apiUrl = 'api/embedded-options';

// Actions

export const getEntities: ICrudGetAllAction<IEmbeddedOption> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EMBEDDEDOPTION_LIST,
    payload: axios.get<IEmbeddedOption>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEmbeddedOption> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EMBEDDEDOPTION,
    payload: axios.get<IEmbeddedOption>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEmbeddedOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EMBEDDEDOPTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IEmbeddedOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EMBEDDEDOPTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEmbeddedOption> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EMBEDDEDOPTION,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
