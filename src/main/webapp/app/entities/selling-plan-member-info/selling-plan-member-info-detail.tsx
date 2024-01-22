import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './selling-plan-member-info.reducer';
import { ISellingPlanMemberInfo } from 'app/shared/model/selling-plan-member-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISellingPlanMemberInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SellingPlanMemberInfoDetail = (props: ISellingPlanMemberInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { sellingPlanMemberInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SellingPlanMemberInfo [<b>{sellingPlanMemberInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{sellingPlanMemberInfoEntity.shop}</dd>
          <dt>
            <span id="subscriptionId">Subscription Id</span>
          </dt>
          <dd>{sellingPlanMemberInfoEntity.subscriptionId}</dd>
          <dt>
            <span id="sellingPlanId">Selling Plan Id</span>
          </dt>
          <dd>{sellingPlanMemberInfoEntity.sellingPlanId}</dd>
          <dt>
            <span id="enableMemberInclusiveTag">Enable Member Inclusive Tag</span>
          </dt>
          <dd>{sellingPlanMemberInfoEntity.enableMemberInclusiveTag ? 'true' : 'false'}</dd>
          <dt>
            <span id="memberInclusiveTags">Member Inclusive Tags</span>
          </dt>
          <dd>{sellingPlanMemberInfoEntity.memberInclusiveTags}</dd>
          <dt>
            <span id="enableMemberExclusiveTag">Enable Member Exclusive Tag</span>
          </dt>
          <dd>{sellingPlanMemberInfoEntity.enableMemberExclusiveTag ? 'true' : 'false'}</dd>
          <dt>
            <span id="memberExclusiveTags">Member Exclusive Tags</span>
          </dt>
          <dd>{sellingPlanMemberInfoEntity.memberExclusiveTags}</dd>
        </dl>
        <Button tag={Link} to="/selling-plan-member-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/selling-plan-member-info/${sellingPlanMemberInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ sellingPlanMemberInfo }: IRootState) => ({
  sellingPlanMemberInfoEntity: sellingPlanMemberInfo.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellingPlanMemberInfoDetail);
