import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './delivery-profile.reducer';
import { IDeliveryProfile } from 'app/shared/model/delivery-profile.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDeliveryProfileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DeliveryProfileDetail = (props: IDeliveryProfileDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { deliveryProfileEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          DeliveryProfile [<b>{deliveryProfileEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{deliveryProfileEntity.shop}</dd>
          <dt>
            <span id="deliveryProfileId">Delivery Profile Id</span>
          </dt>
          <dd>{deliveryProfileEntity.deliveryProfileId}</dd>
        </dl>
        <Button tag={Link} to="/delivery-profile" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/delivery-profile/${deliveryProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ deliveryProfile }: IRootState) => ({
  deliveryProfileEntity: deliveryProfile.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DeliveryProfileDetail);
