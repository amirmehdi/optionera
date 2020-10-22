import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudPutAction, loadMoreDataWhenScrolled, parseHeaderForLinks } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IOptionStats } from 'app/shared/model/option-stats.model';

export const ACTION_TYPES = {
  FETCH_OPTIONSTATS_LIST: 'optionStats/FETCH_OPTIONSTATS_LIST',
  FETCH_OPTIONSTATS: 'optionStats/FETCH_OPTIONSTATS',
  CREATE_OPTIONSTATS: 'optionStats/CREATE_OPTIONSTATS',
  UPDATE_OPTIONSTATS: 'optionStats/UPDATE_OPTIONSTATS',
  DELETE_OPTIONSTATS: 'optionStats/DELETE_OPTIONSTATS',
  RESET: 'optionStats/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOptionStats>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OptionStatsState = Readonly<typeof initialState>;

// Reducer

export default (state: OptionStatsState = initialState, action): OptionStatsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_OPTIONSTATS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_OPTIONSTATS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_OPTIONSTATS):
    case REQUEST(ACTION_TYPES.UPDATE_OPTIONSTATS):
    case REQUEST(ACTION_TYPES.DELETE_OPTIONSTATS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_OPTIONSTATS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_OPTIONSTATS):
    case FAILURE(ACTION_TYPES.CREATE_OPTIONSTATS):
    case FAILURE(ACTION_TYPES.UPDATE_OPTIONSTATS):
    case FAILURE(ACTION_TYPES.DELETE_OPTIONSTATS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_OPTIONSTATS_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_OPTIONSTATS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_OPTIONSTATS):
    case SUCCESS(ACTION_TYPES.UPDATE_OPTIONSTATS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_OPTIONSTATS):
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

const apiUrl = 'api/option-stats';

// Actions

export const getEntities: any = (page, size, sort, instrumentId?, callInTheMoney?, greaterThanOrEqual?, lessThanOrEqual?) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}${
    instrumentId ? `&instrumentId.equals=${instrumentId}` : ''
  }${callInTheMoney ? `&callInTheMoney.equals=${callInTheMoney}` : ''}${
    greaterThanOrEqual ? `&expDate.greaterThanOrEqual=${greaterThanOrEqual}` : ''
  }${lessThanOrEqual ? `&expDate.lessThanOrEqual=${lessThanOrEqual}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_OPTIONSTATS_LIST,
    payload: axios.get<IOptionStats>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOptionStats> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_OPTIONSTATS,
    payload: axios.get<IOptionStats>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOptionStats> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_OPTIONSTATS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IOptionStats> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_OPTIONSTATS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOptionStats> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_OPTIONSTATS,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
