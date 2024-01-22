import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './variant-info.reducer';
import { IVariantInfo } from 'app/shared/model/variant-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVariantInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VariantInfoUpdate = (props: IVariantInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { variantInfoEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/variant-info');
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
        ...variantInfoEntity,
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
          <h2 id="subscriptionApp.variantInfo.home.createOrEditLabel">Create or edit a VariantInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : variantInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="variant-info-id">ID</Label>
                  <AvInput id="variant-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="variant-info-shop">
                  Shop
                </Label>
                <AvField
                  id="variant-info-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="productIdLabel" for="variant-info-productId">
                  Product Id
                </Label>
                <AvField id="variant-info-productId" type="string" className="form-control" name="productId" />
              </AvGroup>
              <AvGroup>
                <Label id="variantIdLabel" for="variant-info-variantId">
                  Variant Id
                </Label>
                <AvField id="variant-info-variantId" type="string" className="form-control" name="variantId" />
              </AvGroup>
              <AvGroup>
                <Label id="productTitleLabel" for="variant-info-productTitle">
                  Product Title
                </Label>
                <AvField id="variant-info-productTitle" type="text" name="productTitle" />
              </AvGroup>
              <AvGroup>
                <Label id="variantTitleLabel" for="variant-info-variantTitle">
                  Variant Title
                </Label>
                <AvField id="variant-info-variantTitle" type="text" name="variantTitle" />
              </AvGroup>
              <AvGroup>
                <Label id="skuLabel" for="variant-info-sku">
                  Sku
                </Label>
                <AvField id="variant-info-sku" type="text" name="sku" />
              </AvGroup>
              <AvGroup>
                <Label id="variantPriceLabel" for="variant-info-variantPrice">
                  Variant Price
                </Label>
                <AvField id="variant-info-variantPrice" type="text" name="variantPrice" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/variant-info" replace color="info">
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
  variantInfoEntity: storeState.variantInfo.entity,
  loading: storeState.variantInfo.loading,
  updating: storeState.variantInfo.updating,
  updateSuccess: storeState.variantInfo.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VariantInfoUpdate);
