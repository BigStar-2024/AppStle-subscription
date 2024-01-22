import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './cancellation-management.reducer';
import { ICancellationManagement } from 'app/shared/model/cancellation-management.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICancellationManagementUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CancellationManagementUpdate = (props: ICancellationManagementUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { cancellationManagementEntity, loading, updating } = props;

  const { cancellationInstructionsText, cancellationReasonsJSON, pauseInstructionsText } = cancellationManagementEntity;

  const handleClose = () => {
    props.history.push('/cancellation-management');
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
        ...cancellationManagementEntity,
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
          <h2 id="subscriptionApp.cancellationManagement.home.createOrEditLabel">Create or edit a CancellationManagement</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : cancellationManagementEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="cancellation-management-id">ID</Label>
                  <AvInput id="cancellation-management-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="cancellation-management-shop">
                  Shop
                </Label>
                <AvField
                  id="cancellation-management-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cancellationTypeLabel" for="cancellation-management-cancellationType">
                  Cancellation Type
                </Label>
                <AvInput
                  id="cancellation-management-cancellationType"
                  type="select"
                  className="form-control"
                  name="cancellationType"
                  value={(!isNew && cancellationManagementEntity.cancellationType) || 'CANCEL_IMMEDIATELY'}
                >
                  <option value="CANCEL_IMMEDIATELY">CANCEL_IMMEDIATELY</option>
                  <option value="CANCELLATION_INSTRUCTIONS">CANCELLATION_INSTRUCTIONS</option>
                  <option value="CUSTOMER_RETENTION_FLOW">CUSTOMER_RETENTION_FLOW</option>
                  <option value="CANCEL_AFTER_PAUSE">CANCEL_AFTER_PAUSE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="cancellationInstructionsTextLabel" for="cancellation-management-cancellationInstructionsText">
                  Cancellation Instructions Text
                </Label>
                <AvInput id="cancellation-management-cancellationInstructionsText" type="textarea" name="cancellationInstructionsText" />
              </AvGroup>
              <AvGroup>
                <Label id="cancellationReasonsJSONLabel" for="cancellation-management-cancellationReasonsJSON">
                  Cancellation Reasons JSON
                </Label>
                <AvInput id="cancellation-management-cancellationReasonsJSON" type="textarea" name="cancellationReasonsJSON" />
              </AvGroup>
              <AvGroup>
                <Label id="pauseInstructionsTextLabel" for="cancellation-management-pauseInstructionsText">
                  Pause Instructions Text
                </Label>
                <AvInput id="cancellation-management-pauseInstructionsText" type="textarea" name="pauseInstructionsText" />
              </AvGroup>
              <AvGroup>
                <Label id="pauseDurationCycleLabel" for="cancellation-management-pauseDurationCycle">
                  Pause Duration Cycle
                </Label>
                <AvField id="cancellation-management-pauseDurationCycle" type="string" className="form-control" name="pauseDurationCycle" />
              </AvGroup>
              <AvGroup check>
                <Label id="enableDiscountEmailLabel">
                  <AvInput
                    id="cancellation-management-enableDiscountEmail"
                    type="checkbox"
                    className="form-check-input"
                    name="enableDiscountEmail"
                  />
                  Enable Discount Email
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="discountEmailAddressLabel" for="cancellation-management-discountEmailAddress">
                  Discount Email Address
                </Label>
                <AvField id="cancellation-management-discountEmailAddress" type="text" name="discountEmailAddress" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/cancellation-management" replace color="info">
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
  cancellationManagementEntity: storeState.cancellationManagement.entity,
  loading: storeState.cancellationManagement.loading,
  updating: storeState.cancellationManagement.updating,
  updateSuccess: storeState.cancellationManagement.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(CancellationManagementUpdate);
