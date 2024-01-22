import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './bundle-dynamic-script.reducer';
import { IBundleDynamicScript } from 'app/shared/model/bundle-dynamic-script.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBundleDynamicScriptProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const BundleDynamicScript = (props: IBundleDynamicScriptProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { bundleDynamicScriptList, match, loading } = props;
  return (
    <div>
      <h2 id="bundle-dynamic-script-heading">
        Bundle Dynamic Scripts
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Bundle Dynamic Script
        </Link>
      </h2>
      <div className="table-responsive">
        {bundleDynamicScriptList && bundleDynamicScriptList.length > 0 ? (
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
              {bundleDynamicScriptList.map((bundleDynamicScript, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${bundleDynamicScript.id}`} color="link" size="sm">
                      {bundleDynamicScript.id}
                    </Button>
                  </td>
                  <td>{bundleDynamicScript.shop}</td>
                  <td>{bundleDynamicScript.dynamicScript}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${bundleDynamicScript.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${bundleDynamicScript.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${bundleDynamicScript.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Bundle Dynamic Scripts found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ bundleDynamicScript }: IRootState) => ({
  bundleDynamicScriptList: bundleDynamicScript.entities,
  loading: bundleDynamicScript.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleDynamicScript);
