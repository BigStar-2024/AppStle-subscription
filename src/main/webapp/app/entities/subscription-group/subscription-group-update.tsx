import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './subscription-group.reducer';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionGroupUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionGroupUpdate = (props: ISubscriptionGroupUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionGroupEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/subscription-group');
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
        ...subscriptionGroupEntity,
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
          <h2 id="subscriptionApp.subscriptionGroup.home.createOrEditLabel">Create or edit a SubscriptionGroup</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionGroupEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-group-id">ID</Label>
                  <AvInput id="subscription-group-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="productCountLabel" for="subscription-group-productCount">
                  Product Count
                </Label>
                <AvField id="subscription-group-productCount" type="string" className="form-control" name="productCount" />
              </AvGroup>
              <AvGroup>
                <Label id="productVariantCountLabel" for="subscription-group-productVariantCount">
                  Product Variant Count
                </Label>
                <AvField id="subscription-group-productVariantCount" type="string" className="form-control" name="productVariantCount" />
              </AvGroup>
              <AvGroup>
                <Label id="frequencyCountLabel" for="subscription-group-frequencyCount">
                  Frequency Count
                </Label>
                <AvField id="subscription-group-frequencyCount" type="string" className="form-control" name="frequencyCount" />
              </AvGroup>
              <AvGroup>
                <Label id="frequencyIntervalLabel" for="subscription-group-frequencyInterval">
                  Frequency Interval
                </Label>
                <AvInput
                  id="subscription-group-frequencyInterval"
                  type="select"
                  className="form-control"
                  name="frequencyInterval"
                  value={(!isNew && subscriptionGroupEntity.frequencyInterval) || 'DAY'}
                >
                  <option value="DAY">DAY</option>
                  <option value="WEEK">WEEK</option>
                  <option value="MONTH">MONTH</option>
                  <option value="YEAR">YEAR</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="frequencyNameLabel" for="subscription-group-frequencyName">
                  Frequency Name
                </Label>
                <AvField id="subscription-group-frequencyName" type="text" name="frequencyName" />
              </AvGroup>
              <AvGroup>
                <Label id="groupNameLabel" for="subscription-group-groupName">
                  Group Name
                </Label>
                <AvField id="subscription-group-groupName" type="text" name="groupName" />
              </AvGroup>
              <AvGroup>
                <Label id="productIdsLabel" for="subscription-group-productIds">
                  Product Ids
                </Label>
                <AvField id="subscription-group-productIds" type="text" name="productIds" />
              </AvGroup>
              <AvGroup>
                <Label id="discountOfferLabel" for="subscription-group-discountOffer">
                  Discount Offer
                </Label>
                <AvField id="subscription-group-discountOffer" type="string" className="form-control" name="discountOffer" />
              </AvGroup>
              <AvGroup>
                <Label id="discountTypeLabel" for="subscription-group-discountType">
                  Discount Type
                </Label>
                <AvInput
                  id="subscription-group-discountType"
                  type="select"
                  className="form-control"
                  name="discountType"
                  value={(!isNew && subscriptionGroupEntity.discountType) || 'PERCENTAGE'}
                >
                  <option value="PERCENTAGE">PERCENTAGE</option>
                  <option value="FIXED">FIXED</option>
                  <option value="PRICE">PRICE</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="discountEnabledLabel">
                  <AvInput id="subscription-group-discountEnabled" type="checkbox" className="form-check-input" name="discountEnabled" />
                  Discount Enabled
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-group" replace color="info">
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
  subscriptionGroupEntity: storeState.subscriptionGroup.entity,
  loading: storeState.subscriptionGroup.loading,
  updating: storeState.subscriptionGroup.updating,
  updateSuccess: storeState.subscriptionGroup.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionGroupUpdate);
