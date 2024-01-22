import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './subscription-group-plan.reducer';
import { ISubscriptionGroupPlan } from 'app/shared/model/subscription-group-plan.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionGroupPlanUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionGroupPlanUpdate = (props: ISubscriptionGroupPlanUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionGroupPlanEntity, loading, updating } = props;

  const { infoJson, productIds, variantIds, variantProductIds } = subscriptionGroupPlanEntity;

  const handleClose = () => {
    props.history.push('/subscription-group-plan');
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
        ...subscriptionGroupPlanEntity,
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
          <h2 id="subscriptionApp.subscriptionGroupPlan.home.createOrEditLabel">Create or edit a SubscriptionGroupPlan</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionGroupPlanEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-group-plan-id">ID</Label>
                  <AvInput id="subscription-group-plan-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-group-plan-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-group-plan-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="groupNameLabel" for="subscription-group-plan-groupName">
                  Group Name
                </Label>
                <AvField id="subscription-group-plan-groupName" type="text" name="groupName" />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionIdLabel" for="subscription-group-plan-subscriptionId">
                  Subscription Id
                </Label>
                <AvField id="subscription-group-plan-subscriptionId" type="string" className="form-control" name="subscriptionId" />
              </AvGroup>
              <AvGroup>
                <Label id="productCountLabel" for="subscription-group-plan-productCount">
                  Product Count
                </Label>
                <AvField id="subscription-group-plan-productCount" type="string" className="form-control" name="productCount" />
              </AvGroup>
              <AvGroup>
                <Label id="productVariantCountLabel" for="subscription-group-plan-productVariantCount">
                  Product Variant Count
                </Label>
                <AvField
                  id="subscription-group-plan-productVariantCount"
                  type="string"
                  className="form-control"
                  name="productVariantCount"
                />
              </AvGroup>
              <AvGroup>
                <Label id="infoJsonLabel" for="subscription-group-plan-infoJson">
                  Info Json
                </Label>
                <AvInput id="subscription-group-plan-infoJson" type="textarea" name="infoJson" />
              </AvGroup>
              <AvGroup>
                <Label id="productIdsLabel" for="subscription-group-plan-productIds">
                  Product Ids
                </Label>
                <AvInput id="subscription-group-plan-productIds" type="textarea" name="productIds" />
              </AvGroup>
              <AvGroup>
                <Label id="variantIdsLabel" for="subscription-group-plan-variantIds">
                  Variant Ids
                </Label>
                <AvInput id="subscription-group-plan-variantIds" type="textarea" name="variantIds" />
              </AvGroup>
              <AvGroup>
                <Label id="variantProductIdsLabel" for="subscription-group-plan-variantProductIds">
                  Variant Product Ids
                </Label>
                <AvInput id="subscription-group-plan-variantProductIds" type="textarea" name="variantProductIds" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-group-plan" replace color="info">
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
  subscriptionGroupPlanEntity: storeState.subscriptionGroupPlan.entity,
  loading: storeState.subscriptionGroupPlan.loading,
  updating: storeState.subscriptionGroupPlan.updating,
  updateSuccess: storeState.subscriptionGroupPlan.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionGroupPlanUpdate);
