import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './social-connection.reducer';
import { ISocialConnection } from 'app/shared/model/social-connection.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISocialConnectionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SocialConnectionDetail = (props: ISocialConnectionDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { socialConnectionEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SocialConnection [<b>{socialConnectionEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="userId">User Id</span>
          </dt>
          <dd>{socialConnectionEntity.userId}</dd>
          <dt>
            <span id="proverId">Prover Id</span>
          </dt>
          <dd>{socialConnectionEntity.proverId}</dd>
          <dt>
            <span id="accessToken">Access Token</span>
          </dt>
          <dd>{socialConnectionEntity.accessToken}</dd>
          <dt>
            <span id="restRateLimit">Rest Rate Limit</span>
          </dt>
          <dd>{socialConnectionEntity.restRateLimit}</dd>
          <dt>
            <span id="graphqlRateLimit">Graphql Rate Limit</span>
          </dt>
          <dd>{socialConnectionEntity.graphqlRateLimit}</dd>
        </dl>
        <Button tag={Link} to="/social-connection" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/social-connection/${socialConnectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ socialConnection }: IRootState) => ({
  socialConnectionEntity: socialConnection.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SocialConnectionDetail);
