import { Moment } from 'moment';

export interface IInstrument {
  isin?: string;
  name?: string;
  tseId?: string;
  volatility30?: number;
  volatility60?: number;
  volatility90?: number;
  updatedAt?: Moment;
}

export const defaultValue: Readonly<IInstrument> = {};
