import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './trigger-rule.reducer';
import { ITriggerRule } from 'app/shared/model/trigger-rule.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITriggerRuleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TriggerRuleUpdate = (props: ITriggerRuleUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { triggerRuleEntity, loading, updating } = props;

  const { fixedTags, dynamicTags, removeTags, notMatchTags, handlerData } = triggerRuleEntity;

  const handleClose = () => {
    props.history.push('/trigger-rule');
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
        ...triggerRuleEntity,
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
          <h2 id="subscriptionApp.triggerRule.home.createOrEditLabel">Create or edit a TriggerRule</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : triggerRuleEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="trigger-rule-id">ID</Label>
                  <AvInput id="trigger-rule-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="trigger-rule-shop">
                  Shop
                </Label>
                <AvField
                  id="trigger-rule-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="trigger-rule-name">
                  Name
                </Label>
                <AvField id="trigger-rule-name" type="text" name="name" />
              </AvGroup>
              <AvGroup check>
                <Label id="appendToNoteLabel">
                  <AvInput id="trigger-rule-appendToNote" type="checkbox" className="form-check-input" name="appendToNote" />
                  Append To Note
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="webhookLabel" for="trigger-rule-webhook">
                  Webhook
                </Label>
                <AvInput
                  id="trigger-rule-webhook"
                  type="select"
                  className="form-control"
                  name="webhook"
                  value={(!isNew && triggerRuleEntity.webhook) || 'ORDER_CREATE'}
                >
                  <option value="ORDER_CREATE">ORDER_CREATE</option>
                  <option value="ORDER_CANCELLED">ORDER_CANCELLED</option>
                  <option value="ORDER_FULFILLED">ORDER_FULFILLED</option>
                  <option value="ORDER_PARTIALLY_FULFILLED">ORDER_PARTIALLY_FULFILLED</option>
                  <option value="REFUND_ISSUED">REFUND_ISSUED</option>
                  <option value="FULFILLMENT_UPDATED">FULFILLMENT_UPDATED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="deactivateAfterDateLabel" for="trigger-rule-deactivateAfterDate">
                  Deactivate After Date
                </Label>
                <AvField id="trigger-rule-deactivateAfterDate" type="date" className="form-control" name="deactivateAfterDate" />
              </AvGroup>
              <AvGroup>
                <Label id="deactivateAfterTimeLabel" for="trigger-rule-deactivateAfterTime">
                  Deactivate After Time
                </Label>
                <AvField id="trigger-rule-deactivateAfterTime" type="text" name="deactivateAfterTime" />
              </AvGroup>
              <AvGroup>
                <Label id="fixedTagsLabel" for="trigger-rule-fixedTags">
                  Fixed Tags
                </Label>
                <AvInput id="trigger-rule-fixedTags" type="textarea" name="fixedTags" />
              </AvGroup>
              <AvGroup>
                <Label id="dynamicTagsLabel" for="trigger-rule-dynamicTags">
                  Dynamic Tags
                </Label>
                <AvInput id="trigger-rule-dynamicTags" type="textarea" name="dynamicTags" />
              </AvGroup>
              <AvGroup>
                <Label id="removeTagsLabel" for="trigger-rule-removeTags">
                  Remove Tags
                </Label>
                <AvInput id="trigger-rule-removeTags" type="textarea" name="removeTags" />
              </AvGroup>
              <AvGroup>
                <Label id="notMatchTagsLabel" for="trigger-rule-notMatchTags">
                  Not Match Tags
                </Label>
                <AvInput id="trigger-rule-notMatchTags" type="textarea" name="notMatchTags" />
              </AvGroup>
              <AvGroup>
                <Label id="removeTagsExpiresInLabel" for="trigger-rule-removeTagsExpiresIn">
                  Remove Tags Expires In
                </Label>
                <AvField id="trigger-rule-removeTagsExpiresIn" type="string" className="form-control" name="removeTagsExpiresIn" />
              </AvGroup>
              <AvGroup>
                <Label id="removeTagsExpiresInUnitLabel" for="trigger-rule-removeTagsExpiresInUnit">
                  Remove Tags Expires In Unit
                </Label>
                <AvInput
                  id="trigger-rule-removeTagsExpiresInUnit"
                  type="select"
                  className="form-control"
                  name="removeTagsExpiresInUnit"
                  value={(!isNew && triggerRuleEntity.removeTagsExpiresInUnit) || 'MINUTES'}
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
                <Label id="handlerDataLabel" for="trigger-rule-handlerData">
                  Handler Data
                </Label>
                <AvInput id="trigger-rule-handlerData" type="textarea" name="handlerData" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/trigger-rule" replace color="info">
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
  triggerRuleEntity: storeState.triggerRule.entity,
  loading: storeState.triggerRule.loading,
  updating: storeState.triggerRule.updating,
  updateSuccess: storeState.triggerRule.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(TriggerRuleUpdate);
