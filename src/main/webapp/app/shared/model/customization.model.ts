import { CustomizationType } from 'app/shared/model/enumerations/customization-type.model';
import { CustomizationCategory } from 'app/shared/model/enumerations/customization-category.model';

export interface ICustomization {
  id?: number;
  label?: string;
  type?: CustomizationType;
  css?: any;
  category?: CustomizationCategory;
}

export const defaultValue: Readonly<ICustomization> = {};
