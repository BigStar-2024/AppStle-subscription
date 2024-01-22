import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './async-update-event-processing.reducer';
import { IAsyncUpdateEventProcessing } from 'app/shared/model/async-update-event-processing.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
// import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IAsyncUpdateEventProcessingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const AsyncUpdateEventProcessing = (props: IAsyncUpdateEventProcessingProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get('sort');
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const { asyncUpdateEventProcessingList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="async-update-event-processing-heading">
        Async Update Event Processings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Async Update Event Processing
        </Link>
      </h2>
      <div className="table-responsive">
        {asyncUpdateEventProcessingList && asyncUpdateEventProcessingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('subscriptionContractId')}>
                  Subscription Contract Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastUpdated')}>
                  Last Updated <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tagModelJson')}>
                  Tag Model Json <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('firstTimeOrderTags')}>
                  First Time Order Tags <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {asyncUpdateEventProcessingList.map((asyncUpdateEventProcessing, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${asyncUpdateEventProcessing.id}`} color="link" size="sm">
                      {asyncUpdateEventProcessing.id}
                    </Button>
                  </td>
                  <td>{asyncUpdateEventProcessing.subscriptionContractId}</td>
                  <td>
                    {asyncUpdateEventProcessing.lastUpdated ? (
                      <TextFormat type="date" value={asyncUpdateEventProcessing.lastUpdated} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{asyncUpdateEventProcessing.tagModelJson}</td>
                  <td>{asyncUpdateEventProcessing.firstTimeOrderTags}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${asyncUpdateEventProcessing.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${asyncUpdateEventProcessing.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${asyncUpdateEventProcessing.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Async Update Event Processings found</div>
        )}
      </div>
      {props.totalItems ? (
        <div className={asyncUpdateEventProcessingList && asyncUpdateEventProcessingList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
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
      ) : (
        ''
      )}
    </div>
  );
};

const mapStateToProps = ({ asyncUpdateEventProcessing }: IRootState) => ({
  asyncUpdateEventProcessingList: asyncUpdateEventProcessing.entities,
  loading: asyncUpdateEventProcessing.loading,
  totalItems: asyncUpdateEventProcessing.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AsyncUpdateEventProcessing);
