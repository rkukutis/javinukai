function ParticipantCard({ handleClick, participant, isWinner }) {
  const { firstName, lastName, email } = participant;

  return (
    <div
      onClick={() => handleClick(email)}
      className="xl:grid xl:grid-cols-2 px-3 xl:px-0 w-full flex justify-between hover:cursor-pointer hover:bg-teal-100"
      style={{
        backgroundColor: isWinner(participant) ? "#99f6e4" : "transparent",
      }}
    >
      <div className="xl:grid xl:grid-cols-10 px-3 xl:px-0 w-full flex justify-between">
        <span>{firstName}</span>
        <span>{lastName}</span>
      </div>
      <div>
        <span>{email}</span>
      </div>
    </div>
  );
}

export default ParticipantCard;
