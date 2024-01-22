import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './bundle-dynamic-script.reducer';
import { IBundleDynamicScript } from 'app/shared/model/bundle-dynamic-script.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBundleDynamicScriptDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BundleDynamicScriptDetail = (props: IBundleDynamicScriptDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bundleDynamicScriptEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          BundleDynamicScript [<b>{bundleDynamicScriptEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{bundleDynamicScriptEntity.shop}</dd>
          <dt>
            <span id="dynamicScript">Dynamic Script</span>
          </dt>
          <dd>{bundleDynamicScriptEntity.dynamicScript}</dd>
        </dl>
        <Button tag={Link} to="/bundle-dynamic-script" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bundle-dynamic-script/${bundleDynamicScriptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bundleDynamicScript }: IRootState) => ({
  bundleDynamicScriptEntity: bundleDynamicScript.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleDynamicScriptDetail);
