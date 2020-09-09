import { Moment } from 'moment';
import { IOption } from 'app/shared/model/option.model';

export interface IInstrument {
  isin?: string;
  name?: string;
  tseId?: string;
  volatility30?: number;
  volatility60?: number;
  volatility90?: number;
  updatedAt?: Moment;
  options?: IOption[];
}

export const defaultValue: Readonly<IInstrument> = {};
