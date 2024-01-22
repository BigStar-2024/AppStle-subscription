import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './email-template.reducer';
import { IEmailTemplate } from 'app/shared/model/email-template.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmailTemplateProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const EmailTemplate = (props: IEmailTemplateProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { emailTemplateList, match, loading } = props;
  return (
    <div>
      <h2 id="email-template-heading">
        Email Templates
      </h2>
      <div className="table-responsive">
        {emailTemplateList && emailTemplateList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Email Type</th>
                {/*<th>Html</th>*/}
                <th />
              </tr>
            </thead>
            <tbody>
              {emailTemplateList.map((emailTemplate, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${emailTemplate.id}`} color="link" size="sm">
                      {emailTemplate.id}
                    </Button>
                  </td>
                  <td>{emailTemplate.emailType}</td>
                  {/*<td>{emailTemplate.html}</td>*/}
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${emailTemplate.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${emailTemplate.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      {/* <Button tag={Link} to={`${match.url}/${emailTemplate.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button> */}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Email Templates found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ emailTemplate }: IRootState) => ({
  emailTemplateList: emailTemplate.entities,
  loading: emailTemplate.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmailTemplate);
