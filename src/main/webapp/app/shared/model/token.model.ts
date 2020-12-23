import { Moment } from 'moment';
import { IBourseCode } from 'app/shared/model/bourse-code.model';
import { Broker } from 'app/shared/model/enumerations/broker.model';

export interface IToken {
  id?: number;
  token?: string;
  broker?: Broker;
  createdAt?: Moment;
  bourseCode?: IBourseCode;
}

export const defaultValue: Readonly<IToken> = {};
