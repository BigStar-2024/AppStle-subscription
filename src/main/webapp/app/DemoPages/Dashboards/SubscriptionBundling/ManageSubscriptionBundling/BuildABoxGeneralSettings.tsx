import React, { useState } from 'react';
import axios from 'axios';
import { Field, useFormState, useForm, FieldInputProps } from 'react-final-form';
import { Button, Card, CardBody, FormGroup, FormText, Input, Label } from 'reactstrap';
import Select from 'react-select';
import Loader from 'react-loaders';
import { BuildABoxType } from 'app/shared/model/enumerations/build-a-box-type.model';
import { useSelector } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import BuildABoxLearnModal from './BuildABoxLearnModal';
import { Link } from 'react-router-dom';

type SelectOption = {
  value: number | string;
  label: string;
};

type BuildABoxGeneralSettingsProps = {
  setIsValidModelOpen?: React.Dispatch<React.SetStateAction<boolean>>;
};

const BuildABoxGeneralSettings = (props: BuildABoxGeneralSettingsProps) => {
  const { setIsValidModelOpen = () => {} } = props;

  const subscriptionGroupEntities = useSelector((state: IRootState) => state.subscriptionGroup.entities);
  const [firstSelectedGroupPlans, setFirstSelectedGroupPlans] = useState<ISubscriptionGroup>(null);
  const [isValidatingPlan, setValidatingPlan] = useState(false);
  const [isLearnModalOpen, setIsLearnModalOpen] = useState(false);

  const form = useForm();
  const { values } = useFormState();

  const options = subscriptionGroupEntities.map(subscriptionGroup => ({
    value: subscriptionGroup.id,
    label: subscriptionGroup?.groupName,
  }));

  async function handleChangeSelectedPlans(
    selectedOptions: SelectOption[],
    plansInput: FieldInputProps<SelectOption[]>,
    idInput: FieldInputProps<number | string>
  ) {
    idInput.onChange(selectedOptions[0]?.value);

    if (selectedOptions.length > 1 && selectedOptions.length > plansInput.value?.length) {
      const valid = await validateLatestGroupWithFirst(selectedOptions);
      if (!valid) {
        setIsValidModelOpen(true);
        return;
      }
    }

    plansInput.onChange(selectedOptions);
  }

  async function validateLatestGroupWithFirst(selectedOptions: SelectOption[]) {
    setValidatingPlan(true);

    const firstSelectedPlans =
      !values?.subscriptionGroup?.length || firstSelectedGroupPlans == null
        ? (await axios.get('api/v2/subscription-groups/' + selectedOptions[0].value)).data.subscriptionPlans
        : firstSelectedGroupPlans;
    setFirstSelectedGroupPlans(firstSelectedPlans);

    const lastSelectedPlans = (await axios.get('api/v2/subscription-groups/' + selectedOptions[selectedOptions.length - 1].value)).data
      .subscriptionPlans;

    const valid =
      firstSelectedPlans?.length !== lastSelectedPlans?.length
        ? false
        : firstSelectedPlans
            .map((firstPlan: ISubscriptionGroup) =>
              lastSelectedPlans.reduce((acc: boolean, secondPlan: ISubscriptionGroup) => checkMatch(firstPlan, secondPlan) || acc, false)
            )
            .reduce((acc: boolean, match: boolean) => acc && match, true);

    setValidatingPlan(false);
    return valid;
  }

  function checkMatch(firstPlan: ISubscriptionGroup, secondPlan: ISubscriptionGroup) {
    const compareFields = [
      'frequencyInterval',
      'frequencyCount',
      'billingFrequencyInterval',
      'billingFrequencyCount',
      'planType',
      'discountOffer',
      'discountOffer2',
      'discountEnabled',
      'discountEnabled2',
      'discountEnabledMasked',
      'discountEnabled2Masked',
      'frequencyType',
      'specificMonthValue',
      'specificDayEnabled',
      'maxCycles',
      'minCycles',
      'cutOff',
      'prepaidFlag',
      'freeTrialEnabled',
      'freeTrialCount',
      'memberExclusiveTags',
      'memberInclusiveTags',
    ];
    for (const field of compareFields) {
      if (firstPlan[field] !== secondPlan[field]) {
        return false;
      }
    }
    return true;
  }

  return (
    <Card>
      <CardBody>
        <FormGroup>
          <Label for="name">Build-A-Box Name</Label>
          <Field
            id="name"
            name="name"
            // validate={value => {
            //   return !value ? 'Please Select Build-A-Box name.' : undefined;
            // }}
          >
            {({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched} />}
          </Field>
        </FormGroup>
        <FormGroup>
          <Label for="buildABoxType">Build-A-Box Type</Label>
          <Field
            render={({ input, meta }) => (
              <Input
                {...input}
                invalid={meta.error && meta.touched}
                onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
                  input.onChange(event.target.value);
                  form.mutators.setValue('subscriptionId', false);
                  form.mutators.setValue('subscriptionGroup', []);
                }}
              >
                <option value={BuildABoxType.CLASSIC}>Classic</option>
                <option value={BuildABoxType.SINGLE_PRODUCT}>Single Product</option>
              </Input>
            )}
            validate={value => (!value ? 'Please Select Build-A-Box type.' : undefined)}
            type="select"
            className="form-control"
            name="buildABoxType"
          />
          <FormText className="d-flex align-items-baseline">
            For more details about {values.buildABoxType === BuildABoxType.CLASSIC ? 'Classic' : 'Single Product'} Build-A-Box{' '}
            <Button
              color="link"
              className="p-0 ml-1"
              onClick={(e: React.MouseEvent) => {
                e.preventDefault();
                setIsLearnModalOpen(true);
              }}
            >
              click here
            </Button>
            .
          </FormText>
        </FormGroup>
        <FormGroup>
          <Label for="subscriptionId">
            {values.buildABoxType === BuildABoxType.CLASSIC ? 'Merge Multiple Subscription Plans' : 'Subscription Plan'}
          </Label>
          <Field
            name="subscriptionGroup"
            validate={(value: SelectOption[]) => (!value.length ? 'Please select a subscription group' : undefined)}
          >
            {({ input: plansInput, meta: plansMeta }) => (
              <Field name="subscriptionId">
                {({ input: idInput }) => (
                  <>
                    <Select
                      value={plansInput.value}
                      options={options}
                      isMulti={values.buildABoxType === BuildABoxType.CLASSIC}
                      placeholder={`Select Subscription Plan${values.buildABoxType === BuildABoxType.CLASSIC ? 's' : ''}`}
                      onChange={(value: SelectOption | SelectOption[]) => {
                        handleChangeSelectedPlans(Array.isArray(value) ? value : [value], plansInput, idInput);
                      }}
                    />
                    {!options.length && (
                      <div className="mt-1">
                        <Link to="/dashboards/subscription-plan">Click here to create your first subscription plan to use in Build-A-Box</Link>
                      </div>
                    )}
                    {plansMeta.error && plansMeta.touched && <span className="d-block invalid-feedback">{plansMeta.error}</span>}
                  </>
                )}
              </Field>
            )}
          </Field>
        </FormGroup>
        {isValidatingPlan && (
          <div className="as-h-screen as-flex as-items-center as-justify-center as-bg-gray-100">
            <Loader type="line-scale" active />
            <span className="as-ml-2 as-text-sm">Please wait...</span>
          </div>
        )}{' '}
      </CardBody>
      <BuildABoxLearnModal boxType={values.buildABoxType} isOpen={isLearnModalOpen} setIsOpen={setIsLearnModalOpen} />
    </Card>
  );
};

export default BuildABoxGeneralSettings;
