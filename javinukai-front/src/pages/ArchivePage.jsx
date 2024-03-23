import { useState } from "react";
import getPastCompetitionRecord from "../services/archive/getPastCompetitionRecord";
import PaginationSettings from "../Components/PaginationSettings";
import { useQuery } from "@tanstack/react-query";
import { useTranslation } from "react-i18next";
import ArchivedContestCard from "../Components/archive/ArchivedContestCard";

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
    <div className="w-full min-h-[82vh] xl:flex xl:flex-col xl:items-center bg-slate-50">
      <div className="xl:w-4/4 w-full px-2">
        <PaginationSettings
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={data?.totalPages}
          limitObjectName={t("UserManagementPage.userLimitObject")}
          sortFieldOptions={
            <>
              <option value="contestName">
                {t("ArchivePage.contestName")}
              </option>
              <option value="contestDescription">
                {t("ArchivePage.contestDescription")}
              </option>
            </>
          }
          searchByFieldName={t("ArchivePage.contestName")}
          firstPage={data?.first}
          lastPage={data?.last}
        />
        <div></div>
        {displayContests}
      </div>
    </div>
  );
}

export default ArchivePage;
