import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './activity-updates-settings.reducer';
import { IActivityUpdatesSettings } from 'app/shared/model/activity-updates-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IActivityUpdatesSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActivityUpdatesSettingsUpdate = (props: IActivityUpdatesSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { activityUpdatesSettingsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/activity-updates-settings' + props.location.search);
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
    values.summaryReportLastSent = convertDateTimeToServer(values.summaryReportLastSent);

    if (errors.length === 0) {
      const entity = {
        ...activityUpdatesSettingsEntity,
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
          <h2 id="subscriptionApp.activityUpdatesSettings.home.createOrEditLabel">Create or edit a ActivityUpdatesSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : activityUpdatesSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="activity-updates-settings-id">ID</Label>
                  <AvInput id="activity-updates-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="activity-updates-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="activity-updates-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="summaryReportEnabledLabel">
                  <AvInput
                    id="activity-updates-settings-summaryReportEnabled"
                    type="checkbox"
                    className="form-check-input"
                    name="summaryReportEnabled"
                  />
                  Summary Report Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="summaryReportFrequencyLabel" for="activity-updates-settings-summaryReportFrequency">
                  Summary Report Frequency
                </Label>
                <AvField
                  id="activity-updates-settings-summaryReportFrequency"
                  type="text"
                  name="summaryReportFrequency"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="summaryReportDeliverToEmailLabel" for="activity-updates-settings-summaryReportDeliverToEmail">
                  Summary Report Deliver To Email
                </Label>
                <AvField id="activity-updates-settings-summaryReportDeliverToEmail" type="text" name="summaryReportDeliverToEmail" />
              </AvGroup>
              <AvGroup>
                <Label id="summaryReportTimePeriodLabel" for="activity-updates-settings-summaryReportTimePeriod">
                  Summary Report Time Period
                </Label>
                <AvField
                  id="activity-updates-settings-summaryReportTimePeriod"
                  type="text"
                  name="summaryReportTimePeriod"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="summaryReportLastSentLabel" for="activity-updates-settings-summaryReportLastSent">
                  Summary Report Last Sent
                </Label>
                <AvInput
                  id="activity-updates-settings-summaryReportLastSent"
                  type="datetime-local"
                  className="form-control"
                  name="summaryReportLastSent"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={
                    isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.activityUpdatesSettingsEntity.summaryReportLastSent)
                  }
                />
              </AvGroup>
              <AvGroup check>
                <Label id="summaryReportProcessingLabel">
                  <AvInput
                    id="activity-updates-settings-summaryReportProcessing"
                    type="checkbox"
                    className="form-check-input"
                    name="summaryReportProcessing"
                  />
                  Summary Report Processing
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/activity-updates-settings" replace color="info">
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
  activityUpdatesSettingsEntity: storeState.activityUpdatesSettings.entity,
  loading: storeState.activityUpdatesSettings.loading,
  updating: storeState.activityUpdatesSettings.updating,
  updateSuccess: storeState.activityUpdatesSettings.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActivityUpdatesSettingsUpdate);
