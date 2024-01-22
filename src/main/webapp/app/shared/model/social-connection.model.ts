export interface ISocialConnection {
  id?: number;
  userId?: string;
  proverId?: string;
  accessToken?: string;
  restRateLimit?: number;
  graphqlRateLimit?: number;
  accessToken1?: string;
  accessToken2?: string;
  accessToken3?: string;
  accessToken4?: string;
  publicAccessToken?: string;
  publicAccessToken1?: string;
  publicAccessToken2?: string;
  publicAccessToken3?: string;
  publicAccessToken4?: string;
}

export const defaultValue: Readonly<ISocialConnection> = {};
