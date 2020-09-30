import React, {useEffect, useState} from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Table} from 'reactstrap';
import {getSortState, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntities, reset} from './option-stats.reducer';
import {ITEMS_PER_PAGE} from 'app/shared/util/pagination.constants';

export interface IOptionStatsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {
}

export const OptionStats = (props: IOptionStatsProps) => {
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

  const {optionStatsList, match, loading} = props;
  return (
    <div>
      <h2 id="option-stats-heading">
        <Translate contentKey="eTradeApp.optionStats.home.title">Option Stats</Translate>
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
          {optionStatsList && optionStatsList.length > 0 ? (
            <Table responsive>
              <thead>
              <tr>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.option">Option</Translate>
                </th>

                <th>
                  <Translate contentKey="eTradeApp.optionStats.blackScholes30">call Black Scholes 30</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.blackScholes60">call Black Scholes 60</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.blackScholes90">call Black Scholes 90</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.callEffectivePrice">Call Effective Price</Translate>{' '}
                </th>
                <th className="hand" onClick={sort('callAskToBS')}>
                  <Translate contentKey="eTradeApp.optionStats.callAskPriceToBS">Call Ask Price To BS</Translate>{' '}
                  <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('callBreakEven')}>
                  <Translate contentKey="eTradeApp.optionStats.callBreakEven">Call Break Even</Translate>
                  <FontAwesomeIcon icon="sort"/>
                </th>


                <th>
                  <Translate contentKey="eTradeApp.optionStats.callTradeVolume">Call Trade Volume</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.callLast">Call Last</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.callOpenInterest">Call Open Interest</Translate>{' '}
                </th>

                <th>
                  <Translate contentKey="eTradeApp.optionStats.callBidVolume">Call Bid Volume</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.callBidPrice">Call Bid Price</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.callAskPrice">Call Ask Price</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.callAskVolume">Call Ask Volume</Translate>
                </th>

                <th className="hand" onClick={sort('strikePrice')}>
                  <Translate contentKey="eTradeApp.option.strikePrice">Strike Price</Translate>
                  <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('expDate')}>
                  <Translate contentKey="eTradeApp.option.expDate">Exp Date</Translate>
                  <FontAwesomeIcon icon="sort"/>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.option.instrument">underlying asset</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.underlyingAssetLast">underlying asset last</Translate>
                </th>


                <th>
                  <Translate contentKey="eTradeApp.optionStats.putBidVolume">Put Bid Volume</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.putBidPrice">Put Bid Price</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.putAskPrice">Put Ask Price</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.putAskVolume">Put Ask Volume</Translate>
                </th>


                <th>
                  <Translate contentKey="eTradeApp.optionStats.putOpenInterest">Put Open Interest</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.putLast">Put Last</Translate>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.putTradeVolume">Put Trade Volume</Translate>{' '}
                </th>


                <th className="hand" onClick={sort('putBreakEven')}>
                  <Translate contentKey="eTradeApp.optionStats.putBreakEven">Put Break Even</Translate>
                  <FontAwesomeIcon icon="sort"/>
                </th>
                <th className="hand" onClick={sort('putAskToBS')}>
                  <Translate contentKey="eTradeApp.optionStats.putAskPriceToBS">Put Ask Price To BS</Translate>{' '}
                  <FontAwesomeIcon icon="sort"/>
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.putEffectivePrice">Put Effective Price</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.blackScholes90">put Black Scholes 90</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.blackScholes60">put Black Scholes 60</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.blackScholes30">put Black Scholes 30</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="eTradeApp.optionStats.option">Option</Translate>
                </th>
                <th/>
              </tr>
              </thead>
              <tbody>
              {optionStatsList.map((optionStats, i) => (
                <tr key={`entity-${i}`}>
                  <td>{optionStats.option ?
                    <Link to={`option/${optionStats.option.id}`}>{'ض'+optionStats.option.name}</Link> : ''}</td>

                  <td>{optionStats.callBS30}</td>
                  <td>{optionStats.callBS60}</td>
                  <td>{optionStats.callBS90}</td>
                  <td>{optionStats.callEffectivePrice}</td>
                  <td>{optionStats.option.callAskToBS}</td>
                  <td>{optionStats.option.callBreakEven}</td>

                  <td>{optionStats.callStockWatch?.tradeVolume}</td>
                  <td>{optionStats.callStockWatch?.last}</td>
                  <td>{optionStats.callStockWatch?.openInterest}</td>

                  <td>{optionStats.callBidAsk.bidQuantity}</td>
                  <td>{optionStats.callBidAsk.bidPrice}</td>
                  <td>{optionStats.callBidAsk.askPrice}</td>
                  <td>{optionStats.callBidAsk.askQuantity}</td>

                  <td>{optionStats.option.strikePrice}</td>
                  <td>{optionStats.option.expDate}</td>
                  <td>{optionStats.option.instrument ? <Link
                    to={`instrument/${optionStats.option.instrument.isin}`}>{optionStats.option.instrument.name}</Link> : ''}</td>
                  <td>{optionStats.baseStockWatch?.last}</td>

                  <td>{optionStats.putBidAsk.bidQuantity}</td>
                  <td>{optionStats.putBidAsk.bidPrice}</td>
                  <td>{optionStats.putBidAsk.askPrice}</td>
                  <td>{optionStats.putBidAsk.askQuantity}</td>

                  <td>{optionStats.putStockWatch?.openInterest}</td>
                  <td>{optionStats.putStockWatch?.last}</td>
                  <td>{optionStats.putStockWatch?.tradeVolume}</td>

                  <td>{optionStats.option.putBreakEven}</td>
                  <td>{optionStats.option.putAskToBS}</td>
                  <td>{optionStats.putEffectivePrice}</td>
                  <td>{optionStats.putBS90}</td>
                  <td>{optionStats.putBS60}</td>
                  <td>{optionStats.putBS30}</td>
                  <td>{optionStats.option ?
                    <Link to={`option/${optionStats.option.id}`}>{'ط'+optionStats.option.name}</Link> : ''}</td>

                </tr>
              ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="eTradeApp.optionStats.home.notFound">No Option Stats found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

const mapStateToProps = ({optionStats}: IRootState) => ({
  optionStatsList: optionStats.entities,
  loading: optionStats.loading,
  totalItems: optionStats.totalItems,
  links: optionStats.links,
  entity: optionStats.entity,
  updateSuccess: optionStats.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OptionStats);
