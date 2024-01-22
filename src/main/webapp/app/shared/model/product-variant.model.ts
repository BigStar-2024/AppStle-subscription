interface Product {
  id: string;
  title: string;
  imageId: string;
  imageSrc: string;
  variants: Variant[];
}

interface Variant {
  id: string;
  price: string;
  title: string;
  displayName: string;
  selectedOptions: selectedOption[];
}

interface selectedOption {
  value: string;
  name: boolean;
}

interface PageInfo {
  cursor: string;
  hasNextPage: boolean;
}
export interface IProductVariant {
  products?: Product[];
  pageInfo?: PageInfo;
}
export const defaultValue: Readonly<IProductVariant> = {};
