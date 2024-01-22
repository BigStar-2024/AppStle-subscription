import Axios from 'axios';
import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import { connect, useSelector } from 'react-redux';
import _ from 'lodash';
import { Row, Col, Button, Badge, FormGroup, Input  } from 'reactstrap';
import VirtualizedProductList from './VirtualizedProductList';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import { toast } from 'react-toastify';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import Slider from "react-slick";

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};

function SearchableProductSelect(props) {
  const { shopName, onComplete, contractId, getCustomerPortalEntity, sellingPlanIds } = props;
  const [nextRequestOptions, setNextRequestOptions] = useState({ next: false, cursor: null });
  const [loading, setLoading] = useState(false);
  const [collection, setCollection] = React.useState([]);
  const [slideProducts, setSlideProducts] = useState([])
  const [searchText, setSearchText] = React.useState('');
  const [selectProduct, setSelectedProduct] = React.useState(null);
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  let [updateInProgress, setUpdateInProgress] = useState(false);
  let [currentDropDownHandles, setCurrentDropDownHandles] = useState([])
  const [sliderSearchValue, setSliderSearchValue] = useState('');
  const [isOpened, setIsOpened] = useState(false);
  const [selectedProductData, setSelectedProductData] = useState();

  useEffect(() => {
    fetchReposByPage(true);
    setCurrentDropDownHandles([]);
  }, [searchText]);

  function SampleNextArrow(props) {
    const { className, style, onClick } = props;
    return (
      <div
        className={className}
        style={{ ...style, display: "block", background: "red" }}
        onClick={onClick}
      />
    );
  }

  function SamplePrevArrow(props) {
    const { className, style, onClick } = props;
    return (
      <div
        className={className}
        style={{ ...style, display: "block", background: "green" }}
        onClick={onClick}
      />
    );
  }


  const fetchReposByPage = async (loadSearchItem) => {
    try {
      if (!loadSearchItem) {
        return;
      }
      let resp = await Axios.get(`/api/data/products`, {
        params: {
          ...nextRequestOptions,
          search: searchText ? encodeURIComponent(searchText) : undefined,
          shop: shopName,
          contractId: contractId,
          sellingPlanIds: sellingPlanIds?.join('|')?.toString()
        }
      });
      let response = resp.data;
      setNextRequestOptions(old => {
        old.next = response.pageInfo.hasNextPage;
        old.cursor = response.pageInfo.cursor ? response.pageInfo.cursor : null;
        return old;
      });
      if (!searchText && customerPortalSettingEntity?.productSelectionOption !== "ALL_PRODUCTS") {
        let handlesArray = []
        response = {
          products: Object.keys(response?.productHandleData).map((key) => {
            if (currentDropDownHandles.indexOf(response?.productHandleData[key]) === -1) {
              handlesArray.push(response?.productHandleData[key])
              return {
                handle: response?.productHandleData[key]
              }
            }
          })
        }
        if (handlesArray.length) {
          setCurrentDropDownHandles(old => [...old, ...handlesArray])
        } else {
          return;
        }

      } else if (response.products.length === 0) {
        let flag = response.pageInfo.cursor ? true : false
        fetchReposByPage(flag);
        return;
      }
      if (response) {
        // let listData = response.products.edges.filter(function (line) {
        //     return line?.node?.status === 'ACTIVE'
        // })
        setSlideProducts(response.products);
        let resultsData = [];
        await response.products.forEach(async function(product, index) {
          const productvariantUrl = `${location.origin}/products/${product.handle}.js`;
          let productvariantResponse = await Axios.get(productvariantUrl);
          let productvariants = productvariantResponse.data;
          productvariants.variants.forEach(function(variant, i) {
            let isVariantValid = true;
            if (sellingPlanIds?.indexOf(null) === -1) {
              if (customerPortalSettingEntity?.productSelectionOption === "PRODUCTS_FROM_ALL_PLANS") {
                  if (!(variant?.selling_plan_allocations?.length)) {
                    isVariantValid = false;
                  }
              } else if (customerPortalSettingEntity?.productSelectionOption === "PRODUCTS_FROM_CURRENT_PLAN") {
                let hasSellingPlan = false;
                variant?.selling_plan_allocations?.forEach(item => {
                  if ((sellingPlanIds?.indexOf(item?.selling_plan_id) !== -1)) {
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
              } else if (productvariants.variants.length > 1) {
                item.title = `${productvariants?.title} - ${variant?.title}`;
              }
              item.price = variant?.price;
              item.id = `gid://shopify/ProductVariant/${variant?.id}`;
              item.imgSrc = `https:${productvariants?.featured_image}`;
              item.currencyCode = productvariants?.currencyCode;

              if(variant?.available) {
                resultsData.push(item);
              } else {
                if (customerPortalSettingEntity?.includeOutOfStockProduct) {
                  resultsData.push(item);
                }
              }
            }

            if (response?.products?.length - 1 === index && productvariants?.variants?.length - 1 === i) {
              setCollection(old => [...old, ...resultsData]);
            }
          });
        });

        // let last = _.last(response.products);
      }
       else {
        setNextRequestOptions({ next: false, cursor: null });
      }
    } catch (error) {
      console.log(error)
    }
    setLoading(false);
  };

  const options = collection.map(item => {
    return {
      value: item.id,
      label: item.title,
      payload: {
        ...item
      }
    };
  });

  var settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 4,
    slidesToScroll: 4,
    initialSlide: 0,
    variableWidth: true,
    // arrows:true,
    // nextArrow: <SampleNextArrow/>,
    // prevArrow: <SamplePrevArrow/>,
    autoplay: true,
    pauseOnHover: true,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
          infinite: true,
          dots: true
        }
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2,
          initialSlide: 2
        }
      },
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1
        }
      }
    ]
  };

  const MenuList = props => {
    // console.log("mm=>", props);
    const listRef = React.useRef(null);
    const children = Array.isArray(props.children) ? props.children : [props.children];
    const heightCalc = children.length * props.selectProps.itemSize();
    const customHeight = heightCalc >= 300 ? 300 : heightCalc;

    const currentIndex = Math.max(
      children.findIndex(child => child.props.isFocused),
      0
    );
    React.useEffect(() => {
      if (listRef.current) {
        listRef.current.scrollToItem(currentIndex);
      }
    }, [currentIndex]);

    return (
      <VirtualizedProductList
        listRef={ref => {
          listRef.current = ref;
        }}
        width={100}
        height={customHeight}
        listStyle={{ width: '100%' }}
        items={children}
        hasNextPage={props.selectProps.hasNextPage}
        isNextPageLoading={props.selectProps.isNextPageLoading}
        loadNextPage={props.selectProps.loadNextPage}
        itemSize={props.selectProps.itemSize}
      >
        {({ index, style }) => {
          const child = children[index];
          return child;
        }}
      </VirtualizedProductList>
    );
  };

  const addProduct = () => {
    if (selectProduct) {
      let product = selectProduct.payload;
      setUpdateInProgress(true);
      const requestUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=1&variantId=${product?.id}&isExternal=true`;
      Axios.put(requestUrl)
        .then(
          res => {
            getCustomerPortalEntity(contractId);
            setUpdateInProgress(false);
            toast.success('Contract Updated', options);
            onComplete();
          },
          () => {
            setUpdateInProgress(false);
          }
        )
        .catch(err => {
          setUpdateInProgress(false);
          toast.error('Contract Update Failed', options);
        });
    } else {
      alert('Please Select a product to add.');
    }
  };


  const formatOptionLabel = ({ value, label, payload }) => {
    return (
      <Row>
        <Col md={3} className="appstle-text-center">
          <img src={payload?.imgSrc} style={{ height: '70px' }} />
        </Col>
        <Col md={6}>{label}</Col>
        <Col md={3}>
          <span>{payload?.price}</span> <span>{payload?.currencyCode}</span>
        </Col>
      </Row>
    );
  };

  function ValueContainer(props) {
    let val = null;
    try {
      val = _.get(props.getValue(), '[0].label', null);
    } catch (error) {}
    return val;
  }

  const isToggle =() => {
    setIsOpened(!isOpened);
  }

  const searchProduct =() => {
    setSearchText(sliderSearchValue);
    setNextRequestOptions({ next: false, cursor: null });
    setCollection([]);
  }

  return (
    <div>
      <div className="appstle-product-search-wrapper" style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
        <Select
          // defaultMenuIsOpen={true}
          // menuIsOpen={true}
          className="appstle-product-search"
          classNamePrefix="appstle"
          onInputChange={inputValue => {
            if (inputValue !== searchText) {
              setSearchText(inputValue);
              setNextRequestOptions({ next: false, cursor: null });
              setCollection([]);
              // let timer = setTimeout(() => {
              //     fetchReposByPage()
              //     clearTimeout(timer);
              // }, 100)
            }
          }}
          name="appstle-product-search"
          // formatOptionLabel={formatOptionLabel}
          options={options}
          hasNextPage={nextRequestOptions.next}
          isNextPageLoading={loading}
          placeholder={customerPortalSettingEntity?.addProductLabelTextV2}
          loadNextPage={_.debounce(() => {
            fetchReposByPage(nextRequestOptions.cursor ? true : false)
          }, 300)}
          itemSize={() => 100}
          onChange={val => {
            console.log('val', val);
            setSelectedProduct(val);
          }}
          components={{
            MenuList
          }}
          style={{ flexGrow: '1' }}
        />

        <MySaveButton
          onClick={() => {
            addProduct();
          }}
          text={customerPortalSettingEntity?.addProductButtonTextV2}
          className="appstle_order-detail_update-button"
          updating={updateInProgress}
          updatingText={customerPortalSettingEntity?.addProductButtonTextV2}
          style={{ padding: '9px ​12px' }}
        >
          {customerPortalSettingEntity?.addProductButtonTextV2}
        </MySaveButton>
        {/* <Button color="warning"
                    style={{padding: "8px ​12px"}}
                    className="ml-2 appstle_order-detail_cancel-button align-items-center appstle-inline" onClick={() => onComplete()}>
                        {customerPortalSettingEntity?.cancelButtonTextV2}
                    </Button> */}
      </div>

      {
        shopName == "dev-dharmik-test2.myshopify.com" &&
        <>
        <div className="appstle-product-search-wrapper" style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
      <FormGroup style={{flex: '1 1 auto'}}>
          <Input
          value= {sliderSearchValue}
          type="text"
          placeholder="search product"
          onChange={event => {
            if (event.target.value !== sliderSearchValue) {
              setSliderSearchValue(event.target.value);
            }
          }}
          className="mt-1"
      />
      </FormGroup>
      <Button color="warning"
          style={{padding: "8px ​12px"}}
          className="ml-2 appstle_order-detail_update-button align-items-center appstle-inline" onClick={searchProduct}>
              Search Product
        </Button>

      </div>
       <Slider {...settings} style={{margin:'10px'}}>
      {
        slideProducts?.map(data=>
          <>
          <img src={data?.imgSrc} style={{height: '150px', width: '100%'}}/>
            <p style={{fontSize: '17px'}}>{data?.title}</p>

            <p style={{fontSize: '15px'}}><b>{data?.price} {data?.currencyCode} </b></p>
            <MySaveButton
              onClick={() => {
                setSelectedProductData(data);
                setIsOpened(true);
                // addSliderProduct(data);
              }}
              text={customerPortalSettingEntity?.addProductButtonTextV2}
              className="appstle_order-detail_update-button"
              updating={updateInProgress}
              updatingText={customerPortalSettingEntity?.addProductButtonTextV2}
              style={{ padding: '9px ​12px', textAlign: 'center' }}
            >
            {customerPortalSettingEntity?.addProductButtonTextV2}
          </MySaveButton>
          </>
          )
      }
        </Slider>

        </>
      }
      </div>
  );
}

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchableProductSelect);
