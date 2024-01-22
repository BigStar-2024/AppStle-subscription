import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './theme-settings.reducer';
import { IThemeSettings } from 'app/shared/model/theme-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IThemeSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ThemeSettingsUpdate = (props: IThemeSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { themeSettingsEntity, loading, updating } = props;

  const {
    selectedSelector,
    subscriptionLinkSelector,
    customJavascript,
    priceSelector,
    badgeTop,
    quickViewClickSelector,
    landingPagePriceSelector,
    cartRowSelector,
    cartLineItemSelector,
    cartLineItemPerQuantityPriceSelector,
    cartLineItemTotalPriceSelector,
    cartLineItemSellingPlanNameSelector,
    cartSubTotalSelector,
    cartLineItemPriceSelector,
    widgetTemplateHtml
  } = themeSettingsEntity;

  const handleClose = () => {
    props.history.push('/admin/theme-settings');
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
        ...themeSettingsEntity,
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
          <h2 id="subscriptionApp.themeSettings.home.createOrEditLabel">Create or edit a ThemeSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : themeSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="theme-settings-id">ID</Label>
                  <AvInput id="theme-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="theme-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="theme-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="skip_setting_themeLabel">
                  <AvInput id="theme-settings-skip_setting_theme" type="checkbox" className="form-check-input" name="skip_setting_theme" />
                  Skip Setting Theme
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="themeV2SavedLabel">
                  <AvInput id="theme-settings-themeV2Saved" type="checkbox" className="form-check-input" name="themeV2Saved" />
                  Theme V 2 Saved
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="themeNameLabel" for="theme-settings-themeName">
                  Theme Name
                </Label>
                <AvField id="theme-settings-themeName" type="text" name="themeName" />
              </AvGroup>
              <AvGroup>
                <Label id="shopifyThemeInstallationVersionLabel" for="theme-settings-shopifyThemeInstallationVersion">
                  Shopify Theme Installation Version
                </Label>
                <AvInput
                  id="theme-settings-shopifyThemeInstallationVersion"
                  type="select"
                  className="form-control"
                  name="shopifyThemeInstallationVersion"
                  value={(!isNew && themeSettingsEntity.shopifyThemeInstallationVersion) || 'V1'}
                >
                  <option value="V1">V1 (Deprecated)</option>
                  <option value="V2">V2</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="selectedSelectorLabel" for="theme-settings-selectedSelector">
                  Selected Selector
                </Label>
                <AvInput id="theme-settings-selectedSelector" type="textarea" name="selectedSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionLinkSelectorLabel" for="theme-settings-subscriptionLinkSelector">
                  Subscription Link Selector
                </Label>
                <AvInput id="theme-settings-subscriptionLinkSelector" type="textarea" name="subscriptionLinkSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="customJavascriptLabel" for="theme-settings-customJavascript">
                  Custom Javascript
                </Label>
                <AvInput id="theme-settings-customJavascript" type="textarea" name="customJavascript" />
              </AvGroup>
              <AvGroup>
                <Label id="placementLabel" for="theme-settings-placement">
                  Placement
                </Label>
                <AvInput
                  id="theme-settings-placement"
                  type="select"
                  className="form-control"
                  name="placement"
                  value={(!isNew && themeSettingsEntity.placement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionLinkPlacementLabel" for="theme-settings-subscriptionLinkPlacement">
                  Subscription Link Placement
                </Label>
                <AvInput
                  id="theme-settings-subscriptionLinkPlacement"
                  type="select"
                  className="form-control"
                  name="subscriptionLinkPlacement"
                  value={(!isNew && themeSettingsEntity.subscriptionLinkPlacement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="priceSelectorLabel" for="theme-settings-priceSelector">
                  Price Selector
                </Label>
                <AvInput id="theme-settings-priceSelector" type="textarea" name="priceSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="pricePlacementLabel" for="theme-settings-pricePlacement">
                  Price Placement
                </Label>
                <AvInput
                  id="theme-settings-pricePlacement"
                  type="select"
                  className="form-control"
                  name="pricePlacement"
                  value={(!isNew && themeSettingsEntity.pricePlacement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="badgeTopLabel" for="theme-settings-badgeTop">
                  Badge Top
                </Label>
                <AvInput id="theme-settings-badgeTop" type="textarea" name="badgeTop" />
              </AvGroup>
              <AvGroup check>
                <Label id="disableLoadingJqueryLabel">
                  <AvInput
                    id="theme-settings-disableLoadingJquery"
                    type="checkbox"
                    className="form-check-input"
                    name="disableLoadingJquery"
                  />
                  Disable Loading Jquery
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="quickViewClickSelectorLabel" for="theme-settings-quickViewClickSelector">
                  Quick View Click Selector
                </Label>
                <AvInput id="theme-settings-quickViewClickSelector" type="textarea" name="quickViewClickSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="landingPagePriceSelectorLabel" for="theme-settings-landingPagePriceSelector">
                  Landing Page Price Selector
                </Label>
                <AvInput id="theme-settings-landingPagePriceSelector" type="textarea" name="landingPagePriceSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartRowSelectorLabel" for="theme-settings-cartRowSelector">
                  Cart Row Selector
                </Label>
                <AvInput id="theme-settings-cartRowSelector" type="textarea" name="cartRowSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemSelectorLabel" for="theme-settings-cartLineItemSelector">
                  Cart Line Item Selector
                </Label>
                <AvInput id="theme-settings-cartLineItemSelector" type="textarea" name="cartLineItemSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemPerQuantityPriceSelectorLabel" for="theme-settings-cartLineItemPerQuantityPriceSelector">
                  Cart Line Item Per Quantity Price Selector
                </Label>
                <AvInput
                  id="theme-settings-cartLineItemPerQuantityPriceSelector"
                  type="textarea"
                  name="cartLineItemPerQuantityPriceSelector"
                />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemTotalPriceSelectorLabel" for="theme-settings-cartLineItemTotalPriceSelector">
                  Cart Line Item Total Price Selector
                </Label>
                <AvInput id="theme-settings-cartLineItemTotalPriceSelector" type="textarea" name="cartLineItemTotalPriceSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemSellingPlanNameSelectorLabel" for="theme-settings-cartLineItemSellingPlanNameSelector">
                  Cart Line Item Selling Plan Name Selector
                </Label>
                <AvInput
                  id="theme-settings-cartLineItemSellingPlanNameSelector"
                  type="textarea"
                  name="cartLineItemSellingPlanNameSelector"
                />
              </AvGroup>
              <AvGroup>
                <Label id="cartSubTotalSelectorLabel" for="theme-settings-cartSubTotalSelector">
                  Cart Sub Total Selector
                </Label>
                <AvInput id="theme-settings-cartSubTotalSelector" type="textarea" name="cartSubTotalSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemPriceSelectorLabel" for="theme-settings-cartLineItemPriceSelector">
                  Cart Line Item Price Selector
                </Label>
                <AvInput id="theme-settings-cartLineItemPriceSelector" type="textarea" name="cartLineItemPriceSelector" />
              </AvGroup>
              <AvGroup check>
                <Label id="enableCartWidgetFeatureLabel">
                  <AvInput
                    id="theme-settings-enableCartWidgetFeature"
                    type="checkbox"
                    className="form-check-input"
                    name="enableCartWidgetFeature"
                  />
                  Enable Cart Widget Feature
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="enableSlowScriptLoadLabel">
                  <AvInput
                    id="theme-settings-enableSlowScriptLoad"
                    type="checkbox"
                    className="form-check-input"
                    name="enableSlowScriptLoad"
                  />
                  Enable Slow Script Load
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="scriptLoadDelayLabel" for="theme-settings-scriptLoadDelay">
                  Script Load Delay
                </Label>
                <AvField id="theme-settings-scriptLoadDelay" type="string" className="form-control" name="scriptLoadDelay" />
              </AvGroup>
              <AvGroup check>
                <Label id="formatMoneyOverrideLabel">
                  <AvInput
                    id="theme-settings-formatMoneyOverride"
                    type="checkbox"
                    className="form-check-input"
                    name="formatMoneyOverride"
                  />
                  Format Money Override
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="widgetTemplateTypeLabel" for="theme-settings-widgetTemplateType">
                  Widget Template Type
                </Label>
                <AvInput
                  id="theme-settings-widgetTemplateType"
                  type="select"
                  className="form-control"
                  name="widgetTemplateType"
                  value={(!isNew && themeSettingsEntity.widgetTemplateType) || 'WIDGET_TYPE_1'}
                >
                  <option value="WIDGET_TYPE_1">WIDGET_TYPE_1</option>
                  <option value="WIDGET_TYPE_2">WIDGET_TYPE_2</option>
                  <option value="WIDGET_TYPE_3">WIDGET_TYPE_3</option>
                  <option value="WIDGET_TYPE_4">WIDGET_TYPE_4</option>
                  <option value="WIDGET_TYPE_5">WIDGET_TYPE_5</option>
                  <option value="WIDGET_TYPE_6">WIDGET_TYPE_6</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="widgetTemplateHtmlLabel" for="theme-settings-widgetTemplateHtml">
                  Widget Template Html
                </Label>
                <AvInput id="theme-settings-widgetTemplateHtml" type="textarea" name="widgetTemplateHtml" />
              </AvGroup>
              <AvGroup>
                <Label id="cartHiddenAttributesSelectorLabel" for="theme-settings-cartHiddenAttributesSelector">
                  Cart Hidden Attributes Selector
                </Label>
                <AvField id="theme-settings-cartHiddenAttributesSelector" type="text" name="cartHiddenAttributesSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="scriptAttributesLabel" for="theme-settings-scriptAttributes">
                  Script Attributes
                </Label>
                <AvField id="theme-settings-scriptAttributes" type="text" name="scriptAttributes" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/theme-settings" replace color="info">
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
  themeSettingsEntity: storeState.themeSettings.entity,
  loading: storeState.themeSettings.loading,
  updating: storeState.themeSettings.updating,
  updateSuccess: storeState.themeSettings.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(ThemeSettingsUpdate);
