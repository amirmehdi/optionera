import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Spin, Table } from 'antd';
import { getSortState, Translate } from 'react-jhipster';
import { IRootState } from 'app/shared/reducers';
import { getEntities, reset, getEntitiesUpdate } from './option-stats.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import './style.scss';
import { SyncOutlined } from '@ant-design/icons';
import { SearchOptionStats } from 'app/entities/option-stats/Search-option-stats';
import DateTime from './../../DateTime/DateTime';
import Number from './../../Framework/Number';
import { Switch } from 'antd';
import { CloseOutlined, CheckOutlined } from '@ant-design/icons';
import {hasAnyAuthority} from "app/shared/auth/private-route";
import {AUTHORITIES} from "app/config/constants";

const { Column, ColumnGroup } = Table;

export interface IOptionStatsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {
}

/* eslint-disable no-var */
var intervalId: any;

export const OptionStats = (props: IOptionStatsProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));
  const [sorting, setSorting] = useState(false);
  const [instrumentId, setInstrumentId] = useState(undefined);
  const [switchId, setSwitchId] = useState(undefined);
  const [fromDate, setFromDate] = useState(undefined);
  const [toDate, setToDate] = useState(undefined);
  const [checkAutoRefresh, setCheckAutoRefresh] = useState(undefined);
  const { account } = props;

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage,
      `${paginationState.sort},${paginationState.order}`, instrumentId?.value, switchId, fromDate, toDate);
  };

  const resetAll = () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1
    });
  };

  useEffect(() => {
    localStorage.removeItem('switch');
    resetAll();
  }, []);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if (paginationState.activePage > 1) {
      setCheckAutoRefresh(undefined);
      window.clearInterval(intervalId);
      clearInterval(intervalId);
      intervalId = 0;
    }
    // eslint-disable-next-line @typescript-eslint/no-use-before-define
    if (optionStatsList && optionStatsList.length > 19) {
      if (window.pageYOffset > 0) {
        setPaginationState({
          ...paginationState,
          activePage: paginationState.activePage + 1
        });
      }
    }
  };
  const getAllEntitiesPageOne = () => {
    setPaginationState({ ...paginationState, activePage: 1 });
    props.getEntitiesUpdate(0, paginationState.itemsPerPage,
      `${paginationState.sort},${paginationState.order}`, instrumentId?.value, switchId, fromDate, toDate);
  };
  const _computeDateInJalaliFormat = (createdAt: any) => {
    const date1 = new Date(createdAt);
    const date2 = new Date();
    const timeDiff = Math.abs(date2.getTime() - date1.getTime());
    const diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

    if (createdAt) {
      const createAtDate = DateTime.stringToDate(createdAt);
      const createAtJalaliDate = DateTime.gregorianToJalali(createAtDate.year, createAtDate.month, createAtDate.day);
      return <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <p style={{ margin: 0, width: 120, textAlign: 'center' }}>
          {createAtJalaliDate.year + '/' + createAtJalaliDate.month + '/' + createAtJalaliDate.day + ' (' + diffDays + ')'}
        </p>
      </div>;
    }
    return null;
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  useEffect(() => {
    if (checkAutoRefresh === true) {
      intervalId = setInterval(() =>
        getAllEntitiesPageOne(), 15000);
    } else if (checkAutoRefresh === false) {
      window.clearInterval(intervalId);
      clearInterval(intervalId);
      intervalId = 0;
    }
  }, [checkAutoRefresh]);

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
      setCheckAutoRefresh(false);
    } else if (sorter.column === undefined) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
        order: paginationState.order === 'asc' ? 'desc' : 'asc',
        sort: sorter.columnKey
      });
      setSorting(true);
      setCheckAutoRefresh(false);
      props.reset();
    }
  };
  const getScroll = () => {
    const AutoRefresh = localStorage.getItem('switch');
    setCheckAutoRefresh(AutoRefresh === 'true');
  };
  const { optionStatsList, loading } = props;
  return (
    <div style={{ width: '100%', paddingRight: 20, paddingLeft: 20 }}>
      <InfiniteScroll
        pageStart={paginationState.activePage}
        loadMore={handleLoadMore}
        hasMore={paginationState.activePage - 1 < props.links.next}
        loader={<div className="loader"><Spin/></div>}
        threshold={0}
        initialLoad={false}
        getScrollParent={() => window.pageYOffset === 0 ? getScroll() : null}
      >
        <div className="content-search-box">
          {!checkAutoRefresh && <SyncOutlined style={{ marginRight: 10 }}
                                              onClick={() => getAllEntitiesPageOne()}/>}
          <div className="auto-refresh">
            <p style={{margin: 0}}>رفرش خودکار</p>
            <Switch
              checked={checkAutoRefresh}
              checkedChildren={<CheckOutlined/>}
              unCheckedChildren={<CloseOutlined/>}
              onChange={(e) => {
                localStorage.setItem('switch', `${e}`);
                setCheckAutoRefresh(e);
              }}
            />
          </div>

          <SearchOptionStats
            switchValue={switchId}
            instrumentValue={instrumentId}
            FromDateValue={fromDate}
            toDateValue={toDate}
            fromDateRange={(from) => {
              setFromDate(from);
            }}
            toDateRange={(to) => {
              // eslint-disable-next-line no-shadow
              const toDate = to ? fromDate : undefined;
              setToDate(to);
              fromDate && props.getEntities(0, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`
                , instrumentId?.value, switchId, toDate, to);
              fromDate && props.reset();
              setCheckAutoRefresh(false);
            }}
            instrumentId={(id) => {
              setInstrumentId(id);
              props.getEntities(0, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`, id?.value, switchId, fromDate, toDate);
              props.reset();
              setCheckAutoRefresh(false);
            }}
            switch={(id: number) => {
              setSwitchId(id);
              props.getEntities(0, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`, instrumentId?.value, id, fromDate, toDate);
              props.reset();
              setCheckAutoRefresh(false);
            }}/>
        </div>

        <div>
          <Table sticky pagination={false} onChange={handleChange} dataSource={optionStatsList}
                 scroll={{ x: 2400 }}>
            <Column
              width={110}
              fixed={'left'} title={<Translate contentKey="eTradeApp.optionStats.option">Option</Translate>}
              dataIndex="option" key="optionStats.name" render={(optionStats, row: any) =>
              <div className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                {/* {optionStats ? <Link to={`option/${optionStats.id}`}>{'ض' + optionStats.name}</Link> : ''}*/}
                {row.option ? <a target="_blank" rel="noopener noreferrer"
                                 href={`http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=${row.option.callTseId}`}>{' ' + 'ض' + row.option.name + ' '}</a> : ''}

              </div>
            }/>
            <ColumnGroup title={<Translate contentKey="eTradeApp.optionStats.callTitle">call</Translate>}>

              <Column
                title={<Translate contentKey="eTradeApp.optionStats.BlackScholes30"> Black Scholes 30</Translate>}
                dataIndex="callBS" key="callBS" render={(callBS, row: any) =>
                <div className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callBS}</Number>
                </div>
              }/>
              <Column
                title={<Translate contentKey="eTradeApp.optionStats.EffectivePrice"> Effective Price</Translate>}
                dataIndex="callEffectivePrice" key="callEffectivePrice" render={(callEffectivePrice, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callEffectivePrice}</Number></div>
              }/>
              {hasAnyAuthority(account.authorities, [AUTHORITIES.ADMIN,AUTHORITIES.GOLDEN]) ?
                <Column
                  title={<Translate contentKey="eTradeApp.optionStats.FinalPrice">FinalPrice</Translate>}
                  dataIndex="callFinalPrice" key="callFinalPrice" render={(callFinalPrice, row: any) =>
                  <div
                    className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                    <Number>{callFinalPrice}</Number></div>
                }/> : null
              }
              <Column
                sorter={true}
                width={110}
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
                width={70}
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
              {hasAnyAuthority(account.authorities, [AUTHORITIES.ADMIN]) ?
                <Column
                  width={80}
                  sorter={true}
                  sortDirections={['ascend', 'descend']}
                  sortOrder={paginationState.sort === 'callMargin' ?
                    paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                  showSorterTooltip={false}
                  title={<Translate contentKey="eTradeApp.optionStats.Margin">Margin</Translate>}
                  dataIndex="option" key="callMargin" render={(option, row: any) =>
                  <div
                    className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                    <Number>{option.callMargin}</Number></div>
                }/> : null
              }
              <Column
                width={90}
                sorter={true}
                sortDirections={['ascend', 'descend']}
                sortOrder={paginationState.sort === 'callIndifference' ?
                  paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                showSorterTooltip={false}
                title={<Translate contentKey="eTradeApp.optionStats.Indifference">Indifference</Translate>}
                dataIndex="option" key="callIndifference" render={(option, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.callIndifference}</Number></div>
              }/>
              {hasAnyAuthority(account.authorities, [AUTHORITIES.ADMIN,AUTHORITIES.GOLDEN]) ?
                <>
                  <Column
                    width={75}
                    sorter={true}
                    sortDirections={['ascend', 'descend']}
                    sortOrder={paginationState.sort === 'callHedge' ?
                      paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                    showSorterTooltip={false}
                    title={<Translate contentKey="eTradeApp.optionStats.Hedge">Hedge</Translate>}
                    dataIndex="option" key="callHedge" render={(option, row: any) =>
                    <div
                      className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                      <Number>{option.callHedge}</Number></div>
                  }/>
                  <Column
                    width={90}
                    sorter={true}
                    sortDirections={['ascend', 'descend']}
                    sortOrder={paginationState.sort === 'callGain' ?
                      paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                    showSorterTooltip={false}
                    title={<Translate contentKey="eTradeApp.optionStats.Gain">Gain</Translate>}
                    dataIndex="option" key="callGain" render={(option, row: any) =>
                    <div
                      className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                      <Number>{option.callGain}</Number></div>
                  }/>
                  <Column
                    width={90}
                    sorter={true}
                    sortDirections={['ascend', 'descend']}
                    sortOrder={paginationState.sort === 'callGainMonthly' ?
                      paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                    showSorterTooltip={false}
                    title={<Translate contentKey="eTradeApp.optionStats.GainMonthly">GainMonthly</Translate>}
                    dataIndex="option" key="callGainMonthly" render={(option, row: any) =>
                    <div
                      className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                      <Number>{option.callGainMonthly}</Number></div>
                  }/>
                </> : null
              }
              <Column
                width={70}
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
              <Column
                width={70}
                sorter={true}
                sortDirections={['ascend', 'descend']}
                sortOrder={paginationState.sort === 'callTradeVolume' ?
                  paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                showSorterTooltip={false}
                title={<Translate contentKey="eTradeApp.optionStats.TradeVolume"> Trade Volume</Translate>}
                dataIndex="option" key="callTradeVolume" render={(option, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.callTradeVolume}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.Last"> Last</Translate>}
                      dataIndex="callStockWatch" key="callStockWatchLast" render={(callStockWatch, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callStockWatch?.last}</Number></div>
              }/>
              { /* <Column title={<Translate contentKey="eTradeApp.optionStats.OpenInterest"> Open Interest</Translate>}
                      dataIndex="callStockWatch" key="callStockWatchOpenInterest" render={(callStockWatch, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callStockWatch?.openInterest}</Number></div>
              }/>*/ }
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidVolume"> Bid Volume</Translate>}
                      dataIndex="callBidAsk" key="callBidAskBidQuantity" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callBidAsk.bidQuantity}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidPrice"> Bid Price</Translate>}
                      dataIndex="callBidAsk" key="callBidAskBidPrice" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callBidAsk.bidPrice}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskPrice"> Ask Price</Translate>}
                      dataIndex="callBidAsk" key="callBidAskAskPrice" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callBidAsk.askPrice}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskVolume"> Ask Volume</Translate>}
                      dataIndex="callBidAsk" key="callBidAskAskQuantity" render={(callBidAsk, row: any) =>
                <div
                  className={`padding-col ${row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{callBidAsk.askQuantity}</Number></div>
              }/>

            </ColumnGroup>
            <Column className="bg-color-gray" sorter={true}
                    sortDirections={['ascend', 'descend']}
                    sortOrder={paginationState.sort === 'strikePrice' ?
                      paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                    showSorterTooltip={false}
                    title={<Translate contentKey="eTradeApp.option.strikePrice">Strike Price</Translate>}
                    dataIndex="option" key="strikePrice" render={(option) =>
              <div className={`padding-col`}><Number>{option.strikePrice}</Number></div>
            }/>
            <Column className="bg-color-gray"
                    width={180}
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
                    width={90}
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
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{putBidAsk.bidQuantity}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BidPrice"> Bid Price</Translate>}
                      dataIndex="putBidAsk" key="putBidAskBidPrice" render={(putBidAsk, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{putBidAsk.bidPrice}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskPrice"> Ask Price</Translate>}
                      dataIndex="putBidAsk" key="putBidAskAskPrice" render={(putBidAsk, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{putBidAsk.askPrice}</Number></div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.AskVolume"> Ask Volume</Translate>}
                      dataIndex="putBidAsk" key="putBidAskAskQuantity" render={(putBidAsk, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{putBidAsk.askQuantity}</Number></div>

              }/>
              { /* <Column title={<Translate contentKey="eTradeApp.optionStats.OpenInterest"> Open Interest</Translate>}
                      dataIndex="putStockWatch" key="putStockWatchOpenInterest" render={(putStockWatch, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{putStockWatch?.openInterest}</Number></div>

              }/>*/ }
              <Column title={<Translate contentKey="eTradeApp.optionStats.Last"> Last</Translate>}
                      dataIndex="putStockWatch" key="putStockWatchLast" render={(putStockWatch, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{putStockWatch?.last}</Number></div>

              }/>
              <Column
                width={70}
                sorter={true}
                sortDirections={['ascend', 'descend']}
                sortOrder={paginationState.sort === 'putTradeVolume' ?
                  paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                showSorterTooltip={false}
                title={<Translate contentKey="eTradeApp.optionStats.TradeVolume"> Trade Volume</Translate>}
                dataIndex="option" key="putTradeVolume" render={(option, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.putTradeVolume}</Number></div>
              }/>
              <Column sorter={true}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putLeverage' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.Leverage"> Leverage</Translate>}
                      dataIndex="option" key="putLeverage" render={(option, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.putLeverage}</Number></div>
              }/>
              <Column sorter={true}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putBreakEven' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.BreakEven"> Break Even</Translate>}
                      dataIndex="option" key="putBreakEven" render={(option, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.putBreakEven}</Number></div>

              }/>
              <Column sorter={true}
                      width={120}
                      sortDirections={['ascend', 'descend']}
                      sortOrder={paginationState.sort === 'putAskToBS' ?
                        paginationState.order === 'asc' ? 'descend' : 'ascend' : undefined}
                      showSorterTooltip={false}
                      title={<Translate contentKey="eTradeApp.optionStats.AskPriceToBS"> Ask Price To BS</Translate>}
                      dataIndex="option" key="putAskToBS" render={(option, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{option.putAskToBS}</Number></div>

              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.EffectivePrice"> Effective Price</Translate>}
                      dataIndex="putEffectivePrice" key="putEffectivePrice" render={(putEffectivePrice, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{putEffectivePrice}</Number></div>
              }/>
              <Column title={<Translate contentKey="eTradeApp.optionStats.BlackScholes30"> Black Scholes 30</Translate>}
                      dataIndex="putBS" key="putBS" render={(putBS, row: any) =>
                <div
                  className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  <Number>{putBS}</Number></div>
              }/>
              <Column
                width={110}
                title={<Translate contentKey="eTradeApp.optionStats.option">Option</Translate>}
                dataIndex="option" key="option" render={(option, row: any) =>
                <div className={`padding-col ${!row.option.callInTheMoney ? 'bg-blue-table' : ''}`}>
                  {/* {option ? <Link to={`option/${option.id}`}>{'ط' + option.name}</Link> : ''}*/}
                  {option ? <a target="_blank" rel="noopener noreferrer"
                               href={`http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=${option.putTseId}`}>{' ' + 'ط' + option.name + ' '}</a> : ''}

                </div>

              }/>
            </ColumnGroup>
          </Table>
          {loading ? <Spin/> : null}
        </div>
      </InfiniteScroll>
    </div>
  );
};

const mapStateToProps = storeState => ({
  optionStatsList: storeState.optionStats.entities,
  loading: storeState.optionStats.loading,
  totalItems: storeState.optionStats.totalItems,
  links: storeState.optionStats.links,
  entity: storeState.optionStats.entity,
  updateSuccess: storeState.optionStats.updateSuccess,
  account: storeState.authentication.account
});

const mapDispatchToProps = {
  getEntitiesUpdate,
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OptionStats);
