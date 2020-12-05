import { Moment } from 'moment';

export interface IPortfolio {
  userId?: string;
  date?: Moment;
  isin?: string;
  quantity?: number;
  avgPrice?: number;
  lastPrice?: number;
  closePrice?: number;
}

export const defaultValue: Readonly<IPortfolio> = {};
