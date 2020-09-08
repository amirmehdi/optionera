import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './instrument-history.reducer';
import { IInstrumentHistory } from 'app/shared/model/instrument-history.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInstrumentHistoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InstrumentHistoryDetail = (props: IInstrumentHistoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { instrumentHistoryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.instrumentHistory.detail.title">InstrumentHistory</Translate> [
          <b>{instrumentHistoryEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="isin">
              <Translate contentKey="eTradeApp.instrumentHistory.isin">Isin</Translate>
            </span>
          </dt>
          <dd>{instrumentHistoryEntity.isin}</dd>
          <dt>
            <span id="closing">
              <Translate contentKey="eTradeApp.instrumentHistory.closing">Closing</Translate>
            </span>
          </dt>
          <dd>{instrumentHistoryEntity.closing}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="eTradeApp.instrumentHistory.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={instrumentHistoryEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
        </dl>
        <Button tag={Link} to="/instrument-history" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/instrument-history/${instrumentHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ instrumentHistory }: IRootState) => ({
  instrumentHistoryEntity: instrumentHistory.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InstrumentHistoryDetail);
