import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {RouteComponentProps} from 'react-router-dom';
import {Modal, ModalHeader, ModalBody, ModalFooter, Button} from 'reactstrap';
import {Translate, ICrudGetAction, ICrudDeleteAction} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IPortfolio} from 'app/shared/model/portfolio.model';
import {IRootState} from 'app/shared/reducers';
import {getEntity, deleteEntity} from './portfolio.reducer';

export interface IPortfolioDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}

export const PortfolioDeleteDialog = (props: IPortfolioDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/portfolio');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.portfolioEntity.userId + ',' + props.portfolioEntity.isin + ',' + props.portfolioEntity.date);
  };

  const {portfolioEntity} = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="eTradeApp.portfolio.delete.question">
        { /* <Translate contentKey="eTradeApp.portfolio.delete.question" interpolate={{ id: portfolioEntity.id }}>
          Are you sure you want to delete this Portfolio?
        </Translate> */ }
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban"/>
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-portfolio" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash"/>
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({portfolio}: IRootState) => ({
  portfolioEntity: portfolio.entity,
  updateSuccess: portfolio.updateSuccess
});

const mapDispatchToProps = {getEntity, deleteEntity};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PortfolioDeleteDialog);
