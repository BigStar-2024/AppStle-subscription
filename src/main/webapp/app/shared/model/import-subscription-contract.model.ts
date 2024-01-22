export interface IImportSubscriptionContract {
  id?: string;
  status?: string;
  deliveryFirstName?: string;
  deliveryLastName?: string;
  deliveryAddress1?: string;
  deliveryAddress2?: string;
  deliveryProvinceCode?: string;
  deliveryCity?: string;
  deliveryZip?: string;
  deliveryCountryCode?: string;
  deliveryPhone?: string;
  deliveryPriceAmount?: string;
  nextOrderDate?: string;
  billingIntervalType?: string;
  billingIntervalCount?: string;
  deliveryIntervalType?: string;
  deliveryIntervalCount?: string;
  lineZeroQuantity?: string;
  lineZeroPriceAmount?: string;
  lineZeroVariantID?: string;
  customerEmail?: string;
  customerID?: string;
  cardID?: string;
  importType?: String;
}

export const defaultValue: Readonly<IImportSubscriptionContract> = {};
