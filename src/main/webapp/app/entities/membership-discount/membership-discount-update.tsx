import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './membership-discount.reducer';
import { IMembershipDiscount } from 'app/shared/model/membership-discount.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMembershipDiscountUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MembershipDiscountUpdate = (props: IMembershipDiscountUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { membershipDiscountEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/membership-discount');
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
        ...membershipDiscountEntity,
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
          <h2 id="subscriptionApp.membershipDiscount.home.createOrEditLabel">Create or edit a MembershipDiscount</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : membershipDiscountEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="membership-discount-id">ID</Label>
                  <AvInput id="membership-discount-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="membership-discount-shop">
                  Shop
                </Label>
                <AvField
                  id="membership-discount-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="titleLabel" for="membership-discount-title">
                  Title
                </Label>
                <AvField id="membership-discount-title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label id="discountLabel" for="membership-discount-discount">
                  Discount
                </Label>
                <AvField id="membership-discount-discount" type="string" className="form-control" name="discount" />
              </AvGroup>
              <AvGroup>
                <Label id="customerTagsLabel" for="membership-discount-customerTags">
                  Customer Tags
                </Label>
                <AvField id="membership-discount-customerTags" type="text" name="customerTags" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/membership-discount" replace color="info">
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
  membershipDiscountEntity: storeState.membershipDiscount.entity,
  loading: storeState.membershipDiscount.loading,
  updating: storeState.membershipDiscount.updating,
  updateSuccess: storeState.membershipDiscount.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscountUpdate);
