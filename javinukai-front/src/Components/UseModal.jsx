import { useState } from "react";

const useModal = () => {
  const [modalStates, setModalStates] = useState({});

  const openModal = (modalId) => {
    setModalStates((prevState) => ({
      ...prevState,
      [modalId]: true,
    }));
  };

  const closeModal = (modalId) => {
    setModalStates((prevState) => ({
      ...prevState,
      [modalId]: false,
    }));
  };

  const handleConfirm = (actionType, modalId) => {
    switch (actionType) {
      case "consoleLog":
        console.log("Participant confirmed");
        closeModal(modalId);
        break;
      case "redirect":
        window.location.href = "/login";
        closeModal(modalId);
        break;
      default:
        break;
    }
  };

  return { modalStates, openModal, closeModal, handleConfirm };
};

export default useModal;