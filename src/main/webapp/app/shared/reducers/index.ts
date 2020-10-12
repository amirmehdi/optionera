import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import instrument, {InstrumentState} from 'app/entities/instrument/instrument.reducer';
// prettier-ignore
import option, {OptionState} from 'app/entities/option/option.reducer';
// prettier-ignore
// prettier-ignore
import optionStats, {OptionStatsState} from 'app/entities/option-stats/option-stats.reducer';
// prettier-ignore
import signal, {SignalState} from 'app/entities/signal/signal.reducer';
// prettier-ignore
import order, {OrderState} from 'app/entities/order/order.reducer';
// prettier-ignore
import token, {TokenState} from 'app/entities/token/token.reducer';

/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly instrument: InstrumentState;
  readonly option: OptionState;
  readonly optionStats: OptionStatsState;
  readonly signal: SignalState;
  readonly order: OrderState;
  readonly token: TokenState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  instrument,
  option,
  optionStats,
  signal,
  order,
  token,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
