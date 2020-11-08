import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntity} from './algorithm.reducer';
import {APP_DATE_FORMAT} from 'app/config/constants';

export interface IAlgorithmDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AlgorithmDetail = (props: IAlgorithmDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { algorithmEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.algorithm.detail.title">Algorithm</Translate> [<b>{algorithmEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="type">
              <Translate contentKey="eTradeApp.algorithm.type">Type</Translate>
            </span>
          </dt>
          <dd>{algorithmEntity.type}</dd>
          <dt>
            <span id="side">
              <Translate contentKey="eTradeApp.algorithm.side">Side</Translate>
            </span>
          </dt>
          <dd>{algorithmEntity.side}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="eTradeApp.algorithm.state">State</Translate>
            </span>
          </dt>
          <dd>{algorithmEntity.state}</dd>
          <dt>
            <span id="input">
              <Translate contentKey="eTradeApp.algorithm.input">Input</Translate>
            </span>
          </dt>
          <dd>{algorithmEntity.input}</dd>
          <dt>
            <span id="tradeVolumeLimit">
              <Translate contentKey="eTradeApp.algorithm.tradeVolumeLimit">Trade Volume Limit</Translate>
            </span>
          </dt>
          <dd>{algorithmEntity.tradeVolumeLimit}</dd>
          <dt>
            <span id="tradeValueLimit">
              <Translate contentKey="eTradeApp.algorithm.tradeValueLimit">Trade Value Limit</Translate>
            </span>
          </dt>
          <dd>{algorithmEntity.tradeValueLimit}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="eTradeApp.algorithm.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={algorithmEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="eTradeApp.algorithm.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={algorithmEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="isins">
              <Translate contentKey="eTradeApp.algorithm.isins">Isins</Translate>
            </span>
          </dt>
          <dd>{algorithmEntity.isins}</dd>
        </dl>
        <Button tag={Link} to="/algorithm" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/algorithm/${algorithmEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ algorithm }: IRootState) => ({
  algorithmEntity: algorithm.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AlgorithmDetail);
