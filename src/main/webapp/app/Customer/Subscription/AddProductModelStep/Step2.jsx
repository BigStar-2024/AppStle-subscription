import React, {useState, useEffect} from 'react';
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  ListGroup,
  ListGroupItem,
  Input,
  Row,
  Col,
  Label,
  FormGroup, FormText, Badge,
  Spinner 
} from 'reactstrap';
import Loader from 'react-loaders';
import productList from "./products";
import { formatPrice } from 'app/shared/util/customer-utils';

import axios from 'axios';
import { connect, useSelector } from 'react-redux';
import {getProductOptions, getProductOptionsExternal} from 'app/entities/fields-render/data-product.reducer';
import {getPrdVariantOptions} from 'app/entities/fields-render/data-product-variant.reducer';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import productMockData from "./productMockData";
const AddVariantListModel = ({
                            getProductOptions,
                            getPrdCollectionOptions,
                            getPrdVariantOptions,
                            productsData,
                            productOptions,
                            prdCollectionOptions,
                            prdVariantOptions,
                            getProductOptionsExternal,
                            subscriptionEntity,
                            ...props
                          }) => {
  const {
    value,
    totalTitle,
    sellingPlanIds,
    purchaseOption,
    contractId,
    isMultiSelect,
    searchBarPlaceholder,
    noProductDataMessage
  } = props;


  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  const [productOptionsData, setProductOptionsData] = useState({products: [], pageInfo: {}});
  const [modal, setModal] = useState(false);
  const [selectedItems, setSelectedItems] = useState(value ? JSON.parse(value) : []);
  const [fields, setFields] = useState(value ? JSON.parse(value) : []);
  const [cursor, setCursor] = useState(null);
  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const [collection, setCollection] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [infoMessage, setInfoMessage] = useState(false);

  useEffect(() => {
    setFields(value ? [...JSON.parse(value)] : []);
    setSelectedItems(value ? [...JSON.parse(value)] : []);
  }, [value])

  useEffect(() => {
    setIsLoading(true);
    setInfoMessage(false);
    getProductOptionsExternal(`${null}&next=${false}&contractId=${contractId}${customerPortalSettingEntity?.productSelectionOption !== 'ALL_PRODUCTS' ?`&sellingPlanIds=${sellingPlanIds}`:``}${purchaseOption === "ONE_TIME" ? `&sendAllData=true` : ``}`);
  }, [])
  
  useEffect(() => {
    if(productsData?.pageInfo != {})
    {
      setCursor(productsData?.pageInfo?.cursor);
      setNext(productsData?.pageInfo?.hasNextPage);
    }
    fetchReposByPage();
    if (customerPortalSettingEntity?.productSelectionOption !== "ALL_PRODUCTS" &&
    (productsData?.products?.length === 0) &&
    !searchValue &&
    productsData?.productHandleData &&
    Object.keys(productsData?.productHandleData)?.length) {
      fetchReposByPage(productsData?.productHandleData);
    } else if (productsData?.pageInfo?.cursor && productsData?.pageInfo?.hasNextPage && (productsData?.products?.length === 0)) {
      handleSeeMore(productsData?.pageInfo?.cursor, productsData?.pageInfo?.hasNextPage);
    }
  }, [productsData])

  const handleSeeMore = (cursor, next) => {
    getProductOptionsExternal(`${cursor}&next=${next}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}&contractId=${contractId}${customerPortalSettingEntity?.productSelectionOption !== 'ALL_PRODUCTS' ?`&sellingPlanIds=${sellingPlanIds}`:``}${purchaseOption === "ONE_TIME" ? `&sendAllData=true` : ``}`);
  }

  const fetchReposByPage = async (productHandleData) => {
    if(productHandleData || productsData?.products?.length > 0)
      {
    //   setIsLoading(true);
      let productsArray = []
      if (productsData?.products?.length > 0) {
        productsArray = JSON.parse(JSON.stringify(productsData.products))
      } else if (productHandleData) {
        productsArray = [...(Object.keys(productHandleData).map((key) => {
            return {
              handle: productHandleData[key]
            }
        }))]
      } else {
        setIsLoading(false);
        setInfoMessage(true);
        return;
      }

      if (!productsArray.length) {
        setIsLoading(false);
        setInfoMessage(true)
        return;
      }

      let resultsData = [];
      let globalIndex = 0
      for await (let product of productsArray) {
        globalIndex = globalIndex + 1;
      //  productMockData.forEach(async function(product, index) {
        var prdItem = {};
        const productvariantUrl = `${location.origin}/products/${product.handle}.js`;
        let productvariantResponse = null
        try {
          productvariantResponse = await axios.get(productvariantUrl);
        } catch (err) {
          console.log(Object.keys(err), err.message);
        }
        if (productvariantResponse) {
          let productvariants = productvariantResponse.data;
          // let productvariants = product;
          prdItem.imgSrc = `https:${productvariants?.featured_image}`;
          prdItem.currencyCode = productvariants?.currencyCode;
          prdItem.prdHandleName = productvariants?.handle;
          prdItem.title = productvariants?.title;
          prdItem.variants = [];
          productvariants?.variants?.forEach(function(variant, i) {
            let isVariantValid = true;
            if (sellingPlanIds?.indexOf(null) === -1 && purchaseOption == "SUBSCRIBE") {
              let filteredSellingPlan = sellingPlanIds.map((plan) => {
                return Number(plan?.split("/").pop())
              });    
              if (customerPortalSettingEntity?.productSelectionOption === "PRODUCTS_FROM_ALL_PLANS") {
                  if (!(variant?.selling_plan_allocations?.length)) {
                    isVariantValid = false;
                  }
              } else if (customerPortalSettingEntity?.productSelectionOption === "PRODUCTS_FROM_CURRENT_PLAN") {
                let hasSellingPlan = false;
                variant?.selling_plan_allocations?.forEach(item => {
                  if ((filteredSellingPlan?.indexOf(item?.selling_plan_id) !== -1)) {
                    hasSellingPlan = true
                  }
                })
                if (!hasSellingPlan) {
                  isVariantValid = false;
                }
              }
            }

            if(isVariantValid) {
              var item = {};
              if (productvariants.variants.length === 1) {
                item.title = productvariants?.title;
                item.price = productvariants?.price;
              } else if (productvariants.variants.length > 1) {
              //   item.title = `${productvariants?.title} - ${variant?.title}`;
                  item.title = `${variant?.title}`;
                  item.price = variant?.price;
              }
              item.id = `gid://shopify/ProductVariant/${variant?.id}`;
              item.uniqueId = variant?.id;

              if(variant?.available) {
                // resultsData.push(item);
                prdItem.variants.push(item);
              } else {
                if (customerPortalSettingEntity?.includeOutOfStockProduct) {
                  // resultsData.push(item);
                  prdItem.variants.push(item);
                }
              }
            }
          });
          resultsData.push(prdItem);
        }
        if (productsArray?.length == globalIndex) {
          setIsLoading(false);
          setInfoMessage(false);
          setCollection(old => [...old, ...resultsData]);
        }
      }
    }
    else {
          setIsLoading(false);
          setInfoMessage(true);
    }
  }

  useEffect(() => {
    setSelectedItems([...fields]);
  }, [fields])

  useEffect(() => {
   props.selectedVariantItems(selectedItems);
  }, [selectedItems])


  const handleCheck = (varData, imageSrc, prdHandleName) => {
    if(isMultiSelect === false)
    {
      setSelectedItems([{id: varData?.id, uniqueId: varData?.uniqueId, title: varData?.title, imageSrc: imageSrc, prdHandleName : prdHandleName}])
    }
    else {
      const foundIndex = selectedItems.findIndex(({uniqueId}) => (uniqueId === varData.uniqueId))
      if (foundIndex !== -1) {
        setSelectedItems((prevItems) => prevItems.filter(({uniqueId}) => uniqueId !== varData.uniqueId))
      } else {
        setSelectedItems((prevItems) => [...prevItems, {id: varData?.id, uniqueId: varData?.uniqueId, title: varData?.title, imageSrc: imageSrc, prdHandleName : prdHandleName}])
      }
    }
  }

  const handleSearch = (event) => {
    if (event.target.value !== searchValue) {
        setCollection([])
        setIsLoading(true)
        setInfoMessage(false);
        setSearchValue(event.target.value);
        getProductOptionsExternal(`${null}&next=${false}${event.target.value ? `&search=${event.target.value || ""}` : ``}&contractId=${contractId}${customerPortalSettingEntity?.productSelectionOption !== 'ALL_PRODUCTS' ?`&sellingPlanIds=${sellingPlanIds}`:``}${purchaseOption === "ONE_TIME" ? `&sendAllData=true` : ``}`);
    }
  }

  const remove = (evt, id) => {
    evt.preventDefault();
    let productsCopy = fields.filter((product) => product.id !== id);
    setFields([...productsCopy]);
  }

  const checkIfProductAlreadySelected = (itemId) => {
    return selectedItems.findIndex(({uniqueId}) => (uniqueId === itemId)) != -1 ? true : false;
  }

  const getdiscountedPrice = (price) => {
    if (subscriptionEntity?.lines?.edges?.length && price) {
      let contractItem = JSON.parse(JSON.stringify(subscriptionEntity?.lines?.edges))?.pop();
      if (contractItem?.node?.pricingPolicy) {
        let pricingPolicy = contractItem?.node?.pricingPolicy
        if (pricingPolicy?.cycleDiscounts?.length === 1) {
          let cycleDiscount = pricingPolicy?.cycleDiscounts.pop();
          if (cycleDiscount?.adjustmentType === "PERCENTAGE") {
            let discountPercentage = cycleDiscount?.adjustmentValue?.percentage;
            return Math.round(((100 - Number(discountPercentage)) /  100) * price);
          } else {
            return price;
          }
        } else {
          return price;
        }
      } else {
        return price;
      }
    } else {
      return price;
    }
  }

  return (
    <div style={{ fontSize: "15px", padding: "3px" }} className="form-wizard-content">
      <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0" >
        <FormGroup>
          <Input type="hidden"
                 attr='required'
                 value={fields.length}
                 required={true}/>
        </FormGroup>
      </ListGroup>

        <div className="multiselect-modal-body">

          <>
            <div className="form-group has-search mb-3">
                <div className="input-group">
                <div className="input-group-prepend">
                    <span className="input-group-text"><i className="pe-7s-search"></i></span>
                </div>
                <input type="text" className="form-control" placeholder={searchBarPlaceholder} onChange={handleSearch} value={searchValue} />
                </div>
            </div>
            {!isLoading && <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0" style={{border: "1px solid rgba(0, 0, 0, 0.125)", borderRadius: "5px", paddingBottom: '20px'}}>
            {
                collection && collection?.length > 0 && collection?.map((prdData, index) => {
                return prdData?.variants?.length > 0 && <ListGroupItem className="multiselect-list-item" key={index}  style={{borderTop: index ? "1px solid rgba(0, 0, 0, 0.125)" : ''}}>
                    {
                      !(customerPortalSettingEntity?.disAllowVariantIdsForOneTimeProductAdd?.includes(prdData?.variants[0]?.uniqueId) && purchaseOption === "ONE_TIME")
                            && !(customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag && purchaseOption === "ONE_TIME" && prdData?.variants[0]?.requires_selling_plan) &&
                            <FormGroup check>
                            <Label className="d-flex align-items-center">
                            { prdData?.variants?.length ==1 &&
                            <Input type={isMultiSelect === false ? "radio" : "checkbox"} onChange={() => handleCheck(prdData?.variants[0], prdData?.imgSrc, prdData?.prdHandleName)} checked={checkIfProductAlreadySelected(prdData?.variants[0]?.uniqueId)}/>}
                            <img className=" shadow-sm mr-2 ml-1 rounded" src={prdData?.imgSrc} />
                            <Row className="ml-1 w-100" style={{padding: "10px 50px 10px 0px"}}>
                                <Col sm={8}>
                                    {prdData?.title}
                                </Col>
                                <Col sm={4}>
                                    {prdData?.variants?.length == 1 && (purchaseOption == "SUBSCRIBE" ? formatPrice(getdiscountedPrice((prdData?.variants[0]).price)) : formatPrice((prdData?.variants[0]).price)) }
                                </Col>
                                </Row>
                            </Label>
                        </FormGroup>
                    }
                    {
                    prdData?.variants?.length > 1 &&  prdData?.variants?.map((varData, varientIndex)=> (
                      !(customerPortalSettingEntity?.disAllowVariantIdsForOneTimeProductAdd?.includes(varData?.uniqueId) && purchaseOption === "ONE_TIME") &&
                      !(customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag && purchaseOption === "ONE_TIME" && varData?.requires_selling_plan) &&
                            <>
                            <FormGroup check style={{padding:"10px 56px 10px 82px"}} key ={varientIndex}>
                                <Input type={isMultiSelect === false ? "radio" : "checkbox"} onChange={() => handleCheck(varData, prdData?.imgSrc, prdData?.prdHandleName)} checked={checkIfProductAlreadySelected(varData?.uniqueId)} />
                                <Row className="ml-1">
                                    <Col sm={8}>
                                    {varData?.title}
                                    </Col>
                                    <Col sm={4}>
                                        {(purchaseOption == "SUBSCRIBE" ? formatPrice(getdiscountedPrice(varData?.price)) : formatPrice(varData?.price))}
                                    </Col>
                                </Row>
                            </FormGroup>
                                </>
                        ))
                    }
                </ListGroupItem>
                })
                }
                {(next && productsData?.products?.length) ? <Button color="primary" className="mx-auto mt-2"  style={{width:"100px"}} onClick={() => handleSeeMore(cursor, next)}> {customerPortalSettingEntity?.seeMoreProductBtnTextV2 || "See More..."}</Button>:null}
            </ListGroup>}
        
            {
            isLoading ?
                <div className="loader-wrapper d-flex justify-content-center align-items-center w-100">
                        <Loader type="line-scale-pulse-out" />
            </div>
            :
            ((infoMessage && searchValue) && 
            <div className="loader-wrapper d-flex justify-content-center align-items-center w-100">
                        <p>{noProductDataMessage}</p>
            </div>)
            }
          </>

        </div>
       </div>
  );
}


const mapStateToProps = storeState => ({
  productOptions: storeState.prdVariant.prdVariantOptions,
  productsData: storeState.product.productOptions,
  subscriptionEntity: storeState.subscription.entity
});

const mapDispatchToProps = {
  getProductOptions,
  getPrdVariantOptions,
  getProductOptionsExternal
};

export default connect(mapStateToProps, mapDispatchToProps)(AddVariantListModel);
