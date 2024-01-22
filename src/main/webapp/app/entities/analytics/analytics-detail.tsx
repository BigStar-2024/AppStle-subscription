import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './analytics.reducer';
import { IAnalytics } from 'app/shared/model/analytics.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnalyticsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnalyticsDetail = (props: IAnalyticsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { analyticsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Analytics [<b>{analyticsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{analyticsEntity.shop}</dd>
          <dt>
            <span id="totalSubscriptions">Total Subscriptions</span>
          </dt>
          <dd>{analyticsEntity.totalSubscriptions}</dd>
          <dt>
            <span id="totalOrders">Total Orders</span>
          </dt>
          <dd>{analyticsEntity.totalOrders}</dd>
          <dt>
            <span id="totalOrderAmount">Total Order Amount</span>
          </dt>
          <dd>{analyticsEntity.totalOrderAmount}</dd>
          <dt>
            <span id="firstTimeOrders">First Time Orders</span>
          </dt>
          <dd>{analyticsEntity.firstTimeOrders}</dd>
          <dt>
            <span id="recurringOrders">Recurring Orders</span>
          </dt>
          <dd>{analyticsEntity.recurringOrders}</dd>
          <dt>
            <span id="totalCustomers">Total Customers</span>
          </dt>
          <dd>{analyticsEntity.totalCustomers}</dd>
        </dl>
        <Button tag={Link} to="/analytics" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/analytics/${analyticsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ analytics }: IRootState) => ({
  analyticsEntity: analytics.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnalyticsDetail);
