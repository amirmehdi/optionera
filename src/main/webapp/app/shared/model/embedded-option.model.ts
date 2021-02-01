import { Moment } from 'moment';
import { IInstrument } from 'app/shared/model/instrument.model';

export interface IEmbeddedOption {
  id?: number;
  name?: string;
  isin?: string;
  expDate?: Moment;
  strikePrice?: number;
  tseId?: string;
  underlyingInstrument?: IInstrument;
}

export const defaultValue: Readonly<IEmbeddedOption> = {};
