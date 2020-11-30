import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderDetail = (props: IOrderDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.order.detail.title">Order</Translate> [<b>{orderEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="isin">
              <Translate contentKey="eTradeApp.order.isin">Isin</Translate>
            </span>
          </dt>
          <dd>{orderEntity.isin}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="eTradeApp.order.price">Price</Translate>
            </span>
          </dt>
          <dd>{orderEntity.price}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="eTradeApp.order.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{orderEntity.quantity}</dd>
          <dt>
            <span id="validity">
              <Translate contentKey="eTradeApp.order.validity">Validity</Translate>
            </span>
          </dt>
          <dd>{orderEntity.validity}</dd>
          <dt>
            <span id="side">
              <Translate contentKey="eTradeApp.order.side">Side</Translate>
            </span>
          </dt>
          <dd>{orderEntity.side}</dd>
          <dt>
            <span id="broker">
              <Translate contentKey="eTradeApp.order.broker">Broker</Translate>
            </span>
          </dt>
          <dd>{orderEntity.broker}</dd>
          <dt>
            <span id="omsId">
              <Translate contentKey="eTradeApp.order.omsId">Oms Id</Translate>
            </span>
          </dt>
          <dd>{orderEntity.omsId}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="eTradeApp.order.state">State</Translate>
            </span>
          </dt>
          <dd>{orderEntity.state}</dd>
          <dt>
            <span id="executed">
              <Translate contentKey="eTradeApp.order.executed">Executed</Translate>
            </span>
          </dt>
          <dd>{orderEntity.executed}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="eTradeApp.order.description">Description</Translate>
            </span>
          </dt>
          <dd>{orderEntity.description}</dd>
          <dt>
            <Translate contentKey="eTradeApp.order.signal">Signal</Translate>
          </dt>
          <dd>{orderEntity.signal ? orderEntity.signal.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order/${orderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ order }: IRootState) => ({
  orderEntity: order.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderDetail);
