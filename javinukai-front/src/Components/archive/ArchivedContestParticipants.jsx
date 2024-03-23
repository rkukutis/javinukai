function ArchivedContestParticipants({ contest, close }) {
  const { participants, winners } = contest;

  console.log("contest in details ->  " + contest);

  const isWinner = (participant) => {
    return participant.split(",").some((name) => winners.includes(name));
  };

  const participantsToDisplay = participants.map((participant, index) => {
    return (
      <div key={participant}>
        {`${index + 1}. ${participant.replace(/,/g, " ")}`}{" "}
        <span style={{ color: isWinner(participant) ? "red" : "black" }}>
          {isWinner(participant) && "Konkurso laimetoja (-as)"}
        </span>
      </div>
    );
  });

  return (
    <div>
      <div>{participantsToDisplay}</div>
    </div>
  );
}

export default ArchivedContestParticipants;
