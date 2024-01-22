import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop-settings.reducer';
import { IShopSettings } from 'app/shared/model/shop-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopSettingsDetail = (props: IShopSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shopSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ShopSettings [<b>{shopSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{shopSettingsEntity.shop}</dd>
          <dt>
            <span id="taggingEnabled">Tagging Enabled</span>
          </dt>
          <dd>{shopSettingsEntity.taggingEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="delayTagging">Delay Tagging</span>
          </dt>
          <dd>{shopSettingsEntity.delayTagging ? 'true' : 'false'}</dd>
          <dt>
            <span id="delayedTaggingValue">Delayed Tagging Value</span>
          </dt>
          <dd>{shopSettingsEntity.delayedTaggingValue}</dd>
          <dt>
            <span id="delayedTaggingUnit">Delayed Tagging Unit</span>
          </dt>
          <dd>{shopSettingsEntity.delayedTaggingUnit}</dd>
          <dt>
            <span id="orderStatus">Order Status</span>
          </dt>
          <dd>{shopSettingsEntity.orderStatus}</dd>
          <dt>
            <span id="paymentStatus">Payment Status</span>
          </dt>
          <dd>{shopSettingsEntity.paymentStatus}</dd>
          <dt>
            <span id="fulfillmentStatus">Fulfillment Status</span>
          </dt>
          <dd>{shopSettingsEntity.fulfillmentStatus}</dd>
          <dt>
            <span id="priceSyncEnabled">Price Sync Enabled</span>
          </dt>
          <dd>{shopSettingsEntity.priceSyncEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="skuSyncEnabled">Sku Sync Enabled</span>
          </dt>
          <dd>{shopSettingsEntity.skuSyncEnabled ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/shop-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shop-settings/${shopSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shopSettings }: IRootState) => ({
  shopSettingsEntity: shopSettings.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopSettingsDetail);
