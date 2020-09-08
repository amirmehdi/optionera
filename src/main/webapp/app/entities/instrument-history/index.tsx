import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import InstrumentHistory from './instrument-history';
import InstrumentHistoryDetail from './instrument-history-detail';
import InstrumentHistoryUpdate from './instrument-history-update';
import InstrumentHistoryDeleteDialog from './instrument-history-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InstrumentHistoryDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InstrumentHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InstrumentHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InstrumentHistoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={InstrumentHistory} />
    </Switch>
  </>
);

export default Routes;
