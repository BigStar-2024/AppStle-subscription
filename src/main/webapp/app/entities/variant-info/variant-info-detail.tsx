import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './variant-info.reducer';
import { IVariantInfo } from 'app/shared/model/variant-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVariantInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VariantInfoDetail = (props: IVariantInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { variantInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          VariantInfo [<b>{variantInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{variantInfoEntity.shop}</dd>
          <dt>
            <span id="productId">Product Id</span>
          </dt>
          <dd>{variantInfoEntity.productId}</dd>
          <dt>
            <span id="variantId">Variant Id</span>
          </dt>
          <dd>{variantInfoEntity.variantId}</dd>
          <dt>
            <span id="productTitle">Product Title</span>
          </dt>
          <dd>{variantInfoEntity.productTitle}</dd>
          <dt>
            <span id="variantTitle">Variant Title</span>
          </dt>
          <dd>{variantInfoEntity.variantTitle}</dd>
          <dt>
            <span id="sku">Sku</span>
          </dt>
          <dd>{variantInfoEntity.sku}</dd>
          <dt>
            <span id="variantPrice">Variant Price</span>
          </dt>
          <dd>{variantInfoEntity.variantPrice}</dd>
        </dl>
        <Button tag={Link} to="/variant-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/variant-info/${variantInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ variantInfo }: IRootState) => ({
  variantInfoEntity: variantInfo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VariantInfoDetail);
