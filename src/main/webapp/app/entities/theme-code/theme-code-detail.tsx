import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './theme-code.reducer';
import { IThemeCode } from 'app/shared/model/theme-code.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IThemeCodeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ThemeCodeDetail = (props: IThemeCodeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { themeCodeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ThemeCode [<b>{themeCodeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="themeName">Theme Name</span>
          </dt>
          <dd>{themeCodeEntity.themeName}</dd>
          <dt>
            <span id="themeNameFriendly">Theme Name Friendly</span>
          </dt>
          <dd>{themeCodeEntity.themeNameFriendly}</dd>
          <dt>
            <span id="themeStoreId">Theme Store Id</span>
          </dt>
          <dd>{themeCodeEntity.themeStoreId}</dd>
          <dt>
            <span id="addToCartSelector">Add To Cart Selector</span>
          </dt>
          <dd>{themeCodeEntity.addToCartSelector}</dd>
          <dt>
            <span id="subscriptionLinkSelector">Subscription Link Selector</span>
          </dt>
          <dd>{themeCodeEntity.subscriptionLinkSelector}</dd>
          <dt>
            <span id="addToCartPlacement">Add To Cart Placement</span>
          </dt>
          <dd>{themeCodeEntity.addToCartPlacement}</dd>
          <dt>
            <span id="subscriptionLinkPlacement">Subscription Link Placement</span>
          </dt>
          <dd>{themeCodeEntity.subscriptionLinkPlacement}</dd>
          <dt>
            <span id="priceSelector">Price Selector</span>
          </dt>
          <dd>{themeCodeEntity.priceSelector}</dd>
          <dt>
            <span id="pricePlacement">Price Placement</span>
          </dt>
          <dd>{themeCodeEntity.pricePlacement}</dd>
          <dt>
            <span id="badgeTop">Badge Top</span>
          </dt>
          <dd>{themeCodeEntity.badgeTop}</dd>
          <dt>
            <span id="cartRowSelector">Cart Row Selector</span>
          </dt>
          <dd>{themeCodeEntity.cartRowSelector}</dd>
          <dt>
            <span id="cartLineItemSelector">Cart Line Item Selector</span>
          </dt>
          <dd>{themeCodeEntity.cartLineItemSelector}</dd>
          <dt>
            <span id="cartLineItemPerQuantityPriceSelector">Cart Line Item Per Quantity Price Selector</span>
          </dt>
          <dd>{themeCodeEntity.cartLineItemPerQuantityPriceSelector}</dd>
          <dt>
            <span id="cartLineItemTotalPriceSelector">Cart Line Item Total Price Selector</span>
          </dt>
          <dd>{themeCodeEntity.cartLineItemTotalPriceSelector}</dd>
          <dt>
            <span id="cartLineItemSellingPlanNameSelector">Cart Line Item Selling Plan Name Selector</span>
          </dt>
          <dd>{themeCodeEntity.cartLineItemSellingPlanNameSelector}</dd>
          <dt>
            <span id="cartSubTotalSelector">Cart Sub Total Selector</span>
          </dt>
          <dd>{themeCodeEntity.cartSubTotalSelector}</dd>
          <dt>
            <span id="cartLineItemPriceSelector">Cart Line Item Price Selector</span>
          </dt>
          <dd>{themeCodeEntity.cartLineItemPriceSelector}</dd>
        </dl>
        <Button tag={Link} to="/theme-code" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/theme-code/${themeCodeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ themeCode }: IRootState) => ({
  themeCodeEntity: themeCode.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ThemeCodeDetail);
