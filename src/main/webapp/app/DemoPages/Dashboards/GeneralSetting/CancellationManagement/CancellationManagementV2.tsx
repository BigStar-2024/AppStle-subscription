import React, { useEffect, useState, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import _ from 'lodash';
import { IPayloadResult } from 'react-jhipster';
import { Spinner } from 'reactstrap';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { Form as FinalForm } from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import { IRootState } from 'app/shared/reducers/';
import { ICancellationManagement } from 'app/shared/model/cancellation-management.model';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import {
  updateEntity as updateCustomerPortalSettingsEntity,
  getEntity as getCustomerPortalSettingsEntity,
} from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import {
  getEntity as getCancellationManagementEntity,
  updateEntity as updateCancellationManagementEntity,
} from 'app/entities/cancellation-management/cancellation-management.reducer';
import { CancellationTypeStatus } from 'app/shared/model/enumerations/cancellation-type-status.model';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck.jsx';
import PageTitle from 'app/Layout/AppMain/PageTitle.js';
import CancellationFlowTypeSelect from './CancellationFlowTypeSelect';
import CancellationReasonsFormPart from './CancellationReasonsFormPart';
import CancellationInstructionsFormPart from './CancellationInstructionsFormPart';
import PauseDurationFormPart from './PauseDurationFormPart';
import CancellationReason, { CancellationReasonAction, defaultValue as defaultReason } from 'app/shared/model/cancellation-reason.model';
import { completeChecklistItem } from 'app/entities/onboarding-info/onboarding-info.reducer';
import OnboardingChecklistStep from 'app/shared/model/enumerations/onboarding-checklist-step.model';

const CancellationManagement = () => {
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );
  const cancellationManagementEntity = useSelector((state: IRootState) => state.cancellationManagement.entity);
  const isUpdating = useSelector((state: IRootState) => state.cancellationManagement.updating || state.customerPortalSettings.updating);
  const dispatch = useDispatch();

  const [cancellationType, setCancellationType] = useState(cancellationManagementEntity.cancellationType);
  const [formValues, setFormValues] = useState<any>({ ...cancellationFormValues(), ...customerPortalSettingsFormValues() });

  const submitForm = useRef<() => void>(() => {
    console.log('No submit function assigned.');
  });

  useEffect(() => {
    dispatch(getCancellationManagementEntity(0));
    dispatch(getCustomerPortalSettingsEntity(0));
    dispatch(completeChecklistItem(OnboardingChecklistStep.DUNNING_CANCELLATION));
  }, []);

  useEffect(() => {
    setCancellationType(cancellationManagementEntity.cancellationType);
    setFormValues((old) => ({ ...old, ...cancellationFormValues() }));
  }, [cancellationManagementEntity]);

  useEffect(() => {
    setFormValues((old) => ({ ...old, ...customerPortalSettingsFormValues() }));
  }, [customerPortalSettingsEntity]);

  const isEntityLoaded = 'cancellationType' in cancellationManagementEntity;

  function cancellationFormValues() {
    const cancellationFormValues: Omit<ICancellationManagement, 'cancellationReasonsJSON'> & {
      cancellationReasonsJSON?: CancellationReason[];
    } = {
      ...cancellationManagementEntity,
      cancellationReasonsJSON: [],
    };

    function addReasonValuesForLegacy(reason: CancellationReason): CancellationReason {
      const newReason: CancellationReason = { ...reason };

      if (newReason?.cancellationAction == null) newReason.cancellationAction = CancellationReasonAction.NONE;
      if (newReason?.cancellationPrompt == null) newReason.cancellationPrompt = false;

      return newReason;
    }

    try {
      const parsedValues = JSON.parse(cancellationManagementEntity.cancellationReasonsJSON) as CancellationReason[];
      cancellationFormValues.cancellationReasonsJSON = parsedValues.map((oldReason: CancellationReason) =>
        addReasonValuesForLegacy(oldReason)
      );
      if (parsedValues.length < 1) {
        cancellationFormValues.cancellationReasonsJSON = [defaultReason];
      }
    } catch (error) {
      console.log('Could not get cancellation reasons yet...');
      cancellationFormValues.cancellationReasonsJSON = [defaultReason];
    }

    return cancellationFormValues;
  }

  function customerPortalSettingsFormValues() {
    return {
      subscriptionIsStillPausedText: customerPortalSettingsEntity?.subscriptionIsStillPausedText,
      discountRecurringCycleLimitOnCancellation: customerPortalSettingsEntity?.discountRecurringCycleLimitOnCancellation,
      discountMessageOnCancellation: customerPortalSettingsEntity?.discountMessageOnCancellation,
      swapMessageOnCancellation: customerPortalSettingsEntity?.swapMessageOnCancellation,
      giftMessageOnCancellation: customerPortalSettingsEntity?.giftMessageOnCancellation,
      pauseMessageOnCancellation: customerPortalSettingsEntity?.pauseMessageOnCancellation,
      skipMessageOnCancellation: customerPortalSettingsEntity?.skipMessageOnCancellation,
      changeDateMessageOnCancellation: customerPortalSettingsEntity?.changeDateMessageOnCancellation,
      changeAddressMessageOnCancellation: customerPortalSettingsEntity?.changeAddressMessageOnCancellation,
      updateFrequencyMessageOnCancellation: customerPortalSettingsEntity?.updateFrequencyMessageOnCancellation,
    };
  }

  function saveCancellationManagementEntity(submittedValues: any) {
    const entity: ICancellationManagement = {
      ...cancellationManagementEntity,
      cancellationType: cancellationType ?? cancellationManagementEntity.cancellationType,
    };

    // Only save and update values relevant to selected cancellation flow type.
    switch (cancellationType) {
      case CancellationTypeStatus.CUSTOMER_RETENTION_FLOW:
        entity.cancellationReasonsJSON =
          JSON.stringify(submittedValues.cancellationReasonsJSON) ?? cancellationManagementEntity.cancellationReasonsJSON;
        entity.pauseDurationCycle = submittedValues.pauseDurationCycle ?? cancellationManagementEntity.pauseDurationCycle;
        entity.enableDiscountEmail = submittedValues.enableDiscountEmail ?? cancellationManagementEntity.enableDiscountEmail;
        entity.discountEmailAddress = submittedValues.discountEmailAddress ?? cancellationManagementEntity.discountEmailAddress;
        break;
      case CancellationTypeStatus.CANCELLATION_INSTRUCTIONS:
        entity.cancellationInstructionsText =
          submittedValues.cancellationInstructionsText ?? cancellationManagementEntity.cancellationInstructionsText;
        break;
      case CancellationTypeStatus.CANCEL_AFTER_PAUSE:
        entity.pauseInstructionsText = submittedValues.pauseInstructionsText ?? cancellationManagementEntity.pauseInstructionsText;
        entity.pauseDurationCycle = submittedValues.pauseDurationCycle ?? cancellationManagementEntity.pauseDurationCycle;
        break;
    }
    (updateCancellationManagementEntity(entity) as IPayloadResult<ICancellationManagement>)(dispatch);
  }

  function saveCustomerPortalSettingsEntity(submittedValues: any) {
    const entity: ICustomerPortalSettings & { [key: string]: any } = {
      ...customerPortalSettingsEntity,
    };

    switch (cancellationType) {
      case CancellationTypeStatus.CUSTOMER_RETENTION_FLOW:
        entity.discountRecurringCycleLimitOnCancellation =
          submittedValues?.discountRecurringCycleLimitOnCancellation ??
          customerPortalSettingsEntity.discountRecurringCycleLimitOnCancellation;
        entity.discountMessageOnCancellation =
          submittedValues?.discountMessageOnCancellation ?? customerPortalSettingsEntity.discountMessageOnCancellation;
        entity.swapMessageOnCancellation =
          submittedValues?.swapMessageOnCancellation ?? customerPortalSettingsEntity.swapMessageOnCancellation;
        entity.giftMessageOnCancellation =
          submittedValues?.giftMessageOnCancellation ?? customerPortalSettingsEntity.giftMessageOnCancellation;
        entity.pauseMessageOnCancellation =
          submittedValues?.pauseMessageOnCancellation ?? customerPortalSettingsEntity.pauseMessageOnCancellation;
        entity.skipMessageOnCancellation =
          submittedValues?.skipMessageOnCancellation ?? customerPortalSettingsEntity.skipMessageOnCancellation;
        entity.changeDateMessageOnCancellation =
          submittedValues?.changeDateMessageOnCancellation ?? customerPortalSettingsEntity.changeDateMessageOnCancellation;
        entity.changeAddressMessageOnCancellation =
          submittedValues?.changeAddressMessageOnCancellation ?? customerPortalSettingsEntity.changeAddressMessageOnCancellation;
        entity.updateFrequencyMessageOnCancellation =
          submittedValues?.updateFrequencyMessageOnCancellation ?? customerPortalSettingsEntity.updateFrequencyMessageOnCancellation;
        break;
      case CancellationTypeStatus.CANCEL_AFTER_PAUSE:
        entity.subscriptionIsStillPausedText =
          submittedValues?.subscriptionIsStillPausedText ?? customerPortalSettingsEntity?.subscriptionIsStillPausedText;
        break;
    }

    // Only update customerPortalSettingsEntity if relevant changes were made
    if (!_.isEqual(entity, customerPortalSettingsEntity)) {
      (updateCustomerPortalSettingsEntity(entity) as IPayloadResult<ICancellationManagement>)(dispatch);
    }
  }

  function handleSelectCancellationType(selectedType: CancellationTypeStatus) {
    setCancellationType(selectedType);
  }

  function handleSubmitForm(submittedValues: any) {
    setFormValues(submittedValues);
    saveCancellationManagementEntity(submittedValues);
    saveCustomerPortalSettingsEntity(submittedValues);
  }

  // Validate the discounts at the form level rather than field level, as
  // field level was not working properly
  function handleFormLevelValidation(values: any) {
    const errors: any = {};
    if ('cancellationReasonsJSON' in values && Array.isArray(values.cancellationReasonsJSON)) {
      values?.cancellationReasonsJSON.forEach((reason: CancellationReason, index: number) => {
        if (reason.cancellationAction != CancellationReasonAction.DISCOUNT) {
          return;
        }

        if (!reason.cancellationDiscount || reason.cancellationDiscount < 1 || reason.cancellationDiscount > 100) {
          if (!errors.cancellationReasonsJSON) {
            errors.cancellationReasonsJSON = new Array(values.cancellationReasonsJSON.length);
          }

          errors.cancellationReasonsJSON[index] = {
            cancellationDiscount: 'Please enter a valid discount',
          };
        }
      });
    }
    return errors;
  }

  const getCancellationTypeFormPart = (selectedCancellationType: CancellationTypeStatus, form: any) => {
    switch (selectedCancellationType) {
      case CancellationTypeStatus.CUSTOMER_RETENTION_FLOW:
        return <CancellationReasonsFormPart mutators={form.mutators} />;
      case CancellationTypeStatus.CANCELLATION_INSTRUCTIONS:
        return <CancellationInstructionsFormPart />;
      case CancellationTypeStatus.CANCEL_AFTER_PAUSE:
        return <PauseDurationFormPart />;
      case CancellationTypeStatus.CANCEL_IMMEDIATELY:
      default:
        false;
    }
  };

  return (
    <FeatureAccessCheck hasAnyAuthorities={'enableCancellationManagement'} upgradeButtonText="Upgrade to enable Cancellation Management">
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading="Cancellation Management"
          subheading=""
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            try {
              submitForm.current();
            } catch (err) {
              console.log(err);
            }
          }}
          onActionUpdating={isUpdating}
          updatingText="Updating"
          sticky
          tutorialButton={{
            show: true,
            videos: [
              {
                title: 'Cancel Immediately',
                url: 'https://www.youtube.com/watch?v=89ijO4qRaAM',
              },
              {
                title: 'Provide Cancellation Instructions',
                url: 'https://www.youtube.com/watch?v=PH-C0aPNntM',
              },
              {
                title: 'Customer Retention Flow',
                url: 'https://www.youtube.com/watch?v=K7Qglo_nJoo',
              },
              {
                title: 'Pause Option as Retention Step Before Cancellation',
                url: 'https://www.youtube.com/watch?v=BShJNofKHk8',
              },
            ],
            docs: [
              {
                title: "How to Smartly Use Appstle's Cancellation Management Feature to Retain Customers",
                url:'https://intercom.help/appstle/en/articles/5417506-how-to-smartly-use-appstle-s-cancelation-management-feature-to-retain-customers'
              }
            ]
          }}
        />
        {!isEntityLoaded && (
          <div className="d-flex justify-content-center" style={{ marginTop: '25vh' }}>
            <Spinner animation="border" color="primary" />
          </div>
        )}
        {!!isEntityLoaded && (
          <div>
            <CancellationFlowTypeSelect cancellationType={cancellationType} onSelect={handleSelectCancellationType} />
            <FinalForm
              onSubmit={handleSubmitForm}
              initialValues={formValues}
              validate={handleFormLevelValidation}
              mutators={{
                ...arrayMutators,
                setDiscount: (args, state, utils) => {
                  const index = args[0];
                  const value = args[1];
                  utils.changeValue(state, `cancellationReasonsJSON[${index}].cancellationDiscount`, () => value);
                },
              }}
              render={({ handleSubmit, form }) => {
                submitForm.current = handleSubmit;
                return (
                  <form className="mt-4" onSubmit={handleSubmit}>
                    {getCancellationTypeFormPart(cancellationType, form)}
                  </form>
                );
              }}
            />
          </div>
        )}
      </ReactCSSTransitionGroup>
    </FeatureAccessCheck>
  );
};

export default CancellationManagement;
