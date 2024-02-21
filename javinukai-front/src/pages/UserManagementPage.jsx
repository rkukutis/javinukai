import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import getUsers from "../services/users/getUsers";
import PaginationSettings from "../Components/PaginationSettings";
import SpinnerPage from "./SpinnerPage";
import { Link } from "react-router-dom";

const defaultPagination = {
  page: 0,
  limit: 10,
  sortBy: "surname",
  sortDesc: "true",
  surnameContains: null,
};

function UserCard({ userInfo }) {
  return (
    <div className="flex py-4 border-b-2 bg-white lg:px-3 border-slate-200 my-2 rounded-md">
      <div className="lg:grid lg:grid-cols-5 px-3 lg:px-0 w-full flex justify-between">
        <div>
          <p>
            {userInfo.name} {userInfo.surname}
          </p>
          <p>{userInfo.email}</p>
        </div>
        <p className="hidden lg:flex items-center justify-left">
          {userInfo.isEnabled ? "yes" : "no"}
        </p>
        <p className="hidden lg:flex items-center justify-left">
          {userInfo.maxSinglePhotos}
        </p>
        <p className="hidden lg:flex items-center justify-left">
          {userInfo.maxCollections}
        </p>
        <div className="flex items-center justify-end space-x-5">
          <Link
            to={userInfo.uuid}
            className="bg-blue-400 hover:bg-blue-300 px-2 py-3 text-slate-50 rounded-md"
          >
            Details
          </Link>
        </div>
      </div>
    </div>
  );
}

export default function UserManagementPage() {
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const { data, isFetching, error } = useQuery({
    queryKey: [
      "users",
      paginationSettings.page,
      paginationSettings.limit,
      paginationSettings.sortBy,
      paginationSettings.sortDesc,
      paginationSettings.surnameContains,
    ],
    queryFn: () =>
      getUsers(
        paginationSettings.page,
        paginationSettings.limit,
        paginationSettings.sortBy,
        paginationSettings.sortDesc,
        paginationSettings.surnameContains
      ),
  });

  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-full min-h-[82vh] lg:flex lg:flex-col lg:items-center bg-slate-50">
          <div className="lg:w-3/4 w-full px-2">
            <PaginationSettings
              pagination={paginationSettings}
              setPagination={setPaginationSettings}
              availablePageNumber={data?.totalPages}
            />
            <div className="hidden lg:grid lg:grid-cols-5 px-3 py-5 font-bold text-lg text-slate-700 bg-white mt-2 rounded-md">
              <p>Name</p>
              <p>Confirmed</p>
              <p>Max singles</p>
              <p>Max collections</p>
            </div>
            {data?.content.map((user) => (
              <UserCard key={user.uuid} userInfo={user} />
            ))}
          </div>
        </div>
      )}
    </>
  );
}
