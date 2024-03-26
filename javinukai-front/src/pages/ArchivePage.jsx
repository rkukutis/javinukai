import { useState } from "react";
import getPastCompetitionRecord from "../services/archive/getPastCompetitionRecord";
import PaginationSettings from "../Components/PaginationSettings";
import { useQuery } from "@tanstack/react-query";
import { useTranslation } from "react-i18next";
import ArchivedContestCard from "../Components/archive/ArchivedContestCard";
import SpinnerPage from "./SpinnerPage";
import ChangePage from "../Components/user-management/ChangePage";

const defaultPagination = {
  page: 0,
  limit: 25,
  sortBy: "contestName",
  sortDesc: "false",
  searchedField: null,
};

function ArchivePage() {
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const { data, isFetching } = useQuery({
    queryKey: [
      "archiveRecords",
      paginationSettings.page,
      paginationSettings.limit,
      paginationSettings.sortBy,
      paginationSettings.sortDesc,
      paginationSettings.searchedField,
    ],
    queryFn: () =>
      getPastCompetitionRecord(
        paginationSettings.page,
        paginationSettings.limit,
        paginationSettings.sortBy,
        paginationSettings.sortDesc,
        paginationSettings.searchedField
      ),
  });
  const { t } = useTranslation();

  const displayContests = data?.content.map((data) => {
    return <ArchivedContestCard key={data.id} contest={data} />;
  });

  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-full min-h-[82vh] xl:flex xl:flex-col xl:items-center bg-slate-50">
          <div className="xl:w-3/4 w-full px-2">
            <PaginationSettings
              pagination={paginationSettings}
              setPagination={setPaginationSettings}
              availablePageNumber={data?.totalPages}
              limitObjectName={t("ArchivePage.contestsLimitObject")}
              sortFieldOptions={
                <>
                  <option value="contestName">
                    {t("ArchivePage.contestNameOption")}
                  </option>
                  <option value="contestDescription">
                    {t("ArchivePage.contestDescriptionOption")}
                  </option>
                  <option value="startDate">
                    {t("ArchivePage.contestStartDateOption")}
                  </option>
                  <option value="endDate">
                    {t("ArchivePage.contestEndDateOption")}
                  </option>
                </>
              }
              searchByFieldName={t("ArchivePage.contestNameSearch")}
              firstPage={data?.first}
              lastPage={data?.last}
            />
            <div>{displayContests}</div>
            <div>
              <ChangePage
                pagination={paginationSettings}
                setPagination={setPaginationSettings}
                availablePageNumber={data?.totalPages}
              />
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default ArchivePage;
