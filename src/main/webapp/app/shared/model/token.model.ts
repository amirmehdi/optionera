import { Moment } from 'moment';
import { IBourseCode } from 'app/shared/model/bourse-code.model';

export interface IToken {
  id?: number;
  token?: string;
  createdAt?: Moment;
  securityFields?: string;
  bourseCode?: IBourseCode;
}

export const defaultValue: Readonly<IToken> = {};
