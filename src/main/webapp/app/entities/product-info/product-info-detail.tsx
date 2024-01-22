import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-info.reducer';
import { IProductInfo } from 'app/shared/model/product-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductInfoDetail = (props: IProductInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ProductInfo [<b>{productInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{productInfoEntity.shop}</dd>
          <dt>
            <span id="productId">Product Id</span>
          </dt>
          <dd>{productInfoEntity.productId}</dd>
          <dt>
            <span id="productTitle">Product Title</span>
          </dt>
          <dd>{productInfoEntity.productTitle}</dd>
          <dt>
            <span id="productHandle">Product Handle</span>
          </dt>
          <dd>{productInfoEntity.productHandle}</dd>
        </dl>
        <Button tag={Link} to="/product-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-info/${productInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productInfo }: IRootState) => ({
  productInfoEntity: productInfo.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductInfoDetail);
