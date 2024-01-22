import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './bulk-automation.reducer';
import { IBulkAutomation } from 'app/shared/model/bulk-automation.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBulkAutomationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BulkAutomationUpdate = (props: IBulkAutomationUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { bulkAutomationEntity, loading, updating } = props;

  const { requestInfo, errorInfo } = bulkAutomationEntity;

  const handleClose = () => {
    props.history.push('/admin/bulk-automation' + props.location.search);
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
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    if (errors.length === 0) {
      const entity = {
        ...bulkAutomationEntity,
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
          <h2 id="subscriptionApp.bulkAutomation.home.createOrEditLabel">Create or edit a BulkAutomation</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bulkAutomationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="bulk-automation-id">ID</Label>
                  <AvInput id="bulk-automation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="bulk-automation-shop">
                  Shop
                </Label>
                <AvField
                  id="bulk-automation-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="automationTypeLabel" for="bulk-automation-automationType">
                  Automation Type
                </Label>
                <AvInput
                  id="bulk-automation-automationType"
                  type="select"
                  className="form-control"
                  name="automationType"
                  value={(!isNew && bulkAutomationEntity.automationType) || 'EXPORT'}
                >
                  <option value="EXPORT">EXPORT</option>
                  <option value="MIGRATION">MIGRATION</option>
                  <option value="ADD_REMOVE_DISCOUNT_CODE">ADD_REMOVE_DISCOUNT_CODE</option>
                  <option value="UPDATE_DELIVERY_METHOD_TYPE">UPDATE_DELIVERY_METHOD_TYPE</option>
                  <option value="ADD_PRODUCT">ADD_PRODUCT</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="runningLabel">
                  <AvInput id="bulk-automation-running" type="checkbox" className="form-check-input" name="running" />
                  Running
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="startTimeLabel" for="bulk-automation-startTime">
                  Start Time
                </Label>
                <AvInput
                  id="bulk-automation-startTime"
                  type="datetime-local"
                  className="form-control"
                  name="startTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.bulkAutomationEntity.startTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endTimeLabel" for="bulk-automation-endTime">
                  End Time
                </Label>
                <AvInput
                  id="bulk-automation-endTime"
                  type="datetime-local"
                  className="form-control"
                  name="endTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.bulkAutomationEntity.endTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="requestInfoLabel" for="bulk-automation-requestInfo">
                  Request Info
                </Label>
                <AvInput id="bulk-automation-requestInfo" type="textarea" name="requestInfo" />
              </AvGroup>
              <AvGroup>
                <Label id="errorInfoLabel" for="bulk-automation-errorInfo">
                  Error Info
                </Label>
                <AvInput id="bulk-automation-errorInfo" type="textarea" name="errorInfo" />
              </AvGroup>
              <AvGroup>
                <Label id="currentExecutionLabel" for="bulk-automation-currentExecution">
                  Current Execution
                </Label>
                <AvField id="bulk-automation-currentExecution" type="text" name="currentExecution" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/admin/bulk-automation" replace color="info">
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
  bulkAutomationEntity: storeState.bulkAutomation.entity,
  loading: storeState.bulkAutomation.loading,
  updating: storeState.bulkAutomation.updating,
  updateSuccess: storeState.bulkAutomation.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(BulkAutomationUpdate);
