import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './customer-processed-order-info.reducer';
import { ICustomerProcessedOrderInfo } from 'app/shared/model/customer-processed-order-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomerProcessedOrderInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerProcessedOrderInfoUpdate = (props: ICustomerProcessedOrderInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { customerProcessedOrderInfoEntity, loading, updating } = props;

  const {
    triggerRuleInfoJson,
    attachedTags,
    attachedTagsToNote,
    removedTags,
    removedTagsAfter,
    removedTagsBefore,
  } = customerProcessedOrderInfoEntity;

  const handleClose = () => {
    props.history.push('/customer-processed-order-info');
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
        ...customerProcessedOrderInfoEntity,
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
          <h2 id="subscriptionApp.customerProcessedOrderInfo.home.createOrEditLabel">Create or edit a CustomerProcessedOrderInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : customerProcessedOrderInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="customer-processed-order-info-id">ID</Label>
                  <AvInput id="customer-processed-order-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="customer-processed-order-info-shop">
                  Shop
                </Label>
                <AvField
                  id="customer-processed-order-info-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="orderIdLabel" for="customer-processed-order-info-orderId">
                  Order Id
                </Label>
                <AvField
                  id="customer-processed-order-info-orderId"
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
                <Label id="orderNumberLabel" for="customer-processed-order-info-orderNumber">
                  Order Number
                </Label>
                <AvField
                  id="customer-processed-order-info-orderNumber"
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
                <Label id="customerNumberLabel" for="customer-processed-order-info-customerNumber">
                  Customer Number
                </Label>
                <AvField
                  id="customer-processed-order-info-customerNumber"
                  type="string"
                  className="form-control"
                  name="customerNumber"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="topicNameLabel" for="customer-processed-order-info-topicName">
                  Topic Name
                </Label>
                <AvField id="customer-processed-order-info-topicName" type="text" name="topicName" />
              </AvGroup>
              <AvGroup>
                <Label id="processedTimeLabel" for="customer-processed-order-info-processedTime">
                  Processed Time
                </Label>
                <AvInput
                  id="customer-processed-order-info-processedTime"
                  type="datetime-local"
                  className="form-control"
                  name="processedTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.customerProcessedOrderInfoEntity.processedTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="triggerRuleInfoJsonLabel" for="customer-processed-order-info-triggerRuleInfoJson">
                  Trigger Rule Info Json
                </Label>
                <AvInput id="customer-processed-order-info-triggerRuleInfoJson" type="textarea" name="triggerRuleInfoJson" />
              </AvGroup>
              <AvGroup>
                <Label id="attachedTagsLabel" for="customer-processed-order-info-attachedTags">
                  Attached Tags
                </Label>
                <AvInput id="customer-processed-order-info-attachedTags" type="textarea" name="attachedTags" />
              </AvGroup>
              <AvGroup>
                <Label id="attachedTagsToNoteLabel" for="customer-processed-order-info-attachedTagsToNote">
                  Attached Tags To Note
                </Label>
                <AvInput id="customer-processed-order-info-attachedTagsToNote" type="textarea" name="attachedTagsToNote" />
              </AvGroup>
              <AvGroup>
                <Label id="removedTagsLabel" for="customer-processed-order-info-removedTags">
                  Removed Tags
                </Label>
                <AvInput id="customer-processed-order-info-removedTags" type="textarea" name="removedTags" />
              </AvGroup>
              <AvGroup>
                <Label id="removedTagsAfterLabel" for="customer-processed-order-info-removedTagsAfter">
                  Removed Tags After
                </Label>
                <AvInput id="customer-processed-order-info-removedTagsAfter" type="textarea" name="removedTagsAfter" />
              </AvGroup>
              <AvGroup>
                <Label id="removedTagsBeforeLabel" for="customer-processed-order-info-removedTagsBefore">
                  Removed Tags Before
                </Label>
                <AvInput id="customer-processed-order-info-removedTagsBefore" type="textarea" name="removedTagsBefore" />
              </AvGroup>
              <AvGroup>
                <Label id="firstNameLabel" for="customer-processed-order-info-firstName">
                  First Name
                </Label>
                <AvField id="customer-processed-order-info-firstName" type="text" name="firstName" />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="customer-processed-order-info-lastName">
                  Last Name
                </Label>
                <AvField id="customer-processed-order-info-lastName" type="text" name="lastName" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="customer-processed-order-info-status">
                  Status
                </Label>
                <AvInput
                  id="customer-processed-order-info-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && customerProcessedOrderInfoEntity.status) || 'PROCESSING'}
                >
                  <option value="PROCESSING">PROCESSING</option>
                  <option value="COMPLETE">COMPLETE</option>
                  <option value="DELAYED">DELAYED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="delayedTaggingValueLabel" for="customer-processed-order-info-delayedTaggingValue">
                  Delayed Tagging Value
                </Label>
                <AvField
                  id="customer-processed-order-info-delayedTaggingValue"
                  type="string"
                  className="form-control"
                  name="delayedTaggingValue"
                />
              </AvGroup>
              <AvGroup>
                <Label id="delayedTaggingUnitLabel" for="customer-processed-order-info-delayedTaggingUnit">
                  Delayed Tagging Unit
                </Label>
                <AvInput
                  id="customer-processed-order-info-delayedTaggingUnit"
                  type="select"
                  className="form-control"
                  name="delayedTaggingUnit"
                  value={(!isNew && customerProcessedOrderInfoEntity.delayedTaggingUnit) || 'SECONDS'}
                >
                  <option value="SECONDS">SECONDS</option>
                  <option value="MINUTES">MINUTES</option>
                  <option value="HOURS">HOURS</option>
                  <option value="DAYS">DAYS</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/customer-processed-order-info" replace color="info">
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
  customerProcessedOrderInfoEntity: storeState.customerProcessedOrderInfo.entity,
  loading: storeState.customerProcessedOrderInfo.loading,
  updating: storeState.customerProcessedOrderInfo.updating,
  updateSuccess: storeState.customerProcessedOrderInfo.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(CustomerProcessedOrderInfoUpdate);