import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IBourseCode } from 'app/shared/model/bourse-code.model';
import { getEntities as getBourseCodes } from 'app/entities/bourse-code/bourse-code.reducer';
import { getEntity, updateEntity, createEntity, reset } from './token.reducer';
import { IToken } from 'app/shared/model/token.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITokenUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TokenUpdate = (props: ITokenUpdateProps) => {
  const [bourseCodeId, setBourseCodeId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { tokenEntity, bourseCodes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/token' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getBourseCodes();
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
        ...tokenEntity,
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
          <h2 id="eTradeApp.token.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.token.home.createOrEditLabel">Create or edit a Token</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : tokenEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="token-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="token-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="tokenLabel" for="token-token">
                  <Translate contentKey="eTradeApp.token.token">Token</Translate>
                </Label>
                <AvField
                  id="token-token"
                  type="text"
                  name="token"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="brokerLabel" for="token-broker">
                  <Translate contentKey="eTradeApp.token.broker">Broker</Translate>
                </Label>
                <AvInput
                  id="token-broker"
                  type="select"
                  className="form-control"
                  name="broker"
                  value={(!isNew && tokenEntity.broker) || 'REFAH'}
                >
                  <option value="REFAH">{translate('eTradeApp.Broker.REFAH')}</option>
                  <option value="FIROOZE_ASIA">{translate('eTradeApp.Broker.FIROOZE_ASIA')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="createdAtLabel" for="token-createdAt">
                  <Translate contentKey="eTradeApp.token.createdAt">Created At</Translate>
                </Label>
                <AvInput
                  id="token-createdAt"
                  type="datetime-local"
                  className="form-control"
                  name="createdAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.tokenEntity.createdAt)}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/token" replace color="info">
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
  bourseCodes: storeState.bourseCode.entities,
  tokenEntity: storeState.token.entity,
  loading: storeState.token.loading,
  updating: storeState.token.updating,
  updateSuccess: storeState.token.updateSuccess
});

const mapDispatchToProps = {
  getBourseCodes,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TokenUpdate);
