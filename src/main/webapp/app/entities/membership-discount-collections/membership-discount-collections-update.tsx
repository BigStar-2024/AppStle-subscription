import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './membership-discount-collections.reducer';
import { IMembershipDiscountCollections } from 'app/shared/model/membership-discount-collections.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMembershipDiscountCollectionsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MembershipDiscountCollectionsUpdate = (props: IMembershipDiscountCollectionsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { membershipDiscountCollectionsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/membership-discount-collections');
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
        ...membershipDiscountCollectionsEntity,
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
          <h2 id="subscriptionApp.membershipDiscountCollections.home.createOrEditLabel">Create or edit a MembershipDiscountCollections</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : membershipDiscountCollectionsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="membership-discount-collections-id">ID</Label>
                  <AvInput id="membership-discount-collections-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="membership-discount-collections-shop">
                  Shop
                </Label>
                <AvField
                  id="membership-discount-collections-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="membershipDiscountIdLabel" for="membership-discount-collections-membershipDiscountId">
                  Membership Discount Id
                </Label>
                <AvField
                  id="membership-discount-collections-membershipDiscountId"
                  type="string"
                  className="form-control"
                  name="membershipDiscountId"
                />
              </AvGroup>
              <AvGroup>
                <Label id="collectionIdLabel" for="membership-discount-collections-collectionId">
                  Collection Id
                </Label>
                <AvField id="membership-discount-collections-collectionId" type="string" className="form-control" name="collectionId" />
              </AvGroup>
              <AvGroup>
                <Label id="collectionTitleLabel" for="membership-discount-collections-collectionTitle">
                  Collection Title
                </Label>
                <AvField id="membership-discount-collections-collectionTitle" type="text" name="collectionTitle" />
              </AvGroup>
              <AvGroup>
                <Label id="collectionTypeLabel" for="membership-discount-collections-collectionType">
                  Collection Type
                </Label>
                <AvInput
                  id="membership-discount-collections-collectionType"
                  type="select"
                  className="form-control"
                  name="collectionType"
                  value={(!isNew && membershipDiscountCollectionsEntity.collectionType) || 'SMART_COLLECTION'}
                >
                  <option value="SMART_COLLECTION">SMART_COLLECTION</option>
                  <option value="CUSTOM_COLLECTION">CUSTOM_COLLECTION</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/membership-discount-collections" replace color="info">
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
  membershipDiscountCollectionsEntity: storeState.membershipDiscountCollections.entity,
  loading: storeState.membershipDiscountCollections.loading,
  updating: storeState.membershipDiscountCollections.updating,
  updateSuccess: storeState.membershipDiscountCollections.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscountCollectionsUpdate);
