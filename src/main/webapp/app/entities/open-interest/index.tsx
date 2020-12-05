import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OpenInterest from './open-interest';
import OpenInterestDetail from './open-interest-detail';
import OpenInterestUpdate from './open-interest-update';
import OpenInterestDeleteDialog from './open-interest-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OpenInterestDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OpenInterestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OpenInterestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OpenInterestDetail} />
      <ErrorBoundaryRoute path={match.url} component={OpenInterest} />
    </Switch>
  </>
);

export default Routes;
