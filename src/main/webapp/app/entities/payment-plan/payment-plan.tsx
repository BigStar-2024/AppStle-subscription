import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './payment-plan.reducer';
import { IPaymentPlan } from 'app/shared/model/payment-plan.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentPlanProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const PaymentPlan = (props: IPaymentPlanProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { paymentPlanList, match } = props;
  return (
    <div>
      <h2 id="payment-plan-heading">
        Payment Plans
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Payment Plan
        </Link>
      </h2>
      <div className="table-responsive">
        {paymentPlanList && paymentPlanList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Charge Activated</th>
                <th>Activation Date</th>
                <th>Recurring Charge Id</th>
                <th>Last Email Sent To Merchant</th>
                <th>Number Of Email Sent To Merchant</th>
                <th>Base Plan</th>
                <th>Additional Details</th>
                <th>Trial Days</th>
                <th>Price</th>
                <th>Name</th>
                <th>Plan Type</th>
                <th>Billing Type</th>
                <th>Based On</th>
                <th>Features</th>
                <th>Test Charge</th>
                <th>Billed Date</th>
                <th>Valid Charge</th>
                <th>Trial Ends On</th>
                <th>Plan Info</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentPlanList.map((paymentPlan, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${paymentPlan.id}`} color="link" size="sm">
                      {paymentPlan.id}
                    </Button>
                  </td>
                  <td>{paymentPlan.shop}</td>
                  <td>{paymentPlan.chargeActivated ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={paymentPlan.activationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{paymentPlan.recurringChargeId}</td>
                  <td>
                    <TextFormat type="date" value={paymentPlan.lastEmailSentToMerchant} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{paymentPlan.numberOfEmailSentToMerchant}</td>
                  <td>{paymentPlan.basePlan}</td>
                  <td>{paymentPlan.additionalDetails}</td>
                  <td>{paymentPlan.trialDays}</td>
                  <td>{paymentPlan.price}</td>
                  <td>{paymentPlan.name}</td>
                  <td>{paymentPlan.planType}</td>
                  <td>{paymentPlan.billingType}</td>
                  <td>{paymentPlan.basedOn}</td>
                  <td>{paymentPlan.features}</td>
                  <td>{paymentPlan.testCharge ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={paymentPlan.billedDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{paymentPlan.validCharge ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={paymentPlan.trialEndsOn} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{paymentPlan.planInfo ? <Link to={`plan-info/${paymentPlan.planInfo.id}`}>{paymentPlan.planInfo.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${paymentPlan.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${paymentPlan.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      {/* <Button tag={Link} to={`${match.url}/${paymentPlan.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button> */}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Payment Plans found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ paymentPlan }: IRootState) => ({
  paymentPlanList: paymentPlan.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentPlan);
