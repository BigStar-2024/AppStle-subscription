import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment-plan.reducer';
import { IPaymentPlan } from 'app/shared/model/payment-plan.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentPlanDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentPlanDetail = (props: IPaymentPlanDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { paymentPlanEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          PaymentPlan [<b>{paymentPlanEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{paymentPlanEntity.shop}</dd>
          <dt>
            <span id="chargeActivated">Charge Activated</span>
          </dt>
          <dd>{paymentPlanEntity.chargeActivated ? 'true' : 'false'}</dd>
          <dt>
            <span id="activationDate">Activation Date</span>
          </dt>
          <dd>
            <TextFormat value={paymentPlanEntity.activationDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="recurringChargeId">Recurring Charge Id</span>
          </dt>
          <dd>{paymentPlanEntity.recurringChargeId}</dd>
          <dt>
            <span id="lastEmailSentToMerchant">Last Email Sent To Merchant</span>
          </dt>
          <dd>
            <TextFormat value={paymentPlanEntity.lastEmailSentToMerchant} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="numberOfEmailSentToMerchant">Number Of Email Sent To Merchant</span>
          </dt>
          <dd>{paymentPlanEntity.numberOfEmailSentToMerchant}</dd>
          <dt>
            <span id="basePlan">Base Plan</span>
          </dt>
          <dd>{paymentPlanEntity.basePlan}</dd>
          <dt>
            <span id="additionalDetails">Additional Details</span>
          </dt>
          <dd>{paymentPlanEntity.additionalDetails}</dd>
          <dt>
            <span id="trialDays">Trial Days</span>
          </dt>
          <dd>{paymentPlanEntity.trialDays}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{paymentPlanEntity.price}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{paymentPlanEntity.name}</dd>
          <dt>
            <span id="planType">Plan Type</span>
          </dt>
          <dd>{paymentPlanEntity.planType}</dd>
          <dt>
            <span id="billingType">Billing Type</span>
          </dt>
          <dd>{paymentPlanEntity.billingType}</dd>
          <dt>
            <span id="basedOn">Based On</span>
          </dt>
          <dd>{paymentPlanEntity.basedOn}</dd>
          <dt>
            <span id="features">Features</span>
          </dt>
          <dd>{paymentPlanEntity.features}</dd>
          <dt>
            <span id="testCharge">Test Charge</span>
          </dt>
          <dd>{paymentPlanEntity.testCharge ? 'true' : 'false'}</dd>
          <dt>
            <span id="billedDate">Billed Date</span>
          </dt>
          <dd>
            <TextFormat value={paymentPlanEntity.billedDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="validCharge">Valid Charge</span>
          </dt>
          <dd>{paymentPlanEntity.validCharge ? 'true' : 'false'}</dd>
          <dt>
            <span id="trialEndsOn">Trial Ends On</span>
          </dt>
          <dd>
            <TextFormat value={paymentPlanEntity.trialEndsOn} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>Plan Info</dt>
          <dd>{paymentPlanEntity.planInfo ? paymentPlanEntity.planInfo.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/admin/payment-plan" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin/payment-plan/${paymentPlanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ paymentPlan }: IRootState) => ({
  paymentPlanEntity: paymentPlan.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentPlanDetail);
