import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OptionStats from './option-stats';
import OptionStatsDetail from './option-stats-detail';
import OptionStatsUpdate from './option-stats-update';
import OptionStatsDeleteDialog from './option-stats-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OptionStatsDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OptionStatsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OptionStatsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OptionStatsDetail} />
      <ErrorBoundaryRoute path={match.url} component={OptionStats} />
    </Switch>
  </>
);

export default Routes;
