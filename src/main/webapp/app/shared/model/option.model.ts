import { Moment } from 'moment';
import { IInstrument } from 'app/shared/model/instrument.model';

export interface IOption {
  id?: number;
  name?: string;
  callIsin?: string;
  putIsin?: string;
  expDate?: Moment;
  strikePrice?: number;
  contractSize?: number;
  instrument?: IInstrument;
}

export const defaultValue: Readonly<IOption> = {};
