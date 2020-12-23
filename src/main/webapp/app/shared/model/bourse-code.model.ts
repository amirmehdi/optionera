import { IToken } from 'app/shared/model/token.model';
import { Broker } from 'app/shared/model/enumerations/broker.model';

export interface IBourseCode {
  id?: number;
  broker?: Broker;
  name?: string;
  code?: string;
  username?: string;
  password?: string;
  buyingPower?: number;
  blocked?: number;
  remain?: number;
  credit?: number;
  token?: IToken;
}

export const defaultValue: Readonly<IBourseCode> = {};
