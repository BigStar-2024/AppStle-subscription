import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-swap.reducer';
import { IProductSwap } from 'app/shared/model/product-swap.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductSwapDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductSwapDetail = (props: IProductSwapDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productSwapEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ProductSwap [<b>{productSwapEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{productSwapEntity.shop}</dd>
          <dt>
            <span id="sourceVariants">Source Variants</span>
          </dt>
          <dd>{productSwapEntity.sourceVariants}</dd>
          <dt>
            <span id="destinationVariants">Destination Variants</span>
          </dt>
          <dd>{productSwapEntity.destinationVariants}</dd>
          <dt>
            <span id="updatedFirstOrder">Updated First Order</span>
          </dt>
          <dd>{productSwapEntity.updatedFirstOrder ? 'true' : 'false'}</dd>
          <dt>
            <span id="checkForEveryRecurringOrder">Check For Every Recurring Order</span>
          </dt>
          <dd>{productSwapEntity.checkForEveryRecurringOrder ? 'true' : 'false'}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{productSwapEntity.name}</dd>
          <dt>
            <span id="changeNextOrderDateBy">Change Next Order Date By</span>
          </dt>
          <dd>{productSwapEntity.changeNextOrderDateBy}</dd>
          <dt>
            <span id="forBillingCycle">For Billing Cycle</span>
          </dt>
          <dd>{productSwapEntity.forBillingCycle}</dd>
          <dt>
            <span id="carryDiscountForward">Carry Discount Forward</span>
          </dt>
          <dd>{productSwapEntity.carryDiscountForward ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/product-swap" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-swap/${productSwapEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productSwap }: IRootState) => ({
  productSwapEntity: productSwap.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductSwapDetail);
