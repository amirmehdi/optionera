import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './option-stats.reducer';
import { IOptionStats } from 'app/shared/model/option-stats.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOptionStatsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OptionStatsDetail = (props: IOptionStatsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { optionStatsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.optionStats.detail.title">OptionStats</Translate> [<b>{optionStatsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="callLast">
              <Translate contentKey="eTradeApp.optionStats.callLast">Call Last</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callLast}</dd>
          <dt>
            <span id="callClose">
              <Translate contentKey="eTradeApp.optionStats.callClose">Call Close</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callClose}</dd>
          <dt>
            <span id="callReferencePrice">
              <Translate contentKey="eTradeApp.optionStats.callReferencePrice">Call Reference Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callReferencePrice}</dd>
          <dt>
            <span id="callSettlementPrice">
              <Translate contentKey="eTradeApp.optionStats.callSettlementPrice">Call Settlement Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callSettlementPrice}</dd>
          <dt>
            <span id="callBsPrice">
              <Translate contentKey="eTradeApp.optionStats.callBsPrice">Call Bs Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callBsPrice}</dd>
          <dt>
            <span id="callTradeVolume">
              <Translate contentKey="eTradeApp.optionStats.callTradeVolume">Call Trade Volume</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callTradeVolume}</dd>
          <dt>
            <span id="callTradeCount">
              <Translate contentKey="eTradeApp.optionStats.callTradeCount">Call Trade Count</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callTradeCount}</dd>
          <dt>
            <span id="callTradeValue">
              <Translate contentKey="eTradeApp.optionStats.callTradeValue">Call Trade Value</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callTradeValue}</dd>
          <dt>
            <span id="callOpenInterest">
              <Translate contentKey="eTradeApp.optionStats.callOpenInterest">Call Open Interest</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callOpenInterest}</dd>
          <dt>
            <span id="callBidPrice">
              <Translate contentKey="eTradeApp.optionStats.callBidPrice">Call Bid Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callBidPrice}</dd>
          <dt>
            <span id="callAskPrice">
              <Translate contentKey="eTradeApp.optionStats.callAskPrice">Call Ask Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callAskPrice}</dd>
          <dt>
            <span id="callBidVolume">
              <Translate contentKey="eTradeApp.optionStats.callBidVolume">Call Bid Volume</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callBidVolume}</dd>
          <dt>
            <span id="callAskVolume">
              <Translate contentKey="eTradeApp.optionStats.callAskVolume">Call Ask Volume</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callAskVolume}</dd>
          <dt>
            <span id="putLast">
              <Translate contentKey="eTradeApp.optionStats.putLast">Put Last</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putLast}</dd>
          <dt>
            <span id="putClose">
              <Translate contentKey="eTradeApp.optionStats.putClose">Put Close</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putClose}</dd>
          <dt>
            <span id="putReferencePrice">
              <Translate contentKey="eTradeApp.optionStats.putReferencePrice">Put Reference Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putReferencePrice}</dd>
          <dt>
            <span id="putSettlementPrice">
              <Translate contentKey="eTradeApp.optionStats.putSettlementPrice">Put Settlement Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putSettlementPrice}</dd>
          <dt>
            <span id="putBsPrice">
              <Translate contentKey="eTradeApp.optionStats.putBsPrice">Put Bs Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putBsPrice}</dd>
          <dt>
            <span id="putTradeVolume">
              <Translate contentKey="eTradeApp.optionStats.putTradeVolume">Put Trade Volume</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putTradeVolume}</dd>
          <dt>
            <span id="putTradeCount">
              <Translate contentKey="eTradeApp.optionStats.putTradeCount">Put Trade Count</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putTradeCount}</dd>
          <dt>
            <span id="putTradeValue">
              <Translate contentKey="eTradeApp.optionStats.putTradeValue">Put Trade Value</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putTradeValue}</dd>
          <dt>
            <span id="putOpenInterest">
              <Translate contentKey="eTradeApp.optionStats.putOpenInterest">Put Open Interest</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putOpenInterest}</dd>
          <dt>
            <span id="putBidPrice">
              <Translate contentKey="eTradeApp.optionStats.putBidPrice">Put Bid Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putBidPrice}</dd>
          <dt>
            <span id="putAskPrice">
              <Translate contentKey="eTradeApp.optionStats.putAskPrice">Put Ask Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putAskPrice}</dd>
          <dt>
            <span id="putBidVolume">
              <Translate contentKey="eTradeApp.optionStats.putBidVolume">Put Bid Volume</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putBidVolume}</dd>
          <dt>
            <span id="putAskVolume">
              <Translate contentKey="eTradeApp.optionStats.putAskVolume">Put Ask Volume</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putAskVolume}</dd>
          <dt>
            <span id="blackScholes30">
              <Translate contentKey="eTradeApp.optionStats.blackScholes30">Black Scholes 30</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.blackScholes30}</dd>
          <dt>
            <span id="blackScholes60">
              <Translate contentKey="eTradeApp.optionStats.blackScholes60">Black Scholes 60</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.blackScholes60}</dd>
          <dt>
            <span id="blackScholes90">
              <Translate contentKey="eTradeApp.optionStats.blackScholes90">Black Scholes 90</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.blackScholes90}</dd>
          <dt>
            <span id="callAskPriceToBS">
              <Translate contentKey="eTradeApp.optionStats.callAskPriceToBS">Call Ask Price To BS</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callAskPriceToBS}</dd>
          <dt>
            <span id="callEffectivePrice">
              <Translate contentKey="eTradeApp.optionStats.callEffectivePrice">Call Effective Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callEffectivePrice}</dd>
          <dt>
            <span id="callBreakEven">
              <Translate contentKey="eTradeApp.optionStats.callBreakEven">Call Break Even</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.callBreakEven}</dd>
          <dt>
            <span id="putAskPriceToBS">
              <Translate contentKey="eTradeApp.optionStats.putAskPriceToBS">Put Ask Price To BS</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putAskPriceToBS}</dd>
          <dt>
            <span id="putEffectivePrice">
              <Translate contentKey="eTradeApp.optionStats.putEffectivePrice">Put Effective Price</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putEffectivePrice}</dd>
          <dt>
            <span id="putBreakEven">
              <Translate contentKey="eTradeApp.optionStats.putBreakEven">Put Break Even</Translate>
            </span>
          </dt>
          <dd>{optionStatsEntity.putBreakEven}</dd>
          <dt>
            <Translate contentKey="eTradeApp.optionStats.option">Option</Translate>
          </dt>
          <dd>{optionStatsEntity.option ? optionStatsEntity.option.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/option-stats" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/option-stats/${optionStatsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ optionStats }: IRootState) => ({
  optionStatsEntity: optionStats.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OptionStatsDetail);
