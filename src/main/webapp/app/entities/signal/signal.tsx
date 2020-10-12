import React, {useEffect, useState} from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Table} from 'reactstrap';
import {getSortState, TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntities, reset} from './signal.reducer';
import {APP_DATE_FORMAT} from 'app/config/constants';
import {ITEMS_PER_PAGE} from 'app/shared/util/pagination.constants';

export interface ISignalProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {
}

export const Signal = (props: ISignalProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));
  const [sorting, setSorting] = useState(false);

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const resetAll = () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1
    });
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p
    });
    setSorting(true);
  };

  const {signalList, match, loading} = props;
  return (
    <div>
      <h2 id="signal-heading">
        <Translate contentKey="eTradeApp.signal.home.title">Signals</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus"/>
          &nbsp;
          <Translate contentKey="eTradeApp.signal.home.createLabel">Create new Signal</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          pageStart={paginationState.activePage}
          loadMore={handleLoadMore}
          hasMore={paginationState.activePage - 1 < props.links.next}
          loader={<div className="loader">Loading ...</div>}
          threshold={0}
          initialLoad={false}
        >
          {signalList && signalList.length > 0 ? (
            <Table responsive>
              <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="eTradeApp.signal.type">Type</Translate> <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('isin')}>
                  <Translate contentKey="eTradeApp.signal.isin">Isin</Translate> <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('last')}>
                  <Translate contentKey="eTradeApp.signal.last">Last</Translate> <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('tradeVolume')}>
                  <Translate contentKey="eTradeApp.signal.tradeVolume">Trade Volume</Translate> <FontAwesomeIcon
                  icon="sort"/>
                </th>
                <th className="hand" onClick={sort('bidVolume')}>
                  <Translate contentKey="eTradeApp.signal.bidVolume">Bid Volume</Translate> <FontAwesomeIcon
                  icon="sort"/>
                </th>
                <th className="hand" onClick={sort('bidPrice')}>
                  <Translate contentKey="eTradeApp.signal.bidPrice">Bid Price</Translate> <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('askPrice')}>
                  <Translate contentKey="eTradeApp.signal.askPrice">Ask Price</Translate> <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('askVolume')}>
                  <Translate contentKey="eTradeApp.signal.askVolume">Ask Volume</Translate> <FontAwesomeIcon
                  icon="sort"/>
                </th>
                <th className="hand" onClick={sort('baseInstrumentLast')}>
                  <Translate contentKey="eTradeApp.signal.baseInstrumentLast">Base Instrument Last</Translate>{' '}
                  <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="eTradeApp.signal.createdAt">Created At</Translate> <FontAwesomeIcon
                  icon="sort"/>
                </th>
                <th/>
              </tr>
              </thead>
              <tbody>
              {signalList.map((signal, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${signal.id}`} color="link" size="sm">
                      {signal.id}
                    </Button>
                  </td>
                  <td>{signal.type}</td>
                  <td>{signal.isin}</td>
                  <td>{signal.last}</td>
                  <td>{signal.tradeVolume}</td>
                  <td>{signal.bidVolume}</td>
                  <td>{signal.bidPrice}</td>
                  <td>{signal.askPrice}</td>
                  <td>{signal.askVolume}</td>
                  <td>{signal.baseInstrumentLast}</td>
                  <td>
                    <TextFormat type="date" value={signal.createdAt} format={APP_DATE_FORMAT}/>
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${signal.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye"/>{' '}
                        <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${signal.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt"/>{' '}
                        <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${signal.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash"/>{' '}
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
                <Translate contentKey="eTradeApp.signal.home.notFound">No Signals found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

const mapStateToProps = ({signal}: IRootState) => ({
  signalList: signal.entities,
  loading: signal.loading,
  totalItems: signal.totalItems,
  links: signal.links,
  entity: signal.entity,
  updateSuccess: signal.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Signal);
