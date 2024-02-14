import React from 'react';
import PropTypes from 'prop-types';

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

  return (
    <>
      {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50" >
          <div className="fixed inset-0 bg-gray-900 opacity-50" onClick={handleBackdropClick}></div>
          <div className="bg-white p-8 rounded-lg z-50" id={id}>
            {children}
            
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
