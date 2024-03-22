function ArchivedContestParticipants({ contest, onClose }) {
  const { participants } = contest;
  console.log(participants);

  console.log("contest in details ->  " + contest);

  const participantsToDisplay = participants.map((participant) => {
    return <div key={participant}>{participant}</div>;
  });

  return (
    <div>
      <div>{participantsToDisplay}</div>
    </div>
  );
}

export default ArchivedContestParticipants;
