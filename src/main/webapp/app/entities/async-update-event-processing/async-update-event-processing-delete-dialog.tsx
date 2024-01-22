import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IAsyncUpdateEventProcessing } from 'app/shared/model/async-update-event-processing.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './async-update-event-processing.reducer';

export interface IAsyncUpdateEventProcessingDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AsyncUpdateEventProcessingDeleteDialog = (props: IAsyncUpdateEventProcessingDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/async-update-event-processing' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.asyncUpdateEventProcessingEntity.id);
  };

  const { asyncUpdateEventProcessingEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Confirm delete operation</ModalHeader>
      <ModalBody id="subscriptionApp.asyncUpdateEventProcessing.delete.question">
        Are you sure you want to delete this AsyncUpdateEventProcessing?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-asyncUpdateEventProcessing" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ asyncUpdateEventProcessing }: IRootState) => ({
  asyncUpdateEventProcessingEntity: asyncUpdateEventProcessing.entity,
  updateSuccess: asyncUpdateEventProcessing.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AsyncUpdateEventProcessingDeleteDialog);
