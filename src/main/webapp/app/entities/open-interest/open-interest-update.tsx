import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './open-interest.reducer';
import { IOpenInterest } from 'app/shared/model/open-interest.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOpenInterestUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OpenInterestUpdate = (props: IOpenInterestUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { openInterestEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/open-interest');
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
    if (errors.length === 0) {
      const entity = {
        ...openInterestEntity,
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
          <h2 id="eTradeApp.openInterest.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.openInterest.home.createOrEditLabel">Create or edit a OpenInterest</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : openInterestEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="open-interest-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="open-interest-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="userIdLabel" for="open-interest-userId">
                  <Translate contentKey="eTradeApp.openInterest.userId">User Id</Translate>
                </Label>
                <AvField id="open-interest-userId" type="text" name="userId" />
              </AvGroup>
              <AvGroup>
                <Label id="dateLabel" for="open-interest-date">
                  <Translate contentKey="eTradeApp.openInterest.date">Date</Translate>
                </Label>
                <AvField id="open-interest-date" type="date" className="form-control" name="date" />
              </AvGroup>
              <AvGroup>
                <Label id="isinLabel" for="open-interest-isin">
                  <Translate contentKey="eTradeApp.openInterest.isin">Isin</Translate>
                </Label>
                <AvField id="open-interest-isin" type="text" name="isin" />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLabel" for="open-interest-quantity">
                  <Translate contentKey="eTradeApp.openInterest.quantity">Quantity</Translate>
                </Label>
                <AvField id="open-interest-quantity" type="string" className="form-control" name="quantity" />
              </AvGroup>
              <AvGroup>
                <Label id="lastPriceLabel" for="open-interest-lastPrice">
                  <Translate contentKey="eTradeApp.openInterest.lastPrice">Last Price</Translate>
                </Label>
                <AvField id="open-interest-lastPrice" type="string" className="form-control" name="lastPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="closePriceLabel" for="open-interest-closePrice">
                  <Translate contentKey="eTradeApp.openInterest.closePrice">Close Price</Translate>
                </Label>
                <AvField id="open-interest-closePrice" type="string" className="form-control" name="closePrice" />
              </AvGroup>
              <AvGroup>
                <Label id="marginAmountLabel" for="open-interest-marginAmount">
                  <Translate contentKey="eTradeApp.openInterest.marginAmount">Margin Amount</Translate>
                </Label>
                <AvField id="open-interest-marginAmount" type="string" className="form-control" name="marginAmount" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/open-interest" replace color="info">
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
  openInterestEntity: storeState.openInterest.entity,
  loading: storeState.openInterest.loading,
  updating: storeState.openInterest.updating,
  updateSuccess: storeState.openInterest.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OpenInterestUpdate);
