import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './activity-updates-settings.reducer';
import { IActivityUpdatesSettings } from 'app/shared/model/activity-updates-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
// vvv Import does not exist
// import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IActivityUpdatesSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ActivityUpdatesSettings = (props: IActivityUpdatesSettingsProps) => {
  const [paginationState, setPaginationState] = useState(
    getSortState(props.location, ITEMS_PER_PAGE.toString()) as IPaginationBaseState
  );

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

  const { activityUpdatesSettingsList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="activity-updates-settings-heading">
        Activity Updates Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Activity Updates Settings
        </Link>
      </h2>
      <div className="table-responsive">
        {activityUpdatesSettingsList && activityUpdatesSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shop')}>
                  Shop <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('summaryReportEnabled')}>
                  Summary Report Enabled <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('summaryReportFrequency')}>
                  Summary Report Frequency <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('summaryReportDeliverToEmail')}>
                  Summary Report Deliver To Email <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('summaryReportTimePeriod')}>
                  Summary Report Time Period <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('summaryReportLastSent')}>
                  Summary Report Last Sent <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('summaryReportProcessing')}>
                  Summary Report Processing <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {activityUpdatesSettingsList.map((activityUpdatesSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${activityUpdatesSettings.id}`} color="link" size="sm">
                      {activityUpdatesSettings.id}
                    </Button>
                  </td>
                  <td>{activityUpdatesSettings.shop}</td>
                  <td>{activityUpdatesSettings.summaryReportEnabled ? 'true' : 'false'}</td>
                  <td>{activityUpdatesSettings.summaryReportFrequency}</td>
                  <td>{activityUpdatesSettings.summaryReportDeliverToEmail}</td>
                  <td>{activityUpdatesSettings.summaryReportTimePeriod}</td>
                  <td>
                    {activityUpdatesSettings.summaryReportLastSent ? (
                      <TextFormat type="date" value={activityUpdatesSettings.summaryReportLastSent} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{activityUpdatesSettings.summaryReportProcessing ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${activityUpdatesSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${activityUpdatesSettings.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${activityUpdatesSettings.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Activity Updates Settings found</div>
        )}
      </div>
      {props.totalItems ? (
        <div className={activityUpdatesSettingsList && activityUpdatesSettingsList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ activityUpdatesSettings }: IRootState) => ({
  activityUpdatesSettingsList: activityUpdatesSettings.entities,
  loading: activityUpdatesSettings.loading,
  totalItems: activityUpdatesSettings.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActivityUpdatesSettings);
