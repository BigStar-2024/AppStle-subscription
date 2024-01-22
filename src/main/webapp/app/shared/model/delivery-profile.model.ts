export interface IDeliveryProfile {
  id?: number;
  shop?: string;
  deliveryProfileId?: string;
  locationInfos?: [{}];
  countryInfos?: [{}];
  name?: string;
}

export const defaultValue: Readonly<IDeliveryProfile> = {
  locationInfos: [{}],
  name: ''
};
