import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Row, Table} from 'reactstrap';
import {getSortState, JhiItemCount, JhiPagination, TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntities} from './board.reducer';
import {APP_DATE_FORMAT} from 'app/config/constants';
import {ITEMS_PER_PAGE} from 'app/shared/util/pagination.constants';

export interface IBoardProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Board = (props: IBoardProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    props.history.push(
      `${props.location.pathname}?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
    );
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

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

  const { boardList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="board-heading">
        <Translate contentKey="eTradeApp.board.home.title">Boards</Translate>
      </h2>
      <div className="table-responsive">
        {boardList && boardList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('isin')}>
                  <Translate contentKey="eTradeApp.board.isin">Isin</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="eTradeApp.board.date">Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('last')}>
                  <Translate contentKey="eTradeApp.board.last">Last</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('close')}>
                  <Translate contentKey="eTradeApp.board.close">Close</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('first')}>
                  <Translate contentKey="eTradeApp.board.first">First</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('low')}>
                  <Translate contentKey="eTradeApp.board.low">Low</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('high')}>
                  <Translate contentKey="eTradeApp.board.high">High</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tradeCount')}>
                  <Translate contentKey="eTradeApp.board.tradeCount">Trade Count</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tradeVolume')}>
                  <Translate contentKey="eTradeApp.board.tradeVolume">Trade Volume</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tradeValue')}>
                  <Translate contentKey="eTradeApp.board.tradeValue">Trade Value</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('askPrice')}>
                  <Translate contentKey="eTradeApp.board.askPrice">Ask Price</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('askVolume')}>
                  <Translate contentKey="eTradeApp.board.askVolume">Ask Volume</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('bidPrice')}>
                  <Translate contentKey="eTradeApp.board.bidPrice">Bid Price</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('bidVolume')}>
                  <Translate contentKey="eTradeApp.board.bidVolume">Bid Volume</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('individualBuyVolume')}>
                  <Translate contentKey="eTradeApp.board.individualBuyVolume">Individual Buy Volume</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('individualSellVolume')}>
                  <Translate contentKey="eTradeApp.board.individualSellVolume">Individual Sell Volume</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('legalBuyVolume')}>
                  <Translate contentKey="eTradeApp.board.legalBuyVolume">Legal Buy Volume</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('legalSellVolume')}>
                  <Translate contentKey="eTradeApp.board.legalSellVolume">Legal Sell Volume</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('referencePrice')}>
                  <Translate contentKey="eTradeApp.board.referencePrice">Reference Price</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {boardList.map((board, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${board.isin}`} color="link" size="sm">
                      {board.isin}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={board.date} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{board.last}</td>
                  <td>{board.close}</td>
                  <td>{board.first}</td>
                  <td>{board.low}</td>
                  <td>{board.high}</td>
                  <td>{board.tradeCount}</td>
                  <td>{board.tradeVolume}</td>
                  <td>{board.tradeValue}</td>
                  <td>{board.askPrice}</td>
                  <td>{board.askVolume}</td>
                  <td>{board.bidPrice}</td>
                  <td>{board.bidVolume}</td>
                  <td>{board.individualBuyVolume}</td>
                  <td>{board.individualSellVolume}</td>
                  <td>{board.legalBuyVolume}</td>
                  <td>{board.legalSellVolume}</td>
                  <td>{board.referencePrice}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="eTradeApp.board.home.notFound">No Boards found</Translate>
            </div>
          )
        )}
      </div>
      <div className={boardList && boardList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ board }: IRootState) => ({
  boardList: board.entities,
  loading: board.loading,
  totalItems: board.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Board);
