import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './subscription-contract-one-off.reducer';
import { ISubscriptionContractOneOff } from 'app/shared/model/subscription-contract-one-off.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionContractOneOffUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractOneOffUpdate = (props: ISubscriptionContractOneOffUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionContractOneOffEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/subscription-contract-one-off' + props.location.search);
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
        ...subscriptionContractOneOffEntity,
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
          <h2 id="subscriptionApp.subscriptionContractOneOff.home.createOrEditLabel">Create or edit a SubscriptionContractOneOff</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionContractOneOffEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-contract-one-off-id">ID</Label>
                  <AvInput id="subscription-contract-one-off-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-contract-one-off-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-contract-one-off-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="billingAttemptIdLabel" for="subscription-contract-one-off-billingAttemptId">
                  Billing Attempt Id
                </Label>
                <AvField
                  id="subscription-contract-one-off-billingAttemptId"
                  type="string"
                  className="form-control"
                  name="billingAttemptId"
                />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionContractIdLabel" for="subscription-contract-one-off-subscriptionContractId">
                  Subscription Contract Id
                </Label>
                <AvField
                  id="subscription-contract-one-off-subscriptionContractId"
                  type="string"
                  className="form-control"
                  name="subscriptionContractId"
                />
              </AvGroup>
              <AvGroup>
                <Label id="variantIdLabel" for="subscription-contract-one-off-variantId">
                  Variant Id
                </Label>
                <AvField id="subscription-contract-one-off-variantId" type="string" className="form-control" name="variantId" />
              </AvGroup>
              <AvGroup>
                <Label id="variantHandleLabel" for="subscription-contract-one-off-variantHandle">
                  Variant Handle
                </Label>
                <AvField id="subscription-contract-one-off-variantHandle" type="text" name="variantHandle" />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLabel" for="subscription-contract-one-off-quantity">
                  Quantity
                </Label>
                <AvField id="subscription-contract-one-off-quantity" type="string" className="form-control" name="quantity" />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="subscription-contract-one-off-price">
                  Price
                </Label>
                <AvField id="subscription-contract-one-off-price" type="string" className="form-control" name="price" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-contract-one-off" replace color="info">
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
  subscriptionContractOneOffEntity: storeState.subscriptionContractOneOff.entity,
  loading: storeState.subscriptionContractOneOff.loading,
  updating: storeState.subscriptionContractOneOff.updating,
  updateSuccess: storeState.subscriptionContractOneOff.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractOneOffUpdate);
