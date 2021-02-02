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
              <AvGroup>
                <Label id="securityFieldsLabel" for="token-securityFields">
                  <Translate contentKey="eTradeApp.token.securityFields">Security Fields</Translate>
                </Label>
                <AvField id="token-securityFields" type="text" name="securityFields" />
              </AvGroup>
              <AvGroup>
                <Label for="token-bourseCode">
                  <Translate contentKey="eTradeApp.token.bourseCode">Bourse Code</Translate>
                </Label>
                <AvInput
                  id="token-bourseCode"
                  type="select"
                  className="form-control"
                  name="bourseCode.id"
                  value={isNew ? bourseCodes[0] && bourseCodes[0].id : tokenEntity.bourseCode.id}
                  required
                >
                  {bourseCodes
                    ? bourseCodes.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
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
