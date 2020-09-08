import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './instrument.reducer';
import { IInstrument } from 'app/shared/model/instrument.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInstrumentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InstrumentUpdate = (props: IInstrumentUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { instrumentEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/instrument' + props.location.search);
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
        ...instrumentEntity,
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
          <h2 id="eTradeApp.instrument.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.instrument.home.createOrEditLabel">Create or edit a Instrument</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : instrumentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="instrument-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="instrument-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="instrument-name">
                  <Translate contentKey="eTradeApp.instrument.name">Name</Translate>
                </Label>
                <AvField
                  id="instrument-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="isinLabel" for="instrument-isin">
                  <Translate contentKey="eTradeApp.instrument.isin">Isin</Translate>
                </Label>
                <AvField
                  id="instrument-isin"
                  type="text"
                  name="isin"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="tseIdLabel" for="instrument-tseId">
                  <Translate contentKey="eTradeApp.instrument.tseId">Tse Id</Translate>
                </Label>
                <AvField id="instrument-tseId" type="text" name="tseId" />
              </AvGroup>
              <AvGroup>
                <Label id="volatility30Label" for="instrument-volatility30">
                  <Translate contentKey="eTradeApp.instrument.volatility30">Volatility 30</Translate>
                </Label>
                <AvField id="instrument-volatility30" type="string" className="form-control" name="volatility30" />
              </AvGroup>
              <AvGroup>
                <Label id="volatility60Label" for="instrument-volatility60">
                  <Translate contentKey="eTradeApp.instrument.volatility60">Volatility 60</Translate>
                </Label>
                <AvField id="instrument-volatility60" type="string" className="form-control" name="volatility60" />
              </AvGroup>
              <AvGroup>
                <Label id="volatility90Label" for="instrument-volatility90">
                  <Translate contentKey="eTradeApp.instrument.volatility90">Volatility 90</Translate>
                </Label>
                <AvField id="instrument-volatility90" type="string" className="form-control" name="volatility90" />
              </AvGroup>
              <AvGroup>
                <Label id="updatedAtLabel" for="instrument-updatedAt">
                  <Translate contentKey="eTradeApp.instrument.updatedAt">Updated At</Translate>
                </Label>
                <AvField id="instrument-updatedAt" type="date" className="form-control" name="updatedAt" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/instrument" replace color="info">
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
  instrumentEntity: storeState.instrument.entity,
  loading: storeState.instrument.loading,
  updating: storeState.instrument.updating,
  updateSuccess: storeState.instrument.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InstrumentUpdate);
