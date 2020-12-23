import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BourseCode from './bourse-code';
import BourseCodeDetail from './bourse-code-detail';
import BourseCodeUpdate from './bourse-code-update';
import BourseCodeDeleteDialog from './bourse-code-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BourseCodeDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BourseCodeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BourseCodeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BourseCodeDetail} />
      <ErrorBoundaryRoute path={match.url} component={BourseCode} />
    </Switch>
  </>
);

export default Routes;
