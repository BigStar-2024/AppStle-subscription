import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './member-only.reducer';
import { IMemberOnly } from 'app/shared/model/member-only.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMemberOnlyProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const MemberOnly = (props: IMemberOnlyProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { memberOnlyList, match, loading } = props;
  return (
    <div>
      <h2 id="member-only-heading">
        Member Onlies
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Member Only
        </Link>
      </h2>
      <div className="table-responsive">
        {memberOnlyList && memberOnlyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Selling Plan Id</th>
                <th>Tags</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {memberOnlyList.map((memberOnly, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${memberOnly.id}`} color="link" size="sm">
                      {memberOnly.id}
                    </Button>
                  </td>
                  <td>{memberOnly.shop}</td>
                  <td>{memberOnly.sellingPlanId}</td>
                  <td>{memberOnly.tags}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${memberOnly.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${memberOnly.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${memberOnly.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Member Onlies found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ memberOnly }: IRootState) => ({
  memberOnlyList: memberOnly.entities,
  loading: memberOnly.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemberOnly);
