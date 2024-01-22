import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop-asset-urls.reducer';
import { IShopAssetUrls } from 'app/shared/model/shop-asset-urls.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopAssetUrlsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShopAssetUrlsDetail = (props: IShopAssetUrlsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shopAssetUrlsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ShopAssetUrls [<b>{shopAssetUrlsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{shopAssetUrlsEntity.shop}</dd>
          <dt>
            <span id="vendorJavascript">Vendor Javascript</span>
          </dt>
          <dd>{shopAssetUrlsEntity.vendorJavascript}</dd>
          <dt>
            <span id="vendorCss">Vendor Css</span>
          </dt>
          <dd>{shopAssetUrlsEntity.vendorCss}</dd>
          <dt>
            <span id="customerJavascript">Customer Javascript</span>
          </dt>
          <dd>{shopAssetUrlsEntity.customerJavascript}</dd>
          <dt>
            <span id="customerCss">Customer Css</span>
          </dt>
          <dd>{shopAssetUrlsEntity.customerCss}</dd>
          <dt>
            <span id="bundleJavascript">Bundle Javascript</span>
          </dt>
          <dd>{shopAssetUrlsEntity.bundleJavascript}</dd>
          <dt>
            <span id="bundleCss">Bundle Css</span>
          </dt>
          <dd>{shopAssetUrlsEntity.bundleCss}</dd>
        </dl>
        <Button tag={Link} to="/shop-asset-urls" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shop-asset-urls/${shopAssetUrlsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shopAssetUrls }: IRootState) => ({
  shopAssetUrlsEntity: shopAssetUrls.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShopAssetUrlsDetail);
