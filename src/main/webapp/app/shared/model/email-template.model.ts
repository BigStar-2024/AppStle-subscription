import { EmailSettingType } from 'app/shared/model/enumerations/email-setting-type.model';

export interface IEmailTemplate {
  id?: number;
  emailType?: EmailSettingType;
  html?: any;
}

export const defaultValue: Readonly<IEmailTemplate> = {};
