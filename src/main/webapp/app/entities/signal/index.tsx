import React from 'react';
import {Switch} from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Signal from './signal';
import SignalDetail from './signal-detail';
import SignalUpdate from './signal-update';
import SignalDeleteDialog from './signal-delete-dialog';

const Routes = ({match}) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SignalDeleteDialog}/>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SignalUpdate}/>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SignalUpdate}/>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SignalDetail}/>
      <ErrorBoundaryRoute path={match.url} component={Signal}/>
    </Switch>
  </>
);

export default Routes;
