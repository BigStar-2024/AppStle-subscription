import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './theme-code.reducer';
import { IThemeCode } from 'app/shared/model/theme-code.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IThemeCodeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ThemeCode = (props: IThemeCodeProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { themeCodeList, match } = props;
  return (
    <div>
      <h2 id="theme-code-heading">
        Theme Codes
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Theme Code
        </Link>
      </h2>
      <div className="table-responsive">
        {themeCodeList && themeCodeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Theme Name</th>
                <th>Theme Name Friendly</th>
                <th>Theme Store Id</th>
                <th>Add To Cart Selector</th>
                <th>Subscription Link Selector</th>
                <th>Add To Cart Placement</th>
                <th>Subscription Link Placement</th>
                <th>Price Selector</th>
                <th>Price Placement</th>
                <th>Badge Top</th>
                <th>Cart Row Selector</th>
                <th>Cart Line Item Selector</th>
                <th>Cart Line Item Per Quantity Price Selector</th>
                <th>Cart Line Item Total Price Selector</th>
                <th>Cart Line Item Selling Plan Name Selector</th>
                <th>Cart Sub Total Selector</th>
                <th>Cart Line Item Price Selector</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {themeCodeList.map((themeCode, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${themeCode.id}`} color="link" size="sm">
                      {themeCode.id}
                    </Button>
                  </td>
                  <td>{themeCode.themeName}</td>
                  <td>{themeCode.themeNameFriendly}</td>
                  <td>{themeCode.themeStoreId}</td>
                  <td>{themeCode.addToCartSelector}</td>
                  <td>{themeCode.subscriptionLinkSelector}</td>
                  <td>{themeCode.addToCartPlacement}</td>
                  <td>{themeCode.subscriptionLinkPlacement}</td>
                  <td>{themeCode.priceSelector}</td>
                  <td>{themeCode.pricePlacement}</td>
                  <td>{themeCode.badgeTop}</td>
                  <td>{themeCode.cartRowSelector}</td>
                  <td>{themeCode.cartLineItemSelector}</td>
                  <td>{themeCode.cartLineItemPerQuantityPriceSelector}</td>
                  <td>{themeCode.cartLineItemTotalPriceSelector}</td>
                  <td>{themeCode.cartLineItemSellingPlanNameSelector}</td>
                  <td>{themeCode.cartSubTotalSelector}</td>
                  <td>{themeCode.cartLineItemPriceSelector}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${themeCode.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${themeCode.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${themeCode.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Theme Codes found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ themeCode }: IRootState) => ({
  themeCodeList: themeCode.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ThemeCode);
