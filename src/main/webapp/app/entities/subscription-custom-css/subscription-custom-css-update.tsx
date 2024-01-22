import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './subscription-custom-css.reducer';
import { ISubscriptionCustomCss } from 'app/shared/model/subscription-custom-css.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionCustomCssUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionCustomCssUpdate = (props: ISubscriptionCustomCssUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionCustomCssEntity, loading, updating } = props;

  const { customCss, customerPoratlCSS, bundlingCSS, bundlingIframeCSS } = subscriptionCustomCssEntity;

  const handleClose = () => {
    props.history.push('/subscription-custom-css');
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
        ...subscriptionCustomCssEntity,
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
          <h2 id="subscriptionApp.subscriptionCustomCss.home.createOrEditLabel">Create or edit a SubscriptionCustomCss</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionCustomCssEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-custom-css-id">ID</Label>
                  <AvInput id="subscription-custom-css-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="shopLabel" for="subscription-custom-css-shop">
                  Shop
                </Label>
                <AvField
                  id="subscription-custom-css-shop"
                  type="text"
                  name="shop"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="customCssLabel" for="subscription-custom-css-customCss">
                  Custom Css
                </Label>
                <AvInput id="subscription-custom-css-customCss" type="textarea" name="customCss" />
              </AvGroup>
              <AvGroup>
                <Label id="customerPoratlCSSLabel" for="subscription-custom-css-customerPoratlCSS">
                  Customer Poratl CSS
                </Label>
                <AvInput id="subscription-custom-css-customerPoratlCSS" type="textarea" name="customerPoratlCSS" />
              </AvGroup>
              <AvGroup>
                <Label id="bundlingCSSLabel" for="subscription-custom-css-bundlingCSS">
                  Bundling CSS
                </Label>
                <AvInput id="subscription-custom-css-bundlingCSS" type="textarea" name="bundlingCSS" />
              </AvGroup>
              <AvGroup>
                <Label id="bundlingIframeCSSLabel" for="subscription-custom-css-bundlingIframeCSS">
                  Bundling Iframe CSS
                </Label>
                <AvInput id="subscription-custom-css-bundlingIframeCSS" type="textarea" name="bundlingIframeCSS" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-custom-css" replace color="info">
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
  subscriptionCustomCssEntity: storeState.subscriptionCustomCss.entity,
  loading: storeState.subscriptionCustomCss.loading,
  updating: storeState.subscriptionCustomCss.updating,
  updateSuccess: storeState.subscriptionCustomCss.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionCustomCssUpdate);
