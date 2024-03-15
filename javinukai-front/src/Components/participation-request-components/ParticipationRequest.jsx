import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";

const ParticipationRequest = ({ request, onButtonClick }) => {
  
  const {
    requestId = request.requestId,
    // createdAt = request.createdAt,
    participantName = request.user.name,
    participantSurname = request.user.surname,
    participantEmail = request.user.email,
    participantBirthYear = request.user.birthYear,
    contestName = request.contest.name,
    requestStatus = request.requestStatus,
  } = request;

  const { t } = useTranslation();
  console.log("status", requestStatus);
  return (
    <>
      <div className="xl:w-3/4 w-full px-2"></div>
      <Link
        className="flex py-4 shadow bg-white xl:px-3 border-white border-4 transition ease-in-out hover:border-teal-400 hover:border-4 hover:cursor-pointer my-2 rounded-md"
      >
        <div className="xl:grid xl:grid-cols-10  xl:px-0 w-full flex justify-between">
  <p className="hidden xl:block text">{participantName}</p>{" "}
  <p className="hidden xl:block text">{participantSurname}</p>
  <p className="text col-span-3">{participantEmail}</p>
  <p className="hidden xl:block text">{participantBirthYear}</p>
  <p className="text col-span-2">{contestName}</p>
  <p className="text-center">
    <button
    type="button"
    disabled={requestStatus === "ACCEPTED" ? true : false}
    onClick={() => {
      onButtonClick(requestId, "ACCEPTED");
      toast.success(t("ParticipationRequest.fieldAcceptSuccess"));
    }}
    className={`${
      requestStatus === "ACCEPTED"
        ? "bg-green-500 cursor-not-allowed"
        : "bg-gray-300 hover:bg-green-300"
    } text-white py-2 px-3 rounded-md text-center transition ease-in-out`}
  >
    {t("ParticipationRequest.fieldAccept")}
  </button>
  </p>
  
  <p className="text-center">
    <button
    type="button"
    disabled={
      requestStatus === "ACCEPTED" || requestStatus === "DECLINED"
        ? true
        : false
    }
    onClick={() => {onButtonClick(requestId, "DECLINED");
    toast.success(t("ParticipationRequest.fieldDeclinedSuccess"));
    }}
    
    className={`${
      requestStatus === "PENDING"
        ? "bg-gray-300 hover:bg-red-300"
        : `${
            requestStatus === "ACCEPTED"
              ? "bg-gray-300 cursor-not-allowed"
              : "bg-red-500 cursor-not-allowed"
          }`
    } text-white py-2 px-3 rounded-md text-center transition ease-in-out`}
  >
    {t("ParticipationRequest.fieldDecline")}
  </button>
  </p>
  
</div>
      </Link>
    </>
  );
};
export default ParticipationRequest;
