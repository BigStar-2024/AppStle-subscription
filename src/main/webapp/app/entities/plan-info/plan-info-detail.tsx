import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './plan-info.reducer';
import { IPlanInfo } from 'app/shared/model/plan-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlanInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PlanInfoDetail = (props: IPlanInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { planInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          PlanInfo [<b>{planInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{planInfoEntity.name}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{planInfoEntity.price}</dd>
          <dt>
            <span id="archived">Archived</span>
          </dt>
          <dd>{planInfoEntity.archived ? 'true' : 'false'}</dd>
          <dt>
            <span id="planType">Plan Type</span>
          </dt>
          <dd>{planInfoEntity.planType}</dd>
          <dt>
            <span id="billingType">Billing Type</span>
          </dt>
          <dd>{planInfoEntity.billingType}</dd>
          <dt>
            <span id="basedOn">Based On</span>
          </dt>
          <dd>{planInfoEntity.basedOn}</dd>
          <dt>
            <span id="additionalDetails">Additional Details</span>
          </dt>
          <dd>{planInfoEntity.additionalDetails}</dd>
          <dt>
            <span id="features">Features</span>
          </dt>
          <dd>{planInfoEntity.features}</dd>
          <dt>
            <span id="trialDays">Trial Days</span>
          </dt>
          <dd>{planInfoEntity.trialDays}</dd>
          <dt>
            <span id="basePlan">Base Plan</span>
          </dt>
          <dd>{planInfoEntity.basePlan}</dd>
        </dl>
        <Button tag={Link} to="/admin/plan-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        {planInfoEntity.archived &&
          <Button tag={Link} to={`/admin/plan-info/${planInfoEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        }
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ planInfo }: IRootState) => ({
  planInfoEntity: planInfo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PlanInfoDetail);
