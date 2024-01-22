import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './appstle-menu-labels.reducer';
import { IAppstleMenuLabels } from 'app/shared/model/appstle-menu-labels.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
// vvv Import does not exist
// import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IAppstleMenuLabelsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const AppstleMenuLabels = (props: IAppstleMenuLabelsProps) => {
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

  const { appstleMenuLabelsList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="appstle-menu-labels-heading">
        Appstle Menu Labels
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Appstle Menu Labels
        </Link>
      </h2>
      <div className="table-responsive">
        {appstleMenuLabelsList && appstleMenuLabelsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shop')}>
                  Shop <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('customCss')}>
                  Custom Css <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('labels')}>
                  Labels <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('seeMore')}>
                  See More <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('noDataFound')}>
                  No Data Found <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('productDetails')}>
                  Product Details <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('editQuantity')}>
                  Edit Quantity <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('addToCart')}>
                  Add To Cart <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('productAddedSuccessfully')}>
                  Product Added Successfully <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('wentWrong')}>
                  Went Wrong <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('results')}>
                  Results <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('adding')}>
                  Adding <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('subscribe')}>
                  Subscribe <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('notAvailable')}>
                  Not Available <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {appstleMenuLabelsList.map((appstleMenuLabels, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${appstleMenuLabels.id}`} color="link" size="sm">
                      {appstleMenuLabels.id}
                    </Button>
                  </td>
                  <td>{appstleMenuLabels.shop}</td>
                  <td>{appstleMenuLabels.customCss}</td>
                  <td>{appstleMenuLabels.labels}</td>
                  <td>{appstleMenuLabels.seeMore}</td>
                  <td>{appstleMenuLabels.noDataFound}</td>
                  <td>{appstleMenuLabels.productDetails}</td>
                  <td>{appstleMenuLabels.editQuantity}</td>
                  <td>{appstleMenuLabels.addToCart}</td>
                  <td>{appstleMenuLabels.productAddedSuccessfully}</td>
                  <td>{appstleMenuLabels.wentWrong}</td>
                  <td>{appstleMenuLabels.results}</td>
                  <td>{appstleMenuLabels.adding}</td>
                  <td>{appstleMenuLabels.subscribe}</td>
                  <td>{appstleMenuLabels.notAvailable}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${appstleMenuLabels.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${appstleMenuLabels.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${appstleMenuLabels.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Appstle Menu Labels found</div>
        )}
      </div>
      {props.totalItems ? (
        <div className={appstleMenuLabelsList && appstleMenuLabelsList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ appstleMenuLabels }: IRootState) => ({
  appstleMenuLabelsList: appstleMenuLabels.entities,
  loading: appstleMenuLabels.loading,
  totalItems: appstleMenuLabels.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AppstleMenuLabels);
