import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './customer-trigger-rule.reducer';
import { ICustomerTriggerRule } from 'app/shared/model/customer-trigger-rule.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomerTriggerRuleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerTriggerRuleUpdate = (props: ICustomerTriggerRuleUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { customerTriggerRuleEntity, loading, updating } = props;

  const { fixedTags, dynamicTags, removeTags, notMatchTags, handlerData } = customerTriggerRuleEntity;

  const handleClose = () => {
    props.history.push('/customer-trigger-rule');
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
    values.deactivatedAt = convertDateTimeToServer(values.deactivatedAt);

    if (errors.length === 0) {
      const entity = {
        ...customerTriggerRuleEntity,
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
          <h2 id="subscriptionApp.customerTriggerRule.home.createOrEditLabel">Create or edit a CustomerTriggerRule</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : customerTriggerRuleEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="customer-trigger-rule-id">ID</Label>
                  <AvInput id="customer-trigger-rule-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="customer-trigger-rule-shop">
                  Shop
                </Label>
                <AvField
                  id="customer-trigger-rule-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="customer-trigger-rule-name">
                  Name
                </Label>
                <AvField id="customer-trigger-rule-name" type="text" name="name" />
              </AvGroup>
              <AvGroup check>
                <Label id="appendToNoteLabel">
                  <AvInput id="customer-trigger-rule-appendToNote" type="checkbox" className="form-check-input" name="appendToNote" />
                  Append To Note
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="webhookLabel" for="customer-trigger-rule-webhook">
                  Webhook
                </Label>
                <AvInput
                  id="customer-trigger-rule-webhook"
                  type="select"
                  className="form-control"
                  name="webhook"
                  value={(!isNew && customerTriggerRuleEntity.webhook) || 'ORDER_CREATE'}
                >
                  <option value="ORDER_CREATE">ORDER_CREATE</option>
                  <option value="ORDER_FULFILLED">ORDER_FULFILLED</option>
                  <option value="ORDER_PARTIALLY_FULFILLED">ORDER_PARTIALLY_FULFILLED</option>
                  <option value="ORDER_CANCELLED">ORDER_CANCELLED</option>
                  <option value="REFUND_ISSUED">REFUND_ISSUED</option>
                  <option value="FULFILLMENT_UPDATED">FULFILLMENT_UPDATED</option>
                  <option value="ACCOUNT_CREATE">ACCOUNT_CREATE</option>
                  <option value="ACCOUNT_ACTIVATE">ACCOUNT_ACTIVATE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="deactivateAfterDateLabel" for="customer-trigger-rule-deactivateAfterDate">
                  Deactivate After Date
                </Label>
                <AvField id="customer-trigger-rule-deactivateAfterDate" type="date" className="form-control" name="deactivateAfterDate" />
              </AvGroup>
              <AvGroup>
                <Label id="deactivateAfterTimeLabel" for="customer-trigger-rule-deactivateAfterTime">
                  Deactivate After Time
                </Label>
                <AvField id="customer-trigger-rule-deactivateAfterTime" type="text" name="deactivateAfterTime" />
              </AvGroup>
              <AvGroup>
                <Label id="fixedTagsLabel" for="customer-trigger-rule-fixedTags">
                  Fixed Tags
                </Label>
                <AvInput id="customer-trigger-rule-fixedTags" type="textarea" name="fixedTags" />
              </AvGroup>
              <AvGroup>
                <Label id="dynamicTagsLabel" for="customer-trigger-rule-dynamicTags">
                  Dynamic Tags
                </Label>
                <AvInput id="customer-trigger-rule-dynamicTags" type="textarea" name="dynamicTags" />
              </AvGroup>
              <AvGroup>
                <Label id="removeTagsLabel" for="customer-trigger-rule-removeTags">
                  Remove Tags
                </Label>
                <AvInput id="customer-trigger-rule-removeTags" type="textarea" name="removeTags" />
              </AvGroup>
              <AvGroup>
                <Label id="notMatchTagsLabel" for="customer-trigger-rule-notMatchTags">
                  Not Match Tags
                </Label>
                <AvInput id="customer-trigger-rule-notMatchTags" type="textarea" name="notMatchTags" />
              </AvGroup>
              <AvGroup>
                <Label id="removeTagsExpiresInLabel" for="customer-trigger-rule-removeTagsExpiresIn">
                  Remove Tags Expires In
                </Label>
                <AvField id="customer-trigger-rule-removeTagsExpiresIn" type="string" className="form-control" name="removeTagsExpiresIn" />
              </AvGroup>
              <AvGroup>
                <Label id="removeTagsExpiresInUnitLabel" for="customer-trigger-rule-removeTagsExpiresInUnit">
                  Remove Tags Expires In Unit
                </Label>
                <AvInput
                  id="customer-trigger-rule-removeTagsExpiresInUnit"
                  type="select"
                  className="form-control"
                  name="removeTagsExpiresInUnit"
                  value={(!isNew && customerTriggerRuleEntity.removeTagsExpiresInUnit) || 'MINUTES'}
                >
                  <option value="MINUTES">MINUTES</option>
                  <option value="HOURS">HOURS</option>
                  <option value="DAYS">DAYS</option>
                  <option value="WEEKS">WEEKS</option>
                  <option value="MONTHS">MONTHS</option>
                  <option value="YEARS">YEARS</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="handlerDataLabel" for="customer-trigger-rule-handlerData">
                  Handler Data
                </Label>
                <AvInput id="customer-trigger-rule-handlerData" type="textarea" name="handlerData" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="customer-trigger-rule-status">
                  Status
                </Label>
                <AvInput
                  id="customer-trigger-rule-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && customerTriggerRuleEntity.status) || 'ACTIVE'}
                >
                  <option value="ACTIVE">ACTIVE</option>
                  <option value="DEACTIVATE">DEACTIVATE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="deactivatedAtLabel" for="customer-trigger-rule-deactivatedAt">
                  Deactivated At
                </Label>
                <AvInput
                  id="customer-trigger-rule-deactivatedAt"
                  type="datetime-local"
                  className="form-control"
                  name="deactivatedAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.customerTriggerRuleEntity.deactivatedAt)}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/customer-trigger-rule" replace color="info">
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
  customerTriggerRuleEntity: storeState.customerTriggerRule.entity,
  loading: storeState.customerTriggerRule.loading,
  updating: storeState.customerTriggerRule.updating,
  updateSuccess: storeState.customerTriggerRule.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(CustomerTriggerRuleUpdate);
