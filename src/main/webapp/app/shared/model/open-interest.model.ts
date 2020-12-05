import { Moment } from 'moment';

export interface IOpenInterest {
  userId?: string;
  date?: Moment;
  isin?: string;
  quantity?: number;
  lastPrice?: number;
  closePrice?: number;
  marginAmount?: number;
}

export const defaultValue: Readonly<IOpenInterest> = {};
