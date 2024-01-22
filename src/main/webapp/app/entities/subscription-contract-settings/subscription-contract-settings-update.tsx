import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './subscription-contract-settings.reducer';
import { ISubscriptionContractSettings } from 'app/shared/model/subscription-contract-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionContractSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractSettingsUpdate = (props: ISubscriptionContractSettingsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionContractSettingsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/subscription-contract-settings');
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
        ...subscriptionContractSettingsEntity,
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
          <h2 id="subscriptionApp.subscriptionContractSettings.home.createOrEditLabel">Create or edit a SubscriptionContractSettings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionContractSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-contract-settings-id">ID</Label>
                  <AvInput id="subscription-contract-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-contract-settings-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-contract-settings-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="productIdLabel" for="subscription-contract-settings-productId">
                  Product Id
                </Label>
                <AvField id="subscription-contract-settings-productId" type="text" name="productId" />
              </AvGroup>
              <AvGroup>
                <Label id="endsOnCountLabel" for="subscription-contract-settings-endsOnCount">
                  Ends On Count
                </Label>
                <AvField
                  id="subscription-contract-settings-endsOnCount"
                  type="string"
                  className="form-control"
                  name="endsOnCount"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endsOnIntervalLabel" for="subscription-contract-settings-endsOnInterval">
                  Ends On Interval
                </Label>
                <AvInput
                  id="subscription-contract-settings-endsOnInterval"
                  type="select"
                  className="form-control"
                  name="endsOnInterval"
                  value={(!isNew && subscriptionContractSettingsEntity.endsOnInterval) || 'DAY'}
                >
                  <option value="DAY">DAY</option>
                  <option value="WEEK">WEEK</option>
                  <option value="MONTH">MONTH</option>
                  <option value="YEAR">YEAR</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-contract-settings" replace color="info">
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
  subscriptionContractSettingsEntity: storeState.subscriptionContractSettings.entity,
  loading: storeState.subscriptionContractSettings.loading,
  updating: storeState.subscriptionContractSettings.updating,
  updateSuccess: storeState.subscriptionContractSettings.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractSettingsUpdate);
