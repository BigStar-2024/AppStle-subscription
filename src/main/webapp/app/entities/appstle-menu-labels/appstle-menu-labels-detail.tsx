import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './appstle-menu-labels.reducer';
import { IAppstleMenuLabels } from 'app/shared/model/appstle-menu-labels.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAppstleMenuLabelsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AppstleMenuLabelsDetail = (props: IAppstleMenuLabelsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { appstleMenuLabelsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          AppstleMenuLabels [<b>{appstleMenuLabelsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.shop}</dd>
          <dt>
            <span id="customCss">Custom Css</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.customCss}</dd>
          <dt>
            <span id="labels">Labels</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.labels}</dd>
          <dt>
            <span id="seeMore">See More</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.seeMore}</dd>
          <dt>
            <span id="noDataFound">No Data Found</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.noDataFound}</dd>
          <dt>
            <span id="productDetails">Product Details</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.productDetails}</dd>
          <dt>
            <span id="editQuantity">Edit Quantity</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.editQuantity}</dd>
          <dt>
            <span id="addToCart">Add To Cart</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.addToCart}</dd>
          <dt>
            <span id="productAddedSuccessfully">Product Added Successfully</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.productAddedSuccessfully}</dd>
          <dt>
            <span id="wentWrong">Went Wrong</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.wentWrong}</dd>
          <dt>
            <span id="results">Results</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.results}</dd>
          <dt>
            <span id="adding">Adding</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.adding}</dd>
          <dt>
            <span id="subscribe">Subscribe</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.subscribe}</dd>
          <dt>
            <span id="notAvailable">Not Available</span>
          </dt>
          <dd>{appstleMenuLabelsEntity.notAvailable}</dd>
        </dl>
        <Button tag={Link} to="/appstle-menu-labels" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/appstle-menu-labels/${appstleMenuLabelsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ appstleMenuLabels }: IRootState) => ({
  appstleMenuLabelsEntity: appstleMenuLabels.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AppstleMenuLabelsDetail);
