import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment-plan-activity.reducer';
import { IPaymentPlanActivity } from 'app/shared/model/payment-plan-activity.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentPlanActivityDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentPlanActivityDetail = (props: IPaymentPlanActivityDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { paymentPlanActivityEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          PaymentPlanActivity [<b>{paymentPlanActivityEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="shop">Shop</span>
          </dt>
          <dd>{paymentPlanActivityEntity.shop}</dd>
          <dt>
            <span id="chargeActivated">Charge Activated</span>
          </dt>
          <dd>{paymentPlanActivityEntity.chargeActivated ? 'true' : 'false'}</dd>
          <dt>
            <span id="activationDate">Activation Date</span>
          </dt>
          <dd>
            <TextFormat value={paymentPlanActivityEntity.activationDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="recurringChargeId">Recurring Charge Id</span>
          </dt>
          <dd>{paymentPlanActivityEntity.recurringChargeId}</dd>
          <dt>
            <span id="lastEmailSentToMerchant">Last Email Sent To Merchant</span>
          </dt>
          <dd>
            <TextFormat value={paymentPlanActivityEntity.lastEmailSentToMerchant} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="numberOfEmailSentToMerchant">Number Of Email Sent To Merchant</span>
          </dt>
          <dd>{paymentPlanActivityEntity.numberOfEmailSentToMerchant}</dd>
          <dt>
            <span id="basePlan">Base Plan</span>
          </dt>
          <dd>{paymentPlanActivityEntity.basePlan}</dd>
          <dt>
            <span id="additionalDetails">Additional Details</span>
          </dt>
          <dd>{paymentPlanActivityEntity.additionalDetails}</dd>
          <dt>
            <span id="trialDays">Trial Days</span>
          </dt>
          <dd>{paymentPlanActivityEntity.trialDays}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{paymentPlanActivityEntity.price}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{paymentPlanActivityEntity.name}</dd>
          <dt>
            <span id="planType">Plan Type</span>
          </dt>
          <dd>{paymentPlanActivityEntity.planType}</dd>
          <dt>
            <span id="billingType">Billing Type</span>
          </dt>
          <dd>{paymentPlanActivityEntity.billingType}</dd>
          <dt>
            <span id="basedOn">Based On</span>
          </dt>
          <dd>{paymentPlanActivityEntity.basedOn}</dd>
          <dt>
            <span id="features">Features</span>
          </dt>
          <dd>{paymentPlanActivityEntity.features}</dd>
          <dt>
            <span id="testCharge">Test Charge</span>
          </dt>
          <dd>{paymentPlanActivityEntity.testCharge ? 'true' : 'false'}</dd>
          <dt>
            <span id="billedDate">Billed Date</span>
          </dt>
          <dd>
            <TextFormat value={paymentPlanActivityEntity.billedDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="validCharge">Valid Charge</span>
          </dt>
          <dd>{paymentPlanActivityEntity.validCharge ? 'true' : 'false'}</dd>
          <dt>
            <span id="trialEndsOn">Trial Ends On</span>
          </dt>
          <dd>
            <TextFormat value={paymentPlanActivityEntity.trialEndsOn} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="shopFrozen">Shop Frozen</span>
          </dt>
          <dd>{paymentPlanActivityEntity.shopFrozen ? 'true' : 'false'}</dd>
          <dt>
            <span id="paymentPlanEvent">Payment Plan Event</span>
          </dt>
          <dd>{paymentPlanActivityEntity.paymentPlanEvent}</dd>
          <dt>Plan Info</dt>
          <dd>{paymentPlanActivityEntity.planInfo ? paymentPlanActivityEntity.planInfo.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment-plan-activity" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-plan-activity/${paymentPlanActivityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ paymentPlanActivity }: IRootState) => ({
  paymentPlanActivityEntity: paymentPlanActivity.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentPlanActivityDetail);
