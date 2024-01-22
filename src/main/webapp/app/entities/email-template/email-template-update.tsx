import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, /*setBlob,*/ reset } from './email-template.reducer';
import { IEmailTemplate } from 'app/shared/model/email-template.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { Editor } from '@monaco-editor/react';

export interface IEmailTemplateUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmailTemplateUpdate = (props: IEmailTemplateUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { emailTemplateEntity, loading, updating } = props;

  const { html } = emailTemplateEntity;
  const [emailTemplate, setEmailTemplate] = useState({ id: 0, html: '', emailType: '' });

  const handleClose = () => {
    props.history.push('/admin/email-template');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
      setEmailTemplate({ id: emailTemplateEntity.id, html: emailTemplateEntity.html, emailType: emailTemplateEntity.emailType });
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
//    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
//    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...emailTemplate,
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
          <h2 id="subscriptionApp.emailTemplate.home.createOrEditLabel">Create or edit a EmailTemplate</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : emailTemplateEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="email-template-id">ID</Label>
                  <AvInput id="email-template-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="emailTypeLabel" for="email-template-emailType">
                  Email Type
                </Label>
                <AvInput
                  id="email-template-emailType"
                  type="select"
                  className="form-control"
                  name="emailType"
                  value={(!isNew && emailTemplateEntity.emailType) || 'SUBSCRIPTION_CREATED'}
                >
                  <option value="SUBSCRIPTION_CREATED">SUBSCRIPTION_CREATED</option>
                  <option value="TRANSACTION_FAILED">TRANSACTION_FAILED</option>
                  <option value="UPCOMING_ORDER">UPCOMING_ORDER</option>
                  <option value="EXPIRING_CREDIT_CARD">EXPIRING_CREDIT_CARD</option>
                </AvInput>
              </AvGroup>
              {/* <AvGroup>
                <Label id="htmlLabel" for="email-template-html">
                  Html
                </Label>
                <AvInput id="email-template-html" type="textarea" name="html" />
              </AvGroup> */}
               <Editor
                height="90vh"
                value={emailTemplateEntity?.html || ""}
                onChange={(e, v) => setEmailTemplate({ emailType: emailTemplateEntity.emailType, html: v, id: emailTemplateEntity.id })}
                language="html"
              />
              <br></br>
              <Button tag={Link} id="cancel-save" to="/admin/email-template" replace color="info">
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
  emailTemplateEntity: storeState.emailTemplate.entity,
  loading: storeState.emailTemplate.loading,
  updating: storeState.emailTemplate.updating,
  updateSuccess: storeState.emailTemplate.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
//  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmailTemplateUpdate);
