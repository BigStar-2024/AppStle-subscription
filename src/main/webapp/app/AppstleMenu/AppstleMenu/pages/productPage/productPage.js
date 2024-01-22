import React, {useEffect, useState} from 'react';
import SideMenu from './sideMenu/side-menu';
import ProductCard from './productCard/productCard';
import {connect} from 'react-redux';
import {useMainContext} from 'app/AppstleMenu/context';
import { getProductsByTags} from 'app/AppstleMenu/AppstleMenu/reducers/appstle-menu.reducer';
import Drawer from 'app/AppstleMenu/AppstleMenu/pages/productPage/drawer/Drawer';
import ProductSkeletonLoader from "app/AppstleMenu/AppstleMenu/Components/productSkeletonLoader";

const ProductPage = props => {
  const {
    filterMenuList,
    selectedFilterMenu,
    setSelectedFilterMenu,
    shopAccessKey,
    shopifyProducts,
    shopifyProductsLoading,
    hasNextPage,
    lastCursor,
    setInitialQuery,
    initialQuery,
    filterQuery,
    setFilterQuery,
    appstleMenuLabels
  } = useMainContext();
  const [cartOpen, setCartOpen] = useState(false);
  const [loadMore, setLoadMore] = useState(8);
  const [productLoading, setProductLoading] = useState(false);
  const [cartProducts, setCartProducts] = useState([]);
  const [products, setProducts] = useState([]);
  const [showSidebar, setShowSidebar] = useState(false);
  const [skeletonCount, setSkeletonCount] = useState([]);

  useEffect(() => {
    if (selectedFilterMenu?.filterGroups?.length > 0) {
      setSkeletonCount([1, 2, 3, 4]);
    } else {
      setSkeletonCount([1, 2, 3, 4]);
    }
  }, [selectedFilterMenu]);

  const filterProductHandler = (filterQuery, after, reset) => {
    //if (shopAccessKey) {
      props.getProductsByTags(shopAccessKey, filterQuery, after, reset);
    //}
  };

  useEffect(() => {
      filterProductHandler(filterQuery, null, true);
  }, [shopAccessKey, selectedFilterMenu, filterQuery]);

  useEffect(() => {
    if (shopifyProducts?.length) {
      setProductLoading(false);
      setProducts([...shopifyProducts]);
    }
  }, [shopifyProducts]);
  useEffect(() => {
    if (cartProducts?.length === 0) {
      setCartOpen(false);
    }
  }, [cartProducts]);

  return (
    <div className="as-container as-mx-auto xl:as-flex lg:as-flex md:as-flex">
      {selectedFilterMenu?.filterGroups?.length ? (
        <>
          <Drawer showSidebar={showSidebar} setShowSidebar={setShowSidebar}>
            <div className="as-py-4">
              <SideMenu/>
            </div>
          </Drawer>

          <div
            className="as-p-4 as-hidden sm:as-hidden md:as-block lg:as-block xl:as-block 2xl:as-block"
            style={{minWidth: 250, maxWidth: 250}}
          >
            <SideMenu/>
          </div>
        </>
      ) : (
        ''
      )}

      <div className="as-p-4 as-w-full">
        <div
          className="as-mb-2 as-py-2 as-flex as-justify-between as-text-2xm as-font-bold as-tracking-tight as-text-gray-900">
          <span className="as-ml-2 as-ext-sm as-font-medium as-text-black">
            {' '}
            {shopifyProducts.length > 0 ? `Results ${shopifyProducts.length}` : 'Results 0'}
          </span>
          {selectedFilterMenu?.filterGroups?.length > 0 ? (
            <div
              className="as-cursor-pointer as-block sm:as-block md:as-hidden lg:as-hidden xl:as-hidden 2xl:as-hidden"
              onClick={() => setShowSidebar(true)}
            >
              <svg
                stroke="currentColor"
                fill="#2563EB"
                strokeWidth="0"
                viewBox="0 0 24 24"
                height="27"
                width="27"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path fill="none" d="M0 0h24v24H0z"/>
                <path d="M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z"/>
              </svg>
            </div>
          ) : (
            ''
          )}
        </div>

        {shopifyProductsLoading ? <ProductSkeletonLoader skeletonCount={skeletonCount}/> : (
          <>
            <div
              className="as-grid as-grid-cols-2 xs:as-grid-cols-1 sm:as-grid-cols-2 md:as-grid-cols-2 lg:as-grid-cols-2 xl:as-grid-cols-2 2xl:as-grid-cols-2 as-gap-5 as-place-items-center">
              {shopifyProducts?.map((item, index) => {
                return <ProductCard key={index} product={item?.node}/>;
              })}
            </div>
            {hasNextPage ? (
              <div className="as-flex as-justify-center as-mt-5">
                <div style={{maxWidth: '150px', width: '100%'}}>
                  <button
                    type="button"
                    onClick={() => filterProductHandler(filterQuery, lastCursor, false)}
                    className="as-mt-1 as-mb-1 as-text-center as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary  appstle-show-more"
                  >
                    {appstleMenuLabels?.seeMore || "Show more"}
                  </button>
                </div>
              </div>
            ) : null}

            {(!shopifyProducts.length || shopifyProducts.length === 0) && (
              <div className="as-text-center">
                <h1
                  className="as-text-2xl as-antialiased as-font-medium as-text-gray-700">{appstleMenuLabels?.noDataFound || "No Data Found!"} </h1>
              </div>
            )}</>
        )}
      </div>
    </div>
  );
};
const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getProductsByTags
};

export default connect(mapStateToProps, mapDispatchToProps)(ProductPage);
