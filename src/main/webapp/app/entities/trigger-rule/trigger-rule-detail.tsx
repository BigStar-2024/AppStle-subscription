import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './trigger-rule.reducer';
import { ITriggerRule } from 'app/shared/model/trigger-rule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITriggerRuleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TriggerRuleDetail = (props: ITriggerRuleDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { triggerRuleEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          TriggerRule [<b>{triggerRuleEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{triggerRuleEntity.shop}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{triggerRuleEntity.name}</dd>
          <dt>
            <span id="appendToNote">Append To Note</span>
          </dt>
          <dd>{triggerRuleEntity.appendToNote ? 'true' : 'false'}</dd>
          <dt>
            <span id="webhook">Webhook</span>
          </dt>
          <dd>{triggerRuleEntity.webhook}</dd>
          <dt>
            <span id="deactivateAfterDate">Deactivate After Date</span>
          </dt>
          <dd>
            {triggerRuleEntity.deactivateAfterDate ? (
              <TextFormat value={triggerRuleEntity.deactivateAfterDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deactivateAfterTime">Deactivate After Time</span>
          </dt>
          <dd>{triggerRuleEntity.deactivateAfterTime}</dd>
          <dt>
            <span id="fixedTags">Fixed Tags</span>
          </dt>
          <dd>{triggerRuleEntity.fixedTags}</dd>
          <dt>
            <span id="dynamicTags">Dynamic Tags</span>
          </dt>
          <dd>{triggerRuleEntity.dynamicTags}</dd>
          <dt>
            <span id="removeTags">Remove Tags</span>
          </dt>
          <dd>{triggerRuleEntity.removeTags}</dd>
          <dt>
            <span id="notMatchTags">Not Match Tags</span>
          </dt>
          <dd>{triggerRuleEntity.notMatchTags}</dd>
          <dt>
            <span id="removeTagsExpiresIn">Remove Tags Expires In</span>
          </dt>
          <dd>{triggerRuleEntity.removeTagsExpiresIn}</dd>
          <dt>
            <span id="removeTagsExpiresInUnit">Remove Tags Expires In Unit</span>
          </dt>
          <dd>{triggerRuleEntity.removeTagsExpiresInUnit}</dd>
          <dt>
            <span id="handlerData">Handler Data</span>
          </dt>
          <dd>{triggerRuleEntity.handlerData}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{triggerRuleEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/trigger-rule" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trigger-rule/${triggerRuleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ triggerRule }: IRootState) => ({
  triggerRuleEntity: triggerRule.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TriggerRuleDetail);
