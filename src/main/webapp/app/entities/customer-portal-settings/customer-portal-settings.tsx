import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './customer-portal-settings.reducer';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerPortalSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CustomerPortalSettings = (props: ICustomerPortalSettingsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { customerPortalSettingsList, match, loading } = props;
  return (
    <div>
      <h2 id="customer-portal-settings-heading">
        Customer Portal Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Customer Portal Settings
        </Link>
      </h2>
      <div className="table-responsive">
        {customerPortalSettingsList && customerPortalSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Order Frequency Text</th>
                <th>Total Products Text</th>
                <th>Next Order Text</th>
                <th>Status Text</th>
                <th>Cancel Subscription Btn Text</th>
                <th>No Subscription Message</th>
                <th>Subscription No Text</th>
                <th>Update Payment Message</th>
                <th>Card Last Four Digit Text</th>
                <th>Card Expiry Text</th>
                <th>Card Holder Name Text</th>
                <th>Card Type Text</th>
                <th>Payment Method Type Text</th>
                <th>Cancel Accordion Title</th>
                <th>Payment Detail Accordion Title</th>
                <th>Upcoming Order Accordion Title</th>
                <th>Payment Info Text</th>
                <th>Update Payment Btn Text</th>
                <th>Next Order Date Lbl</th>
                <th>Status Lbl</th>
                <th>Quantity Lbl</th>
                <th>Amount Lbl</th>
                <th>Order No Lbl</th>
                <th>Edit Frequency Btn Text</th>
                <th>Cancel Freq Btn Text</th>
                <th>Update Freq Btn Text</th>
                <th>Pause Resume Sub</th>
                <th>Change Next Order Date</th>
                <th>Cancel Sub</th>
                <th>Change Order Frequency</th>
                <th>Create Additional Order</th>
                <th>Manage Subscription Button Text</th>
                <th>Edit Change Order Btn Text</th>
                <th>Cancel Change Order Btn Text</th>
                <th>Update Change Order Btn Text</th>
                <th>Edit Product Button Text</th>
                <th>Delete Button Text</th>
                <th>Update Button Text</th>
                <th>Cancel Button Text</th>
                <th>Add Product Button Text</th>
                <th>Add Product Label Text</th>
                <th>Active Badge Text</th>
                <th>Close Badge Text</th>
                <th>Skip Order Button Text</th>
                <th>Product Label Text</th>
                <th>See More Details Text</th>
                <th>Hide Details Text</th>
                <th>Product In Subscription Text</th>
                <th>Edit Quantity Label Text</th>
                <th>Sub Total Label Text</th>
                <th>Payment Notification Text</th>
                <th>Edit Product Flag</th>
                <th>Delete Product Flag</th>
                <th>Show Shipment</th>
                <th>Add Additional Product</th>
                <th>Success Text</th>
                <th>Cancel Subscription Confirm Prepaid Text</th>
                <th>Cancel Subscription Confirm Pay As You Go Text</th>
                <th>Cancel Subscription Prepaid Button Text</th>
                <th>Cancel Subscription Pay As You Go Button Text</th>
                <th>Upcoming Fulfillment Text</th>
                <th>Credit Card Text</th>
                <th>Ending With Text</th>
                <th>Week Text</th>
                <th>Day Text</th>
                <th>Month Text</th>
                <th>Year Text</th>
                <th>Skip Badge Text</th>
                <th>Queue Badge Text</th>
                <th>Customer Portal Setting Json</th>
                <th>Order Note Flag</th>
                <th>Order Note Text</th>
                <th>Use Url With Customer Id</th>
                <th>Product Selection Option</th>
                <th>Include Out Of Stock Product</th>
                <th>Open Badge Text</th>
                <th>Update Shipment Billing Date</th>
                <th>Discount Code</th>
                <th>Freeze Order Till Min Cycle</th>
                <th>Add One Time Product</th>
                <th>Allow Order Now</th>
                <th>Min Qty To Allow During Add Product</th>
                <th>Allow Split Contract</th>
                <th>Enable Swap Product Variant</th>
                <th>Enable Redirect My Account Button</th>
                <th>Enable Allow Only One Discount Code</th>
                <th>Enable Redirect To Product Page</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerPortalSettingsList.map((customerPortalSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customerPortalSettings.id}`} color="link" size="sm">
                      {customerPortalSettings.id}
                    </Button>
                  </td>
                  <td>{customerPortalSettings.shop}</td>
                  <td>{customerPortalSettings.orderFrequencyText}</td>
                  <td>{customerPortalSettings.totalProductsText}</td>
                  <td>{customerPortalSettings.nextOrderText}</td>
                  <td>{customerPortalSettings.statusText}</td>
                  <td>{customerPortalSettings.cancelSubscriptionBtnText}</td>
                  <td>{customerPortalSettings.noSubscriptionMessage}</td>
                  <td>{customerPortalSettings.subscriptionNoText}</td>
                  <td>{customerPortalSettings.updatePaymentMessage}</td>
                  <td>{customerPortalSettings.cardLastFourDigitText}</td>
                  <td>{customerPortalSettings.cardExpiryText}</td>
                  <td>{customerPortalSettings.cardHolderNameText}</td>
                  <td>{customerPortalSettings.cardTypeText}</td>
                  <td>{customerPortalSettings.paymentMethodTypeText}</td>
                  <td>{customerPortalSettings.cancelAccordionTitle}</td>
                  <td>{customerPortalSettings.paymentDetailAccordionTitle}</td>
                  <td>{customerPortalSettings.upcomingOrderAccordionTitle}</td>
                  <td>{customerPortalSettings.paymentInfoText}</td>
                  <td>{customerPortalSettings.updatePaymentBtnText}</td>
                  <td>{customerPortalSettings.nextOrderDateLbl}</td>
                  <td>{customerPortalSettings.statusLbl}</td>
                  <td>{customerPortalSettings.quantityLbl}</td>
                  <td>{customerPortalSettings.amountLbl}</td>
                  <td>{customerPortalSettings.orderNoLbl}</td>
                  <td>{customerPortalSettings.editFrequencyBtnText}</td>
                  <td>{customerPortalSettings.cancelFreqBtnText}</td>
                  <td>{customerPortalSettings.updateFreqBtnText}</td>
                  <td>{customerPortalSettings.pauseResumeSub ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.changeNextOrderDate ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.cancelSub ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.changeOrderFrequency ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.createAdditionalOrder ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.manageSubscriptionButtonText}</td>
                  <td>{customerPortalSettings.editChangeOrderBtnText}</td>
                  <td>{customerPortalSettings.cancelChangeOrderBtnText}</td>
                  <td>{customerPortalSettings.updateChangeOrderBtnText}</td>
                  <td>{customerPortalSettings.editProductButtonText}</td>
                  <td>{customerPortalSettings.deleteButtonText}</td>
                  <td>{customerPortalSettings.updateButtonText}</td>
                  <td>{customerPortalSettings.cancelButtonText}</td>
                  <td>{customerPortalSettings.addProductButtonText}</td>
                  <td>{customerPortalSettings.addProductLabelText}</td>
                  <td>{customerPortalSettings.activeBadgeText}</td>
                  <td>{customerPortalSettings.closeBadgeText}</td>
                  <td>{customerPortalSettings.skipOrderButtonText}</td>
                  <td>{customerPortalSettings.productLabelText}</td>
                  <td>{customerPortalSettings.seeMoreDetailsText}</td>
                  <td>{customerPortalSettings.hideDetailsText}</td>
                  <td>{customerPortalSettings.productInSubscriptionText}</td>
                  <td>{customerPortalSettings.EditQuantityLabelText}</td>
                  <td>{customerPortalSettings.subTotalLabelText}</td>
                  <td>{customerPortalSettings.paymentNotificationText}</td>
                  <td>{customerPortalSettings.editProductFlag ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.deleteProductFlag ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.showShipment ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.addAdditionalProduct ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.successText}</td>
                  <td>{customerPortalSettings.cancelSubscriptionConfirmPrepaidText}</td>
                  <td>{customerPortalSettings.cancelSubscriptionConfirmPayAsYouGoText}</td>
                  <td>{customerPortalSettings.cancelSubscriptionPrepaidButtonText}</td>
                  <td>{customerPortalSettings.cancelSubscriptionPayAsYouGoButtonText}</td>
                  <td>{customerPortalSettings.upcomingFulfillmentText}</td>
                  <td>{customerPortalSettings.creditCardText}</td>
                  <td>{customerPortalSettings.endingWithText}</td>
                  <td>{customerPortalSettings.weekText}</td>
                  <td>{customerPortalSettings.dayText}</td>
                  <td>{customerPortalSettings.monthText}</td>
                  <td>{customerPortalSettings.yearText}</td>
                  <td>{customerPortalSettings.skipBadgeText}</td>
                  <td>{customerPortalSettings.queueBadgeText}</td>
                  <td>{customerPortalSettings.customerPortalSettingJson}</td>
                  <td>{customerPortalSettings.orderNoteFlag ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.orderNoteText}</td>
                  <td>{customerPortalSettings.useUrlWithCustomerId ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.productSelectionOption}</td>
                  <td>{customerPortalSettings.includeOutOfStockProduct ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.openBadgeText}</td>
                  <td>{customerPortalSettings.updateShipmentBillingDate ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.discountCode ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.freezeOrderTillMinCycle ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.addOneTimeProduct ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.allowOrderNow ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.minQtyToAllowDuringAddProduct}</td>
                  <td>{customerPortalSettings.allowSplitContract ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.enableSwapProductVariant}</td>
                  <td>{customerPortalSettings.enableRedirectMyAccountButton ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.enableAllowOnlyOneDiscountCode ? 'true' : 'false'}</td>
                  <td>{customerPortalSettings.enableRedirectToProductPage ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customerPortalSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerPortalSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerPortalSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Customer Portal Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ customerPortalSettings }: IRootState) => ({
  customerPortalSettingsList: customerPortalSettings.entities,
  loading: customerPortalSettings.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPortalSettings);
