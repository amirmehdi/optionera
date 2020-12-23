import { Moment } from 'moment';

export interface IPortfolio {
  userId?: number;
  date?: Moment;
  isin?: string;
  quantity?: number;
  avgPrice?: number;
}

export const defaultValue: Readonly<IPortfolio> = {};
