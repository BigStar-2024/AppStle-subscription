import React, {useState, useEffect} from 'react';
import Product from './Product';
import SelectedProduct from './SelectedProduct';
import _ from 'lodash';
import {faAngleLeft, faQuestionCircle} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {Input} from 'reactstrap';
import { Cart } from 'react-ionicons';
import {toast} from 'react-toastify';
import SweetAlert from 'sweetalert-react';
import './style.scss';
import axios from 'axios';

function ProductSelection(props) {
  const [products, setProducts] = useState(props.products);
  const [subscriptionBundleSettingsEntity, setSubscriptionBundleSettingsEntity] = useState(props.subscriptionBundleSettingsEntity);
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [subscriptionPlans, setSubscriptionPlans] = useState([]);
  const [selectedSellingPlan, setSelectedSellingPlan] = useState(null);
  const [selectedSellingPlanDisplayName, setSelectedSellingPlanDisplayName] = useState();
  const [cartSliderOpen, setCartSliderOpen] = useState(false);
  const [isCheckingOut, setIsCheckingOut] = useState(false)
  const [maxProductCountInValid, setMaxProductCountInValid] = useState(false);
  const [error, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };


  useEffect(() => {
    if (props.bundleData?.subscription?.infoJson) {
      setSubscriptionPlans(JSON.parse(props.bundleData?.subscription?.infoJson)?.subscriptionPlans)
    }
  }, [props.bundleData]);

  useEffect(() => {
    if (props.subscriptionBundleSettingsEntity) {
      setSubscriptionBundleSettingsEntity(props.subscriptionBundleSettingsEntity);
    }
  }, [props.subscriptionBundleSettingsEntity]);

  useEffect(() => {
    if (!selectedSellingPlan) {
      setSelectedSellingPlan(subscriptionPlans[0]?.id?.split("/")?.pop())
    }
  }, [subscriptionPlans])

  useEffect(() => {
    setSelectedSellingPlanDisplayName(subscriptionPlans.filter(
      (item) => item?.id?.split("/")?.pop() === selectedSellingPlan
    )?.pop()?.frequencyName)
  }, [selectedSellingPlan])

  const addToCart = async () => {
    setIsCheckingOut(true);
    if (props.bundleData?.bundle?.discount) {
      fetch(`${location.origin}/cart/clear.js`, {
        method: 'POST', // or 'PUT'
        headers: {
          'Content-Type': 'application/json',
        }
      }).then((res) => {
        checkoutBundle(res)
      })
        .catch(err => {
          setErrorMessage(err?.description);
          setError(true);
          setIsCheckingOut(false);
        })
    } else {
      checkoutBundle({})
    }
  }

  let checkoutBundle = (res) => {
    let addPayload = {items: []};
    if (selectedProducts.length) {
      selectedProducts.forEach(item => {
        var payloadData = {
          'id': item?.variant?.id,
          'quantity': item?.quantity,
          'selling_plan': selectedSellingPlan
        }
        addPayload?.items?.push(payloadData)
      })
      let config = {
        method: 'POST', // or 'PUT'
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(addPayload)
      }
      fetch(`${location.origin}/cart/add.js`, config)
        .then((res) => res.json())
        .catch(err => {
          setErrorMessage(err?.description);
          setError(true);
          setIsCheckingOut(false);
        })
        .then(data => {
          if(data?.items?.length)
          {
            fetch(`${location.origin}/cart.js`)
              .then(res => res.json())
              .then(async data => {
                await axios.put(`/api/subscription-bundlings/discount/${props?.bundleSlug}`, {"cart": data})
                  .then(res => {
                    console.log(res.data);
                    window?.parent?.postMessage(JSON.stringify({
                      type: "appstle_message_to_redirect_to_checkout",
                      discountCode: res?.data?.discountCode || "appstle_no_discount",
                      redirectType: props.bundleData?.bundle?.bundleRedirect,
                      redirectURL: props.bundleData?.bundle?.customRedirectURL
                    }));
                  })
                  .catch((err) => {
                    console.log(err);
                    setIsCheckingOut(false);
                    setErrorMessage(err?.description);
                    setError(true);
                  })
              })
          }
          else if(data?.status && data?.status !== 200)
          {
            setErrorMessage(data?.description);
            setError(true);
            setIsCheckingOut(false);
          }
        })
        .catch((err) => {
          console.log(err);
          setIsCheckingOut(false);
          setErrorMessage(err?.description);
          setError(true);
        })
    } else {
      window?.parent?.postMessage(JSON.stringify({
        type: "appstle_message_to_redirect_to_checkout",
        discountCode: "appstle_no_discount",
        redirectType: props.bundleData?.bundle?.bundleRedirect,
        redirectURL: props.bundleData?.bundle?.customRedirectURL
      }));
    }
  }


  const minProduct = props.bundleData.bundle.minProductCount;
  const maxProduct = props.bundleData.bundle.maxProductCount || 99999999999999999999;
  const onProductAdd = product => {
    let newSelectedProducts = JSON.parse(JSON.stringify(selectedProducts));
    let idx = _.findIndex(newSelectedProducts, o => o.product.id == product.id && o.variant.id == product?.currentVariant?.id);
    if (idx === -1) {
      newSelectedProducts = [...newSelectedProducts, {
        product: {...product},
        variant: {...product.currentVariant},
        quantity: 1
      }]
    } else {
      newSelectedProducts[idx].quantity += 1;
    }
    let count = (_.sumBy(newSelectedProducts, 'quantity'))
    if (count > maxProduct) {
      setMaxProductCountInValid(true);
      return;
    } else {
      setSelectedProducts(newSelectedProducts);
    }
    // toast.success("Added to Cart", options);
  };
  const onProductRemove = variant => {
    let oldSelectedProducts = JSON.parse(JSON.stringify(selectedProducts));
    let idx = _.findIndex(oldSelectedProducts, o => o.variant.id == variant.id);
    if (oldSelectedProducts[idx].quantity > 1) {
      oldSelectedProducts[idx].quantity -= 1;
      setSelectedProducts(oldSelectedProducts)
    } else {
      setSelectedProducts(oldProducts => {
        return _.filter(oldProducts, o => {
          return o.variant.id !== variant.id;
        });
      });
    }
  };
  const onVariantChange = (product, variant) => {
    product.currentVariant = variant;
    setProducts(oldProducts => {
      return _.map(oldProducts, o => {
        if (o.product.id == product.id) {
          return {product};
        } else {
          return o;
        }
      });
    });
  };
  const onDragStart = (event, item) => {
    event.dataTransfer.setData('productId', item.product.id);
  };
  const onDragOver = event => {
    event.preventDefault();
  };
  const onDrop = (event, dropable) => {
    let productId = event.dataTransfer.getData('productId');
    if (dropable && productId) {
      let productInfo = _.find(products, o => o.product.id == productId);
      if (productInfo) {
        onProductAdd(productInfo.product, productInfo.product.currentVariant);
      }
    }
  };
  return (
    <>
      <div className={`appstleBundle_wrapper ${cartSliderOpen && "appstle_slider_open"}`}>
        <div className="appstle_product_selection d-flex flex-column flex-grow-1 w-100 column-menu">
          <div className="appstle_product_selection_backdrop"
               onClick={() => setCartSliderOpen(false)}>
          </div>
          <header className="appstleBundle_productSelection_header">
            <div className="container-fluid appstleBundle_productList_header">
              <div className="d-flex align-items-center" style={{margin: "auto"}}><span
                style={{"minWidth": "180px"}}>{subscriptionBundleSettingsEntity?.selectedFrequencyLabelText || `Selected Frequency`}:</span>
                <Input
                  type="select"
                  name="freq-plan"
                  style={{
                    border: "none",
                    borderRadius: 0,
                    borderBottom: "1px solid currentColor"
                  }}
                  onChange={(event) => setSelectedSellingPlan(event.target.value)}
                >
                  {subscriptionPlans?.map((plan) => {
                    return (<option value={plan?.id?.split("/").pop()}>{plan?.frequencyName}</option>)
                  })}
                </Input>
              </div>
              <button onClick={() => setCartSliderOpen(true)}
                      className="appstle_cart_icon btn-icon btn-icon-only btn btn-link btn-sm relative">
                <Cart width="25px" height="25px" color="#465661"/>
                <span className="badge badge-pill badge-success">{_.sumBy(selectedProducts, 'quantity')}</span>
              </button>
            </div>
            <div class="container-fluid d-flex appstle_md_hide mt-2">
              <button disabled={(minProduct && (_.sumBy(selectedProducts, 'quantity') < minProduct)) || (maxProduct && (_.sumBy(selectedProducts, 'quantity') > maxProduct)) || isCheckingOut} onClick={addToCart}
                      className="w-100 btn" style={{
                fontSize: '13px',
                height: "36px",
                margin: '7px',
                borderRadius: "4px",
                backgroundColor: "#FCBA03",
                borderColor: "#FCBA03"}}>
                {(minProduct && (_.sumBy(selectedProducts, 'quantity') < minProduct)) ? (
                  `${subscriptionBundleSettingsEntity?.selectMinimumProductButtonText ? subscriptionBundleSettingsEntity.selectMinimumProductButtonText.replace(`{{minProduct}}`, minProduct) : `Please select minimun ${minProduct} products`}`
                ) : (isCheckingOut ? <div className="appstle_loadersmall"/>
                    : `${subscriptionBundleSettingsEntity?.proceedToCheckoutButtonText || "Proceed to checkout"}`
                )}
              </button>
            </div>
          </header>
          <div className="container-fluid appstle_product_container">
            <div className="row">
              {products?.map((each, idx) => {
                return (
                  <div
                    className={`col-12 col-md-6 col-lg-4 col-xl-3 appstle_product appstle_draggable `}
                    draggable
                    onDragStart={event => onDragStart(event, each)}
                    key={idx}
                    style={{padding: "8px"}}
                  >
                    {(subscriptionPlans?.length > 0) &&
                    <Product
                      bundleData={props.bundleData}
                      product={each?.product}
                      onProductAdd={onProductAdd}
                      onProductRemove={onProductRemove}
                      onVariantChange={onVariantChange}
                      showAddProduct
                      showVariant
                      variants={props?.bundleData?.variants}
                      selectedProducts={selectedProducts}
                      subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                      sellingPlanIds={subscriptionPlans?.map(plan => (Number(plan?.id?.split("/").pop())))}
                      selectedSellingPlan={selectedSellingPlan}
                    />}
                  </div>
                );
              })}
            </div>
          </div>
        </div>
        <SweetAlert
          title={`${subscriptionBundleSettingsEntity?.failedToAddTitleText || "Failed To Add"}`}
          confirmButtonColor=""
          confirmButtonText = {`${subscriptionBundleSettingsEntity?.okBtnText || "ok"}`}
          show={maxProductCountInValid}
          text={`${subscriptionBundleSettingsEntity?.failedToAddMsgText ? subscriptionBundleSettingsEntity.failedToAddMsgText.replace(`{{maxProduct}}`, maxProduct) : `Cart can't have more than ${maxProduct} products`}`}
          type="error"
          onConfirm={() => setMaxProductCountInValid(false)}
        />
        <SweetAlert
          title={`${subscriptionBundleSettingsEntity?.failedToAddTitleText || "Failed To Add"}`}
          confirmButtonColor=""
          confirmButtonText = {`${subscriptionBundleSettingsEntity?.okBtnText || "ok"}`}
          show={error}
          text={errorMessage}
          type="error"
          onConfirm={() => {
            setError(false);
            setErrorMessage("");
          }}
        />
        <div className="appstle_selected_product_list" onDragOver={event => onDragOver(event)}
             onDrop={event => onDrop(event, 'dropable')}>
          <header className="appstleBundle_selectedProduct_header d-flex justify-content-center align-items-center">
            {/* <button className="appstleBundle_back" onClick={() => setCartSliderOpen(false)}>
              <FontAwesomeIcon icon={faAngleLeft} color="#007bff" className="mr-1"/>
            </button> */}
            {/* <div>{subscriptionBundleSettingsEntity?.myDeliveryText ? subscriptionBundleSettingsEntity.myDeliveryText.replace(`{{selectedSellingPlanDisplayName}}`, selectedSellingPlanDisplayName) : `My ${selectedSellingPlanDisplayName} delivery`}</div> */}
            <button disabled={(minProduct && (_.sumBy(selectedProducts, 'quantity') < minProduct)) || (maxProduct && (_.sumBy(selectedProducts, 'quantity') > maxProduct)) || isCheckingOut} onClick={addToCart}
                    className="w-100 btn appstle-checkout-button" style={{
              fontSize: '13px',
              height: "36px",
              margin: '7px',
              borderRadius: "4px",
              backgroundColor: subscriptionBundleSettingsEntity?.buttonBackgroundColor || "#fcba03",
              borderColor: subscriptionBundleSettingsEntity?.buttonBackgroundColor || "#fcba03",
              color: subscriptionBundleSettingsEntity?.buttonColor || "#3a3a3a"
            }}>
              {(minProduct && (_.sumBy(selectedProducts, 'quantity') < minProduct)) ? (
                `${subscriptionBundleSettingsEntity?.selectMinimumProductButtonText ? subscriptionBundleSettingsEntity.selectMinimumProductButtonText.replace(`{{minProduct}}`, minProduct) : `Please select minimun ${minProduct} products`}`
              ) : (isCheckingOut ? <div className="appstle_loadersmall"/>
                  : `${subscriptionBundleSettingsEntity?.proceedToCheckoutButtonText || "Proceed to checkout"}`
              )}
            </button>
          </header>
          {selectedProducts?.length > 0 ? (
            <div className={`appstle_droppable`}>
              <div className="">
                {selectedProducts?.map((each, idx) => {
                  return (
                    <div key={each.variant.id} className="appstleBundle_selectedproduct">
                      <SelectedProduct
                        bundleData={props.bundleData}
                        product={each.product}
                        variant={each.variant}
                        quantity={each.quantity}
                        onProductAdd={onProductAdd}
                        showRemoveProduct
                        showQuantity
                        onProductRemove={onProductRemove}
                        selectedSellingPlan={selectedSellingPlan}
                        subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                      />
                    </div>
                  );
                })}
              </div>
            </div>
          ) : (
            <div className="appstleBundle_selectedProduct_placeholder ml-auto mr-auto mt-auto mb-auto">
              {subscriptionBundleSettingsEntity?.productsToProceedText || `Please select products to proceed`}
            </div>
          )}
          <button disabled={(_.sumBy(selectedProducts, 'quantity') < minProduct) || (_.sumBy(selectedProducts, 'quantity') > maxProduct) || isCheckingOut} onClick={addToCart}
                  style={{backgroundColor: subscriptionBundleSettingsEntity?.buttonBackgroundColor || "#fcba03"}}
                  className="mt-auto btn appstle-checkout-button" style={{
            borderRadius: '0',
            fontSize: '13px',
            height: "36px",
            margin: '7px',
            borderRadius: "4px",
            backgroundColor: subscriptionBundleSettingsEntity?.buttonBackgroundColor || "#fcba03",
            borderColor: subscriptionBundleSettingsEntity?.buttonBackgroundColor || "#fcba03",
            color: subscriptionBundleSettingsEntity?.buttonColor || "#3a3a3a"
          }}>
            {_.sumBy(selectedProducts, 'quantity') < minProduct ? (
              `${subscriptionBundleSettingsEntity?.selectMinimumProductButtonText ? subscriptionBundleSettingsEntity.selectMinimumProductButtonText.replace(`{{minProduct}}`, minProduct) : `Please select minimun ${minProduct} products`}`
            ) : (isCheckingOut ? <div className="appstle_loadersmall"/>
                : `${subscriptionBundleSettingsEntity?.proceedToCheckoutButtonText || "Proceed to checkout"}`
            )}
          </button>
        </div>
      </div>
    </>
  );
}

export default ProductSelection;
