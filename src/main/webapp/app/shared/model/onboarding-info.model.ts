import OnboardingChecklistStep from './enumerations/onboarding-checklist-step.model';

export interface IOnboardingInfo {
  id?: number;
  shop?: string;
  uncompletedChecklistSteps?: OnboardingChecklistStep[];
  completedChecklistSteps?: OnboardingChecklistStep[];
  checklistCompleted?: boolean;
}

export const defaultValue: Readonly<IOnboardingInfo> = {
  uncompletedChecklistSteps: Object.values(OnboardingChecklistStep),
  completedChecklistSteps: [],
  checklistCompleted: false
};
