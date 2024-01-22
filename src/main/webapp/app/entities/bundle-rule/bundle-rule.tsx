import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './bundle-rule.reducer';
import { IBundleRule } from 'app/shared/model/bundle-rule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
// vvv Import does not exist
// import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IBundleRuleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const BundleRule = (props: IBundleRuleProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE.toString()) as IPaginationBaseState);

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

  const { bundleRuleList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="bundle-rule-heading">
        Bundle Rules
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Bundle Rule
        </Link>
      </h2>
      <div className="table-responsive">
        {bundleRuleList && bundleRuleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shop')}>
                  Shop <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('title')}>
                  Title <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('priceSummary')}>
                  Price Summary <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('actionButtonText')}>
                  Action Button Text <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('actionButtonDescription')}>
                  Action Button Description <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('showBundleWidget')}>
                  Show Bundle Widget <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('customerIncludeTags')}>
                  Customer Include Tags <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  Start Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  End Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('discountType')}>
                  Discount Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('discountValue')}>
                  Discount Value <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('bundleLevel')}>
                  Bundle Level <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('products')}>
                  Products <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('variants')}>
                  Variants <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('discountCondition')}>
                  Discount Condition <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('sequenceNo')}>
                  Sequence No <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('bundleType')}>
                  Bundle Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('showCombinedSellingPlan')}>
                  Show Combined Selling Plan <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('selectSubscriptionByDefault')}>
                  Select Subscription By Default <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('minimumNumberOfItems')}>
                  Minimum Number Of Items <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('maximumNumberOfItems')}>
                  Maximum Number Of Items <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('maxQuantity')}>
                  Max Quantity <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bundleRuleList.map((bundleRule, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${bundleRule.id}`} color="link" size="sm">
                      {bundleRule.id}
                    </Button>
                  </td>
                  <td>{bundleRule.shop}</td>
                  <td>{bundleRule.name}</td>
                  <td>{bundleRule.title}</td>
                  <td>{bundleRule.description}</td>
                  <td>{bundleRule.priceSummary}</td>
                  <td>{bundleRule.actionButtonText}</td>
                  <td>{bundleRule.actionButtonDescription}</td>
                  <td>{bundleRule.status}</td>
                  <td>{bundleRule.showBundleWidget ? 'true' : 'false'}</td>
                  <td>{bundleRule.customerIncludeTags}</td>
                  <td>{bundleRule.startDate ? <TextFormat type="date" value={bundleRule.startDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{bundleRule.endDate ? <TextFormat type="date" value={bundleRule.endDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{bundleRule.discountType}</td>
                  <td>{bundleRule.discountValue}</td>
                  <td>{bundleRule.bundleLevel}</td>
                  <td>{bundleRule.products}</td>
                  <td>{bundleRule.variants}</td>
                  <td>{bundleRule.discountCondition}</td>
                  <td>{bundleRule.sequenceNo}</td>
                  <td>{bundleRule.bundleType}</td>
                  <td>{bundleRule.showCombinedSellingPlan ? 'true' : 'false'}</td>
                  <td>{bundleRule.selectSubscriptionByDefault ? 'true' : 'false'}</td>
                  <td>{bundleRule.minimumNumberOfItems}</td>
                  <td>{bundleRule.maximumNumberOfItems}</td>
                  <td>{bundleRule.maxQuantity}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${bundleRule.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bundleRule.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bundleRule.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Bundle Rules found</div>
        )}
      </div>
      {props.totalItems ? (
        <div className={bundleRuleList && bundleRuleList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ bundleRule }: IRootState) => ({
  bundleRuleList: bundleRule.entities,
  loading: bundleRule.loading,
  totalItems: bundleRule.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleRule);
