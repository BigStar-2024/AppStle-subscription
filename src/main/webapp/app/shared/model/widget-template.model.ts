import { WidgetTemplateType } from 'app/shared/model/enumerations/widget-template-type.model';

export interface IWidgetTemplate {
  id?: number;
  type?: WidgetTemplateType;
  title?: string;
  html?: any;
}

export const defaultValue: Readonly<IWidgetTemplate> = {};
