import { Moment } from 'moment';

export interface IBoard {
  isin?: string;
  date?: Moment;
  last?: number;
  close?: number;
  first?: number;
  low?: number;
  high?: number;
  tradeCount?: number;
  tradeVolume?: number;
  tradeValue?: number;
  askPrice?: number;
  askVolume?: number;
  bidPrice?: number;
  bidVolume?: number;
  individualBuyVolume?: number;
  individualSellVolume?: number;
  legalBuyVolume?: number;
  legalSellVolume?: number;
  referencePrice?: number;
  state?: string;
}

export const defaultValue: Readonly<IBoard> = {};
