import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Instrument from './instrument';
import InstrumentDetail from './instrument-detail';
import InstrumentUpdate from './instrument-update';
import InstrumentDeleteDialog from './instrument-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InstrumentDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InstrumentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InstrumentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InstrumentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Instrument} />
    </Switch>
  </>
);

export default Routes;
