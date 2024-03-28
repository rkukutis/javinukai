import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import getContests from "../services/contests/getContests";
import PaginationSettings from "../Components/PaginationSettings";
import ContestCard from "../Components/contest/ContestCard";
import { useTranslation } from "react-i18next";
import SpinnerPage from "./SpinnerPage";
import ChangePage from "../Components/user-management/ChangePage";

const defaultPagination = {
  page: 0,
  limit: 10,
  sortBy: "createdAt",
  sortDesc: "true",
  searchedField: null,
};

function ContestsPage() {
  const { t } = useTranslation();
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const { data, isFetching } = useQuery({
    queryKey: [
      "contests",
      paginationSettings.page,
      paginationSettings.limit,
      paginationSettings.sortBy,
      paginationSettings.sortDesc,
      paginationSettings.searchedField,
    ],
    queryFn: () =>
      getContests(
        paginationSettings.page,
        paginationSettings.limit,
        paginationSettings.sortBy,
        paginationSettings.sortDesc,
        paginationSettings.searchedField
      ),
  });

  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-3/4 pb-4">
          <PaginationSettings
            limitObjectName={t("ContestsPage.contestsLimitObject")}
            sortFieldOptions={
              <>
                <option value="name">
                  {t("ContestsPage.contestNameOption")}
                </option>
                <option value="description">
                  {t("ContestsPage.contestDescriptionOption")}
                </option>
                <option value="startDate">
                  {t("ContestsPage.contestStartDateOption")}
                </option>
                <option value="endDate">
                  {t("ContestsPage.contestEndDateOption")}
                </option>
                <option value="totalSubmissions">
                  {t("ContestsPage.contestTotalEntriesOption")}
                </option>
                <option value="createdAt">
                  {t("ContestsPage.contestCreatedDate")}</option>
              </>
            }
            lastPage={data?.last}
            firstPage={data?.first}
            searchByFieldName={t("ContestsPage.contestSeachFieldName")}
            pagination={paginationSettings}
            setPagination={setPaginationSettings}
            availablePageNumber={data?.totalPages}
          />
          <div className="space-y-4">
            {data?.content.map((contest) => (
              <ContestCard key={contest.id} contestInfo={contest} />
            ))}
          </div>
          <div>
              <ChangePage
                pagination={paginationSettings}
                setPagination={setPaginationSettings}
                availablePageNumber={data?.totalPages}
              />
            </div>
        </div>
      )}
    </>
  );
}

export default ContestsPage;
