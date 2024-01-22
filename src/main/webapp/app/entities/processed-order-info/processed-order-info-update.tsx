import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './processed-order-info.reducer';
import { IProcessedOrderInfo } from 'app/shared/model/processed-order-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProcessedOrderInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProcessedOrderInfoUpdate = (props: IProcessedOrderInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { processedOrderInfoEntity, loading, updating } = props;

  const {
    triggerRuleInfoJson,
    attachedTags,
    attachedTagsToNote,
    removedTags,
    removedTagsAfter,
    removedTagsBefore,
  } = processedOrderInfoEntity;

  const handleClose = () => {
    props.history.push('/processed-order-info');
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
    values.processedTime = convertDateTimeToServer(values.processedTime);

    if (errors.length === 0) {
      const entity = {
        ...processedOrderInfoEntity,
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
          <h2 id="subscriptionApp.processedOrderInfo.home.createOrEditLabel">Create or edit a ProcessedOrderInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : processedOrderInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="processed-order-info-id">ID</Label>
                  <AvInput id="processed-order-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="processed-order-info-shop">
                  Shop
                </Label>
                <AvField
                  id="processed-order-info-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="orderIdLabel" for="processed-order-info-orderId">
                  Order Id
                </Label>
                <AvField
                  id="processed-order-info-orderId"
                  type="string"
                  className="form-control"
                  name="orderId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="orderNumberLabel" for="processed-order-info-orderNumber">
                  Order Number
                </Label>
                <AvField
                  id="processed-order-info-orderNumber"
                  type="string"
                  className="form-control"
                  name="orderNumber"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="topicNameLabel" for="processed-order-info-topicName">
                  Topic Name
                </Label>
                <AvField id="processed-order-info-topicName" type="text" name="topicName" />
              </AvGroup>
              <AvGroup>
                <Label id="processedTimeLabel" for="processed-order-info-processedTime">
                  Processed Time
                </Label>
                <AvInput
                  id="processed-order-info-processedTime"
                  type="datetime-local"
                  className="form-control"
                  name="processedTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.processedOrderInfoEntity.processedTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="triggerRuleInfoJsonLabel" for="processed-order-info-triggerRuleInfoJson">
                  Trigger Rule Info Json
                </Label>
                <AvInput id="processed-order-info-triggerRuleInfoJson" type="textarea" name="triggerRuleInfoJson" />
              </AvGroup>
              <AvGroup>
                <Label id="attachedTagsLabel" for="processed-order-info-attachedTags">
                  Attached Tags
                </Label>
                <AvInput id="processed-order-info-attachedTags" type="textarea" name="attachedTags" />
              </AvGroup>
              <AvGroup>
                <Label id="attachedTagsToNoteLabel" for="processed-order-info-attachedTagsToNote">
                  Attached Tags To Note
                </Label>
                <AvInput id="processed-order-info-attachedTagsToNote" type="textarea" name="attachedTagsToNote" />
              </AvGroup>
              <AvGroup>
                <Label id="removedTagsLabel" for="processed-order-info-removedTags">
                  Removed Tags
                </Label>
                <AvInput id="processed-order-info-removedTags" type="textarea" name="removedTags" />
              </AvGroup>
              <AvGroup>
                <Label id="removedTagsAfterLabel" for="processed-order-info-removedTagsAfter">
                  Removed Tags After
                </Label>
                <AvInput id="processed-order-info-removedTagsAfter" type="textarea" name="removedTagsAfter" />
              </AvGroup>
              <AvGroup>
                <Label id="removedTagsBeforeLabel" for="processed-order-info-removedTagsBefore">
                  Removed Tags Before
                </Label>
                <AvInput id="processed-order-info-removedTagsBefore" type="textarea" name="removedTagsBefore" />
              </AvGroup>
              <AvGroup>
                <Label id="firstNameLabel" for="processed-order-info-firstName">
                  First Name
                </Label>
                <AvField id="processed-order-info-firstName" type="text" name="firstName" />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="processed-order-info-lastName">
                  Last Name
                </Label>
                <AvField id="processed-order-info-lastName" type="text" name="lastName" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="processed-order-info-status">
                  Status
                </Label>
                <AvInput
                  id="processed-order-info-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && processedOrderInfoEntity.status) || 'PROCESSING'}
                >
                  <option value="PROCESSING">PROCESSING</option>
                  <option value="COMPLETE">COMPLETE</option>
                  <option value="DELAYED">DELAYED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="delayedTaggingValueLabel" for="processed-order-info-delayedTaggingValue">
                  Delayed Tagging Value
                </Label>
                <AvField id="processed-order-info-delayedTaggingValue" type="string" className="form-control" name="delayedTaggingValue" />
              </AvGroup>
              <AvGroup>
                <Label id="delayedTaggingUnitLabel" for="processed-order-info-delayedTaggingUnit">
                  Delayed Tagging Unit
                </Label>
                <AvInput
                  id="processed-order-info-delayedTaggingUnit"
                  type="select"
                  className="form-control"
                  name="delayedTaggingUnit"
                  value={(!isNew && processedOrderInfoEntity.delayedTaggingUnit) || 'SECONDS'}
                >
                  <option value="SECONDS">SECONDS</option>
                  <option value="MINUTES">MINUTES</option>
                  <option value="HOURS">HOURS</option>
                  <option value="DAYS">DAYS</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/processed-order-info" replace color="info">
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
  processedOrderInfoEntity: storeState.processedOrderInfo.entity,
  loading: storeState.processedOrderInfo.loading,
  updating: storeState.processedOrderInfo.updating,
  updateSuccess: storeState.processedOrderInfo.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(ProcessedOrderInfoUpdate);
