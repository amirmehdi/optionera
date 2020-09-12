import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOption } from 'app/shared/model/option.model';
import { getEntities as getOptions } from 'app/entities/option/option.reducer';
import { getEntity, updateEntity, createEntity, reset } from './option-stats.reducer';
import { IOptionStats } from 'app/shared/model/option-stats.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOptionStatsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OptionStatsUpdate = (props: IOptionStatsUpdateProps) => {
  const [optionId, setOptionId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { optionStatsEntity, options, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/option-stats');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getOptions();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...optionStatsEntity,
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
          <h2 id="eTradeApp.optionStats.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.optionStats.home.createOrEditLabel">Create or edit a OptionStats</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : optionStatsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="option-stats-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="option-stats-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="callLastLabel" for="option-stats-callLast">
                  <Translate contentKey="eTradeApp.optionStats.callLast">Call Last</Translate>
                </Label>
                <AvField id="option-stats-callLast" type="string" className="form-control" name="callLast" />
              </AvGroup>
              <AvGroup>
                <Label id="callCloseLabel" for="option-stats-callClose">
                  <Translate contentKey="eTradeApp.optionStats.callClose">Call Close</Translate>
                </Label>
                <AvField id="option-stats-callClose" type="string" className="form-control" name="callClose" />
              </AvGroup>
              <AvGroup>
                <Label id="callReferencePriceLabel" for="option-stats-callReferencePrice">
                  <Translate contentKey="eTradeApp.optionStats.callReferencePrice">Call Reference Price</Translate>
                </Label>
                <AvField id="option-stats-callReferencePrice" type="string" className="form-control" name="callReferencePrice" />
              </AvGroup>
              <AvGroup>
                <Label id="callSettlementPriceLabel" for="option-stats-callSettlementPrice">
                  <Translate contentKey="eTradeApp.optionStats.callSettlementPrice">Call Settlement Price</Translate>
                </Label>
                <AvField id="option-stats-callSettlementPrice" type="string" className="form-control" name="callSettlementPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="callBsPriceLabel" for="option-stats-callBsPrice">
                  <Translate contentKey="eTradeApp.optionStats.callBsPrice">Call Bs Price</Translate>
                </Label>
                <AvField id="option-stats-callBsPrice" type="string" className="form-control" name="callBsPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="callTradeVolumeLabel" for="option-stats-callTradeVolume">
                  <Translate contentKey="eTradeApp.optionStats.callTradeVolume">Call Trade Volume</Translate>
                </Label>
                <AvField id="option-stats-callTradeVolume" type="string" className="form-control" name="callTradeVolume" />
              </AvGroup>
              <AvGroup>
                <Label id="callTradeCountLabel" for="option-stats-callTradeCount">
                  <Translate contentKey="eTradeApp.optionStats.callTradeCount">Call Trade Count</Translate>
                </Label>
                <AvField id="option-stats-callTradeCount" type="string" className="form-control" name="callTradeCount" />
              </AvGroup>
              <AvGroup>
                <Label id="callTradeValueLabel" for="option-stats-callTradeValue">
                  <Translate contentKey="eTradeApp.optionStats.callTradeValue">Call Trade Value</Translate>
                </Label>
                <AvField id="option-stats-callTradeValue" type="string" className="form-control" name="callTradeValue" />
              </AvGroup>
              <AvGroup>
                <Label id="callOpenInterestLabel" for="option-stats-callOpenInterest">
                  <Translate contentKey="eTradeApp.optionStats.callOpenInterest">Call Open Interest</Translate>
                </Label>
                <AvField id="option-stats-callOpenInterest" type="string" className="form-control" name="callOpenInterest" />
              </AvGroup>
              <AvGroup>
                <Label id="callBidPriceLabel" for="option-stats-callBidPrice">
                  <Translate contentKey="eTradeApp.optionStats.callBidPrice">Call Bid Price</Translate>
                </Label>
                <AvField id="option-stats-callBidPrice" type="string" className="form-control" name="callBidPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="callAskPriceLabel" for="option-stats-callAskPrice">
                  <Translate contentKey="eTradeApp.optionStats.callAskPrice">Call Ask Price</Translate>
                </Label>
                <AvField id="option-stats-callAskPrice" type="string" className="form-control" name="callAskPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="callBidVolumeLabel" for="option-stats-callBidVolume">
                  <Translate contentKey="eTradeApp.optionStats.callBidVolume">Call Bid Volume</Translate>
                </Label>
                <AvField id="option-stats-callBidVolume" type="string" className="form-control" name="callBidVolume" />
              </AvGroup>
              <AvGroup>
                <Label id="callAskVolumeLabel" for="option-stats-callAskVolume">
                  <Translate contentKey="eTradeApp.optionStats.callAskVolume">Call Ask Volume</Translate>
                </Label>
                <AvField id="option-stats-callAskVolume" type="string" className="form-control" name="callAskVolume" />
              </AvGroup>
              <AvGroup>
                <Label id="putLastLabel" for="option-stats-putLast">
                  <Translate contentKey="eTradeApp.optionStats.putLast">Put Last</Translate>
                </Label>
                <AvField id="option-stats-putLast" type="string" className="form-control" name="putLast" />
              </AvGroup>
              <AvGroup>
                <Label id="putCloseLabel" for="option-stats-putClose">
                  <Translate contentKey="eTradeApp.optionStats.putClose">Put Close</Translate>
                </Label>
                <AvField id="option-stats-putClose" type="string" className="form-control" name="putClose" />
              </AvGroup>
              <AvGroup>
                <Label id="putReferencePriceLabel" for="option-stats-putReferencePrice">
                  <Translate contentKey="eTradeApp.optionStats.putReferencePrice">Put Reference Price</Translate>
                </Label>
                <AvField id="option-stats-putReferencePrice" type="string" className="form-control" name="putReferencePrice" />
              </AvGroup>
              <AvGroup>
                <Label id="putSettlementPriceLabel" for="option-stats-putSettlementPrice">
                  <Translate contentKey="eTradeApp.optionStats.putSettlementPrice">Put Settlement Price</Translate>
                </Label>
                <AvField id="option-stats-putSettlementPrice" type="string" className="form-control" name="putSettlementPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="putBsPriceLabel" for="option-stats-putBsPrice">
                  <Translate contentKey="eTradeApp.optionStats.putBsPrice">Put Bs Price</Translate>
                </Label>
                <AvField id="option-stats-putBsPrice" type="string" className="form-control" name="putBsPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="putTradeVolumeLabel" for="option-stats-putTradeVolume">
                  <Translate contentKey="eTradeApp.optionStats.putTradeVolume">Put Trade Volume</Translate>
                </Label>
                <AvField id="option-stats-putTradeVolume" type="string" className="form-control" name="putTradeVolume" />
              </AvGroup>
              <AvGroup>
                <Label id="putTradeCountLabel" for="option-stats-putTradeCount">
                  <Translate contentKey="eTradeApp.optionStats.putTradeCount">Put Trade Count</Translate>
                </Label>
                <AvField id="option-stats-putTradeCount" type="string" className="form-control" name="putTradeCount" />
              </AvGroup>
              <AvGroup>
                <Label id="putTradeValueLabel" for="option-stats-putTradeValue">
                  <Translate contentKey="eTradeApp.optionStats.putTradeValue">Put Trade Value</Translate>
                </Label>
                <AvField id="option-stats-putTradeValue" type="string" className="form-control" name="putTradeValue" />
              </AvGroup>
              <AvGroup>
                <Label id="putOpenInterestLabel" for="option-stats-putOpenInterest">
                  <Translate contentKey="eTradeApp.optionStats.putOpenInterest">Put Open Interest</Translate>
                </Label>
                <AvField id="option-stats-putOpenInterest" type="string" className="form-control" name="putOpenInterest" />
              </AvGroup>
              <AvGroup>
                <Label id="putBidPriceLabel" for="option-stats-putBidPrice">
                  <Translate contentKey="eTradeApp.optionStats.putBidPrice">Put Bid Price</Translate>
                </Label>
                <AvField id="option-stats-putBidPrice" type="string" className="form-control" name="putBidPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="putAskPriceLabel" for="option-stats-putAskPrice">
                  <Translate contentKey="eTradeApp.optionStats.putAskPrice">Put Ask Price</Translate>
                </Label>
                <AvField id="option-stats-putAskPrice" type="string" className="form-control" name="putAskPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="putBidVolumeLabel" for="option-stats-putBidVolume">
                  <Translate contentKey="eTradeApp.optionStats.putBidVolume">Put Bid Volume</Translate>
                </Label>
                <AvField id="option-stats-putBidVolume" type="string" className="form-control" name="putBidVolume" />
              </AvGroup>
              <AvGroup>
                <Label id="putAskVolumeLabel" for="option-stats-putAskVolume">
                  <Translate contentKey="eTradeApp.optionStats.putAskVolume">Put Ask Volume</Translate>
                </Label>
                <AvField id="option-stats-putAskVolume" type="string" className="form-control" name="putAskVolume" />
              </AvGroup>
              <AvGroup>
                <Label id="blackScholes30Label" for="option-stats-blackScholes30">
                  <Translate contentKey="eTradeApp.optionStats.blackScholes30">Black Scholes 30</Translate>
                </Label>
                <AvField id="option-stats-blackScholes30" type="string" className="form-control" name="blackScholes30" />
              </AvGroup>
              <AvGroup>
                <Label id="blackScholes60Label" for="option-stats-blackScholes60">
                  <Translate contentKey="eTradeApp.optionStats.blackScholes60">Black Scholes 60</Translate>
                </Label>
                <AvField id="option-stats-blackScholes60" type="string" className="form-control" name="blackScholes60" />
              </AvGroup>
              <AvGroup>
                <Label id="blackScholes90Label" for="option-stats-blackScholes90">
                  <Translate contentKey="eTradeApp.optionStats.blackScholes90">Black Scholes 90</Translate>
                </Label>
                <AvField id="option-stats-blackScholes90" type="string" className="form-control" name="blackScholes90" />
              </AvGroup>
              <AvGroup>
                <Label id="callAskPriceToBSLabel" for="option-stats-callAskPriceToBS">
                  <Translate contentKey="eTradeApp.optionStats.callAskPriceToBS">Call Ask Price To BS</Translate>
                </Label>
                <AvField id="option-stats-callAskPriceToBS" type="string" className="form-control" name="callAskPriceToBS" />
              </AvGroup>
              <AvGroup>
                <Label id="callEffectivePriceLabel" for="option-stats-callEffectivePrice">
                  <Translate contentKey="eTradeApp.optionStats.callEffectivePrice">Call Effective Price</Translate>
                </Label>
                <AvField id="option-stats-callEffectivePrice" type="string" className="form-control" name="callEffectivePrice" />
              </AvGroup>
              <AvGroup>
                <Label id="callBreakEvenLabel" for="option-stats-callBreakEven">
                  <Translate contentKey="eTradeApp.optionStats.callBreakEven">Call Break Even</Translate>
                </Label>
                <AvField id="option-stats-callBreakEven" type="string" className="form-control" name="callBreakEven" />
              </AvGroup>
              <AvGroup>
                <Label id="putAskPriceToBSLabel" for="option-stats-putAskPriceToBS">
                  <Translate contentKey="eTradeApp.optionStats.putAskPriceToBS">Put Ask Price To BS</Translate>
                </Label>
                <AvField id="option-stats-putAskPriceToBS" type="string" className="form-control" name="putAskPriceToBS" />
              </AvGroup>
              <AvGroup>
                <Label id="putEffectivePriceLabel" for="option-stats-putEffectivePrice">
                  <Translate contentKey="eTradeApp.optionStats.putEffectivePrice">Put Effective Price</Translate>
                </Label>
                <AvField id="option-stats-putEffectivePrice" type="string" className="form-control" name="putEffectivePrice" />
              </AvGroup>
              <AvGroup>
                <Label id="putBreakEvenLabel" for="option-stats-putBreakEven">
                  <Translate contentKey="eTradeApp.optionStats.putBreakEven">Put Break Even</Translate>
                </Label>
                <AvField id="option-stats-putBreakEven" type="string" className="form-control" name="putBreakEven" />
              </AvGroup>
              <AvGroup>
                <Label for="option-stats-option">
                  <Translate contentKey="eTradeApp.optionStats.option">Option</Translate>
                </Label>
                <AvInput
                  id="option-stats-option"
                  type="select"
                  className="form-control"
                  name="option.id"
                  value={isNew ? options[0] && options[0].id : optionStatsEntity.option.id}
                  required
                >
                  {options
                    ? options.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/option-stats" replace color="info">
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
  options: storeState.option.entities,
  optionStatsEntity: storeState.optionStats.entity,
  loading: storeState.optionStats.loading,
  updating: storeState.optionStats.updating,
  updateSuccess: storeState.optionStats.updateSuccess
});

const mapDispatchToProps = {
  getOptions,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OptionStatsUpdate);
