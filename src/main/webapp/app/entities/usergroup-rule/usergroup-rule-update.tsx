import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './usergroup-rule.reducer';
import { IUsergroupRule } from 'app/shared/model/usergroup-rule.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUsergroupRuleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UsergroupRuleUpdate = (props: IUsergroupRuleUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { usergroupRuleEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/usergroup-rule');
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
        ...usergroupRuleEntity,
        ...values
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
          <h2 id="subscriptionApp.usergroupRule.home.createOrEditLabel">Create or edit a UsergroupRule</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : usergroupRuleEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="usergroup-rule-id">ID</Label>
                  <AvInput id="usergroup-rule-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="usergroup-rule-shop">
                  Shop
                </Label>
                <AvField id="usergroup-rule-shop" type="text" name="shop" />
              </AvGroup>
              <AvGroup>
                <Label id="forCustomersWithTagsLabel" for="usergroup-rule-forCustomersWithTags">
                  For Customers With Tags
                </Label>
                <AvField id="usergroup-rule-forCustomersWithTags" type="text" name="forCustomersWithTags" />
              </AvGroup>
              <AvGroup>
                <Label id="forProductsWithTagsLabel" for="usergroup-rule-forProductsWithTags">
                  For Products With Tags
                </Label>
                <AvField id="usergroup-rule-forProductsWithTags" type="text" name="forProductsWithTags" />
              </AvGroup>
              <AvGroup>
                <Label id="actionLabel" for="usergroup-rule-action">
                  Action
                </Label>
                <AvInput
                  id="usergroup-rule-action"
                  type="select"
                  className="form-control"
                  name="action"
                  value={(!isNew && usergroupRuleEntity.action) || 'SHOW_PRODUCTS'}
                >
                  <option value="SHOW_PRODUCTS">SHOW_PRODUCTS</option>
                  <option value="HIDE_PRODUCTS">HIDE_PRODUCTS</option>
                  <option value="SHOW_PRICES">SHOW_PRICES</option>
                  <option value="HIDE_PRICES">HIDE_PRICES</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/usergroup-rule" replace color="info">
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
  usergroupRuleEntity: storeState.usergroupRule.entity,
  loading: storeState.usergroupRule.loading,
  updating: storeState.usergroupRule.updating,
  updateSuccess: storeState.usergroupRule.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UsergroupRuleUpdate);
