import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Label, Row} from 'reactstrap';
import {AvField, AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';
import {Translate, translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {IRootState} from 'app/shared/reducers';

import {createEntity, getEntity, reset, updateEntity} from './algorithm.reducer';
import {convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime} from 'app/shared/util/date-utils';

export interface IAlgorithmUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AlgorithmUpdate = (props: IAlgorithmUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { algorithmEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/algorithm' + props.location.search);
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    if (errors.length === 0) {
      const entity = {
        ...algorithmEntity,
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
          <h2 id="eTradeApp.algorithm.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.algorithm.home.createOrEditLabel">Create or edit a Algorithm</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : algorithmEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="algorithm-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="algorithm-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="typeLabel" for="algorithm-type">
                  <Translate contentKey="eTradeApp.algorithm.type">Type</Translate>
                </Label>
                <AvField
                  id="algorithm-type"
                  type="text"
                  name="type"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="sideLabel" for="algorithm-side">
                  <Translate contentKey="eTradeApp.algorithm.side">Side</Translate>
                </Label>
                <AvInput
                  id="algorithm-side"
                  type="select"
                  className="form-control"
                  name="side"
                  value={(!isNew && algorithmEntity.side) || 'BUY'}
                >
                  <option value="BUY">{translate('eTradeApp.AlgorithmSide.BUY')}</option>
                  <option value="SELL">{translate('eTradeApp.AlgorithmSide.SELL')}</option>
                  <option value="SIDE">{translate('eTradeApp.AlgorithmSide.SIDE')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="stateLabel" for="algorithm-state">
                  <Translate contentKey="eTradeApp.algorithm.state">State</Translate>
                </Label>
                <AvInput
                  id="algorithm-state"
                  type="select"
                  className="form-control"
                  name="state"
                  value={(!isNew && algorithmEntity.state) || 'ENABLE'}
                >
                  <option value="ENABLE">{translate('eTradeApp.AlgorithmState.ENABLE')}</option>
                  <option value="DISABLE">{translate('eTradeApp.AlgorithmState.DISABLE')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="inputLabel" for="algorithm-input">
                  <Translate contentKey="eTradeApp.algorithm.input">Input</Translate>
                </Label>
                <AvField id="algorithm-input" type="text" name="input" />
              </AvGroup>
              <AvGroup>
                <Label id="tradeVolumeLimitLabel" for="algorithm-tradeVolumeLimit">
                  <Translate contentKey="eTradeApp.algorithm.tradeVolumeLimit">Trade Volume Limit</Translate>
                </Label>
                <AvField id="algorithm-tradeVolumeLimit" type="string" className="form-control" name="tradeVolumeLimit" />
              </AvGroup>
              <AvGroup>
                <Label id="tradeValueLimitLabel" for="algorithm-tradeValueLimit">
                  <Translate contentKey="eTradeApp.algorithm.tradeValueLimit">Trade Value Limit</Translate>
                </Label>
                <AvField id="algorithm-tradeValueLimit" type="string" className="form-control" name="tradeValueLimit" />
              </AvGroup>
              <AvGroup>
                <Label id="createdAtLabel" for="algorithm-createdAt">
                  <Translate contentKey="eTradeApp.algorithm.createdAt">Created At</Translate>
                </Label>
                <AvInput
                  id="algorithm-createdAt"
                  type="datetime-local"
                  className="form-control"
                  name="createdAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.algorithmEntity.createdAt)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedAtLabel" for="algorithm-updatedAt">
                  <Translate contentKey="eTradeApp.algorithm.updatedAt">Updated At</Translate>
                </Label>
                <AvInput
                  id="algorithm-updatedAt"
                  type="datetime-local"
                  className="form-control"
                  name="updatedAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.algorithmEntity.updatedAt)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="isinsLabel" for="algorithm-isins">
                  <Translate contentKey="eTradeApp.algorithm.isins">Isins</Translate>
                </Label>
                <AvField
                  id="algorithm-isins"
                  type="text"
                  name="isins"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/algorithm" replace color="info">
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
  algorithmEntity: storeState.algorithm.entity,
  loading: storeState.algorithm.loading,
  updating: storeState.algorithm.updating,
  updateSuccess: storeState.algorithm.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AlgorithmUpdate);
