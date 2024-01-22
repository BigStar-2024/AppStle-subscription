import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './product-swap.reducer';
import { IProductSwap } from 'app/shared/model/product-swap.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductSwapUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductSwapUpdate = (props: IProductSwapUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { productSwapEntity, loading, updating } = props;

  const { sourceVariants, destinationVariants, name } = productSwapEntity;

  const handleClose = () => {
    props.history.push('/product-swap');
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
        ...productSwapEntity,
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
          <h2 id="subscriptionApp.productSwap.home.createOrEditLabel">Create or edit a ProductSwap</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productSwapEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-swap-id">ID</Label>
                  <AvInput id="product-swap-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="product-swap-shop">
                  Shop
                </Label>
                <AvField
                  id="product-swap-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="sourceVariantsLabel" for="product-swap-sourceVariants">
                  Source Variants
                </Label>
                <AvInput id="product-swap-sourceVariants" type="textarea" name="sourceVariants" />
              </AvGroup>
              <AvGroup>
                <Label id="destinationVariantsLabel" for="product-swap-destinationVariants">
                  Destination Variants
                </Label>
                <AvInput id="product-swap-destinationVariants" type="textarea" name="destinationVariants" />
              </AvGroup>
              <AvGroup check>
                <Label id="updatedFirstOrderLabel">
                  <AvInput id="product-swap-updatedFirstOrder" type="checkbox" className="form-check-input" name="updatedFirstOrder" />
                  Updated First Order
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="checkForEveryRecurringOrderLabel">
                  <AvInput
                    id="product-swap-checkForEveryRecurringOrder"
                    type="checkbox"
                    className="form-check-input"
                    name="checkForEveryRecurringOrder"
                  />
                  Check For Every Recurring Order
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="product-swap-name">
                  Name
                </Label>
                <AvInput id="product-swap-name" type="textarea" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="changeNextOrderDateByLabel" for="product-swap-changeNextOrderDateBy">
                  Change Next Order Date By
                </Label>
                <AvField id="product-swap-changeNextOrderDateBy" type="string" className="form-control" name="changeNextOrderDateBy" />
              </AvGroup>
              <AvGroup>
                <Label id="forBillingCycleLabel" for="product-swap-forBillingCycle">
                  For Billing Cycle
                </Label>
                <AvField id="product-swap-forBillingCycle" type="string" className="form-control" name="forBillingCycle" />
              </AvGroup>
              <AvGroup check>
                <Label id="carryDiscountForwardLabel">
                  <AvInput
                    id="product-swap-carryDiscountForward"
                    type="checkbox"
                    className="form-check-input"
                    name="carryDiscountForward"
                  />
                  Carry Discount Forward
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/product-swap" replace color="info">
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
  productSwapEntity: storeState.productSwap.entity,
  loading: storeState.productSwap.loading,
  updating: storeState.productSwap.updating,
  updateSuccess: storeState.productSwap.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(ProductSwapUpdate);
