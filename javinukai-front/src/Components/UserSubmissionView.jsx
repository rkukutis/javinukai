import { useQuery } from "@tanstack/react-query";
import getCompetitionRecord from "../services/records/getCompetitionRecord";
import { useOutletContext } from "react-router-dom";
import ContestEntry from "./contest/ContestEntry";

function UserSubmissionView() {
  const { contestId, categoryId } = useOutletContext();
  const { data, isFetching } = useQuery({
    queryKey: ["contestRecord", contestId, categoryId],
    queryFn: () => getCompetitionRecord(contestId, categoryId),
  });

  return (
    <div className="">
      {data?.entries.map((entry, i) => (
        <ContestEntry key={entry.id} entry={entry} index={i} />
      ))}
    </div>
  );
}

export default UserSubmissionView;
