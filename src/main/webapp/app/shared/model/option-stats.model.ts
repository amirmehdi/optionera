import { IOption } from 'app/shared/model/option.model';
import { IStockWatch } from 'app/shared/model/stock-watch.model';
import { IBidAskItem } from 'app/shared/model/bid-ask-item.model';

export interface IOptionStats {
  id?: number;
  blackScholes30?: number;
  blackScholes60?: number;
  blackScholes90?: number;
  callEffectivePrice?: number;
  putEffectivePrice?: number;

  option?: IOption;

  callStockWatch?: IStockWatch;
  callBidAsk?: IBidAskItem;

  putStockWatch?: IStockWatch;
  putBidAsk?: IBidAskItem;

  baseStockWatch?: IStockWatch;
  baseBidAsk?: IBidAskItem;
}

export const defaultValue: Readonly<IOptionStats> = {};
