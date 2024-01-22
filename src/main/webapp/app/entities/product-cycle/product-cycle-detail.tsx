import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-cycle.reducer';
import { IProductCycle } from 'app/shared/model/product-cycle.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductCycleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductCycleDetail = (props: IProductCycleDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productCycleEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ProductCycle [<b>{productCycleEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{productCycleEntity.shop}</dd>
          <dt>
            <span id="sourceProduct">Source Product</span>
          </dt>
          <dd>{productCycleEntity.sourceProduct}</dd>
          <dt>
            <span id="destinationProducts">Destination Products</span>
          </dt>
          <dd>{productCycleEntity.destinationProducts}</dd>
        </dl>
        <Button tag={Link} to="/product-cycle" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-cycle/${productCycleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productCycle }: IRootState) => ({
  productCycleEntity: productCycle.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductCycleDetail);
