import React from 'react';
import {Switch} from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Instrument from './instrument';
import Option from './option';
import OptionStats from './option-stats';
import Signal from './signal';
import Order from './order';
import Token from './token';
import Board from './board';
import Algorithm from './algorithm';
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
      <ErrorBoundaryRoute path={`${match.url}algorithm`} component={Algorithm} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
