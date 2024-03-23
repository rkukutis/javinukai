import { useState } from "react";
import Modal from "../Modal";
import ArchivedContestParticipants from "./ArchivedContestParticipants";
import moment from "moment";
import { useTranslation } from "react-i18next";

function ArchivedContestCard({ contest }) {
  const { t } = useTranslation();
  const [pastParticipantsModalOpen, setPastParticipantsModalOpen] =
    useState(false);
  const { contestName, contestDescription, categories, startDate, endDate } =
    contest;

  return (
    <>
      <div onClick={() => setPastParticipantsModalOpen(true)}>
        <div>
          <p>
            <b>{contestName}</b>
          </p>
        </div>
        <div>{contestDescription}</div>
        <div>
          <p>
            <b>{categories}</b>
          </p>
        </div>
        <div>
          {t("ArchivePage.startDate")} {" - "}
          {moment(startDate).format("YYYY-MM-DD")}
        </div>
        <div>
          {" "}
          {t("ArchivePage.endDate")} {" - "}
          {moment(endDate).format("YYYY-MM-DD")}
        </div>
      </div>
      <Modal
        isOpen={pastParticipantsModalOpen}
        setIsOpen={setPastParticipantsModalOpen}
      >
        <ArchivedContestParticipants
          contest={contest}
          close={() => setPastParticipantsModalOpen(false)}
        />
      </Modal>
    </>
  );
}

export default ArchivedContestCard;
