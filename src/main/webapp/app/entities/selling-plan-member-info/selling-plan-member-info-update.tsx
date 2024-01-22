import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './selling-plan-member-info.reducer';
import { ISellingPlanMemberInfo } from 'app/shared/model/selling-plan-member-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISellingPlanMemberInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SellingPlanMemberInfoUpdate = (props: ISellingPlanMemberInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { sellingPlanMemberInfoEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/selling-plan-member-info');
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
        ...sellingPlanMemberInfoEntity,
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
          <h2 id="subscriptionApp.sellingPlanMemberInfo.home.createOrEditLabel">Create or edit a SellingPlanMemberInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : sellingPlanMemberInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="selling-plan-member-info-id">ID</Label>
                  <AvInput id="selling-plan-member-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="selling-plan-member-info-shop">
                  Shop
                </Label>
                <AvField
                  id="selling-plan-member-info-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="subscriptionIdLabel" for="selling-plan-member-info-subscriptionId">
                  Subscription Id
                </Label>
                <AvField id="selling-plan-member-info-subscriptionId" type="string" className="form-control" name="subscriptionId" />
              </AvGroup>
              <AvGroup>
                <Label id="sellingPlanIdLabel" for="selling-plan-member-info-sellingPlanId">
                  Selling Plan Id
                </Label>
                <AvField id="selling-plan-member-info-sellingPlanId" type="string" className="form-control" name="sellingPlanId" />
              </AvGroup>
              <AvGroup check>
                <Label id="enableMemberInclusiveTagLabel">
                  <AvInput
                    id="selling-plan-member-info-enableMemberInclusiveTag"
                    type="checkbox"
                    className="form-check-input"
                    name="enableMemberInclusiveTag"
                  />
                  Enable Member Inclusive Tag
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="memberInclusiveTagsLabel" for="selling-plan-member-info-memberInclusiveTags">
                  Member Inclusive Tags
                </Label>
                <AvField id="selling-plan-member-info-memberInclusiveTags" type="text" name="memberInclusiveTags" />
              </AvGroup>
              <AvGroup check>
                <Label id="enableMemberExclusiveTagLabel">
                  <AvInput
                    id="selling-plan-member-info-enableMemberExclusiveTag"
                    type="checkbox"
                    className="form-check-input"
                    name="enableMemberExclusiveTag"
                  />
                  Enable Member Exclusive Tag
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="memberExclusiveTagsLabel" for="selling-plan-member-info-memberExclusiveTags">
                  Member Exclusive Tags
                </Label>
                <AvField id="selling-plan-member-info-memberExclusiveTags" type="text" name="memberExclusiveTags" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/selling-plan-member-info" replace color="info">
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
  sellingPlanMemberInfoEntity: storeState.sellingPlanMemberInfo.entity,
  loading: storeState.sellingPlanMemberInfo.loading,
  updating: storeState.sellingPlanMemberInfo.updating,
  updateSuccess: storeState.sellingPlanMemberInfo.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellingPlanMemberInfoUpdate);
