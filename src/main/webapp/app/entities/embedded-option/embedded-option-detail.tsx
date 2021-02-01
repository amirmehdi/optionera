import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './embedded-option.reducer';
import { IEmbeddedOption } from 'app/shared/model/embedded-option.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmbeddedOptionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmbeddedOptionDetail = (props: IEmbeddedOptionDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { embeddedOptionEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.embeddedOption.detail.title">EmbeddedOption</Translate> [<b>{embeddedOptionEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="eTradeApp.embeddedOption.name">Name</Translate>
            </span>
          </dt>
          <dd>{embeddedOptionEntity.name}</dd>
          <dt>
            <span id="isin">
              <Translate contentKey="eTradeApp.embeddedOption.isin">Isin</Translate>
            </span>
          </dt>
          <dd>{embeddedOptionEntity.isin}</dd>
          <dt>
            <span id="expDate">
              <Translate contentKey="eTradeApp.embeddedOption.expDate">Exp Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={embeddedOptionEntity.expDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="strikePrice">
              <Translate contentKey="eTradeApp.embeddedOption.strikePrice">Strike Price</Translate>
            </span>
          </dt>
          <dd>{embeddedOptionEntity.strikePrice}</dd>
          <dt>
            <span id="tseId">
              <Translate contentKey="eTradeApp.embeddedOption.tseId">Tse Id</Translate>
            </span>
          </dt>
          <dd>{embeddedOptionEntity.tseId}</dd>
          <dt>
            <Translate contentKey="eTradeApp.embeddedOption.underlyingInstrument">Underlying Instrument</Translate>
          </dt>
          <dd>{embeddedOptionEntity.underlyingInstrument ? embeddedOptionEntity.underlyingInstrument.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/embedded-option" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/embedded-option/${embeddedOptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ embeddedOption }: IRootState) => ({
  embeddedOptionEntity: embeddedOption.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmbeddedOptionDetail);
