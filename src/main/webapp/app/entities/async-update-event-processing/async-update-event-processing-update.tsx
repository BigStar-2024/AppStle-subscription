import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './async-update-event-processing.reducer';
import { IAsyncUpdateEventProcessing } from 'app/shared/model/async-update-event-processing.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAsyncUpdateEventProcessingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AsyncUpdateEventProcessingUpdate = (props: IAsyncUpdateEventProcessingUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { asyncUpdateEventProcessingEntity, loading, updating } = props;

  const { tagModelJson } = asyncUpdateEventProcessingEntity;

  const handleClose = () => {
    props.history.push('/async-update-event-processing' + props.location.search);
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
    values.lastUpdated = convertDateTimeToServer(values.lastUpdated);

    if (errors.length === 0) {
      const entity = {
        ...asyncUpdateEventProcessingEntity,
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
          <h2 id="subscriptionApp.asyncUpdateEventProcessing.home.createOrEditLabel">Create or edit a AsyncUpdateEventProcessing</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : asyncUpdateEventProcessingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="async-update-event-processing-id">ID</Label>
                  <AvInput id="async-update-event-processing-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="subscriptionContractIdLabel" for="async-update-event-processing-subscriptionContractId">
                  Subscription Contract Id
                </Label>
                <AvField
                  id="async-update-event-processing-subscriptionContractId"
                  type="string"
                  className="form-control"
                  name="subscriptionContractId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lastUpdatedLabel" for="async-update-event-processing-lastUpdated">
                  Last Updated
                </Label>
                <AvInput
                  id="async-update-event-processing-lastUpdated"
                  type="datetime-local"
                  className="form-control"
                  name="lastUpdated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.asyncUpdateEventProcessingEntity.lastUpdated)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="tagModelJsonLabel" for="async-update-event-processing-tagModelJson">
                  Tag Model Json
                </Label>
                <AvInput id="async-update-event-processing-tagModelJson" type="textarea" name="tagModelJson" />
              </AvGroup>
              <AvGroup>
                <Label id="firstTimeOrderTagsLabel" for="async-update-event-processing-firstTimeOrderTags">
                  First Time Order Tags
                </Label>
                <AvField id="async-update-event-processing-firstTimeOrderTags" type="text" name="firstTimeOrderTags" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/async-update-event-processing" replace color="info">
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
  asyncUpdateEventProcessingEntity: storeState.asyncUpdateEventProcessing.entity,
  loading: storeState.asyncUpdateEventProcessing.loading,
  updating: storeState.asyncUpdateEventProcessing.updating,
  updateSuccess: storeState.asyncUpdateEventProcessing.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(AsyncUpdateEventProcessingUpdate);
