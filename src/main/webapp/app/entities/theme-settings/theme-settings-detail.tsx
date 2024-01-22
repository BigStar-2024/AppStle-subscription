import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './theme-settings.reducer';
import { IThemeSettings } from 'app/shared/model/theme-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IThemeSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ThemeSettingsDetail = (props: IThemeSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { themeSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ThemeSettings [<b>{themeSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{themeSettingsEntity.shop}</dd>
          <dt>
            <span id="skip_setting_theme">Skip Setting Theme</span>
          </dt>
          <dd>{themeSettingsEntity.skip_setting_theme ? 'true' : 'false'}</dd>
          <dt>
            <span id="themeV2Saved">Theme V 2 Saved</span>
          </dt>
          <dd>{themeSettingsEntity.themeV2Saved ? 'true' : 'false'}</dd>
          <dt>
            <span id="themeName">Theme Name</span>
          </dt>
          <dd>{themeSettingsEntity.themeName}</dd>
          <dt>
            <span id="shopifyThemeInstallationVersion">Shopify Theme Installation Version</span>
          </dt>
          <dd>{themeSettingsEntity.shopifyThemeInstallationVersion}</dd>
          <dt>
            <span id="selectedSelector">Selected Selector</span>
          </dt>
          <dd>{themeSettingsEntity.selectedSelector}</dd>
          <dt>
            <span id="subscriptionLinkSelector">Subscription Link Selector</span>
          </dt>
          <dd>{themeSettingsEntity.subscriptionLinkSelector}</dd>
          <dt>
            <span id="customJavascript">Custom Javascript</span>
          </dt>
          <dd>{themeSettingsEntity.customJavascript}</dd>
          <dt>
            <span id="placement">Placement</span>
          </dt>
          <dd>{themeSettingsEntity.placement}</dd>
          <dt>
            <span id="subscriptionLinkPlacement">Subscription Link Placement</span>
          </dt>
          <dd>{themeSettingsEntity.subscriptionLinkPlacement}</dd>
          <dt>
            <span id="priceSelector">Price Selector</span>
          </dt>
          <dd>{themeSettingsEntity.priceSelector}</dd>
          <dt>
            <span id="pricePlacement">Price Placement</span>
          </dt>
          <dd>{themeSettingsEntity.pricePlacement}</dd>
          <dt>
            <span id="badgeTop">Badge Top</span>
          </dt>
          <dd>{themeSettingsEntity.badgeTop}</dd>
          <dt>
            <span id="disableLoadingJquery">Disable Loading Jquery</span>
          </dt>
          <dd>{themeSettingsEntity.disableLoadingJquery ? 'true' : 'false'}</dd>
          <dt>
            <span id="quickViewClickSelector">Quick View Click Selector</span>
          </dt>
          <dd>{themeSettingsEntity.quickViewClickSelector}</dd>
          <dt>
            <span id="landingPagePriceSelector">Landing Page Price Selector</span>
          </dt>
          <dd>{themeSettingsEntity.landingPagePriceSelector}</dd>
          <dt>
            <span id="cartRowSelector">Cart Row Selector</span>
          </dt>
          <dd>{themeSettingsEntity.cartRowSelector}</dd>
          <dt>
            <span id="cartLineItemSelector">Cart Line Item Selector</span>
          </dt>
          <dd>{themeSettingsEntity.cartLineItemSelector}</dd>
          <dt>
            <span id="cartLineItemPerQuantityPriceSelector">Cart Line Item Per Quantity Price Selector</span>
          </dt>
          <dd>{themeSettingsEntity.cartLineItemPerQuantityPriceSelector}</dd>
          <dt>
            <span id="cartLineItemTotalPriceSelector">Cart Line Item Total Price Selector</span>
          </dt>
          <dd>{themeSettingsEntity.cartLineItemTotalPriceSelector}</dd>
          <dt>
            <span id="cartLineItemSellingPlanNameSelector">Cart Line Item Selling Plan Name Selector</span>
          </dt>
          <dd>{themeSettingsEntity.cartLineItemSellingPlanNameSelector}</dd>
          <dt>
            <span id="cartSubTotalSelector">Cart Sub Total Selector</span>
          </dt>
          <dd>{themeSettingsEntity.cartSubTotalSelector}</dd>
          <dt>
            <span id="cartLineItemPriceSelector">Cart Line Item Price Selector</span>
          </dt>
          <dd>{themeSettingsEntity.cartLineItemPriceSelector}</dd>
          <dt>
            <span id="enableCartWidgetFeature">Enable Cart Widget Feature</span>
          </dt>
          <dd>{themeSettingsEntity.enableCartWidgetFeature ? 'true' : 'false'}</dd>
          <dt>
            <span id="enableSlowScriptLoad">Enable Slow Script Load</span>
          </dt>
          <dd>{themeSettingsEntity.enableSlowScriptLoad ? 'true' : 'false'}</dd>
          <dt>
            <span id="scriptLoadDelay">Script Load Delay</span>
          </dt>
          <dd>{themeSettingsEntity.scriptLoadDelay}</dd>
          <dt>
            <span id="formatMoneyOverride">Format Money Override</span>
          </dt>
          <dd>{themeSettingsEntity.formatMoneyOverride ? 'true' : 'false'}</dd>
          <dt>
            <span id="widgetTemplateType">Widget Template Type</span>
          </dt>
          <dd>{themeSettingsEntity.widgetTemplateType}</dd>
          <dt>
            <span id="widgetTemplateHtml">Widget Template Html</span>
          </dt>
          <dd>{themeSettingsEntity.widgetTemplateHtml}</dd>
          <dt>
            <span id="cartHiddenAttributesSelector">Cart Hidden Attributes Selector</span>
          </dt>
          <dd>{themeSettingsEntity.cartHiddenAttributesSelector}</dd>
          <dt>
            <span id="scriptAttributes">Script Attributes</span>
          </dt>
          <dd>{themeSettingsEntity.scriptAttributes}</dd>
        </dl>
        <Button tag={Link} to="/admin/theme-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/theme-settings/${themeSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ themeSettings }: IRootState) => ({
  themeSettingsEntity: themeSettings.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ThemeSettingsDetail);
