import { IOption } from 'app/shared/model/option.model';
import { IStockWatch } from 'app/shared/model/stock-watch.model';

export interface IOptionStats {
  id?: number;
  callLast?: number;
  callClose?: number;
  callReferencePrice?: number;
  callSettlementPrice?: number;
  callBsPrice?: number;
  callTradeVolume?: number;
  callTradeCount?: number;
  callTradeValue?: number;
  callOpenInterest?: number;
  callBidPrice?: number;
  callAskPrice?: number;
  callBidVolume?: number;
  callAskVolume?: number;
  putLast?: number;
  putClose?: number;
  putReferencePrice?: number;
  putSettlementPrice?: number;
  putBsPrice?: number;
  putTradeVolume?: number;
  putTradeCount?: number;
  putTradeValue?: number;
  putOpenInterest?: number;
  putBidPrice?: number;
  putAskPrice?: number;
  putBidVolume?: number;
  putAskVolume?: number;
  blackScholes30?: number;
  blackScholes60?: number;
  blackScholes90?: number;
  callAskPriceToBS?: number;
  callEffectivePrice?: number;
  callBreakEven?: number;
  putAskPriceToBS?: number;
  putEffectivePrice?: number;
  putBreakEven?: number;
  option?: IOption;
  baseStockWatch?: IStockWatch;
}

export const defaultValue: Readonly<IOptionStats> = {};
