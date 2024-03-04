import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import getCompetitionRecords from "../services/records/getCompetitionRecords";
import ContestEntry from "./contest/ContestEntry";
import ContestEntryAnon from "./contest/ContestEntryAnon";

function CompetitionRecord({ record }) {
  return (
    <div className="xl:grid grid-cols-3">
      {record.entries.map((entry) => (
        <ContestEntryAnon key={entry.id} entry={entry} />
      ))}
    </div>
  );
}

function JurySubmissionView() {
  const { contestId, categoryId } = useParams();

  const { data: records, isFetching } = useQuery({
    queryKey: ["contestRecords", contestId, categoryId],
    queryFn: () => getCompetitionRecords(contestId, categoryId),
  });

  console.log(records);

  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-50">
      <div className="w-4/5 bg-white">
        {records?.content.map((record, i) => (
          <CompetitionRecord key={record.id} record={record} />
        ))}
      </div>
    </div>
  );
}

export default JurySubmissionView;
