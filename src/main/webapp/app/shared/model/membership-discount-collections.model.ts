import { CollectionType } from 'app/shared/model/enumerations/collection-type.model';

export interface IMembershipDiscountCollections {
  id?: number;
  shop?: string;
  membershipDiscountId?: number;
  collectionId?: number;
  collectionTitle?: string;
  collectionType?: CollectionType;
}

export const defaultValue: Readonly<IMembershipDiscountCollections> = {};
