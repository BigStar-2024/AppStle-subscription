import React from 'react';
import { Button } from 'reactstrap';
import { SubscriptionPlan, defaultValue } from 'app/shared/model/subscription-group.model';
import { useFormState } from 'react-final-form';
import { PlanChildProps } from '../CreateSubscriptionGroupPlans';

type PlanControlButtonsProps = PlanChildProps & {
  setEditPlanIndex: (index: number) => void;
  prevPlanData: SubscriptionPlan;
};

const PlanControlButtons = ({ fields, index, setEditPlanIndex, prevPlanData }: PlanControlButtonsProps) => {
  const { errors } = useFormState();

  const canSave = !errors?.subscriptionPlans?.[index];

  return (
    <div className="d-flex" style={{ width: '100%', gap: '.5rem' }}>
      {fields.value.length > 1 && (
        <Button
          onClick={() => {
            if (!canSave) return;
            setEditPlanIndex(-1);
          }}
          disabled={!canSave}
          size="lg"
          className="btn-shadow-primary"
          color="primary"
        >
          Complete this Edit
        </Button>
      )}
      {fields.value.length - 1 === index && (
        <Button
          onClick={() => {
            if (!canSave) return;
            fields.push(defaultValue.subscriptionPlans[0]);
            setEditPlanIndex(index+1);
          }}
          disabled={!canSave}
          size="lg"
          className="btn-shadow-warning"
          color="warning"
        >
          <i className="lnr-add btn-icon-wrapper" style={{ fontSize: '13px', marginLeft: '7' }}></i>
          Add Another Frequency Option
        </Button>
      )}

      {fields.value.length > 1 && (
        <Button
          onClick={() => {
            setEditPlanIndex(-1);
            if (prevPlanData) {
              fields.update(index, prevPlanData);
            } else {
              fields.remove(index);
            }
          }}
          size="lg"
          className="btn-shadow-danger"
          color="danger"
        >
          Cancel
        </Button>
      )}
    </div>
  );
};

export default PlanControlButtons;
