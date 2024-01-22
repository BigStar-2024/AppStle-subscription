import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './theme-code.reducer';
import { IThemeCode } from 'app/shared/model/theme-code.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IThemeCodeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ThemeCodeUpdate = (props: IThemeCodeUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { themeCodeEntity, loading, updating } = props;

  const {
    addToCartSelector,
    subscriptionLinkSelector,
    priceSelector,
    badgeTop,
    cartRowSelector,
    cartLineItemSelector,
    cartLineItemPerQuantityPriceSelector,
    cartLineItemTotalPriceSelector,
    cartLineItemSellingPlanNameSelector,
    cartSubTotalSelector,
    cartLineItemPriceSelector
  } = themeCodeEntity;

  const handleClose = () => {
    props.history.push('/theme-code');
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
        ...themeCodeEntity,
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
          <h2 id="subscriptionApp.themeCode.home.createOrEditLabel">Create or edit a ThemeCode</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : themeCodeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="theme-code-id">ID</Label>
                  <AvInput id="theme-code-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="themeNameLabel" for="theme-code-themeName">
                  Theme Name
                </Label>
                <AvField
                  id="theme-code-themeName"
                  type="text"
                  name="themeName"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="themeNameFriendlyLabel" for="theme-code-themeNameFriendly">
                  Theme Name Friendly
                </Label>
                <AvField
                  id="theme-code-themeNameFriendly"
                  type="text"
                  name="themeNameFriendly"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="themeStoreIdLabel" for="theme-code-themeStoreId">
                  Theme Store Id
                </Label>
                <AvField id="theme-code-themeStoreId" type="string" className="form-control" name="themeStoreId" />
              </AvGroup>
              <AvGroup>
                <Label id="addToCartSelectorLabel" for="theme-code-addToCartSelector">
                  Add To Cart Selector
                </Label>
                <AvInput id="theme-code-addToCartSelector" type="textarea" name="addToCartSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionLinkSelectorLabel" for="theme-code-subscriptionLinkSelector">
                  Subscription Link Selector
                </Label>
                <AvInput id="theme-code-subscriptionLinkSelector" type="textarea" name="subscriptionLinkSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="addToCartPlacementLabel" for="theme-code-addToCartPlacement">
                  Add To Cart Placement
                </Label>
                <AvInput
                  id="theme-code-addToCartPlacement"
                  type="select"
                  className="form-control"
                  name="addToCartPlacement"
                  value={(!isNew && themeCodeEntity.addToCartPlacement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionLinkPlacementLabel" for="theme-code-subscriptionLinkPlacement">
                  Subscription Link Placement
                </Label>
                <AvInput
                  id="theme-code-subscriptionLinkPlacement"
                  type="select"
                  className="form-control"
                  name="subscriptionLinkPlacement"
                  value={(!isNew && themeCodeEntity.subscriptionLinkPlacement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="priceSelectorLabel" for="theme-code-priceSelector">
                  Price Selector
                </Label>
                <AvInput id="theme-code-priceSelector" type="textarea" name="priceSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="pricePlacementLabel" for="theme-code-pricePlacement">
                  Price Placement
                </Label>
                <AvInput
                  id="theme-code-pricePlacement"
                  type="select"
                  className="form-control"
                  name="pricePlacement"
                  value={(!isNew && themeCodeEntity.pricePlacement) || 'BEFORE'}
                >
                  <option value="BEFORE">BEFORE</option>
                  <option value="AFTER">AFTER</option>
                  <option value="FIRST_CHILD">FIRST_CHILD</option>
                  <option value="LAST_CHILD">LAST_CHILD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="badgeTopLabel" for="theme-code-badgeTop">
                  Badge Top
                </Label>
                <AvInput id="theme-code-badgeTop" type="textarea" name="badgeTop" />
              </AvGroup>
              <AvGroup>
                <Label id="cartRowSelectorLabel" for="theme-code-cartRowSelector">
                  Cart Row Selector
                </Label>
                <AvInput id="theme-code-cartRowSelector" type="textarea" name="cartRowSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemSelectorLabel" for="theme-code-cartLineItemSelector">
                  Cart Line Item Selector
                </Label>
                <AvInput id="theme-code-cartLineItemSelector" type="textarea" name="cartLineItemSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemPerQuantityPriceSelectorLabel" for="theme-code-cartLineItemPerQuantityPriceSelector">
                  Cart Line Item Per Quantity Price Selector
                </Label>
                <AvInput id="theme-code-cartLineItemPerQuantityPriceSelector" type="textarea" name="cartLineItemPerQuantityPriceSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemTotalPriceSelectorLabel" for="theme-code-cartLineItemTotalPriceSelector">
                  Cart Line Item Total Price Selector
                </Label>
                <AvInput id="theme-code-cartLineItemTotalPriceSelector" type="textarea" name="cartLineItemTotalPriceSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemSellingPlanNameSelectorLabel" for="theme-code-cartLineItemSellingPlanNameSelector">
                  Cart Line Item Selling Plan Name Selector
                </Label>
                <AvInput id="theme-code-cartLineItemSellingPlanNameSelector" type="textarea" name="cartLineItemSellingPlanNameSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartSubTotalSelectorLabel" for="theme-code-cartSubTotalSelector">
                  Cart Sub Total Selector
                </Label>
                <AvInput id="theme-code-cartSubTotalSelector" type="textarea" name="cartSubTotalSelector" />
              </AvGroup>
              <AvGroup>
                <Label id="cartLineItemPriceSelectorLabel" for="theme-code-cartLineItemPriceSelector">
                  Cart Line Item Price Selector
                </Label>
                <AvInput id="theme-code-cartLineItemPriceSelector" type="textarea" name="cartLineItemPriceSelector" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/theme-code" replace color="info">
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
  themeCodeEntity: storeState.themeCode.entity,
  loading: storeState.themeCode.loading,
  updating: storeState.themeCode.updating,
  updateSuccess: storeState.themeCode.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(ThemeCodeUpdate);
