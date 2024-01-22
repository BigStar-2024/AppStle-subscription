import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-widget-settings.reducer';
import { ISubscriptionWidgetSettings } from 'app/shared/model/subscription-widget-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionWidgetSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionWidgetSettings = (props: ISubscriptionWidgetSettingsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { subscriptionWidgetSettingsList, match, loading } = props;
  return (
    <div>
      <h2 id="subscription-widget-settings-heading">
        Subscription Widget Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Subscription Widget Settings
        </Link>
      </h2>
      <div className="table-responsive">
        {subscriptionWidgetSettingsList && subscriptionWidgetSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>One Time Purchase Text</th>
                <th>Delivery Text</th>
                <th>Purchase Options Text</th>
                <th>Subscription Option Text</th>
                <th>Selling Plan Select Title</th>
                <th>Tooltip Title</th>
                <th>Tooltip Desctiption</th>
                <th>Subscription Widget Margin Top</th>
                <th>Subscription Widget Margin Bottom</th>
                <th>Subscription Wrapper Border Width</th>
                <th>Subscription Wrapper Border Color</th>
                <th>Circle Border Color</th>
                <th>Dot Background Color</th>
                <th>Select Padding Top</th>
                <th>Select Padding Bottom</th>
                <th>Select Padding Left</th>
                <th>Select Padding Right</th>
                <th>Select Border Width</th>
                <th>Select Border Style</th>
                <th>Select Border Color</th>
                <th>Select Border Radius</th>
                <th>Tooltip Subscription Svg Fill</th>
                <th>Tooltip Color</th>
                <th>Tooltip Background Color</th>
                <th>Tooltip Border Top Color Border Top Color</th>
                <th>Subscription Final Price Color</th>
                <th>Subscription Widget Text Color</th>
                <th>Order Status Manage Subscription Title</th>
                <th>Order Status Manage Subscription Description</th>
                <th>Order Status Manage Subscription Button Text</th>
                <th>Show Tooltip On Click</th>
                <th>Subscription Option Selected By Default</th>
                <th>Widget Enabled</th>
                <th>Show Tooltip</th>
                <th>Selling Plan Title Text</th>
                <th>One Time Price Text</th>
                <th>Selected Pay As You Go Selling Plan Price Text</th>
                <th>Selected Prepaid Selling Plan Price Text</th>
                <th>Selected Discount Format</th>
                <th>Manage Subscription Btn Format</th>
                <th>Tooltip Description On Prepaid Plan</th>
                <th>Tooltip Description On Multiple Discount</th>
                <th>Tooltip Description Customization</th>
                <th>Show Static Tooltip</th>
                <th>Show Appstle Link</th>
                <th>Subscription Price Display Text</th>
                <th>Sort By Default Sequence</th>
                <th>Show Sub Option Before One Time</th>
                <th>Show Checkout Subscription Btn</th>
                <th>Total Price Per Delivery Text</th>
                <th>Widget Enabled On Sold Variant</th>
                <th>Enable Cart Widget Feature</th>
                <th>Switch Radio Button Widget</th>
                <th>Form Mapping Attribute Name</th>
                <th>Form Mapping Attribute Selector</th>
                <th>Quick View Modal Polling Selector</th>
                <th>Update Price On Quantity Change</th>
                <th>Widget Parent Selector</th>
                <th>Quantity Selector</th>
                <th>Loyalty Details Label Text</th>
                <th>Loyalty Perk Description Text</th>
                <th>Detect Variant From URL Params</th>
                <th>Disable Query Params Update</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionWidgetSettingsList.map((subscriptionWidgetSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionWidgetSettings.id}`} color="link" size="sm">
                      {subscriptionWidgetSettings.id}
                    </Button>
                  </td>
                  <td>{subscriptionWidgetSettings.shop}</td>
                  <td>{subscriptionWidgetSettings.oneTimePurchaseText}</td>
                  <td>{subscriptionWidgetSettings.deliveryText}</td>
                  <td>{subscriptionWidgetSettings.purchaseOptionsText}</td>
                  <td>{subscriptionWidgetSettings.subscriptionOptionText}</td>
                  <td>{subscriptionWidgetSettings.sellingPlanSelectTitle}</td>
                  <td>{subscriptionWidgetSettings.tooltipTitle}</td>
                  <td>{subscriptionWidgetSettings.tooltipDesctiption}</td>
                  <td>{subscriptionWidgetSettings.subscriptionWidgetMarginTop}</td>
                  <td>{subscriptionWidgetSettings.subscriptionWidgetMarginBottom}</td>
                  <td>{subscriptionWidgetSettings.subscriptionWrapperBorderWidth}</td>
                  <td>{subscriptionWidgetSettings.subscriptionWrapperBorderColor}</td>
                  <td>{subscriptionWidgetSettings.circleBorderColor}</td>
                  <td>{subscriptionWidgetSettings.dotBackgroundColor}</td>
                  <td>{subscriptionWidgetSettings.selectPaddingTop}</td>
                  <td>{subscriptionWidgetSettings.selectPaddingBottom}</td>
                  <td>{subscriptionWidgetSettings.selectPaddingLeft}</td>
                  <td>{subscriptionWidgetSettings.selectPaddingRight}</td>
                  <td>{subscriptionWidgetSettings.selectBorderWidth}</td>
                  <td>{subscriptionWidgetSettings.selectBorderStyle}</td>
                  <td>{subscriptionWidgetSettings.selectBorderColor}</td>
                  <td>{subscriptionWidgetSettings.selectBorderRadius}</td>
                  <td>{subscriptionWidgetSettings.tooltipSubscriptionSvgFill}</td>
                  <td>{subscriptionWidgetSettings.tooltipColor}</td>
                  <td>{subscriptionWidgetSettings.tooltipBackgroundColor}</td>
                  <td>{subscriptionWidgetSettings.tooltipBorderTopColorBorderTopColor}</td>
                  <td>{subscriptionWidgetSettings.subscriptionFinalPriceColor}</td>
                  <td>{subscriptionWidgetSettings.subscriptionWidgetTextColor}</td>
                  <td>{subscriptionWidgetSettings.orderStatusManageSubscriptionTitle}</td>
                  <td>{subscriptionWidgetSettings.orderStatusManageSubscriptionDescription}</td>
                  <td>{subscriptionWidgetSettings.orderStatusManageSubscriptionButtonText}</td>
                  <td>{subscriptionWidgetSettings.showTooltipOnClick ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.subscriptionOptionSelectedByDefault ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.widgetEnabled ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.showTooltip ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.sellingPlanTitleText}</td>
                  <td>{subscriptionWidgetSettings.oneTimePriceText}</td>
                  <td>{subscriptionWidgetSettings.selectedPayAsYouGoSellingPlanPriceText}</td>
                  <td>{subscriptionWidgetSettings.selectedPrepaidSellingPlanPriceText}</td>
                  <td>{subscriptionWidgetSettings.selectedDiscountFormat}</td>
                  <td>{subscriptionWidgetSettings.manageSubscriptionBtnFormat}</td>
                  <td>{subscriptionWidgetSettings.tooltipDescriptionOnPrepaidPlan}</td>
                  <td>{subscriptionWidgetSettings.tooltipDescriptionOnMultipleDiscount}</td>
                  <td>{subscriptionWidgetSettings.tooltipDescriptionCustomization}</td>
                  <td>{subscriptionWidgetSettings.showStaticTooltip ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.showAppstleLink ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.subscriptionPriceDisplayText}</td>
                  <td>{subscriptionWidgetSettings.sortByDefaultSequence ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.showSubOptionBeforeOneTime ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.showCheckoutSubscriptionBtn ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.totalPricePerDeliveryText}</td>
                  <td>{subscriptionWidgetSettings.widgetEnabledOnSoldVariant ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.enableCartWidgetFeature ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.switchRadioButtonWidget ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.formMappingAttributeName}</td>
                  <td>{subscriptionWidgetSettings.formMappingAttributeSelector}</td>
                  <td>{subscriptionWidgetSettings.quickViewModalPollingSelector}</td>
                  <td>{subscriptionWidgetSettings.updatePriceOnQuantityChange}</td>
                  <td>{subscriptionWidgetSettings.widgetParentSelector}</td>
                  <td>{subscriptionWidgetSettings.quantitySelector}</td>
                  <td>{subscriptionWidgetSettings.loyaltyDetailsLabelText}</td>
                  <td>{subscriptionWidgetSettings.loyaltyPerkDescriptionText}</td>
                  <td>{subscriptionWidgetSettings.detectVariantFromURLParams ? 'true' : 'false'}</td>
                  <td>{subscriptionWidgetSettings.disableQueryParamsUpdate ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionWidgetSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionWidgetSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subscriptionWidgetSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Subscription Widget Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionWidgetSettings }: IRootState) => ({
  subscriptionWidgetSettingsList: subscriptionWidgetSettings.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionWidgetSettings);
