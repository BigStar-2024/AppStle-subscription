import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './widget-template.reducer';
import { IWidgetTemplate } from 'app/shared/model/widget-template.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IWidgetTemplateDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const WidgetTemplateDetail = (props: IWidgetTemplateDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { widgetTemplateEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          WidgetTemplate [<b>{widgetTemplateEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{widgetTemplateEntity.type}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{widgetTemplateEntity.title}</dd>
          <dt>
            <span id="html">Html</span>
          </dt>
          <dd>{widgetTemplateEntity.html}</dd>
        </dl>
        <Button tag={Link} to="/admin/widget-template" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/widget-template/${widgetTemplateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ widgetTemplate }: IRootState) => ({
  widgetTemplateEntity: widgetTemplate.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(WidgetTemplateDetail);
