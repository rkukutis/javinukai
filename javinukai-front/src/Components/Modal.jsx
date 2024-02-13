import React, { useState } from 'react';

const Modal = ({ openModalText, confirmText, redirectText, actionType }) => {
  const [isOpen, setIsOpen] = useState(false);

  const openModal = () => {
    setIsOpen(true);
  };

  const closeModal = () => {
    setIsOpen(false);
  };

  const handleBackdropClick = () => {
    closeModal();
  };

  const handleAction = () => {
    switch (actionType) {
      case 'confirm':
        console.log("Participant confirmed");
        break;
      case 'redirect':
        window.location.href = '/register'; 
        break;
      default:
        break;
    }
    closeModal(); 
  };

  return (
    <>
      <button onClick={openModal} className="bg-blue-500 text-white px-4 py-2 rounded">{openModalText}</button>
      {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
          <div className="fixed inset-0 bg-gray-900 opacity-50" onClick={handleBackdropClick}></div>
          <div className="bg-white p-8 rounded-lg z-50">
            <h2 className="text-xl font-semibold mb-4">
              {actionType === 'confirm' ? 'Participation Confirmation' : 'Registration Confirmation'}
            </h2>
            <p>
              {actionType === 'confirm' 
                ? 'Do you want to participate in this contest?' 
                : 'Would you like to register for this contest?'
              }
            </p>
            <div className="mt-4 flex justify-between">
              <button onClick={closeModal} className="px-4 py-2 bg-gray-300 text-gray-800 rounded mr-4">No</button>
              <button onClick={handleAction} className="px-4 py-2 bg-blue-500 text-white rounded">
                {actionType === 'confirm' ? confirmText : redirectText}
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default Modal;
