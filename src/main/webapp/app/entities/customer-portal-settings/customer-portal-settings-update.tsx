import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './customer-portal-settings.reducer';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomerPortalSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerPortalSettingsUpdate = (props: ICustomerPortalSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { customerPortalSettingsEntity, loading, updating } = props;

  const { customerPortalSettingJson } = customerPortalSettingsEntity;

  const handleClose = () => {
    props.history.push('/admin/customer-portal-settings');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
//    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
//    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...customerPortalSettingsEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="subscriptionApp.customerPortalSettings.home.createOrEditLabel">Create or edit a CustomerPortalSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : customerPortalSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="customer-portal-settings-id">ID</Label>
                  <AvInput id="customer-portal-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="customer-portal-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="customer-portal-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="orderFrequencyTextLabel" for="customer-portal-settings-orderFrequencyText">
                  Order Frequency Text
                </Label>
                <AvField
                  id="customer-portal-settings-orderFrequencyText"
                  type="text"
                  name="orderFrequencyText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="totalProductsTextLabel" for="customer-portal-settings-totalProductsText">
                  Total Products Text
                </Label>
                <AvField
                  id="customer-portal-settings-totalProductsText"
                  type="text"
                  name="totalProductsText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nextOrderTextLabel" for="customer-portal-settings-nextOrderText">
                  Next Order Text
                </Label>
                <AvField
                  id="customer-portal-settings-nextOrderText"
                  type="text"
                  name="nextOrderText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusTextLabel" for="customer-portal-settings-statusText">
                  Status Text
                </Label>
                <AvField
                  id="customer-portal-settings-statusText"
                  type="text"
                  name="statusText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cancelSubscriptionBtnTextLabel" for="customer-portal-settings-cancelSubscriptionBtnText">
                  Cancel Subscription Btn Text
                </Label>
                <AvField
                  id="customer-portal-settings-cancelSubscriptionBtnText"
                  type="text"
                  name="cancelSubscriptionBtnText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="noSubscriptionMessageLabel" for="customer-portal-settings-noSubscriptionMessage">
                  No Subscription Message
                </Label>
                <AvField
                  id="customer-portal-settings-noSubscriptionMessage"
                  type="text"
                  name="noSubscriptionMessage"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionNoTextLabel" for="customer-portal-settings-subscriptionNoText">
                  Subscription No Text
                </Label>
                <AvField
                  id="customer-portal-settings-subscriptionNoText"
                  type="text"
                  name="subscriptionNoText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatePaymentMessageLabel" for="customer-portal-settings-updatePaymentMessage">
                  Update Payment Message
                </Label>
                <AvField
                  id="customer-portal-settings-updatePaymentMessage"
                  type="text"
                  name="updatePaymentMessage"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cardLastFourDigitTextLabel" for="customer-portal-settings-cardLastFourDigitText">
                  Card Last Four Digit Text
                </Label>
                <AvField
                  id="customer-portal-settings-cardLastFourDigitText"
                  type="text"
                  name="cardLastFourDigitText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cardExpiryTextLabel" for="customer-portal-settings-cardExpiryText">
                  Card Expiry Text
                </Label>
                <AvField
                  id="customer-portal-settings-cardExpiryText"
                  type="text"
                  name="cardExpiryText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cardHolderNameTextLabel" for="customer-portal-settings-cardHolderNameText">
                  Card Holder Name Text
                </Label>
                <AvField
                  id="customer-portal-settings-cardHolderNameText"
                  type="text"
                  name="cardHolderNameText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cardTypeTextLabel" for="customer-portal-settings-cardTypeText">
                  Card Type Text
                </Label>
                <AvField
                  id="customer-portal-settings-cardTypeText"
                  type="text"
                  name="cardTypeText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="paymentMethodTypeTextLabel" for="customer-portal-settings-paymentMethodTypeText">
                  Payment Method Type Text
                </Label>
                <AvField
                  id="customer-portal-settings-paymentMethodTypeText"
                  type="text"
                  name="paymentMethodTypeText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cancelAccordionTitleLabel" for="customer-portal-settings-cancelAccordionTitle">
                  Cancel Accordion Title
                </Label>
                <AvField
                  id="customer-portal-settings-cancelAccordionTitle"
                  type="text"
                  name="cancelAccordionTitle"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="paymentDetailAccordionTitleLabel" for="customer-portal-settings-paymentDetailAccordionTitle">
                  Payment Detail Accordion Title
                </Label>
                <AvField
                  id="customer-portal-settings-paymentDetailAccordionTitle"
                  type="text"
                  name="paymentDetailAccordionTitle"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="upcomingOrderAccordionTitleLabel" for="customer-portal-settings-upcomingOrderAccordionTitle">
                  Upcoming Order Accordion Title
                </Label>
                <AvField
                  id="customer-portal-settings-upcomingOrderAccordionTitle"
                  type="text"
                  name="upcomingOrderAccordionTitle"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="paymentInfoTextLabel" for="customer-portal-settings-paymentInfoText">
                  Payment Info Text
                </Label>
                <AvField
                  id="customer-portal-settings-paymentInfoText"
                  type="text"
                  name="paymentInfoText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatePaymentBtnTextLabel" for="customer-portal-settings-updatePaymentBtnText">
                  Update Payment Btn Text
                </Label>
                <AvField
                  id="customer-portal-settings-updatePaymentBtnText"
                  type="text"
                  name="updatePaymentBtnText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nextOrderDateLblLabel" for="customer-portal-settings-nextOrderDateLbl">
                  Next Order Date Lbl
                </Label>
                <AvField
                  id="customer-portal-settings-nextOrderDateLbl"
                  type="text"
                  name="nextOrderDateLbl"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusLblLabel" for="customer-portal-settings-statusLbl">
                  Status Lbl
                </Label>
                <AvField
                  id="customer-portal-settings-statusLbl"
                  type="text"
                  name="statusLbl"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLblLabel" for="customer-portal-settings-quantityLbl">
                  Quantity Lbl
                </Label>
                <AvField
                  id="customer-portal-settings-quantityLbl"
                  type="text"
                  name="quantityLbl"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="amountLblLabel" for="customer-portal-settings-amountLbl">
                  Amount Lbl
                </Label>
                <AvField
                  id="customer-portal-settings-amountLbl"
                  type="text"
                  name="amountLbl"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="orderNoLblLabel" for="customer-portal-settings-orderNoLbl">
                  Order No Lbl
                </Label>
                <AvField
                  id="customer-portal-settings-orderNoLbl"
                  type="text"
                  name="orderNoLbl"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="editFrequencyBtnTextLabel" for="customer-portal-settings-editFrequencyBtnText">
                  Edit Frequency Btn Text
                </Label>
                <AvField
                  id="customer-portal-settings-editFrequencyBtnText"
                  type="text"
                  name="editFrequencyBtnText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cancelFreqBtnTextLabel" for="customer-portal-settings-cancelFreqBtnText">
                  Cancel Freq Btn Text
                </Label>
                <AvField
                  id="customer-portal-settings-cancelFreqBtnText"
                  type="text"
                  name="cancelFreqBtnText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updateFreqBtnTextLabel" for="customer-portal-settings-updateFreqBtnText">
                  Update Freq Btn Text
                </Label>
                <AvField
                  id="customer-portal-settings-updateFreqBtnText"
                  type="text"
                  name="updateFreqBtnText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="pauseResumeSubLabel">
                  <AvInput
                    id="customer-portal-settings-pauseResumeSub"
                    type="checkbox"
                    className="form-check-input"
                    name="pauseResumeSub"
                  />
                  Pause Resume Sub
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="changeNextOrderDateLabel">
                  <AvInput
                    id="customer-portal-settings-changeNextOrderDate"
                    type="checkbox"
                    className="form-check-input"
                    name="changeNextOrderDate"
                  />
                  Change Next Order Date
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="cancelSubLabel">
                  <AvInput id="customer-portal-settings-cancelSub" type="checkbox" className="form-check-input" name="cancelSub" />
                  Cancel Sub
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="changeOrderFrequencyLabel">
                  <AvInput
                    id="customer-portal-settings-changeOrderFrequency"
                    type="checkbox"
                    className="form-check-input"
                    name="changeOrderFrequency"
                  />
                  Change Order Frequency
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="createAdditionalOrderLabel">
                  <AvInput
                    id="customer-portal-settings-createAdditionalOrder"
                    type="checkbox"
                    className="form-check-input"
                    name="createAdditionalOrder"
                  />
                  Create Additional Order
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="manageSubscriptionButtonTextLabel" for="customer-portal-settings-manageSubscriptionButtonText">
                  Manage Subscription Button Text
                </Label>
                <AvField id="customer-portal-settings-manageSubscriptionButtonText" type="text" name="manageSubscriptionButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="editChangeOrderBtnTextLabel" for="customer-portal-settings-editChangeOrderBtnText">
                  Edit Change Order Btn Text
                </Label>
                <AvField id="customer-portal-settings-editChangeOrderBtnText" type="text" name="editChangeOrderBtnText" />
              </AvGroup>
              <AvGroup>
                <Label id="cancelChangeOrderBtnTextLabel" for="customer-portal-settings-cancelChangeOrderBtnText">
                  Cancel Change Order Btn Text
                </Label>
                <AvField id="customer-portal-settings-cancelChangeOrderBtnText" type="text" name="cancelChangeOrderBtnText" />
              </AvGroup>
              <AvGroup>
                <Label id="updateChangeOrderBtnTextLabel" for="customer-portal-settings-updateChangeOrderBtnText">
                  Update Change Order Btn Text
                </Label>
                <AvField id="customer-portal-settings-updateChangeOrderBtnText" type="text" name="updateChangeOrderBtnText" />
              </AvGroup>
              <AvGroup>
                <Label id="editProductButtonTextLabel" for="customer-portal-settings-editProductButtonText">
                  Edit Product Button Text
                </Label>
                <AvField id="customer-portal-settings-editProductButtonText" type="text" name="editProductButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="deleteButtonTextLabel" for="customer-portal-settings-deleteButtonText">
                  Delete Button Text
                </Label>
                <AvField id="customer-portal-settings-deleteButtonText" type="text" name="deleteButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="updateButtonTextLabel" for="customer-portal-settings-updateButtonText">
                  Update Button Text
                </Label>
                <AvField id="customer-portal-settings-updateButtonText" type="text" name="updateButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="cancelButtonTextLabel" for="customer-portal-settings-cancelButtonText">
                  Cancel Button Text
                </Label>
                <AvField id="customer-portal-settings-cancelButtonText" type="text" name="cancelButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="addProductButtonTextLabel" for="customer-portal-settings-addProductButtonText">
                  Add Product Button Text
                </Label>
                <AvField id="customer-portal-settings-addProductButtonText" type="text" name="addProductButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="addProductLabelTextLabel" for="customer-portal-settings-addProductLabelText">
                  Add Product Label Text
                </Label>
                <AvField id="customer-portal-settings-addProductLabelText" type="text" name="addProductLabelText" />
              </AvGroup>
              <AvGroup>
                <Label id="activeBadgeTextLabel" for="customer-portal-settings-activeBadgeText">
                  Active Badge Text
                </Label>
                <AvField id="customer-portal-settings-activeBadgeText" type="text" name="activeBadgeText" />
              </AvGroup>
              <AvGroup>
                <Label id="closeBadgeTextLabel" for="customer-portal-settings-closeBadgeText">
                  Close Badge Text
                </Label>
                <AvField id="customer-portal-settings-closeBadgeText" type="text" name="closeBadgeText" />
              </AvGroup>
              <AvGroup>
                <Label id="skipOrderButtonTextLabel" for="customer-portal-settings-skipOrderButtonText">
                  Skip Order Button Text
                </Label>
                <AvField id="customer-portal-settings-skipOrderButtonText" type="text" name="skipOrderButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="productLabelTextLabel" for="customer-portal-settings-productLabelText">
                  Product Label Text
                </Label>
                <AvField id="customer-portal-settings-productLabelText" type="text" name="productLabelText" />
              </AvGroup>
              <AvGroup>
                <Label id="seeMoreDetailsTextLabel" for="customer-portal-settings-seeMoreDetailsText">
                  See More Details Text
                </Label>
                <AvField id="customer-portal-settings-seeMoreDetailsText" type="text" name="seeMoreDetailsText" />
              </AvGroup>
              <AvGroup>
                <Label id="hideDetailsTextLabel" for="customer-portal-settings-hideDetailsText">
                  Hide Details Text
                </Label>
                <AvField id="customer-portal-settings-hideDetailsText" type="text" name="hideDetailsText" />
              </AvGroup>
              <AvGroup>
                <Label id="productInSubscriptionTextLabel" for="customer-portal-settings-productInSubscriptionText">
                  Product In Subscription Text
                </Label>
                <AvField id="customer-portal-settings-productInSubscriptionText" type="text" name="productInSubscriptionText" />
              </AvGroup>
              <AvGroup>
                <Label id="EditQuantityLabelTextLabel" for="customer-portal-settings-EditQuantityLabelText">
                  Edit Quantity Label Text
                </Label>
                <AvField id="customer-portal-settings-EditQuantityLabelText" type="text" name="EditQuantityLabelText" />
              </AvGroup>
              <AvGroup>
                <Label id="subTotalLabelTextLabel" for="customer-portal-settings-subTotalLabelText">
                  Sub Total Label Text
                </Label>
                <AvField id="customer-portal-settings-subTotalLabelText" type="text" name="subTotalLabelText" />
              </AvGroup>
              <AvGroup>
                <Label id="paymentNotificationTextLabel" for="customer-portal-settings-paymentNotificationText">
                  Payment Notification Text
                </Label>
                <AvField id="customer-portal-settings-paymentNotificationText" type="text" name="paymentNotificationText" />
              </AvGroup>
              <AvGroup check>
                <Label id="editProductFlagLabel">
                  <AvInput
                    id="customer-portal-settings-editProductFlag"
                    type="checkbox"
                    className="form-check-input"
                    name="editProductFlag"
                  />
                  Edit Product Flag
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="deleteProductFlagLabel">
                  <AvInput
                    id="customer-portal-settings-deleteProductFlag"
                    type="checkbox"
                    className="form-check-input"
                    name="deleteProductFlag"
                  />
                  Delete Product Flag
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="showShipmentLabel">
                  <AvInput id="customer-portal-settings-showShipment" type="checkbox" className="form-check-input" name="showShipment" />
                  Show Shipment
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="addAdditionalProductLabel">
                  <AvInput
                    id="customer-portal-settings-addAdditionalProduct"
                    type="checkbox"
                    className="form-check-input"
                    name="addAdditionalProduct"
                  />
                  Add Additional Product
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="successTextLabel" for="customer-portal-settings-successText">
                  Success Text
                </Label>
                <AvField id="customer-portal-settings-successText" type="text" name="successText" />
              </AvGroup>
              <AvGroup>
                <Label id="cancelSubscriptionConfirmPrepaidTextLabel" for="customer-portal-settings-cancelSubscriptionConfirmPrepaidText">
                  Cancel Subscription Confirm Prepaid Text
                </Label>
                <AvField
                  id="customer-portal-settings-cancelSubscriptionConfirmPrepaidText"
                  type="text"
                  name="cancelSubscriptionConfirmPrepaidText"
                />
              </AvGroup>
              <AvGroup>
                <Label
                  id="cancelSubscriptionConfirmPayAsYouGoTextLabel"
                  for="customer-portal-settings-cancelSubscriptionConfirmPayAsYouGoText"
                >
                  Cancel Subscription Confirm Pay As You Go Text
                </Label>
                <AvField
                  id="customer-portal-settings-cancelSubscriptionConfirmPayAsYouGoText"
                  type="text"
                  name="cancelSubscriptionConfirmPayAsYouGoText"
                />
              </AvGroup>
              <AvGroup>
                <Label id="cancelSubscriptionPrepaidButtonTextLabel" for="customer-portal-settings-cancelSubscriptionPrepaidButtonText">
                  Cancel Subscription Prepaid Button Text
                </Label>
                <AvField
                  id="customer-portal-settings-cancelSubscriptionPrepaidButtonText"
                  type="text"
                  name="cancelSubscriptionPrepaidButtonText"
                />
              </AvGroup>
              <AvGroup>
                <Label
                  id="cancelSubscriptionPayAsYouGoButtonTextLabel"
                  for="customer-portal-settings-cancelSubscriptionPayAsYouGoButtonText"
                >
                  Cancel Subscription Pay As You Go Button Text
                </Label>
                <AvField
                  id="customer-portal-settings-cancelSubscriptionPayAsYouGoButtonText"
                  type="text"
                  name="cancelSubscriptionPayAsYouGoButtonText"
                />
              </AvGroup>
              <AvGroup>
                <Label id="upcomingFulfillmentTextLabel" for="customer-portal-settings-upcomingFulfillmentText">
                  Upcoming Fulfillment Text
                </Label>
                <AvField id="customer-portal-settings-upcomingFulfillmentText" type="text" name="upcomingFulfillmentText" />
              </AvGroup>
              <AvGroup>
                <Label id="creditCardTextLabel" for="customer-portal-settings-creditCardText">
                  Credit Card Text
                </Label>
                <AvField id="customer-portal-settings-creditCardText" type="text" name="creditCardText" />
              </AvGroup>
              <AvGroup>
                <Label id="endingWithTextLabel" for="customer-portal-settings-endingWithText">
                  Ending With Text
                </Label>
                <AvField id="customer-portal-settings-endingWithText" type="text" name="endingWithText" />
              </AvGroup>
              <AvGroup>
                <Label id="weekTextLabel" for="customer-portal-settings-weekText">
                  Week Text
                </Label>
                <AvField id="customer-portal-settings-weekText" type="text" name="weekText" />
              </AvGroup>
              <AvGroup>
                <Label id="dayTextLabel" for="customer-portal-settings-dayText">
                  Day Text
                </Label>
                <AvField id="customer-portal-settings-dayText" type="text" name="dayText" />
              </AvGroup>
              <AvGroup>
                <Label id="monthTextLabel" for="customer-portal-settings-monthText">
                  Month Text
                </Label>
                <AvField id="customer-portal-settings-monthText" type="text" name="monthText" />
              </AvGroup>
              <AvGroup>
                <Label id="yearTextLabel" for="customer-portal-settings-yearText">
                  Year Text
                </Label>
                <AvField id="customer-portal-settings-yearText" type="text" name="yearText" />
              </AvGroup>
              <AvGroup>
                <Label id="skipBadgeTextLabel" for="customer-portal-settings-skipBadgeText">
                  Skip Badge Text
                </Label>
                <AvField id="customer-portal-settings-skipBadgeText" type="text" name="skipBadgeText" />
              </AvGroup>
              <AvGroup>
                <Label id="queueBadgeTextLabel" for="customer-portal-settings-queueBadgeText">
                  Queue Badge Text
                </Label>
                <AvField id="customer-portal-settings-queueBadgeText" type="text" name="queueBadgeText" />
              </AvGroup>
              <AvGroup>
                <Label id="customerPortalSettingJsonLabel" for="customer-portal-settings-customerPortalSettingJson">
                  Customer Portal Setting Json
                </Label>
                <AvInput id="customer-portal-settings-customerPortalSettingJson" type="textarea" name="customerPortalSettingJson" />
              </AvGroup>
              <AvGroup check>
                <Label id="orderNoteFlagLabel">
                  <AvInput id="customer-portal-settings-orderNoteFlag" type="checkbox" className="form-check-input" name="orderNoteFlag" />
                  Order Note Flag
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="orderNoteTextLabel" for="customer-portal-settings-orderNoteText">
                  Order Note Text
                </Label>
                <AvField id="customer-portal-settings-orderNoteText" type="text" name="orderNoteText" />
              </AvGroup>
              <AvGroup check>
                <Label id="useUrlWithCustomerIdLabel">
                  <AvInput
                    id="customer-portal-settings-useUrlWithCustomerId"
                    type="checkbox"
                    className="form-check-input"
                    name="useUrlWithCustomerId"
                  />
                  Use Url With Customer Id
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="productSelectionOptionLabel" for="customer-portal-settings-productSelectionOption">
                  Product Selection Option
                </Label>
                <AvInput
                  id="customer-portal-settings-productSelectionOption"
                  type="select"
                  className="form-control"
                  name="productSelectionOption"
                  value={(!isNew && customerPortalSettingsEntity.productSelectionOption) || 'ALL_PRODUCTS'}
                >
                  <option value="ALL_PRODUCTS">ALL_PRODUCTS</option>
                  <option value="PRODUCTS_FROM_ALL_PLANS">PRODUCTS_FROM_ALL_PLANS</option>
                  <option value="PRODUCTS_FROM_CURRENT_PLAN">PRODUCTS_FROM_CURRENT_PLAN</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="includeOutOfStockProductLabel">
                  <AvInput
                    id="customer-portal-settings-includeOutOfStockProduct"
                    type="checkbox"
                    className="form-check-input"
                    name="includeOutOfStockProduct"
                  />
                  Include Out Of Stock Product
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="openBadgeTextLabel" for="customer-portal-settings-openBadgeText">
                  Open Badge Text
                </Label>
                <AvField id="customer-portal-settings-openBadgeText" type="text" name="openBadgeText" />
              </AvGroup>
              <AvGroup check>
                <Label id="updateShipmentBillingDateLabel">
                  <AvInput
                    id="customer-portal-settings-updateShipmentBillingDate"
                    type="checkbox"
                    className="form-check-input"
                    name="updateShipmentBillingDate"
                  />
                  Update Shipment Billing Date
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="discountCodeLabel">
                  <AvInput id="customer-portal-settings-discountCode" type="checkbox" className="form-check-input" name="discountCode" />
                  Discount Code
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="freezeOrderTillMinCycleLabel">
                  <AvInput
                    id="customer-portal-settings-freezeOrderTillMinCycle"
                    type="checkbox"
                    className="form-check-input"
                    name="freezeOrderTillMinCycle"
                  />
                  Freeze Order Till Min Cycle
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="addOneTimeProductLabel">
                  <AvInput
                    id="customer-portal-settings-addOneTimeProduct"
                    type="checkbox"
                    className="form-check-input"
                    name="addOneTimeProduct"
                  />
                  Add One Time Product
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="allowOrderNowLabel">
                  <AvInput id="customer-portal-settings-allowOrderNow" type="checkbox" className="form-check-input" name="allowOrderNow" />
                  Allow Order Now
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="minQtyToAllowDuringAddProductLabel" for="customer-portal-settings-minQtyToAllowDuringAddProduct">
                  Min Qty To Allow During Add Product
                </Label>
                <AvField
                  id="customer-portal-settings-minQtyToAllowDuringAddProduct"
                  type="string"
                  className="form-control"
                  name="minQtyToAllowDuringAddProduct"
                />
              </AvGroup>
              <AvGroup check>
                <Label id="allowSplitContractLabel">
                  <AvInput
                    id="customer-portal-settings-allowSplitContract"
                    type="checkbox"
                    className="form-check-input"
                    name="allowSplitContract"
                  />
                  Allow Split Contract
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableRedirectMyAccountButtonLabel">
                  <AvInput
                    id="customer-portal-settings-enableRedirectMyAccountButton"
                    type="checkbox"
                    className="form-check-input"
                    name="enableRedirectMyAccountButton"
                  />
                  Enable Redirect My Account Button
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableAllowOnlyOneDiscountCodeLabel">
                  <AvInput
                    id="customer-portal-settings-enableAllowOnlyOneDiscountCode"
                    type="checkbox"
                    className="form-check-input"
                    name="enableAllowOnlyOneDiscountCode"
                  />
                  Enable Allow Only One Discount Code
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableRedirectToProductPageLabel">
                  <AvInput
                    id="customer-portal-settings-enableRedirectToProductPage"
                    type="checkbox"
                    className="form-check-input"
                    name="enableRedirectToProductPage"
                  />
                  Enable Redirect To Product Page
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/customer-portal-settings" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  customerPortalSettingsEntity: storeState.customerPortalSettings.entity,
  loading: storeState.customerPortalSettings.loading,
  updating: storeState.customerPortalSettings.updating,
  updateSuccess: storeState.customerPortalSettings.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
//  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPortalSettingsUpdate);
