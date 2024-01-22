interface PaymentSettings {
  __typename: string;
  supportedDigitalWallets: string[];
}

export interface IShopPaymentInfo {
  __typename?: string;
  id?: string;
  paymentSettings?: PaymentSettings;
}

export const defaultValue: Readonly<IShopPaymentInfo> = {};
