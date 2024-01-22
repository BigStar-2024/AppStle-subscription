export interface IProductCycle {
  id?: number;
  shop?: string;
  sourceProduct?: string;
  destinationProducts?: string;
}

export const defaultValue: Readonly<IProductCycle> = {};
