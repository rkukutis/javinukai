import { useQuery } from "@tanstack/react-query";
import { useLocation, useParams } from "react-router-dom";
import SpinnerPage from "../pages/SpinnerPage";
import getContestCategoryEntries from "../services/entries/getContestCategoryEntries";
import { useState } from "react";
import { JuryViewPagination } from "./JuryViewPagination";
import { SinglesSection } from "./SinglesSection";
import SeriesEntry from "./contest/SeriesEntry";
import { useTranslation } from "react-i18next";

function SeriesSection({ entries }) {
  
  return (
    <div>
      {entries.content.map((entry) => (
        <SeriesEntry
          key={entry.collection.id}
          entry={entry}
          entries={entries}
        />
      ))}
    </div>
  );
}

const defaultPagination = {
  limit: 25,
  page: 0,
  display: "all",
};

function JurySubmissionView() {
  const [pagination, setPagination] = useState(defaultPagination);
  const location = useLocation();
  const { contestId, categoryId } = useParams();

  const { data: entries, isFetching } = useQuery({
    queryKey: [
      "contestCategoryEntries",
      contestId,
      categoryId,
      pagination.display,
      pagination.limit,
      pagination.page,
    ],
    queryFn: () =>
      getContestCategoryEntries(
        contestId,
        categoryId,
        pagination.page,
        pagination.limit,
        pagination.display
      ),
  });

  console.log(entries);
  const { t } = useTranslation();
  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-4/5 bg-white rounded my-12 min-h-[80vh] p-3">
          <h1 className="text-2xl my-3">
            <b>{t("JurySubmissionView.contestTitle")} </b>
            {location?.state.contestInfo.name}
          </h1>
          <h1 className="text-xl my-3">
            <b>{t("JurySubmissionView.categoryTitle")} </b>
            {location?.state.categoryInfo.name}
          </h1>
          <JuryViewPagination
            pagination={pagination}
            setPagination={setPagination}
            availablePageNumber={entries?.totalPages}
            limitObjectName="entries"
            firstPage={entries?.first}
            lastPage={entries?.last}
          />
          {entries?.content.length == 0 ? (
            <p>{t("JurySubmissionView.noEntriesTitle")}</p>
          ) : (
            <>
              {location.state.categoryInfo.type == "SINGLE" ? (
                <SinglesSection entries={entries} />
              ) : (
                <SeriesSection entries={entries} />
              )}
            </>
          )}
        </div>
      )}
    </>
  );
}

export default JurySubmissionView;
