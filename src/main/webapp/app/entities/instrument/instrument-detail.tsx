import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './instrument.reducer';
import { IInstrument } from 'app/shared/model/instrument.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInstrumentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InstrumentDetail = (props: IInstrumentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { instrumentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.instrument.detail.title">Instrument</Translate> [<b>{instrumentEntity.isin}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="eTradeApp.instrument.name">Name</Translate>
            </span>
          </dt>
          <dd>{instrumentEntity.name}</dd>
          <dt>
            <span id="isin">
              <Translate contentKey="eTradeApp.instrument.isin">Isin</Translate>
            </span>
          </dt>
          <dd>{instrumentEntity.isin}</dd>
          <dt>
            <span id="tseId">
              <Translate contentKey="eTradeApp.instrument.tseId">Tse Id</Translate>
            </span>
          </dt>
          <dd>{instrumentEntity.tseId}</dd>
          <dt>
            <span id="volatility30">
              <Translate contentKey="eTradeApp.instrument.volatility30">Volatility 30</Translate>
            </span>
          </dt>
          <dd>{instrumentEntity.volatility30}</dd>
          <dt>
            <span id="volatility60">
              <Translate contentKey="eTradeApp.instrument.volatility60">Volatility 60</Translate>
            </span>
          </dt>
          <dd>{instrumentEntity.volatility60}</dd>
          <dt>
            <span id="volatility90">
              <Translate contentKey="eTradeApp.instrument.volatility90">Volatility 90</Translate>
            </span>
          </dt>
          <dd>{instrumentEntity.volatility90}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="eTradeApp.instrument.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={instrumentEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
        </dl>
        <Button tag={Link} to="/instrument" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/instrument/${instrumentEntity.isin}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ instrument }: IRootState) => ({
  instrumentEntity: instrument.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InstrumentDetail);
