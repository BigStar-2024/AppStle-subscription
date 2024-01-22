interface Product {
  id: string;
  title: string;
  imageId: string;
  imageSrc: string;
  variants: [];
}
interface PageInfo {
  cursor: string;
  hasNextPage: boolean;
}
export interface IProduct {
  products?: Product[];
  pageInfo?: PageInfo;
}

export const defaultValue: Readonly<IProduct> = {};
