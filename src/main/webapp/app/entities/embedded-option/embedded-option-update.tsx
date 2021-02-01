import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IInstrument } from 'app/shared/model/instrument.model';
import { getEntities as getInstruments } from 'app/entities/instrument/instrument.reducer';
import { getEntity, updateEntity, createEntity, reset } from './embedded-option.reducer';
import { IEmbeddedOption } from 'app/shared/model/embedded-option.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEmbeddedOptionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmbeddedOptionUpdate = (props: IEmbeddedOptionUpdateProps) => {
  const [underlyingInstrumentId, setUnderlyingInstrumentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { embeddedOptionEntity, instruments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/embedded-option');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getInstruments();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...embeddedOptionEntity,
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
          <h2 id="eTradeApp.embeddedOption.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.embeddedOption.home.createOrEditLabel">Create or edit a EmbeddedOption</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : embeddedOptionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="embedded-option-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="embedded-option-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="embedded-option-name">
                  <Translate contentKey="eTradeApp.embeddedOption.name">Name</Translate>
                </Label>
                <AvField
                  id="embedded-option-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="isinLabel" for="embedded-option-isin">
                  <Translate contentKey="eTradeApp.embeddedOption.isin">Isin</Translate>
                </Label>
                <AvField
                  id="embedded-option-isin"
                  type="text"
                  name="isin"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="expDateLabel" for="embedded-option-expDate">
                  <Translate contentKey="eTradeApp.embeddedOption.expDate">Exp Date</Translate>
                </Label>
                <AvField
                  id="embedded-option-expDate"
                  type="date"
                  className="form-control"
                  name="expDate"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="strikePriceLabel" for="embedded-option-strikePrice">
                  <Translate contentKey="eTradeApp.embeddedOption.strikePrice">Strike Price</Translate>
                </Label>
                <AvField
                  id="embedded-option-strikePrice"
                  type="string"
                  className="form-control"
                  name="strikePrice"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="tseIdLabel" for="embedded-option-tseId">
                  <Translate contentKey="eTradeApp.embeddedOption.tseId">Tse Id</Translate>
                </Label>
                <AvField id="embedded-option-tseId" type="text" name="tseId" />
              </AvGroup>
              <AvGroup>
                <Label for="embedded-option-underlyingInstrument">
                  <Translate contentKey="eTradeApp.embeddedOption.underlyingInstrument">Underlying Instrument</Translate>
                </Label>
                <AvInput
                  id="embedded-option-underlyingInstrument"
                  type="select"
                  className="form-control"
                  name="underlyingInstrument.id"
                  value={isNew ? instruments[0] && instruments[0].isin : embeddedOptionEntity.underlyingInstrument.isin}
                  required
                >
                  {instruments
                    ? instruments.map(otherEntity => (
                        <option value={otherEntity.isin} key={otherEntity.isin}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/embedded-option" replace color="info">
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
  instruments: storeState.instrument.entities,
  embeddedOptionEntity: storeState.embeddedOption.entity,
  loading: storeState.embeddedOption.loading,
  updating: storeState.embeddedOption.updating,
  updateSuccess: storeState.embeddedOption.updateSuccess
});

const mapDispatchToProps = {
  getInstruments,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmbeddedOptionUpdate);
