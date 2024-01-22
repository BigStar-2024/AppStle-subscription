// @ts-ignore
import Client from 'shopify-buy/index.unoptimized.umd';
import axios from 'axios';
import {ApolloClient, InMemoryCache} from "@apollo/client";
import PRODUCTS_QUERY from '../graphql/ProductsQuery';


const apolloConnection = storeFrontAccesKey => {
    storeFrontAccesKey = storeFrontAccesKey || window?.appstleMenu?.storeFrontAccessKey || window?.storeFrontAccessKey
    const domain = window?.Shopify?.shop || 'appstle-jaydeep-35.myshopify.com';
    return new ApolloClient({
        uri: "https://"+domain+"/api/2023-07/graphql",
        headers: {
          'X-Shopify-Storefront-Access-Token': storeFrontAccesKey
        },
        cache: new InMemoryCache()
      });
};

const connection = storeFrontAccesKey => {
    const domain = window?.Shopify?.shop || 'appstle-menu-demo-1.myshopify.com';
    return Client.buildClient({
        domain: domain,
        storefrontAccessToken: storeFrontAccesKey
    });
};

export const ACTION_TYPE = {
    HAS_NEXT_PAGE: 'shopify/HAS_NEXT_PAGE',
    LAST_CURSOR: 'shopify/LAST_CURSOR',
    PRODUCTS_FOUND: 'shopify/PRODUCTS_FOUND',
    PRODUCTS_TAGS: 'shopify/TAGS',
    VENDORS: 'shopify/VENDORS',
    COLLECTION_FOUND: 'shopify/COLLECTION_FOUND',
    LOADER_RUNNING: 'shopify/LOADER_RUNNING',
    LOADER_STOP: 'shopify/LOADER_STOP',
    ADD_VARIANT_TO_CART: 'shopify/ADD_VARIANT_TO_CART'
};

const initialState = {
    loading: false,
    error: undefined,
    products: [],
    allCollection: [],
    tags: [],
    vendors: [],
    cartItems: [],
    count: 10
};

export default (state = initialState, action) => {
    switch (action.type) {
        case ACTION_TYPE.HAS_NEXT_PAGE: {
            return {...state, hasNextPage: action.payload};
        }
        case ACTION_TYPE.LAST_CURSOR: {
            return {...state, lastCursor: action.payload};
        }
        case ACTION_TYPE.PRODUCTS_FOUND: {
            return {...state, products: action.reset ? action.payload : [...state.products, ...action.payload]};
        }
        case ACTION_TYPE.PRODUCTS_TAGS: {
            return {...state, tags: action.payload};
        }
        case ACTION_TYPE.VENDORS: {
            return {...state, vendors: action.payload};
        }
        case ACTION_TYPE.COLLECTION_FOUND: {
            return {...state, allCollection: action.payload};
        }
        case ACTION_TYPE.ADD_VARIANT_TO_CART: {
            return {...state, cartItems: action.payload};
        }
        case ACTION_TYPE.LOADER_RUNNING: {
            return {...state, loading: true};
        }
        case ACTION_TYPE.LOADER_STOP: {
            return {...state, loading: false};
        }

        default:
            return state;
    }
};

const startLoading = () => {
    return dispatch => {
        dispatch({
            type: ACTION_TYPE.LOADER_RUNNING,
            payload: true
        });
    };
};

const stopLoading = () => {
    return dispatch => {
        dispatch({
            type: ACTION_TYPE.LOADER_STOP,
            payload: false
        });
    };
};

export const getProductsByTags = (storeFrontAccesKey, queryNames, after, reset) => {
    storeFrontAccesKey = storeFrontAccesKey || window?.appstleMenu?.storeFrontAccessKey || window?.storeFrontAccessKey
    const client = apolloConnection(storeFrontAccesKey);
    return dispatch => {
        dispatch(startLoading());
        client.query({
            query: PRODUCTS_QUERY,
            variables: { after: after, first: 8, query: queryNames}
        }).then((response) => {
            dispatch(stopLoading());
            const products =  response?.data?.products?.edges;
            const hasNextPage = response?.data?.products?.pageInfo?.hasNextPage;
            const lastCursor = response?.data?.products?.pageInfo?.endCursor;

            dispatch({
                type: ACTION_TYPE.HAS_NEXT_PAGE,
                payload: hasNextPage
            });
            dispatch({
                type: ACTION_TYPE.LAST_CURSOR,
                payload: lastCursor
            });
            dispatch({
                type: ACTION_TYPE.PRODUCTS_FOUND,
                reset: reset,
                payload: products.length > 0 ? products : []
            });
        })
        .catch((error) => {
            dispatch(stopLoading());
            console.error("Error while fetching products: " + JSON.stringify(error));
        });
    };
};

const getTags = () => {
    const productsQuery = client.graphQLClient.query(root => {
        root.addConnection('products', {args: {first: 10}}, product => {
            product.add('title');
            product.add('tags');
        });
    });

    return dispatch => {
        client.graphQLClient
            .send(productsQuery)
            .then(({model, data}) => {
                dispatch(stopLoading());
                const {products} = model;
                let tags = [];
                products?.map(item =>
                    item.tags.map(subITem => {
                        if (!tags.includes(subITem.value)) {
                            tags.push(subITem.value);
                        }
                    })
                );
                if (tags?.length) {
                    dispatch({
                        type: ACTION_TYPE.PRODUCTS_TAGS,
                        payload: tags
                    });
                }
            })
            .catch(err => dispatch(stopLoading()));
    };
};

const getVendors = () => {
    const productsQuery = client.graphQLClient.query(root => {
        root.addConnection('products', {args: {first: 10}}, product => {
            product.add('title');
            product.add('vendor');
        });
    });
    return dispatch => {
        client.graphQLClient
            .send(productsQuery)
            .then(({model, data}) => {
                dispatch(stopLoading());
                const {products} = model;
                let vendors = [];
                products?.map(item => {
                    if (!vendors.includes(item.vendor)) {
                        vendors.push(item.vendor);
                    }
                });
                if (vendors?.length) {
                    dispatch({
                        type: ACTION_TYPE.VENDORS,
                        payload: vendors
                    });
                }
            })
            .catch(err => dispatch(stopLoading()));
    };
};

const getCollection = () => {
    return dispatch => {
        client.collection.fetchAllWithProducts().then(collections => {
            dispatch(stopLoading());
            dispatch({
                type: ACTION_TYPE.COLLECTION_FOUND,
                payload: collections?.map(item => {
                    return {products: item.products, title: item.title, id: item.id};
                })
            });
        });
    };
};

const addVariantToCart = lineItemsToAdd => {
    const checkoutId = 'Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0SW1hZ2UvMTgyMTc3ODc1OTI=';
    return async dispatch => {
        const response = await client.checkout.addLineItems(checkoutId, lineItemsToAdd);
        // console.log(response, 'response')
    };
};
export const getAppstleMenuSettingByShop = async () => {
    return await axios.get('/api/appstle-menu-settings/by-shop');
};

export const getShopInformation = async () => {
    return await axios.get('/api/shop-infos/shop-infos-current-login');
};

export const getAppstleMenuLabelsByShop = async () => {
    return await axios.get('/api/appstle-menu-labels/shop');
};

export const createCheckout = storeFrontAccesKey => {
    const client = connection(storeFrontAccesKey);
    client.checkout.create().then(checkout => {
        const checkoutId = checkout.id; // ID of an existing checkout
        const lineItemsToAdd = [
            {
                variantId: 'gid://shopify/ProductVariant/41852973121718',
                quantity: 5
            }
        ];
        client.checkout.addLineItems(checkoutId, lineItemsToAdd).then(checkout => {
            // console.log(checkout.lineItems);
        });
    });
};
