import { Moment } from 'moment';
import { IOrder } from 'app/shared/model/order.model';

export interface ISignal {
  id?: number;
  type?: string;
  isin?: string;
  last?: number;
  tradeVolume?: number;
  bidVolume?: number;
  bidPrice?: number;
  askPrice?: number;
  askVolume?: number;
  baseInstrumentLast?: number;
  createdAt?: Moment;
  orders?: IOrder[];
}

export const defaultValue: Readonly<ISignal> = {};
