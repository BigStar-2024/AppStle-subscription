import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './plan-info-discount.reducer';
import { IPlanInfoDiscount } from 'app/shared/model/plan-info-discount.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlanInfoDiscountDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PlanInfoDiscountDetail = (props: IPlanInfoDiscountDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { planInfoDiscountEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          PlanInfoDiscount [<b>{planInfoDiscountEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="discountCode">Discount Code</span>
          </dt>
          <dd>{planInfoDiscountEntity.discountCode}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{planInfoDiscountEntity.description}</dd>
          <dt>
            <span id="discountType">Discount Type</span>
          </dt>
          <dd>{planInfoDiscountEntity.discountType}</dd>
          <dt>
            <span id="discount">Discount</span>
          </dt>
          <dd>{planInfoDiscountEntity.discount}</dd>
          <dt>
            <span id="maxDiscountAmount">Max Discount Amount</span>
          </dt>
          <dd>{planInfoDiscountEntity.maxDiscountAmount}</dd>
          <dt>
            <span id="trialDays">Trial Days</span>
          </dt>
          <dd>{planInfoDiscountEntity.trialDays}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>
            <TextFormat value={planInfoDiscountEntity.startDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>
            <TextFormat value={planInfoDiscountEntity.endDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="archived">Archived</span>
          </dt>
          <dd>{planInfoDiscountEntity.archived ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/admin/plan-info-discount" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/plan-info-discount/${planInfoDiscountEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ planInfoDiscount }: IRootState) => ({
  planInfoDiscountEntity: planInfoDiscount.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PlanInfoDiscountDetail);
