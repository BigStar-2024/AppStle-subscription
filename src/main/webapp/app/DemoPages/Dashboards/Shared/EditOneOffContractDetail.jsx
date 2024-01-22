import React, { useState, useEffect, Fragment, useCallback } from 'react';
import { Button, Input, Label, FormGroup, FormFeedback } from 'reactstrap';
import axios from 'axios';
import { getEntity, deleteOneOffProducts } from 'app/entities/subscriptions/subscription.reducer';
import { connect } from 'react-redux';
import './loader.scss';
import { toast } from 'react-toastify';

const EditOneOffContractDetail = ({
  variantId,
  billingID,
  contractId,
  deleteOneOffProducts
}) => {

  let [deleteInProgress, setDeleteInProgress] = useState(false);

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };;

  const deleteProduct = async () => {
    setDeleteInProgress(true);
    await deleteOneOffProducts(variantId, contractId, billingID).then(res => {
      toast.success('Product deleted from contract', options);
      return res
    }).catch((err) => {
      toast.error('Delete Product Failed', options);
      return err;
    })
    await getEntity(contractId);
    setDeleteInProgress(false);
};

  return (
    <>
      <div className="mt-3 d-flex">
          <Button color="danger" className="ml-2 primary btn btn-primary d-flex align-items-center" onClick={deleteProduct}>
            {!deleteInProgress && <i className="lnr lnr-trash btn-icon-wrapper"></i>}
            {deleteInProgress && <div className="appstle_loadersmall" />}
          </Button>
      </div>
    </>
  );
};

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  getEntity,
  deleteOneOffProducts
};

export default connect(mapStateToProps, mapDispatchToProps)(EditOneOffContractDetail);
