import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-custom-css.reducer';
import { ISubscriptionCustomCss } from 'app/shared/model/subscription-custom-css.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionCustomCssDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionCustomCssDetail = (props: ISubscriptionCustomCssDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionCustomCssEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionCustomCss [<b>{subscriptionCustomCssEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionCustomCssEntity.shop}</dd>
          <dt>
            <span id="customCss">Custom Css</span>
          </dt>
          <dd>{subscriptionCustomCssEntity.customCss}</dd>
          <dt>
            <span id="customerPoratlCSS">Customer Poratl CSS</span>
          </dt>
          <dd>{subscriptionCustomCssEntity.customerPoratlCSS}</dd>
          <dt>
            <span id="bundlingCSS">Bundling CSS</span>
          </dt>
          <dd>{subscriptionCustomCssEntity.bundlingCSS}</dd>
          <dt>
            <span id="bundlingIframeCSS">Bundling Iframe CSS</span>
          </dt>
          <dd>{subscriptionCustomCssEntity.bundlingIframeCSS}</dd>
        </dl>
        <Button tag={Link} to="/subscription-custom-css" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-custom-css/${subscriptionCustomCssEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionCustomCss }: IRootState) => ({
  subscriptionCustomCssEntity: subscriptionCustomCss.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionCustomCssDetail);
