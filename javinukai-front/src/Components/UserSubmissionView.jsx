import { useQuery } from "@tanstack/react-query";
import getCompetitionRecord from "../services/getCompetitionRecord";
import { useOutletContext, useParams } from "react-router-dom";
import formatTimestap from "../utils/formatTimestap";
import { useState } from "react";

function PhotoEntry({ entry, index }) {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className="border-2 p-4 rounded-md">
      <div
        className="flex space-x-5"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <h1>Entry #{index + 1}</h1>
        <h1>{entry.name}</h1>
        <p>{entry.description}</p>
        <p>Added: {formatTimestap(entry.createdAt)}</p>
        <p>Photos: {entry.images.length}</p>
      </div>
      {isExpanded && (
        <div className="flex flex-col xl:grid xl:grid-cols-3 w-full gap-6 my-4">
          {entry.images.map((image) => (
            <img
              className="rounded hover:scale-105"
              key={image.id}
              src={image.urlMiddle}
            />
          ))}
        </div>
      )}
    </div>
  );
}

function UserSubmissionView() {
  const { contestId, categoryId } = useOutletContext();
  const { data, isFetching } = useQuery({
    queryKey: ["contestRecord", contestId, categoryId],
    queryFn: () => getCompetitionRecord(contestId, categoryId),
  });

  console.log(contestId, categoryId);
  return (
    <div className="">
      {data?.entries.map((entry, i) => (
        <PhotoEntry key={entry.id} entry={entry} index={i} />
      ))}
    </div>
  );
}

export default UserSubmissionView;
