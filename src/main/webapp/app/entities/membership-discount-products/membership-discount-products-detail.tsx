import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './membership-discount-products.reducer';
import { IMembershipDiscountProducts } from 'app/shared/model/membership-discount-products.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMembershipDiscountProductsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MembershipDiscountProductsDetail = (props: IMembershipDiscountProductsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { membershipDiscountProductsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          MembershipDiscountProducts [<b>{membershipDiscountProductsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{membershipDiscountProductsEntity.shop}</dd>
          <dt>
            <span id="membershipDiscountId">Membership Discount Id</span>
          </dt>
          <dd>{membershipDiscountProductsEntity.membershipDiscountId}</dd>
          <dt>
            <span id="productId">Product Id</span>
          </dt>
          <dd>{membershipDiscountProductsEntity.productId}</dd>
          <dt>
            <span id="productTitle">Product Title</span>
          </dt>
          <dd>{membershipDiscountProductsEntity.productTitle}</dd>
        </dl>
        <Button tag={Link} to="/membership-discount-products" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/membership-discount-products/${membershipDiscountProductsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ membershipDiscountProducts }: IRootState) => ({
  membershipDiscountProductsEntity: membershipDiscountProducts.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscountProductsDetail);
