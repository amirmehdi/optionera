import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './instrument-history.reducer';
import { IInstrumentHistory } from 'app/shared/model/instrument-history.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInstrumentHistoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InstrumentHistoryUpdate = (props: IInstrumentHistoryUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { instrumentHistoryEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/instrument-history' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...instrumentHistoryEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eTradeApp.instrumentHistory.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.instrumentHistory.home.createOrEditLabel">Create or edit a InstrumentHistory</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : instrumentHistoryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="instrument-history-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="instrument-history-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="isinLabel" for="instrument-history-isin">
                  <Translate contentKey="eTradeApp.instrumentHistory.isin">Isin</Translate>
                </Label>
                <AvField
                  id="instrument-history-isin"
                  type="text"
                  name="isin"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="closingLabel" for="instrument-history-closing">
                  <Translate contentKey="eTradeApp.instrumentHistory.closing">Closing</Translate>
                </Label>
                <AvField id="instrument-history-closing" type="string" className="form-control" name="closing" />
              </AvGroup>
              <AvGroup>
                <Label id="updatedAtLabel" for="instrument-history-updatedAt">
                  <Translate contentKey="eTradeApp.instrumentHistory.updatedAt">Updated At</Translate>
                </Label>
                <AvField id="instrument-history-updatedAt" type="date" className="form-control" name="updatedAt" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/instrument-history" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  instrumentHistoryEntity: storeState.instrumentHistory.entity,
  loading: storeState.instrumentHistory.loading,
  updating: storeState.instrumentHistory.updating,
  updateSuccess: storeState.instrumentHistory.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InstrumentHistoryUpdate);
