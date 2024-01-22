import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './dunning-management.reducer';
import { IDunningManagement } from 'app/shared/model/dunning-management.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDunningManagementDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DunningManagementDetail = (props: IDunningManagementDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { dunningManagementEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          DunningManagement [<b>{dunningManagementEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{dunningManagementEntity.shop}</dd>
          <dt>
            <span id="retryAttempts">Retry Attempts</span>
          </dt>
          <dd>{dunningManagementEntity.retryAttempts}</dd>
          <dt>
            <span id="daysBeforeRetrying">Days Before Retrying</span>
          </dt>
          <dd>{dunningManagementEntity.daysBeforeRetrying}</dd>
          <dt>
            <span id="maxNumberOfFailures">Max Number Of Failures</span>
          </dt>
          <dd>{dunningManagementEntity.maxNumberOfFailures}</dd>
        </dl>
        <Button tag={Link} to="/dunning-management" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dunning-management/${dunningManagementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ dunningManagement }: IRootState) => ({
  dunningManagementEntity: dunningManagement.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DunningManagementDetail);
