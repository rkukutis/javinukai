import { useQuery } from "@tanstack/react-query";
import getContests from "../services/contests/getContests";
import { useState } from "react";
import contestPhoto from "../assets/contest-photo.jpg";
import formatTimestap from "../utils/formatTimestap";
import calendarIcon from "../assets/icons/date_range_FILL0_wght400_GRAD0_opsz24.svg";
import Button from "../Components/Button";
import PaginationSettingsReusable from "../Components/user-management/PaginationSettingsReusable";
import useUserStore from "../stores/userStore";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

const defaultPagination = {
  page: 0,
  limit: 10,
  sortBy: "createdAt",
  sortDesc: "false",
  searchedField: null,
};

function ContestPage() {
  const { user } = useUserStore((state) => state);
  const navigate = useNavigate();
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

  function CategoryListing({ categoryInfo }) {
    return (
      <div className="py-1 bg-white mt-1 rounded-md px-2 text-slate-700">
        <p className="truncate">{categoryInfo.name}</p>
      </div>
    );
  }

  function ContestCard({ contestInfo }) {
    function handleClickParticipate() {
      if (!user) {
        toast.error("Only confirmed users may participate. Please log in");
        navigate("/login");
      }
      {
        if (confirm("Are you sure you want to participate in this contest?")) {
          toast.success(
            "Participation request sent to admin. You will receive an email on approval."
          );
        } else {
          return;
        }
      }
    }

    return (
      <div className="bg-white rounded-md xl:grid xl:grid-cols-12">
        <img
          className="h-[15rem] w-full object-cover shadow-md xl:col-span-2 lg:rounded-l-md rounded-t-md"
          src={contestPhoto}
        />
        <div className="text xl:col-span-8 p-6 space-y-3">
          <h1 className="text-2xl font-semibold text-teal-500 over">
            {contestInfo.name}
          </h1>
          <p className="text overflow-clip h-24 bg-gradient-to-b text-slate-700">
            {contestInfo.description}
          </p>
          <div className="flex flex-col xl:flex-row space-y-2 lg:space-y-0 items-center xl:space-x-2 w-">
            {user && (
              <Button
                onClick={() => handleClickParticipate()}
                extraStyle="w-full xl:w-fit"
              >
                Request to participate
              </Button>
            )}

            <Button extraStyle="w-full xl:w-fit">Contest Details</Button>
            <div className="flex items-center space-x-2">
              <img className="inline" src={calendarIcon} />
              <p className="text-slate-600 font-semibold">
                {formatTimestap(contestInfo.startDate)} -{" "}
                {formatTimestap(contestInfo.endDate)}
              </p>
            </div>
          </div>
        </div>
        <div className="text col-span-2 p-6 bg-teal-500 lg:rounded-r-md rounded-b-md">
          <h1 className="text-white text-xl font-semibold">Categories</h1>
          {contestInfo?.categories.map((category) => (
            <CategoryListing key={category.id} categoryInfo={category} />
          ))}
        </div>
      </div>
    );
  }

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

export default ContestPage;
