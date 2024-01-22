import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './rule-criteria.reducer';
import { IRuleCriteria } from 'app/shared/model/rule-criteria.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRuleCriteriaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RuleCriteriaUpdate = (props: IRuleCriteriaUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { ruleCriteriaEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/rule-criteria');
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
        ...ruleCriteriaEntity,
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
          <h2 id="subscriptionApp.ruleCriteria.home.createOrEditLabel">Create or edit a RuleCriteria</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : ruleCriteriaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="rule-criteria-id">ID</Label>
                  <AvInput id="rule-criteria-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="rule-criteria-shop">
                  Shop
                </Label>
                <AvField
                  id="rule-criteria-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="rule-criteria-name">
                  Name
                </Label>
                <AvField id="rule-criteria-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="identifierLabel" for="rule-criteria-identifier">
                  Identifier
                </Label>
                <AvField id="rule-criteria-identifier" type="text" name="identifier" />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="rule-criteria-type">
                  Type
                </Label>
                <AvField id="rule-criteria-type" type="text" name="type" />
              </AvGroup>
              <AvGroup>
                <Label id="groupLabel" for="rule-criteria-group">
                  Group
                </Label>
                <AvField id="rule-criteria-group" type="text" name="group" />
              </AvGroup>
              <AvGroup>
                <Label id="fieldsLabel" for="rule-criteria-fields">
                  Fields
                </Label>
                <AvField id="rule-criteria-fields" type="text" name="fields" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/rule-criteria" replace color="info">
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
  ruleCriteriaEntity: storeState.ruleCriteria.entity,
  loading: storeState.ruleCriteria.loading,
  updating: storeState.ruleCriteria.updating,
  updateSuccess: storeState.ruleCriteria.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RuleCriteriaUpdate);
