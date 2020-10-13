import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Label, Row} from 'reactstrap';
import {AvField, AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';
import {Translate, translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {IRootState} from 'app/shared/reducers';

import {createEntity, getEntity, reset, updateEntity} from './signal.reducer';
import {convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime} from 'app/shared/util/date-utils';

export interface ISignalUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}

export const SignalUpdate = (props: ISignalUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const {signalEntity, loading, updating} = props;

  const handleClose = () => {
    props.history.push('/signal');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdAt = convertDateTimeToServer(values.createdAt);

    if (errors.length === 0) {
      const entity = {
        ...signalEntity,
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
          <h2 id="eTradeApp.signal.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.signal.home.createOrEditLabel">Create or edit a Signal</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : signalEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="signal-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="signal-id" type="text" className="form-control" name="id" required readOnly/>
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="typeLabel" for="signal-type">
                  <Translate contentKey="eTradeApp.signal.type">Type</Translate>
                </Label>
                <AvField
                  id="signal-type"
                  type="text"
                  name="type"
                  validate={{
                    required: {value: true, errorMessage: translate('entity.validation.required')}
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="isinLabel" for="signal-isin">
                  <Translate contentKey="eTradeApp.signal.isin">Isin</Translate>
                </Label>
                <AvField
                  id="signal-isin"
                  type="text"
                  name="isin"
                  validate={{
                    required: {value: true, errorMessage: translate('entity.validation.required')}
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lastLabel" for="signal-last">
                  <Translate contentKey="eTradeApp.signal.last">Last</Translate>
                </Label>
                <AvField id="signal-last" type="string" className="form-control" name="last"/>
              </AvGroup>
              <AvGroup>
                <Label id="tradeVolumeLabel" for="signal-tradeVolume">
                  <Translate contentKey="eTradeApp.signal.tradeVolume">Trade Volume</Translate>
                </Label>
                <AvField id="signal-tradeVolume" type="string" className="form-control" name="tradeVolume"/>
              </AvGroup>
              <AvGroup>
                <Label id="bidVolumeLabel" for="signal-bidVolume">
                  <Translate contentKey="eTradeApp.signal.bidVolume">Bid Volume</Translate>
                </Label>
                <AvField id="signal-bidVolume" type="string" className="form-control" name="bidVolume"/>
              </AvGroup>
              <AvGroup>
                <Label id="bidPriceLabel" for="signal-bidPrice">
                  <Translate contentKey="eTradeApp.signal.bidPrice">Bid Price</Translate>
                </Label>
                <AvField id="signal-bidPrice" type="string" className="form-control" name="bidPrice"/>
              </AvGroup>
              <AvGroup>
                <Label id="askPriceLabel" for="signal-askPrice">
                  <Translate contentKey="eTradeApp.signal.askPrice">Ask Price</Translate>
                </Label>
                <AvField id="signal-askPrice" type="string" className="form-control" name="askPrice"/>
              </AvGroup>
              <AvGroup>
                <Label id="askVolumeLabel" for="signal-askVolume">
                  <Translate contentKey="eTradeApp.signal.askVolume">Ask Volume</Translate>
                </Label>
                <AvField id="signal-askVolume" type="string" className="form-control" name="askVolume"/>
              </AvGroup>
              <AvGroup>
                <Label id="baseInstrumentLastLabel" for="signal-baseInstrumentLast">
                  <Translate contentKey="eTradeApp.signal.baseInstrumentLast">Base Instrument Last</Translate>
                </Label>
                <AvField id="signal-baseInstrumentLast" type="string" className="form-control"
                         name="baseInstrumentLast"/>
              </AvGroup>
              <AvGroup>
                <Label id="createdAtLabel" for="signal-createdAt">
                  <Translate contentKey="eTradeApp.signal.createdAt">Created At</Translate>
                </Label>
                <AvInput
                  id="signal-createdAt"
                  type="datetime-local"
                  className="form-control"
                  name="createdAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.signalEntity.createdAt)}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/signal" replace color="info">
                <FontAwesomeIcon icon="arrow-left"/>
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save"/>
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
  signalEntity: storeState.signal.entity,
  loading: storeState.signal.loading,
  updating: storeState.signal.updating,
  updateSuccess: storeState.signal.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SignalUpdate);
