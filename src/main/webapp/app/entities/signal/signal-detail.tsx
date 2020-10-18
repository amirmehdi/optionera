import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntity, sendOrder} from './signal.reducer';
import {APP_DATE_FORMAT} from 'app/config/constants';

export interface ISignalDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}

export const SignalDetail = (props: ISignalDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const {signalEntity} = props;
  const sendOrderOfSignal = (event, errors, values) => {
    props.sendOrder(props.signalEntity.id);
  };
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.signal.detail.title">Signal</Translate> [<b>{signalEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="type">
              <Translate contentKey="eTradeApp.signal.type">Type</Translate>
            </span>
          </dt>
          <dd>{signalEntity.type}</dd>
          <dt>
            <span id="isin">
              <Translate contentKey="eTradeApp.signal.isin">Isin</Translate>
            </span>
          </dt>
          <dd>{signalEntity.isin}</dd>
          <dt>
            <span id="last">
              <Translate contentKey="eTradeApp.signal.last">Last</Translate>
            </span>
          </dt>
          <dd>{signalEntity.last}</dd>
          <dt>
            <span id="tradeVolume">
              <Translate contentKey="eTradeApp.signal.tradeVolume">Trade Volume</Translate>
            </span>
          </dt>
          <dd>{signalEntity.tradeVolume}</dd>
          <dt>
            <span id="bidVolume">
              <Translate contentKey="eTradeApp.signal.bidVolume">Bid Volume</Translate>
            </span>
          </dt>
          <dd>{signalEntity.bidVolume}</dd>
          <dt>
            <span id="bidPrice">
              <Translate contentKey="eTradeApp.signal.bidPrice">Bid Price</Translate>
            </span>
          </dt>
          <dd>{signalEntity.bidPrice}</dd>
          <dt>
            <span id="askPrice">
              <Translate contentKey="eTradeApp.signal.askPrice">Ask Price</Translate>
            </span>
          </dt>
          <dd>{signalEntity.askPrice}</dd>
          <dt>
            <span id="askVolume">
              <Translate contentKey="eTradeApp.signal.askVolume">Ask Volume</Translate>
            </span>
          </dt>
          <dd>{signalEntity.askVolume}</dd>
          <dt>
            <span id="baseInstrumentLast">
              <Translate contentKey="eTradeApp.signal.baseInstrumentLast">Base Instrument Last</Translate>
            </span>
          </dt>
          <dd>{signalEntity.baseInstrumentLast}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="eTradeApp.signal.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={signalEntity.createdAt} type="date" format={APP_DATE_FORMAT}/>
          </dd>
        </dl>
        <Button tag={Link} to="/signal" replace color="info">
          <FontAwesomeIcon icon="arrow-left"/>{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/signal/${signalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt"/>{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
        &nbsp;
        <Button onclick={sendOrderOfSignal} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt"/>{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.sendOrder">send order</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({signal}: IRootState) => ({
  signalEntity: signal.entity
});

const mapDispatchToProps = {
  getEntity,
  sendOrder
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SignalDetail);
