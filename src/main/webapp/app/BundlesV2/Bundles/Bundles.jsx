import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import './style.scss';
import ProductSelection from './ProductSelection';
import axios from 'axios';
import _ from 'lodash';
import 'react-toastify/dist/ReactToastify.css';
import {getToken} from './Bundle.util';
import {getEntityByShop} from 'app/entities/subscription-bundle-settings/subscription-bundle-settings.reducer';
import Loader from './Loader';
import {getShopInfoByCurrentLogin} from "app/entities/shop-info/shop-info.reducer";
import SingleProduct from "./SingleProduct";
import { ArrowLeft, ArrowRight, KeyboardArrowLeft } from "@mui/icons-material";
import Select from 'react-select';

const queryString = require('query-string');

const Bundles = props => {
    const [bundleSlug,setBundleSlug] = useState(getToken());
    const [products, setProducts] = useState([]);
    const [bundleData, setBundleData] = useState({bundle: {minProductCount: 5}});
    const [loaded, setLoaded] = useState(false);
    const [singleProductSettings, setSingleProductSettings] = useState([]);

    const [selectedSingleProduct, setSelectedSingleProduct] = useState(null)
    const [selectedSingleProductBundleData, setSelectedSingleProductBundleData] = useState(null);

    const [showProductSelectionScreen, setShowProductSelectionScreen] = useState(true);

    const [isBundleTypeClassic, setIsBundleTypeClassic] = useState(true);

    const [selectedProductSellingPlans, setSelectedProductSellingPlans] = useState([])

    const [isReviewBundleModalOpen, setIsReviewBundleModalOpen] = useState(false)

    const [isNextButtonValid, setIsNextButtonValid] = useState(true);

    const [selectedMasterProductData, setSelectedMasterProductData] = useState({})
    const [singleProductAllData, setSingleProductAllData] = useState([])

    const [page, setPage] = useState(1);
    const [loadingProducts, setLoadingProducts] = useState(false);
    const [pageSize, setPageSize] = useState(100);

    const [redirectHistoryForIframe, setRedirectHistoryForIframe] = useState([]);
    const [isClickGoBackButton, setIsClickGoBackButton] = useState(false)

    const [iframeSelectedFilterData, setIframeSelectedFilterData] = useState({ vendor: [], tags: [], productType: [] });

    useEffect(() => {
        if (!showProductSelectionScreen) {
            setIsNextButtonValid(true)
        }

        if (showProductSelectionScreen) {
            document.querySelector('html').classList.add('appstle-product-selection-screen')
          } else {
            document.querySelector('html').classList.remove('appstle-product-selection-screen')
          }
    }, [showProductSelectionScreen])

    useEffect(() => {
        if (isReviewBundleModalOpen) {
          document.querySelector('html').classList.add('appstle-review-screen')
        } else {
          document.querySelector('html').classList.remove('appstle-review-screen')
        }
      }, [isReviewBundleModalOpen])


    useEffect(() => {
        if (selectedSingleProduct) {
            let currentBundleData = singleProductSettings.filter(item => {
                return item?.sourceProduct?.id === selectedSingleProduct?.id
            }).pop();

            setProducts(currentBundleData?.products);
            setSelectedSingleProductBundleData(currentBundleData);
            getSelectedSingleProductData(currentBundleData?.products);
        }
    }, [selectedSingleProduct]);

    const fetchProductData = async (handle) => {
        try {
            const response = await fetch(`${Shopify?.routes?.root || '/'}products/${handle}.js`);
            if (!response.ok) {
                throw new Error('Failed to fetch product data');
            }
            return response.json();
        } catch (error) {
            console.error(`Error fetching product data for handle ${handle}:`, error);
            throw error;
        }
    };

    const updateProductWithMatchingData = (product, matchingData) => {
        return {
            ...product,
            vendor: matchingData.vendor,
            type: matchingData.type,
            tags: matchingData.tags,
            price: matchingData.price,
            variants: matchingData.variants
        };
    };

    const [isShowVariantDropdown, setIsShowVariantDropdown] = useState(false);

    const getSelectedSingleProductData = async (products, props) => {
        if (!products || products.length === 0) {
            return;
        }

        // Extract product handles and filter out null values
        const productHandles = [...new Set(products.map(product => product?.productHandle).filter(Boolean))];
        if (productHandles.length === 0) {
            return;
        }

        try {
            // Fetch data for all product handles in parallel
            const productDataPromises = productHandles.map(fetchProductData);
            const productData = await Promise.all(productDataPromises);

            let updatedProducts = [];

            if (isShowVariantDropdown) {
                for (let index = 0; index < productHandles.length; index++) {
                    const handle = productHandles[index];
                    const matchingProductData = productData.find(data => data.handle === handle);
                    const prd = products.filter(p => p.productHandle === handle);
                    const onlyProduct = prd.find(p => p.type === 'PRODUCT');

                    if (onlyProduct) {
                        updatedProducts.push(updateProductWithMatchingData(onlyProduct, matchingProductData));
                    } else {
                        const variants = prd.filter(p => p.type === 'VARIANT');
                        const variantIds = variants.map(v => v.id);
                        const filteredVariants = matchingProductData.variants.filter(v => variantIds.includes(v.id));
                        updatedProducts.push({
                            ...variants[0],
                            title: matchingProductData.title,
                            ...updateProductWithMatchingData({}, matchingProductData),
                            variants: filteredVariants
                        });
                    }
                }
            } else {
                // Update product data with additional information
                updatedProducts = products.map(p => {
                    const matchingProductData = productData.find(data => data.handle === p.productHandle);
                    return matchingProductData ? updateProductWithMatchingData(p, matchingProductData) : p;
                });
            }

            // Update the state or perform any other necessary actions
            setProducts(updatedProducts);
        } catch (error) {
            // Handle errors, e.g., network issues, JSON parsing errors
            console.error('Error fetching and updating product data:', error);
        }
    };


    useEffect(() => {
        let firstAvailableItem = null;
        if (singleProductAllData.length) {
            singleProductSettings?.forEach(item => {
                singleProductAllData.forEach(prd => {
                    if (!firstAvailableItem && (item?.sourceProduct?.id === prd?.id) && (prd.available)) {
                        firstAvailableItem = prd;
                    }
                })
            })
            // firstAvailableItem = (singleProductAllData.filter(item => item?.available))[0]
            if (firstAvailableItem) {
                setSelectedSingleProduct(firstAvailableItem);
            } else {
                setIsNextButtonValid(false)
            }
           // setTimeout(() => setSelectedSingleProduct(singleProductSettings[0].sourceProduct), 500)
        }
    }, [singleProductAllData])

    useEffect(() => {
        props.getEntityByShop(Shopify.shop);
        props.getShopInfoByCurrentLogin();
        axios.get(`/api/v3/subscription-bundlings/external/get-bundle/${bundleSlug}`).then(async resp => {
            let response = resp.data;
            setBundleData(response);
            let processes = [];
            if (response?.bundle?.buildABoxType !== "SINGLE_PRODUCT") {
                setLoadingProducts(true)
                _.each(response.products.slice(0, ((page * pageSize < response.products?.length) ? (page * pageSize) : response.products?.length)), prod => {
                    if (Shopify.shop === 'biltongsh.myshopify.com') {
                        processes.push(fetch(`${Shopify?.routes?.root || '/'}products/${prod.productHandle}.js`));
                    } else {
                        processes.push(fetch(`${Shopify?.routes?.root || '/'}products/${prod.productHandle}.js`));
                    }
                });
                let productListData = [];
                let results = await Promise.all(processes);
                let validResults = results.filter(response => response.ok);
                validResults = await Promise.all(validResults.map(result => result.json()));
                productListData = _.map(validResults, o => ({product: o}));
                setProducts(productListData);
                setLoaded(true);
                setLoadingProducts(false)

            } else {
                console.log(JSON.parse(response?.bundle?.singleProductSettings))
                setSingleProductSettings(JSON.parse(response?.bundle?.singleProductSettings));
                setShowProductSelectionScreen(false)
                setIsBundleTypeClassic(false)
            }
        });
    }, [bundleSlug, page, pageSize]);

    useEffect(() => {
        setPageSize((props?.subscriptionBundleSettingsEntity?.numberOfProductsPerPageV2 ? parseInt(props?.subscriptionBundleSettingsEntity?.numberOfProductsPerPageV2) : 20))
        if (props?.subscriptionBundleSettingsEntity?.isMergeIntoSingleBABVariantDropdown) {
            setIsShowVariantDropdown(props?.subscriptionBundleSettingsEntity?.isMergeIntoSingleBABVariantDropdown);   
        }
    }, [props?.subscriptionBundleSettingsEntity])

    const setSlug = (token) => {
        setBundleSlug(token)
    }

    const [productFilterConfig, setProductFilterConfig] = useState(null);

    useEffect(() => {
    if (props?.shopInfo?.buildBoxVersion === "V2_IFRAME" && props?.subscriptionBundleSettingsEntity?.productFilterConfig) {
        setProductFilterConfig(JSON.parse(props?.subscriptionBundleSettingsEntity?.productFilterConfig));
    }
    }, [props?.subscriptionBundleSettingsEntity]);

    const FilterDropdown = data => {
    const { item } = data?.data;
    return (
        <>
            <Select
                options={item}
                isMulti={data?.data?.basedOn != 'sorting'}
                placeholder={data?.data?.title}
                value={iframeSelectedFilterData[data?.data?.basedOn]}
                onChange={e => {
                onChangeFilterData(e, data?.data?.basedOn);
                }}
                className="as-mt-5  as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500   focus:as-border-blue-500 as-block as-w-full dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 as-text-black dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
            />
        </>
    );
    };

    const onChangeFilterData = (value, filterType) => {
        setIframeSelectedFilterData(prevFilterData => ({
          ...prevFilterData,
          [filterType]: filterType === 'sorting' ? [value] : value,
        }));
    };
    return (
        <div className='as-bundle-html-wrapper'>
            <div className={`${bundleSlug} appstle_bundle ${(!isBundleTypeClassic || (props?.shopInfo?.buildBoxVersion === "V2_IFRAME")) && `as-bg-gray-100 as-py-8`}`}>
            {((bundleData?.bundle?.buildABoxType === "SINGLE_PRODUCT") || (props?.shopInfo?.buildBoxVersion === "V2_IFRAME")) &&
            <div className="as-container as-mx-auto as-px-4 as-text-center as-pb-5">
                <div className="as-flex as-justify-between as-items-center as-flex-col lg:as-flex-row">
                    <div className="as-breadcrumb-wrapper">
                            <nav className="as-flex" aria-label="Breadcrumb">
                                <ol className="as-inline-flex as-items-center as-space-x-1 md:as-space-x-3">
                                    {!isBundleTypeClassic && <li className="as-inline-flex as-items-center">
                                    <a href="javascript:;"  onClick={() => {
                                          setShowProductSelectionScreen(false)
                                          setLoaded(false)
                                          setIsReviewBundleModalOpen(false)
                                    }} className={`as-inline-flex as-items-center as-text-sm font-medium ${!showProductSelectionScreen ? `as-text-indigo-600` : `as-text-gray-700 hover:as-text-gray-900`}`}>
                                        <svg className="as-w-4 as-h-4 as-mr-2" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"></path></svg>
                                        {props?.subscriptionBundleSettingsEntity?.selectPlanTextV2 || 'Select Plan'}
                                    </a>
                                    </li>}
                                    <li className="as-inline-flex as-items-center">
                                    {isBundleTypeClassic && <svg className="as-w-4 as-h-4 as-mr-2" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"></path></svg>}
                                    {!isBundleTypeClassic && <svg class="as-w-6 as-h-6 as-text-gray-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"></path></svg>}
                                    <a href="javascript:;" onClick={() => {
                                          setShowProductSelectionScreen(true)
                                          setLoaded(true)
                                          setIsReviewBundleModalOpen(false)
                                    }} className={`as-inline-flex as-items-center as-text-sm font-medium ${(showProductSelectionScreen && !isReviewBundleModalOpen) ? `as-text-indigo-600` : `as-text-gray-700 hover:as-text-gray-900`}`}>
                                       {bundleData?.bundle?.chooseProductsText || props?.subscriptionBundleSettingsEntity?.chooseProductsTextV2 || 'Choose Products'}
                                    </a>
                                    </li>
                                    <li className="as-inline-flex as-items-center">
                                    <svg class="as-w-6 as-h-6 as-text-gray-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"></path></svg>
                                    <a href="javascript:;" className={`as-inline-flex as-items-center as-text-sm font-medium ${(showProductSelectionScreen && isReviewBundleModalOpen) ? `as-text-indigo-600` : `as-text-gray-700 hover:as-text-gray-900`}`}>
                                    {props?.subscriptionBundleSettingsEntity?.reviewBundleTextV2 || 'Review Bundle'}

                                    </a>
                                    </li>
                                </ol>
                            </nav>
                    </div>
                    <div className="as-flex as-text-center as-justify-center as-mt-6 lg:as-mt-0 as-items-center">
                        {(showProductSelectionScreen && (bundleData?.bundle?.buildABoxType === "SINGLE_PRODUCT")) && <p onClick={() => {
                            if (!isReviewBundleModalOpen) {
                                setShowProductSelectionScreen(false);
                                setIsReviewBundleModalOpen(false)
                            } else if (isReviewBundleModalOpen) {
                                setShowProductSelectionScreen(true);
                                setIsReviewBundleModalOpen(false)
                            }
                        }}
                            class="as-mx-2 as-text-center as-cursor-pointer as-text-indigo-600 as-text-sm as-cta as-cta_modal-close">
                            <ArrowLeft/>
                            {props?.subscriptionBundleSettingsEntity?.previousStepButtonTextV2 || 'Previous Step'}
                        </p>}
                        {(isReviewBundleModalOpen && (bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT") && (props?.shopInfo?.buildBoxVersion === "V2_IFRAME")) &&  <p onClick={() => {
                            setShowProductSelectionScreen(true);
                            setIsReviewBundleModalOpen(false)
                        }}
                            class="as-mx-2 as-text-center as-cursor-pointer as-text-indigo-600 as-text-sm as-cta as-cta_modal-close">
                            <ArrowLeft/>
                            {props?.subscriptionBundleSettingsEntity?.previousStepButtonTextV2 || 'Previous Step'}
                        </p>}
                        {!isReviewBundleModalOpen && (
                            <>
                                <button
                                    disabled={!isNextButtonValid}
                                    onClick={() => {
                                        if (showProductSelectionScreen) {
                                            setIsReviewBundleModalOpen(true);
                                        } else {
                                            setShowProductSelectionScreen(true);
                                            setLoaded(true);
                                        }
                                    }}
                                    className="as-mx-2 first-line:as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                                >
                                    {props?.subscriptionBundleSettingsEntity?.nextStepButtonTextV2 || 'Next Step'}
                                    <ArrowRight />
                                </button>
                                {redirectHistoryForIframe && redirectHistoryForIframe?.length > 0 && (
                                    <button
                                        class="appstle-bab-go-back-button as-flex as-items-center"
                                        onClick={() => {
                                            setIsClickGoBackButton(true);
                                        }}
                                    >
                                        <KeyboardArrowLeft style={{ height: '18px' }} /> {props?.subscriptionBundleSettingsEntity?.goBackButtonText || `Go Back`}
                                    </button>
                                )}
                            </>
                        )}
                    </div>
                </div>
                {(props?.shopInfo?.buildBoxVersion === "V2_IFRAME" && productFilterConfig && productFilterConfig?.enabled && bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT" && !isReviewBundleModalOpen) && (
                    <div className={`as-flex as-gap-2 filterDropdown as-text-left`}>
                        {productFilterConfig && productFilterConfig?.filters?.map(filter => (
                            <div className='as-pt-4 filterDropdownWidth'>
                                <FilterDropdown data={filter} />
                            </div>
                        ))}
                    </div>
                )}
            </div>}

            {!showProductSelectionScreen && <div className="as-grid as-items-center as-justify-center">
            <div className="as-container as-mx-auto as-px-4 as-text-center">
                    <h2 className="as-single-product-title as-text-2xl as-mb-3" dangerouslySetInnerHTML={{__html: props?.subscriptionBundleSettingsEntity?.choosePlanLifeStyleTextV2 || 'Choose a plan to match your lifestyle'}} />
                    <div className="as-single-product-description as-text-sm as-mb-6" dangerouslySetInnerHTML={{__html: props?.subscriptionBundleSettingsEntity?.choosePlanLifeStyleDescriptionTextV2 || " Cutoff each week to amend your meals is midnight on Thursday's. (Delivery Schedule is as follows: Gold Coast - Mondays | Brisbane & Sunshine Coast - Wednesdays)"}} />
                    <div className="as-single-product-list-wrapper as-mt-2 as-pt-5 as-flex as-flex-wrap as-mx-auto as-justify-center">
                     {singleProductSettings?.map(item => {
                        return <SingleProduct
                            product={item?.sourceProduct}
                            shopInfo={props?.shopInfo}
                            subscriptionBundleSettingsEntity={props?.subscriptionBundleSettingsEntity}
                            bundleData={bundleData}
                            setSelectedSingleProduct={setSelectedSingleProduct}
                            selectedSingleProduct={selectedSingleProduct}
                            setSelectedProductSellingPlans={setSelectedProductSellingPlans}
                            setSelectedMasterProductData={setSelectedMasterProductData}
                            setSingleProductAllData={setSingleProductAllData}
                            singleProductAllData={singleProductAllData}
                        />
                     })}
                    </div>
                    {<button
                        onClick={() => {
                            setShowProductSelectionScreen(true);
                            setLoaded(true);
                        }}
                        disabled={!selectedSingleProduct}
                        className=" as-mt-6 as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                    >
                         {props?.subscriptionBundleSettingsEntity?.nextStepButtonTextV2 || 'Next Step'}
                        <ArrowRight/>
                    </button>}
                </div>
            </div>}
            {showProductSelectionScreen && <div className="">
                {loaded ? (
                    <ProductSelection
                        products={products}
                        bundleData={bundleData}
                        bundleSlug={bundleSlug}
                        shopInfo={props.shopInfo}
                        subscriptionBundleSettingsEntity={props.subscriptionBundleSettingsEntity}
                        selectedSingleProductBundleData={selectedSingleProductBundleData}
                        setShowProductSelectionScreen={setShowProductSelectionScreen}
                        isBundleTypeClassic={isBundleTypeClassic}
                        selectedSingleProduct={selectedSingleProduct}
                        selectedProductSellingPlans={selectedProductSellingPlans}
                        setIsReviewBundleModalOpen={setIsReviewBundleModalOpen}
                        isReviewBundleModalOpen={isReviewBundleModalOpen}
                        setIsNextButtonValid={setIsNextButtonValid}
                        isNextButtonValid={isNextButtonValid}
                        selectedMasterProductData={selectedMasterProductData}
                        setToken = {setSlug}
                        iframeSelectedFilterData={iframeSelectedFilterData}
                        isClickGoBackButton={isClickGoBackButton}
                        setIsClickGoBackButton={setIsClickGoBackButton}
                        setRedirectHistoryForIframe={setRedirectHistoryForIframe}
                    />
                ) : (
                    <div className="as-h-screen as-flex as-items-center as-justify-center as-bg-gray-100">
                        <Loader/> <span className="as-ml-2 as-text-sm">{props?.subscriptionBundleSettingsEntity?.pleaseWaitLabelTextV2 || "Please wait..."}</span>
                    </div>
                )}
                {((bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT") && loaded && ((page * pageSize ) < bundleData.products.length)) &&  <button
                        onClick={() => {
                            setPage(old => old + 1)
                            setLoadingProducts(true)
                        }}
                        disabled={loadingProducts}
                        className="as-mx-auto as-mt-6 as-w-full as-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary as-mb-12 "
                    >    {loadingProducts && <Loader/>}
                         <span className='as-ml-2'>{props?.subscriptionBundleSettingsEntity?.loadMoreTextV2 || 'Load More'}</span>
                        <ArrowRight/>
                    </button>}
            </div>}

            </div>
        </div>
    );
};

const mapStateToProps = state => ({
    subscriptionBundleSettingsEntity: state.subscriptionBundleSettings.entity,
    shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
    getEntityByShop,
    getShopInfoByCurrentLogin
};

export default connect(mapStateToProps, mapDispatchToProps)(Bundles);
