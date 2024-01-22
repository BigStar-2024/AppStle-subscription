import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './customization.reducer';
import { ICustomization } from 'app/shared/model/customization.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomizationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomizationUpdate = (props: ICustomizationUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { customizationEntity, loading, updating } = props;

  const { css } = customizationEntity;

  const handleClose = () => {
    props.history.push('/admin/customization');
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
        ...customizationEntity,
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
          <h2 id="subscriptionApp.customization.home.createOrEditLabel">Create or edit a Customization</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : customizationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="customization-id">ID</Label>
                  <AvInput id="customization-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="labelLabel" for="customization-label">
                  Label
                </Label>
                <AvField id="customization-label" type="text" name="label" />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="customization-type">
                  Type
                </Label>
                <AvInput
                  id="customization-type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && customizationEntity.type) || 'COLOR_PICKER'}
                >
                  <option value="COLOR_PICKER">COLOR_PICKER</option>
                  <option value="PIXEL_FIELD">PIXEL_FIELD</option>
                  <option value="TOGGLE">TOGGLE</option>
                  <option value="TEXT_FIELD">TEXT_FIELD</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="cssLabel" for="customization-css">
                  Css
                </Label>
                <AvInput id="customization-css" type="textarea" name="css" />
              </AvGroup>
              <AvGroup>
                <Label id="categoryLabel" for="customization-category">
                  Category
                </Label>
                <AvInput
                  id="customization-category"
                  type="select"
                  className="form-control"
                  name="category"
                  value={(!isNew && customizationEntity.category) || 'PRODUCT_PAGE_WIDGET'}
                >
                  <option value="PRODUCT_PAGE_WIDGET">PRODUCT_PAGE_WIDGET</option>
                  <option value="CUSTOMER_PORTAL">CUSTOMER_PORTAL</option>
                  <option value="BUILD_A_BOX">BUILD_A_BOX</option>
                  <option value="CART_PAGE_WIDGET">CART_PAGE_WIDGET</option>
                  <option value="BUNDLING">BUNDLING</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/admin/customization" replace color="info">
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
  customizationEntity: storeState.customization.entity,
  loading: storeState.customization.loading,
  updating: storeState.customization.updating,
  updateSuccess: storeState.customization.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(CustomizationUpdate);
