import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './rule-criteria.reducer';
import { IRuleCriteria } from 'app/shared/model/rule-criteria.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRuleCriteriaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RuleCriteriaDetail = (props: IRuleCriteriaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { ruleCriteriaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          RuleCriteria [<b>{ruleCriteriaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{ruleCriteriaEntity.shop}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{ruleCriteriaEntity.name}</dd>
          <dt>
            <span id="identifier">Identifier</span>
          </dt>
          <dd>{ruleCriteriaEntity.identifier}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{ruleCriteriaEntity.type}</dd>
          <dt>
            <span id="group">Group</span>
          </dt>
          <dd>{ruleCriteriaEntity.group}</dd>
          <dt>
            <span id="fields">Fields</span>
          </dt>
          <dd>{ruleCriteriaEntity.fields}</dd>
        </dl>
        <Button tag={Link} to="/rule-criteria" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rule-criteria/${ruleCriteriaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ ruleCriteria }: IRootState) => ({
  ruleCriteriaEntity: ruleCriteria.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RuleCriteriaDetail);
