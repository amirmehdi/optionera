import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Spin, Table } from 'antd';
import { getSortState, Translate } from 'react-jhipster';
import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './option-stats.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import './style.scss';
import { SyncOutlined } from '@ant-design/icons';
import { SearchOptionStats } from 'app/entities/option-stats/Search-option-stats';
import DateTime from "./../../DateTime/DateTime"

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
  const _computeDateInJalaliFormat = (createdAt:any) => {
    if (createdAt) {
      const createAtDate = DateTime.stringToDate(createdAt);
      const createAtJalaliDate = DateTime.gregorianToJalali(createAtDate.year, createAtDate.month, createAtDate.day);
      const weekDayNumber = DateTime.getJalaliMonthFirstWeekDay(createAtJalaliDate.year, createAtJalaliDate.month, createAtJalaliDate.day);
      return DateTime.weekNames[weekDayNumber] + ' ' + createAtJalaliDate.day + ' ' +
        DateTime.monthNames[createAtJalaliDate.month - 1] + ' ' + createAtJalaliDate.year + ' - ' + createAtDate.hour + ':' + createAtDate.minute + ':' + createAtDate.second
    }
    return null
  }

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const handleChange = (pagination, filters, sorter) => {
    if (sorter.column && sorter.column.sorter) {
      props.reset();
      setPaginationState({
        ...paginationState,
        activePage: 1,
        order: paginationState.order === 'asc' ? 'desc' : 'asc',
        sort: sorter.columnKey
      });
      setSorting(true);
    } else if (sorter.column === undefined) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
        order: paginationState.order === 'asc' ? 'desc' : 'asc',
        sort: sorter.columnKey
      });
      setSorting(true);
      props.reset();
    }
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
        <div>
          <div className="content-search-box">
            <SyncOutlined onClick={() => getAllEntities()}/>
            <SearchOptionStats instrumentId={(id:number) => id && props.getEntities(id)}/>
          </div>
          <Table sticky pagination={false} onChange={handleChange} dataSource={optionStatsList as any}
                 scroll={{ x: 2400 }}>
            <Column fixed={'left'} title={<Translate contentKey="eTradeApp.optionStats.option">Option</Translate>}
                    dataIndex="option" key="optionStats.name" render={(optionStats , row:any) =>
              <div className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{optionStats ?
                <Link to={`option/${optionStats.id}`}>{'ض' + optionStats.name}</Link> : ''}</div>
            }/>
            <ColumnGroup title="call">

              <Column
                title={<Translate contentKey="eTradeApp.optionStats.BlackScholes30"> Black Scholes 30</Translate>}
                dataIndex="callBS30" key="callBS30" render={(callBS30, row: any) =>
                <div className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callBS30}</div>
              }/>
              <Column
                title={<Translate contentKey="eTradeApp.optionStats.EffectivePrice"> Effective Price</Translate>}
                dataIndex="callEffectivePrice" key="callEffectivePrice" render={(callEffectivePrice, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callEffectivePrice}</div>
              }/>
              <Column
                sorter={true}
                sortDirections={['ascend', 'descend']}
                sortOrder={paginationState.sort === 'callAskToBS' ?
                  paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                showSorterTooltip={false}
                title={<Translate contentKey="eTradeApp.optionStats.AskPriceToBS"> Ask Price To BS</Translate>}
                dataIndex="option" key="callAskToBS" render={(option, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option.callAskToBS}</div>
              }/>
              <Column
                sorter={true}
                sortDirections={['ascend', 'descend']}
                sortOrder={paginationState.sort === 'callBreakEven' ?
                  paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                showSorterTooltip={false}
                title={<Translate contentKey="eTradeApp.optionStats.BreakEven"> Break Even</Translate>}
                dataIndex="option" key="callBreakEven" render={(option, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option.callBreakEven}</div>
              }/>
              <Column
                sorter={true}
                sortDirections={['ascend', 'descend']}
                sortOrder={paginationState.sort === 'callLeverage' ?
                  paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                showSorterTooltip={false}
                title={<Translate contentKey="eTradeApp.optionStats.Leverage"> Leverage</Translate>}
                dataIndex="option" key="callLeverage" render={(option, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option.callLeverage}</div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.TradeVolume"> Trade Volume</Translate>}
                      dataIndex="callStockWatch" key="callStockWatchTradeVolume" render={(callStockWatch, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callStockWatch?.tradeVolume}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.Last"> Last</Translate>}
                      dataIndex="callStockWatch" key="callStockWatchLast" render={(callStockWatch, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callStockWatch?.last}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.OpenInterest"> Open Interest</Translate>}
                      dataIndex="callStockWatch" key="callStockWatchOpenInterest" render={(callStockWatch, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callStockWatch?.openInterest}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidVolume"> Bid Volume</Translate>}
                      dataIndex="callBidAsk" key="callBidAskBidQuantity" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callBidAsk.bidQuantity}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidPrice"> Bid Price</Translate>}
                      dataIndex="callBidAsk" key="callBidAskBidPrice" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callBidAsk.bidPrice}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskPrice"> Ask Price</Translate>}
                      dataIndex="callBidAsk" key="callBidAskAskPrice" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callBidAsk.askPrice}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskVolume"> Ask Volume</Translate>}
                      dataIndex="callBidAsk" key="callBidAskAskQuantity" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{callBidAsk.askQuantity}</div>
              }/>

            </ColumnGroup>
            <Column className="bg-color-gray" sorter={true}
                    sortDirections={['ascend', 'descend']}
                    sortOrder={paginationState.sort === 'strikePrice' ?
                      paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                    showSorterTooltip={false}
                    title={<Translate contentKey="eTradeApp.option.strikePrice">Strike Price</Translate>}
                    dataIndex="option" key="strikePrice" render={(option) =>
              <div className={`padding-col`}>{option.strikePrice}</div>
            }/>
            <Column className="bg-color-gray" sorter={true}
                    sortDirections={['ascend', 'descend']}
                    sortOrder={paginationState.sort === 'expDate' ?
                      paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                    showSorterTooltip={false}
                    title={<Translate contentKey="eTradeApp.option.expDate">Exp Date</Translate>}
                    dataIndex="option" key="expDate" render={(option) =>
              <div className={`padding-col`}>{_computeDateInJalaliFormat(option.expDate)}</div>
            }/>
            <Column className="bg-color-gray"
                    title={<Translate contentKey="eTradeApp.option.instrument">underlying asset</Translate>}
                    dataIndex="option" key="optionInstrument" render={(option, record: any) =>
              <div className={`padding-col`}>
                { option.instrument ? <a
                href={`http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=${option.instrument.tseId}`}>{option.instrument.name} {record.baseStockWatch?.last}</a> : ''}

              </div>
            }/>
            <ColumnGroup title="Put">
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidVolume"> Bid Volume</Translate>}
                      dataIndex="putBidAsk" key="putBidAskBidQuantity" render={(putBidAsk , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{putBidAsk.bidQuantity}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidPrice"> Bid Price</Translate>}
                      dataIndex="putBidAsk" key="putBidAskBidPrice" render={(putBidAsk , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{putBidAsk.bidPrice}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskPrice"> Ask Price</Translate>}
                      dataIndex="putBidAsk" key="putBidAskAskPrice" render={(putBidAsk , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{putBidAsk.askPrice}</div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskVolume"> Ask Volume</Translate>}
                      dataIndex="putBidAsk" key="putBidAskAskQuantity" render={(putBidAsk , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{putBidAsk.askQuantity}</div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.OpenInterest"> Open Interest</Translate>}
                      dataIndex="putStockWatch" key="putStockWatchOpenInterest" render={(putStockWatch , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{putStockWatch?.openInterest}</div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.Last"> Last</Translate>}
                      dataIndex="putStockWatch" key="putStockWatchLast" render={(putStockWatch , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{putStockWatch?.last}</div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.TradeVolume"> Trade Volume</Translate>}
                      dataIndex="putStockWatch" key="putStockWatchTradeVolume" render={(putStockWatch , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{putStockWatch?.tradeVolume}</div>
              }/>
              <Column sorter={true}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putLeverage' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.Leverage"> Leverage</Translate>}
                      dataIndex="option" key="putLeverage" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option.putLeverage}</div>
              }/>
              <Column sorter={true}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putBreakEven' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.BreakEven"> Break Even</Translate>}
                      dataIndex="option" key="putBreakEven" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option.putBreakEven}</div>

              }/>
              <Column sorter={true}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putAskToBS' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.AskPriceToBS"> Ask Price To BS</Translate>}
                      dataIndex="option" key="putAskToBS" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option.putAskToBS}</div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.EffectivePrice"> Effective Price</Translate>}
                      dataIndex="option" key="optionPutEffectivePrice" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option.putEffectivePrice}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BlackScholes30"> Black Scholes 30</Translate>}
                      dataIndex="option" key="optionPutBS30" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option.putBS30}</div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.option">Option</Translate>}
                      dataIndex="option" key="option" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>{option ? <Link to={`option/${option.id}`}>{'ض' + option.name}</Link> : ''}</div>

              }/>
            </ColumnGroup>
          </Table>
          {loading ? <Spin/> : ""}
        </div>


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
