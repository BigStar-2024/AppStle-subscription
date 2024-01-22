import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-bundle-settings.reducer';
import { ISubscriptionBundleSettings } from 'app/shared/model/subscription-bundle-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionBundleSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionBundleSettings = (props: ISubscriptionBundleSettingsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { subscriptionBundleSettingsList, match, loading } = props;
  return (
    <div>
      <h2 id="subscription-bundle-settings-heading">
        Subscription Bundle Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Subscription Bundle Settings
        </Link>
      </h2>
      <div className="table-responsive">
        {subscriptionBundleSettingsList && subscriptionBundleSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Selected Frequency Label Text</th>
                <th>Add Button Text</th>
                <th>Select Minimum Product Button Text</th>
                <th>Products To Proceed Text</th>
                <th>Proceed To Checkout Button Text</th>
                <th>My Delivery Text</th>
                <th>Build-A-Box Top Html</th>
                <th>Build-A-Box Bottom HTML</th>
                <th>Failed To Add Title Text</th>
                <th>Ok Btn Text</th>
                <th>Failed To Add Msg Text</th>
                <th>Button Color</th>
                <th>Background Color</th>
                <th>Page Background Color</th>
                <th>Button Background Color</th>
                <th>Product Title Font Color</th>
                <th>Variant Not Available</th>
                <th>Bundle Redirect</th>
                <th>Is Bundle Without Scroll</th>
                <th>Description Length</th>
                <th>Currency Switcher Class Name</th>
                <th>Product Price Format Field</th>
                <th>Custom Redirect URL</th>
                <th>View Product</th>
                <th>Product Details</th>
                <th>Edit Quantity</th>
                <th>Cart</th>
                <th>Shopping Cart</th>
                <th>Title</th>
                <th>Tiered Discount</th>
                <th>Subtotal</th>
                <th>Checkout Message</th>
                <th>Continue Shopping</th>
                <th>Spend Amount Get Discount</th>
                <th>Buy Quantity Get Discount</th>
                <th>Remove Item</th>
                <th>Show Compare At Price</th>
                <th>Enable Redirect To Product Page</th>
                <th>Save Discount Text</th>
                <th>Disable Product Description</th>
                <th>Enable Custom Advanced Fields</th>
                <th>Hide Product Search Box</th>
                <th>Product Filter Config</th>
                <th>Enable Product Detail Button</th>
                <th>Enable Show Product Base Price</th>
                <th>Enable Clear Cart Selected Products</th>
                <th>Enable Skiey BAB Header</th>
                <th>Enable Opening Sidebar</th>
                <th>Right Sidebar HTML</th>
                <th>Left Sidebar HTML</th>
                <th>Is Merge Into Single BAB Variant Dropdown</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionBundleSettingsList.map((subscriptionBundleSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionBundleSettings.id}`} color="link" size="sm">
                      {subscriptionBundleSettings.id}
                    </Button>
                  </td>
                  <td>{subscriptionBundleSettings.shop}</td>
                  <td>{subscriptionBundleSettings.selectedFrequencyLabelText}</td>
                  <td>{subscriptionBundleSettings.addButtonText}</td>
                  <td>{subscriptionBundleSettings.selectMinimumProductButtonText}</td>
                  <td>{subscriptionBundleSettings.productsToProceedText}</td>
                  <td>{subscriptionBundleSettings.proceedToCheckoutButtonText}</td>
                  <td>{subscriptionBundleSettings.myDeliveryText}</td>
                  <td>{subscriptionBundleSettings.bundleTopHtml}</td>
                  <td>{subscriptionBundleSettings.bundleBottomHtml}</td>
                  <td>{subscriptionBundleSettings.failedToAddTitleText}</td>
                  <td>{subscriptionBundleSettings.okBtnText}</td>
                  <td>{subscriptionBundleSettings.failedToAddMsgText}</td>
                  <td>{subscriptionBundleSettings.buttonColor}</td>
                  <td>{subscriptionBundleSettings.backgroundColor}</td>
                  <td>{subscriptionBundleSettings.pageBackgroundColor}</td>
                  <td>{subscriptionBundleSettings.buttonBackgroundColor}</td>
                  <td>{subscriptionBundleSettings.ProductTitleFontColor}</td>
                  <td>{subscriptionBundleSettings.variantNotAvailable}</td>
                  <td>{subscriptionBundleSettings.bundleRedirect}</td>
                  <td>{subscriptionBundleSettings.isBundleWithoutScroll ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.descriptionLength}</td>
                  <td>{subscriptionBundleSettings.currencySwitcherClassName}</td>
                  <td>{subscriptionBundleSettings.productPriceFormatField}</td>
                  <td>{subscriptionBundleSettings.customRedirectURL}</td>
                  <td>{subscriptionBundleSettings.viewProduct}</td>
                  <td>{subscriptionBundleSettings.productDetails}</td>
                  <td>{subscriptionBundleSettings.editQuantity}</td>
                  <td>{subscriptionBundleSettings.cart}</td>
                  <td>{subscriptionBundleSettings.shoppingCart}</td>
                  <td>{subscriptionBundleSettings.title}</td>
                  <td>{subscriptionBundleSettings.tieredDiscount}</td>
                  <td>{subscriptionBundleSettings.subtotal}</td>
                  <td>{subscriptionBundleSettings.checkoutMessage}</td>
                  <td>{subscriptionBundleSettings.continueShopping}</td>
                  <td>{subscriptionBundleSettings.spendAmountGetDiscount}</td>
                  <td>{subscriptionBundleSettings.buyQuantityGetDiscount}</td>
                  <td>{subscriptionBundleSettings.removeItem}</td>
                  <td>{subscriptionBundleSettings.showCompareAtPrice ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.enableRedirectToProductPage}</td>
                  <td>{subscriptionBundleSettings.saveDiscountText}</td>
                  <td>{subscriptionBundleSettings.disableProductDescription ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.enableCustomAdvancedFields ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.hideProductSearchBox ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.productFilterConfig}</td>
                  <td>{subscriptionBundleSettings.enableProductDetailButton ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.enableShowProductBasePrice ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.enableClearCartSelectedProducts ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.enableSkieyBABHeader ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.enableOpeningSidebar ? 'true' : 'false'}</td>
                  <td>{subscriptionBundleSettings.rightSidebarHTML}</td>
                  <td>{subscriptionBundleSettings.leftSidebarHTML}</td>
                  <td>{subscriptionBundleSettings.isMergeIntoSingleBABVariantDropdown ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionBundleSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionBundleSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionBundleSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Subscription Bundle Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionBundleSettings }: IRootState) => ({
  subscriptionBundleSettingsList: subscriptionBundleSettings.entities,
  loading: subscriptionBundleSettings.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBundleSettings);
