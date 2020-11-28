import { ISignal } from 'app/shared/model/signal.model';
import { Validity } from 'app/shared/model/enumerations/validity.model';
import { Side } from 'app/shared/model/enumerations/side.model';
import { Broker } from 'app/shared/model/enumerations/broker.model';
import { State } from 'app/shared/model/enumerations/state.model';

export interface IOrder {
  id?: number;
  isin?: string;
  price?: number;
  quantity?: number;
  validity?: Validity;
  side?: Side;
  broker?: Broker;
  omsId?: string;
  state?: State;
  executed?: number;
  signal?: ISignal;
}

export const defaultValue: Readonly<IOrder> = {};
