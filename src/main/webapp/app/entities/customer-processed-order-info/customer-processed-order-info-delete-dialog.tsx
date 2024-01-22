import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICustomerProcessedOrderInfo } from 'app/shared/model/customer-processed-order-info.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './customer-processed-order-info.reducer';

export interface ICustomerProcessedOrderInfoDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerProcessedOrderInfoDeleteDialog = (props: ICustomerProcessedOrderInfoDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/customer-processed-order-info');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.customerProcessedOrderInfoEntity.id);
  };

  const { customerProcessedOrderInfoEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Confirm delete operation</ModalHeader>
      <ModalBody id="subscriptionApp.customerProcessedOrderInfo.delete.question">
        Are you sure you want to delete this CustomerProcessedOrderInfo?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-customerProcessedOrderInfo" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ customerProcessedOrderInfo }: IRootState) => ({
  customerProcessedOrderInfoEntity: customerProcessedOrderInfo.entity,
  updateSuccess: customerProcessedOrderInfo.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerProcessedOrderInfoDeleteDialog);
