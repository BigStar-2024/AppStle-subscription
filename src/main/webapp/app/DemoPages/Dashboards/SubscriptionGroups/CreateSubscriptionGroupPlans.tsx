import React, { useState, useEffect } from 'react';
import { Button } from 'reactstrap';
import { FieldArray, FieldArrayRenderProps } from 'react-final-form-arrays';
import { SubscriptionPlan } from 'app/shared/model/subscription-group.model';
import { defaultValue } from 'app/shared/model/subscription-group.model';
import PlanItem from './CreateSubscriptionGroupPlans/PlanItem';
import { useFormState, useForm } from 'react-final-form';
import { CreateSubscriptionGroupValues, SubscriptionPlanValues } from './CreateSubscriptionGroup';

export type PlanChildProps = {
  fields: FieldArrayRenderProps<SubscriptionPlanValues, HTMLElement>['fields'];
  name: string;
  index: number;
};

const CreateSubscriptionGroupPlans = ({ showAdvancedOptions = true }) => {
  const [editPlanIndex, setEditPlanIndex] = useState<number>(-1);

  const { errors, values } = useFormState<CreateSubscriptionGroupValues>();

  useEffect(() => {
    if (values.subscriptionPlans?.length <= 1) {
      setEditPlanIndex(0);
    }
  }, [values.subscriptionPlans]);

  return (
    <FieldArray name="subscriptionPlans">
      {({ fields }) => {
        return (
          <>
            {fields.map((name, index) => (
              <PlanItem
                key={index}
                fields={fields}
                name={name}
                index={index}
                editPlanIndex={editPlanIndex}
                setEditPlanIndex={setEditPlanIndex}
                showAdvancedOptions={showAdvancedOptions}
              />
            ))}
            <Button
              onClick={() => {
                fields.push(defaultValue.subscriptionPlans[0]);
                setEditPlanIndex(fields.value.length);
              }}
              size="lg"
              className="btn-shadow-primary"
              color="primary"
              style={{ display: !errors?.subscriptionPlans ? 'inline-block' : 'none' }}
            >
              Add Frequency Plan
            </Button>
          </>
        );
      }}
    </FieldArray>
  );
};

export default CreateSubscriptionGroupPlans;
