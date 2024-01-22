import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './selling-plan-member-info.reducer';
import { ISellingPlanMemberInfo } from 'app/shared/model/selling-plan-member-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISellingPlanMemberInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SellingPlanMemberInfo = (props: ISellingPlanMemberInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { sellingPlanMemberInfoList, match, loading } = props;
  return (
    <div>
      <h2 id="selling-plan-member-info-heading">
        Selling Plan Member Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Selling Plan Member Info
        </Link>
      </h2>
      <div className="table-responsive">
        {sellingPlanMemberInfoList && sellingPlanMemberInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Subscription Id</th>
                <th>Selling Plan Id</th>
                <th>Enable Member Inclusive Tag</th>
                <th>Member Inclusive Tags</th>
                <th>Enable Member Exclusive Tag</th>
                <th>Member Exclusive Tags</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sellingPlanMemberInfoList.map((sellingPlanMemberInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${sellingPlanMemberInfo.id}`} color="link" size="sm">
                      {sellingPlanMemberInfo.id}
                    </Button>
                  </td>
                  <td>{sellingPlanMemberInfo.shop}</td>
                  <td>{sellingPlanMemberInfo.subscriptionId}</td>
                  <td>{sellingPlanMemberInfo.sellingPlanId}</td>
                  <td>{sellingPlanMemberInfo.enableMemberInclusiveTag ? 'true' : 'false'}</td>
                  <td>{sellingPlanMemberInfo.memberInclusiveTags}</td>
                  <td>{sellingPlanMemberInfo.enableMemberExclusiveTag ? 'true' : 'false'}</td>
                  <td>{sellingPlanMemberInfo.memberExclusiveTags}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${sellingPlanMemberInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${sellingPlanMemberInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${sellingPlanMemberInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Selling Plan Member Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ sellingPlanMemberInfo }: IRootState) => ({
  sellingPlanMemberInfoList: sellingPlanMemberInfo.entities,
  loading: sellingPlanMemberInfo.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellingPlanMemberInfo);
