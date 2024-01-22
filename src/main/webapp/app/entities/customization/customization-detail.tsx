import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customization.reducer';
import { ICustomization } from 'app/shared/model/customization.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomizationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomizationDetail = (props: ICustomizationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { customizationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Customization [<b>{customizationEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{customizationEntity.label}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{customizationEntity.type}</dd>
          <dt>
            <span id="css">Css</span>
          </dt>
          <dd>{customizationEntity.css}</dd>
          <dt>
            <span id="category">Category</span>
          </dt>
          <dd>{customizationEntity.category}</dd>
        </dl>
        <Button tag={Link} to="/admin/customization" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/customization/${customizationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ customization }: IRootState) => ({
  customizationEntity: customization.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomizationDetail);
