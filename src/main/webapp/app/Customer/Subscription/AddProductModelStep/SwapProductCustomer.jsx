import React, { Fragment, useState, useEffect } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faLock} from '@fortawesome/free-solid-svg-icons';
import {
  Row,
  Col,
  Button
} from 'reactstrap';
import Axios from 'axios';
import {
  getCustomerPortalEntity,
} from 'app/entities/subscriptions/subscription.reducer';
import { toast } from 'react-toastify';
import {resetProductOptions} from 'app/entities/fields-render/data-product.reducer';
import Step2 from './Step2';
import { connect, useSelector } from 'react-redux';

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};


function SwapProductCustomer(props) {
  const { shopName, contractId, getCustomerPortalEntity, sellingPlanIds, upcomingOrderId, formStep, setFormStep, loading, lineId, resetProductOptions, customerPortalSettingEntity, subscriptionContractFreezeStatus } = props;
  const [isOpened, setIsOpened] = useState(false);
  const [updateInProgress, setUpdateInProgress] = useState(false);
  const [selectedVariantItems, setSelectedVariantItems] = useState([]);
  const [purchaseOption, setPurchaseOption] = useState("SUBSCRIBE");
  const [errors, setErrors] = useState([]);


  useEffect(() => {
    resetProductOptions();
  }, [purchaseOption])

    useEffect(() => {
        if ((customerPortalSettingEntity?.addOneTimeProduct && customerPortalSettingEntity?.addAdditionalProduct) || !upcomingOrderId) {
          setPurchaseOption("SUBSCRIBE");
        } else if (customerPortalSettingEntity?.addOneTimeProduct) {
          setPurchaseOption("ONE_TIME");
        } else if (customerPortalSettingEntity?.addAdditionalProduct) {
          setPurchaseOption("SUBSCRIBE");
        }
      }, [customerPortalSettingEntity])


      const addProduct = (selectedItems, tempError) => {
        if (!tempError) {
          tempError = [];
        }
        setUpdateInProgress(true);
        let newSelectedItems = JSON.parse(JSON.stringify(selectedItems))
        let selectProduct = newSelectedItems?.shift();
        if(!selectProduct)
        {
            setUpdateInProgress(false);
            // props.toggle();
            setErrors([...tempError])
            getCustomerPortalEntity(contractId);
            return;
        }
        else if(selectProduct && purchaseOption == "ONE_TIME")
        {
            let oneTimeUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=1&variantId=${selectProduct?.id}&isOneTimeProduct=true`;
    
            Axios.put(oneTimeUrl)
            .then(res => {
                tempError.push({success: true, item: selectProduct})
                addProduct(newSelectedItems, tempError);
              // toast.success('Contract Updated', options);
              if(!newSelectedItems?.length) {
                deleteProduct()
                }
              }
            )
            .catch(err => {
                console.log(err);
                tempError.push({success: false, item: selectProduct, err: err?.response?.data?.message})
                addProduct(newSelectedItems, tempError);
                // setUpdateInProgress(false);
                // props.toggle();
              // toast.error('Contract Update Failed', options);
            });
        }
        else if (selectProduct && purchaseOption == "SUBSCRIBE") {
          const requestUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=1&variantId=${selectProduct?.id}`;
          Axios.put(requestUrl)
            .then(res => {
                // setUpdateInProgress(false);
                // toast.success('Contract Updated', options);
                tempError.push({success: true, item: selectProduct})
                addProduct(newSelectedItems, tempError);
                if(!newSelectedItems?.length) {
                  deleteProduct()
                }
              }
            )
            .catch(err => {
                console.log(err);
                tempError.push({success: false, item: selectProduct, err: err?.response?.data?.message})
                addProduct(newSelectedItems, tempError);
              //   props.toggle();
              // setUpdateInProgress(false);
              // toast.error('Contract Update Failed', options);
            });
        }
      };


      const deleteProduct = () => {
        // setDeleteInProgress(true);
        // setHandleAlert({ show: false, data: null });
        const requestUrl = `api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}&isExternal=true`;
        Axios
          .put(requestUrl)
          .then(res => {
            getCustomerPortalEntity(contractId);
            setUpdateInProgress(false);
            setIsOpened(false);
            // setDeleteInProgress(false);
    
            toast.success('Product deleted from contract', options);
          })
          .catch(err => {
            console.log('Error in deleting product');
            setUpdateInProgress(false);
            getCustomerPortalEntity(contractId);
            // setDeleteInProgress(false);
            toast.error(err.response.data.message, options);
          });
      };
  return (
      <>
       <Col md={2}>
      </Col>
       <Col md={10}>
        {!isOpened && <div style={{display: "flex", justifyContent: "start", fontSize: "14px", marginBottom: "10px", marginTop: "15px"}}>
        {
            subscriptionContractFreezeStatus ?
            <span style={{color: "#13b5ea", textDecoration: "underline", cursor: "pointer"}} >
            <b> <FontAwesomeIcon
                    className='mr-2'
                      icon={faLock}/> {customerPortalSettingEntity?.clickHereTextV2 || "Click here"}</b>
            </span>
            :
            <span style={{color: "#13b5ea", textDecoration: "underline", cursor: "pointer"}} 
            onClick={() => setIsOpened(true)}>
            <b>{customerPortalSettingEntity?.clickHereTextV2 || "Click here"}</b>
            </span>
        }
       &nbsp; 
       <span>{customerPortalSettingEntity?.swapProductLabelTextV2 || "to swap the current product."}</span></div>}
        {isOpened && 
        <div className="" style={{boxShadow: "rgb(0 0 0 / 15%) 0px 0px 4px 0px"}}>
        <div style={{fontSize: "1rem",
            color: "#465661",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            padding: ".75rem 1.25rem",
            borderBottom: "1px solid rgba(0,0,0,.125)"}}>
                {customerPortalSettingEntity?.swapProductBtnTextV2 || "Swap Product"}
                <Button
                    color="danger"
                    className="btn-shadow btn-wide float-right  btn-hover-shine"
                    style={{marginLeft: "auto"}}
                    onClick={() => setIsOpened(false)}
                >
                {customerPortalSettingEntity?.closeBtnTextV2 || "Close"}
                </Button>
        </div>
        {
         isOpened &&
         <>

          <Step2
            isMultiSelect = {false}
            purchaseOption ={purchaseOption}
            sellingPlanIds = {sellingPlanIds}
            contractId={contractId}
            updateInProgress ={updateInProgress}
            selectedVariantItems ={setSelectedVariantItems}
            customerPortalSettingEntity={customerPortalSettingEntity}
            totalTitle="Select Source Product"
            methodName="Save"
            searchBarPlaceholder = {customerPortalSettingEntity?.swapProductSearchBarTextV2 || "Search a product to swap"}
            noProductDataMessage = {customerPortalSettingEntity?.noProductFoundMessageV2 || "There are no product available on given search result"}
        />
          <Button
              color="primary"
              className="btn-shadow btn-wide float-right btn-pill btn-hover-shine"
              onClick ={()=> addProduct(selectedVariantItems)}
              disabled={!selectedVariantItems}
            >
              {(updateInProgress) ? <div className="appstle_loadersmall" /> : `${customerPortalSettingEntity?.swapProductBtnTextV2 || 'Swap Product'}`}
            </Button>
         </>
           
        
        }
        </div>}
        </Col>
      </>
     
  )
}

const mapStateToProps = state => ({
    loading: state.subscription.loading,
  });
  
  const mapDispatchToProps = {
    getCustomerPortalEntity,
    resetProductOptions
  };
  
  export default connect(mapStateToProps, mapDispatchToProps)(SwapProductCustomer);