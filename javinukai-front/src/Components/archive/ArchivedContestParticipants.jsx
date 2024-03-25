import { useTranslation } from "react-i18next";

function ArchivedContestParticipants({ contest }) {
  const { t } = useTranslation();
  const { participants, winners } = contest;

  const isWinner = (participant) => {
    return participant.split(",").some((name) => winners.includes(name));
  };

  const participantsToDisplay = participants.map((participant, index) => {
    return (
      <div key={participant}>
        {`${index + 1}. ${participant.replace(/,/g, " ")}`}{" "}
        <span style={{ color: isWinner(participant) ? "red" : "black" }}>
          {isWinner(participant) &&
            t("ContestPastParticipants.winnerIndicator")}
        </span>
      </div>
    );
  });

  return (
    <div className="p-6 rounded w-[50vw] h-fit">
      <div className="p-1 text-lg">
        {t("ContestPastParticipants.pastParticipantsTitle")}
      </div>
      <div>{participantsToDisplay}</div>
    </div>
  );
}

export default ArchivedContestParticipants;
