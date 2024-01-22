import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './membership-discount.reducer';
import { IMembershipDiscount } from 'app/shared/model/membership-discount.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMembershipDiscountDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MembershipDiscountDetail = (props: IMembershipDiscountDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { membershipDiscountEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          MembershipDiscount [<b>{membershipDiscountEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{membershipDiscountEntity.shop}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{membershipDiscountEntity.title}</dd>
          <dt>
            <span id="discount">Discount</span>
          </dt>
          <dd>{membershipDiscountEntity.discount}</dd>
          <dt>
            <span id="customerTags">Customer Tags</span>
          </dt>
          <dd>{membershipDiscountEntity.customerTags}</dd>
        </dl>
        <Button tag={Link} to="/membership-discount" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/membership-discount/${membershipDiscountEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ membershipDiscount }: IRootState) => ({
  membershipDiscountEntity: membershipDiscount.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscountDetail);
