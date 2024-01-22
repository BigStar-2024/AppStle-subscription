import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './bulk-automation.reducer';
import { IBulkAutomation } from 'app/shared/model/bulk-automation.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IBulkAutomationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const BulkAutomation = (props: IBulkAutomationProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  useEffect(() => {
    getAllEntities();
  }, []);

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

  const { bulkAutomationList, match, totalItems } = props;
  return (
    <div>
      <h2 id="bulk-automation-heading">
        Bulk Automations
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Bulk Automation
        </Link>
      </h2>
      <div className="table-responsive">
        {bulkAutomationList && bulkAutomationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shop')}>
                  Shop <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('automationType')}>
                  Automation Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('running')}>
                  Running <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('startTime')}>
                  Start Time <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  End Time <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('requestInfo')}>
                  Request Info <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('errorInfo')}>
                  Error Info <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('currentExecution')}>
                  Current Execution <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bulkAutomationList.map((bulkAutomation, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${bulkAutomation.id}`} color="link" size="sm">
                      {bulkAutomation.id}
                    </Button>
                  </td>
                  <td>{bulkAutomation.shop}</td>
                  <td>{bulkAutomation.automationType}</td>
                  <td>{bulkAutomation.running ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={bulkAutomation.startTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={bulkAutomation.endTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{bulkAutomation.requestInfo}</td>
                  <td>{bulkAutomation.errorInfo}</td>
                  <td>{bulkAutomation.currentExecution}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${bulkAutomation.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bulkAutomation.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bulkAutomation.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          <div className="alert alert-warning">No Bulk Automations found</div>
        )}
      </div>
      <div className={bulkAutomationList && bulkAutomationList.length > 0 ? '' : 'd-none'}>
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
    </div>
  );
};

const mapStateToProps = ({ bulkAutomation }: IRootState) => ({
  bulkAutomationList: bulkAutomation.entities,
  totalItems: bulkAutomation.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BulkAutomation);
