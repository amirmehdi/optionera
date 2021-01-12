import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './bourse-code.reducer';
import { IBourseCode } from 'app/shared/model/bourse-code.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBourseCodeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BourseCodeDetail = (props: IBourseCodeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bourseCodeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.bourseCode.detail.title">BourseCode</Translate> [<b>{bourseCodeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="broker">
              <Translate contentKey="eTradeApp.bourseCode.broker">Broker</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.broker}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="eTradeApp.bourseCode.name">Name</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.name}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="eTradeApp.bourseCode.code">Code</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.code}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="eTradeApp.bourseCode.username">Username</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.username}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="eTradeApp.bourseCode.password">Password</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.password}</dd>
          <dt>
            <span id="buyingPower">
              <Translate contentKey="eTradeApp.bourseCode.buyingPower">Buying Power</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.buyingPower}</dd>
          <dt>
            <span id="blocked">
              <Translate contentKey="eTradeApp.bourseCode.blocked">Blocked</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.blocked}</dd>
          <dt>
            <span id="remain">
              <Translate contentKey="eTradeApp.bourseCode.remain">Remain</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.remain}</dd>
          <dt>
            <span id="credit">
              <Translate contentKey="eTradeApp.bourseCode.credit">Credit</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.credit}</dd>
          <dt>
            <span id="conditions">
              <Translate contentKey="eTradeApp.bourseCode.conditions">Conditions</Translate>
            </span>
          </dt>
          <dd>{bourseCodeEntity.conditions}</dd>
          <dt>
            <Translate contentKey="eTradeApp.bourseCode.token">Token</Translate>
          </dt>
          <dd>{bourseCodeEntity.token ? bourseCodeEntity.token.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/bourse-code" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bourse-code/${bourseCodeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bourseCode }: IRootState) => ({
  bourseCodeEntity: bourseCode.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BourseCodeDetail);
