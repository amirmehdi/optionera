import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './board.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBoardDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BoardDetail = (props: IBoardDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { boardEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.board.detail.title">Board</Translate> [<b>{boardEntity.isin}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="isin">
              <Translate contentKey="eTradeApp.board.isin">Isin</Translate>
            </span>
          </dt>
          <dd>{boardEntity.isin}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="eTradeApp.board.date">Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={boardEntity.date} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="last">
              <Translate contentKey="eTradeApp.board.last">Last</Translate>
            </span>
          </dt>
          <dd>{boardEntity.last}</dd>
          <dt>
            <span id="close">
              <Translate contentKey="eTradeApp.board.close">Close</Translate>
            </span>
          </dt>
          <dd>{boardEntity.close}</dd>
          <dt>
            <span id="first">
              <Translate contentKey="eTradeApp.board.first">First</Translate>
            </span>
          </dt>
          <dd>{boardEntity.first}</dd>
          <dt>
            <span id="low">
              <Translate contentKey="eTradeApp.board.low">Low</Translate>
            </span>
          </dt>
          <dd>{boardEntity.low}</dd>
          <dt>
            <span id="high">
              <Translate contentKey="eTradeApp.board.high">High</Translate>
            </span>
          </dt>
          <dd>{boardEntity.high}</dd>
          <dt>
            <span id="tradeCount">
              <Translate contentKey="eTradeApp.board.tradeCount">Trade Count</Translate>
            </span>
          </dt>
          <dd>{boardEntity.tradeCount}</dd>
          <dt>
            <span id="tradeVolume">
              <Translate contentKey="eTradeApp.board.tradeVolume">Trade Volume</Translate>
            </span>
          </dt>
          <dd>{boardEntity.tradeVolume}</dd>
          <dt>
            <span id="tradeValue">
              <Translate contentKey="eTradeApp.board.tradeValue">Trade Value</Translate>
            </span>
          </dt>
          <dd>{boardEntity.tradeValue}</dd>
          <dt>
            <span id="askPrice">
              <Translate contentKey="eTradeApp.board.askPrice">Ask Price</Translate>
            </span>
          </dt>
          <dd>{boardEntity.askPrice}</dd>
          <dt>
            <span id="askVolume">
              <Translate contentKey="eTradeApp.board.askVolume">Ask Volume</Translate>
            </span>
          </dt>
          <dd>{boardEntity.askVolume}</dd>
          <dt>
            <span id="bidPrice">
              <Translate contentKey="eTradeApp.board.bidPrice">Bid Price</Translate>
            </span>
          </dt>
          <dd>{boardEntity.bidPrice}</dd>
          <dt>
            <span id="bidVolume">
              <Translate contentKey="eTradeApp.board.bidVolume">Bid Volume</Translate>
            </span>
          </dt>
          <dd>{boardEntity.bidVolume}</dd>
          <dt>
            <span id="individualBuyVolume">
              <Translate contentKey="eTradeApp.board.individualBuyVolume">Individual Buy Volume</Translate>
            </span>
          </dt>
          <dd>{boardEntity.individualBuyVolume}</dd>
          <dt>
            <span id="individualSellVolume">
              <Translate contentKey="eTradeApp.board.individualSellVolume">Individual Sell Volume</Translate>
            </span>
          </dt>
          <dd>{boardEntity.individualSellVolume}</dd>
          <dt>
            <span id="legalBuyVolume">
              <Translate contentKey="eTradeApp.board.legalBuyVolume">Legal Buy Volume</Translate>
            </span>
          </dt>
          <dd>{boardEntity.legalBuyVolume}</dd>
          <dt>
            <span id="legalSellVolume">
              <Translate contentKey="eTradeApp.board.legalSellVolume">Legal Sell Volume</Translate>
            </span>
          </dt>
          <dd>{boardEntity.legalSellVolume}</dd>
          <dt>
            <span id="referencePrice">
              <Translate contentKey="eTradeApp.board.referencePrice">Reference Price</Translate>
            </span>
          </dt>
          <dd>{boardEntity.referencePrice}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="eTradeApp.board.state">State</Translate>
            </span>
          </dt>
          <dd>{boardEntity.state}</dd>
        </dl>
        <Button tag={Link} to="/board" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ board }: IRootState) => ({
  boardEntity: board.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BoardDetail);
