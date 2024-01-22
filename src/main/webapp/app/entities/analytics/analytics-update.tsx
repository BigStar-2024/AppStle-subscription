import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './analytics.reducer';
import { IAnalytics } from 'app/shared/model/analytics.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnalyticsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnalyticsUpdate = (props: IAnalyticsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { analyticsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/analytics');
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
        ...analyticsEntity,
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
          <h2 id="subscriptionApp.analytics.home.createOrEditLabel">Create or edit a Analytics</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : analyticsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="analytics-id">ID</Label>
                  <AvInput id="analytics-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="analytics-shop">
                  Shop
                </Label>
                <AvField
                  id="analytics-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="totalSubscriptionsLabel" for="analytics-totalSubscriptions">
                  Total Subscriptions
                </Label>
                <AvField id="analytics-totalSubscriptions" type="string" className="form-control" name="totalSubscriptions" />
              </AvGroup>
              <AvGroup>
                <Label id="totalOrdersLabel" for="analytics-totalOrders">
                  Total Orders
                </Label>
                <AvField id="analytics-totalOrders" type="string" className="form-control" name="totalOrders" />
              </AvGroup>
              <AvGroup>
                <Label id="totalOrderAmountLabel" for="analytics-totalOrderAmount">
                  Total Order Amount
                </Label>
                <AvField id="analytics-totalOrderAmount" type="string" className="form-control" name="totalOrderAmount" />
              </AvGroup>
              <AvGroup>
                <Label id="firstTimeOrdersLabel" for="analytics-firstTimeOrders">
                  First Time Orders
                </Label>
                <AvField id="analytics-firstTimeOrders" type="string" className="form-control" name="firstTimeOrders" />
              </AvGroup>
              <AvGroup>
                <Label id="recurringOrdersLabel" for="analytics-recurringOrders">
                  Recurring Orders
                </Label>
                <AvField id="analytics-recurringOrders" type="string" className="form-control" name="recurringOrders" />
              </AvGroup>
              <AvGroup>
                <Label id="totalCustomersLabel" for="analytics-totalCustomers">
                  Total Customers
                </Label>
                <AvField id="analytics-totalCustomers" type="string" className="form-control" name="totalCustomers" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/analytics" replace color="info">
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
  analyticsEntity: storeState.analytics.entity,
  loading: storeState.analytics.loading,
  updating: storeState.analytics.updating,
  updateSuccess: storeState.analytics.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnalyticsUpdate);
