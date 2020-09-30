export interface IBidAskItem {
  bidNumber?: number;
  bidPrice?: number;
  bidQuantity?: number;
  askNumber?: number;
  askPrice?: number;
  askQuantity?: number;
}

export const defaultValue: Readonly<IBidAskItem> = {};
