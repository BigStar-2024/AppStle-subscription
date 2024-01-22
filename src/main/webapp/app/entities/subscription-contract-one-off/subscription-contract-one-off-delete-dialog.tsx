import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ISubscriptionContractOneOff } from 'app/shared/model/subscription-contract-one-off.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './subscription-contract-one-off.reducer';

export interface ISubscriptionContractOneOffDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionContractOneOffDeleteDialog = (props: ISubscriptionContractOneOffDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/subscription-contract-one-off' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.subscriptionContractOneOffEntity.id);
  };

  const { subscriptionContractOneOffEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Confirm delete operation</ModalHeader>
      <ModalBody id="subscriptionApp.subscriptionContractOneOff.delete.question">
        Are you sure you want to delete this SubscriptionContractOneOff?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-subscriptionContractOneOff" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ subscriptionContractOneOff }: IRootState) => ({
  subscriptionContractOneOffEntity: subscriptionContractOneOff.entity,
  updateSuccess: subscriptionContractOneOff.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionContractOneOffDeleteDialog);
