import { EmailSettingType } from 'app/shared/model/enumerations/email-setting-type.model';

export interface IEmailTemplateSetting {
  id?: number;
  shop?: string;
  emailSettingType?: EmailSettingType;
  sendEmailDisabled?: boolean;
  subject?: any;
  fromEmail?: string;
  logo?: string;
  heading?: any;
  headingTextColor?: string;
  contentTextColor?: string;
  contentLinkColor?: string;
  content?: any;
  footerTextColor?: string;
  footerLinkColor?: string;
  footerText?: any;
  templateBackgroundColor?: string;
  html?: any;
  textImageUrl?: string;
  manageSubscriptionButtonColor?: string;
  manageSubscriptionButtonText?: string;
  manageSubscriptionButtonTextColor?: string;
  shippingAddressText?: string;
  billingAddressText?: string;
  nextOrderdateText?: string;
  paymentMethodText?: string;
  endingInText?: string;
  bccEmail?: string;
  upcomingOrderEmailBuffer?: number;
  headingImageUrl?: string;
  quantityText?: string;
  manageSubscriptionButtonUrl?: string;
  logoHeight?: string;
  logoWidth?: string;
  thanksImageHeight?: string;
  thanksImageWidth?: string;
  logoAlignment?: string;
  thanksImageAlignment?: string;
  shippingAddress?: any;
  billingAddress?: any;
  replyTo?: string;
  sendBCCEmailFlag?: boolean;
  sellingPlanNameText?: string;
  variantSkuText?: string;
}

export const defaultValue: Readonly<IEmailTemplateSetting> = {
  sendEmailDisabled: false,
  sendBCCEmailFlag: false
};
