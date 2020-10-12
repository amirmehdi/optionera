import { Moment } from 'moment';
import { Broker } from 'app/shared/model/enumerations/broker.model';

export interface IToken {
  id?: number;
  token?: string;
  broker?: Broker;
  createdAt?: Moment;
}

export const defaultValue: Readonly<IToken> = {};
