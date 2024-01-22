import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './customization.reducer';
import { ICustomization } from 'app/shared/model/customization.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomizationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Customization = (props: ICustomizationProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { customizationList, match } = props;
  return (
    <div>
      <h2 id="customization-heading">
        Customizations
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Customization
        </Link>
      </h2>
      <div className="table-responsive">
        {customizationList && customizationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Label</th>
                <th>Type</th>
                <th>Css</th>
                <th>Category</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customizationList.map((customization, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customization.id}`} color="link" size="sm">
                      {customization.id}
                    </Button>
                  </td>
                  <td>{customization.label}</td>
                  <td>{customization.type}</td>
                  <td>{customization.css}</td>
                  <td>{customization.category}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customization.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customization.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customization.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Customizations found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ customization }: IRootState) => ({
  customizationList: customization.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Customization);
