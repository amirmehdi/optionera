import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './option.reducer';
import { IOption } from 'app/shared/model/option.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOptionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OptionDetail = (props: IOptionDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { optionEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.option.detail.title">Option</Translate> [<b>{optionEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="eTradeApp.option.name">Name</Translate>
            </span>
          </dt>
          <dd>{optionEntity.name}</dd>
          <dt>
            <span id="callIsin">
              <Translate contentKey="eTradeApp.option.callIsin">Call Isin</Translate>
            </span>
          </dt>
          <dd>{optionEntity.callIsin}</dd>
          <dt>
            <span id="putIsin">
              <Translate contentKey="eTradeApp.option.putIsin">Put Isin</Translate>
            </span>
          </dt>
          <dd>{optionEntity.putIsin}</dd>
          <dt>
            <span id="expDate">
              <Translate contentKey="eTradeApp.option.expDate">Exp Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={optionEntity.expDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="strikePrice">
              <Translate contentKey="eTradeApp.option.strikePrice">Strike Price</Translate>
            </span>
          </dt>
          <dd>{optionEntity.strikePrice}</dd>
          <dt>
            <span id="contractSize">
              <Translate contentKey="eTradeApp.option.contractSize">Contract Size</Translate>
            </span>
          </dt>
          <dd>{optionEntity.contractSize}</dd>
          <dt>
            <span id="callInTheMoney">
              <Translate contentKey="eTradeApp.option.callInTheMoney">Call In The Money</Translate>
            </span>
          </dt>
          <dd>{optionEntity.callInTheMoney ? 'true' : 'false'}</dd>
          <dt>
            <span id="callBreakEven">
              <Translate contentKey="eTradeApp.option.callBreakEven">Call Break Even</Translate>
            </span>
          </dt>
          <dd>{optionEntity.callBreakEven}</dd>
          <dt>
            <span id="putBreakEven">
              <Translate contentKey="eTradeApp.option.putBreakEven">Put Break Even</Translate>
            </span>
          </dt>
          <dd>{optionEntity.putBreakEven}</dd>
          <dt>
            <span id="callAskToBS">
              <Translate contentKey="eTradeApp.option.callAskToBS">Call Ask To BS</Translate>
            </span>
          </dt>
          <dd>{optionEntity.callAskToBS}</dd>
          <dt>
            <span id="putAskToBS">
              <Translate contentKey="eTradeApp.option.putAskToBS">Put Ask To BS</Translate>
            </span>
          </dt>
          <dd>{optionEntity.putAskToBS}</dd>
          <dt>
            <span id="callLeverage">
              <Translate contentKey="eTradeApp.option.callLeverage">Call Leverage</Translate>
            </span>
          </dt>
          <dd>{optionEntity.callLeverage}</dd>
          <dt>
            <span id="putLeverage">
              <Translate contentKey="eTradeApp.option.putLeverage">Put Leverage</Translate>
            </span>
          </dt>
          <dd>{optionEntity.putLeverage}</dd>
          <dt>
            <Translate contentKey="eTradeApp.option.instrument">Instrument</Translate>
          </dt>
          <dd>{optionEntity.instrument ? optionEntity.instrument.isin : ''}</dd>
        </dl>
        <Button tag={Link} to="/option" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/option/${optionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ option }: IRootState) => ({
  optionEntity: option.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OptionDetail);
