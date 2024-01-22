import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './product-info.reducer';
import { IProductInfo } from 'app/shared/model/product-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductInfoUpdate = (props: IProductInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { productInfoEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/product-info');
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
        ...productInfoEntity,
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
          <h2 id="subscriptionApp.productInfo.home.createOrEditLabel">Create or edit a ProductInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-info-id">ID</Label>
                  <AvInput id="product-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="product-info-shop">
                  Shop
                </Label>
                <AvField
                  id="product-info-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="productIdLabel" for="product-info-productId">
                  Product Id
                </Label>
                <AvField id="product-info-productId" type="string" className="form-control" name="productId" />
              </AvGroup>
              <AvGroup>
                <Label id="productTitleLabel" for="product-info-productTitle">
                  Product Title
                </Label>
                <AvField id="product-info-productTitle" type="text" name="productTitle" />
              </AvGroup>
              <AvGroup>
                <Label id="productHandleLabel" for="product-info-productHandle">
                  Product Handle
                </Label>
                <AvField id="product-info-productHandle" type="text" name="productHandle" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/product-info" replace color="info">
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
  productInfoEntity: storeState.productInfo.entity,
  loading: storeState.productInfo.loading,
  updating: storeState.productInfo.updating,
  updateSuccess: storeState.productInfo.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductInfoUpdate);
