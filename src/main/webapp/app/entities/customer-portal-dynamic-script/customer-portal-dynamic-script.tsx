import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './customer-portal-dynamic-script.reducer';
import { ICustomerPortalDynamicScript } from 'app/shared/model/customer-portal-dynamic-script.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerPortalDynamicScriptProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CustomerPortalDynamicScript = (props: ICustomerPortalDynamicScriptProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { customerPortalDynamicScriptList, match, loading } = props;
  return (
    <div>
      <h2 id="customer-portal-dynamic-script-heading">
        Customer Portal Dynamic Scripts
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Customer Portal Dynamic Script
        </Link>
      </h2>
      <div className="table-responsive">
        {customerPortalDynamicScriptList && customerPortalDynamicScriptList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Dynamic Script</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerPortalDynamicScriptList.map((customerPortalDynamicScript, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customerPortalDynamicScript.id}`} color="link" size="sm">
                      {customerPortalDynamicScript.id}
                    </Button>
                  </td>
                  <td>{customerPortalDynamicScript.shop}</td>
                  <td>{customerPortalDynamicScript.dynamicScript}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customerPortalDynamicScript.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerPortalDynamicScript.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerPortalDynamicScript.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Customer Portal Dynamic Scripts found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ customerPortalDynamicScript }: IRootState) => ({
  customerPortalDynamicScriptList: customerPortalDynamicScript.entities,
  loading: customerPortalDynamicScript.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPortalDynamicScript);
