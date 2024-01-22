export interface IRuleCriteria {
  id?: number;
  shop?: string;
  name?: string;
  identifier?: string;
  type?: string;
  group?: string;
  fields?: string;
}

export const defaultValue: Readonly<IRuleCriteria> = {};
