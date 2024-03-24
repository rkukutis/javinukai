function ParticipantCard({ handleClick, participant, isWinner }) {
  const { firstName, lastName, email } = participant;

  return (
    <div
      onClick={() => handleClick(email)}
      className="px-1 w-full flex hover:cursor-pointer hover:bg-teal-100"
      style={{
        backgroundColor: isWinner(participant) ? "#99f6e4" : "transparent",
      }}
    >
      <div className="grid grid-cols-2 w-full">
        <div className="col-span-1 text-teal-600">
          {firstName} {lastName}
        </div>
        <div className="col-span-1 text-teal-600">{email}</div>
      </div>
    </div>
  );
}

export default ParticipantCard;
