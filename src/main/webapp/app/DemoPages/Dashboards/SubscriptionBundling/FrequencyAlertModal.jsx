import React, { Fragment } from 'react';
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter
} from 'reactstrap';
import frequencyAlertImage from '../../../../static/theme/assets/utils/images/widget-setting/subscription_group_exist.png';

function FrequencyAlertModal(props) {
  const {
    isFrequencyAlertModelOpen,
    toggleUpdateOrderNoteModal,
    seletedPlans
  } = props;


  return (
    <Fragment>
      <div>
        <Modal isOpen={isFrequencyAlertModelOpen} size="lg" toggle={toggleUpdateOrderNoteModal}>
          <ModalHeader toggle={toggleUpdateOrderNoteModal}>Frequencies of the selling plans are not identical.</ModalHeader>
          <ModalBody>
            <div>
              <p>
                Unfortunately, we cannot merge the <b>{seletedPlans[0]?.label}</b> and <b>{seletedPlans[seletedPlans.length - 1]?.label}</b> because they are not identical
                in regards to <b>frequencies</b>.
              </p>
              <p>
                In order to resolve the issue, please make sure the plans you are planning to merge together in a Build-A-Box are having
                identical frequencies.
              </p>
              <h5>What are identical frequencies?</h5>
              <p>
                The subscription frequency that you are defining in while setting up a plan in <b>Manage Plans</b> needs to be same in
                accordance to the <b>{seletedPlans[0]?.label}</b>. You need to make sure all the following fields are same and identical in
                all the plans you are planning to merge:
              </p>

              <img
                src={frequencyAlertImage}
                width="100%"
                style={{
                  borderRadius: '15px',
                  border: '1px solid darkblue'
                }}
              ></img>
            </div>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" onClick={toggleUpdateOrderNoteModal}>
              Ok
            </Button>
          </ModalFooter>
        </Modal>
      </div>
    </Fragment>
  );
}

export default FrequencyAlertModal;
