export interface IOrderInfo {
  id?: number;
  shop?: string;
  orderId?: number;
  linesJson?: any;
}

export const defaultValue: Readonly<IOrderInfo> = {};
