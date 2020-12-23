import { Moment } from 'moment';

export interface IOpenInterest {
  userId?: number;
  date?: Moment;
  isin?: string;
  quantity?: number;
  marginAmount?: number;
}

export const defaultValue: Readonly<IOpenInterest> = {};
