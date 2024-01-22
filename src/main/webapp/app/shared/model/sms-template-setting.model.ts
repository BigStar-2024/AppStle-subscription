import { SmsSettingType } from 'app/shared/model/enumerations/sms-setting-type.model';

export interface ISmsTemplateSetting {
  id?: number;
  shop?: string;
  smsSettingType?: SmsSettingType;
  sendSmsDisabled?: boolean;
  smsContent?: any;
  stopReplySMS?: boolean;
}

export const defaultValue: Readonly<ISmsTemplateSetting> = {
  sendSmsDisabled: false,
  stopReplySMS: false
};
