import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './bundle-setting.reducer';
import { IBundleSetting } from 'app/shared/model/bundle-setting.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBundleSettingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BundleSettingDetail = (props: IBundleSettingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bundleSettingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          BundleSetting [<b>{bundleSettingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{bundleSettingEntity.shop}</dd>
          <dt>
            <span id="showOnProductPage">Show On Product Page</span>
          </dt>
          <dd>{bundleSettingEntity.showOnProductPage ? 'true' : 'false'}</dd>
          <dt>
            <span id="showMultipleOnProductPage">Show Multiple On Product Page</span>
          </dt>
          <dd>{bundleSettingEntity.showMultipleOnProductPage ? 'true' : 'false'}</dd>
          <dt>
            <span id="actionButtonColor">Action Button Color</span>
          </dt>
          <dd>{bundleSettingEntity.actionButtonColor}</dd>
          <dt>
            <span id="actionButtonFontColor">Action Button Font Color</span>
          </dt>
          <dd>{bundleSettingEntity.actionButtonFontColor}</dd>
          <dt>
            <span id="productTitleColor">Product Title Color</span>
          </dt>
          <dd>{bundleSettingEntity.productTitleColor}</dd>
          <dt>
            <span id="productPriceColor">Product Price Color</span>
          </dt>
          <dd>{bundleSettingEntity.productPriceColor}</dd>
          <dt>
            <span id="redirectTo">Redirect To</span>
          </dt>
          <dd>{bundleSettingEntity.redirectTo}</dd>
          <dt>
            <span id="showProductPrice">Show Product Price</span>
          </dt>
          <dd>{bundleSettingEntity.showProductPrice ? 'true' : 'false'}</dd>
          <dt>
            <span id="oneTimeDiscount">One Time Discount</span>
          </dt>
          <dd>{bundleSettingEntity.oneTimeDiscount ? 'true' : 'false'}</dd>
          <dt>
            <span id="showDiscountInCart">Show Discount In Cart</span>
          </dt>
          <dd>{bundleSettingEntity.showDiscountInCart ? 'true' : 'false'}</dd>
          <dt>
            <span id="selector">Selector</span>
          </dt>
          <dd>{bundleSettingEntity.selector}</dd>
          <dt>
            <span id="placement">Placement</span>
          </dt>
          <dd>{bundleSettingEntity.placement}</dd>
          <dt>
            <span id="customCss">Custom Css</span>
          </dt>
          <dd>{bundleSettingEntity.customCss}</dd>
          <dt>
            <span id="variant">Variant</span>
          </dt>
          <dd>{bundleSettingEntity.variant}</dd>
          <dt>
            <span id="deliveryFrequency">Delivery Frequency</span>
          </dt>
          <dd>{bundleSettingEntity.deliveryFrequency}</dd>
          <dt>
            <span id="perDelivery">Per Delivery</span>
          </dt>
          <dd>{bundleSettingEntity.perDelivery}</dd>
          <dt>
            <span id="discountPopupHeader">Discount Popup Header</span>
          </dt>
          <dd>{bundleSettingEntity.discountPopupHeader}</dd>
          <dt>
            <span id="discountPopupAmount">Discount Popup Amount</span>
          </dt>
          <dd>{bundleSettingEntity.discountPopupAmount}</dd>
          <dt>
            <span id="discountPopupCheckoutMessage">Discount Popup Checkout Message</span>
          </dt>
          <dd>{bundleSettingEntity.discountPopupCheckoutMessage}</dd>
          <dt>
            <span id="discountPopupBuy">Discount Popup Buy</span>
          </dt>
          <dd>{bundleSettingEntity.discountPopupBuy}</dd>
          <dt>
            <span id="discountPopupNo">Discount Popup No</span>
          </dt>
          <dd>{bundleSettingEntity.discountPopupNo}</dd>
          <dt>
            <span id="showDiscountPopup">Show Discount Popup</span>
          </dt>
          <dd>{bundleSettingEntity.showDiscountPopup ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/bundle-setting" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bundle-setting/${bundleSettingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bundleSetting }: IRootState) => ({
  bundleSettingEntity: bundleSetting.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleSettingDetail);
