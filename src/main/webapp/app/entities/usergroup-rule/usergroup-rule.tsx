import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './usergroup-rule.reducer';
import { IUsergroupRule } from 'app/shared/model/usergroup-rule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUsergroupRuleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const UsergroupRule = (props: IUsergroupRuleProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { usergroupRuleList, match } = props;
  return (
    <div>
      <h2 id="usergroup-rule-heading">
        Usergroup Rules
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Usergroup Rule
        </Link>
      </h2>
      <div className="table-responsive">
        {usergroupRuleList && usergroupRuleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>For Customers With Tags</th>
                <th>For Products With Tags</th>
                <th>Action</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {usergroupRuleList.map((usergroupRule, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${usergroupRule.id}`} color="link" size="sm">
                      {usergroupRule.id}
                    </Button>
                  </td>
                  <td>{usergroupRule.shop}</td>
                  <td>{usergroupRule.forCustomersWithTags}</td>
                  <td>{usergroupRule.forProductsWithTags}</td>
                  <td>{usergroupRule.action}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${usergroupRule.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${usergroupRule.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${usergroupRule.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Usergroup Rules found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ usergroupRule }: IRootState) => ({
  usergroupRuleList: usergroupRule.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UsergroupRule);
