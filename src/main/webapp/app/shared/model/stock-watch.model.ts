import { Moment } from 'moment';

export interface IStockWatch {
  id?: number;
  isin?: string;
  last?: number;
  closing?: number;
  first?: number;
  high?: number;
  low?: number;
  min?: number;
  max?: number;
  string?: number;
  tradeValue?: number;
  tradeVolume?: number;
  tradesCount?: number;
  referencePrice?: number;
  dateTime?: Moment;
  openInterest?: number;
  settlementPrice?: number;
}

export const defaultValue: Readonly<IStockWatch> = {};
