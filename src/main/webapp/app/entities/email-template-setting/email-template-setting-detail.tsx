import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './email-template-setting.reducer';
import { IEmailTemplateSetting } from 'app/shared/model/email-template-setting.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmailTemplateSettingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmailTemplateSettingDetail = (props: IEmailTemplateSettingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { emailTemplateSettingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          EmailTemplateSetting [<b>{emailTemplateSettingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{emailTemplateSettingEntity.shop}</dd>
          <dt>
            <span id="emailSettingType">Email Setting Type</span>
          </dt>
          <dd>{emailTemplateSettingEntity.emailSettingType}</dd>
          <dt>
            <span id="sendEmailDisabled">Send Email Disabled</span>
          </dt>
          <dd>{emailTemplateSettingEntity.sendEmailDisabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="subject">Subject</span>
          </dt>
          <dd>{emailTemplateSettingEntity.subject}</dd>
          <dt>
            <span id="fromEmail">From Email</span>
          </dt>
          <dd>{emailTemplateSettingEntity.fromEmail}</dd>
          <dt>
            <span id="logo">Logo</span>
          </dt>
          <dd>{emailTemplateSettingEntity.logo}</dd>
          <dt>
            <span id="heading">Heading</span>
          </dt>
          <dd>{emailTemplateSettingEntity.heading}</dd>
          <dt>
            <span id="headingTextColor">Heading Text Color</span>
          </dt>
          <dd>{emailTemplateSettingEntity.headingTextColor}</dd>
          <dt>
            <span id="contentTextColor">Content Text Color</span>
          </dt>
          <dd>{emailTemplateSettingEntity.contentTextColor}</dd>
          <dt>
            <span id="contentLinkColor">Content Link Color</span>
          </dt>
          <dd>{emailTemplateSettingEntity.contentLinkColor}</dd>
          <dt>
            <span id="content">Content</span>
          </dt>
          <dd>{emailTemplateSettingEntity.content}</dd>
          <dt>
            <span id="footerTextColor">Footer Text Color</span>
          </dt>
          <dd>{emailTemplateSettingEntity.footerTextColor}</dd>
          <dt>
            <span id="footerLinkColor">Footer Link Color</span>
          </dt>
          <dd>{emailTemplateSettingEntity.footerLinkColor}</dd>
          <dt>
            <span id="footerText">Footer Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.footerText}</dd>
          <dt>
            <span id="templateBackgroundColor">Template Background Color</span>
          </dt>
          <dd>{emailTemplateSettingEntity.templateBackgroundColor}</dd>
          <dt>
            <span id="html">Html</span>
          </dt>
          <dd>{emailTemplateSettingEntity.html}</dd>
          <dt>
            <span id="textImageUrl">Text Image Url</span>
          </dt>
          <dd>{emailTemplateSettingEntity.textImageUrl}</dd>
          <dt>
            <span id="manageSubscriptionButtonColor">Manage Subscription Button Color</span>
          </dt>
          <dd>{emailTemplateSettingEntity.manageSubscriptionButtonColor}</dd>
          <dt>
            <span id="manageSubscriptionButtonText">Manage Subscription Button Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.manageSubscriptionButtonText}</dd>
          <dt>
            <span id="manageSubscriptionButtonTextColor">Manage Subscription Button Text Color</span>
          </dt>
          <dd>{emailTemplateSettingEntity.manageSubscriptionButtonTextColor}</dd>
          <dt>
            <span id="shippingAddressText">Shipping Address Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.shippingAddressText}</dd>
          <dt>
            <span id="billingAddressText">Billing Address Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.billingAddressText}</dd>
          <dt>
            <span id="nextOrderdateText">Next Orderdate Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.nextOrderdateText}</dd>
          <dt>
            <span id="paymentMethodText">Payment Method Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.paymentMethodText}</dd>
          <dt>
            <span id="endingInText">Ending In Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.endingInText}</dd>
          <dt>
            <span id="bccEmail">Bcc Email</span>
          </dt>
          <dd>{emailTemplateSettingEntity.bccEmail}</dd>
          <dt>
            <span id="upcomingOrderEmailBuffer">Upcoming Order Email Buffer</span>
          </dt>
          <dd>{emailTemplateSettingEntity.upcomingOrderEmailBuffer}</dd>
          <dt>
            <span id="headingImageUrl">Heading Image Url</span>
          </dt>
          <dd>{emailTemplateSettingEntity.headingImageUrl}</dd>
          <dt>
            <span id="quantityText">Quantity Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.quantityText}</dd>
          <dt>
            <span id="manageSubscriptionButtonUrl">Manage Subscription Button Url</span>
          </dt>
          <dd>{emailTemplateSettingEntity.manageSubscriptionButtonUrl}</dd>
          <dt>
            <span id="logoHeight">Logo Height</span>
          </dt>
          <dd>{emailTemplateSettingEntity.logoHeight}</dd>
          <dt>
            <span id="logoWidth">Logo Width</span>
          </dt>
          <dd>{emailTemplateSettingEntity.logoWidth}</dd>
          <dt>
            <span id="thanksImageHeight">Thanks Image Height</span>
          </dt>
          <dd>{emailTemplateSettingEntity.thanksImageHeight}</dd>
          <dt>
            <span id="thanksImageWidth">Thanks Image Width</span>
          </dt>
          <dd>{emailTemplateSettingEntity.thanksImageWidth}</dd>
          <dt>
            <span id="logoAlignment">Logo Alignment</span>
          </dt>
          <dd>{emailTemplateSettingEntity.logoAlignment}</dd>
          <dt>
            <span id="thanksImageAlignment">Thanks Image Alignment</span>
          </dt>
          <dd>{emailTemplateSettingEntity.thanksImageAlignment}</dd>
          <dt>
            <span id="shippingAddress">Shipping Address</span>
          </dt>
          <dd>{emailTemplateSettingEntity.shippingAddress}</dd>
          <dt>
            <span id="billingAddress">Billing Address</span>
          </dt>
          <dd>{emailTemplateSettingEntity.billingAddress}</dd>
          <dt>
            <span id="replyTo">Reply To</span>
          </dt>
          <dd>{emailTemplateSettingEntity.replyTo}</dd>
          <dt>
            <span id="sendBCCEmailFlag">Send BCC Email Flag</span>
          </dt>
          <dd>{emailTemplateSettingEntity.sendBCCEmailFlag ? 'true' : 'false'}</dd>
          <dt>
            <span id="sellingPlanNameText">Selling Plan Name Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.sellingPlanNameText}</dd>
          <dt>
            <span id="variantSkuText">Variant Sku Text</span>
          </dt>
          <dd>{emailTemplateSettingEntity.variantSkuText}</dd>
        </dl>
        <Button tag={Link} to="/email-template-setting" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/email-template-setting/${emailTemplateSettingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ emailTemplateSetting }: IRootState) => ({
  emailTemplateSettingEntity: emailTemplateSetting.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmailTemplateSettingDetail);
