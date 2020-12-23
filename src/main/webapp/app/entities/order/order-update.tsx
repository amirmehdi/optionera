import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISignal } from 'app/shared/model/signal.model';
import { getEntities as getSignals } from 'app/entities/signal/signal.reducer';
import { IBourseCode } from 'app/shared/model/bourse-code.model';
import { getEntities as getBourseCodes } from 'app/entities/bourse-code/bourse-code.reducer';
import { getEntity, updateEntity, createEntity, reset } from './order.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderUpdate = (props: IOrderUpdateProps) => {
  const [signalId, setSignalId] = useState('0');
  const [bourseCodeId, setBourseCodeId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { orderEntity, signals, bourseCodes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/order');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getSignals();
    props.getBourseCodes();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...orderEntity,
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
          <h2 id="eTradeApp.order.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.order.home.createOrEditLabel">Create or edit a Order</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : orderEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="order-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="order-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="isinLabel" for="order-isin">
                  <Translate contentKey="eTradeApp.order.isin">Isin</Translate>
                </Label>
                <AvField
                  id="order-isin"
                  type="text"
                  name="isin"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="order-price">
                  <Translate contentKey="eTradeApp.order.price">Price</Translate>
                </Label>
                <AvField
                  id="order-price"
                  type="string"
                  className="form-control"
                  name="price"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLabel" for="order-quantity">
                  <Translate contentKey="eTradeApp.order.quantity">Quantity</Translate>
                </Label>
                <AvField
                  id="order-quantity"
                  type="string"
                  className="form-control"
                  name="quantity"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="validityLabel" for="order-validity">
                  <Translate contentKey="eTradeApp.order.validity">Validity</Translate>
                </Label>
                <AvInput
                  id="order-validity"
                  type="select"
                  className="form-control"
                  name="validity"
                  value={(!isNew && orderEntity.validity) || 'DAY'}
                >
                  <option value="DAY">{translate('eTradeApp.Validity.DAY')}</option>
                  <option value="FILL_AND_KILL">{translate('eTradeApp.Validity.FILL_AND_KILL')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="sideLabel" for="order-side">
                  <Translate contentKey="eTradeApp.order.side">Side</Translate>
                </Label>
                <AvInput id="order-side" type="select" className="form-control" name="side" value={(!isNew && orderEntity.side) || 'BUY'}>
                  <option value="BUY">{translate('eTradeApp.Side.BUY')}</option>
                  <option value="SELL">{translate('eTradeApp.Side.SELL')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="brokerLabel" for="order-broker">
                  <Translate contentKey="eTradeApp.order.broker">Broker</Translate>
                </Label>
                <AvInput
                  id="order-broker"
                  type="select"
                  className="form-control"
                  name="broker"
                  value={(!isNew && orderEntity.broker) || 'REFAH'}
                >
                  <option value="REFAH">{translate('eTradeApp.Broker.REFAH')}</option>
                  <option value="FIROOZE_ASIA">{translate('eTradeApp.Broker.FIROOZE_ASIA')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="omsIdLabel" for="order-omsId">
                  <Translate contentKey="eTradeApp.order.omsId">Oms Id</Translate>
                </Label>
                <AvField id="order-omsId" type="text" name="omsId" />
              </AvGroup>
              <AvGroup>
                <Label id="stateLabel" for="order-state">
                  <Translate contentKey="eTradeApp.order.state">State</Translate>
                </Label>
                <AvInput
                  id="order-state"
                  type="select"
                  className="form-control"
                  name="state"
                  value={(!isNew && orderEntity.state) || 'NONE'}
                >
                  <option value="NONE">{translate('eTradeApp.State.NONE')}</option>
                  <option value="ACTIVE">{translate('eTradeApp.State.ACTIVE')}</option>
                  <option value="EXECUTED">{translate('eTradeApp.State.EXECUTED')}</option>
                  <option value="CANCELLED">{translate('eTradeApp.State.CANCELLED')}</option>
                  <option value="ERROR">{translate('eTradeApp.State.ERROR')}</option>
                  <option value="HEADLINE">{translate('eTradeApp.State.HEADLINE')}</option>
                  <option value="PARTIALLY_EXECUTED">{translate('eTradeApp.State.PARTIALLY_EXECUTED')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="executedLabel" for="order-executed">
                  <Translate contentKey="eTradeApp.order.executed">Executed</Translate>
                </Label>
                <AvField id="order-executed" type="string" className="form-control" name="executed" />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="order-description">
                  <Translate contentKey="eTradeApp.order.description">Description</Translate>
                </Label>
                <AvField id="order-description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label for="order-signal">
                  <Translate contentKey="eTradeApp.order.signal">Signal</Translate>
                </Label>
                <AvInput id="order-signal" type="select" className="form-control" name="signal.id">
                  <option value="" key="0" />
                  {signals
                    ? signals.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="order-bourseCode">
                  <Translate contentKey="eTradeApp.order.bourseCode">Bourse Code</Translate>
                </Label>
                <AvInput
                  id="order-bourseCode"
                  type="select"
                  className="form-control"
                  name="bourseCode.id"
                  value={isNew ? bourseCodes[0] && bourseCodes[0].id : orderEntity.bourseCode.id}
                  required
                >
                  {bourseCodes
                    ? bourseCodes.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.username}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/order" replace color="info">
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
  signals: storeState.signal.entities,
  bourseCodes: storeState.bourseCode.entities,
  orderEntity: storeState.order.entity,
  loading: storeState.order.loading,
  updating: storeState.order.updating,
  updateSuccess: storeState.order.updateSuccess
});

const mapDispatchToProps = {
  getSignals,
  getBourseCodes,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderUpdate);
