import cat1 from "../assets/cat1.jpg";
import cat2 from "../assets/cat2.jpg";
import cat3 from "../assets/cat3.jpg";
import Button from "../Components/Button";
import Modal from "../Components/Modal";
import useModal from "../Components/UseModal";
import useUserStore from "../stores/userStore";
import { useTranslation } from "react-i18next";

function HomePage() {

  const { t } = useTranslation()
  const { user } = useUserStore((state) => state);
  const { modalStates, openModal, closeModal, handleConfirm } = useModal();
  
  return (
    
    <div className="flex flex-col items-center p-10 ">
      <div className="flex items-center mx-4 mb-4 justify-center" style={{ flexDirection: window.innerWidth > 768 ? 'row' : 'column' }}>
        
        <img
          src={cat1}
          alt="cat1"
          className="max-w-full h-auto object-cover"
        
          style={{ width: "40%", height: "40%", maxHeight: "80vh" }}
        />
        <div className="flex flex-col items-center justify-center p-5">
          <p className="ml-4">contest 20</p>
          <p>{t('homepage.description')}</p>
          {user ? (
            <Button onClick={() => openModal("modal1")}>{t('homepage.participate')}</Button>
          ) : (
            <Button onClick={() => openModal("modal2")}>{t('homepage.loginToParticipate')}</Button>
          )}
          <Modal
            isOpen={modalStates.modal1}
            closeModal={() => closeModal("modal1")}
            id="modal1"
            openModal={openModal} 
          >
            <h2 className="text-xl font-semibold mb-4">Participate</h2>
            <p className="text-gray-700 mb-4">{t('homepage.description')}</p>
            <button onClick={() => handleConfirm("consoleLog", "modal1")}>{t('homepage.confirmParticipation')}</button>
          </Modal>
          <Modal
            isOpen={modalStates.modal2}
            closeModal={() => closeModal("modal2")}
            id="modal2"
            openModal={openModal} 
          >
            <h2>{t('homepage.loginToParticipate')}</h2>
            <p>{t('homepage.description')}</p>
            <button onClick={() => handleConfirm("redirect", "modal2")}>LOG IN</button>
          </Modal>
         
        </div>
      </div>

      <div className="flex items-center mx-4 mb-4 justify-center" style={{ flexDirection: window.innerWidth > 768 ? 'row' : 'column' }}>
        <img
          src={cat2}
          alt="cat2"
          className="max-w-full max-h-full object-cover"
          style={{ width: "40%", height: "40%", maxHeight: "80vh" }}
        />
        <div className="fle items-center justify-center p-5">
          <p className="ml-4">contest 2091</p>
         
         <p>{t('homepage.description')}</p>
         {user ? (
            <Button onClick={() => openModal("modal1")}>{t('homepage.participate')}</Button>
          ) : (
            <Button onClick={() => openModal("modal2")}>{t('homepage.loginToParticipate')}</Button>
          )}
          <Modal
            isOpen={modalStates.modal1}
            closeModal={() => closeModal("modal1")}
            id="modal1"
            openModal={openModal} 
          >
            <h2 className="text-xl font-semibold mb-4">Participate</h2>
            <p className="text-gray-700 mb-4">{t('homepage.description')}</p>
            <button onClick={() => handleConfirm("consoleLog", "modal1")}>{t('homepage.confirmParticipation')}</button>
          </Modal>
          <Modal
            isOpen={modalStates.modal2}
            closeModal={() => closeModal("modal2")}
            id="modal2"
            openModal={openModal} 
          >
            <h2>{t('homepage.loginToParticipate')}</h2>
            <p>{t('homepage.description')}</p>
            <button onClick={() => handleConfirm("redirect", "modal2")}>LOG IN</button>
          </Modal>
         
        </div>
      </div>

      <div className="flex items-center mx-4 mb-4 justify-center" style={{ flexDirection: window.innerWidth > 768 ? 'row' : 'column' }}>
        <img
          src={cat3}
          alt="cat3"
          className="max-w-full max-h-full object-cover"
          style={{ width: "40%", height: "40%", maxHeight: "80vh" }}
        />
        <div className="fle items-center justify-center p-5">
          <p className="ml-4">contest 2018</p>
          <p>{t('homepage.description')}</p>
          {user ? (
            <Button onClick={() => openModal("modal1")}>{t('homepage.participate')}</Button>
          ) : (
            <Button onClick={() => openModal("modal2")}>{t('homepage.loginToParticipate')}</Button>
          )}
          <Modal
            isOpen={modalStates.modal1}
            closeModal={() => closeModal("modal1")}
            id="modal1"
            openModal={openModal} 
          >
            <h2 className="text-xl font-semibold mb-4">VISU KONTESTU PAVADINIMAI VIENODI?</h2>
            <p className="text-gray-700 mb-4">{t('homepage.description')}</p>
            <button onClick={() => handleConfirm("consoleLog", "modal1")}>{t('homepage.confirmParticipation')}</button>
          </Modal>
          <Modal
            isOpen={modalStates.modal2}
            closeModal={() => closeModal("modal2")}
            id="modal2"
            openModal={openModal} 
          >
            <h2>{t('homepage.loginToParticipate')}</h2>
            <p>{t('homepage.description')}</p>
            <button onClick={() => handleConfirm("redirect", "modal2")}>{t('homepage.login')}</button>
          </Modal>
        </div>
      </div>
    </div>
   
  );
}

export default HomePage;
