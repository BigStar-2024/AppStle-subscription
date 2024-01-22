import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './customer-trigger-rule.reducer';
import { ICustomerTriggerRule } from 'app/shared/model/customer-trigger-rule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerTriggerRuleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CustomerTriggerRule = (props: ICustomerTriggerRuleProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { customerTriggerRuleList, match, loading } = props;
  return (
    <div>
      <h2 id="customer-trigger-rule-heading">
        Customer Trigger Rules
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Customer Trigger Rule
        </Link>
      </h2>
      <div className="table-responsive">
        {customerTriggerRuleList && customerTriggerRuleList.length > 0 ? (
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
              {customerTriggerRuleList.map((customerTriggerRule, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customerTriggerRule.id}`} color="link" size="sm">
                      {customerTriggerRule.id}
                    </Button>
                  </td>
                  <td>{customerTriggerRule.shop}</td>
                  <td>{customerTriggerRule.name}</td>
                  <td>{customerTriggerRule.appendToNote ? 'true' : 'false'}</td>
                  <td>{customerTriggerRule.webhook}</td>
                  <td>
                    {customerTriggerRule.deactivateAfterDate ? (
                      <TextFormat type="date" value={customerTriggerRule.deactivateAfterDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{customerTriggerRule.deactivateAfterTime}</td>
                  <td>{customerTriggerRule.fixedTags}</td>
                  <td>{customerTriggerRule.dynamicTags}</td>
                  <td>{customerTriggerRule.removeTags}</td>
                  <td>{customerTriggerRule.notMatchTags}</td>
                  <td>{customerTriggerRule.removeTagsExpiresIn}</td>
                  <td>{customerTriggerRule.removeTagsExpiresInUnit}</td>
                  <td>{customerTriggerRule.handlerData}</td>
                  <td>{customerTriggerRule.status}</td>
                  <td>
                    {customerTriggerRule.deactivatedAt ? (
                      <TextFormat type="date" value={customerTriggerRule.deactivatedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customerTriggerRule.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerTriggerRule.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerTriggerRule.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Customer Trigger Rules found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ customerTriggerRule }: IRootState) => ({
  customerTriggerRuleList: customerTriggerRule.entities,
  loading: customerTriggerRule.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerTriggerRule);
