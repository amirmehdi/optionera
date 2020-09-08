import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Instrument from './instrument';
import Option from './option';
import InstrumentHistory from './instrument-history';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}instrument`} component={Instrument} />
      <ErrorBoundaryRoute path={`${match.url}option`} component={Option} />
      <ErrorBoundaryRoute path={`${match.url}instrument-history`} component={InstrumentHistory} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
