import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './cart-widget-settings.reducer';
import { ICartWidgetSettings } from 'app/shared/model/cart-widget-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICartWidgetSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CartWidgetSettingsUpdate = (props: ICartWidgetSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { cartWidgetSettingsEntity, loading, updating } = props;

  const { cartRowSelector, cartLineItemSelector, cartFormSelector, appstelCustomeSelector } = cartWidgetSettingsEntity;

  const handleClose = () => {
    props.history.push('/cart-widget-settings');
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
        ...cartWidgetSettingsEntity,
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
          <h2 id="subscriptionApp.cartWidgetSettings.home.createOrEditLabel">Create or edit a CartWidgetSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : cartWidgetSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="cart-widget-settings-id">ID</Label>
                  <AvInput id="cart-widget-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="cart-widget-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="cart-widget-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="enable_cart_widget_settingsLabel">
                  <AvInput
                    id="cart-widget-settings-enable_cart_widget_settings"
                    type="checkbox"
                    className="form-check-input"
                    name="enable_cart_widget_settings"
                  />
                  Enable Cart Widget Settings
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="cartWidgetSettingApproachLabel" for="cart-widget-settings-cartWidgetSettingApproach">
                  Cart Widget Setting Approach
                </Label>
                <AvInput
                  id="cart-widget-settings-cartWidgetSettingApproach"
                  type="select"
                  className="form-control"
                  name="cartWidgetSettingApproach"
                  value={(!isNew && cartWidgetSettingsEntity.cartWidgetSettingApproach) || 'V1'}
                >
                  <option value="V1">V1</option>
                  <option value="V2">V2</option>
                  <option value="V3">V3</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="cartRowSelectorLabel" for="cart-widget-settings-cartRowSelector">
                  Cart Row Selector
                </Label>
                <AvInput id="cart-widget-settings-cartRowSelector" type="textarea" name="cartRowSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartRowPlacementLabel" for="cart-widget-settings-cartRowPlacement">
                  Cart Row Placement
                </Label>
                <AvInput
                  id="cart-widget-settings-cartRowPlacement"
                  type="select"
                  className="form-control"
                  name="cartRowPlacement"
                  value={(!isNew && cartWidgetSettingsEntity.cartRowPlacement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemSelectorLabel" for="cart-widget-settings-cartLineItemSelector">
                  Cart Line Item Selector
                </Label>
                <AvInput id="cart-widget-settings-cartLineItemSelector" type="textarea" name="cartLineItemSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemPlacementLabel" for="cart-widget-settings-cartLineItemPlacement">
                  Cart Line Item Placement
                </Label>
                <AvInput
                  id="cart-widget-settings-cartLineItemPlacement"
                  type="select"
                  className="form-control"
                  name="cartLineItemPlacement"
                  value={(!isNew && cartWidgetSettingsEntity.cartLineItemPlacement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="cartFormSelectorLabel" for="cart-widget-settings-cartFormSelector">
                  Cart Form Selector
                </Label>
                <AvInput id="cart-widget-settings-cartFormSelector" type="textarea" name="cartFormSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="appstelCustomeSelectorLabel" for="cart-widget-settings-appstelCustomeSelector">
                  Appstel Custome Selector
                </Label>
                <AvInput id="cart-widget-settings-appstelCustomeSelector" type="textarea" name="appstelCustomeSelector" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/cart-widget-settings" replace color="info">
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
  cartWidgetSettingsEntity: storeState.cartWidgetSettings.entity,
  loading: storeState.cartWidgetSettings.loading,
  updating: storeState.cartWidgetSettings.updating,
  updateSuccess: storeState.cartWidgetSettings.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CartWidgetSettingsUpdate);
