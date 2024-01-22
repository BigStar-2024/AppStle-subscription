export interface IProductField {
  id?: number;
  shop?: string;
  title?: string;
  enabled?: boolean;
}

export const defaultValue: Readonly<IProductField> = {
  enabled: false
};
