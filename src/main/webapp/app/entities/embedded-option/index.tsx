import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EmbeddedOption from './embedded-option';
import EmbeddedOptionDetail from './embedded-option-detail';
import EmbeddedOptionUpdate from './embedded-option-update';
import EmbeddedOptionDeleteDialog from './embedded-option-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EmbeddedOptionDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmbeddedOptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmbeddedOptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmbeddedOptionDetail} />
      <ErrorBoundaryRoute path={match.url} component={EmbeddedOption} />
    </Switch>
  </>
);

export default Routes;
