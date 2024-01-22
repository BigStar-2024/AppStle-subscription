import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './widget-template.reducer';
import { IWidgetTemplate } from 'app/shared/model/widget-template.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IWidgetTemplateUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const WidgetTemplateUpdate = (props: IWidgetTemplateUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { widgetTemplateEntity, loading, updating } = props;

  const { html } = widgetTemplateEntity;

  const handleClose = () => {
    props.history.push('/admin/widget-template');
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
        ...widgetTemplateEntity,
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
          <h2 id="subscriptionApp.widgetTemplate.home.createOrEditLabel">Create or edit a WidgetTemplate</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : widgetTemplateEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="widget-template-id">ID</Label>
                  <AvInput id="widget-template-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="typeLabel" for="widget-template-type">
                  Type
                </Label>
                <AvInput
                  id="widget-template-type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && widgetTemplateEntity.type) || 'WIDGET_TYPE_1'}
                >
                  <option value="WIDGET_TYPE_1">WIDGET_TYPE_1</option>
                  <option value="WIDGET_TYPE_2">WIDGET_TYPE_2</option>
                  <option value="WIDGET_TYPE_3">WIDGET_TYPE_3</option>
                  <option value="WIDGET_TYPE_4">WIDGET_TYPE_4</option>
                  <option value="WIDGET_TYPE_5">WIDGET_TYPE_5</option>
                  <option value="WIDGET_TYPE_6">WIDGET_TYPE_6</option>
                  <option value="WIDGET_TYPE_7">WIDGET_TYPE_7</option>
                  <option value="WIDGET_TYPE_8">WIDGET_TYPE_8</option>
                  <option value="WIDGET_TYPE_9">WIDGET_TYPE_9</option>
                  <option value="WIDGET_TYPE_10">WIDGET_TYPE_10</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="titleLabel" for="widget-template-title">
                  Title
                </Label>
                <AvField id="widget-template-title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label id="htmlLabel" for="widget-template-html">
                  Html
                </Label>
                <AvInput id="widget-template-html" type="textarea" name="html" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/admin/widget-template" replace color="info">
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
  widgetTemplateEntity: storeState.widgetTemplate.entity,
  loading: storeState.widgetTemplate.loading,
  updating: storeState.widgetTemplate.updating,
  updateSuccess: storeState.widgetTemplate.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(WidgetTemplateUpdate);
