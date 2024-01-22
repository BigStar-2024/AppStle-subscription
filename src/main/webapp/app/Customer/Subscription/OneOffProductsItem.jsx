import React, {useState, useEffect} from 'react';
import { connect, useSelector } from 'react-redux';
import {
  deleteOneOffProducts, getCustomerPortalEntity
} from 'app/entities/subscriptions/subscription.reducer';

const OneOffProductsItem = ({getCustomerPortalEntity, deleteOneOffProducts, item, ...props}) => {
    let [deleteinProgress, setDeleteInProgress] = useState(false);
    let [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
    const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);

    useEffect(() => {
        setDeleteInProgress(false);
        setShowDeleteConfirmation(false);
    }, [])

    const deleteOneTimeVariants = async (variantId, contractId, billingID) => {
        setDeleteInProgress(true);
        await deleteOneOffProducts(variantId, contractId, billingID);
        // setDeleteInProgress(false);
        // setShowDeleteConfirmation(false);
    }

    return (
        <div className="appstle_font_size" style={{marginTop: '5px', color: '#13b5ea', display: "flex", alignItems: "center"}}>
            {item.variantName}<sup>*</sup> {!deleteinProgress && !showDeleteConfirmation && <span style={{color: "red", textDecoration: "underline", marginLeft: "5px", cursor: "pointer"}} onClick={() => setShowDeleteConfirmation(true)}>{customerPortalSettingEntity?.deleteButtonText || "delete"}</span>}
            {deleteinProgress && <div className="appstle_loadersmall" style={{margin: "0", marginLeft: "5px"}} />}
            {!deleteinProgress && showDeleteConfirmation && <div className="confirmText">
                <div style={{color: "#495661"}}>{customerPortalSettingEntity?.deleteConfirmationMsgTextV2 || "Are you sure?"}</div>
                <div className="confirmButtonWrapper">
                    <div onClick={() => deleteOneTimeVariants(props?.id, props?.contractId, props?.billingId)} className="appstle_font_size confirmButton">{customerPortalSettingEntity?.yesBtnTextV2 || "Yes"}</div>
                    <div className="rejectButton"  onClick={() => setShowDeleteConfirmation(false)}>{customerPortalSettingEntity?.noBtnTextV2 || "No"}</div>
                </div>
            </div>}
        </div>
    )
}

const mapStateToProps = state => ({});

const mapDispatchToProps = {
    deleteOneOffProducts,
    getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(OneOffProductsItem);