import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import formatTimestap from "../../utils/formatTimestap.js";

const ParticipationRequest = ({ request, onButtonClick }) => {
  const {
    requestId = request.requestId,
    // createdAt = request.createdAt,
    participantName = request.user.name,
    participantSurname = request.user.surname,
    participantEmail = request.user.email,
    participantBirthYear = request.user.birthYear,
    participantPhoneNumber = request.user.phoneNumber,
    contestName = request.contest.name,
    contestStartDate = request.contest.startDate,
    contestEndDate = request.contest.endDate,
    requestStatus = request.requestStatus,
  } = request;

  const { t } = useTranslation();
  console.log("status", requestStatus);
  return (
    <>
      <div className="xl:w-3/4 w-full px-2"></div>
      <Link
        //   to={""}
        className="flex py-4 shadow bg-white xl:px-3 border-white border-4 transition ease-in-out hover:border-teal-400 hover:border-4 hover:cursor-pointer my-2 rounded-md"
      >
        <div className="xl:grid xl:grid-cols-10 px-3 xl:px-0 w-full flex justify-between">
          <div>
            {/* <p className="xl:block hidden">{formatTimestap(createdAt)}</p> */}
            <span className="">{participantName}</span>{" "}
            <span className="xl:hidden inline">{participantSurname}</span>
            <p className="xl:hidden block break-all">{participantEmail}</p>
          </div>
          <p className="xl:block hidden">{participantSurname}</p>
          <p className="xl:block hidden break-all text-wrap ">
            {participantEmail}
          </p>
          <p className="xl:block hidden">{participantBirthYear}</p>
          <p className="xl:block hidden">{participantPhoneNumber}</p>
          <p className="xl:block hidden">{contestName}</p>
          <span className="xl:hidden inline">
            <p>{t("ParticipationRequest.fieldContestName")}</p>
            {contestName}
          </span>
          <p className="xl:block hidden">{formatTimestap(contestStartDate)}</p>
          <p className="xl:block hidden">{formatTimestap(contestEndDate)}</p>
          <div>
            <button
              type="button"
              disabled={requestStatus === "ACCEPTED" ? true : false}
              onClick={() => onButtonClick(requestId, "ACCEPTED")}
              className={`${
                requestStatus === "ACCEPTED"
                  ? "bg-green-500 cursor-not-allowed"
                  : "bg-gray-300 hover:bg-green-300"
              } text-white py-2 px-4 rounded-md transition ease-in-out`}
            >
              {t("ParticipationRequest.fieldAccept")}
            </button>
          </div>

          <div>
            <button
              type="button"
              disabled={
                requestStatus === "ACCEPTED" || requestStatus === "DECLINED"
                  ? true
                  : false
              }
              onClick={() => onButtonClick(requestId, "DECLINED")}
              className={`${
                requestStatus === "PENDING"
                  ? "bg-gray-300 hover:bg-red-300"
                  : `${
                      requestStatus === "ACCEPTED"
                        ? "bg-gray-300 cursor-not-allowed"
                        : "bg-red-500 cursor-not-allowed"
                    }`
              } text-white py-2 px-4 rounded-md transition ease-in-out`}
            >
              {t("ParticipationRequest.fieldDecline")}
            </button>
          </div>
        </div>
      </Link>
    </>
  );
};
export default ParticipationRequest;
