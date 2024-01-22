import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ISubscriptionWidgetSettings } from 'app/shared/model/subscription-widget-settings.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './subscription-widget-settings.reducer';

export interface ISubscriptionWidgetSettingsDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionWidgetSettingsDeleteDialog = (props: ISubscriptionWidgetSettingsDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/subscription-widget-settings');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.subscriptionWidgetSettingsEntity.id);
  };

  const { subscriptionWidgetSettingsEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Confirm delete operation</ModalHeader>
      <ModalBody id="subscriptionApp.subscriptionWidgetSettings.delete.question">
        Are you sure you want to delete this SubscriptionWidgetSettings?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-subscriptionWidgetSettings" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ subscriptionWidgetSettings }: IRootState) => ({
  subscriptionWidgetSettingsEntity: subscriptionWidgetSettings.entity,
  updateSuccess: subscriptionWidgetSettings.updateSuccess
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionWidgetSettingsDeleteDialog);
