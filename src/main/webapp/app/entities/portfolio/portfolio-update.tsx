import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './portfolio.reducer';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPortfolioUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PortfolioUpdate = (props: IPortfolioUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { portfolioEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/portfolio');
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
        ...portfolioEntity,
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
          <h2 id="eTradeApp.portfolio.home.createOrEditLabel">
            <Translate contentKey="eTradeApp.portfolio.home.createOrEditLabel">Create or edit a Portfolio</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : portfolioEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="portfolio-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="portfolio-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="userIdLabel" for="portfolio-userId">
                  <Translate contentKey="eTradeApp.portfolio.userId">User Id</Translate>
                </Label>
                <AvField id="portfolio-userId" type="text" name="userId" />
              </AvGroup>
              <AvGroup>
                <Label id="dateLabel" for="portfolio-date">
                  <Translate contentKey="eTradeApp.portfolio.date">Date</Translate>
                </Label>
                <AvField id="portfolio-date" type="date" className="form-control" name="date" />
              </AvGroup>
              <AvGroup>
                <Label id="isinLabel" for="portfolio-isin">
                  <Translate contentKey="eTradeApp.portfolio.isin">Isin</Translate>
                </Label>
                <AvField id="portfolio-isin" type="text" name="isin" />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLabel" for="portfolio-quantity">
                  <Translate contentKey="eTradeApp.portfolio.quantity">Quantity</Translate>
                </Label>
                <AvField id="portfolio-quantity" type="string" className="form-control" name="quantity" />
              </AvGroup>
              <AvGroup>
                <Label id="avgPriceLabel" for="portfolio-avgPrice">
                  <Translate contentKey="eTradeApp.portfolio.avgPrice">Avg Price</Translate>
                </Label>
                <AvField id="portfolio-avgPrice" type="string" className="form-control" name="avgPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="lastPriceLabel" for="portfolio-lastPrice">
                  <Translate contentKey="eTradeApp.portfolio.lastPrice">Last Price</Translate>
                </Label>
                <AvField id="portfolio-lastPrice" type="string" className="form-control" name="lastPrice" />
              </AvGroup>
              <AvGroup>
                <Label id="closePriceLabel" for="portfolio-closePrice">
                  <Translate contentKey="eTradeApp.portfolio.closePrice">Close Price</Translate>
                </Label>
                <AvField id="portfolio-closePrice" type="string" className="form-control" name="closePrice" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/portfolio" replace color="info">
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
  portfolioEntity: storeState.portfolio.entity,
  loading: storeState.portfolio.loading,
  updating: storeState.portfolio.updating,
  updateSuccess: storeState.portfolio.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PortfolioUpdate);
