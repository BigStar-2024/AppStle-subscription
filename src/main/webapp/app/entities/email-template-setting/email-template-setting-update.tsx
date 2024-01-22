import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './email-template-setting.reducer';
import { IEmailTemplateSetting } from 'app/shared/model/email-template-setting.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEmailTemplateSettingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmailTemplateSettingUpdate = (props: IEmailTemplateSettingUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { emailTemplateSettingEntity, loading, updating } = props;

  const { subject, heading, content, footerText, html, shippingAddress, billingAddress } = emailTemplateSettingEntity;

  const handleClose = () => {
    props.history.push('/email-template-setting');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...emailTemplateSettingEntity,
        ...values
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
          <h2 id="subscriptionApp.emailTemplateSetting.home.createOrEditLabel">Create or edit a EmailTemplateSetting</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : emailTemplateSettingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="email-template-setting-id">ID</Label>
                  <AvInput id="email-template-setting-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="email-template-setting-shop">
                  Shop
                </Label>
                <AvField
                  id="email-template-setting-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="emailSettingTypeLabel" for="email-template-setting-emailSettingType">
                  Email Setting Type
                </Label>
                <AvInput
                  id="email-template-setting-emailSettingType"
                  type="select"
                  className="form-control"
                  name="emailSettingType"
                  value={(!isNew && emailTemplateSettingEntity.emailSettingType) || 'SUBSCRIPTION_CREATED'}
                >
                  <option value="SUBSCRIPTION_CREATED">SUBSCRIPTION_CREATED</option>
                  <option value="TRANSACTION_FAILED">TRANSACTION_FAILED</option>
                  <option value="UPCOMING_ORDER">UPCOMING_ORDER</option>
                  <option value="EXPIRING_CREDIT_CARD">EXPIRING_CREDIT_CARD</option>
                  <option value="SHIPPING_ADDRESS_UPDATED">SHIPPING_ADDRESS_UPDATED</option>
                  <option value="ORDER_FREQUENCY_UPDATED">ORDER_FREQUENCY_UPDATED</option>
                  <option value="NEXT_ORDER_DATE_UPDATED">NEXT_ORDER_DATE_UPDATED</option>
                  <option value="SUBSCRIPTION_PAUSED">SUBSCRIPTION_PAUSED</option>
                  <option value="SUBSCRIPTION_CANCELLED">SUBSCRIPTION_CANCELLED</option>
                  <option value="SUBSCRIPTION_RESUMED">SUBSCRIPTION_RESUMED</option>
                  <option value="SUBSCRIPTION_PRODUCT_ADDED">SUBSCRIPTION_PRODUCT_ADDED</option>
                  <option value="SUBSCRIPTION_PRODUCT_REMOVED">SUBSCRIPTION_PRODUCT_REMOVED</option>
                  <option value="SUBSCRIPTION_PRODUCT_REPLACED">SUBSCRIPTION_PRODUCT_REPLACED</option>
                  <option value="SECURITY_CHALLENGE">SECURITY_CHALLENGE</option>
                  <option value="SUBSCRIPTION_MANAGEMENT_LINK">SUBSCRIPTION_MANAGEMENT_LINK</option>
                  <option value="ORDER_SKIPPED">ORDER_SKIPPED</option>
                  <option value="OUT_OF_STOCK">OUT_OF_STOCK</option>
                  <option value="TRANSACTION_SUCCESS">TRANSACTION_SUCCESS</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="sendEmailDisabledLabel">
                  <AvInput
                    id="email-template-setting-sendEmailDisabled"
                    type="checkbox"
                    className="form-check-input"
                    name="sendEmailDisabled"
                  />
                  Send Email Disabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="subjectLabel" for="email-template-setting-subject">
                  Subject
                </Label>
                <AvInput
                  id="email-template-setting-subject"
                  type="textarea"
                  name="subject"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fromEmailLabel" for="email-template-setting-fromEmail">
                  From Email
                </Label>
                <AvField
                  id="email-template-setting-fromEmail"
                  type="text"
                  name="fromEmail"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="logoLabel" for="email-template-setting-logo">
                  Logo
                </Label>
                <AvField
                  id="email-template-setting-logo"
                  type="text"
                  name="logo"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="headingLabel" for="email-template-setting-heading">
                  Heading
                </Label>
                <AvInput
                  id="email-template-setting-heading"
                  type="textarea"
                  name="heading"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="headingTextColorLabel" for="email-template-setting-headingTextColor">
                  Heading Text Color
                </Label>
                <AvField
                  id="email-template-setting-headingTextColor"
                  type="text"
                  name="headingTextColor"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="contentTextColorLabel" for="email-template-setting-contentTextColor">
                  Content Text Color
                </Label>
                <AvField
                  id="email-template-setting-contentTextColor"
                  type="text"
                  name="contentTextColor"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="contentLinkColorLabel" for="email-template-setting-contentLinkColor">
                  Content Link Color
                </Label>
                <AvField
                  id="email-template-setting-contentLinkColor"
                  type="text"
                  name="contentLinkColor"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="contentLabel" for="email-template-setting-content">
                  Content
                </Label>
                <AvInput
                  id="email-template-setting-content"
                  type="textarea"
                  name="content"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="footerTextColorLabel" for="email-template-setting-footerTextColor">
                  Footer Text Color
                </Label>
                <AvField
                  id="email-template-setting-footerTextColor"
                  type="text"
                  name="footerTextColor"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="footerLinkColorLabel" for="email-template-setting-footerLinkColor">
                  Footer Link Color
                </Label>
                <AvField
                  id="email-template-setting-footerLinkColor"
                  type="text"
                  name="footerLinkColor"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="footerTextLabel" for="email-template-setting-footerText">
                  Footer Text
                </Label>
                <AvInput
                  id="email-template-setting-footerText"
                  type="textarea"
                  name="footerText"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="templateBackgroundColorLabel" for="email-template-setting-templateBackgroundColor">
                  Template Background Color
                </Label>
                <AvField
                  id="email-template-setting-templateBackgroundColor"
                  type="text"
                  name="templateBackgroundColor"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="htmlLabel" for="email-template-setting-html">
                  Html
                </Label>
                <AvInput id="email-template-setting-html" type="textarea" name="html" />
              </AvGroup>
              <AvGroup>
                <Label id="textImageUrlLabel" for="email-template-setting-textImageUrl">
                  Text Image Url
                </Label>
                <AvField id="email-template-setting-textImageUrl" type="text" name="textImageUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="manageSubscriptionButtonColorLabel" for="email-template-setting-manageSubscriptionButtonColor">
                  Manage Subscription Button Color
                </Label>
                <AvField id="email-template-setting-manageSubscriptionButtonColor" type="text" name="manageSubscriptionButtonColor" />
              </AvGroup>
              <AvGroup>
                <Label id="manageSubscriptionButtonTextLabel" for="email-template-setting-manageSubscriptionButtonText">
                  Manage Subscription Button Text
                </Label>
                <AvField id="email-template-setting-manageSubscriptionButtonText" type="text" name="manageSubscriptionButtonText" />
              </AvGroup>
              <AvGroup>
                <Label id="manageSubscriptionButtonTextColorLabel" for="email-template-setting-manageSubscriptionButtonTextColor">
                  Manage Subscription Button Text Color
                </Label>
                <AvField
                  id="email-template-setting-manageSubscriptionButtonTextColor"
                  type="text"
                  name="manageSubscriptionButtonTextColor"
                />
              </AvGroup>
              <AvGroup>
                <Label id="shippingAddressTextLabel" for="email-template-setting-shippingAddressText">
                  Shipping Address Text
                </Label>
                <AvField id="email-template-setting-shippingAddressText" type="text" name="shippingAddressText" />
              </AvGroup>
              <AvGroup>
                <Label id="billingAddressTextLabel" for="email-template-setting-billingAddressText">
                  Billing Address Text
                </Label>
                <AvField id="email-template-setting-billingAddressText" type="text" name="billingAddressText" />
              </AvGroup>
              <AvGroup>
                <Label id="nextOrderdateTextLabel" for="email-template-setting-nextOrderdateText">
                  Next Orderdate Text
                </Label>
                <AvField id="email-template-setting-nextOrderdateText" type="text" name="nextOrderdateText" />
              </AvGroup>
              <AvGroup>
                <Label id="paymentMethodTextLabel" for="email-template-setting-paymentMethodText">
                  Payment Method Text
                </Label>
                <AvField id="email-template-setting-paymentMethodText" type="text" name="paymentMethodText" />
              </AvGroup>
              <AvGroup>
                <Label id="endingInTextLabel" for="email-template-setting-endingInText">
                  Ending In Text
                </Label>
                <AvField id="email-template-setting-endingInText" type="text" name="endingInText" />
              </AvGroup>
              <AvGroup>
                <Label id="bccEmailLabel" for="email-template-setting-bccEmail">
                  Bcc Email
                </Label>
                <AvField id="email-template-setting-bccEmail" type="text" name="bccEmail" />
              </AvGroup>
              <AvGroup>
                <Label id="upcomingOrderEmailBufferLabel" for="email-template-setting-upcomingOrderEmailBuffer">
                  Upcoming Order Email Buffer
                </Label>
                <AvField
                  id="email-template-setting-upcomingOrderEmailBuffer"
                  type="string"
                  className="form-control"
                  name="upcomingOrderEmailBuffer"
                />
              </AvGroup>
              <AvGroup>
                <Label id="headingImageUrlLabel" for="email-template-setting-headingImageUrl">
                  Heading Image Url
                </Label>
                <AvField id="email-template-setting-headingImageUrl" type="text" name="headingImageUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="quantityTextLabel" for="email-template-setting-quantityText">
                  Quantity Text
                </Label>
                <AvField id="email-template-setting-quantityText" type="text" name="quantityText" />
              </AvGroup>
              <AvGroup>
                <Label id="manageSubscriptionButtonUrlLabel" for="email-template-setting-manageSubscriptionButtonUrl">
                  Manage Subscription Button Url
                </Label>
                <AvField id="email-template-setting-manageSubscriptionButtonUrl" type="text" name="manageSubscriptionButtonUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="logoHeightLabel" for="email-template-setting-logoHeight">
                  Logo Height
                </Label>
                <AvField id="email-template-setting-logoHeight" type="text" name="logoHeight" />
              </AvGroup>
              <AvGroup>
                <Label id="logoWidthLabel" for="email-template-setting-logoWidth">
                  Logo Width
                </Label>
                <AvField id="email-template-setting-logoWidth" type="text" name="logoWidth" />
              </AvGroup>
              <AvGroup>
                <Label id="thanksImageHeightLabel" for="email-template-setting-thanksImageHeight">
                  Thanks Image Height
                </Label>
                <AvField id="email-template-setting-thanksImageHeight" type="text" name="thanksImageHeight" />
              </AvGroup>
              <AvGroup>
                <Label id="thanksImageWidthLabel" for="email-template-setting-thanksImageWidth">
                  Thanks Image Width
                </Label>
                <AvField id="email-template-setting-thanksImageWidth" type="text" name="thanksImageWidth" />
              </AvGroup>
              <AvGroup>
                <Label id="logoAlignmentLabel" for="email-template-setting-logoAlignment">
                  Logo Alignment
                </Label>
                <AvField id="email-template-setting-logoAlignment" type="text" name="logoAlignment" />
              </AvGroup>
              <AvGroup>
                <Label id="thanksImageAlignmentLabel" for="email-template-setting-thanksImageAlignment">
                  Thanks Image Alignment
                </Label>
                <AvField id="email-template-setting-thanksImageAlignment" type="text" name="thanksImageAlignment" />
              </AvGroup>
              <AvGroup>
                <Label id="shippingAddressLabel" for="email-template-setting-shippingAddress">
                  Shipping Address
                </Label>
                <AvInput
                  id="email-template-setting-shippingAddress"
                  type="textarea"
                  name="shippingAddress"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="billingAddressLabel" for="email-template-setting-billingAddress">
                  Billing Address
                </Label>
                <AvInput
                  id="email-template-setting-billingAddress"
                  type="textarea"
                  name="billingAddress"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="replyToLabel" for="email-template-setting-replyTo">
                  Reply To
                </Label>
                <AvField id="email-template-setting-replyTo" type="text" name="replyTo" />
              </AvGroup>
              <AvGroup check>
                <Label id="sendBCCEmailFlagLabel">
                  <AvInput
                    id="email-template-setting-sendBCCEmailFlag"
                    type="checkbox"
                    className="form-check-input"
                    name="sendBCCEmailFlag"
                  />
                  Send BCC Email Flag
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/email-template-setting" replace color="info">
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
  emailTemplateSettingEntity: storeState.emailTemplateSetting.entity,
  loading: storeState.emailTemplateSetting.loading,
  updating: storeState.emailTemplateSetting.updating,
  updateSuccess: storeState.emailTemplateSetting.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmailTemplateSettingUpdate);
