import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './sms-template-setting.reducer';
import { ISmsTemplateSetting } from 'app/shared/model/sms-template-setting.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISmsTemplateSettingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SmsTemplateSettingDetail = (props: ISmsTemplateSettingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { smsTemplateSettingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SmsTemplateSetting [<b>{smsTemplateSettingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{smsTemplateSettingEntity.shop}</dd>
          <dt>
            <span id="smsSettingType">Sms Setting Type</span>
          </dt>
          <dd>{smsTemplateSettingEntity.smsSettingType}</dd>
          <dt>
            <span id="sendSmsDisabled">Send Sms Disabled</span>
          </dt>
          <dd>{smsTemplateSettingEntity.sendSmsDisabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="smsContent">Sms Content</span>
          </dt>
          <dd>{smsTemplateSettingEntity.smsContent}</dd>
          <dt>
            <span id="stopReplySMS">Stop Reply SMS</span>
          </dt>
          <dd>{smsTemplateSettingEntity.stopReplySMS ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/sms-template-setting" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sms-template-setting/${smsTemplateSettingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ smsTemplateSetting }: IRootState) => ({
  smsTemplateSettingEntity: smsTemplateSetting.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SmsTemplateSettingDetail);
