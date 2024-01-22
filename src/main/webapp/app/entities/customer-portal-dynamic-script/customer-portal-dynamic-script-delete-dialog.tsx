import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICustomerPortalDynamicScript } from 'app/shared/model/customer-portal-dynamic-script.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './customer-portal-dynamic-script.reducer';

export interface ICustomerPortalDynamicScriptDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerPortalDynamicScriptDeleteDialog = (props: ICustomerPortalDynamicScriptDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/customer-portal-dynamic-script');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.customerPortalDynamicScriptEntity.id);
  };

  const { customerPortalDynamicScriptEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Confirm delete operation</ModalHeader>
      <ModalBody id="subscriptionApp.customerPortalDynamicScript.delete.question">
        Are you sure you want to delete this CustomerPortalDynamicScript?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-customerPortalDynamicScript" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ customerPortalDynamicScript }: IRootState) => ({
  customerPortalDynamicScriptEntity: customerPortalDynamicScript.entity,
  updateSuccess: customerPortalDynamicScript.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPortalDynamicScriptDeleteDialog);
