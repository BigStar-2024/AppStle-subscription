import React, { useState } from 'react';
import {
  Card,
  CardBody,
} from 'reactstrap';
import _ from 'lodash';
import PlanSummary from './PlanSummary';
import PlanControlButtons from './PlanControlButtons';
import { SubscriptionPlan } from 'app/shared/model/subscription-group.model';
import { PlanChildProps } from '../CreateSubscriptionGroupPlans';
import PlanGeneralOptions from './PlanGeneralOptions';
import PlanAdvancedOptions from './PlanAdvancedOptions';

type PlanItemProps = PlanChildProps & {
  showAdvancedOptions?: boolean;
  editPlanIndex: number;
  setEditPlanIndex: (index: number) => void;
};

const PlanItem = (props: PlanItemProps) => {
  const { fields, name, index, showAdvancedOptions = true, editPlanIndex, setEditPlanIndex } = props;

  const [prevPlanData, setPrevPlanData] = useState<SubscriptionPlan>(null);

  const isEditing = editPlanIndex === index;
  const planChildProps = { fields, name, index };

  return (
    <Card className="card-margin mb-3">
      <CardBody>
        {!isEditing && (
          <PlanSummary
            {...planChildProps}
            editPlanIndex={editPlanIndex}
            setEditPlanIndex={setEditPlanIndex}
            setPrevPlanData={setPrevPlanData}
          />
        )}
        {isEditing && (
          <>
            <PlanControlButtons {...planChildProps} setEditPlanIndex={setEditPlanIndex} prevPlanData={prevPlanData} />
            <hr className="mx-0 my-3" />
            <PlanGeneralOptions {...planChildProps} />
            {showAdvancedOptions && <PlanAdvancedOptions {...planChildProps} />}
          </>
        )}
      </CardBody>
    </Card>
  );
};

export default PlanItem;
