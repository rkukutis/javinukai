import { useState } from "react";
import Modal from "../Modal";
import ArchivedContestParticipants from "./ArchivedContestParticipants";

function ArchivedContestCard({ contest }) {
  const [pastParticipantsModalOpen, setPastParticipantsModalOpen] =
    useState(false);
  const {
    contestName,
    contestDescription,
    categories,
    winners,
    startDate,
    endDate,
  } = contest;

  return (
    <div onClick={() => setPastParticipantsModalOpen(true)}>
      <div>{contestName}</div>
      <div>{contestDescription}</div>
      <div>{categories}</div>
      <div>{winners}</div>
      <div>{startDate}</div>
      <div>{endDate}</div>
      <Modal
        isOpen={pastParticipantsModalOpen}
        setIsOpen={setPastParticipantsModalOpen}
      >
        <ArchivedContestParticipants
          contest={contest}
          onClose={() => setPastParticipantsModalOpen(false)}
        />
      </Modal>
      ;
    </div>
  );
}

export default ArchivedContestCard;
