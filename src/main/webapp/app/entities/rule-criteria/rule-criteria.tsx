import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './rule-criteria.reducer';
import { IRuleCriteria } from 'app/shared/model/rule-criteria.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRuleCriteriaProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const RuleCriteria = (props: IRuleCriteriaProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { ruleCriteriaList, match, loading } = props;
  return (
    <div>
      <h2 id="rule-criteria-heading">
        Rule Criteria
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Rule Criteria
        </Link>
      </h2>
      <div className="table-responsive">
        {ruleCriteriaList && ruleCriteriaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Name</th>
                <th>Identifier</th>
                <th>Type</th>
                <th>Group</th>
                <th>Fields</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ruleCriteriaList.map((ruleCriteria, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${ruleCriteria.id}`} color="link" size="sm">
                      {ruleCriteria.id}
                    </Button>
                  </td>
                  <td>{ruleCriteria.shop}</td>
                  <td>{ruleCriteria.name}</td>
                  <td>{ruleCriteria.identifier}</td>
                  <td>{ruleCriteria.type}</td>
                  <td>{ruleCriteria.group}</td>
                  <td>{ruleCriteria.fields}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${ruleCriteria.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ruleCriteria.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ruleCriteria.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Rule Criteria found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ ruleCriteria }: IRootState) => ({
  ruleCriteriaList: ruleCriteria.entities,
  loading: ruleCriteria.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RuleCriteria);
