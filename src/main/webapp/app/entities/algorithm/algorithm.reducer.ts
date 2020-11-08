import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IAlgorithm } from 'app/shared/model/algorithm.model';

export const ACTION_TYPES = {
  FETCH_ALGORITHM_LIST: 'algorithm/FETCH_ALGORITHM_LIST',
  FETCH_ALGORITHM: 'algorithm/FETCH_ALGORITHM',
  CREATE_ALGORITHM: 'algorithm/CREATE_ALGORITHM',
  UPDATE_ALGORITHM: 'algorithm/UPDATE_ALGORITHM',
  DELETE_ALGORITHM: 'algorithm/DELETE_ALGORITHM',
  RESET: 'algorithm/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAlgorithm>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AlgorithmState = Readonly<typeof initialState>;

// Reducer

export default (state: AlgorithmState = initialState, action): AlgorithmState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ALGORITHM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ALGORITHM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ALGORITHM):
    case REQUEST(ACTION_TYPES.UPDATE_ALGORITHM):
    case REQUEST(ACTION_TYPES.DELETE_ALGORITHM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ALGORITHM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ALGORITHM):
    case FAILURE(ACTION_TYPES.CREATE_ALGORITHM):
    case FAILURE(ACTION_TYPES.UPDATE_ALGORITHM):
    case FAILURE(ACTION_TYPES.DELETE_ALGORITHM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ALGORITHM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ALGORITHM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ALGORITHM):
    case SUCCESS(ACTION_TYPES.UPDATE_ALGORITHM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ALGORITHM):
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

const apiUrl = 'api/algorithms';

// Actions

export const getEntities: ICrudGetAllAction<IAlgorithm> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ALGORITHM_LIST,
    payload: axios.get<IAlgorithm>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAlgorithm> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ALGORITHM,
    payload: axios.get<IAlgorithm>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAlgorithm> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ALGORITHM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAlgorithm> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ALGORITHM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAlgorithm> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ALGORITHM,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
