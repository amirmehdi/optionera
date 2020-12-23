import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IToken } from 'app/shared/model/token.model';
import { getEntities as getTokens } from 'app/entities/token/token.reducer';
import { getEntity, updateEntity, createEntity, reset } from './bourse-code.reducer';
import { IBourseCode } from 'app/shared/model/bourse-code.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBourseCodeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BourseCodeUpdate = (props: IBourseCodeUpdateProps) => {
  const [tokenId, setTokenId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { bourseCodeEntity, tokens, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/bourse-code' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getTokens();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...bourseCodeEntity,
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
          <h2 id="eTradeApp.bourseCode.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.bourseCode.home.createOrEditLabel">Create or edit a BourseCode</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bourseCodeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="bourse-code-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="bourse-code-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="brokerLabel" for="bourse-code-broker">
                  <Translate contentKey="eTradeApp.bourseCode.broker">Broker</Translate>
                </Label>
                <AvInput
                  id="bourse-code-broker"
                  type="select"
                  className="form-control"
                  name="broker"
                  value={(!isNew && bourseCodeEntity.broker) || 'REFAH'}
                >
                  <option value="REFAH">{translate('eTradeApp.Broker.REFAH')}</option>
                  <option value="HAFEZ">{translate('eTradeApp.Broker.HAFEZ')}</option>
                  <option value="GANJINE">{translate('eTradeApp.Broker.GANJINE')}</option>
                  <option value="FIROOZE_ASIA">{translate('eTradeApp.Broker.FIROOZE_ASIA')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="bourse-code-name">
                  <Translate contentKey="eTradeApp.bourseCode.name">Name</Translate>
                </Label>
                <AvField id="bourse-code-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="bourse-code-code">
                  <Translate contentKey="eTradeApp.bourseCode.code">Code</Translate>
                </Label>
                <AvField id="bourse-code-code" type="text" name="code" />
              </AvGroup>
              <AvGroup>
                <Label id="usernameLabel" for="bourse-code-username">
                  <Translate contentKey="eTradeApp.bourseCode.username">Username</Translate>
                </Label>
                <AvField id="bourse-code-username" type="text" name="username" />
              </AvGroup>
              <AvGroup>
                <Label id="passwordLabel" for="bourse-code-password">
                  <Translate contentKey="eTradeApp.bourseCode.password">Password</Translate>
                </Label>
                <AvField id="bourse-code-password" type="text" name="password" />
              </AvGroup>
              <AvGroup>
                <Label id="buyingPowerLabel" for="bourse-code-buyingPower">
                  <Translate contentKey="eTradeApp.bourseCode.buyingPower">Buying Power</Translate>
                </Label>
                <AvField id="bourse-code-buyingPower" type="string" className="form-control" name="buyingPower" />
              </AvGroup>
              <AvGroup>
                <Label id="blockedLabel" for="bourse-code-blocked">
                  <Translate contentKey="eTradeApp.bourseCode.blocked">Blocked</Translate>
                </Label>
                <AvField id="bourse-code-blocked" type="string" className="form-control" name="blocked" />
              </AvGroup>
              <AvGroup>
                <Label id="remainLabel" for="bourse-code-remain">
                  <Translate contentKey="eTradeApp.bourseCode.remain">Remain</Translate>
                </Label>
                <AvField id="bourse-code-remain" type="string" className="form-control" name="remain" />
              </AvGroup>
              <AvGroup>
                <Label id="creditLabel" for="bourse-code-credit">
                  <Translate contentKey="eTradeApp.bourseCode.credit">Credit</Translate>
                </Label>
                <AvField id="bourse-code-credit" type="string" className="form-control" name="credit" />
              </AvGroup>
              <AvGroup>
                <Label for="bourse-code-token">
                  <Translate contentKey="eTradeApp.bourseCode.token">Token</Translate>
                </Label>
                <AvInput id="bourse-code-token" type="select" className="form-control" name="token.id">
                  <option value="" key="0" />
                  {tokens
                    ? tokens.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/bourse-code" replace color="info">
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
  tokens: storeState.token.entities,
  bourseCodeEntity: storeState.bourseCode.entity,
  loading: storeState.bourseCode.loading,
  updating: storeState.bourseCode.updating,
  updateSuccess: storeState.bourseCode.updateSuccess
});

const mapDispatchToProps = {
  getTokens,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BourseCodeUpdate);
