import React, {useEffect, useState} from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import {connect} from 'react-redux';
import {RouteComponentProps} from 'react-router-dom';
import {Spin, Table, Divider} from 'antd';
import {getSortState, Translate} from 'react-jhipster';
import {IRootState} from 'app/shared/reducers';
import {getEntities, reset} from './option-stats.reducer';
import {ITEMS_PER_PAGE} from 'app/shared/util/pagination.constants';
import './style.scss';
import {SyncOutlined} from '@ant-design/icons';
import {SearchOptionStats} from 'app/entities/option-stats/Search-option-stats';
import DateTime from "./../../DateTime/DateTime"
import Number from "./../../Framework/Number"
const {Column, ColumnGroup} = Table;

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
      const date1 = new Date(createdAt);
      const date2 = new Date();
      const timeDiff = Math.abs(date2.getTime() - date1.getTime());
      const diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

    if (createdAt) {
      const createAtDate = DateTime.stringToDate(createdAt);
      const createAtJalaliDate = DateTime.gregorianToJalali(createAtDate.year, createAtDate.month, createAtDate.day);
      return  <div style={{display: "flex", alignItems: "center",
        justifyContent: "center"}}><p style={{margin: 0, width:110 , textAlign: "center"}}>{DateTime.monthNames[createAtJalaliDate.month - 1]  + ' - ' + createAtJalaliDate.day + ' - ' + createAtJalaliDate.year}</p>
        <Divider type="vertical" />
        {diffDays}
      </div>
    }
    return null
  };

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
                    dataIndex="option" key="optionStats.name" render={(optionStats, row: any) =>
              <div className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                {/* {optionStats ? <Link to={`option/${optionStats.id}`}>{'ض' + optionStats.name}</Link> : ''}*/}
                {row.option ? <a target="_blank" rel="noopener noreferrer"
                                 href={`http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=${row.option.callTseId}`}>{'ض' + row.option.name}</a> : ''}

              </div>
            }/>
            <ColumnGroup title={<Translate contentKey="eTradeApp.optionStats.callTitle">call</Translate>}>

              <Column
                title={<Translate contentKey="eTradeApp.optionStats.BlackScholes30"> Black Scholes 30</Translate>}
                dataIndex="callBS30" key="callBS30" render={(callBS30, row: any) =>
                <div className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callBS30}</Number>
                </div>
              }/>
              <Column
                title={<Translate contentKey="eTradeApp.optionStats.EffectivePrice"> Effective Price</Translate>}
                dataIndex="callEffectivePrice" key="callEffectivePrice" render={(callEffectivePrice, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callEffectivePrice}</Number></div>
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
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.callAskToBS}</Number></div>
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
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.callBreakEven}</Number></div>
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
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.callLeverage}</Number></div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.TradeVolume"> Trade Volume</Translate>}
                      dataIndex="callStockWatch" key="callStockWatchTradeVolume" render={(callStockWatch, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callStockWatch?.tradeVolume}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.Last"> Last</Translate>}
                      dataIndex="callStockWatch" key="callStockWatchLast" render={(callStockWatch, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{callStockWatch?.last}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.OpenInterest"> Open Interest</Translate>}
                      dataIndex="callStockWatch" key="callStockWatchOpenInterest" render={(callStockWatch, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{callStockWatch?.openInterest}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidVolume"> Bid Volume</Translate>}
                      dataIndex="callBidAsk" key="callBidAskBidQuantity" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{callBidAsk.bidQuantity}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidPrice"> Bid Price</Translate>}
                      dataIndex="callBidAsk" key="callBidAskBidPrice" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{callBidAsk.bidPrice}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskPrice"> Ask Price</Translate>}
                      dataIndex="callBidAsk" key="callBidAskAskPrice" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{callBidAsk.askPrice}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskVolume"> Ask Volume</Translate>}
                      dataIndex="callBidAsk" key="callBidAskAskQuantity" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{callBidAsk.askQuantity}</Number></div>
              }/>

            </ColumnGroup>
            <Column className="bg-color-gray" sorter={true}
                    sortDirections={['ascend', 'descend']}
                    sortOrder={paginationState.sort === 'strikePrice' ?
                      paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                    showSorterTooltip={false}
                    title={<Translate contentKey="eTradeApp.option.strikePrice">Strike Price</Translate>}
                    dataIndex="option" key="strikePrice" render={(option) =>
              <div className={`padding-col`}> <Number>{option.strikePrice}</Number></div>
            }/>
            <Column className="bg-color-gray"
                    width={210}
                    sorter={true}
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
                {option.instrument ? <a target="_blank" rel="noopener noreferrer"
                                        href={`http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=${option.instrument.tseId}`}>{option.instrument.name} {record.baseStockWatch?.last}</a> : ''}

              </div>
            }/>
            <ColumnGroup title={<Translate contentKey="eTradeApp.optionStats.putTitle">put</Translate>}>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidVolume"> Bid Volume</Translate>}
                      dataIndex="putBidAsk" key="putBidAskBidQuantity" render={(putBidAsk, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{putBidAsk.bidQuantity}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidPrice"> Bid Price</Translate>}
                      dataIndex="putBidAsk" key="putBidAskBidPrice" render={(putBidAsk, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{putBidAsk.bidPrice}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskPrice"> Ask Price</Translate>}
                      dataIndex="putBidAsk" key="putBidAskAskPrice" render={(putBidAsk , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{putBidAsk.askPrice}</Number></div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskVolume"> Ask Volume</Translate>}
                      dataIndex="putBidAsk" key="putBidAskAskQuantity" render={(putBidAsk , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{putBidAsk.askQuantity}</Number></div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.OpenInterest"> Open Interest</Translate>}
                      dataIndex="putStockWatch" key="putStockWatchOpenInterest" render={(putStockWatch , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{putStockWatch?.openInterest}</Number></div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.Last"> Last</Translate>}
                      dataIndex="putStockWatch" key="putStockWatchLast" render={(putStockWatch , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{putStockWatch?.last}</Number></div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.TradeVolume"> Trade Volume</Translate>}
                      dataIndex="putStockWatch" key="putStockWatchTradeVolume" render={(putStockWatch , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{putStockWatch?.tradeVolume}</Number></div>
              }/>
              <Column sorter={true}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putLeverage' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.Leverage"> Leverage</Translate>}
                      dataIndex="option" key="putLeverage" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{option.putLeverage}</Number></div>
              }/>
              <Column sorter={true}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putBreakEven' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.BreakEven"> Break Even</Translate>}
                      dataIndex="option" key="putBreakEven" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{option.putBreakEven}</Number></div>

              }/>
              <Column sorter={true}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putAskToBS' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.AskPriceToBS"> Ask Price To BS</Translate>}
                      dataIndex="option" key="putAskToBS" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{option.putAskToBS}</Number></div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.EffectivePrice"> Effective Price</Translate>}
                      dataIndex="option" key="optionPutEffectivePrice" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{option.putEffectivePrice}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BlackScholes30"> Black Scholes 30</Translate>}
                      dataIndex="option" key="optionPutBS30" render={(option , row:any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}> <Number>{option.putBS30}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.option">Option</Translate>}
                      dataIndex="option" key="option" render={(option, row: any) =>
                <div className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  {/* {option ? <Link to={`option/${option.id}`}>{'ض' + option.name}</Link> : ''}*/}
                  {option ? <a target="_blank" rel="noopener noreferrer"
                               href={`http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=${option.putTseId}`}>{'ط' + option.name}</a> : ''}

                </div>

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
