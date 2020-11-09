import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Label, Row} from 'reactstrap';
import {AvFeedback, AvField, AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';
import {Translate, translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {IRootState} from 'app/shared/reducers';
import {getEntities as getInstruments} from 'app/entities/instrument/instrument.reducer';
import {createEntity, getEntity, reset, updateEntity} from './option.reducer';

export interface IOptionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OptionUpdate = (props: IOptionUpdateProps) => {
  const [instrumentId, setInstrumentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { optionEntity, instruments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/option');
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
        ...optionEntity,
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
          <h2 id="eTradeApp.option.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.option.home.createOrEditLabel">Create or edit a Option</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : optionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="option-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="option-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="option-name">
                  <Translate contentKey="eTradeApp.option.name">Name</Translate>
                </Label>
                <AvField
                  id="option-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="callIsinLabel" for="option-callIsin">
                  <Translate contentKey="eTradeApp.option.callIsin">Call Isin</Translate>
                </Label>
                <AvField
                  id="option-callIsin"
                  type="text"
                  name="callIsin"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="putIsinLabel" for="option-putIsin">
                  <Translate contentKey="eTradeApp.option.putIsin">Put Isin</Translate>
                </Label>
                <AvField
                  id="option-putIsin"
                  type="text"
                  name="putIsin"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="expDateLabel" for="option-expDate">
                  <Translate contentKey="eTradeApp.option.expDate">Exp Date</Translate>
                </Label>
                <AvField
                  id="option-expDate"
                  type="date"
                  className="form-control"
                  name="expDate"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="strikePriceLabel" for="option-strikePrice">
                  <Translate contentKey="eTradeApp.option.strikePrice">Strike Price</Translate>
                </Label>
                <AvField
                  id="option-strikePrice"
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
                <Label id="contractSizeLabel" for="option-contractSize">
                  <Translate contentKey="eTradeApp.option.contractSize">Contract Size</Translate>
                </Label>
                <AvField id="option-contractSize" type="string" className="form-control" name="contractSize" />
              </AvGroup>
              <AvGroup check>
                <Label id="callInTheMoneyLabel">
                  <AvInput id="option-callInTheMoney" type="checkbox" className="form-check-input" name="callInTheMoney" />
                  <Translate contentKey="eTradeApp.option.callInTheMoney">Call In The Money</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="callBreakEvenLabel" for="option-callBreakEven">
                  <Translate contentKey="eTradeApp.option.callBreakEven">Call Break Even</Translate>
                </Label>
                <AvField
                  id="option-callBreakEven"
                  type="string"
                  className="form-control"
                  name="callBreakEven"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="putBreakEvenLabel" for="option-putBreakEven">
                  <Translate contentKey="eTradeApp.option.putBreakEven">Put Break Even</Translate>
                </Label>
                <AvField
                  id="option-putBreakEven"
                  type="string"
                  className="form-control"
                  name="putBreakEven"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="callAskToBSLabel" for="option-callAskToBS">
                  <Translate contentKey="eTradeApp.option.callAskToBS">Call Ask To BS</Translate>
                </Label>
                <AvField
                  id="option-callAskToBS"
                  type="string"
                  className="form-control"
                  name="callAskToBS"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="putAskToBSLabel" for="option-putAskToBS">
                  <Translate contentKey="eTradeApp.option.putAskToBS">Put Ask To BS</Translate>
                </Label>
                <AvField
                  id="option-putAskToBS"
                  type="string"
                  className="form-control"
                  name="putAskToBS"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="callLeverageLabel" for="option-callLeverage">
                  <Translate contentKey="eTradeApp.option.callLeverage">Call Leverage</Translate>
                </Label>
                <AvField
                  id="option-callLeverage"
                  type="string"
                  className="form-control"
                  name="callLeverage"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="putLeverageLabel" for="option-putLeverage">
                  <Translate contentKey="eTradeApp.option.putLeverage">Put Leverage</Translate>
                </Label>
                <AvField
                  id="option-putLeverage"
                  type="string"
                  className="form-control"
                  name="putLeverage"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="callHedgeLabel" for="option-callHedge">
                  <Translate contentKey="eTradeApp.option.callHedge">Call Hedge</Translate>
                </Label>
                <AvField
                  id="option-callHedge"
                  type="string"
                  className="form-control"
                  name="callHedge"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="callIndifferenceLabel" for="option-callIndifference">
                  <Translate contentKey="eTradeApp.option.callIndifference">Call Indifference</Translate>
                </Label>
                <AvField
                  id="option-callIndifference"
                  type="string"
                  className="form-control"
                  name="callIndifference"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="callGainLabel" for="option-callGain">
                  <Translate contentKey="eTradeApp.option.callGain">Call Gain</Translate>
                </Label>
                <AvField
                  id="option-callGain"
                  type="string"
                  className="form-control"
                  name="callGain"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="callGainMonthlyLabel" for="option-callGainMonthly">
                  <Translate contentKey="eTradeApp.option.callGainMonthly">Call Gain Monthly</Translate>
                </Label>
                <AvField
                  id="option-callGainMonthly"
                  type="string"
                  className="form-control"
                  name="callGainMonthly"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="callMarginLabel" for="option-callMargin">
                  <Translate contentKey="eTradeApp.option.callMargin">Call Margin</Translate>
                </Label>
                <AvField
                  id="option-callMargin"
                  type="string"
                  className="form-control"
                  name="callMargin"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="putMarginLabel" for="option-putMargin">
                  <Translate contentKey="eTradeApp.option.putMargin">Put Margin</Translate>
                </Label>
                <AvField
                  id="option-putMargin"
                  type="string"
                  className="form-control"
                  name="putMargin"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="option-instrument">
                  <Translate contentKey="eTradeApp.option.instrument">Instrument</Translate>
                </Label>
                <AvInput
                  id="option-instrument"
                  type="select"
                  className="form-control"
                  name="instrument.id"
                  value={isNew ? instruments[0] && instruments[0].isin : optionEntity.instrument.isin}
                  required
                >
                  {instruments
                    ? instruments.map(otherEntity => (
                        <option value={otherEntity.isin} key={otherEntity.isin}>
                          {otherEntity.isin}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/option" replace color="info">
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
  optionEntity: storeState.option.entity,
  loading: storeState.option.loading,
  updating: storeState.option.updating,
  updateSuccess: storeState.option.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(OptionUpdate);
