import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './open-interest.reducer';
import { IOpenInterest } from 'app/shared/model/open-interest.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOpenInterestDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OpenInterestDetail = (props: IOpenInterestDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { openInterestEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.openInterest.detail.title">OpenInterest</Translate> [<b>{openInterestEntity.userId + ',' + openInterestEntity.isin + ',' + openInterestEntity.date}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="userId">
              <Translate contentKey="eTradeApp.openInterest.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{openInterestEntity.userId}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="eTradeApp.openInterest.date">Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={openInterestEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="isin">
              <Translate contentKey="eTradeApp.openInterest.isin">Isin</Translate>
            </span>
          </dt>
          <dd>{openInterestEntity.isin}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="eTradeApp.openInterest.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{openInterestEntity.quantity}</dd>
          <dt>
            <span id="lastPrice">
              <Translate contentKey="eTradeApp.openInterest.lastPrice">Last Price</Translate>
            </span>
          </dt>
          <dd>{openInterestEntity.lastPrice}</dd>
          <dt>
            <span id="closePrice">
              <Translate contentKey="eTradeApp.openInterest.closePrice">Close Price</Translate>
            </span>
          </dt>
          <dd>{openInterestEntity.closePrice}</dd>
          <dt>
            <span id="marginAmount">
              <Translate contentKey="eTradeApp.openInterest.marginAmount">Margin Amount</Translate>
            </span>
          </dt>
          <dd>{openInterestEntity.marginAmount}</dd>
        </dl>
        <Button tag={Link} to="/open-interest" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/open-interest/${openInterestEntity.userId + ',' + openInterestEntity.isin + ',' + openInterestEntity.date}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ openInterest }: IRootState) => ({
  openInterestEntity: openInterest.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OpenInterestDetail);
