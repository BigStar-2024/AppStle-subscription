import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './subscription-contract-processing.reducer';
import { ISubscriptionContractProcessing } from 'app/shared/model/subscription-contract-processing.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionContractProcessingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractProcessingUpdate = (props: ISubscriptionContractProcessingUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionContractProcessingEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/subscription-contract-processing' + props.location.search);
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
        ...subscriptionContractProcessingEntity,
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
          <h2 id="subscriptionApp.subscriptionContractProcessing.home.createOrEditLabel">
            Create or edit a SubscriptionContractProcessing
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionContractProcessingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-contract-processing-id">ID</Label>
                  <AvInput id="subscription-contract-processing-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="contractIdLabel" for="subscription-contract-processing-contractId">
                  Contract Id
                </Label>
                <AvField id="subscription-contract-processing-contractId" type="string" className="form-control" name="contractId" />
              </AvGroup>
              <AvGroup>
                <Label id="attemptCountLabel" for="subscription-contract-processing-attemptCount">
                  Attempt Count
                </Label>
                <AvField id="subscription-contract-processing-attemptCount" type="string" className="form-control" name="attemptCount" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-contract-processing" replace color="info">
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
  subscriptionContractProcessingEntity: storeState.subscriptionContractProcessing.entity,
  loading: storeState.subscriptionContractProcessing.loading,
  updating: storeState.subscriptionContractProcessing.updating,
  updateSuccess: storeState.subscriptionContractProcessing.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractProcessingUpdate);
