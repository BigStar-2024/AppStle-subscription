import React, { useState, useEffect, Fragment, useCallback } from 'react';
import { Button, Input, Label, FormGroup, FormFeedback, Row, Col, Popover, PopoverHeader, PopoverBody, Tooltip  } from 'reactstrap';
import axios from 'axios';
import { connect, useSelector } from 'react-redux';
// import './loader.scss';
import { toast } from 'react-toastify';
import _ from 'lodash';
import SweetAlert from 'sweetalert-react';
import './appstle-subscription.scss';
import { faPencilAlt, faTrash, faCheck, faTimes } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { motion, AnimatePresence } from "framer-motion";
import { formatPrice } from 'app/shared/util/customer-utils';
import {
  deleteOneOffProducts, getCustomerPortalEntity
} from 'app/entities/subscriptions/subscription.reducer';

const EditOneOffProduct = ({
  productQuantity,
  getCustomerPortalEntity,
  contractId,
  lineId,
  shop,
  productId,
  variantId,
  subscriptionEntities,
  totalProductPriceObj,
  productPrice,
  index,
  onComplete,
  shopInfo,
  currencyCode,
  deleteOneOffProducts,
  ...props
}) => {

  let [deleteInProgress, setDeleteInProgress] = useState(false);
  let [handleAlert, setHandleAlert] = useState({ show: false, data: null });
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  const deleteOneTimeVariants = async (variantId, contractId, billingID) => {
    setDeleteInProgress(true);
    setHandleAlert({ show: false, data: null });
    await deleteOneOffProducts(variantId, contractId, billingID);
    // setDeleteInProgress(false);
    // setShowDeleteConfirmation(false);
}

  return (
    <>
      <Col md={2} className="appstle_edit_delete_button_wrapper" id={`popover-product-oneoff-col${index}`}>
          <Popover container={document.getElementById(`popover-product-oneoff-col${index}`)} placement="bottom" isOpen={handleAlert.show} target={`popover-oneoff-index${index}`} toggle={() => setHandleAlert({ show: false, data: null })}>
              <PopoverHeader style={{fontWeight: 'bold', color: '#eb3023'}}>{customerPortalSettingEntity?.deleteConfirmationMsgTextV2 || "Are you sure?"}</PopoverHeader>
              <PopoverBody className="d-flex justify-content-center align-items-center">
                  <Button
                      style={{ padding: '8px 10px',backgroundColor: 'white', color: '#eb3023', border: '1px solid #eb3023' }}
                      className="d-flex align-items-center mr-1"
                      onClick={() =>  deleteOneTimeVariants(props?.id, contractId, props?.billingId)}
                    >
                    <FontAwesomeIcon  icon={faCheck} />
                    </Button>
                    <Button
                      style={{ padding: '8px 10px' }}
                      className="appstle_order-detail_update-button d-flex align-items-center"
                      onClick={()=>setHandleAlert({ show: false, data: null })}
                    >
                    <FontAwesomeIcon  icon={faTimes} />
                    </Button>
              </PopoverBody>
            </Popover>
          <Button
            style={{ padding: '10px 12px' }}
            id={`popover-oneoff-index${index}`}
            className="ml-2 appstle_deleteButton d-flex align-items-center"
            onClick={() => {
              setHandleAlert({ show: true, data: null });
            }}
          >
            {!deleteInProgress && (
              <FontAwesomeIcon
                icon={faTrash}
              />
            )}
            {deleteInProgress && <div className="appstle_loadersmall" />}
          </Button>
      </Col>
    </>
  );
};

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  getCustomerPortalEntity,
  deleteOneOffProducts
};

export default connect(mapStateToProps, mapDispatchToProps)(EditOneOffProduct);
