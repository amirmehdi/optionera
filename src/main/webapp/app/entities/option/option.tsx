import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './option.reducer';
import { IOption } from 'app/shared/model/option.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IOptionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Option = (props: IOptionProps) => {
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

  const { optionList, match, loading } = props;
  return (
    <div>
      <h2 id="option-heading">
        <Translate contentKey="eTradeApp.option.home.title">Options</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="eTradeApp.option.home.createLabel">Create new Option</Translate>
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
          {optionList && optionList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('name')}>
                    <Translate contentKey="eTradeApp.option.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callIsin')}>
                    <Translate contentKey="eTradeApp.option.callIsin">Call Isin</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('putIsin')}>
                    <Translate contentKey="eTradeApp.option.putIsin">Put Isin</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('expDate')}>
                    <Translate contentKey="eTradeApp.option.expDate">Exp Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('strikePrice')}>
                    <Translate contentKey="eTradeApp.option.strikePrice">Strike Price</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('contractSize')}>
                    <Translate contentKey="eTradeApp.option.contractSize">Contract Size</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callInTheMoney')}>
                    <Translate contentKey="eTradeApp.option.callInTheMoney">Call In The Money</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callBreakEven')}>
                    <Translate contentKey="eTradeApp.option.callBreakEven">Call Break Even</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('putBreakEven')}>
                    <Translate contentKey="eTradeApp.option.putBreakEven">Put Break Even</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callAskToBS')}>
                    <Translate contentKey="eTradeApp.option.callAskToBS">Call Ask To BS</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('putAskToBS')}>
                    <Translate contentKey="eTradeApp.option.putAskToBS">Put Ask To BS</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callLeverage')}>
                    <Translate contentKey="eTradeApp.option.callLeverage">Call Leverage</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('putLeverage')}>
                    <Translate contentKey="eTradeApp.option.putLeverage">Put Leverage</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callHedge')}>
                    <Translate contentKey="eTradeApp.option.callHedge">Call Hedge</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callIndifference')}>
                    <Translate contentKey="eTradeApp.option.callIndifference">Call Indifference</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callGain')}>
                    <Translate contentKey="eTradeApp.option.callGain">Call Gain</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callGainMonthly')}>
                    <Translate contentKey="eTradeApp.option.callGainMonthly">Call Gain Monthly</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callMargin')}>
                    <Translate contentKey="eTradeApp.option.callMargin">Call Margin</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('putMargin')}>
                    <Translate contentKey="eTradeApp.option.putMargin">Put Margin</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('callTradeVolume')}>
                    <Translate contentKey="eTradeApp.option.callTradeVolume">Call Trade Volume</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('putTradeVolume')}>
                    <Translate contentKey="eTradeApp.option.putTradeVolume">Put Trade Volume</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="eTradeApp.option.instrument">Instrument</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {optionList.map((option, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${option.id}`} color="link" size="sm">
                        {option.id}
                      </Button>
                    </td>
                    <td>{option.name}</td>
                    <td>{option.callIsin}</td>
                    <td>{option.putIsin}</td>
                    <td>
                      <TextFormat type="date" value={option.expDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{option.strikePrice}</td>
                    <td>{option.contractSize}</td>
                    <td>{option.callInTheMoney ? 'true' : 'false'}</td>
                    <td>{option.callBreakEven}</td>
                    <td>{option.putBreakEven}</td>
                    <td>{option.callAskToBS}</td>
                    <td>{option.putAskToBS}</td>
                    <td>{option.callLeverage}</td>
                    <td>{option.putLeverage}</td>
                    <td>{option.callHedge}</td>
                    <td>{option.callIndifference}</td>
                    <td>{option.callGain}</td>
                    <td>{option.callGainMonthly}</td>
                    <td>{option.callMargin}</td>
                    <td>{option.putMargin}</td>
                    <td>{option.callTradeVolume}</td>
                    <td>{option.putTradeVolume}</td>
                    <td>{option.instrument ? <Link to={`instrument/${option.instrument.isin}`}>{option.instrument.name}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${option.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${option.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${option.id}/delete`} color="danger" size="sm">
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
                <Translate contentKey="eTradeApp.option.home.notFound">No Options found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

const mapStateToProps = ({ option }: IRootState) => ({
  optionList: option.entities,
  loading: option.loading,
  totalItems: option.totalItems,
  links: option.links,
  entity: option.entity,
  updateSuccess: option.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Option);
