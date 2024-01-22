import { gql } from '@apollo/client';

const PRODUCTS_QUERY = gql`
query ProductsQuery ($after: String, $first: Int!, $query: String) {
  products(after: $after, first: $first, query: $query) {
    pageInfo {
      hasNextPage
      hasPreviousPage
      endCursor
    }
    edges {
      cursor
      node {
        availableForSale
        compareAtPriceRange {
          maxVariantPrice {
            amount
            currencyCode
          }
          minVariantPrice {
            amount
            currencyCode
          }
        }
        description
        featuredImage {
          url
        }
        handle
        id
        options {
          id
          name
          values
        }
        priceRange {
          maxVariantPrice {
            amount
            currencyCode
          }
          minVariantPrice {
            amount
            currencyCode
          }
        }
        productType
        requiresSellingPlan
        sellingPlanGroups(first: 50) {
          pageInfo {
            hasNextPage
            hasPreviousPage
          }
          edges {
            cursor
            node {
              appName
              name
              options {
                name
                values
              }
              sellingPlans(first: 50) {
                pageInfo {
                  hasNextPage
                  hasPreviousPage
                }
                edges {
                  cursor
                  node {
                    description
                    id
                    name
                    options {
                      name
                      value
                    }
                    recurringDeliveries
                  }
                }
              }
            }
          }
        }
        tags
        title
        totalInventory
        vendor
        variants(first: 100) {
          pageInfo {
            hasNextPage
            hasPreviousPage
          }
          edges {
            cursor
            node {
              price {
                amount
                currencyCode
              }
              availableForSale
              compareAtPrice {
                amount
                currencyCode
              }
              currentlyNotInStock
              id
              quantityAvailable
              selectedOptions {
                name
                value
              }
              sellingPlanAllocations(first: 50) {
                pageInfo {
                  hasNextPage
                  hasPreviousPage
                }
                edges {
                  cursor
                  node {
                    sellingPlan {
                      description
                      id
                      name
                      options {
                        name
                        value
                      }
                      recurringDeliveries
                    }
                    priceAdjustments {
                      compareAtPrice {
                        amount
                        currencyCode
                      }
                      perDeliveryPrice {
                        amount
                        currencyCode
                      }
                      price {
                        amount
                        currencyCode
                      }
                      unitPrice {
                        amount
                        currencyCode
                      }
                    }
                  }
                }
              }
              title
              unitPrice {
                amount
                currencyCode
              }
              image {
                src
                url(transform: {})
                originalSrc
              }
            }
          }
        }
      }
    }
  }
}
`;

export default PRODUCTS_QUERY;
