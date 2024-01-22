import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './sms-template-setting.reducer';
import { ISmsTemplateSetting } from 'app/shared/model/sms-template-setting.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISmsTemplateSettingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SmsTemplateSetting = (props: ISmsTemplateSettingProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { smsTemplateSettingList, match, loading } = props;
  return (
    <div>
      <h2 id="sms-template-setting-heading">
        Sms Template Settings
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Sms Template Setting
        </Link>
      </h2>
      <div className="table-responsive">
        {smsTemplateSettingList && smsTemplateSettingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Sms Setting Type</th>
                <th>Send Sms Disabled</th>
                <th>Sms Content</th>
                <th>Stop Reply SMS</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {smsTemplateSettingList.map((smsTemplateSetting, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${smsTemplateSetting.id}`} color="link" size="sm">
                      {smsTemplateSetting.id}
                    </Button>
                  </td>
                  <td>{smsTemplateSetting.shop}</td>
                  <td>{smsTemplateSetting.smsSettingType}</td>
                  <td>{smsTemplateSetting.sendSmsDisabled ? 'true' : 'false'}</td>
                  <td>{smsTemplateSetting.smsContent}</td>
                  <td>{smsTemplateSetting.stopReplySMS ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${smsTemplateSetting.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${smsTemplateSetting.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${smsTemplateSetting.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Sms Template Settings found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ smsTemplateSetting }: IRootState) => ({
  smsTemplateSettingList: smsTemplateSetting.entities,
  loading: smsTemplateSetting.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SmsTemplateSetting);
