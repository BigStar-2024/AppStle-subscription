import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './shop-settings.reducer';
import { IShopSettings } from 'app/shared/model/shop-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShopSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopSettingsUpdate = (props: IShopSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { shopSettingsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/shop-settings');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...shopSettingsEntity,
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
          <h2 id="subscriptionApp.shopSettings.home.createOrEditLabel">Create or edit a ShopSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : shopSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="shop-settings-id">ID</Label>
                  <AvInput id="shop-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="shop-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="shop-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="taggingEnabledLabel">
                  <AvInput id="shop-settings-taggingEnabled" type="checkbox" className="form-check-input" name="taggingEnabled" />
                  Tagging Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="delayTaggingLabel">
                  <AvInput id="shop-settings-delayTagging" type="checkbox" className="form-check-input" name="delayTagging" />
                  Delay Tagging
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="delayedTaggingValueLabel" for="shop-settings-delayedTaggingValue">
                  Delayed Tagging Value
                </Label>
                <AvField id="shop-settings-delayedTaggingValue" type="string" className="form-control" name="delayedTaggingValue" />
              </AvGroup>
              <AvGroup>
                <Label id="delayedTaggingUnitLabel" for="shop-settings-delayedTaggingUnit">
                  Delayed Tagging Unit
                </Label>
                <AvInput
                  id="shop-settings-delayedTaggingUnit"
                  type="select"
                  className="form-control"
                  name="delayedTaggingUnit"
                  value={(!isNew && shopSettingsEntity.delayedTaggingUnit) || 'SECONDS'}
                >
                  <option value="SECONDS">SECONDS</option>
                  <option value="MINUTES">MINUTES</option>
                  <option value="HOURS">HOURS</option>
                  <option value="DAYS">DAYS</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="orderStatusLabel" for="shop-settings-orderStatus">
                  Order Status
                </Label>
                <AvInput
                  id="shop-settings-orderStatus"
                  type="select"
                  className="form-control"
                  name="orderStatus"
                  value={(!isNew && shopSettingsEntity.orderStatus) || 'ANY'}
                >
                  <option value="ANY">ANY</option>
                  <option value="OPEN">OPEN</option>
                  <option value="ARCHIVED">ARCHIVED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="paymentStatusLabel" for="shop-settings-paymentStatus">
                  Payment Status
                </Label>
                <AvInput
                  id="shop-settings-paymentStatus"
                  type="select"
                  className="form-control"
                  name="paymentStatus"
                  value={(!isNew && shopSettingsEntity.paymentStatus) || 'ANY'}
                >
                  <option value="ANY">ANY</option>
                  <option value="PAID">PAID</option>
                  <option value="UNPAID">UNPAID</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="fulfillmentStatusLabel" for="shop-settings-fulfillmentStatus">
                  Fulfillment Status
                </Label>
                <AvInput
                  id="shop-settings-fulfillmentStatus"
                  type="select"
                  className="form-control"
                  name="fulfillmentStatus"
                  value={(!isNew && shopSettingsEntity.fulfillmentStatus) || 'ANY'}
                >
                  <option value="ANY">ANY</option>
                  <option value="UNFULFILLED">UNFULFILLED</option>
                  <option value="FULFILLED">FULFILLED</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="priceSyncEnabledLabel">
                  <AvInput id="shop-settings-priceSyncEnabled" type="checkbox" className="form-check-input" name="priceSyncEnabled" />
                  Price Sync Enabled
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="skuSyncEnabledLabel">
                  <AvInput id="shop-settings-skuSyncEnabled" type="checkbox" className="form-check-input" name="skuSyncEnabled" />
                  Sku Sync Enabled
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/shop-settings" replace color="info">
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
  shopSettingsEntity: storeState.shopSettings.entity,
  loading: storeState.shopSettings.loading,
  updating: storeState.shopSettings.updating,
  updateSuccess: storeState.shopSettings.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopSettingsUpdate);
