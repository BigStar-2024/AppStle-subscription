import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-contract-settings.reducer';
import { ISubscriptionContractSettings } from 'app/shared/model/subscription-contract-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionContractSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractSettingsDetail = (props: ISubscriptionContractSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionContractSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SubscriptionContractSettings [<b>{subscriptionContractSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{subscriptionContractSettingsEntity.shop}</dd>
          <dt>
            <span id="productId">Product Id</span>
          </dt>
          <dd>{subscriptionContractSettingsEntity.productId}</dd>
          <dt>
            <span id="endsOnCount">Ends On Count</span>
          </dt>
          <dd>{subscriptionContractSettingsEntity.endsOnCount}</dd>
          <dt>
            <span id="endsOnInterval">Ends On Interval</span>
          </dt>
          <dd>{subscriptionContractSettingsEntity.endsOnInterval}</dd>
        </dl>
        <Button tag={Link} to="/subscription-contract-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-contract-settings/${subscriptionContractSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionContractSettings }: IRootState) => ({
  subscriptionContractSettingsEntity: subscriptionContractSettings.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractSettingsDetail);
