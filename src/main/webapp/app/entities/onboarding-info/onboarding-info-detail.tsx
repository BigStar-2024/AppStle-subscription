import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './onboarding-info.reducer';
import { IOnboardingInfo } from 'app/shared/model/onboarding-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOnboardingInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OnboardingInfoDetail = (props: IOnboardingInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { onboardingInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          OnboardingInfo [<b>{onboardingInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{onboardingInfoEntity.shop}</dd>
          <dt>
            <span id="uncompletedChecklistSteps">Uncompleted Checklist Steps</span>
          </dt>
          <dd>{onboardingInfoEntity.uncompletedChecklistSteps}</dd>
          <dt>
            <span id="completedChecklistSteps">Completed Checklist Steps</span>
          </dt>
          <dd>{onboardingInfoEntity.completedChecklistSteps}</dd>
          <dt>
            <span id="checklistCompleted">Checklist Completed</span>
          </dt>
          <dd>{onboardingInfoEntity.checklistCompleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/onboarding-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/onboarding-info/${onboardingInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ onboardingInfo }: IRootState) => ({
  onboardingInfoEntity: onboardingInfo.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OnboardingInfoDetail);
