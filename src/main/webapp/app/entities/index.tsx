import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Instrument from './instrument';
import Option from './option';
import OptionStats from './option-stats';
import Signal from './signal';
import Order from './order';
import Token from './token';
import Board from './board';
import Portfolio from './portfolio';
import OpenInterest from './open-interest';
import BourseCode from './bourse-code';
import EmbeddedOption from './embedded-option';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}instrument`} component={Instrument}/>
      <ErrorBoundaryRoute path={`${match.url}option`} component={Option} />
      <ErrorBoundaryRoute path={`${match.url}option-stats`} component={OptionStats} />
      <ErrorBoundaryRoute path={`${match.url}signal`} component={Signal} />
      <ErrorBoundaryRoute path={`${match.url}order`} component={Order} />
      <ErrorBoundaryRoute path={`${match.url}token`} component={Token} />
      <ErrorBoundaryRoute path={`${match.url}board`} component={Board} />
      <ErrorBoundaryRoute path={`${match.url}portfolio`} component={Portfolio} />
      <ErrorBoundaryRoute path={`${match.url}open-interest`} component={OpenInterest} />
      <ErrorBoundaryRoute path={`${match.url}bourse-code`} component={BourseCode} />
      <ErrorBoundaryRoute path={`${match.url}embedded-option`} component={EmbeddedOption} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
