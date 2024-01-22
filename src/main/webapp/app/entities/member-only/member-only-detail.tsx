import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './member-only.reducer';
import { IMemberOnly } from 'app/shared/model/member-only.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMemberOnlyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemberOnlyDetail = (props: IMemberOnlyDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { memberOnlyEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          MemberOnly [<b>{memberOnlyEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{memberOnlyEntity.shop}</dd>
          <dt>
            <span id="sellingPlanId">Selling Plan Id</span>
          </dt>
          <dd>{memberOnlyEntity.sellingPlanId}</dd>
          <dt>
            <span id="tags">Tags</span>
          </dt>
          <dd>{memberOnlyEntity.tags}</dd>
        </dl>
        <Button tag={Link} to="/member-only" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/member-only/${memberOnlyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ memberOnly }: IRootState) => ({
  memberOnlyEntity: memberOnly.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemberOnlyDetail);
