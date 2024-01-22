import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ISubscriptionBundleSettings } from 'app/shared/model/subscription-bundle-settings.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './subscription-bundle-settings.reducer';

export interface ISubscriptionBundleSettingsDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionBundleSettingsDeleteDialog = (props: ISubscriptionBundleSettingsDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/subscription-bundle-settings');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.subscriptionBundleSettingsEntity.id);
  };

  const { subscriptionBundleSettingsEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Confirm delete operation</ModalHeader>
      <ModalBody id="subscriptionApp.subscriptionBundleSettings.delete.question">
        Are you sure you want to delete this SubscriptionBundleSettings?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-subscriptionBundleSettings" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ subscriptionBundleSettings }: IRootState) => ({
  subscriptionBundleSettingsEntity: subscriptionBundleSettings.entity,
  updateSuccess: subscriptionBundleSettings.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBundleSettingsDeleteDialog);
