import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-field.reducer';
import { IProductField } from 'app/shared/model/product-field.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductFieldDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductFieldDetail = (props: IProductFieldDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productFieldEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ProductField [<b>{productFieldEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{productFieldEntity.shop}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{productFieldEntity.title}</dd>
          <dt>
            <span id="enabled">Enabled</span>
          </dt>
          <dd>{productFieldEntity.enabled ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/product-field" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-field/${productFieldEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productField }: IRootState) => ({
  productFieldEntity: productField.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductFieldDetail);
