import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './social-connection.reducer';
import { ISocialConnection } from 'app/shared/model/social-connection.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface ISocialConnectionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SocialConnection = (props: ISocialConnectionProps) => {
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

  const { socialConnectionList, match, totalItems } = props;
  return (
    <div>
      <h2 id="social-connection-heading">
        Social Connections
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Social Connection
        </Link>
      </h2>
      <div className="table-responsive">
        {socialConnectionList && socialConnectionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  User Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('proverId')}>
                  Prover Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('accessToken')}>
                  Access Token <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('restRateLimit')}>
                  Rest Rate Limit <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('graphqlRateLimit')}>
                  Graphql Rate Limit <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('accessToken1')}>
                  Access Token 1 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('accessToken2')}>
                  Access Token 2 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('accessToken3')}>
                  Access Token 3 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('accessToken4')}>
                  Access Token 4 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('publicAccessToken')}>
                  Public Access Token <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('publicAccessToken1')}>
                  Public Access Token 1 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('publicAccessToken2')}>
                  Public Access Token 2 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('publicAccessToken3')}>
                  Public Access Token 3 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('publicAccessToken4')}>
                  Public Access Token 4 <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {socialConnectionList.map((socialConnection, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${socialConnection.id}`} color="link" size="sm">
                      {socialConnection.id}
                    </Button>
                  </td>
                  <td>{socialConnection.userId}</td>
                  <td>{socialConnection.proverId}</td>
                  <td>{socialConnection.accessToken}</td>
                  <td>{socialConnection.restRateLimit}</td>
                  <td>{socialConnection.graphqlRateLimit}</td>
                  <td>{socialConnection.accessToken1}</td>
                  <td>{socialConnection.accessToken2}</td>
                  <td>{socialConnection.accessToken3}</td>
                  <td>{socialConnection.accessToken4}</td>
                  <td>{socialConnection.publicAccessToken}</td>
                  <td>{socialConnection.publicAccessToken1}</td>
                  <td>{socialConnection.publicAccessToken2}</td>
                  <td>{socialConnection.publicAccessToken3}</td>
                  <td>{socialConnection.publicAccessToken4}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${socialConnection.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${socialConnection.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${socialConnection.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          <div className="alert alert-warning">No Social Connections found</div>
        )}
      </div>
      <div className={socialConnectionList && socialConnectionList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ socialConnection }: IRootState) => ({
  socialConnectionList: socialConnection.entities,
  totalItems: socialConnection.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SocialConnection);
