import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {IRootState} from 'app/shared/reducers';
import {deleteEntity, getEntity} from 'app/entities/bundle-rule/bundle-rule.reducer';

export const BundleRuleDeleteDialog = props => {
  const handleClose = () => {
    props.history.push('/dashboards/bundles');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.match.params.id);
  };

  const {bundleRuleEntity} = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Confirm delete operation</ModalHeader>
      <ModalBody id="subscriptionApp.bundleRule.delete.question">Are you sure you want to delete this
        BundleRule?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban"/>
          &nbsp; Cancel
        </Button>
        <Button disabled={props.updating} id="jhi-confirm-delete-bundleRule" color="danger" onClick={confirmDelete}>
          {props.updating ? (
            'Deleting'
          ) : (
            <>
              <FontAwesomeIcon icon="trash"/>
              &nbsp; Delete
            </>
          )}
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({bundleRule}: IRootState) => ({
  bundleRuleEntity: bundleRule.entity,
  updateSuccess: bundleRule.updateSuccess,
  updating: bundleRule.updating
});

const mapDispatchToProps = {getEntity, deleteEntity};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BundleRuleDeleteDialog);
