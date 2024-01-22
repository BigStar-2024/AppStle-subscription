import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './email-template.reducer';
import { IEmailTemplate } from 'app/shared/model/email-template.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmailTemplateDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmailTemplateDetail = (props: IEmailTemplateDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { emailTemplateEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          EmailTemplate [<b>{emailTemplateEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="emailType">Email Type</span>
          </dt>
          <dd>{emailTemplateEntity.emailType}</dd>
          {/*<dt>
            <span id="html">Html</span>
          </dt>*/}
          {/*<dd>{emailTemplateEntity.html}</dd>*/}
        </dl>
        <Button tag={Link} to="/admin/email-template" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/email-template/${emailTemplateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ emailTemplate }: IRootState) => ({
  emailTemplateEntity: emailTemplate.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmailTemplateDetail);
