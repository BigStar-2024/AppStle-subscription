import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './bundle-dynamic-script.reducer';
import { IBundleDynamicScript } from 'app/shared/model/bundle-dynamic-script.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBundleDynamicScriptUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BundleDynamicScriptUpdate = (props: IBundleDynamicScriptUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { bundleDynamicScriptEntity, loading, updating } = props;

  const { dynamicScript } = bundleDynamicScriptEntity;

  const handleClose = () => {
    props.history.push('/bundle-dynamic-script');
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
        ...bundleDynamicScriptEntity,
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
          <h2 id="subscriptionApp.bundleDynamicScript.home.createOrEditLabel">Create or edit a BundleDynamicScript</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bundleDynamicScriptEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="bundle-dynamic-script-id">ID</Label>
                  <AvInput id="bundle-dynamic-script-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="bundle-dynamic-script-shop">
                  Shop
                </Label>
                <AvField
                  id="bundle-dynamic-script-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dynamicScriptLabel" for="bundle-dynamic-script-dynamicScript">
                  Dynamic Script
                </Label>
                <AvInput id="bundle-dynamic-script-dynamicScript" type="textarea" name="dynamicScript" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/bundle-dynamic-script" replace color="info">
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
  bundleDynamicScriptEntity: storeState.bundleDynamicScript.entity,
  loading: storeState.bundleDynamicScript.loading,
  updating: storeState.bundleDynamicScript.updating,
  updateSuccess: storeState.bundleDynamicScript.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(BundleDynamicScriptUpdate);
