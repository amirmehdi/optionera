import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './token.reducer';
import { IToken } from 'app/shared/model/token.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITokenDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TokenDetail = (props: ITokenDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { tokenEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.token.detail.title">Token</Translate> [<b>{tokenEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="token">
              <Translate contentKey="eTradeApp.token.token">Token</Translate>
            </span>
          </dt>
          <dd>{tokenEntity.token}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="eTradeApp.token.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={tokenEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="securityFields">
              <Translate contentKey="eTradeApp.token.securityFields">Security Fields</Translate>
            </span>
          </dt>
          <dd>{tokenEntity.securityFields}</dd>
          <dt>
            <Translate contentKey="eTradeApp.token.bourseCode">Bourse Code</Translate>
          </dt>
          <dd>{tokenEntity.bourseCode ? tokenEntity.bourseCode.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/token" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/token/${tokenEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ token }: IRootState) => ({
  tokenEntity: token.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TokenDetail);
