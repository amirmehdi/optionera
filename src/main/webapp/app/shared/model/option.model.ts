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
  callInTheMoney?: boolean;
  callBreakEven?: number;
  putBreakEven?: number;
  callAskToBS?: number;
  putAskToBS?: number;
  instrument?: IInstrument;
}

export const defaultValue: Readonly<IOption> = {
  callInTheMoney: false
};
