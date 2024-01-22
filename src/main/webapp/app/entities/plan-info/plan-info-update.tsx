import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './plan-info.reducer';
import { IPlanInfo } from 'app/shared/model/plan-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPlanInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PlanInfoUpdate = (props: IPlanInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { planInfoEntity, loading, updating } = props;

  const { additionalDetails, features } = planInfoEntity;

  const handleClose = () => {
    props.history.push('/admin/plan-info');
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
        ...planInfoEntity,
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
          <h2 id="subscriptionApp.planInfo.home.createOrEditLabel">Create or edit a PlanInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : planInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="plan-info-id">ID</Label>
                  <AvInput id="plan-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="plan-info-name">
                  Name
                </Label>
                <AvField id="plan-info-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="plan-info-price">
                  Price
                </Label>
                <AvField id="plan-info-price" type="string" className="form-control" name="price" />
              </AvGroup>
              <AvGroup check>
                <Label id="archivedLabel">
                  <AvInput id="plan-info-archived" type="checkbox" className="form-check-input" name="archived" />
                  Archived
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="planTypeLabel" for="plan-info-planType">
                  Plan Type
                </Label>
                <AvInput
                  id="plan-info-planType"
                  type="select"
                  className="form-control"
                  name="planType"
                  value={(!isNew && planInfoEntity.planType) || 'MONTHLY'}
                >
                  <option value="MONTHLY">MONTHLY</option>
                  <option value="YEARLY">YEARLY</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="billingTypeLabel" for="plan-info-billingType">
                  Billing Type
                </Label>
                <AvInput
                  id="plan-info-billingType"
                  type="select"
                  className="form-control"
                  name="billingType"
                  value={(!isNew && planInfoEntity.billingType) || 'SHOPIFY_RECURRING'}
                >
                  <option value="SHOPIFY_RECURRING">SHOPIFY_RECURRING</option>
                  <option value="SHOPIFY_USAGE">SHOPIFY_USAGE</option>
                  <option value="SHOPIFY_ONE_TIME">SHOPIFY_ONE_TIME</option>
                  <option value="SHOPIFY_ANNUAL">SHOPIFY_ANNUAL</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="basedOnLabel" for="plan-info-basedOn">
                  Based On
                </Label>
                <AvInput
                  id="plan-info-basedOn"
                  type="select"
                  className="form-control"
                  name="basedOn"
                  value={(!isNew && planInfoEntity.basedOn) || 'FIXED_PRICE'}
                >
                  <option value="FIXED_PRICE">FIXED_PRICE</option>
                  <option value="PRODUCT_COUNT">PRODUCT_COUNT</option>
                  <option value="SHOPIFY_PLAN">SHOPIFY_PLAN</option>
                  <option value="PRODUCT_COUNT_AND_SHOPIFY_PLAN">PRODUCT_COUNT_AND_SHOPIFY_PLAN</option>
                  <option value="SUBSCRIPTION_ORDER_AMOUNT">SUBSCRIPTION_ORDER_AMOUNT</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="additionalDetailsLabel" for="plan-info-additionalDetails">
                  Additional Details
                </Label>
                <AvInput id="plan-info-additionalDetails" type="textarea" name="additionalDetails" />
              </AvGroup>
              <AvGroup>
                <Label id="featuresLabel" for="plan-info-features">
                  Features
                </Label>
                <AvInput id="plan-info-features" type="textarea" name="features" />
              </AvGroup>
              <AvGroup>
                <Label id="trialDaysLabel" for="plan-info-trialDays">
                  Trial Days
                </Label>
                <AvField id="plan-info-trialDays" type="string" className="form-control" name="trialDays" />
              </AvGroup>
              <AvGroup>
                <Label id="basePlanLabel" for="plan-info-basePlan">
                  Base Plan
                </Label>
                <AvInput
                  id="plan-info-basePlan"
                  type="select"
                  className="form-control"
                  name="basePlan"
                  value={(!isNew && planInfoEntity.basePlan) || 'FREE'}
                >
                  <option value="FREE">FREE</option>
                  <option value="FREE_ANNUAL">FREE_ANNUAL</option>
                  <option value="STARTER">STARTER</option>
                  <option value="STARTER_ANNUAL">STARTER_ANNUAL</option>
                  <option value="STARTER_PAYG">STARTER_PAYG</option>
                  <option value="BUSINESS">BUSINESS</option>
                  <option value="BUSINESS_ANNUAL">BUSINESS_ANNUAL</option>
                  <option value="ENTERPRISE">ENTERPRISE</option>
                  <option value="ENTERPRISE_ANNUAL">ENTERPRISE_ANNUAL</option>
                  <option value="ENTERPRISE_PLUS">ENTERPRISE_PLUS</option>
                  <option value="ENTERPRISE_PLUS_ANNUAL">ENTERPRISE_PLUS_ANNUAL</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/admin/plan-info" replace color="info">
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
  planInfoEntity: storeState.planInfo.entity,
  loading: storeState.planInfo.loading,
  updating: storeState.planInfo.updating,
  updateSuccess: storeState.planInfo.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(PlanInfoUpdate);
