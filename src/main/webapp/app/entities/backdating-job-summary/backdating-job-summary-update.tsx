import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './backdating-job-summary.reducer';
import { IBackdatingJobSummary } from 'app/shared/model/backdating-job-summary.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBackdatingJobSummaryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BackdatingJobSummaryUpdate = (props: IBackdatingJobSummaryUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { backdatingJobSummaryEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/backdating-job-summary');
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
        ...backdatingJobSummaryEntity,
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
          <h2 id="subscriptionApp.backdatingJobSummary.home.createOrEditLabel">Create or edit a BackdatingJobSummary</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : backdatingJobSummaryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="backdating-job-summary-id">ID</Label>
                  <AvInput id="backdating-job-summary-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="backdating-job-summary-shop">
                  Shop
                </Label>
                <AvField
                  id="backdating-job-summary-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="jobOrdersTypeLabel" for="backdating-job-summary-jobOrdersType">
                  Job Orders Type
                </Label>
                <AvInput
                  id="backdating-job-summary-jobOrdersType"
                  type="select"
                  className="form-control"
                  name="jobOrdersType"
                  value={(!isNew && backdatingJobSummaryEntity.jobOrdersType) || 'ALL_ORDERS'}
                >
                  <option value="ALL_ORDERS">ALL_ORDERS</option>
                  <option value="ORDERS_DATE_RANGE">ORDERS_DATE_RANGE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="jobOrdersBeginDateLabel" for="backdating-job-summary-jobOrdersBeginDate">
                  Job Orders Begin Date
                </Label>
                <AvField id="backdating-job-summary-jobOrdersBeginDate" type="date" className="form-control" name="jobOrdersBeginDate" />
              </AvGroup>
              <AvGroup>
                <Label id="jobOrdersEndDateLabel" for="backdating-job-summary-jobOrdersEndDate">
                  Job Orders End Date
                </Label>
                <AvField id="backdating-job-summary-jobOrdersEndDate" type="date" className="form-control" name="jobOrdersEndDate" />
              </AvGroup>
              <AvGroup>
                <Label id="jobRulesTypeLabel" for="backdating-job-summary-jobRulesType">
                  Job Rules Type
                </Label>
                <AvInput
                  id="backdating-job-summary-jobRulesType"
                  type="select"
                  className="form-control"
                  name="jobRulesType"
                  value={(!isNew && backdatingJobSummaryEntity.jobRulesType) || 'ALL_RULES'}
                >
                  <option value="ALL_RULES">ALL_RULES</option>
                  <option value="SPECIFIC_RULES">SPECIFIC_RULES</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="triggerRuleIdsLabel" for="backdating-job-summary-triggerRuleIds">
                  Trigger Rule Ids
                </Label>
                <AvField id="backdating-job-summary-triggerRuleIds" type="text" name="triggerRuleIds" />
              </AvGroup>
              <AvGroup>
                <Label id="applicationChargeIdLabel" for="backdating-job-summary-applicationChargeId">
                  Application Charge Id
                </Label>
                <AvField
                  id="backdating-job-summary-applicationChargeId"
                  type="string"
                  className="form-control"
                  name="applicationChargeId"
                />
              </AvGroup>
              <AvGroup>
                <Label id="chargeLabel" for="backdating-job-summary-charge">
                  Charge
                </Label>
                <AvField id="backdating-job-summary-charge" type="string" className="form-control" name="charge" />
              </AvGroup>
              <AvGroup>
                <Label id="ordersCountLabel" for="backdating-job-summary-ordersCount">
                  Orders Count
                </Label>
                <AvField id="backdating-job-summary-ordersCount" type="string" className="form-control" name="ordersCount" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="backdating-job-summary-status">
                  Status
                </Label>
                <AvInput
                  id="backdating-job-summary-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && backdatingJobSummaryEntity.status) || 'COMPLETE'}
                >
                  <option value="COMPLETE">COMPLETE</option>
                  <option value="IN_PROGRESS">IN_PROGRESS</option>
                  <option value="FAILED">FAILED</option>
                  <option value="YET_TO_START">YET_TO_START</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="paymentAcceptedLabel">
                  <AvInput
                    id="backdating-job-summary-paymentAccepted"
                    type="checkbox"
                    className="form-check-input"
                    name="paymentAccepted"
                  />
                  Payment Accepted
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="orderMigrationIdentifierLabel" for="backdating-job-summary-orderMigrationIdentifier">
                  Order Migration Identifier
                </Label>
                <AvField id="backdating-job-summary-orderMigrationIdentifier" type="text" name="orderMigrationIdentifier" />
              </AvGroup>
              <AvGroup>
                <Label id="totalOrdersCompletedLabel" for="backdating-job-summary-totalOrdersCompleted">
                  Total Orders Completed
                </Label>
                <AvField
                  id="backdating-job-summary-totalOrdersCompleted"
                  type="string"
                  className="form-control"
                  name="totalOrdersCompleted"
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/backdating-job-summary" replace color="info">
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
  backdatingJobSummaryEntity: storeState.backdatingJobSummary.entity,
  loading: storeState.backdatingJobSummary.loading,
  updating: storeState.backdatingJobSummary.updating,
  updateSuccess: storeState.backdatingJobSummary.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BackdatingJobSummaryUpdate);
