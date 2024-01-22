import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './sms-template-setting.reducer';
import { ISmsTemplateSetting } from 'app/shared/model/sms-template-setting.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISmsTemplateSettingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SmsTemplateSettingUpdate = (props: ISmsTemplateSettingUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { smsTemplateSettingEntity, loading, updating } = props;

  const { smsContent } = smsTemplateSettingEntity;

  const handleClose = () => {
    props.history.push('/sms-template-setting');
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
        ...smsTemplateSettingEntity,
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
          <h2 id="subscriptionApp.smsTemplateSetting.home.createOrEditLabel">Create or edit a SmsTemplateSetting</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : smsTemplateSettingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="sms-template-setting-id">ID</Label>
                  <AvInput id="sms-template-setting-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="sms-template-setting-shop">
                  Shop
                </Label>
                <AvField
                  id="sms-template-setting-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="smsSettingTypeLabel" for="sms-template-setting-smsSettingType">
                  Sms Setting Type
                </Label>
                <AvInput
                  id="sms-template-setting-smsSettingType"
                  type="select"
                  className="form-control"
                  name="smsSettingType"
                  value={(!isNew && smsTemplateSettingEntity.smsSettingType) || 'SUBSCRIPTION_CREATED'}
                >
                  <option value="SUBSCRIPTION_CREATED">SUBSCRIPTION_CREATED</option>
                  <option value="TRANSACTION_FAILED">TRANSACTION_FAILED</option>
                  <option value="UPCOMING_ORDER">UPCOMING_ORDER</option>
                  <option value="EXPIRING_CREDIT_CARD">EXPIRING_CREDIT_CARD</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="sendSmsDisabledLabel">
                  <AvInput id="sms-template-setting-sendSmsDisabled" type="checkbox" className="form-check-input" name="sendSmsDisabled" />
                  Send Sms Disabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="smsContentLabel" for="sms-template-setting-smsContent">
                  Sms Content
                </Label>
                <AvInput
                  id="sms-template-setting-smsContent"
                  type="textarea"
                  name="smsContent"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="stopReplySMSLabel">
                  <AvInput id="sms-template-setting-stopReplySMS" type="checkbox" className="form-check-input" name="stopReplySMS" />
                  Stop Reply SMS
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/sms-template-setting" replace color="info">
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
  smsTemplateSettingEntity: storeState.smsTemplateSetting.entity,
  loading: storeState.smsTemplateSetting.loading,
  updating: storeState.smsTemplateSetting.updating,
  updateSuccess: storeState.smsTemplateSetting.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(SmsTemplateSettingUpdate);
