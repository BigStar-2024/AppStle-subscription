import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer-trigger-rule.reducer';
import { ICustomerTriggerRule } from 'app/shared/model/customer-trigger-rule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerTriggerRuleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerTriggerRuleDetail = (props: ICustomerTriggerRuleDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { customerTriggerRuleEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CustomerTriggerRule [<b>{customerTriggerRuleEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{customerTriggerRuleEntity.shop}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{customerTriggerRuleEntity.name}</dd>
          <dt>
            <span id="appendToNote">Append To Note</span>
          </dt>
          <dd>{customerTriggerRuleEntity.appendToNote ? 'true' : 'false'}</dd>
          <dt>
            <span id="webhook">Webhook</span>
          </dt>
          <dd>{customerTriggerRuleEntity.webhook}</dd>
          <dt>
            <span id="deactivateAfterDate">Deactivate After Date</span>
          </dt>
          <dd>
            {customerTriggerRuleEntity.deactivateAfterDate ? (
              <TextFormat value={customerTriggerRuleEntity.deactivateAfterDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deactivateAfterTime">Deactivate After Time</span>
          </dt>
          <dd>{customerTriggerRuleEntity.deactivateAfterTime}</dd>
          <dt>
            <span id="fixedTags">Fixed Tags</span>
          </dt>
          <dd>{customerTriggerRuleEntity.fixedTags}</dd>
          <dt>
            <span id="dynamicTags">Dynamic Tags</span>
          </dt>
          <dd>{customerTriggerRuleEntity.dynamicTags}</dd>
          <dt>
            <span id="removeTags">Remove Tags</span>
          </dt>
          <dd>{customerTriggerRuleEntity.removeTags}</dd>
          <dt>
            <span id="notMatchTags">Not Match Tags</span>
          </dt>
          <dd>{customerTriggerRuleEntity.notMatchTags}</dd>
          <dt>
            <span id="removeTagsExpiresIn">Remove Tags Expires In</span>
          </dt>
          <dd>{customerTriggerRuleEntity.removeTagsExpiresIn}</dd>
          <dt>
            <span id="removeTagsExpiresInUnit">Remove Tags Expires In Unit</span>
          </dt>
          <dd>{customerTriggerRuleEntity.removeTagsExpiresInUnit}</dd>
          <dt>
            <span id="handlerData">Handler Data</span>
          </dt>
          <dd>{customerTriggerRuleEntity.handlerData}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{customerTriggerRuleEntity.status}</dd>
          <dt>
            <span id="deactivatedAt">Deactivated At</span>
          </dt>
          <dd>
            {customerTriggerRuleEntity.deactivatedAt ? (
              <TextFormat value={customerTriggerRuleEntity.deactivatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/customer-trigger-rule" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer-trigger-rule/${customerTriggerRuleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ customerTriggerRule }: IRootState) => ({
  customerTriggerRuleEntity: customerTriggerRule.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerTriggerRuleDetail);
