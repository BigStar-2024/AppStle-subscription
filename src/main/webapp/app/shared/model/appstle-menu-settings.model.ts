import { ProductViewSettings } from 'app/shared/model/enumerations/product-view-settings.model';

export interface IAppstleMenuSettings {
  id?: number;
  shop?: string;
  filterMenu?: any;
  menuUrl?: string;
  menuStyle?: any;
  active?: boolean;
  handle?: string;
  productViewStyle?: ProductViewSettings;
}

export const defaultValue: Readonly<IAppstleMenuSettings> = {
  active: false,
  menuStyle: '',
  productViewStyle: ProductViewSettings.QUICK_ADD
};
