import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './email-template-setting.reducer';
import { IEmailTemplateSetting } from 'app/shared/model/email-template-setting.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmailTemplateSettingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const EmailTemplateSetting = (props: IEmailTemplateSettingProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { emailTemplateSettingList, match } = props;
  return (
    <div>
      <h2 id="email-template-setting-heading">
        Email Template Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Email Template Setting
        </Link>
      </h2>
      <div className="table-responsive">
        {emailTemplateSettingList && emailTemplateSettingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Email Setting Type</th>
                <th>Send Email Disabled</th>
                <th>Subject</th>
                <th>From Email</th>
                <th>Logo</th>
                <th>Heading</th>
                <th>Heading Text Color</th>
                <th>Content Text Color</th>
                <th>Content Link Color</th>
                <th>Content</th>
                <th>Footer Text Color</th>
                <th>Footer Link Color</th>
                <th>Footer Text</th>
                <th>Template Background Color</th>
                <th>Html</th>
                <th>Text Image Url</th>
                <th>Manage Subscription Button Color</th>
                <th>Manage Subscription Button Text</th>
                <th>Manage Subscription Button Text Color</th>
                <th>Shipping Address Text</th>
                <th>Billing Address Text</th>
                <th>Next Orderdate Text</th>
                <th>Payment Method Text</th>
                <th>Ending In Text</th>
                <th>Bcc Email</th>
                <th>Upcoming Order Email Buffer</th>
                <th>Heading Image Url</th>
                <th>Quantity Text</th>
                <th>Manage Subscription Button Url</th>
                <th>Logo Height</th>
                <th>Logo Width</th>
                <th>Thanks Image Height</th>
                <th>Thanks Image Width</th>
                <th>Logo Alignment</th>
                <th>Thanks Image Alignment</th>
                <th>Shipping Address</th>
                <th>Billing Address</th>
                <th>Reply To</th>
                <th>Send BCC Email Flag</th>
                <th>Selling Plan Name Text</th>
                <th>Variant Sku Text</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {emailTemplateSettingList.map((emailTemplateSetting, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${emailTemplateSetting.id}`} color="link" size="sm">
                      {emailTemplateSetting.id}
                    </Button>
                  </td>
                  <td>{emailTemplateSetting.shop}</td>
                  <td>{emailTemplateSetting.emailSettingType}</td>
                  <td>{emailTemplateSetting.sendEmailDisabled ? 'true' : 'false'}</td>
                  <td>{emailTemplateSetting.subject}</td>
                  <td>{emailTemplateSetting.fromEmail}</td>
                  <td>{emailTemplateSetting.logo}</td>
                  <td>{emailTemplateSetting.heading}</td>
                  <td>{emailTemplateSetting.headingTextColor}</td>
                  <td>{emailTemplateSetting.contentTextColor}</td>
                  <td>{emailTemplateSetting.contentLinkColor}</td>
                  <td>{emailTemplateSetting.content}</td>
                  <td>{emailTemplateSetting.footerTextColor}</td>
                  <td>{emailTemplateSetting.footerLinkColor}</td>
                  <td>{emailTemplateSetting.footerText}</td>
                  <td>{emailTemplateSetting.templateBackgroundColor}</td>
                  <td>{emailTemplateSetting.html}</td>
                  <td>{emailTemplateSetting.textImageUrl}</td>
                  <td>{emailTemplateSetting.manageSubscriptionButtonColor}</td>
                  <td>{emailTemplateSetting.manageSubscriptionButtonText}</td>
                  <td>{emailTemplateSetting.manageSubscriptionButtonTextColor}</td>
                  <td>{emailTemplateSetting.shippingAddressText}</td>
                  <td>{emailTemplateSetting.billingAddressText}</td>
                  <td>{emailTemplateSetting.nextOrderdateText}</td>
                  <td>{emailTemplateSetting.paymentMethodText}</td>
                  <td>{emailTemplateSetting.endingInText}</td>
                  <td>{emailTemplateSetting.bccEmail}</td>
                  <td>{emailTemplateSetting.upcomingOrderEmailBuffer}</td>
                  <td>{emailTemplateSetting.headingImageUrl}</td>
                  <td>{emailTemplateSetting.quantityText}</td>
                  <td>{emailTemplateSetting.manageSubscriptionButtonUrl}</td>
                  <td>{emailTemplateSetting.logoHeight}</td>
                  <td>{emailTemplateSetting.logoWidth}</td>
                  <td>{emailTemplateSetting.thanksImageHeight}</td>
                  <td>{emailTemplateSetting.thanksImageWidth}</td>
                  <td>{emailTemplateSetting.logoAlignment}</td>
                  <td>{emailTemplateSetting.thanksImageAlignment}</td>
                  <td>{emailTemplateSetting.shippingAddress}</td>
                  <td>{emailTemplateSetting.billingAddress}</td>
                  <td>{emailTemplateSetting.replyTo}</td>
                  <td>{emailTemplateSetting.sendBCCEmailFlag ? 'true' : 'false'}</td>
                  <td>{emailTemplateSetting.sellingPlanNameText}</td>
                  <td>{emailTemplateSetting.variantSkuText}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${emailTemplateSetting.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${emailTemplateSetting.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${emailTemplateSetting.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Email Template Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ emailTemplateSetting }: IRootState) => ({
  emailTemplateSettingList: emailTemplateSetting.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmailTemplateSetting);
