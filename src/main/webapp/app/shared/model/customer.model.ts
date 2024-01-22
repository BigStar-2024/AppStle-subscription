export interface ICustomers {
  customers: Customer[];
  pageInfo: PageInfo;
}

export const defaultValue: Readonly<ICustomers> = {
  customers: [],
  pageInfo: { hasNextPage: false, cursor: '' }
};

interface PageInfo {
  cursor?: any;
  hasNextPage: boolean;
}

interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  displayName: string;
  email: string;
  subscriptionCount: number;
  nextOrderDate: string;
  totalOrderCount: number;
}
