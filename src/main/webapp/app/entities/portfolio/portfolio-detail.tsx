import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './portfolio.reducer';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPortfolioDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PortfolioDetail = (props: IPortfolioDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { portfolioEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="eTradeApp.portfolio.detail.title">Portfolio</Translate> [<b>{portfolioEntity.userId + ',' + portfolioEntity.isin + ',' + portfolioEntity.date}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="userId">
              <Translate contentKey="eTradeApp.portfolio.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{portfolioEntity.userId}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="eTradeApp.portfolio.date">Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={portfolioEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="isin">
              <Translate contentKey="eTradeApp.portfolio.isin">Isin</Translate>
            </span>
          </dt>
          <dd>{portfolioEntity.isin}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="eTradeApp.portfolio.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{portfolioEntity.quantity}</dd>
          <dt>
            <span id="avgPrice">
              <Translate contentKey="eTradeApp.portfolio.avgPrice">Avg Price</Translate>
            </span>
          </dt>
          <dd>{portfolioEntity.avgPrice}</dd>
        </dl>
        <Button tag={Link} to="/portfolio" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/portfolio/${portfolioEntity.userId + ',' + portfolioEntity.isin + ',' + portfolioEntity.date}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ portfolio }: IRootState) => ({
  portfolioEntity: portfolio.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PortfolioDetail);
