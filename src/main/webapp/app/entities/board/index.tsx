import React from 'react';
import {Switch} from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Board from './board';
import BoardDetail from './board-detail';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BoardDetail} />
      <ErrorBoundaryRoute path={match.url} component={Board} />
    </Switch>
  </>
);

export default Routes;
