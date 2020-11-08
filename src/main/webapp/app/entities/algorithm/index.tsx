import React from 'react';
import {Switch} from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Algorithm from './algorithm';
import AlgorithmDetail from './algorithm-detail';
import AlgorithmUpdate from './algorithm-update';
import AlgorithmDeleteDialog from './algorithm-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AlgorithmDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AlgorithmUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AlgorithmUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AlgorithmDetail} />
      <ErrorBoundaryRoute path={match.url} component={Algorithm} />
    </Switch>
  </>
);

export default Routes;
