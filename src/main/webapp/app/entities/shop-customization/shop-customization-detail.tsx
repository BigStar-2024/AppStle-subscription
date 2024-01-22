import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop-customization.reducer';
import { IShopCustomization } from 'app/shared/model/shop-customization.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopCustomizationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopCustomizationDetail = (props: IShopCustomizationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shopCustomizationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ShopCustomization [<b>{shopCustomizationEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{shopCustomizationEntity.shop}</dd>
          <dt>
            <span id="labelId">Label Id</span>
          </dt>
          <dd>{shopCustomizationEntity.labelId}</dd>
          <dt>
            <span id="value">Value</span>
          </dt>
          <dd>{shopCustomizationEntity.value}</dd>
        </dl>
        <Button tag={Link} to="/admin/shop-customization" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/shop-customization/${shopCustomizationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shopCustomization }: IRootState) => ({
  shopCustomizationEntity: shopCustomization.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopCustomizationDetail);
