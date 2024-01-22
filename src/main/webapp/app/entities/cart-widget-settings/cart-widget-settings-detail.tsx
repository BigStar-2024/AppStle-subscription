import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './cart-widget-settings.reducer';
import { ICartWidgetSettings } from 'app/shared/model/cart-widget-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICartWidgetSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CartWidgetSettingsDetail = (props: ICartWidgetSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { cartWidgetSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CartWidgetSettings [<b>{cartWidgetSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.shop}</dd>
          <dt>
            <span id="enable_cart_widget_settings">Enable Cart Widget Settings</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.enable_cart_widget_settings ? 'true' : 'false'}</dd>
          <dt>
            <span id="cartWidgetSettingApproach">Cart Widget Setting Approach</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.cartWidgetSettingApproach}</dd>
          <dt>
            <span id="cartRowSelector">Cart Row Selector</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.cartRowSelector}</dd>
          <dt>
            <span id="cartRowPlacement">Cart Row Placement</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.cartRowPlacement}</dd>
          <dt>
            <span id="cartLineItemSelector">Cart Line Item Selector</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.cartLineItemSelector}</dd>
          <dt>
            <span id="cartLineItemPlacement">Cart Line Item Placement</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.cartLineItemPlacement}</dd>
          <dt>
            <span id="cartFormSelector">Cart Form Selector</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.cartFormSelector}</dd>
          <dt>
            <span id="appstelCustomeSelector">Appstel Custome Selector</span>
          </dt>
          <dd>{cartWidgetSettingsEntity.appstelCustomeSelector}</dd>
        </dl>
        <Button tag={Link} to="/cart-widget-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cart-widget-settings/${cartWidgetSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ cartWidgetSettings }: IRootState) => ({
  cartWidgetSettingsEntity: cartWidgetSettings.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CartWidgetSettingsDetail);
