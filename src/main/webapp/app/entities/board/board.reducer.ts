import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction } from 'react-jhipster';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, IBoard } from 'app/shared/model/board.model';

export const ACTION_TYPES = {
  FETCH_BOARD_LIST: 'board/FETCH_BOARD_LIST',
  FETCH_BOARD: 'board/FETCH_BOARD',
  CREATE_BOARD: 'board/CREATE_BOARD',
  UPDATE_BOARD: 'board/UPDATE_BOARD',
  DELETE_BOARD: 'board/DELETE_BOARD',
  RESET: 'board/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBoard>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type BoardState = Readonly<typeof initialState>;

// Reducer

export default (state: BoardState = initialState, action): BoardState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BOARD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BOARD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BOARD):
    case REQUEST(ACTION_TYPES.UPDATE_BOARD):
    case REQUEST(ACTION_TYPES.DELETE_BOARD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BOARD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BOARD):
    case FAILURE(ACTION_TYPES.CREATE_BOARD):
    case FAILURE(ACTION_TYPES.UPDATE_BOARD):
    case FAILURE(ACTION_TYPES.DELETE_BOARD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOARD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOARD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BOARD):
    case SUCCESS(ACTION_TYPES.UPDATE_BOARD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BOARD):
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

const apiUrl = 'api/boards';

// Actions

export const getEntities: ICrudGetAllAction<IBoard> = (page, size, sort) => {
  if (sort === 'id,asc') {
    sort = 'isin,asc';
  }
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BOARD_LIST,
    payload: axios.get<IBoard>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IBoard> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BOARD,
    payload: axios.get<IBoard>(requestUrl)
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
