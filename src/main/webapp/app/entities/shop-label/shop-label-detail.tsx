import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop-label.reducer';
import { IShopLabel } from 'app/shared/model/shop-label.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopLabelDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopLabelDetail = (props: IShopLabelDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shopLabelEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ShopLabel [<b>{shopLabelEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{shopLabelEntity.shop}</dd>
          <dt>
            <span id="labels">Labels</span>
          </dt>
          <dd>{shopLabelEntity.labels}</dd>
        </dl>
        <Button tag={Link} to="/shop-label" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shop-label/${shopLabelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shopLabel }: IRootState) => ({
  shopLabelEntity: shopLabel.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopLabelDetail);
