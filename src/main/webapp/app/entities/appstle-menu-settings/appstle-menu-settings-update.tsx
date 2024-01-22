import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './appstle-menu-settings.reducer';
import { IAppstleMenuSettings } from 'app/shared/model/appstle-menu-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAppstleMenuSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AppstleMenuSettingsUpdate = (props: IAppstleMenuSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { appstleMenuSettingsEntity, loading, updating } = props;

  const { filterMenu, menuStyle } = appstleMenuSettingsEntity;

  const handleClose = () => {
    props.history.push('/appstle-menu-settings' + props.location.search);
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
        ...appstleMenuSettingsEntity,
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
          <h2 id="subscriptionApp.appstleMenuSettings.home.createOrEditLabel">Create or edit a AppstleMenuSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : appstleMenuSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="appstle-menu-settings-id">ID</Label>
                  <AvInput id="appstle-menu-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="appstle-menu-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="appstle-menu-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="filterMenuLabel" for="appstle-menu-settings-filterMenu">
                  Filter Menu
                </Label>
                <AvInput id="appstle-menu-settings-filterMenu" type="textarea" name="filterMenu" />
              </AvGroup>
              <AvGroup>
                <Label id="menuUrlLabel" for="appstle-menu-settings-menuUrl">
                  Menu Url
                </Label>
                <AvField id="appstle-menu-settings-menuUrl" type="text" name="menuUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="menuStyleLabel" for="appstle-menu-settings-menuStyle">
                  Menu Style
                </Label>
                <AvInput id="appstle-menu-settings-menuStyle" type="textarea" name="menuStyle" />
              </AvGroup>
              <AvGroup check>
                <Label id="activeLabel">
                  <AvInput id="appstle-menu-settings-active" type="checkbox" className="form-check-input" name="active" />
                  Active
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="handleLabel" for="appstle-menu-settings-handle">
                  Handle
                </Label>
                <AvField id="appstle-menu-settings-handle" type="text" name="handle" />
              </AvGroup>
              <AvGroup>
                <Label id="productViewStyleLabel" for="appstle-menu-settings-productViewStyle">
                  Product View Style
                </Label>
                <AvInput
                  id="appstle-menu-settings-productViewStyle"
                  type="select"
                  className="form-control"
                  name="productViewStyle"
                  value={(!isNew && appstleMenuSettingsEntity.productViewStyle) || 'QUICK_ADD'}
                >
                  <option value="QUICK_ADD">QUICK_ADD</option>
                  <option value="VIEW_DETAILS">VIEW_DETAILS</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/appstle-menu-settings" replace color="info">
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
  appstleMenuSettingsEntity: storeState.appstleMenuSettings.entity,
  loading: storeState.appstleMenuSettings.loading,
  updating: storeState.appstleMenuSettings.updating,
  updateSuccess: storeState.appstleMenuSettings.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(AppstleMenuSettingsUpdate);
