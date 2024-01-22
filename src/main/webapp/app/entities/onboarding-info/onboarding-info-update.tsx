import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './onboarding-info.reducer';
import { IOnboardingInfo } from 'app/shared/model/onboarding-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOnboardingInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OnboardingInfoUpdate = (props: IOnboardingInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { onboardingInfoEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/onboarding-info');
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
        ...onboardingInfoEntity,
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
          <h2 id="subscriptionApp.onboardingInfo.home.createOrEditLabel">Create or edit a OnboardingInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : onboardingInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="onboarding-info-id">ID</Label>
                  <AvInput id="onboarding-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="onboarding-info-shop">
                  Shop
                </Label>
                <AvField id="onboarding-info-shop" type="text" name="shop" />
              </AvGroup>
              <AvGroup>
                <Label id="uncompletedChecklistStepsLabel" for="onboarding-info-uncompletedChecklistSteps">
                  Uncompleted Checklist Steps
                </Label>
                <AvField id="onboarding-info-uncompletedChecklistSteps" type="text" name="uncompletedChecklistSteps" />
              </AvGroup>
              <AvGroup>
                <Label id="completedChecklistStepsLabel" for="onboarding-info-completedChecklistSteps">
                  Completed Checklist Steps
                </Label>
                <AvField id="onboarding-info-completedChecklistSteps" type="text" name="completedChecklistSteps" />
              </AvGroup>
              <AvGroup check>
                <Label id="checklistCompletedLabel">
                  <AvInput id="onboarding-info-checklistCompleted" type="checkbox" className="form-check-input" name="checklistCompleted" />
                  Checklist Completed
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/onboarding-info" replace color="info">
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
  onboardingInfoEntity: storeState.onboardingInfo.entity,
  loading: storeState.onboardingInfo.loading,
  updating: storeState.onboardingInfo.updating,
  updateSuccess: storeState.onboardingInfo.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OnboardingInfoUpdate);
