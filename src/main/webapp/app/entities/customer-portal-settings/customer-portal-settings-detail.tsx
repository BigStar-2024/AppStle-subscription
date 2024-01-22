import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer-portal-settings.reducer';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerPortalSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerPortalSettingsDetail = (props: ICustomerPortalSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { customerPortalSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CustomerPortalSettings [<b>{customerPortalSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{customerPortalSettingsEntity.shop}</dd>
          <dt>
            <span id="orderFrequencyText">Order Frequency Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.orderFrequencyText}</dd>
          <dt>
            <span id="totalProductsText">Total Products Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.totalProductsText}</dd>
          <dt>
            <span id="nextOrderText">Next Order Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.nextOrderText}</dd>
          <dt>
            <span id="statusText">Status Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.statusText}</dd>
          <dt>
            <span id="cancelSubscriptionBtnText">Cancel Subscription Btn Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelSubscriptionBtnText}</dd>
          <dt>
            <span id="noSubscriptionMessage">No Subscription Message</span>
          </dt>
          <dd>{customerPortalSettingsEntity.noSubscriptionMessage}</dd>
          <dt>
            <span id="subscriptionNoText">Subscription No Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.subscriptionNoText}</dd>
          <dt>
            <span id="updatePaymentMessage">Update Payment Message</span>
          </dt>
          <dd>{customerPortalSettingsEntity.updatePaymentMessage}</dd>
          <dt>
            <span id="cardLastFourDigitText">Card Last Four Digit Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cardLastFourDigitText}</dd>
          <dt>
            <span id="cardExpiryText">Card Expiry Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cardExpiryText}</dd>
          <dt>
            <span id="cardHolderNameText">Card Holder Name Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cardHolderNameText}</dd>
          <dt>
            <span id="cardTypeText">Card Type Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cardTypeText}</dd>
          <dt>
            <span id="paymentMethodTypeText">Payment Method Type Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.paymentMethodTypeText}</dd>
          <dt>
            <span id="cancelAccordionTitle">Cancel Accordion Title</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelAccordionTitle}</dd>
          <dt>
            <span id="paymentDetailAccordionTitle">Payment Detail Accordion Title</span>
          </dt>
          <dd>{customerPortalSettingsEntity.paymentDetailAccordionTitle}</dd>
          <dt>
            <span id="upcomingOrderAccordionTitle">Upcoming Order Accordion Title</span>
          </dt>
          <dd>{customerPortalSettingsEntity.upcomingOrderAccordionTitle}</dd>
          <dt>
            <span id="paymentInfoText">Payment Info Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.paymentInfoText}</dd>
          <dt>
            <span id="updatePaymentBtnText">Update Payment Btn Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.updatePaymentBtnText}</dd>
          <dt>
            <span id="nextOrderDateLbl">Next Order Date Lbl</span>
          </dt>
          <dd>{customerPortalSettingsEntity.nextOrderDateLbl}</dd>
          <dt>
            <span id="statusLbl">Status Lbl</span>
          </dt>
          <dd>{customerPortalSettingsEntity.statusLbl}</dd>
          <dt>
            <span id="quantityLbl">Quantity Lbl</span>
          </dt>
          <dd>{customerPortalSettingsEntity.quantityLbl}</dd>
          <dt>
            <span id="amountLbl">Amount Lbl</span>
          </dt>
          <dd>{customerPortalSettingsEntity.amountLbl}</dd>
          <dt>
            <span id="orderNoLbl">Order No Lbl</span>
          </dt>
          <dd>{customerPortalSettingsEntity.orderNoLbl}</dd>
          <dt>
            <span id="editFrequencyBtnText">Edit Frequency Btn Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.editFrequencyBtnText}</dd>
          <dt>
            <span id="cancelFreqBtnText">Cancel Freq Btn Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelFreqBtnText}</dd>
          <dt>
            <span id="updateFreqBtnText">Update Freq Btn Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.updateFreqBtnText}</dd>
          <dt>
            <span id="pauseResumeSub">Pause Resume Sub</span>
          </dt>
          <dd>{customerPortalSettingsEntity.pauseResumeSub ? 'true' : 'false'}</dd>
          <dt>
            <span id="changeNextOrderDate">Change Next Order Date</span>
          </dt>
          <dd>{customerPortalSettingsEntity.changeNextOrderDate ? 'true' : 'false'}</dd>
          <dt>
            <span id="cancelSub">Cancel Sub</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelSub ? 'true' : 'false'}</dd>
          <dt>
            <span id="changeOrderFrequency">Change Order Frequency</span>
          </dt>
          <dd>{customerPortalSettingsEntity.changeOrderFrequency ? 'true' : 'false'}</dd>
          <dt>
            <span id="createAdditionalOrder">Create Additional Order</span>
          </dt>
          <dd>{customerPortalSettingsEntity.createAdditionalOrder ? 'true' : 'false'}</dd>
          <dt>
            <span id="manageSubscriptionButtonText">Manage Subscription Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.manageSubscriptionButtonText}</dd>
          <dt>
            <span id="editChangeOrderBtnText">Edit Change Order Btn Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.editChangeOrderBtnText}</dd>
          <dt>
            <span id="cancelChangeOrderBtnText">Cancel Change Order Btn Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelChangeOrderBtnText}</dd>
          <dt>
            <span id="updateChangeOrderBtnText">Update Change Order Btn Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.updateChangeOrderBtnText}</dd>
          <dt>
            <span id="editProductButtonText">Edit Product Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.editProductButtonText}</dd>
          <dt>
            <span id="deleteButtonText">Delete Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.deleteButtonText}</dd>
          <dt>
            <span id="updateButtonText">Update Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.updateButtonText}</dd>
          <dt>
            <span id="cancelButtonText">Cancel Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelButtonText}</dd>
          <dt>
            <span id="addProductButtonText">Add Product Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.addProductButtonText}</dd>
          <dt>
            <span id="addProductLabelText">Add Product Label Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.addProductLabelText}</dd>
          <dt>
            <span id="activeBadgeText">Active Badge Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.activeBadgeText}</dd>
          <dt>
            <span id="closeBadgeText">Close Badge Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.closeBadgeText}</dd>
          <dt>
            <span id="skipOrderButtonText">Skip Order Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.skipOrderButtonText}</dd>
          <dt>
            <span id="productLabelText">Product Label Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.productLabelText}</dd>
          <dt>
            <span id="seeMoreDetailsText">See More Details Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.seeMoreDetailsText}</dd>
          <dt>
            <span id="hideDetailsText">Hide Details Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.hideDetailsText}</dd>
          <dt>
            <span id="productInSubscriptionText">Product In Subscription Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.productInSubscriptionText}</dd>
          <dt>
            <span id="EditQuantityLabelText">Edit Quantity Label Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.EditQuantityLabelText}</dd>
          <dt>
            <span id="subTotalLabelText">Sub Total Label Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.subTotalLabelText}</dd>
          <dt>
            <span id="paymentNotificationText">Payment Notification Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.paymentNotificationText}</dd>
          <dt>
            <span id="editProductFlag">Edit Product Flag</span>
          </dt>
          <dd>{customerPortalSettingsEntity.editProductFlag ? 'true' : 'false'}</dd>
          <dt>
            <span id="deleteProductFlag">Delete Product Flag</span>
          </dt>
          <dd>{customerPortalSettingsEntity.deleteProductFlag ? 'true' : 'false'}</dd>
          <dt>
            <span id="showShipment">Show Shipment</span>
          </dt>
          <dd>{customerPortalSettingsEntity.showShipment ? 'true' : 'false'}</dd>
          <dt>
            <span id="addAdditionalProduct">Add Additional Product</span>
          </dt>
          <dd>{customerPortalSettingsEntity.addAdditionalProduct ? 'true' : 'false'}</dd>
          <dt>
            <span id="successText">Success Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.successText}</dd>
          <dt>
            <span id="cancelSubscriptionConfirmPrepaidText">Cancel Subscription Confirm Prepaid Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelSubscriptionConfirmPrepaidText}</dd>
          <dt>
            <span id="cancelSubscriptionConfirmPayAsYouGoText">Cancel Subscription Confirm Pay As You Go Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelSubscriptionConfirmPayAsYouGoText}</dd>
          <dt>
            <span id="cancelSubscriptionPrepaidButtonText">Cancel Subscription Prepaid Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelSubscriptionPrepaidButtonText}</dd>
          <dt>
            <span id="cancelSubscriptionPayAsYouGoButtonText">Cancel Subscription Pay As You Go Button Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.cancelSubscriptionPayAsYouGoButtonText}</dd>
          <dt>
            <span id="upcomingFulfillmentText">Upcoming Fulfillment Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.upcomingFulfillmentText}</dd>
          <dt>
            <span id="creditCardText">Credit Card Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.creditCardText}</dd>
          <dt>
            <span id="endingWithText">Ending With Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.endingWithText}</dd>
          <dt>
            <span id="weekText">Week Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.weekText}</dd>
          <dt>
            <span id="dayText">Day Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.dayText}</dd>
          <dt>
            <span id="monthText">Month Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.monthText}</dd>
          <dt>
            <span id="yearText">Year Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.yearText}</dd>
          <dt>
            <span id="skipBadgeText">Skip Badge Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.skipBadgeText}</dd>
          <dt>
            <span id="queueBadgeText">Queue Badge Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.queueBadgeText}</dd>
          <dt>
            <span id="customerPortalSettingJson">Customer Portal Setting Json</span>
          </dt>
          <dd>{customerPortalSettingsEntity.customerPortalSettingJson}</dd>
          <dt>
            <span id="orderNoteFlag">Order Note Flag</span>
          </dt>
          <dd>{customerPortalSettingsEntity.orderNoteFlag ? 'true' : 'false'}</dd>
          <dt>
            <span id="orderNoteText">Order Note Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.orderNoteText}</dd>
          <dt>
            <span id="useUrlWithCustomerId">Use Url With Customer Id</span>
          </dt>
          <dd>{customerPortalSettingsEntity.useUrlWithCustomerId ? 'true' : 'false'}</dd>
          <dt>
            <span id="productSelectionOption">Product Selection Option</span>
          </dt>
          <dd>{customerPortalSettingsEntity.productSelectionOption}</dd>
          <dt>
            <span id="includeOutOfStockProduct">Include Out Of Stock Product</span>
          </dt>
          <dd>{customerPortalSettingsEntity.includeOutOfStockProduct ? 'true' : 'false'}</dd>
          <dt>
            <span id="openBadgeText">Open Badge Text</span>
          </dt>
          <dd>{customerPortalSettingsEntity.openBadgeText}</dd>
          <dt>
            <span id="updateShipmentBillingDate">Update Shipment Billing Date</span>
          </dt>
          <dd>{customerPortalSettingsEntity.updateShipmentBillingDate ? 'true' : 'false'}</dd>
          <dt>
            <span id="discountCode">Discount Code</span>
          </dt>
          <dd>{customerPortalSettingsEntity.discountCode ? 'true' : 'false'}</dd>
          <dt>
            <span id="freezeOrderTillMinCycle">Freeze Order Till Min Cycle</span>
          </dt>
          <dd>{customerPortalSettingsEntity.freezeOrderTillMinCycle ? 'true' : 'false'}</dd>
          <dt>
            <span id="addOneTimeProduct">Add One Time Product</span>
          </dt>
          <dd>{customerPortalSettingsEntity.addOneTimeProduct ? 'true' : 'false'}</dd>
          <dt>
            <span id="allowOrderNow">Allow Order Now</span>
          </dt>
          <dd>{customerPortalSettingsEntity.allowOrderNow ? 'true' : 'false'}</dd>
          <dt>
            <span id="minQtyToAllowDuringAddProduct">Min Qty To Allow During Add Product</span>
          </dt>
          <dd>{customerPortalSettingsEntity.minQtyToAllowDuringAddProduct}</dd>
          <dt>
            <span id="allowSplitContract">Allow Split Contract</span>
          </dt>
          <dd>{customerPortalSettingsEntity.allowSplitContract ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableSwapProductVariant">Enable Swap Product Variant</span>
          </dt>
          <dd>{customerPortalSettingsEntity.enableSwapProductVariant}</dd>
          <dt>
            <span id="enableRedirectMyAccountButton">Enable Redirect My Account Button</span>
          </dt>
          <dd>{customerPortalSettingsEntity.enableRedirectMyAccountButton ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableAllowOnlyOneDiscountCode">Enable Allow Only One Discount Code</span>
          </dt>
          <dd>{customerPortalSettingsEntity.enableAllowOnlyOneDiscountCode ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableRedirectToProductPage">Enable Redirect To Product Page</span>
          </dt>
          <dd>{customerPortalSettingsEntity.enableRedirectToProductPage ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/customer-portal-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer-portal-settings/${customerPortalSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ customerPortalSettings }: IRootState) => ({
  customerPortalSettingsEntity: customerPortalSettings.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPortalSettingsDetail);
