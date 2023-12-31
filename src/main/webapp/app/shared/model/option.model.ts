import { Moment } from 'moment';
import { IInstrument } from 'app/shared/model/instrument.model';

export interface IOption {
  id?: number;
  name?: string;
  callIsin?: string;
  putIsin?: string;
  callTseId?: string;
  putTseId?: string;
  expDate?: Moment;
  strikePrice?: number;
  contractSize?: number;
  callInTheMoney?: boolean;
  callBreakEven?: number;
  putBreakEven?: number;
  callAskToBS?: number;
  putAskToBS?: number;
  callLeverage?: number;
  putLeverage?: number;
  callHedge?: number;
  callIndifference?: number;
  callGain?: number;
  callGainMonthly?: number;
  callMargin?: number;
  putMargin?: number;
  callTradeVolume?: number;
  putTradeVolume?: number;
  instrument?: IInstrument;
}

export const defaultValue: Readonly<IOption> = {
  callInTheMoney: false
};
