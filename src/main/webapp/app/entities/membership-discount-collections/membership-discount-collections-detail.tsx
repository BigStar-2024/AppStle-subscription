import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './membership-discount-collections.reducer';
import { IMembershipDiscountCollections } from 'app/shared/model/membership-discount-collections.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMembershipDiscountCollectionsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MembershipDiscountCollectionsDetail = (props: IMembershipDiscountCollectionsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { membershipDiscountCollectionsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          MembershipDiscountCollections [<b>{membershipDiscountCollectionsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{membershipDiscountCollectionsEntity.shop}</dd>
          <dt>
            <span id="membershipDiscountId">Membership Discount Id</span>
          </dt>
          <dd>{membershipDiscountCollectionsEntity.membershipDiscountId}</dd>
          <dt>
            <span id="collectionId">Collection Id</span>
          </dt>
          <dd>{membershipDiscountCollectionsEntity.collectionId}</dd>
          <dt>
            <span id="collectionTitle">Collection Title</span>
          </dt>
          <dd>{membershipDiscountCollectionsEntity.collectionTitle}</dd>
          <dt>
            <span id="collectionType">Collection Type</span>
          </dt>
          <dd>{membershipDiscountCollectionsEntity.collectionType}</dd>
        </dl>
        <Button tag={Link} to="/membership-discount-collections" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/membership-discount-collections/${membershipDiscountCollectionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ membershipDiscountCollections }: IRootState) => ({
  membershipDiscountCollectionsEntity: membershipDiscountCollections.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscountCollectionsDetail);
