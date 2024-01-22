import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IActivityUpdatesSettings } from 'app/shared/model/activity-updates-settings.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './activity-updates-settings.reducer';

export interface IActivityUpdatesSettingsDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActivityUpdatesSettingsDeleteDialog = (props: IActivityUpdatesSettingsDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/activity-updates-settings' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.activityUpdatesSettingsEntity.id);
  };

  const { activityUpdatesSettingsEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Confirm delete operation</ModalHeader>
      <ModalBody id="subscriptionApp.activityUpdatesSettings.delete.question">
        Are you sure you want to delete this ActivityUpdatesSettings?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-activityUpdatesSettings" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ activityUpdatesSettings }: IRootState) => ({
  activityUpdatesSettingsEntity: activityUpdatesSettings.entity,
  updateSuccess: activityUpdatesSettings.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActivityUpdatesSettingsDeleteDialog);
