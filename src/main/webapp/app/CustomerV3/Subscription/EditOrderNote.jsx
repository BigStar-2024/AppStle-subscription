import React, {useEffect, useState} from 'react';
import {connect, useSelector} from 'react-redux';
import 'react-datepicker/dist/react-datepicker.css';
import {Input} from 'reactstrap';
import {toast} from 'react-toastify';
import {getCustomerPortalEntity} from 'app/entities/subscriptions/subscription.reducer';
import Axios from 'axios';
import TailwindModal from './TailwindModal';
import PopupMessaging from './PopupMessaging';
import {
  getSubscriptionContractDetailsByContractId
} from 'app/entities/subscription-contract-details/user-subscription-contract-details.reducer';
import {updateEntity} from "app/entities/subscription-contract-details/subscription-contract-details.reducer";

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};

function EditOrderNote(props) {
  const {
    contractId,
    shopName,
    getCustomerPortalEntity,
    orderNote,
    pageName,
    id,
    subscriptionContractFreezeStatus,
    subscriptionContractDetails,
    updateEntity
  } = props;

  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  const subscriptionEntities = useSelector(state => state.subscription.entity);
  let [updateInProgress, setUpdateInProgress] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [orderNoteLocal, setOrderNoteLocal] = useState(orderNote);

  const [isOpen, setIsOpen] = useState(false);

  const [showMessaging, setShowMessaging] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);

  useEffect(() => {
    setOrderNoteLocal(orderNote);
  }, [orderNote]);

  const updateOrderNote = async () => {
    setUpdateInProgress(true);
    if (pageName == 'UPCOMIG_ORDER') {
      const requestUrl = `api/subscription-billing-attempts-update-order-note/${id}?orderNote=${orderNoteLocal}&isExternal=true`;
      return Axios.put(requestUrl)
        .then(res => {
          props.getSubscriptionContractDetailsByContractId(id);
          setUpdateInProgress(false);
          toast.success('Upcoming Order Updated', options);
          setEditMode(!editMode);
          return res;
        })
        .catch(err => {
          setUpdateInProgress(false);
          toast.error('Upcoming Order Failed', options);
          return err;
        });
    } else {
      const requestUrl = `api/subscription-contract-details`;
      const entity = {...subscriptionContractDetails, orderNote: orderNoteLocal}
      return Axios.put(requestUrl, entity)
        .then(res => {
          props.getSubscriptionContractDetailsByContractId(props.contractId);
          getCustomerPortalEntity(contractId);
          setUpdateInProgress(false);
          toast.success(customerPortalSettingEntity?.contractUpdateMessageTextV2 || 'Contract Updated', options);
          setEditMode(!editMode);
          return res;
        })
        .catch(err => {
          setUpdateInProgress(false);
          toast.error(customerPortalSettingEntity?.contractErrorMessageTextV2 || 'Contract Update Failed', options);
          return err;
        });
    }
  };

  const orderNoteChangeHandler = note => {
    setOrderNoteLocal(note);
  };

  const updateOrderNoteHandler = async () => {
    let results = await updateOrderNote();
    if (results) {
      setShowMessaging(true);
      if (results?.status === 200) {
        setSuccess(true);
        setError(false);
      } else {
        setSuccess(false);
        setError(true);
      }
    }
  };

  const resetModal = () => {
    setIsOpen(false);
    setShowMessaging(false);
    setSuccess(false);
    setError(false);
  };

  return (
    <div>
      <>
        <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-edit-ordernote">
          <div className="as-flex as-justify-between as-mb-2">
            <p className="as-text-sm as-text-gray-500 as-card_title as-edit-ordernote_title">
              {customerPortalSettingEntity?.orderNoteText}
            </p>
            {customerPortalSettingEntity?.enableEditOrderNotes && <p
              className="as-text-sm as-text-blue-500 as-cursor-pointer as-cta as-card_cta as-edit-ordernote_cta"
              onClick={() => setIsOpen(true)}
            >
              {customerPortalSettingEntity?.updateButtonText}
            </p>}
          </div>
          <p class="as-text-sm as-text-gray-800 as-pt-3 as-card_data as-edit-ordernote_data">
            {orderNote || customerPortalSettingEntity?.noOrderNotAvailableMessageV2 || 'No Order Note Added'}
          </p>
        </div>
        <TailwindModal
          open={isOpen}
          setOpen={resetModal}
          actionMethod={updateOrderNoteHandler}
          actionButtonText={!showMessaging ? customerPortalSettingEntity?.updateChangeOrderBtnTextV2 : ''}
          updatingFlag={updateInProgress}
          modalTitle={customerPortalSettingEntity?.orderNoteText || 'Order Note'}
          className="as-model-order-note"
          success={success}
        >
          {showMessaging && <PopupMessaging showSuccess={success} showError={error} />}
          {!showMessaging && (
            <div className="as-text-sm as-text-gray-500">
              <div>
                <label htmlFor="orderNote" className="as-block as-text-sm as-font-medium as-text-gray-700">
                  {customerPortalSettingEntity?.enterOrderNoteLblTextV2 || 'Enter Order Note'}
                </label>
                <Input
                  value={orderNoteLocal}
                  type="text"
                  onChange={event => orderNoteChangeHandler(event.target.value)}
                  name="orderNote"
                  className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md"
                />
              </div>
            </div>
          )}
        </TailwindModal>
      </>
    </div>
  );
}

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getCustomerPortalEntity,
  getSubscriptionContractDetailsByContractId,
  updateEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(EditOrderNote);
