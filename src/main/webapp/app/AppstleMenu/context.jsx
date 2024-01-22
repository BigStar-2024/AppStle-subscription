import React, {useEffect, useState} from 'react'
import {IS_LOCALHOST} from "app/config/api-config";
import {
  getAppstleMenuLabelsByShop,
  getAppstleMenuSettingByShop,
  getShopInformation
} from "./AppstleMenu/reducers/appstle-menu.reducer";
import {useDispatch, useSelector} from "react-redux";
import toast from 'react-hot-toast';
import success from "app/AppstleMenu/assets/success.gif";
import { ApolloProvider, ApolloClient, InMemoryCache } from "@apollo/client";
import PRODUCTS_QUERY from './AppstleMenu/graphql/ProductsQuery';

const initialState = {
  loading: false
};

const MainContext = React.createContext(initialState);

export const useMainContext = () => React.useContext(MainContext);

export const MainProvider = ({children}) => {
  const dispatch = useDispatch();
  const shopifyProducts = useSelector(state => state.appstleMenuCustomerState?.products);  //current page products to show in list
  const shopifyProductsLoading = useSelector(state => state.appstleMenuCustomerState?.loading);
  const hasNextPage = useSelector(state => state.appstleMenuCustomerState?.hasNextPage); // is nex page is available
  const lastCursor = useSelector(state => state.appstleMenuCustomerState?.lastCursor); //current showing products last cursor

  const [loading, setLoading] = useState(false);
  const [shop, setShop] = useState(null);
  const [filterMenuList, setFilterMenuList] = useState(null); // all menu list in header
  const [selectedFilterMenu, setSelectedFilterMenu] = useState(null); //selected menu in header
  const [shopAccessKey, setShopAccessKey] = useState(null); // storefront access token
  const [errorMessage, setErrorMessage] = useState(null);
  const [error, setError] = useState(false);
  const [isCheckingOut, setIsCheckingOut] = useState(false);
  const [adding, setAdding] = useState(false); //adding to cart in progress
  const [productId, setProductId] = useState(null);
  const [filterValues, setFilterValues] = useState([]); //selected items from side bar
  const [initialQuery, setInitialQuery] = useState(""); //initial query for header based filter
  const [filterQuery, setFilterQuery] = useState(PRODUCTS_QUERY); //filter query may be with tags and vendors
  const [appstleMenuLabels, setAppstleMenuLabels] = useState(null); //all labels and custom css
  const [appstleMenuSettings, setAppstleMenuSettings] = useState(null); //all labels and custom css
  const [apolloClient, setApolloClient] = useState(null);

  useEffect(() => {
    if (IS_LOCALHOST) {
      getAppstleMenuSettingByShop().then((response) => {
        const data = response.data;
        setShop(data.shop);
        const filterMenus = data.filterMenu ? JSON.parse(data.filterMenu) : [];
        setAppstleMenuSettings(data);
        setFilterMenuList(filterMenus);
        setSelectedFilterMenu(filterMenus.length > 0 ? filterMenus[0] : null);
        if (filterMenus.length > 0) {
          handleChangeFilterMenu(filterMenus[0], []);
        }
      })
      getShopInformation().then((response) => {
        const data = response.data;
        setShopAccessKey(data.storeFrontAccessToken);
      })

      getAppstleMenuLabelsByShop().then((response) => {
        const data = response.data;
        setAppstleMenuLabels(data);
      })
    } else {
      processAppstleMenuDetails();
    }
  }, [])


  const processAppstleMenuDetails = () => {
    setTimeout(() => {
      if (filterMenuList == null || shopAccessKey == null) {
        setShopAccessKey(window?.appstleMenu?.storeFrontAccessKey);
        const filterMenus = window?.appstleMenu?.menu !== undefined ? JSON.parse(window?.appstleMenu?.menu?.filterMenu) : [];
        const shop = window?.appstleMenu?.menu?.shop || "";
        const menuSettings = window?.appstleMenu?.menu !== undefined ? window?.appstleMenu?.menu : null;
        setAppstleMenuSettings(menuSettings);
        setShop(shop);
        setFilterMenuList(filterMenus);
        setSelectedFilterMenu(filterMenus.length > 0 ? filterMenus[0] : null);
        if (filterMenus.length > 0) {
          handleChangeFilterMenu(filterMenus[0], []);
        }
        const filterMenuLabels = window?.appstleMenu?.appstleMenuLabels !== undefined ? window?.appstleMenu?.appstleMenuLabels : null;
        setAppstleMenuLabels(filterMenuLabels)
      }
    }, [500])
  }

  const toastSuccess = () => toast.custom((t) => (
    <div
      className={`${
        t.visible ? 'as-animate-enter' : 'as-animate-leave'
      } as-max-w-md as-w-full as-bg-white as-shadow-lg as-rounded-lg as-pointer-events-auto as-flex as-ring-1 as-ring-black as-ring-opacity-5`}
    >
      <div className="as-flex-1 as-w-0 as-p-4">
        <div className="as-flex">
          <div>
            <img src={success} alt="" style={{height: 50, width: 50}}/>
          </div>
          <div className="as-ml-3 as-flex as-justify-center as-items-center">
            <p className="as-text-sm as-font-medium as-text-gray-900">
              Product added successfully.
            </p>
          </div>
        </div>
      </div>
      <div className="as-flex as-border-l as-border-gray-200">
        <button
          onClick={() => toast.dismiss(t.id)}
          className="as-w-full as-border as-border-transparent as-rounded-none as-rounded-r-lg as-p-4 as-flex as-items-center as-justify-center as-text-sm as-font-medium as-text-indigo-600 as-hover:text-indigo-500 as-focus:outline-none as-focus:ring-2 as-focus:ring-indigo-500"
        >
          Close
        </button>
      </div>
    </div>
  ))

  const addToCart = (selectedVariant, quantity, sellingPlan) => {
    setAdding(true);
    setProductId(selectedVariant.id);
    let cartItems = {items: []};
    var payloadData = {
      id: selectedVariant.id.split("/").pop(),
      quantity: quantity,
    };
    if (selectedFilterMenu?.menuType === "SUBSCRIBE" && sellingPlan != null) {
      payloadData.selling_plan = sellingPlan.id.split("/").pop()
    }
    cartItems?.items?.push(payloadData);
    let config = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(cartItems)
    };
    fetch(`${location.origin}/cart/add.js`, config)
      .then(res => res.json())
      .catch(err => {
        setErrorMessage(err?.description);
        setError(true);
        setIsCheckingOut(false);
      })
      .then(data => {
        if (data?.items?.length) {
          fetch(`${location.origin}/cart.js`)
            .then(res => res.json())
            .then(async data => {
              setAdding(false);
              setProductId(null);
              document.documentElement.dispatchEvent(
                new CustomEvent('cart:refresh', {
                  bubbles: true,
                })
              );
              const totalQuantity = data.items.reduce((partialSum, a) => partialSum + a.quantity, 0);
              jQuery('.cart-count-bubble').html(totalQuantity);
              toastSuccess();
            });
        } else if (data?.status && data?.status !== 200) {
          setErrorMessage(data?.description);
          setError(true);
          setIsCheckingOut(false);
        }
      })
      .catch(err => {
        console.log(err);
        setIsCheckingOut(false);
        setErrorMessage(err?.description);
        setError(true);
        toast.error('Something went wrong.', {icon: "❌"});
      }).finally(() => {
      document.documentElement.dispatchEvent(
        new CustomEvent('cart:refresh', {
          bubbles: true,
        })
      );
    });
  };


  const handleChangeFilterMenu = (item) => {
    setFilterValues([]);
    setSelectedFilterMenu(item);
    prepareProductSearchQuery(item, []);
  };

  const handleChangeFilterValues = (values) => {
    setFilterValues(values);
    prepareProductSearchQuery(selectedFilterMenu, values);
  };

  const preparedTagFilter = (filters) => {
    const tags = [];
    filters.map((item) => {
      if (item.filterType === "tag_filter") {
        if (tags.length > 0) {
          tags.push(`OR tag:'${item.filterValue}'`);
        } else {
          tags.push(`tag:'${item.filterValue}'`);
        }
      }
    });
    return tags;
  }

  const preparedVendorFilter = (filters) => {
    const tags = [];
    filters.map((item) => {
      if (item.filterType === "vendor_filter") {
        if (tags.length > 0) {
          tags.push(`OR vendor:'${item.filterValue}'`);
        } else {
          tags.push(`vendor:'${item.filterValue}'`);
        }
      }
    });
    return tags;
  }

  const prepareProductSearchQuery = (filterMenu, sideFilters) => {
    const primaryQuery = `${filterMenu?.menuType === "ONE_TIME" ? "tag:APPSTLE_ONE_TIME" + filterMenu.sourceCollection : filterMenu?.menuType === "SUBSCRIBE" ? "tag:APPSTLE_SUBSCRIBE" + filterMenu.subscriptionGroup : filterMenu?.menuType === "BUNDLE" ? "tag:'APPSTLE_BUNDLE'" : ""}`;
    const tags = preparedTagFilter(sideFilters);
    const vendors = preparedVendorFilter(sideFilters);
    let creatingQuery = [
      `${primaryQuery}${tags.length > 0 ? ` AND ${tags.join('')}` : ""}`,
      `${vendors.length > 0 ? ` AND  ${vendors.join('')}` : ""}`
    ];
    setFilterQuery(creatingQuery.join(''));
    setInitialQuery(primaryQuery);
  };

  useEffect(() => {
   const  apolloClient = new ApolloClient({
      uri: "https://"+shop+"/api/2023-07/graphql",
      headers: {
        'X-Shopify-Storefront-Access-Token': (shopAccessKey || window?.appstleMenu?.storeFrontAccessKey || window?.storeFrontAccessKey)
      },
      cache: new InMemoryCache()
    });
    setApolloClient(apolloClient);
  }, [shop, shopAccessKey])


  return (
    <MainContext.Provider
      value={{
        loading,
        shop,
        filterMenuList,
        selectedFilterMenu,
        setSelectedFilterMenu,
        shopAccessKey,
        shopifyProducts,
        shopifyProductsLoading,
        addToCart,
        adding,
        productId,
        filterValues,
        setFilterValues,
        hasNextPage,
        lastCursor,
        setInitialQuery,
        initialQuery,
        filterQuery,
        setFilterQuery,
        appstleMenuLabels,
        setAppstleMenuLabels,
        handleChangeFilterMenu,
        handleChangeFilterValues,
        appstleMenuSettings
      }}>
      { apolloClient &&
        <ApolloProvider client={apolloClient}>
          {children}
        </ApolloProvider>
      }
    </MainContext.Provider>
  )
}
