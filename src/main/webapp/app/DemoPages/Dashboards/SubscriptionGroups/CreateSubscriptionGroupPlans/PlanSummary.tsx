import React from 'react';
import { Button } from 'reactstrap';
import { SubscriptionPlan } from 'app/shared/model/subscription-group.model';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';
import { DiscountTypeUnit } from 'app/shared/model/enumerations/discount-type-unit.model';
import { PlanChildProps } from '../CreateSubscriptionGroupPlans';

type CreateSubscriptionGroupPlanSummaryProps = PlanChildProps & {
  editPlanIndex: number;
  setEditPlanIndex: (index: number) => void;
  setPrevPlanData: (data: SubscriptionPlan) => void;
};

const PlanSummary = ({ fields, index, editPlanIndex, setEditPlanIndex, setPrevPlanData }: CreateSubscriptionGroupPlanSummaryProps) => {
  const field = fields.value[index];

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: '100%',
      }}
    >
      <div>
        <h5>
          <b>{field?.planType === OrderBillingType.PAY_AS_YOU_GO ? 'Pay as You Go Plan ' : 'Prepaid Plan'}</b>
        </h5>
        <div>
          Frequency Plan Name: <b>{field?.frequencyName}</b>
        </div>
        <div>
          {field?.planType !== OrderBillingType.PAY_AS_YOU_GO ? 'Delivery/Fulfillment frequency: ' : 'Order frequency: '}
          <b>
            {`${field.frequencyCount} ${field.frequencyInterval}${field.frequencyCount > 1 ? 'S' : ''}`}
          </b>
        </div>
        {field.specificDayEnabled && (
          <div>
            Delivery Day: <b>{field.specificDayValue}</b>
          </div>
        )}
        {field.planType !== OrderBillingType.PAY_AS_YOU_GO && (
          <div>
            Billing frequency:
            <b>{`${field.billingFrequencyCount} ${field.frequencyInterval}${field.frequencyCount > 1 ? 'S' : ''}`}</b>
          </div>
        )}

        <div>
          Discount Enabled:
          <b>{field.discountEnabled ? ' Yes' : ' No'}</b>
        </div>
        {field.discountEnabled && (
          <div>
            Discount:{' '}
            <b>
              {`
                ${field.discountType === DiscountTypeUnit.FIXED ? '$' : ''}
                ${field.discountOffer}
                ${field.discountType === DiscountTypeUnit.PERCENTAGE ? '%' : ''}
              `}
            </b>
          </div>
        )}
      </div>
      {editPlanIndex < 0 && (
        <div>
          <Button
            className="btn-wide mb-2 btn-icon btn-icon-right btn-pill"
            outline
            size="sm"
            color="success"
            style={{
              marginRight: '0px !important',
              padding: '3px 12px 3px 12px',
            }}
            onClick={() => {
              setPrevPlanData(fields.value[index]);
              setEditPlanIndex(index);
            }}
          >
            Edit
            <i className="lnr-pencil btn-icon-wrapper" style={{ fontSize: '13px', marginLeft: '7' }}></i>
          </Button>
          <Button
            className="btn-wide mb-2 ml-2 btn-icon btn-icon-right btn-pill"
            outline
            size="sm"
            color="danger"
            style={{ marginRight: '0px !important', padding: '1px 5px 0 11px' }}
            onClick={() => {
              setEditPlanIndex(-1);
              fields.remove(index);
            }}
          >
            Delete
            <i className="pe-7s-close btn-icon-wrapper" style={{ fontSize: '24px', marginLeft: '0' }}></i>
          </Button>
          <Button
            className="btn-wide mb-2  ml-2  btn-icon btn-icon-right btn-pill"
            outline
            size="sm"
            color="success"
            disabled={index <= 0}
            onClick={() => fields.swap(index, index - 1)}
            style={{ marginRight: '0px !important', padding: '3px 12px 3px 12px' }}
          >
            <i className="lnr-pencil lnr-arrow-up-circle" style={{ fontSize: '13px', marginLeft: '7' }}></i>
          </Button>
          <Button
            className="btn-wide mb-2 ml-2 btn-icon btn-icon-right btn-pill"
            outline
            size="sm"
            color="success"
            onClick={() => fields.swap(index, index + 1)}
            disabled={index >= fields.value.length - 1}
            style={{ marginRight: '0px !important', padding: '3px 12px 3px 12px' }}
          >
            <i className="lnr-pencil lnr-arrow-down-circle" style={{ fontSize: '13px', marginLeft: '7' }}></i>
          </Button>
        </div>
      )}
    </div>
  );
};

export default PlanSummary;
