import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Table } from 'antd';
import { getSortState, Translate } from 'react-jhipster';
import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './option-stats.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

const { Column, ColumnGroup } = Table;

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

  const { optionStatsList, loading } = props;
  return (
        <InfiniteScroll
          pageStart={paginationState.activePage}
          loadMore={handleLoadMore}
          hasMore={paginationState.activePage - 1 < props.links.next}
          loader={<div className="loader">Loading ...</div>}
          threshold={0}
          initialLoad={false}
        >
          {optionStatsList && optionStatsList.length > 0 ? (

              <Table sticky pagination={false} loading={loading} dataSource={optionStatsList as any}
                     scroll={{ x: 4000 }}>
                <Column fixed={'left'}   title={<Translate contentKey="eTradeApp.optionStats.option">Option</Translate>}
                         dataIndex="option" key="optionStats.name" render={(optionStats) =>
                  optionStats ? <Link to={`option/${optionStats.id}`}>{'ض' + optionStats.name}</Link> : ''
                }/>
                <ColumnGroup title="call">

                  <Column
                    title={<Translate contentKey="eTradeApp.optionStats.BlackScholes30"> Black Scholes 30</Translate>}
                    dataIndex="callBS30" key="callBS30" render={(callBS30) =>
                    callBS30
                  }/>
                  <Column
                    title={<Translate contentKey="eTradeApp.optionStats.EffectivePrice"> Effective Price</Translate>}
                    dataIndex="callEffectivePrice" key="callEffectivePrice"/>
                  <Column
                    title={<Translate contentKey="eTradeApp.optionStats.AskPriceToBS"> Ask Price To BS</Translate>}
                    dataIndex="option" key=" optionCallAskToBS" render={(option) =>
                    option.callAskToBS
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.BreakEven"> Break Even</Translate>}
                          dataIndex="option" key="optionCallBreakEven" render={(option) =>
                    option.callBreakEven
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.Leverage"> Leverage</Translate>}
                          dataIndex="option" key="optionCallLeverage" render={(option) =>
                    option.callLeverage
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.TradeVolume"> Trade Volume</Translate>}
                          dataIndex="callStockWatch" key="callStockWatchTradeVolume" render={(callStockWatch) =>
                    callStockWatch?.tradeVolume
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.Last"> Last</Translate>}
                          dataIndex="callStockWatch" key="callStockWatchLast" render={(callStockWatch) =>
                    callStockWatch?.last
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.OpenInterest"> Open Interest</Translate>}
                          dataIndex="callStockWatch" key="callStockWatchOpenInterest" render={(callStockWatch) =>
                    callStockWatch?.openInterest
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.BidVolume"> Bid Volume</Translate>}
                          dataIndex="callBidAsk" key="callBidAskBidQuantity" render={(callBidAsk) =>
                    callBidAsk.bidQuantity
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.BidPrice"> Bid Price</Translate>}
                          dataIndex="callBidAsk" key="callBidAskBidPrice" render={(callBidAsk) =>
                    callBidAsk.bidPrice
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.AskPrice"> Ask Price</Translate>}
                          dataIndex="callBidAsk" key="callBidAskAskPrice" render={(callBidAsk) =>
                    callBidAsk.askPrice
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.AskVolume"> Ask Volume</Translate>}
                          dataIndex="callBidAsk" key="callBidAskAskQuantity" render={(callBidAsk) =>
                    callBidAsk.askQuantity
                  }/>

                </ColumnGroup>
                <Column  title={<Translate contentKey="eTradeApp.option.strikePrice">Strike Price</Translate>}
                        dataIndex="option" key="optionStrikePrice" render={(option) =>
                  option.strikePrice
                }/>
                <Column  title={<Translate contentKey="eTradeApp.option.expDate">Exp Date</Translate>}
                        dataIndex="option" key="optionExpDate" render={(option) =>
                  option.expDate
                }/>
                <Column title={<Translate contentKey="eTradeApp.option.instrument">underlying asset</Translate>}
                        dataIndex="option" key="optionInstrument" render={(option , record:any) =>
                  option.instrument ? <a href={`http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=${option.instrument.tseId}`}>{option.instrument.name} {record.baseStockWatch?.last}</a> : ''

                }/>
                <ColumnGroup title="Put">
                  <Column title={<Translate contentKey="eTradeApp.optionStats.BidVolume"> Bid Volume</Translate>}
                          dataIndex="putBidAsk" key="putBidAskBidQuantity" render={(putBidAsk) =>
                    putBidAsk.bidQuantity
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.BidPrice"> Bid Price</Translate>}
                          dataIndex="putBidAsk" key="putBidAskBidPrice" render={(putBidAsk) =>
                    putBidAsk.bidPrice
                  }/>
                  <Column title={ <Translate contentKey="eTradeApp.optionStats.AskPrice"> Ask Price</Translate>}
                          dataIndex="putBidAsk" key="putBidAskAskPrice" render={(putBidAsk) =>
                    putBidAsk.askPrice
                  }/>
                  <Column title={  <Translate contentKey="eTradeApp.optionStats.AskVolume"> Ask Volume</Translate>}
                          dataIndex="putBidAsk" key="putBidAskAskQuantity" render={(putBidAsk) =>
                    putBidAsk.askQuantity
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.OpenInterest"> Open Interest</Translate>}
                          dataIndex="putStockWatch" key="putStockWatchOpenInterest" render={(putStockWatch) =>
                    putStockWatch?.openInterest
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.Last"> Last</Translate>}
                          dataIndex="putStockWatch" key="putStockWatchLast" render={(putStockWatch) =>
                    putStockWatch?.last
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.TradeVolume"> Trade Volume</Translate>}
                          dataIndex="putStockWatch" key="putStockWatchTradeVolume" render={(putStockWatch) =>
                    putStockWatch?.tradeVolume
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.Leverage"> Leverage</Translate>}
                          dataIndex="option" key="optionPutLeverage" render={(option) =>
                    option.putLeverage
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.BreakEven"> Break Even</Translate>}
                          dataIndex="option" key="optionPutBreakEven" render={(option) =>
                    option.putBreakEven
                  }/>
                  <Column title={ <Translate contentKey="eTradeApp.optionStats.AskPriceToBS"> Ask Price To BS</Translate>}
                          dataIndex="option" key="optionPutAskToBS" render={(option) =>
                    option.putAskToBS
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.EffectivePrice"> Effective Price</Translate>}
                          dataIndex="option" key="optionPutEffectivePrice" render={(option) =>
                    option.putEffectivePrice
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.BlackScholes30"> Black Scholes 30</Translate>}
                          dataIndex="option" key="optionPutBS30" render={(option) =>
                    option.putBS30
                  }/>
                  <Column title={<Translate contentKey="eTradeApp.optionStats.option">Option</Translate>}
                           dataIndex="option" key="option" render={(option) =>
                    option ?  <Link to={`option/${option.id}`}>{'ض'+ option.name}</Link> : ''
                  }/>
                </ColumnGroup>
              </Table>



          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="eTradeApp.optionStats.home.notFound">No Option Stats found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
  );
};

const mapStateToProps = ({ optionStats }: IRootState) => ({
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
