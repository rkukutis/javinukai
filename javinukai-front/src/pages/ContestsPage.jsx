import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import getContests from "../services/contests/getContests";
import PaginationSettingsReusable from "../Components/user-management/PaginationSettingsReusable";
import ContestCard from "../Components/contest/ContestCard";

const defaultPagination = {
  page: 0,
  limit: 10,
  sortBy: "createdAt",
  sortDesc: "false",
  searchedField: null,
};

function ContestsPage() {
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
    <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-50">
      <div className="w-3/4 pb-4">
        <PaginationSettingsReusable
          limitObjectName="contests"
          sortFieldOptions={
            <>
              <option value="name">Name</option>
              <option value="description">Description</option>
              <option value="startDate">Start Date</option>
              <option value="endDate">End Date</option>
              <option value="totalSubmissions">Total Submissions</option>
            </>
          }
          lastPage={data?.last}
          firstPage={data?.first}
          searchByFieldName="name"
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={data?.totalPages}
        />
        <div className="space-y-4">
          {data?.content.map((contest) => (
            <ContestCard key={contest.id} contestInfo={contest} />
          ))}
        </div>
      </div>
    </div>
  );
}

export default ContestsPage;
