import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './shop-asset-urls.reducer';
import { IShopAssetUrls } from 'app/shared/model/shop-asset-urls.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShopAssetUrlsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopAssetUrlsUpdate = (props: IShopAssetUrlsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { shopAssetUrlsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/shop-asset-urls');
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
        ...shopAssetUrlsEntity,
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
          <h2 id="subscriptionApp.shopAssetUrls.home.createOrEditLabel">Create or edit a ShopAssetUrls</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : shopAssetUrlsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="shop-asset-urls-id">ID</Label>
                  <AvInput id="shop-asset-urls-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="shop-asset-urls-shop">
                  Shop
                </Label>
                <AvField
                  id="shop-asset-urls-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="vendorJavascriptLabel" for="shop-asset-urls-vendorJavascript">
                  Vendor Javascript
                </Label>
                <AvField id="shop-asset-urls-vendorJavascript" type="text" name="vendorJavascript" />
              </AvGroup>
              <AvGroup>
                <Label id="vendorCssLabel" for="shop-asset-urls-vendorCss">
                  Vendor Css
                </Label>
                <AvField id="shop-asset-urls-vendorCss" type="text" name="vendorCss" />
              </AvGroup>
              <AvGroup>
                <Label id="customerJavascriptLabel" for="shop-asset-urls-customerJavascript">
                  Customer Javascript
                </Label>
                <AvField id="shop-asset-urls-customerJavascript" type="text" name="customerJavascript" />
              </AvGroup>
              <AvGroup>
                <Label id="customerCssLabel" for="shop-asset-urls-customerCss">
                  Customer Css
                </Label>
                <AvField id="shop-asset-urls-customerCss" type="text" name="customerCss" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleJavascriptLabel" for="shop-asset-urls-bundleJavascript">
                  Bundle Javascript
                </Label>
                <AvField id="shop-asset-urls-bundleJavascript" type="text" name="bundleJavascript" />
              </AvGroup>
              <AvGroup>
                <Label id="bundleCssLabel" for="shop-asset-urls-bundleCss">
                  Bundle Css
                </Label>
                <AvField id="shop-asset-urls-bundleCss" type="text" name="bundleCss" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/shop-asset-urls" replace color="info">
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
  shopAssetUrlsEntity: storeState.shopAssetUrls.entity,
  loading: storeState.shopAssetUrls.loading,
  updating: storeState.shopAssetUrls.updating,
  updateSuccess: storeState.shopAssetUrls.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopAssetUrlsUpdate);
