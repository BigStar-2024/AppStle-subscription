import React, {useState, useEffect  } from 'react';
import { Row, Col, Button, Badge, FormGroup, Input  } from 'reactstrap';
import Variation1 from './Variation1'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {  faLock } from '@fortawesome/free-solid-svg-icons';

function AddNewProduct(props) {
    const [isOpened, setIsOpened] = useState(false);
    const { shopName, contractId, sellingPlanIds, upcomingOrderId, isPrepaid, fullfillmentId, customerPortalSettingEntity, subscriptionContractFreezeStatus} = props;
    const [formStep, setFormStep] = useState(1);

    return (
        <div>
        {!isOpened && <div style={{display: "flex", justifyContent: "end", fontSize: "14px", marginBottom: "10px", marginTop: "15px"}}>
        {
            subscriptionContractFreezeStatus ?
            <span style={{marginLeft: 'auto', color: "#13b5ea", textDecoration: "underline", cursor: "pointer"}} >
            <b> <FontAwesomeIcon
                    className='mr-2'
                      icon={faLock}/> {customerPortalSettingEntity?.clickHereTextV2 || "Click here"}</b>
            </span>
            :
            <span style={{marginLeft: 'auto', color: "#13b5ea", textDecoration: "underline", cursor: "pointer"}} 
            onClick={() => setIsOpened(true)}>
            <b>{customerPortalSettingEntity?.clickHereTextV2 || "Click here"}</b>
            </span>
        }
       &nbsp; 
       <span>{customerPortalSettingEntity?.productAddMessageTextV2 || "to add products in this contract."}</span></div>}
        {isOpened && 
        <div className="" style={{boxShadow: "rgb(0 0 0 / 15%) 0px 0px 4px 0px"}}>
        <div style={{fontSize: "1rem",
            color: "#465661",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            padding: ".75rem 1.25rem",
            borderBottom: "1px solid rgba(0,0,0,.125)"}}>
                {customerPortalSettingEntity?.addProductButtonTextV2 || "Add Product"}
                <Button
                color="danger"
                className="btn-shadow btn-wide float-right  btn-hover-shine"
                style={{marginLeft: "auto"}}
                onClick={() => setIsOpened(false)}
                >
                {customerPortalSettingEntity?.closeBtnTextV2 || "Close"}
                </Button>
        </div>
        <Variation1 
            sellingPlanIds ={sellingPlanIds}
            isOpen ={isOpened}
            formStep ={formStep}
            setFormStep ={setFormStep}
            upcomingOrderId ={upcomingOrderId}
            modalHeader = {customerPortalSettingEntity?.selectProductLabelTextV2 || "Select Products"}
            shopName= {shopName}
            contractId ={contractId}
            customerPortalSettingEntity={customerPortalSettingEntity}
        />
        {/* <div style={{fontSize: "1.2rem", color: "#465661", paddingLeft: "40px", marginTop: "15px"}}>Currently Added Products</div>
        <div style={{padding: "10px 20px 20px 20px", display: "flex"}}>       
            <div style={{display: "flex", alignItems: "center", flexGrow: 1}}>
                <span style={{
                    width: "112px",
                    height: "1px",
                    borderBottom: "1px solid #465661",
                    lineHeight: "1rem",
                    width: "100%",
                    color: "#465661"
                }}></span>
            </div>
        </div> */}
        </div>}
        </div>
    )
}

export default AddNewProduct;
