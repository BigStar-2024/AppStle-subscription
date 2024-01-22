import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './theme-settings.reducer';
import { IThemeSettings } from 'app/shared/model/theme-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IThemeSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ThemeSettings = (props: IThemeSettingsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { themeSettingsList, match } = props;
  return (
    <div>
      <h2 id="theme-settings-heading">
        Theme Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Theme Settings
        </Link>
      </h2>
      <div className="table-responsive">
        {themeSettingsList && themeSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Skip Setting Theme</th>
                <th>Theme V 2 Saved</th>
                <th>Theme Name</th>
                <th>Shopify Theme Installation Version</th>
                <th>Selected Selector</th>
                <th>Subscription Link Selector</th>
                <th>Custom Javascript</th>
                <th>Placement</th>
                <th>Subscription Link Placement</th>
                <th>Price Selector</th>
                <th>Price Placement</th>
                <th>Badge Top</th>
                <th>Disable Loading Jquery</th>
                <th>Quick View Click Selector</th>
                <th>Landing Page Price Selector</th>
                <th>Cart Row Selector</th>
                <th>Cart Line Item Selector</th>
                <th>Cart Line Item Per Quantity Price Selector</th>
                <th>Cart Line Item Total Price Selector</th>
                <th>Cart Line Item Selling Plan Name Selector</th>
                <th>Cart Sub Total Selector</th>
                <th>Cart Line Item Price Selector</th>
                <th>Enable Cart Widget Feature</th>
                <th>Enable Slow Script Load</th>
                <th>Script Load Delay</th>
                <th>Format Money Override</th>
                <th>Widget Template Type</th>
                <th>Widget Template Html</th>
                <th>Cart Hidden Attributes Selector</th>
                <th>Script Attributes</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {themeSettingsList.map((themeSettings, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${themeSettings.id}`} color="link" size="sm">
                      {themeSettings.id}
                    </Button>
                  </td>
                  <td>{themeSettings.shop}</td>
                  <td>{themeSettings.skip_setting_theme ? 'true' : 'false'}</td>
                  <td>{themeSettings.themeV2Saved ? 'true' : 'false'}</td>
                  <td>{themeSettings.themeName}</td>
                  <td>{themeSettings.shopifyThemeInstallationVersion}</td>
                  <td>{themeSettings.selectedSelector}</td>
                  <td>{themeSettings.subscriptionLinkSelector}</td>
                  <td>{themeSettings.customJavascript}</td>
                  <td>{themeSettings.placement}</td>
                  <td>{themeSettings.subscriptionLinkPlacement}</td>
                  <td>{themeSettings.priceSelector}</td>
                  <td>{themeSettings.pricePlacement}</td>
                  <td>{themeSettings.badgeTop}</td>
                  <td>{themeSettings.disableLoadingJquery ? 'true' : 'false'}</td>
                  <td>{themeSettings.quickViewClickSelector}</td>
                  <td>{themeSettings.landingPagePriceSelector}</td>
                  <td>{themeSettings.cartRowSelector}</td>
                  <td>{themeSettings.cartLineItemSelector}</td>
                  <td>{themeSettings.cartLineItemPerQuantityPriceSelector}</td>
                  <td>{themeSettings.cartLineItemTotalPriceSelector}</td>
                  <td>{themeSettings.cartLineItemSellingPlanNameSelector}</td>
                  <td>{themeSettings.cartSubTotalSelector}</td>
                  <td>{themeSettings.cartLineItemPriceSelector}</td>
                  <td>{themeSettings.enableCartWidgetFeature ? 'true' : 'false'}</td>
                  <td>{themeSettings.enableSlowScriptLoad ? 'true' : 'false'}</td>
                  <td>{themeSettings.scriptLoadDelay}</td>
                  <td>{themeSettings.formatMoneyOverride ? 'true' : 'false'}</td>
                  <td>{themeSettings.widgetTemplateType}</td>
                  <td>{themeSettings.widgetTemplateHtml}</td>
                  <td>{themeSettings.cartHiddenAttributesSelector}</td>
                  <td>{themeSettings.scriptAttributes}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${themeSettings.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${themeSettings.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${themeSettings.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Theme Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ themeSettings }: IRootState) => ({
  themeSettingsList: themeSettings.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ThemeSettings);
