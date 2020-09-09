import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './instrument.reducer';
import { IInstrument } from 'app/shared/model/instrument.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IInstrumentProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Instrument = (props: IInstrumentProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `updatedAt,${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    props.history.push(
      `${props.location.pathname}?page=${paginationState.activePage}&sort=updatedAt,${paginationState.order}`
    );
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, 'updatedAt']);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage
    });

  const { instrumentList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="instrument-heading">
        <Translate contentKey="eTradeApp.instrument.home.title">Instruments</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="eTradeApp.instrument.home.createLabel">Create new Instrument</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {instrumentList && instrumentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('isin')}>
                  <Translate contentKey="eTradeApp.instrument.isin">Isin</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="eTradeApp.instrument.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tseId')}>
                  <Translate contentKey="eTradeApp.instrument.tseId">Tse Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('volatility30')}>
                  <Translate contentKey="eTradeApp.instrument.volatility30">Volatility 30</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('volatility60')}>
                  <Translate contentKey="eTradeApp.instrument.volatility60">Volatility 60</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('volatility90')}>
                  <Translate contentKey="eTradeApp.instrument.volatility90">Volatility 90</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="eTradeApp.instrument.updatedAt">Updated At</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {instrumentList.map((instrument, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${instrument.isin}`} color="link" size="sm">
                      {instrument.isin}
                    </Button>
                  </td>
                  <td>{instrument.name}</td>
                  <td>{instrument.tseId}</td>
                  <td>{instrument.volatility30}</td>
                  <td>{instrument.volatility60}</td>
                  <td>{instrument.volatility90}</td>
                  <td>
                    <TextFormat type="date" value={instrument.updatedAt} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${instrument.isin}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${instrument.isin}/edit?page=${paginationState.activePage}&sort=updatedAt,${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${instrument.isin}/delete?page=${paginationState.activePage}&sort=updatedAt,${paginationState.order}`}
                        color="danger"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="eTradeApp.instrument.home.notFound">No Instruments found</Translate>
            </div>
          )
        )}
      </div>
      <div className={instrumentList && instrumentList.length > 0 ? '' : 'd-none'}>
        <Row className="justify-content-center">
          <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
        </Row>
        <Row className="justify-content-center">
          <JhiPagination
            activePage={paginationState.activePage}
            onSelect={handlePagination}
            maxButtons={5}
            itemsPerPage={paginationState.itemsPerPage}
            totalItems={props.totalItems}
          />
        </Row>
      </div>
    </div>
  );
};

const mapStateToProps = ({ instrument }: IRootState) => ({
  instrumentList: instrument.entities,
  loading: instrument.loading,
  totalItems: instrument.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Instrument);
