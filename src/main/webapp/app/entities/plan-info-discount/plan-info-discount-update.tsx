import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './plan-info-discount.reducer';
import { IPlanInfoDiscount } from 'app/shared/model/plan-info-discount.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPlanInfoDiscountUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PlanInfoDiscountUpdate = (props: IPlanInfoDiscountUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { planInfoDiscountEntity, loading, updating } = props;

  const { description } = planInfoDiscountEntity;

  const handleClose = () => {
    props.history.push('/admin/plan-info-discount' + props.location.search);
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
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    if (errors.length === 0) {
      const entity = {
        ...planInfoDiscountEntity,
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
          <h2 id="subscriptionApp.planInfoDiscount.home.createOrEditLabel">Create or edit a PlanInfoDiscount</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : planInfoDiscountEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="plan-info-discount-id">ID</Label>
                  <AvInput id="plan-info-discount-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="discountCodeLabel" for="plan-info-discount-discountCode">
                  Discount Code
                </Label>
                <AvField id="plan-info-discount-discountCode" type="text" name="discountCode" validate={{}} />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="plan-info-discount-description">
                  Description
                </Label>
                <AvInput id="plan-info-discount-description" type="textarea" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="discountTypeLabel" for="plan-info-discount-discountType">
                  Discount Type
                </Label>
                <AvInput
                  id="plan-info-discount-discountType"
                  type="select"
                  className="form-control"
                  name="discountType"
                  value={(!isNew && planInfoDiscountEntity.discountType) || 'PERCENTAGE'}
                >
                  <option value="PERCENTAGE">PERCENTAGE</option>
                  <option value="FIXED_AMOUNT">FIXED_AMOUNT</option>
                  {/* <option value="TRIAL_DAYS">TRIAL_DAYS</option> */}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="discountLabel" for="plan-info-discount-discount">
                  Discount
                </Label>
                <AvField id="plan-info-discount-discount" type="text" name="discount" />
              </AvGroup>
              <AvGroup>
                <Label id="maxDiscountAmountLabel" for="plan-info-discount-maxDiscountAmount">
                  Max Discount Amount
                </Label>
                <AvField id="plan-info-discount-maxDiscountAmount" type="text" name="maxDiscountAmount" />
              </AvGroup>
              {/* <AvGroup>
                <Label id="trialDaysLabel" for="plan-info-discount-trialDays">
                  Trial Days
                </Label>
                <AvField id="plan-info-discount-trialDays" type="string" className="form-control" name="trialDays" />
              </AvGroup> */}
              <AvGroup>
                <Label id="startDateLabel" for="plan-info-discount-startDate">
                  Start Date
                </Label>
                <AvInput
                  id="plan-info-discount-startDate"
                  type="datetime-local"
                  className="form-control"
                  name="startDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.planInfoDiscountEntity.startDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endDateLabel" for="plan-info-discount-endDate">
                  End Date
                </Label>
                <AvInput
                  id="plan-info-discount-endDate"
                  type="datetime-local"
                  className="form-control"
                  name="endDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.planInfoDiscountEntity.endDate)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="archivedLabel">
                  <AvInput id="plan-info-discount-archived" type="checkbox" className="form-check-input" name="archived" />
                  Archived
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/admin/plan-info-discount" replace color="info">
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
  planInfoDiscountEntity: storeState.planInfoDiscount.entity,
  loading: storeState.planInfoDiscount.loading,
  updating: storeState.planInfoDiscount.updating,
  updateSuccess: storeState.planInfoDiscount.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(PlanInfoDiscountUpdate);
