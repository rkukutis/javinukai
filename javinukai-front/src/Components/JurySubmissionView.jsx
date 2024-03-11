import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import getCompetitionRecords from "../services/records/getCompetitionRecords";
import ContestEntryJury from "./contest/ContestEntryJury";
import SpinnerPage from "../pages/SpinnerPage";

function JurySubmissionView() {
  const { contestId, categoryId } = useParams();
  const { data: records, isFetching } = useQuery({
    queryKey: ["contestRecords", contestId, categoryId],
    queryFn: () => getCompetitionRecords(contestId, categoryId),
  });

  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-50">
          <div className="w-4/5 bg-white rounded my-12 min-h-[80vh] p-3">
            <h1 className="text-2xl text-teal-500 my-3">Contest Entries</h1>
            {records?.content.map((record) => (
              <div className="flex-col flex space-y-2" key={record.id}>
                {record.entries.map((entry, i) => (
                  <ContestEntryJury
                    index={i}
                    key={entry.id}
                    entry={entry}
                    categoryType={record.category.type}
                  />
                ))}
              </div>
            ))}
          </div>
        </div>
      )}
    </>
  );
}

export default JurySubmissionView;
