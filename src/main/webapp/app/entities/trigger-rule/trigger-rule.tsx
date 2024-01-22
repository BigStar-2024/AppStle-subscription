import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './trigger-rule.reducer';
import { ITriggerRule } from 'app/shared/model/trigger-rule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITriggerRuleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const TriggerRule = (props: ITriggerRuleProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { triggerRuleList, match, loading } = props;
  return (
    <div>
      <h2 id="trigger-rule-heading">
        Trigger Rules
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Trigger Rule
        </Link>
      </h2>
      <div className="table-responsive">
        {triggerRuleList && triggerRuleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Name</th>
                <th>Append To Note</th>
                <th>Webhook</th>
                <th>Deactivate After Date</th>
                <th>Deactivate After Time</th>
                <th>Fixed Tags</th>
                <th>Dynamic Tags</th>
                <th>Remove Tags</th>
                <th>Not Match Tags</th>
                <th>Remove Tags Expires In</th>
                <th>Remove Tags Expires In Unit</th>
                <th>Handler Data</th>
                <th>Status</th>
                <th>Deactivated At</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {triggerRuleList.map((triggerRule, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${triggerRule.id}`} color="link" size="sm">
                      {triggerRule.id}
                    </Button>
                  </td>
                  <td>{triggerRule.shop}</td>
                  <td>{triggerRule.name}</td>
                  <td>{triggerRule.appendToNote ? 'true' : 'false'}</td>
                  <td>{triggerRule.webhook}</td>
                  <td>
                    {triggerRule.deactivateAfterDate ? (
                      <TextFormat type="date" value={triggerRule.deactivateAfterDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{triggerRule.deactivateAfterTime}</td>
                  <td>{triggerRule.fixedTags}</td>
                  <td>{triggerRule.dynamicTags}</td>
                  <td>{triggerRule.removeTags}</td>
                  <td>{triggerRule.notMatchTags}</td>
                  <td>{triggerRule.removeTagsExpiresIn}</td>
                  <td>{triggerRule.removeTagsExpiresInUnit}</td>
                  <td>{triggerRule.handlerData}</td>
                  <td>{triggerRule.status}</td>
                  <td>
                    {triggerRule.deactivatedAt ? (
                      <TextFormat type="date" value={triggerRule.deactivatedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${triggerRule.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${triggerRule.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${triggerRule.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Trigger Rules found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ triggerRule }: IRootState) => ({
  triggerRuleList: triggerRule.entities,
  loading: triggerRule.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TriggerRule);
