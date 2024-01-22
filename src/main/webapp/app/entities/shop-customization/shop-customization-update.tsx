import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './shop-customization.reducer';
import { IShopCustomization } from 'app/shared/model/shop-customization.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShopCustomizationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopCustomizationUpdate = (props: IShopCustomizationUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { shopCustomizationEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/admin/shop-customization');
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
        ...shopCustomizationEntity,
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
          <h2 id="subscriptionApp.shopCustomization.home.createOrEditLabel">Create or edit a ShopCustomization</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : shopCustomizationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="shop-customization-id">ID</Label>
                  <AvInput id="shop-customization-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="shop-customization-shop">
                  Shop
                </Label>
                <AvField id="shop-customization-shop" type="text" name="shop" />
              </AvGroup>
              <AvGroup>
                <Label id="labelIdLabel" for="shop-customization-labelId">
                  Label Id
                </Label>
                <AvField id="shop-customization-labelId" type="string" className="form-control" name="labelId" />
              </AvGroup>
              <AvGroup>
                <Label id="valueLabel" for="shop-customization-value">
                  Value
                </Label>
                <AvField id="shop-customization-value" type="text" name="value" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/admin/shop-customization" replace color="info">
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
  shopCustomizationEntity: storeState.shopCustomization.entity,
  loading: storeState.shopCustomization.loading,
  updating: storeState.shopCustomization.updating,
  updateSuccess: storeState.shopCustomization.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopCustomizationUpdate);
