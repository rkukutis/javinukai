import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

const Modal = ({ isOpen, closeModal, children, id, openModal }) => {
  const handleBackdropClick = () => {
    if (closeModal) {
      closeModal();
    }
  };

  const handleOpenModal = () => {
    if (openModal) {
      openModal(id); 
    }
  };

  const { t } = useTranslation();

  return (
    <>
      {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50" >
          <div className="fixed inset-0 bg-gray-900 opacity-50" onClick={handleBackdropClick}></div>
          <div className="bg-white p-8 rounded-lg z-50" id={id}>
            {children}
            <button className="bg-gray-300 text-gray-700 font-semibold py-2 px-4 rounded hover:bg-gray-400" onClick={closeModal}>{t('modal.closeModal')}</button>
          </div>
        </div>
      )}
    </>
  );
};

Modal.propTypes = {
  
  closeModal: PropTypes.func.isRequired,
  children: PropTypes.node.isRequired,
  id: PropTypes.string.isRequired, 
  openModal: PropTypes.func.isRequired 
};

export default Modal;
