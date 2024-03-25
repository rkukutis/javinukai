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
      <div
        onClick={() => setPastParticipantsModalOpen(true)}
        className="w-full flex flex-col items-center bg-slate-100"
      >
        <div className="hover:bg-gray-100 lg:w-5/6 w-full h-fit bg-white shadow-md lg:my-4 p-8 rounded-md">
          <div>
            <p className="text-2xl">{contestName}</p>
          </div>
          <div className="lg:space-y-0 pb-2">{contestDescription}</div>
          <div>
            <p className="text-xl">{t("ArchivePage.categories")}</p>
            {categories.map((category) => {
              return (
                <p
                  className="text-teal-600"
                  key={category}
                >{`- ${category}`}</p>
              );
            })}
          </div>
          <div className="lg:grid lg:grid-cols-3 flex flex-col">
            <div>
              <span className="text-lg">
                {t("ArchivePage.startDate")} {" - "}
              </span>
              <span className="text-lg text-wrap text-teal-600">
                {moment(startDate).format("YYYY-MM-DD")}
              </span>
            </div>
            <div>
              <span className="text-lg">
                {t("ArchivePage.endDate")} {" - "}
              </span>
              <span className="text-lg text-wrap text-teal-600">
                {moment(endDate).format("YYYY-MM-DD")}
              </span>
            </div>
          </div>
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
