import { Moment } from 'moment';
import { IOrder } from 'app/shared/model/order.model';
import { AlgorithmSide } from 'app/shared/model/enumerations/algorithm-side.model';
import { AlgorithmState } from 'app/shared/model/enumerations/algorithm-state.model';

export interface IAlgorithm {
  id?: number;
  type?: string;
  side?: AlgorithmSide;
  state?: AlgorithmState;
  input?: string;
  tradeVolumeLimit?: number;
  tradeValueLimit?: number;
  createdAt?: Moment;
  updatedAt?: Moment;
  isins?: string;
  orders?: IOrder[];
}

export const defaultValue: Readonly<IAlgorithm> = {};
