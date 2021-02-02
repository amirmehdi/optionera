import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './bourse-code.reducer';
import { IBourseCode } from 'app/shared/model/bourse-code.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IBourseCodeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const BourseCode = (props: IBourseCodeProps) => {
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

  const { bourseCodeList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="bourse-code-heading">
        <Translate contentKey="eTradeApp.bourseCode.home.title">Bourse Codes</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="eTradeApp.bourseCode.home.createLabel">Create new Bourse Code</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {bourseCodeList && bourseCodeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('broker')}>
                  <Translate contentKey="eTradeApp.bourseCode.broker">Broker</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="eTradeApp.bourseCode.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="eTradeApp.bourseCode.code">Code</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('username')}>
                  <Translate contentKey="eTradeApp.bourseCode.username">Username</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('password')}>
                  <Translate contentKey="eTradeApp.bourseCode.password">Password</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('buyingPower')}>
                  <Translate contentKey="eTradeApp.bourseCode.buyingPower">Buying Power</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('blocked')}>
                  <Translate contentKey="eTradeApp.bourseCode.blocked">Blocked</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('remain')}>
                  <Translate contentKey="eTradeApp.bourseCode.remain">Remain</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('credit')}>
                  <Translate contentKey="eTradeApp.bourseCode.credit">Credit</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('conditions')}>
                  <Translate contentKey="eTradeApp.bourseCode.conditions">Conditions</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bourseCodeList.map((bourseCode, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${bourseCode.id}`} color="link" size="sm">
                      {bourseCode.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`eTradeApp.Broker.${bourseCode.broker}`} />
                  </td>
                  <td>{bourseCode.name}</td>
                  <td>{bourseCode.code}</td>
                  <td>{bourseCode.username}</td>
                  <td>{bourseCode.password}</td>
                  <td>{bourseCode.buyingPower}</td>
                  <td>{bourseCode.blocked}</td>
                  <td>{bourseCode.remain}</td>
                  <td>{bourseCode.credit}</td>
                  <td>{bourseCode.conditions}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${bourseCode.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bourseCode.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`${match.url}/${bourseCode.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="eTradeApp.bourseCode.home.notFound">No Bourse Codes found</Translate>
            </div>
          )
        )}
      </div>
      <div className={bourseCodeList && bourseCodeList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ bourseCode }: IRootState) => ({
  bourseCodeList: bourseCode.entities,
  loading: bourseCode.loading,
  totalItems: bourseCode.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BourseCode);
