import { IOption } from 'app/shared/model/option.model';
import { IStockWatch } from 'app/shared/model/stock-watch.model';
import { IBidAskItem } from 'app/shared/model/bid-ask-item.model';

export interface IOptionStats {
  id?: number;
  callBS?: number;
  callBS30?: number;
  callBS60?: number;
  callBS90?: number;
  putBS?: number;
  putBS30?: number;
  putBS60?: number;
  putBS90?: number;
  callEffectivePrice?: number;
  putEffectivePrice?: number;
  callFinalPrice?: number;

  option?: IOption;

  callStockWatch?: IStockWatch;
  callBidAsk?: IBidAskItem;

  putStockWatch?: IStockWatch;
  putBidAsk?: IBidAskItem;

  baseStockWatch?: IStockWatch;
  baseBidAsk?: IBidAskItem;
}

export const defaultValue: Readonly<IOptionStats> = {};
