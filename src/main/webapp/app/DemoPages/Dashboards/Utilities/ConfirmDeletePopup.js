import React, { useState } from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import Loader from 'react-loaders'

const ConfirmDeletePopup = (props) => {
  const {
    buttonLabel,
    // className,
    onCloseModel,
    modaltitle,
    modalMessage,
    confirmBtnText,
    cancelBtnText,
    isOpenFlag,
    toggleModal,
    deleteId,
    deleteEntity,
    deleteLoading
  } = props;

//   const [isOpenFlag, setIsOpenFlag] = useState(false);

//   const toggle = () => setModal(!isOpenFlag);

  return (
    <div>
      <Modal isOpen={isOpenFlag} toggle={toggleModal}>
        <ModalHeader toggle={toggleModal}>{modaltitle}</ModalHeader>
        <ModalBody>
         <div style={{textAlign:'center', fontSize:'18px', fontWeight:'bold'}}>
            {modalMessage}
         </div>
        </ModalBody>
        <ModalFooter>
         {deleteLoading ? 
           <Loader color="red" type="ball-clip-rotate" /> : 
          <Button color="danger" onClick={()=>deleteEntity(deleteId)}>{confirmBtnText}</Button>}
          {!deleteLoading ?
          <Button color="secondary" onClick={onCloseModel}>{cancelBtnText}</Button> : null}
        </ModalFooter>
      </Modal>
    </div>
  );
}

export default ConfirmDeletePopup;