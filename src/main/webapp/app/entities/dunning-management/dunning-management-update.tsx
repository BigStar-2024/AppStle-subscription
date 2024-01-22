import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './dunning-management.reducer';
import { IDunningManagement } from 'app/shared/model/dunning-management.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDunningManagementUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DunningManagementUpdate = (props: IDunningManagementUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { dunningManagementEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/dunning-management');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...dunningManagementEntity,
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
          <h2 id="subscriptionApp.dunningManagement.home.createOrEditLabel">Create or edit a DunningManagement</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : dunningManagementEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="dunning-management-id">ID</Label>
                  <AvInput id="dunning-management-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="dunning-management-shop">
                  Shop
                </Label>
                <AvField
                  id="dunning-management-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="retryAttemptsLabel" for="dunning-management-retryAttempts">
                  Retry Attempts
                </Label>
                <AvInput
                  id="dunning-management-retryAttempts"
                  type="select"
                  className="form-control"
                  name="retryAttempts"
                  value={(!isNew && dunningManagementEntity.retryAttempts) || 'ONE_ATTEMPT'}
                >
                  <option value="ONE_ATTEMPT">ONE_ATTEMPT</option>
                  <option value="TWO_ATTEMPTS">TWO_ATTEMPTS</option>
                  <option value="THREE_ATTEMPTS">THREE_ATTEMPTS</option>
                  <option value="FOUR_ATTEMPTS">FOUR_ATTEMPTS</option>
                  <option value="FIVE_ATTEMPTS">FIVE_ATTEMPTS</option>
                  <option value="SIX_ATTEMPTS">SIX_ATTEMPTS</option>
                  <option value="SEVEN_ATTEMPTS">SEVEN_ATTEMPTS</option>
                  <option value="EIGHT_ATTEMPTS">EIGHT_ATTEMPTS</option>
                  <option value="NINE_ATTEMPTS">NINE_ATTEMPTS</option>
                  <option value="TEN_ATTEMPTS">TEN_ATTEMPTS</option>
                  <option value="ELEVEN_ATTEMPTS">ELEVEN_ATTEMPTS</option>
                  <option value="TWELVE_ATTEMPTS">TWELVE_ATTEMPTS</option>
                  <option value="THIRTEEN_ATTEMPTS">THIRTEEN_ATTEMPTS</option>
                  <option value="FOURTEEN_ATTEMPTS">FOURTEEN_ATTEMPTS</option>
                  <option value="FIFTEEN_ATTEMPTS">FIFTEEN_ATTEMPTS</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="daysBeforeRetryingLabel" for="dunning-management-daysBeforeRetrying">
                  Days Before Retrying
                </Label>
                <AvInput
                  id="dunning-management-daysBeforeRetrying"
                  type="select"
                  className="form-control"
                  name="daysBeforeRetrying"
                  value={(!isNew && dunningManagementEntity.daysBeforeRetrying) || 'ONE_DAY'}
                >
                  <option value="ONE_DAY">ONE_DAY</option>
                  <option value="TWO_DAYS">TWO_DAYS</option>
                  <option value="THREE_DAYS">THREE_DAYS</option>
                  <option value="FOUR_DAYS">FOUR_DAYS</option>
                  <option value="FIVE_DAYS">FIVE_DAYS</option>
                  <option value="SIX_DAYS">SIX_DAYS</option>
                  <option value="SEVEN_DAYS">SEVEN_DAYS</option>
                  <option value="EIGHT_DAYS">EIGHT_DAYS</option>
                  <option value="NINE_DAYS">NINE_DAYS</option>
                  <option value="TEN_DAYS">TEN_DAYS</option>
                  <option value="ELEVEN_DAYS">ELEVEN_DAYS</option>
                  <option value="TWELVE_DAYS">TWELVE_DAYS</option>
                  <option value="THIRTEEN_DAYS">THIRTEEN_DAYS</option>
                  <option value="FOURTEEN_DAYS">FOURTEEN_DAYS</option>
                  <option value="FIFTEEN_DAYS">FIFTEEN_DAYS</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="maxNumberOfFailuresLabel" for="dunning-management-maxNumberOfFailures">
                  Max Number Of Failures
                </Label>
                <AvInput
                  id="dunning-management-maxNumberOfFailures"
                  type="select"
                  className="form-control"
                  name="maxNumberOfFailures"
                  value={(!isNew && dunningManagementEntity.maxNumberOfFailures) || 'CANCEL_SUBSCRIPTION'}
                >
                  <option value="CANCEL_SUBSCRIPTION">CANCEL_SUBSCRIPTION</option>
                  <option value="PAUSE_SUBSCRIPTION">PAUSE_SUBSCRIPTION</option>
                  <option value="SKIP_FAILED_ORDER">SKIP_FAILED_ORDER</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/dunning-management" replace color="info">
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
  dunningManagementEntity: storeState.dunningManagement.entity,
  loading: storeState.dunningManagement.loading,
  updating: storeState.dunningManagement.updating,
  updateSuccess: storeState.dunningManagement.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DunningManagementUpdate);
