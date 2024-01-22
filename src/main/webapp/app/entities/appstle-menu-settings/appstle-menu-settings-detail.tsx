import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './appstle-menu-settings.reducer';
import { IAppstleMenuSettings } from 'app/shared/model/appstle-menu-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAppstleMenuSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AppstleMenuSettingsDetail = (props: IAppstleMenuSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { appstleMenuSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          AppstleMenuSettings [<b>{appstleMenuSettingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{appstleMenuSettingsEntity.shop}</dd>
          <dt>
            <span id="filterMenu">Filter Menu</span>
          </dt>
          <dd>{appstleMenuSettingsEntity.filterMenu}</dd>
          <dt>
            <span id="menuUrl">Menu Url</span>
          </dt>
          <dd>{appstleMenuSettingsEntity.menuUrl}</dd>
          <dt>
            <span id="menuStyle">Menu Style</span>
          </dt>
          <dd>{appstleMenuSettingsEntity.menuStyle}</dd>
          <dt>
            <span id="active">Active</span>
          </dt>
          <dd>{appstleMenuSettingsEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="handle">Handle</span>
          </dt>
          <dd>{appstleMenuSettingsEntity.handle}</dd>
          <dt>
            <span id="productViewStyle">Product View Style</span>
          </dt>
          <dd>{appstleMenuSettingsEntity.productViewStyle}</dd>
        </dl>
        <Button tag={Link} to="/appstle-menu-settings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/appstle-menu-settings/${appstleMenuSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ appstleMenuSettings }: IRootState) => ({
  appstleMenuSettingsEntity: appstleMenuSettings.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AppstleMenuSettingsDetail);
