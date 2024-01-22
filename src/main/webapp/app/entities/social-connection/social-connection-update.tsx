import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './social-connection.reducer';
import { ISocialConnection } from 'app/shared/model/social-connection.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISocialConnectionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SocialConnectionUpdate = (props: ISocialConnectionUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { socialConnectionEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/admin/social-connection' + props.location.search);
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
        ...socialConnectionEntity,
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
          <h2 id="subscriptionApp.socialConnection.home.createOrEditLabel">Create or edit a SocialConnection</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : socialConnectionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="social-connection-id">ID</Label>
                  <AvInput id="social-connection-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="userIdLabel" for="social-connection-userId">
                  User Id
                </Label>
                <AvField
                  id="social-connection-userId"
                  type="text"
                  name="userId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="proverIdLabel" for="social-connection-proverId">
                  Prover Id
                </Label>
                <AvField
                  id="social-connection-proverId"
                  type="text"
                  name="proverId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="accessTokenLabel" for="social-connection-accessToken">
                  Access Token
                </Label>
                <AvField
                  id="social-connection-accessToken"
                  type="text"
                  name="accessToken"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="restRateLimitLabel" for="social-connection-restRateLimit">
                  Rest Rate Limit
                </Label>
                <AvField id="social-connection-restRateLimit" type="string" className="form-control" name="restRateLimit" />
              </AvGroup>
              <AvGroup>
                <Label id="graphqlRateLimitLabel" for="social-connection-graphqlRateLimit">
                  Graphql Rate Limit
                </Label>
                <AvField id="social-connection-graphqlRateLimit" type="string" className="form-control" name="graphqlRateLimit" />
              </AvGroup>
              <AvGroup>
                <Label id="accessToken1Label" for="social-connection-accessToken1">
                  Access Token 1
                </Label>
                <AvField id="social-connection-accessToken1" type="text" name="accessToken1" />
              </AvGroup>
              <AvGroup>
                <Label id="accessToken2Label" for="social-connection-accessToken2">
                  Access Token 2
                </Label>
                <AvField id="social-connection-accessToken2" type="text" name="accessToken2" />
              </AvGroup>
              <AvGroup>
                <Label id="accessToken3Label" for="social-connection-accessToken3">
                  Access Token 3
                </Label>
                <AvField id="social-connection-accessToken3" type="text" name="accessToken3" />
              </AvGroup>
              <AvGroup>
                <Label id="accessToken4Label" for="social-connection-accessToken4">
                  Access Token 4
                </Label>
                <AvField id="social-connection-accessToken4" type="text" name="accessToken4" />
              </AvGroup>
              <AvGroup>
                <Label id="publicAccessTokenLabel" for="social-connection-publicAccessToken">
                  Public Access Token
                </Label>
                <AvField id="social-connection-publicAccessToken" type="text" name="publicAccessToken" />
              </AvGroup>
              <AvGroup>
                <Label id="publicAccessToken1Label" for="social-connection-publicAccessToken1">
                  Public Access Token 1
                </Label>
                <AvField id="social-connection-publicAccessToken1" type="text" name="publicAccessToken1" />
              </AvGroup>
              <AvGroup>
                <Label id="publicAccessToken2Label" for="social-connection-publicAccessToken2">
                  Public Access Token 2
                </Label>
                <AvField id="social-connection-publicAccessToken2" type="text" name="publicAccessToken2" />
              </AvGroup>
              <AvGroup>
                <Label id="publicAccessToken3Label" for="social-connection-publicAccessToken3">
                  Public Access Token 3
                </Label>
                <AvField id="social-connection-publicAccessToken3" type="text" name="publicAccessToken3" />
              </AvGroup>
              <AvGroup>
                <Label id="publicAccessToken4Label" for="social-connection-publicAccessToken4">
                  Public Access Token 4
                </Label>
                <AvField id="social-connection-publicAccessToken4" type="text" name="publicAccessToken4" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/social-connection" replace color="info">
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
  socialConnectionEntity: storeState.socialConnection.entity,
  loading: storeState.socialConnection.loading,
  updating: storeState.socialConnection.updating,
  updateSuccess: storeState.socialConnection.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SocialConnectionUpdate);
