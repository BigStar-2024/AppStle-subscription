import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './widget-template.reducer';
import { IWidgetTemplate } from 'app/shared/model/widget-template.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IWidgetTemplateProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const WidgetTemplate = (props: IWidgetTemplateProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { widgetTemplateList, match } = props;
  return (
    <div>
      <h2 id="widget-template-heading">
        Widget Templates
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Widget Template
        </Link>
      </h2>
      <div className="table-responsive">
        {widgetTemplateList && widgetTemplateList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Title</th>
                <th>Html</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {widgetTemplateList.map((widgetTemplate, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${widgetTemplate.id}`} color="link" size="sm">
                      {widgetTemplate.id}
                    </Button>
                  </td>
                  <td>{widgetTemplate.type}</td>
                  <td>{widgetTemplate.title}</td>
                  <td>{widgetTemplate.html}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${widgetTemplate.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${widgetTemplate.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${widgetTemplate.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Widget Templates found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ widgetTemplate }: IRootState) => ({
  widgetTemplateList: widgetTemplate.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(WidgetTemplate);
