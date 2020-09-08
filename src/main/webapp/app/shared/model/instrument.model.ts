import { Moment } from 'moment';
import { IOption } from 'app/shared/model/option.model';

export interface IInstrument {
  id?: number;
  name?: string;
  isin?: string;
  tseId?: string;
  volatility30?: number;
  volatility60?: number;
  volatility90?: number;
  updatedAt?: Moment;
  options?: IOption[];
}

export const defaultValue: Readonly<IInstrument> = {};
