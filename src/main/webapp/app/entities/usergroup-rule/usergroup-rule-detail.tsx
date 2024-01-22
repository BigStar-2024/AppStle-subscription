import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './usergroup-rule.reducer';
import { IUsergroupRule } from 'app/shared/model/usergroup-rule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUsergroupRuleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UsergroupRuleDetail = (props: IUsergroupRuleDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { usergroupRuleEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          UsergroupRule [<b>{usergroupRuleEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{usergroupRuleEntity.shop}</dd>
          <dt>
            <span id="forCustomersWithTags">For Customers With Tags</span>
          </dt>
          <dd>{usergroupRuleEntity.forCustomersWithTags}</dd>
          <dt>
            <span id="forProductsWithTags">For Products With Tags</span>
          </dt>
          <dd>{usergroupRuleEntity.forProductsWithTags}</dd>
          <dt>
            <span id="action">Action</span>
          </dt>
          <dd>{usergroupRuleEntity.action}</dd>
        </dl>
        <Button tag={Link} to="/usergroup-rule" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/usergroup-rule/${usergroupRuleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ usergroupRule }: IRootState) => ({
  usergroupRuleEntity: usergroupRule.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UsergroupRuleDetail);
