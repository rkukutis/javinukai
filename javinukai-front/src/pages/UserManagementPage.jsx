import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import getUsers from "../services/users/getUsers";
import PaginationSettings from "../Components/PaginationSettings";
import { UserListItem } from "../Components/user-management/UserListItem";
import { BarLoader } from "react-spinners";
import { useTranslation } from "react-i18next";
import ChangePage from "../Components/user-management/ChangePage";

const defaultPagination = {
  page: 0,
  limit: 10,
  sortBy: "surname",
  sortDesc: "false",
  searchedField: null,
};

export default function UserManagementPage() {
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const { data, isFetching } = useQuery({
    queryKey: [
      "users",
      paginationSettings.page,
      paginationSettings.limit,
      paginationSettings.sortBy,
      paginationSettings.sortDesc,
      paginationSettings.searchedField,
    ],
    queryFn: () =>
      getUsers(
        paginationSettings.page,
        paginationSettings.limit,
        paginationSettings.sortBy,
        paginationSettings.sortDesc,
        paginationSettings.searchedField
      ),
  });

  const { t } = useTranslation();

  return (
    <div className="w-full min-h-[82vh] xl:flex xl:flex-col xl:items-center bg-slate-50">
      <div className="xl:w-3/4 w-full px-2">
        <PaginationSettings
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={data?.totalPages}
          limitObjectName="users"
          sortFieldOptions={
            <>
              <option value="name">{t("PaginationSettings.fieldName")}</option>
              <option value="surname">
                {t("PaginationSettings.fieldSurname")}
              </option>
              <option value="email">
                {t("PaginationSettings.fieldEmail")}
              </option>
              <option value="role">{t("PaginationSettings.fieldRole")}</option>
              <option value="maxTotal">
                {t("PaginationSettings.fieldMaxTotalEntries")}
              </option>
              <option value="maxSinglePhotos">
                {t("PaginationSettings.fieldMaxSingles")}
              </option>
              <option value="maxCollections">
                {t("PaginationSettings.fieldMaxCollections")}
              </option>
              <option value="isEnabled">
                {t("PaginationSettings.fieldIsEnabled")}
              </option>
              <option value="isNonLocked">
                {t("PaginationSettings.fieldIsNonLocked")}
              </option>
            </>
          }
          searchByFieldName="surname"
          firstPage={data?.firs}
          lastPage={data?.last}
        />
        <div className="hidden xl:grid xl:grid-cols-10 px-3 py-5 font-bold text-lg text-slate-700 bg-white mt-2 rounded-md shadow">
          <p>{t("PaginationSettings.fieldName")}</p>
          <p>{t("PaginationSettings.fieldSurname")}</p>
          <p className="text col-span-3">
            {t("PaginationSettings.fieldEmail")}
          </p>
          <p className="text col-span-2">
            {t("PaginationSettings.fieldRole")}</p>
          <p>{t("PaginationSettings.fieldLimits")}</p>
          <p className="text-center break-all">
            {t("PaginationSettings.fieldIsEnabled")}
          </p>
          <p className="text-center break-all">
            {t("PaginationSettings.fieldIsNonLocked")}
          </p>
        </div>
        {isFetching ? (
          <div className="h-[50vh] flex flex-col justify-center items-center">
            <BarLoader />
          </div>
        ) : (
          <>
            {data?.content.map((user) => (
              <UserListItem key={user.uuid} userInfo={user} />
            ))}
          </>
        )}
      </div>
      <div>
        <ChangePage
        pagination={paginationSettings}
        setPagination={setPaginationSettings}
        availablePageNumber={data?.totalPages}
        />
      </div>
    </div>
  );
}
