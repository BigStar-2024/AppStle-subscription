import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './appstle-menu-labels.reducer';
import { IAppstleMenuLabels } from 'app/shared/model/appstle-menu-labels.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAppstleMenuLabelsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AppstleMenuLabelsUpdate = (props: IAppstleMenuLabelsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { appstleMenuLabelsEntity, loading, updating } = props;

  const { customCss, labels } = appstleMenuLabelsEntity;

  const handleClose = () => {
    props.history.push('/appstle-menu-labels' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...appstleMenuLabelsEntity,
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
          <h2 id="subscriptionApp.appstleMenuLabels.home.createOrEditLabel">Create or edit a AppstleMenuLabels</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : appstleMenuLabelsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="appstle-menu-labels-id">ID</Label>
                  <AvInput id="appstle-menu-labels-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="appstle-menu-labels-shop">
                  Shop
                </Label>
                <AvField
                  id="appstle-menu-labels-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="customCssLabel" for="appstle-menu-labels-customCss">
                  Custom Css
                </Label>
                <AvInput id="appstle-menu-labels-customCss" type="textarea" name="customCss" />
              </AvGroup>
              <AvGroup>
                <Label id="labelsLabel" for="appstle-menu-labels-labels">
                  Labels
                </Label>
                <AvInput id="appstle-menu-labels-labels" type="textarea" name="labels" />
              </AvGroup>
              <AvGroup>
                <Label id="seeMoreLabel" for="appstle-menu-labels-seeMore">
                  See More
                </Label>
                <AvField id="appstle-menu-labels-seeMore" type="text" name="seeMore" />
              </AvGroup>
              <AvGroup>
                <Label id="noDataFoundLabel" for="appstle-menu-labels-noDataFound">
                  No Data Found
                </Label>
                <AvField id="appstle-menu-labels-noDataFound" type="text" name="noDataFound" />
              </AvGroup>
              <AvGroup>
                <Label id="productDetailsLabel" for="appstle-menu-labels-productDetails">
                  Product Details
                </Label>
                <AvField id="appstle-menu-labels-productDetails" type="text" name="productDetails" />
              </AvGroup>
              <AvGroup>
                <Label id="editQuantityLabel" for="appstle-menu-labels-editQuantity">
                  Edit Quantity
                </Label>
                <AvField id="appstle-menu-labels-editQuantity" type="text" name="editQuantity" />
              </AvGroup>
              <AvGroup>
                <Label id="addToCartLabel" for="appstle-menu-labels-addToCart">
                  Add To Cart
                </Label>
                <AvField id="appstle-menu-labels-addToCart" type="text" name="addToCart" />
              </AvGroup>
              <AvGroup>
                <Label id="productAddedSuccessfullyLabel" for="appstle-menu-labels-productAddedSuccessfully">
                  Product Added Successfully
                </Label>
                <AvField id="appstle-menu-labels-productAddedSuccessfully" type="text" name="productAddedSuccessfully" />
              </AvGroup>
              <AvGroup>
                <Label id="wentWrongLabel" for="appstle-menu-labels-wentWrong">
                  Went Wrong
                </Label>
                <AvField id="appstle-menu-labels-wentWrong" type="text" name="wentWrong" />
              </AvGroup>
              <AvGroup>
                <Label id="resultsLabel" for="appstle-menu-labels-results">
                  Results
                </Label>
                <AvField id="appstle-menu-labels-results" type="text" name="results" />
              </AvGroup>
              <AvGroup>
                <Label id="addingLabel" for="appstle-menu-labels-adding">
                  Adding
                </Label>
                <AvField id="appstle-menu-labels-adding" type="text" name="adding" />
              </AvGroup>
              <AvGroup>
                <Label id="subscribeLabel" for="appstle-menu-labels-subscribe">
                  Subscribe
                </Label>
                <AvField id="appstle-menu-labels-subscribe" type="text" name="subscribe" />
              </AvGroup>
              <AvGroup>
                <Label id="notAvailableLabel" for="appstle-menu-labels-notAvailable">
                  Not Available
                </Label>
                <AvField id="appstle-menu-labels-notAvailable" type="text" name="notAvailable" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/appstle-menu-labels" replace color="info">
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
  appstleMenuLabelsEntity: storeState.appstleMenuLabels.entity,
  loading: storeState.appstleMenuLabels.loading,
  updating: storeState.appstleMenuLabels.updating,
  updateSuccess: storeState.appstleMenuLabels.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AppstleMenuLabelsUpdate);
