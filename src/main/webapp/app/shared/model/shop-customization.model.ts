export interface IShopCustomization {
  id?: number;
  shop?: string;
  labelId?: number;
  value?: string;
}

export const defaultValue: Readonly<IShopCustomization> = {};
