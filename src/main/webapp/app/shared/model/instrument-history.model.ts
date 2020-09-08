import { Moment } from 'moment';

export interface IInstrumentHistory {
  id?: number;
  isin?: string;
  closing?: number;
  updatedAt?: Moment;
}

export const defaultValue: Readonly<IInstrumentHistory> = {};
