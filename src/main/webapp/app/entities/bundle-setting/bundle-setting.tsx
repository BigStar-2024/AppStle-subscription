import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './bundle-setting.reducer';
import { IBundleSetting } from 'app/shared/model/bundle-setting.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
// vvv Import does not exist
// import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IBundleSettingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const BundleSetting = (props: IBundleSettingProps) => {
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

  const { bundleSettingList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="bundle-setting-heading">
        Bundle Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Bundle Setting
        </Link>
      </h2>
      <div className="table-responsive">
        {bundleSettingList && bundleSettingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shop')}>
                  Shop <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('showOnProductPage')}>
                  Show On Product Page <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('showMultipleOnProductPage')}>
                  Show Multiple On Product Page <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('actionButtonColor')}>
                  Action Button Color <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('actionButtonFontColor')}>
                  Action Button Font Color <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('productTitleColor')}>
                  Product Title Color <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('productPriceColor')}>
                  Product Price Color <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('redirectTo')}>
                  Redirect To <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('showProductPrice')}>
                  Show Product Price <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('oneTimeDiscount')}>
                  One Time Discount <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('showDiscountInCart')}>
                  Show Discount In Cart <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('selector')}>
                  Selector <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('placement')}>
                  Placement <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('customCss')}>
                  Custom Css <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('variant')}>
                  Variant <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('deliveryFrequency')}>
                  Delivery Frequency <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('perDelivery')}>
                  Per Delivery <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('discountPopupHeader')}>
                  Discount Popup Header <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('discountPopupAmount')}>
                  Discount Popup Amount <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('discountPopupCheckoutMessage')}>
                  Discount Popup Checkout Message <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('discountPopupBuy')}>
                  Discount Popup Buy <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('discountPopupNo')}>
                  Discount Popup No <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('showDiscountPopup')}>
                  Show Discount Popup <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bundleSettingList.map((bundleSetting, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${bundleSetting.id}`} color="link" size="sm">
                      {bundleSetting.id}
                    </Button>
                  </td>
                  <td>{bundleSetting.shop}</td>
                  <td>{bundleSetting.showOnProductPage ? 'true' : 'false'}</td>
                  <td>{bundleSetting.showMultipleOnProductPage ? 'true' : 'false'}</td>
                  <td>{bundleSetting.actionButtonColor}</td>
                  <td>{bundleSetting.actionButtonFontColor}</td>
                  <td>{bundleSetting.productTitleColor}</td>
                  <td>{bundleSetting.productPriceColor}</td>
                  <td>{bundleSetting.redirectTo}</td>
                  <td>{bundleSetting.showProductPrice ? 'true' : 'false'}</td>
                  <td>{bundleSetting.oneTimeDiscount ? 'true' : 'false'}</td>
                  <td>{bundleSetting.showDiscountInCart ? 'true' : 'false'}</td>
                  <td>{bundleSetting.selector}</td>
                  <td>{bundleSetting.placement}</td>
                  <td>{bundleSetting.customCss}</td>
                  <td>{bundleSetting.variant}</td>
                  <td>{bundleSetting.deliveryFrequency}</td>
                  <td>{bundleSetting.perDelivery}</td>
                  <td>{bundleSetting.discountPopupHeader}</td>
                  <td>{bundleSetting.discountPopupAmount}</td>
                  <td>{bundleSetting.discountPopupCheckoutMessage}</td>
                  <td>{bundleSetting.discountPopupBuy}</td>
                  <td>{bundleSetting.discountPopupNo}</td>
                  <td>{bundleSetting.showDiscountPopup ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${bundleSetting.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bundleSetting.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bundleSetting.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Bundle Settings found</div>
        )}
      </div>
      {props.totalItems ? (
        <div className={bundleSettingList && bundleSettingList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ bundleSetting }: IRootState) => ({
  bundleSettingList: bundleSetting.entities,
  loading: bundleSetting.loading,
  totalItems: bundleSetting.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleSetting);
