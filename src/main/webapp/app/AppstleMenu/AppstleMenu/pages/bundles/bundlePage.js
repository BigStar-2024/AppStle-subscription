import React, { useEffect, useState } from 'react';
import BundleSideMenu from './bundleSideMenu/bundleSideMenu';
import BundleCard from './bundleCard/bundleCard';
// vvv No longer exists in reducer
// import { useShopify } from '../../reducers/appstle-menu.reducer';
import { refectoredProductData } from 'app/AppstleMenu/config/helper/helper';

const BundlePage = () => {
  const [products, setProducts] = useState();
  const [selectedProductsForAdd, setSelectedProductsForAdd] = useState([]);
  const [dropDownValue, setDropDownValue] = useState('');
  const [counter, setCounter] = useState(false);
  const { fetchCollection, fetchProductByTags, fetchVendors, shopify_products_all_collection } = {}//useShopify();
  const { hash } = window.location;
  useEffect(() => {
    fetchCollection();
    setProducts([]);
    fetchProductByTags();
    fetchVendors();
    selectedCollection(shopify_products_all_collection?.length ? shopify_products_all_collection[0].title : '');
    setDropDownValue(shopify_products_all_collection?.length ? shopify_products_all_collection[0].title : '');
  }, []);
  const selectedCollection = selectedCollectionName => {
    setDropDownValue(selectedCollectionName);
    if (selectedCollectionName !== null && shopify_products_all_collection?.length) {
      let getSelectedCollectionProducts = shopify_products_all_collection?.find(
        item => item.title?.toLowerCase() === selectedCollectionName?.toLowerCase()
      );
      if (getSelectedCollectionProducts) {
        const gettingRefectorProducts = refectoredProductData(getSelectedCollectionProducts.products);
        if (gettingRefectorProducts?.length) {
          setProducts([...gettingRefectorProducts]);
        }
      }
    }
  };
  const onChangeVariant = (productId, variant, index) => {
    let dublicateProducts = [...products];
    dublicateProducts[index] = { ...products.find(item => item.id === productId), currentVariant: variant };
    if (dublicateProducts?.length) {
      setProducts([...dublicateProducts]);
    }
  };
  const quantityCounter = (key, index, productId) => {
    let singleProduct = {};
    if (key === 'increment' && index !== null) {
      if (selectedProductsForAdd?.length && productId) {
        let selectedProductQuantity = selectedProductsForAdd.find(item => item.id === productId);
        let DublicateArray = selectedProductsForAdd.map(item =>
          item.id === productId
            ? {
                ...selectedProductQuantity,
                currentVariant: {
                  ...selectedProductQuantity.currentVariant,
                  unitPriceMeasurement: {
                    ...selectedProductQuantity.currentVariant.unitPriceMeasurement,
                    quantityValue: selectedProductQuantity.currentVariant.unitPriceMeasurement.quantityValue + 1
                  }
                }
              }
            : item
        );
        setSelectedProductsForAdd([...DublicateArray]);
      }
      singleProduct = {
        ...products[index],
        currentVariant: {
          ...products[index].currentVariant,
          unitPriceMeasurement: {
            ...products[index].currentVariant.unitPriceMeasurement,
            quantityValue: products[index].currentVariant.unitPriceMeasurement.quantityValue + 1
          }
        }
      };
      products[index] = { ...singleProduct };
      setProducts([...products]);
    } else if (key === 'dicrement' && index !== null) {
      if (selectedProductsForAdd?.length && productId) {
        let selectedProductQuantity = selectedProductsForAdd.find(item => item.id === productId);
        if (selectedProductQuantity.currentVariant.unitPriceMeasurement.quantityValue <= 1) {
          let deletedProductIdex = selectedProductsForAdd.findIndex(item => item.id === selectedProductQuantity.id);
          if (deletedProductIdex !== null) {
            let dublicateArray = [...selectedProductsForAdd];
            dublicateArray.splice(deletedProductIdex, 1);
            setSelectedProductsForAdd([...dublicateArray]);
          }
        } else {
          let selectedProductQuantity = selectedProductsForAdd.find(item => item.id === productId);
          let DublicateArray = selectedProductsForAdd.map(item =>
            item.id === productId
              ? {
                  ...selectedProductQuantity,
                  currentVariant: {
                    ...selectedProductQuantity.currentVariant,
                    unitPriceMeasurement: {
                      ...selectedProductQuantity.currentVariant.unitPriceMeasurement,
                      quantityValue: selectedProductQuantity.currentVariant.unitPriceMeasurement.quantityValue - 1
                    }
                  }
                }
              : item
          );
          setSelectedProductsForAdd([...DublicateArray]);
        }
      }
      if (products[index].currentVariant.unitPriceMeasurement.quantityValue <= 0) {
        singleProduct = { ...products[index], quantityButton: false };
        products[index] = { ...singleProduct };
        setProducts([...products]);
      } else {
        singleProduct = {
          ...products[index],
          currentVariant: {
            ...products[index].currentVariant,
            unitPriceMeasurement: {
              ...products[index].currentVariant.unitPriceMeasurement,
              quantityValue: products[index].currentVariant.unitPriceMeasurement.quantityValue - 1
            }
          }
        };
        products[index] = { ...singleProduct };
        setProducts([...products]);
      }
    }
    let selectedProductsForBundels = products.filter(item => item.currentVariant.unitPriceMeasurement.quantityValue >= 1);
    setSelectedProductsForAdd([...selectedProductsForBundels]);
  };
  const productAddHandler = (key, index, productsID) => {
    setCounter(false);
    let singleProduct = {};
    if (key === 'subscribe') {
      console.log('subscribe');
      setCounter(true);
      if (products?.length) {
        singleProduct = { ...products[index], quantityButton: true };
        products[index] = { ...singleProduct };
        setProducts([...products]);
      }
    } else {
      console.log('add');
      setCounter(true);
      if (products?.length) {
        singleProduct = {
          ...products[index],
          quantityButton: true,
          currentVariant: {
            ...products[index].currentVariant,
            unitPriceMeasurement: {
              ...products[index].currentVariant.unitPriceMeasurement,
              quantityValue: (products[index].currentVariant.unitPriceMeasurement.quantityValue = 1)
            }
          }
        };
        products[index] = { ...singleProduct };
        setProducts([...products]);
      }
      if (productsID) {
        let selectedProduct = products.find(item => item.id === productsID);
        if (selectedProduct && !selectedProductsForAdd.find(item => item.id === productsID)) {
          selectedProductsForAdd.push(selectedProduct);
          setSelectedProductsForAdd([...selectedProductsForAdd]);
        }
      }
    }
  };
  return (
    <div className="as-container as-mx-auto as-flex ">
      <div className="as-p-5 ">
        <BundleSideMenu
          collection={shopify_products_all_collection}
          selectedCollection={selectedCollection}
          selectedProductsForAdd={selectedProductsForAdd}
          dropDownValue={dropDownValue}
        />
      </div>

      <div className=" as-p-5 as-bg-[#F9F4EA] as-w-full ">
        <div class="as-grid as-grid-cols-1">
          {products?.length ? (
            products?.map((item, index) => {
              return (
                <BundleCard
                  detail={item.description}
                  title={item.title}
                  src={
                    item?.currentVariant?.image?.src ||
                    item?.image[0]?.src ||
                    'https://flowbite.s3.amazonaws.com/blocks/marketing-ui/content/content-gallery-3.png'
                  }
                  variants={item.variants}
                  price={item?.currentVariant?.price || '0'}
                  quantity={item.currentVariant.unitPriceMeasurement.quantityValue || 0}
                  quantityCounter={quantityCounter}
                  index={index}
                  onChangeVariant={onChangeVariant}
                  productId={item.id}
                  location={hash?.split('/')[1]}
                  counterButton={item?.quantityButton}
                  productAddHandler={productAddHandler}
                />
              );
            })
          ) : (
            <div className="as-text-center as-ml-2 as-ext-sm as-font-medium as-text-gray-900 dark:as-text-gray-300">
              <h4>Choose you Collection!</h4>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
export default BundlePage;
